package com.smartcontrol.security;

import com.smartcontrol.model.User;
import com.smartcontrol.repository.UserRepository;
import com.smartcontrol.service.TokenBlacklistService;
import com.smartcontrol.service.UserService;
import com.smartcontrol.util.TokenUtils;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenUtils tokenUtils;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); 

            try {
                
                if (request.getRequestURI().equals("/api/auth/refresh")) {
                    tokenUtils.validateToken(token);
                    filterChain.doFilter(request, response);
                    return; 
                }

                
                if (tokenBlacklistService.isBlacklisted(token)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json; charset=UTF-8");
                    response.getWriter().write("{\"message\": \"블랙리스트 토큰입니다.\"}");
                    return;
                }

                
                Claims claims = tokenUtils.validateToken(token);
                String email = claims.getSubject();

                
                User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

                
                System.out.println("인증된 사용자: " + user.getEmail());
                System.out.println("사용자 역할: " + user.getRole());

                
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                
                System.err.println("JWT 인증 실패: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write("{\"message\": \"유효하지 않은 토큰입니다.\"}");
                return;
            }
        }

        
        filterChain.doFilter(request, response);
    }
}
