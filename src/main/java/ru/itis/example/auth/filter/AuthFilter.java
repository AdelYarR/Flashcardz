package ru.itis.example.auth.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.auth.repository.SessionRepository;
import ru.itis.example.auth.service.AuthService;
import ru.itis.example.logger.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@WebFilter("*")
public class AuthFilter implements Filter {

    private final Logger logger = new Logger(this.getClass().getName());
    private AuthService authService;

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/",
            "/registry",
            "/log",
            "/index.jsp",
            "/registry.jsp",
            "/log.jsp"
    );

    @Override
    public void init(FilterConfig filterConfig) {
        SessionRepository sessionRepository = (SessionRepository) filterConfig.getServletContext().getAttribute("session_repository");
        authService = new AuthService(sessionRepository);
        logger.info("Successful initialization.");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = getRequestPath(request);
        logger.info("Filtering the path: " + path);
        if (PUBLIC_PATHS.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = request.getCookies();
        String sessionId = getSessionIdFromCookies(cookies);

        boolean isAuthenticated = (sessionId != null && authService.isValid(sessionId));

        System.out.println(sessionId);
        if (!isAuthenticated) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getRequestPath(HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length());
    }

    private String getSessionIdFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(c -> "session_id".equals(c.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }
        return null;
    }
}
