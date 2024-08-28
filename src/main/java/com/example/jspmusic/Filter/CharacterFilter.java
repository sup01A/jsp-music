package com.example.jspmusic.Filter;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class CharacterFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        // 设置请求的字符编码为UTF-8
        httpRequest.setCharacterEncoding("UTF-8");

        // 设置响应的字符编码为UTF-8
        httpResponse.setCharacterEncoding("UTF-8");

        filterChain.doFilter(httpRequest,httpResponse);
    }
}
