package ru.itis.example.auth.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.example.auth.repository.SessionRepository;
import ru.itis.example.auth.service.SessionService;
import ru.itis.example.logger.Logger;
import ru.itis.example.util.CookieHelper;

import java.io.IOException;
import java.util.Set;

@WebFilter("*")
public class AuthFilter implements Filter {

    private final Logger logger = new Logger(this.getClass().getName());
    private SessionService sessionService;

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/",
            "/registry",
            "/log",
            "/index.jsp",
            "/registry.jsp",
            "/log.jsp",
            "/css/"
    );

    @Override
    public void init(FilterConfig filterConfig) {
        SessionRepository sessionRepository = (SessionRepository) filterConfig.getServletContext().getAttribute("session_repository");
        sessionService = new SessionService(sessionRepository);
        logger.info("Successful initialization.");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Cookie[] cookies = request.getCookies();
        String sessionId = CookieHelper.getValueFromCookies(cookies, "session_id");

        boolean isAuthenticated = (sessionId != null && sessionService.isValid(sessionId));

        String path = getRequestPath(request);
        logger.info("Filtering the path: " + path);
        if (PUBLIC_PATHS.contains(path) && !isAuthenticated) {
            filterChain.doFilter(request, response);
            return;
        } else if (PUBLIC_PATHS.contains(path) && isAuthenticated) {
            response.sendRedirect(request.getContextPath() + "/hub/groups");
            return;
        }

        if (!isAuthenticated) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getRequestPath(HttpServletRequest request) {
        return request.getRequestURI().substring(request.getContextPath().length());
    }
}
