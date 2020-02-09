package red.softn.standard.jsf.common;

import org.apache.commons.lang3.StringUtils;
import red.softn.standard.common.DefaultUtils;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * JSF utilities.
 *
 * @author ICEsoft Technologies Inc.
 * @since 2.0
 */
public class FacesUtils {
    
    private static Properties buildProperties = null;
    
    public static boolean redirectTo(String pageName) {
        pageName = StringUtils.removeEnd(DefaultUtils.removeSlash(pageName), ".xhtml");
        
        return FacesUtils.redirect(String.format("%1$s/%2$s.xhtml", FacesUtils.getExternalContext()
                                                                             .getRequestContextPath(), pageName));
    }
    
    public static boolean redirect(String url) {
        try {
            FacesUtils.getExternalContext()
                      .redirect(url + "?faces-redirect=true");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesUtils.getExternalContext()
                                              .getRequest();
    }
    
    public static boolean checkURI(String value) {
        return FacesUtils.checkURI(FacesUtils.getRequest(), value);
    }
    
    public static boolean checkURI(HttpServletRequest request, String value) {
        return request.getRequestURI()
                      .contains(request.getContextPath()
                                       .concat(value));
    }
    
    public static boolean redirectPram(String url, String param, String value) {
        try {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append(url);
            strBuilder.append("?");
            strBuilder.append(param);
            strBuilder.append("=");
            strBuilder.append(value);
            strBuilder.append("&faces-redirect=true");
            
            FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect(strBuilder.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get servlet context.
     *
     * @return the servlet context
     */
    public static ServletContext getServletContext() {
        return (ServletContext) FacesUtils.getExternalContext()
                                          .getContext();
    }
    
    public static ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance()
                           .getExternalContext();
    }
    
    public static HttpSession getHttpSession(boolean create) {
        return (HttpSession) FacesUtils.getExternalContext()
                                       .getSession(create);
    }
    
    public static int getMaxInactiveInterval() {
        return FacesUtils.getHttpSession(false)
                         .getMaxInactiveInterval();
    }
    
    /**
     * Get managed bean based on the bean name.
     *
     * @param beanName the bean name
     *
     * @return the managed bean associated with the bean name
     */
    public static Object getManagedBean(String beanName) {
        FacesContext fc  = FacesContext.getCurrentInstance();
        ELContext    elc = fc.getELContext();
        ExpressionFactory ef = fc.getApplication()
                                 .getExpressionFactory();
        ValueExpression ve = ef.createValueExpression(elc, getJsfEl(beanName), Object.class);
        return ve.getValue(elc);
    }
    
    /**
     * Remove the managed bean based on the bean name.
     *
     * @param beanName the bean name of the managed bean to be removed
     */
    public static void resetManagedBean(String beanName) {
        FacesContext fc  = FacesContext.getCurrentInstance();
        ELContext    elc = fc.getELContext();
        ExpressionFactory ef = fc.getApplication()
                                 .getExpressionFactory();
        ef.createValueExpression(elc, getJsfEl(beanName), Object.class)
          .setValue(elc, null);
    }
    
    /**
     * Store the managed bean inside the session scope.
     *
     * @param beanName    the name of the managed bean to be stored
     * @param managedBean the managed bean to be stored
     */
    public static void setManagedBeanInSession(String beanName, Object managedBean) {
        FacesUtils.getExternalContext()
                  .getSessionMap()
                  .put(beanName, managedBean);
    }
    
    /**
     * Store the managed bean inside the request scope.
     *
     * @param beanName    the name of the managed bean to be stored
     * @param managedBean the managed bean to be stored
     */
    public static void setManagedBeanInRequest(String beanName, Object managedBean) {
        FacesUtils.getExternalContext()
                  .getRequestMap()
                  .put(beanName, managedBean);
    }
    
    /**
     * Get parameter value from request scope.
     *
     * @param name the name of the parameter
     *
     * @return the parameter value
     */
    public static String getRequestParameter(String name) {
        return FacesUtils.getExternalContext()
                         .getRequestParameterMap()
                         .get(name);
    }
    
    /**
     * Get named request map object value from request map.
     *
     * @param name the name of the key in map
     *
     * @return the key value if any, null otherwise.
     */
    public static Object getRequestMapValue(String name) {
        return FacesUtils.getExternalContext()
                         .getRequestMap()
                         .get(name);
    }
    
    /**
     * Gets the action attribute value from the specified event for the given
     * name.  Action attributes are specified by &lt;f:attribute /&gt;.
     *
     * @param event
     * @param name
     *
     * @return
     */
    public static String getActionAttribute(ActionEvent event, String name) {
        return (String) event.getComponent()
                             .getAttributes()
                             .get(name);
    }
    
    public static String getBuildAttribute(String name) {
        if (buildProperties != null) {
            return buildProperties.getProperty(name, "unknown");
        }
        InputStream is = null;
        try {
            is = getServletContext().getResourceAsStream("/WEB-INF/buildversion.properties");
            buildProperties = new Properties();
            buildProperties.load(is);
        } catch (Throwable e) {
            is = null;
            buildProperties = null;
            return "unknown";
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Throwable ignored) {
                }
            }
        }
        return buildProperties.getProperty(name, "unknown");
    }
    
    /**
     * Gest parameter value from the the session scope.
     *
     * @param name name of the parameter
     *
     * @return the parameter value if any.
     */
    public static String getSessionParameter(String name) {
        HttpServletRequest myRequest = (HttpServletRequest) FacesUtils.getExternalContext()
                                                                      .getRequest();
        HttpSession mySession = myRequest.getSession();
        return myRequest.getParameter(name);
    }
    
    /**
     * Get parameter value from the web.xml file
     *
     * @param parameter name to look up
     *
     * @return the value of the parameter
     */
    public static String getFacesParameter(String parameter) {
        // Get the servlet context based on the faces context
        ServletContext sc = (ServletContext) FacesUtils.getExternalContext()
                                                       .getContext();
        
        // Return the value read from the parameter
        return sc.getInitParameter(parameter);
    }
    
    /**
     * Add information message.
     *
     * @param msg the information message
     */
    public static void addInfoMessage(String msg) {
        addInfoMessage(null, msg);
    }
    
    /**
     * Add information message to a specific client.
     *
     * @param clientId the client id
     * @param msg      the information message
     */
    public static void addInfoMessage(String clientId, String msg) {
        addMessage(clientId, msg, FacesMessage.SEVERITY_INFO);
    }
    
    /**
     * Add information message to a specific client.
     *
     * @param clientId the client id
     * @param msg      the information message
     */
    public static void addWarnMessage(String clientId, String msg) {
        addMessage(clientId, msg, FacesMessage.SEVERITY_WARN);
    }
    
    /**
     * Add error message.
     *
     * @param msg the error message
     */
    public static void addErrorMessage(String msg) {
        addErrorMessage(null, msg);
    }
    
    /**
     * Finds component with the given id
     *
     * @param c  component check children of.
     * @param id id of component to search for.
     *
     * @return found component if any.
     */
    public static UIComponent findComponent(UIComponent c, String id) {
        if (id.equals(c.getId())) {
            return c;
        }
        Iterator<UIComponent> kids = c.getFacetsAndChildren();
        while (kids.hasNext()) {
            UIComponent found = findComponent(kids.next(), id);
            if (found != null) {
                return found;
            }
        }
        return null;
    }
    
    /**
     * Finds all component with the given id.  Component id's are formed from
     * the concatination of parent component ids.  This search will find
     * all componet in the component tree with the specified id as it is possible
     * to have the same id used more then once in the component tree
     *
     * @param root            component check children of.
     * @param id              id of component to search for.
     * @param foundComponents list of found component with the specified id.
     *
     */
    public static void findAllComponents(UIComponent root, String id, List<UIComponent> foundComponents) {
        if (id.equals(root.getId())) {
            foundComponents.add(root);
        }
        Iterator<UIComponent> kids = root.getFacetsAndChildren();
        while (kids.hasNext()) {
            findAllComponents(kids.next(), id, foundComponents);
        }
    }
    
    //    public static Object getUiParam(String nombreParam) {
    //        FaceletContext faceletContext = (FaceletContext) FacesContext.getCurrentInstance()
    //                                                                     .getAttributes()
    //                                                                     .get(FaceletContext.FACELET_CONTEXT_KEY);
    //        return faceletContext.getAttribute(nombreParam);
    //    }
    
    /**
     * Add error message to a specific client.
     *
     * @param clientId the client id
     * @param msg      the error message
     */
    public static void addErrorMessage(String clientId, String msg) {
        addMessage(clientId, msg, FacesMessage.SEVERITY_ERROR);
    }
    
    private static void addMessage(String clientId, String msg, FacesMessage.Severity severity) {
        FacesContext.getCurrentInstance()
                    .addMessage(clientId, new FacesMessage(severity, msg, msg));
        FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getFlash()
                    .setKeepMessages(true);
    }
    
    public static void putFlash(String nameParam, Object param) {
        FacesUtils.getExternalContext()
                  .getFlash()
                  .put(nameParam, param);
    }
    
    public static Object getFlash(String nameParam) {
        return FacesUtils.getExternalContext()
                         .getFlash()
                         .get(nameParam);
    }
    
    private static String getJsfEl(String value) {
        return "#{" + value + "}";
    }
}
