package compasso.test.springboot.catalogoprodutos.config;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SwaggerFilter implements Filter {

	private static final String DOCUMENTATION_URL = "/swagger-ui.html";

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		if (request.getRequestURI().equals("") || request.getRequestURI().equals("/")) {
			response.sendRedirect(DOCUMENTATION_URL);
			return;
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}
}
