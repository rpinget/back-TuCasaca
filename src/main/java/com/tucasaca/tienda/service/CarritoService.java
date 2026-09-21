package com.tucasaca.tienda.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tucasaca.tienda.dto.AgregarItemDTO;
import com.tucasaca.tienda.dto.CarritoDTO;
import com.tucasaca.tienda.dto.ItemCarritoDTO;
import com.tucasaca.tienda.exception.CarritoVacioException;
import com.tucasaca.tienda.exception.OperacionNoPermitidaException;
import com.tucasaca.tienda.exception.ResourceNotFoundException;
import com.tucasaca.tienda.exception.StockInsuficienteException;
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

    public void validarAccesoUsuario(Long usuarioId, String authenticatedEmail, boolean isAdmin) {
        if (authenticatedEmail == null || isAdmin) {
            return;
        }
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", usuarioId));
        if (!usuario.getEmail().equalsIgnoreCase(authenticatedEmail)) {
            throw new OperacionNoPermitidaException("No tienes permiso para acceder al carrito de otro usuario");
        }
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
                .orElseThrow(() -> new ResourceNotFoundException("Casaca", dto.getCasacaId()));

        if (!casaca.getActivo()) {
            throw new StockInsuficienteException("La casaca no está disponible");
        }
        if (casaca.getStock() < dto.getCantidad()) {
            throw new StockInsuficienteException("Stock insuficiente. Disponible: " + casaca.getStock());
        }

        Carrito carrito = carritoRepository
                .findByUsuarioIdAndEstado(usuarioId, EstadoCarrito.ACTIVO)
                .orElseGet(() -> crearCarritoNuevo(usuarioId));

        // Si ya existe el ítem, sumar cantidad
        itemCarritoRepository.findByCarritoIdAndCasacaId(carrito.getId(), casaca.getId())
                .ifPresentOrElse(item -> {
                    int nuevaCantidad = item.getCantidad() + dto.getCantidad();
                    if (casaca.getStock() < nuevaCantidad) {
                        throw new StockInsuficienteException(
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
                .orElseThrow(() -> new ResourceNotFoundException("No hay carrito activo para el usuario con id: " + usuarioId));

        ItemCarrito item = itemCarritoRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("ItemCarrito", itemId));

        carrito.getItems().remove(item);
        itemCarritoRepository.delete(item);
        return toDTO(carritoRepository.save(carrito));
    }

    // Vaciar el carrito (eliminar todos los ítems)
    @Transactional
    public CarritoDTO vaciarCarrito(Long usuarioId) {
        Carrito carrito = carritoRepository
                .findByUsuarioIdAndEstado(usuarioId, EstadoCarrito.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("No hay carrito activo para el usuario con id: " + usuarioId));

        carrito.getItems().clear();
        return toDTO(carritoRepository.save(carrito));
    }

    // Checkout: cerrar carrito y descontar stock
    @Transactional
    public CarritoDTO checkout(Long usuarioId) {
        Carrito carrito = carritoRepository
                .findByUsuarioIdAndEstado(usuarioId, EstadoCarrito.ACTIVO)
                .orElseThrow(() -> new ResourceNotFoundException("No hay carrito activo para el usuario con id: " + usuarioId));

        if (carrito.getItems().isEmpty()) {
            throw new CarritoVacioException("El carrito está vacío");
        }

        // Validar stock y descontar
        for (ItemCarrito item : carrito.getItems()) {
            Casaca casaca = item.getCasaca();
            if (casaca.getStock() < item.getCantidad()) {
                throw new StockInsuficienteException("Stock insuficiente para: " + casaca.getJugador()
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
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", usuarioId));
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
