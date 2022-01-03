package org.solent.com504.oodd.cart.spring.web;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.solent.com504.oodd.cart.model.dto.ShoppingItem;
import org.solent.com504.oodd.cart.model.dto.User;
import org.solent.com504.oodd.cart.model.dto.UserRole;
import org.solent.com504.oodd.cart.model.service.ShoppingCart;
import org.solent.com504.oodd.cart.model.service.ShoppingService;
import org.solent.com504.oodd.cart.web.WebObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.solent.com504.oodd.bank.CreditCard;
import org.solent.com504.oodd.cardchecker.CardValidationResult;
import org.solent.com504.oodd.cardchecker.RegexCardValidator;
import org.solent.com504.oodd.cart.dao.impl.UserRepository;

@Controller
@RequestMapping("/")
public class MVCController {

    final static Logger LOG = LogManager.getLogger(MVCController.class);

    // this could be done with an autowired bean
    //private ShoppingService shoppingService = WebObjectFactory.getShoppingService();
    @Autowired
    ShoppingService shoppingService = null;
    
    @Autowired
    UserRepository userRepo;

    // note that scope is session in configuration
    // so the shopping cart is unique for each web session
    @Autowired
    ShoppingCart shoppingCart = null;

    private User getSessionUser(HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            sessionUser = new User();
            sessionUser.setUsername("anonymous");
            sessionUser.setUserRole(UserRole.ANONYMOUS);
            session.setAttribute("sessionUser",sessionUser);
        }
        return sessionUser;
    }

    // this redirects calls to the root of our application to index.html
    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {
        return "redirect:/index.html";
    }

    @RequestMapping(value = "/home", method = {RequestMethod.GET, RequestMethod.POST})
    public String viewHome(@RequestParam(name = "action", required = false) String action,
            @RequestParam(name = "itemName", required = false) String itemName,
            @RequestParam(name = "itemUUID", required = false) String itemUuid,
            Model model,
            HttpSession session) {

        // get sessionUser from session
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        // used to set tab selected
        model.addAttribute("selectedPage", "home");

        String message = "";
        String errorMessage = "";

        // note that the shopping cart is is stored in the sessionUser's session
        // so there is one cart per sessionUser
//        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("shoppingCart");
//        if (shoppingCart == null) synchronized (this) {
//            if (shoppingCart == null) {
//                shoppingCart = WebObjectFactory.getNewShoppingCart();
//                session.setAttribute("shoppingCart", shoppingCart);
//            }
//        }
        if (action == null) {
            // do nothing but show page
        } else if ("addItemToCart".equals(action)) {
            ShoppingItem shoppingItem = shoppingService.getNewItemByName(itemName);
            if (shoppingItem == null) {
                message = "cannot add unknown " + itemName + " to cart";
            } else {
                message = "adding " + itemName + " to cart price= " + shoppingItem.getPrice();
                shoppingCart.addItemToCart(shoppingItem);
            }
        } else if ("removeItemFromCart".equals(action)) {
            message = "removed " + itemName + " from cart";
            shoppingCart.removeItemFromCart(itemUuid);
        } else {
            message = "unknown action=" + action;
        }

        List<ShoppingItem> availableItems = shoppingService.getAvailableItems();

        List<ShoppingItem> shoppingCartItems = shoppingCart.getShoppingCartItems();

        Double shoppingcartTotal = shoppingCart.getTotal();

        // populate model with values
        model.addAttribute("availableItems", availableItems);
        model.addAttribute("shoppingCartItems", shoppingCartItems);
        model.addAttribute("shoppingcartTotal", shoppingcartTotal);
        model.addAttribute("message", message);
        model.addAttribute("errorMessage", errorMessage);

        return "home";
    }

    
    @RequestMapping(value = "/catalog", method = {RequestMethod.GET, RequestMethod.POST})
    public String catalogList(Model model, HttpSession session) {

        // get sessionUser from session
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);
        
        List<ShoppingItem> availableItems = shoppingService.getAvailableItems();
        
                
        model.addAttribute("availableItems", availableItems);
        
        // used to set tab selected
        model.addAttribute("selectedPage", "admin");
        return "catalog";
    }
    
    @RequestMapping(value = "/properties", method = {RequestMethod.GET, RequestMethod.POST})
    public String propertiesCart(Model model, HttpSession session) {

        // get sessionUser from session
        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);
        
        // used to set tab selected
        model.addAttribute("selectedPage", "properties");
        return "properties";
    }
    
    @RequestMapping(value = "/cart", method = {RequestMethod.GET, RequestMethod.POST})
    public String viewCart(
            @RequestParam(name = "action", required = false) String action,
            @RequestParam(name = "itemName", required = false) String itemName,
            @RequestParam(name = "itemUuid", required = false) String itemUuid,
            Model model,
            HttpSession session) {

        User sessionUser = getSessionUser(session);
        
        model.addAttribute("sessionUser", sessionUser);

        model.addAttribute("selectedPage", "cart");

        String message = "";
        String errorMessage = "";
        
        if ("removeItemFromCart".equals(action)) {
            message = "removed " + itemName + " from cart";
            shoppingCart.removeItemFromCart(itemUuid);
        } 


        List<ShoppingItem> shoppingCartItems = shoppingCart.getShoppingCartItems();

        Double shoppingcartTotal = shoppingCart.getTotal();

        // populate model with values
        model.addAttribute("shoppingCartItems", shoppingCartItems);
        model.addAttribute("shoppingcartTotal", shoppingcartTotal);
        model.addAttribute("message", message);
        model.addAttribute("errorMessage", errorMessage);
        LOG.warn(errorMessage);
        return "cart";
    }
    
    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
        public String CheckoutView(
            Model model,
            HttpSession session) {

        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);

        
        List<User> purchaser = userRepo.findByUsername(sessionUser.getUsername());
        if(purchaser.size() > 0){
            User purchaseUser = purchaser.get(0);
            model.addAttribute("user", purchaseUser);    
        }
        
        // used to set tab selected
        model.addAttribute("selectedPage", "checkout");

        String message = "";
        String errorMessage = "";


        List<ShoppingItem> shoppingCartItems = shoppingCart.getShoppingCartItems();

        Double shoppingcartTotal = shoppingCart.getTotal();
        
        // populate model with values
        model.addAttribute("shoppingCartItems", shoppingCartItems);
        model.addAttribute("shoppingcartTotal", shoppingcartTotal);
        model.addAttribute("message", message);
        model.addAttribute("errorMessage", errorMessage);
        return "checkout";
    }
    

    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
    public String CheckoutCart(
            @RequestParam(name = "cardnumber", required = false) String cardnumber,            
            @RequestParam(name = "cardname", required = false) String cardname,
            @RequestParam(name = "expirydate", required = false) String expirydate,
            @RequestParam(name = "issuenumber", required = false) String issuenumber,
            @RequestParam(name = "cvv", required = false) String cvv,
            Model model,
            HttpSession session) {

        User sessionUser = getSessionUser(session);
        model.addAttribute("sessionUser", sessionUser);
        
        model.addAttribute("selectedPage", "checkout");

        String message = "";
        String errorMessage = "";
        
        //Validate
        CardValidationResult result = RegexCardValidator.isValid(cardnumber);
        LOG.info("Validating: " + cardnumber + " is valid: " + result.isValid() + " message: " + result.getMessage());

        
        
        if(!result.isValid()){
            CreditCard card = new CreditCard();
            card.setCvv(cvv);
            card.setCardnumber(cardnumber);
            card.setEndDate(expirydate);
            card.setIssueNumber(issuenumber);
            card.setName(name);
            }
            if(errorMessage.equals("")){
                
                //Check stock
                String stockMessage = shoppingService.checkStock(shoppingCart);
                if(stockMessage.equals("")){
                    try{
                        boolean purchased = shoppingService.purchaseItems(shoppingCart, sessionUser, card);
                        if(!purchased){
                            errorMessage = "Unable to purchase items. Please make sure you have entered your details correctly and that you have enough money in your account";
                            LOG.error("Checkout Failed: " + errorMessage);
                        }
                        else{
                            message = "Successfully purchased items";
                        }
        }
        else{
            errorMessage = result.getError();
            LOG.error("Checkout Failed: " + errorMessage);  
            
        }
        
        List<ShoppingItem> shoppingCartItems = shoppingCart.getShoppingCartItems();
        Double shoppingcartTotal = shoppingCart.getTotal();
        

        // populate model with values
        model.addAttribute("shoppingCartItems", shoppingCartItems);
        model.addAttribute("shoppingcartTotal", shoppingcartTotal);
        model.addAttribute("message", message);
        model.addAttribute("errorMessage", errorMessage);
        LOG.warn(errorMessage);
        return "checkout";
    }



    @ExceptionHandler(Exception.class)
    public String myExceptionHandler(final Exception e, Model model, HttpServletRequest request) {
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
