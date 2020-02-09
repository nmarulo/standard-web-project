package red.softn.standard.jsf.filter;

import org.apache.commons.lang3.StringUtils;
import red.softn.standard.common.DefaultUtils;
import red.softn.standard.jsf.common.FacesUtils;

import javax.faces.application.ResourceHandler;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public abstract class ABasicFilter {
    
    protected void doFilter(ServletRequest requestParam, ServletResponse responseParam, FilterChain chain) throws Exception {
        HttpServletRequest  request  = (HttpServletRequest) requestParam;
        HttpServletResponse response = (HttpServletResponse) responseParam;
        
        if (checkRequestURI(request) || checkSession(request)) {
            chain.doFilter(request, response);
        } else if (request.isRequestedSessionIdValid()) {
            redirect(request, response, DefaultUtils.removeSlash(getUriIndex()));
        } else {
            redirect(request, response, DefaultUtils.removeSlash(getUriError500()));
        }
    }
    
    protected void redirect(HttpServletRequest request, HttpServletResponse response, String page) throws IOException {
        response.sendRedirect(String.format("%1$s/%2$s.xhtml", request.getContextPath(), StringUtils.removeEnd(page, ".xhtml")));
    }
    
    protected boolean checkRequestURI(HttpServletRequest request) {
        if (FacesUtils.checkURI(request, ResourceHandler.RESOURCE_IDENTIFIER)) {
            return true;
        }
        
        String dashboard = StringUtils.join("/", DefaultUtils.removeSlash(getPathDashboard()), "/");
        String error500  = "/" + DefaultUtils.removeSlash(getUriError500());
        
        //Comprueba si es la pagina de inicio, fuera del panel de administración.
        return !FacesUtils.checkURI(request, dashboard) || FacesUtils.checkURI(request, error500);
    }
    
    protected boolean checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        
        return session != null && request.getRequestedSessionId() != null && request.isRequestedSessionIdValid() && session.getAttribute(getSessionUserName()) != null;
    }
    
    protected abstract String getSessionUserName();
    
    protected abstract String getPathDashboard();
    
    protected abstract String getUriError500();
    
    protected final String getUriIndex() {
        return "index";
    }
}
