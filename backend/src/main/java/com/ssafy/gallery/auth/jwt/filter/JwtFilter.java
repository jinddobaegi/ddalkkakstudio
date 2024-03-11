package com.ssafy.gallery.auth.jwt.filter;

import com.ssafy.gallery.auth.jwt.exception.JwtExceptionEnum;
import com.ssafy.gallery.auth.redis.dto.LoginTokenDto;
import com.ssafy.gallery.auth.redis.repository.LoginTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final LoginTokenRepository loginTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorization: {}", authorization);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.error("authorization이 없습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];
        log.info("Jwt Token: {}", loginTokenRepository.findById(token));

        if (loginTokenRepository.findById(token).isPresent()) {
            LoginTokenDto loginTokenDto = loginTokenRepository.findById(token).get();
            log.info("로그인한 유저의 정보: {}", loginTokenDto);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginTokenDto.getUserId(), null, List.of(new SimpleGrantedAuthority("USER")));

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            if (loginTokenDto.getTokenType().equals("refresh")) {
                // 토큰을 새로 만들어서 저장해야함
            }
        } else {
            log.error("[Exception] message: {}", JwtExceptionEnum.TOKEN_NOT_EXIST.getMessage());
            request.setAttribute("exception", JwtExceptionEnum.TOKEN_NOT_EXIST.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}