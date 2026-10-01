package com.tucasaca.tienda.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tucasaca.tienda.model.Casaca;
import com.tucasaca.tienda.model.Equipo;
import com.tucasaca.tienda.model.Liga;
import com.tucasaca.tienda.model.Role;
import com.tucasaca.tienda.model.Sexo;
import com.tucasaca.tienda.model.Usuario;
import com.tucasaca.tienda.repository.CasacaRepository;
import com.tucasaca.tienda.repository.EquipoRepository;
import com.tucasaca.tienda.repository.LigaRepository;
import com.tucasaca.tienda.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedCasacas(
            CasacaRepository casacaRepository,
            EquipoRepository equipoRepository,
            LigaRepository ligaRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            Usuario admin = usuarioRepository.findByEmail("admin@tucasaca.com").orElseGet(() -> {
                return usuarioRepository.save(crearUsuario("admin", "Admin", "TuCasaca", "admin@tucasaca.com",
                        passwordEncoder.encode("admin123"), LocalDate.of(1990, 1, 1), Sexo.MASCULINO, Role.ADMIN, true));
            });

            if (usuarioRepository.findByEmail("ana.gomez@tucasaca.com").isEmpty()) {
                String passwordUsuario = passwordEncoder.encode("usuario123");
                List<Usuario> usuarios = List.of(
                        crearUsuario("anagomez", "Ana", "Gomez", "ana.gomez@tucasaca.com", passwordUsuario, LocalDate.of(1995, 6, 15), Sexo.FEMENINO, Role.USUARIO, true),
                        crearUsuario("juanperez", "Juan", "Perez", "juan.perez@tucasaca.com", passwordUsuario, LocalDate.of(1988, 3, 22), Sexo.MASCULINO, Role.USUARIO, true),
                        crearUsuario("sofilopez", "Sofia", "Lopez", "sofia.lopez@tucasaca.com", passwordUsuario, LocalDate.of(2001, 11, 2), Sexo.FEMENINO, Role.USUARIO, true),
                        crearUsuario("luisbaja", "Luis", "Baja", "luis.baja@tucasaca.com", passwordUsuario, LocalDate.of(1979, 8, 30), Sexo.NO_ESPECIFICA, Role.USUARIO, false));
                usuarioRepository.saveAll(usuarios);
            }

            if (casacaRepository.count() > 0) {
                return;
            }

            Liga ligaArg = ligaRepository.save(crearLiga("Liga Profesional", "Argentina"));
            Liga ligaEsp = ligaRepository.save(crearLiga("LaLiga", "España"));
            Liga ligaIng = ligaRepository.save(crearLiga("Premier League", "Inglaterra"));
            Liga ligaIta = ligaRepository.save(crearLiga("Serie A", "Italia"));

            Equipo sanLorenzo = equipoRepository.save(crearEquipo("San Lorenzo", ligaArg));
            Equipo boca = equipoRepository.save(crearEquipo("Boca Juniors", ligaArg));
            Equipo river = equipoRepository.save(crearEquipo("River Plate", ligaArg));
            Equipo racing = equipoRepository.save(crearEquipo("Racing Club", ligaArg));
            Equipo barcelona = equipoRepository.save(crearEquipo("FC Barcelona", ligaEsp));
            Equipo realMadrid = equipoRepository.save(crearEquipo("Real Madrid", ligaEsp));
            Equipo manUnited = equipoRepository.save(crearEquipo("Manchester United", ligaIng));
            Equipo liverpool = equipoRepository.save(crearEquipo("Liverpool", ligaIng));
            Equipo milan = equipoRepository.save(crearEquipo("AC Milan", ligaIta));
            Equipo juventus = equipoRepository.save(crearEquipo("Juventus", ligaIta));

            List<Casaca> camisetas = List.of(
                    crearCamiseta(sanLorenzo, ligaArg, 2014, "Ortigoza", 20, "S", 10, true, admin),
                    crearCamiseta(sanLorenzo, ligaArg, 2014, "Mercier", 5, "M", 10, true, admin),
                    crearCamiseta(sanLorenzo, ligaArg, 2014, "Romagnoli", 10, "XL", 3, true, admin),
                    crearCamiseta(boca, ligaArg, 2000, "Riquelme", 10, "L", 8, true, admin),
                    crearCamiseta(boca, ligaArg, 2023, "Cavani", 10, "M", 15, true, admin),
                    crearCamiseta(river, ligaArg, 2018, "Quintero", 10, "S", 6, true, admin),
                    crearCamiseta(river, ligaArg, 2015, "Pity Martinez", 8, "L", 0, true, admin),
                    crearCamiseta(racing, ligaArg, 2019, "Lisandro Lopez", 9, "XL", 12, true, admin),
                    crearCamiseta(barcelona, ligaEsp, 2011, "Messi", 10, "M", 20, true, admin),
                    crearCamiseta(barcelona, ligaEsp, 2009, "Xavi", 6, "S", 1, true, admin),
                    crearCamiseta(realMadrid, ligaEsp, 2017, "Cristiano Ronaldo", 7, "L", 9, true, admin),
                    crearCamiseta(realMadrid, ligaEsp, 2022, "Benzema", 9, "XL", 5, true, admin),
                    crearCamiseta(manUnited, ligaIng, 1999, "Beckham", 7, "M", 4, true, admin),
                    crearCamiseta(liverpool, ligaIng, 2019, "Salah", 11, "S", 18, true, admin),
                    crearCamiseta(milan, ligaIta, 2007, "Kaka", 22, "L", 7, true, admin),
                    crearCamiseta(juventus, ligaIta, 2005, "Del Piero", 10, "XL", 0, false, admin));

            casacaRepository.saveAll(camisetas);
        };
    }

    private Usuario crearUsuario(String nombreUsuario, String nombre, String apellido, String email, String passwordCodificada, LocalDate fechaNacimiento, Sexo sexo, Role rol, Boolean activo) {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setPassword(passwordCodificada);
        usuario.setFechaNacimiento(fechaNacimiento);
        usuario.setSexo(sexo);
        usuario.setRol(rol);
        usuario.setActivo(activo);
        return usuario;
    }

    private Liga crearLiga(String nombre, String pais) {
        Liga liga = new Liga();
        liga.setNombre(nombre);
        liga.setPais(pais);
        return liga;
    }

    private Equipo crearEquipo(String nombre, Liga liga) {
        Equipo equipo = new Equipo();
        equipo.setNombre(nombre);
        equipo.setLiga(liga);
        return equipo;
    }

    private Casaca crearCamiseta(Equipo equipo, Liga liga, Integer anio, String jugador, Integer numero, String talle, Integer stock, Boolean activo, Usuario creador) {
        Casaca casaca = new Casaca();
        casaca.setEquipo(equipo);
        casaca.setLiga(liga);
        casaca.setAnio(anio);
        casaca.setJugador(jugador);
        casaca.setNumero(numero);
        casaca.setTalle(talle);
        casaca.setPrecio(BigDecimal.valueOf(50 + (Math.random() * 100)));
        casaca.setImagenUrl("https://example.com/camisetas/" + equipo.getNombre().replace(" ", "-") + "-" + numero + ".png");
        casaca.setDescripcion("Camiseta oficial de " + equipo.getNombre() + " edición " + anio + " de " + jugador + " #" + numero);
        casaca.setCreador(creador);
        casaca.setActivo(activo);
        casaca.setStock(stock);
        return casaca;
    }
}
