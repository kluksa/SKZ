/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dhz.skz.web;

import java.io.Serializable;
import java.security.Principal;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author kraljevic
 */
@ManagedBean
@RequestScoped
public class Auth implements Serializable{

    private String username;
    private String password;
    private String originalURL;
    private String rola;

    public String getRola() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        Principal userPrincipal = ec.getUserPrincipal();
        return null;
        
    }

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        originalURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
//
        if (originalURL == null) {
//            originalURL = externalContext.getRequestContextPath() + "/faces/admin/newjsf.xhtml";
        } 
//        else {
//            String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);
//
//            if (originalQuery != null) {
//                originalURL += "?" + originalQuery;
//            }
//        }
    }

    public void login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.login(this.username, this.password);
        } catch (ServletException e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pogreška", "Pogrešna lozinka.");
            context.addMessage(null, msg);
//            return "error";
        }
//        return "/faces/admin/postaje.xhtml";
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage("Logout failed."));
        }
        return "/faces/index.xhtml";

    }

    public void loginFailure() {
        FacesMessage msg;
        msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Pogreška", "Pogrešna lozinka.");
             
        FacesContext.getCurrentInstance().addMessage(null, msg); 
    }
//    public void login() throws IOException, ServletException {
//        FacesContext context = FacesContext.getCurrentInstance();
//        ExternalContext externalContext = context.getExternalContext();
//        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
//
//        try {
//            request.login(username, password);
////            User user = userService.find(username, password);
//            Korisnik user = new Korisnik();
//            externalContext.getSessionMap().put("user", user);
//            externalContext.redirect(originalURL);
//        } catch (ServletException e) {
//            // Handle unknown username/password in request.login().
//            context.addMessage(null, new FacesMessage("Unknown login"));
//        }
//    }
//
//    public void logout() throws IOException {
//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//        externalContext.invalidateSession();
//        externalContext.redirect(externalContext.getRequestContextPath() + "/faces/login.xhtml");
//    }
    // Getters/setters for username and password.
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
