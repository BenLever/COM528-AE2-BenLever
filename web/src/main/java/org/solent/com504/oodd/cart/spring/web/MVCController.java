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
import org.solent.com504.oodd.properties.dao.impl.WebObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.solent.com504.oodd.bank.model.dto.CreditCard;
import org.solent.com504.oodd.cardchecker.CardValidationResult;
import org.solent.com504.oodd.cardchecker.RegexCardValidator;
import org.solent.com504.oodd.cart.dao.impl.UserRepository;
import static org.solent.com504.oodd.cart.spring.service.PopulateDatabaseOnStart.BANK_URL;
import org.solent.com504.oodd.properties.dao.impl.PropertiesDao;
import org.solent.com504.oodd.bank.model.dto.TransactionRequestMessage;
import org.solent.com504.oodd.bank.model.dto.BankTransactionStatus;
import org.solent.com504.oodd.bank.model.dto.TransactionReplyMessage;
import org.solent.com504.oodd.bank.model.client.BankRestClient;
import org.solent.com504.oodd.bank.client.impl.BankRestClientImpl;
import org.solent.com504.oodd.bank.model.dto.BankTransaction;

@Controller
@RequestMapping("/")
public class MVCController {

    final static Logger LOG = LogManager.getLogger(MVCController.class);
    
    public static final PropertiesDao propertiesDao = WebObjectFactory.getPropertiesDao();
    public static CreditCard cardTo = null;
    public static final String BANK_URL = propertiesDao.getProperty("rest_url");
    

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
            } else if (shoppingCart.addItemToCart(shoppingItem) == false) {
                message = "Not enough " + itemName + " to purchase";
            } else {
                message = "adding " + itemName + " to cart price= " + shoppingItem.getPrice();

            }
        } else if ("removeItemFromCart".equals(action)) {
            message = "removed " + itemName;
            shoppingCart.removeItemFromCart(itemUuid);

        } else if ("addItemsToBasket".equals(action)) {
            message = "Added " + itemName + " to cart";
            shoppingCart.removeItemFromCart(itemUuid);
        } else {
            message = "unknown action=" + action;
        }

        List<ShoppingItem> availableItems = null;

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
    @SuppressWarnings("empty-statement")
    public String viewCart
            (@RequestParam(name = "action", required = false) String action,
            @RequestParam(name = "itemName", required = false) String itemName,
            @RequestParam(name = "itemUUID", required = false) String itemUuid,
            @RequestParam(name = "cust_cardnumber", required = false) String custcardnumber,
            @RequestParam(name = "cust_expirydate", required = false) String custexpirydate,
            @RequestParam(name = "cust_cvv", required = false) String custCVV,
            @RequestParam(name = "cust_issueNumber", required = false) String custissuenumber,
            Model model,
            HttpSession session) {

        User sessionUser = getSessionUser(session);
        
        model.addAttribute("sessionUser", sessionUser);

        model.addAttribute("selectedPage", "cart");

        String message = "";
        String errorMessage = "";
        
        List<ShoppingItem> shoppingCartItems = shoppingCart.getShoppingCartItems();

        Double shoppingcartTotal = shoppingCart.getTotal();
        
        String name = propertiesDao.getProperty("name");
        String cardnumber = propertiesDao.getProperty("cardnumber");
        String issuenumber = propertiesDao.getProperty("issuenumber");
        String expirydate = propertiesDao.getProperty("expirydate");
        String cvv = propertiesDao.getProperty("cvv");
        
        //Card To
        CreditCard toCard = new CreditCard();
        toCard.setCardnumber(cardnumber);
        toCard.setCvv(cvv);
        toCard.setEndDate(expirydate);
        toCard.setIssueNumber(issuenumber);
        toCard.setName(name);
        
        
        //Card From
        CreditCard fromCard = new CreditCard();
        fromCard.setEndDate("");
        fromCard.setCardnumber("");
        fromCard.setCvv("");
        fromCard.setIssueNumber("");
        
        TransactionReplyMessage reply = null;
        
        if (action == null) {
            // do nothing but show page
        } else if ("removeItemFromCart".equals(action)) {
            message = "removed " + itemName;
            shoppingCart.removeItemFromCart(itemUuid);
            LOG.debug("Item Removed");

        } else if ("payment".equals(action)) {
            fromCard.setEndDate(custexpirydate);
            fromCard.setCardnumber(custcardnumber);
            fromCard.setCvv(custCVV);
            fromCard.setIssueNumber(custissuenumber);
            LOG.debug("card number: " + fromCard.getCardnumber());
            
            //Starts Client
            String bankurl;
            bankurl = BANK_URL;
            BankRestClient client = new BankRestClientImpl(bankurl);

            {
                Double amount = shoppingCart.getTotal();
                LOG.debug("amount: " + amount);
                reply = client.transferMoney(toCard, fromCard, amount);
                message = "Transaction" + reply;
            }

            if (message.contains("SUCCESS")) 
            {shoppingCart.removeStock(itemName);
            String reciept = shoppingCartItems.toString();
            LOG.debug(reciept);
            
            
            };
        }
        // populate model with values
        model.addAttribute("shoppingCartItems", shoppingCartItems);
        model.addAttribute("shoppingcartTotal", shoppingcartTotal);
        model.addAttribute("message", message);
        model.addAttribute("errorMessage", errorMessage);
        
        LOG.warn(errorMessage);
        return "cart";
    }

    /*
     * Default exception handler, catches all exceptions, redirects to friendly
     * error page. Does not catch request mapping errors
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
