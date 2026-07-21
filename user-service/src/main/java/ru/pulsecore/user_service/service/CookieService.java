package ru.pulsecore.user_service.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import ru.pulsecore.user_service.config.JwtCookieProperties;


@Service
@RequiredArgsConstructor
public class CookieService {

    private final JwtCookieProperties props;

    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from(props.getName(), refreshToken)
                .httpOnly(props.isHttpOnly())
                .secure(props.isSecure())
                .path(props.getPath())
                .maxAge(props.getMaxAge())
//                .sameSite("None")  // поменяй на None
//                .domain(props.getDomain())
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(props.getName(), "")
                .httpOnly(props.isHttpOnly())
                .path(props.getPath())
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}