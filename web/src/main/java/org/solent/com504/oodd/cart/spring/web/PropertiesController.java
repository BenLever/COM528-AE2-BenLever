/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.oodd.cart.spring.web;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.solent.com504.oodd.cart.model.dto.User;
import org.solent.com504.oodd.cart.model.dto.UserRole;
import static org.solent.com504.oodd.cart.spring.web.MVCController.LOG;
import org.solent.com504.oodd.properties.dao.impl.PropertiesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PropertiesController {
    final static Logger LOG = LogManager.getLogger(PropertiesController.class);

    @Autowired
    PropertiesDao propertiesDao;
    
    private User getSessionUser(HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        LOG.info("Got session user");
        if (sessionUser == null) {
            sessionUser = new User();
            sessionUser.setUsername("anonymous");
            sessionUser.setUserRole(UserRole.ANONYMOUS);
            session.setAttribute("sessionUser",sessionUser);
        }
        return sessionUser;
    }
    
    /**
     * Get and Post method for the properties page
     * @param action either empty or update properties
     * @param url new value for the url property
     * @param username new value for the username property
     * @param password new value for the password property
     * @param shopKeeperCard new value for the shop keeper's card property
     * @param model mvc model
     * @param session web session
     * @return the properties page
     */
    @RequestMapping(value = "/properties", method = {RequestMethod.GET, RequestMethod.POST})
    public String propertiesPage(
            @RequestParam(name = "action", required = false) String action,            
            @RequestParam(name = "url", required = false) String url,            
            @RequestParam(name = "username", required = false) String username,            
            @RequestParam(name = "password", required = false) String password,            
            @RequestParam(name = "shopKeeperCard", required = false) String shopKeeperCard,
            Model model,
            HttpSession session) {
        
        
        LOG.info("Properties Input values " + action + url + username + password + shopKeeperCard);
        
        String message = "";
        User sessionuser = getSessionUser(session);
        LOG.info(sessionuser.getUserRole());
        if(UserRole.ADMINISTRATOR.equals(sessionuser.getUserRole())){
            try{
                LOG.info("User is an admin");
                if ("updateProperties".equals(action)) {
                    LOG.info("Update action correct");
                    message = "Properties updated sucessfully";
                    propertiesDao.setProperty("org.solent.oodd.pos.service.apiUrl", url);
                    propertiesDao.setProperty("org.solent.oodd.pos.service.apiUsername", username);
                    propertiesDao.setProperty("org.solent.oodd.pos.service.apiPassword", password);
                    propertiesDao.setProperty("org.solent.oodd.pos.service.shopKeeperCard", shopKeeperCard);
                }
            } catch(Error er) {
                LOG.error(er);
            }
        }
        else{
            message = "Non-admin tried to change properties";
        }

        
        String newurl = propertiesDao.getProperty("org.solent.oodd.pos.service.apiUrl");
        String newusername = propertiesDao.getProperty("org.solent.oodd.pos.service.apiUsername");
        String newpassword = propertiesDao.getProperty("org.solent.oodd.pos.service.apiPassword");
        String newshopKeeperCard = propertiesDao.getProperty("org.solent.oodd.pos.service.shopKeeperCard");
        
        
        LOG.info(newshopKeeperCard);
        LOG.info(newurl);
        LOG.info(newusername);
        LOG.info(newpassword);

        
        model.addAttribute("sessionUser", sessionuser);        
        model.addAttribute("message", message);        
        model.addAttribute("shopcard", newshopKeeperCard);        
        model.addAttribute("url", newurl);        
        model.addAttribute("username", newusername);
        model.addAttribute("password", newpassword);

        // used to set tab selected
        model.addAttribute("selectedPage", "properties");
        
        return "properties";
    }
    
    /**
     * Exception handler page 
     * @param e exception to show
     * @param model mvc model
     * @param request web request
     * @return error page
     */
    @ExceptionHandler(Exception.class)
    public String myExceptionHandler(final Exception e, Model model, HttpServletRequest request) {
        LOG.error(e);
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        final String strStackTrace = sw.toString(); // stack trace as a string
        String urlStr = "not defined";
        if (request != null) {
            StringBuffer url = request.getRequestURL();
            urlStr = url.toString();
        }
        model.addAttribute("requestUrl", urlStr);
        model.addAttribute("strStackTrace", strStackTrace);
        model.addAttribute("exception", e);
        //logger.error(strStackTrace); // send to logger first
        return "error"; // default friendly exception message for sessionUser
    }
}
