package cn.lz.data.bootstrap.config.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @Desc
 * @Author zhanglei
 * @Date 2024/1/4 17:11
 */
public class UrlPatternFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
