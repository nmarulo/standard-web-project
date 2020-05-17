package red.softn.standard.jsf.filter;

import red.softn.standard.jsf.bean.SessionBean;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = {
    "*.xhtml",
    "*.html"
})
public class AuthFilter extends ABasicFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    
    }
    
    @Override
    public void doFilter(ServletRequest requestParam, ServletResponse responseParam, FilterChain chain) throws IOException, ServletException {
        try {
            super.doFilter(requestParam, responseParam, chain);
        } catch (Exception e) {
            //TODO: log
            e.printStackTrace();
        }
    }
    
    @Override
    public void destroy() {
    }
    
    @Override
    protected String getSessionUserName() {
        return SessionBean.USER_NAME;
    }
    
    @Override
    protected String getPathDashboard() {
        return "/dashboard/";
    }
    
    @Override
    protected String getUriError500() {
        return "/500.xhtml";
    }
}
