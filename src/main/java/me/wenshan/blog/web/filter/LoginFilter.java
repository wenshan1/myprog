package me.wenshan.blog.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import me.wenshan.util.Global;

@Configuration
@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
public class LoginFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		request.setAttribute("g", Global.getInstance(request));
		String str = SecurityContextHolder.getContext().getAuthentication().getName();
		request.setAttribute("authUser", str);
		filterChain.doFilter(request, response);
	}
}
