package com.tucasaca.tienda.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tucasaca.tienda.model.Producto;
import com.tucasaca.tienda.repository.ProductoRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedProductos(ProductoRepository productoRepository) {
        return args -> {
            if (productoRepository.count() > 0) {
                return;
            }

            List<Producto> camisetas = List.of(
                    crearCamiseta("San Lorenzo", "Liga Profesional Argentina", 2014, "Ortigoza", 20),
                    crearCamiseta("San Lorenzo", "Liga Profesional Argentina", 2014, "Mercier", 5),
                    crearCamiseta("San Lorenzo", "Liga Profesional Argentina", 2014, "Romagnoli", 10));

            productoRepository.saveAll(camisetas);
        };
    }

    private Producto crearCamiseta(String equipo, String liga, Integer anio, String jugador, Integer numero) {
        Producto producto = new Producto();
        producto.setEquipo(equipo);
        producto.setLiga(liga);
        producto.setAnio(anio);
        producto.setJugador(jugador);
        producto.setNumero(numero);
        producto.setPrecio(BigDecimal.valueOf(50 + (Math.random() * 100)));
        producto.setImagenUrl("https://example.com/camisetas/" + equipo.replace(" ", "-") + "-" + numero + ".png");
        producto.setActivo(true);
        return producto;
    }
}