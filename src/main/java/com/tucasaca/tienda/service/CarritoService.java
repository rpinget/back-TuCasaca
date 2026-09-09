package com.tucasaca.tienda.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tucasaca.tienda.dto.AgregarItemDTO;
import com.tucasaca.tienda.dto.CarritoDTO;
import com.tucasaca.tienda.dto.ItemCarritoDTO;
import com.tucasaca.tienda.mapper.CasacaMapper;
import com.tucasaca.tienda.model.Carrito;
import com.tucasaca.tienda.model.Carrito.EstadoCarrito;
import com.tucasaca.tienda.model.Casaca;
import com.tucasaca.tienda.model.ItemCarrito;
import com.tucasaca.tienda.model.Usuario;
import com.tucasaca.tienda.repository.CarritoRepository;
import com.tucasaca.tienda.repository.CasacaRepository;
import com.tucasaca.tienda.repository.ItemCarritoRepository;
import com.tucasaca.tienda.repository.UsuarioRepository;

import jakarta.persistence.EntityManager;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final CasacaRepository casacaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CasacaMapper casacaMapper;
    private final EntityManager entityManager;

    public CarritoService(
            CarritoRepository carritoRepository,
            ItemCarritoRepository itemCarritoRepository,
            CasacaRepository casacaRepository,
            UsuarioRepository usuarioRepository,
            CasacaMapper casacaMapper,
            EntityManager entityManager) {
        this.carritoRepository = carritoRepository;
        this.itemCarritoRepository = itemCarritoRepository;
        this.casacaRepository = casacaRepository;
        this.usuarioRepository = usuarioRepository;
        this.casacaMapper = casacaMapper;
        this.entityManager = entityManager;
    }

    // Obtener o crear el carrito activo del usuario
    @Transactional
    public CarritoDTO obtenerCarrito(Long usuarioId) {
        Carrito carrito = carritoRepository
                .findByUsuarioIdAndEstado(usuarioId, EstadoCarrito.ACTIVO)
                .orElseGet(() -> crearCarritoNuevo(usuarioId));
        return toDTO(carrito);
    }

    // Agregar ítem al carrito, validando stock
    @Transactional
    public CarritoDTO agregarItem(Long usuarioId, AgregarItemDTO dto) {
        Casaca casaca = casacaRepository.findById(dto.getCasacaId())
                .orElseThrow(() -> new RuntimeException("Casaca no encontrada: " + dto.getCasacaId()));

        if (!casaca.getActivo()) {
            throw new RuntimeException("La casaca no está disponible");
        }
        if (casaca.getStock() < dto.getCantidad()) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + casaca.getStock());
        }

        Carrito carrito = carritoRepository
                .findByUsuarioIdAndEstado(usuarioId, EstadoCarrito.ACTIVO)
                .orElseGet(() -> crearCarritoNuevo(usuarioId));

        // Si ya existe el ítem, sumar cantidad
        itemCarritoRepository.findByCarritoIdAndCasacaId(carrito.getId(), casaca.getId())
                .ifPresentOrElse(item -> {
                    int nuevaCantidad = item.getCantidad() + dto.getCantidad();
                    if (casaca.getStock() < nuevaCantidad) {
                        throw new RuntimeException(
                                "Stock insuficiente para la cantidad total. Disponible: " + casaca.getStock());
                    }
                    item.setCantidad(nuevaCantidad);
                    itemCarritoRepository.save(item);
                }, () -> {
                    ItemCarrito nuevoItem = new ItemCarrito();
                    nuevoItem.setCarrito(carrito);
                    nuevoItem.setCasaca(casaca);
                    nuevoItem.setCantidad(dto.getCantidad());
                    nuevoItem.setPrecioUnitario(casaca.getPrecio());
                    itemCarritoRepository.save(nuevoItem);
                    carrito.getItems().add(nuevoItem);
                });

        carritoRepository.save(carrito);
        entityManager.flush();
        entityManager.clear();
        Carrito actualizado = carritoRepository.findById(carrito.getId()).orElseThrow();
        return toDTO(actualizado);
    }

    // Eliminar un ítem del carrito
    @Transactional
    public CarritoDTO eliminarItem(Long usuarioId, Long itemId) {
        Carrito carrito = carritoRepository
                .findByUsuarioIdAndEstado(usuarioId, EstadoCarrito.ACTIVO)
                .orElseThrow(() -> new RuntimeException("No hay carrito activo para el usuario: " + usuarioId));

        ItemCarrito item = itemCarritoRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Ítem no encontrado: " + itemId));

        carrito.getItems().remove(item);
        itemCarritoRepository.delete(item);
        return toDTO(carritoRepository.save(carrito));
    }

    // Vaciar el carrito (eliminar todos los ítems)
    @Transactional
    public CarritoDTO vaciarCarrito(Long usuarioId) {
        Carrito carrito = carritoRepository
                .findByUsuarioIdAndEstado(usuarioId, EstadoCarrito.ACTIVO)
                .orElseThrow(() -> new RuntimeException("No hay carrito activo para el usuario: " + usuarioId));

        carrito.getItems().clear();
        return toDTO(carritoRepository.save(carrito));
    }

    // Checkout: cerrar carrito y descontar stock
    @Transactional
    public CarritoDTO checkout(Long usuarioId) {
        Carrito carrito = carritoRepository
                .findByUsuarioIdAndEstado(usuarioId, EstadoCarrito.ACTIVO)
                .orElseThrow(() -> new RuntimeException("No hay carrito activo para el usuario: " + usuarioId));

        if (carrito.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        // Validar stock y descontar
        for (ItemCarrito item : carrito.getItems()) {
            Casaca casaca = item.getCasaca();
            if (casaca.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + casaca.getJugador()
                        + " #" + casaca.getNumero() + ". Disponible: " + casaca.getStock());
            }
            casaca.setStock(casaca.getStock() - item.getCantidad());
            casacaRepository.save(casaca);
        }

        carrito.setEstado(EstadoCarrito.CERRADO);
        return toDTO(carritoRepository.save(carrito));
    }

    // --- Helpers ---

    private Carrito crearCarritoNuevo(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + usuarioId));
        Carrito nuevo = new Carrito();
        nuevo.setUsuario(usuario);
        nuevo.setEstado(EstadoCarrito.ACTIVO);
        return carritoRepository.save(nuevo);
    }

    private CarritoDTO toDTO(Carrito carrito) {
        List<ItemCarritoDTO> itemsDTO = carrito.getItems().stream()
                .map(item -> {
                    BigDecimal subtotal = item.getPrecioUnitario()
                            .multiply(BigDecimal.valueOf(item.getCantidad()));
                    return new ItemCarritoDTO(
                            item.getId(),
                            casacaMapper.toDTO(item.getCasaca()),
                            item.getCantidad(),
                            item.getPrecioUnitario(),
                            subtotal);
                }).toList();

        BigDecimal total = itemsDTO.stream()
                .map(ItemCarritoDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CarritoDTO(
                carrito.getId(),
                carrito.getUsuario().getId(),
                carrito.getEstado().name(),
                itemsDTO,
                total,
                carrito.getFechaCreacion());
    }
}
