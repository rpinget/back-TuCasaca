package com.tucasaca.tienda.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tucasaca.tienda.model.Casaca;
import com.tucasaca.tienda.model.Equipo;
import com.tucasaca.tienda.model.Liga;
import com.tucasaca.tienda.repository.CasacaRepository;
import com.tucasaca.tienda.repository.EquipoRepository;
import com.tucasaca.tienda.repository.LigaRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedCasacas(
            CasacaRepository casacaRepository,
            EquipoRepository equipoRepository,
            LigaRepository ligaRepository) {
        return args -> {
            if (casacaRepository.count() > 0) {
                return;
            }

            Liga ligaArg = new Liga();
            ligaArg.setNombre("Liga Profesional");
            ligaArg.setPais("Argentina");
            ligaRepository.save(ligaArg);

            Equipo sanLorenzo = new Equipo();
            sanLorenzo.setNombre("San Lorenzo");
            sanLorenzo.setLiga(ligaArg);
            equipoRepository.save(sanLorenzo);

            List<Casaca> camisetas = List.of(
                    crearCamiseta(sanLorenzo, ligaArg, 2014, "Ortigoza", 20, "S"),
                    crearCamiseta(sanLorenzo, ligaArg, 2014, "Mercier", 5, "M"),
                    crearCamiseta(sanLorenzo, ligaArg, 2014, "Romagnoli", 10, "XL"));

            casacaRepository.saveAll(camisetas);
        };
    }

    private Casaca crearCamiseta(Equipo equipo, Liga liga, Integer anio, String jugador, Integer numero, String talle) {
        Casaca casaca = new Casaca();
        casaca.setEquipo(equipo);
        casaca.setLiga(liga);
        casaca.setAnio(anio);
        casaca.setJugador(jugador);
        casaca.setNumero(numero);
        casaca.setTalle(talle);
        casaca.setPrecio(BigDecimal.valueOf(50 + (Math.random() * 100)));
        casaca.setImagenUrl("https://example.com/camisetas/" + equipo.getNombre().replace(" ", "-") + "-" + numero + ".png");
        casaca.setActivo(true);
        return casaca;
    }
}