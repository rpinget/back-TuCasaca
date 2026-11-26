package com.tucasaca.tienda.security;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

/**
 * Construye la cookie que transporta el JWT.
 * La cookie es HttpOnly: JavaScript no puede leerla (mitiga el robo del token por XSS).
 */
@Component
public class AuthCookieService {

    public static final String COOKIE_NAME = "access_token";

    // true en producción (HTTPS). En desarrollo sobre http puede desactivarse con jwt.cookie.secure=false
    @Value("${jwt.cookie.secure:true}")
    private boolean secure;

    // Lax: el navegador no envía la cookie en POST/PUT/DELETE iniciados desde otros sitios (mitiga CSRF)
    @Value("${jwt.cookie.same-site:Lax}")
    private String sameSite;

    // Misma duración que la expiración del JWT (en milisegundos)
    @Value("${jwt.expiration}")
    private long expirationMs;

    public ResponseCookie crear(String token) {
        return base(token)
                .maxAge(Duration.ofMillis(expirationMs))
                .build();
    }

    /** Cookie vacía y expirada: el navegador la elimina (logout). */
    public ResponseCookie borrar() {
        return base("")
                .maxAge(Duration.ZERO)
                .build();
    }

    private ResponseCookie.ResponseCookieBuilder base(String value) {
        return ResponseCookie.from(COOKIE_NAME, value)
                .httpOnly(true)      // JS no puede leerla -> XSS
                .secure(secure)      // solo viaja por HTTPS
                .sameSite(sameSite)  // -> CSRF
                .path("/");          // disponible para todos los endpoints
    }
}
