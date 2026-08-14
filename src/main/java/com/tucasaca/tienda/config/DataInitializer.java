package com.tucasaca.tienda.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tucasaca.tienda.model.Casaca;
import com.tucasaca.tienda.repository.CasacaRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedCasacas(CasacaRepository casacaRepository) {
        return args -> {
            if (casacaRepository.count() > 0) {
                return;
            }

            List<Casaca> camisetas = List.of(
                    crearCamiseta("San Lorenzo", "Argentina", 2014, "Ortigoza", 20),
                    crearCamiseta("San Lorenzo", "Argentina", 2014, "Mercier", 5),
                    crearCamiseta("San Lorenzo", "Argentina", 2014, "Romagnoli", 10));

            casacaRepository.saveAll(camisetas);
        };
    }

    private Casaca crearCamiseta(String equipo, String liga, Integer anio, String jugador, Integer numero) {
        Casaca casaca = new Casaca();
        casaca.setEquipo(equipo);
        casaca.setLiga(liga);
        casaca.setAnio(anio);
        casaca.setJugador(jugador);
        casaca.setNumero(numero);
        casaca.setPrecio(BigDecimal.valueOf(50 + (Math.random() * 100)));
        casaca.setImagenUrl("https://example.com/camisetas/" + equipo.replace(" ", "-") + "-" + numero + ".png");
        casaca.setActivo(true);
        return casaca;
    }
}