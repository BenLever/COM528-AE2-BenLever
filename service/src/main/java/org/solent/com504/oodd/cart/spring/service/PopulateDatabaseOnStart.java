/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.oodd.cart.spring.service;

import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.solent.com504.oodd.cart.dao.impl.ShoppingItemCatalogRepository;
import org.solent.com504.oodd.cart.dao.impl.UserRepository;
import org.solent.com504.oodd.cart.model.dto.ShoppingItem;
import org.solent.com504.oodd.bank.model.dto.CreditCard;
import org.solent.com504.oodd.bank.client.impl.BankRestClientImpl;
import org.solent.com504.oodd.cart.model.dto.User;
import org.solent.com504.oodd.cart.model.dto.UserRole;
import org.solent.com504.oodd.properties.dao.impl.WebObjectFactory;
import org.solent.com504.oodd.properties.dao.impl.PropertiesDao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author cgallen
 */
@Component
public class PopulateDatabaseOnStart {

    private static final Logger LOG = LogManager.getLogger(PopulateDatabaseOnStart.class);
    
    public static final PropertiesDao propertiesDao = WebObjectFactory.getPropertiesDao();
    public static CreditCard cardTo = null;
    public static final String BANK_URL = propertiesDao.getProperty("rest_url");

    private static final String DEFAULT_ADMIN_USERNAME = "globaladmin";
    private static final String DEFAULT_ADMIN_PASSWORD = "globaladmin";

    private static final String DEFAULT_USER_PASSWORD = "user1234";
    private static final String DEFAULT_USER_USERNAME = "user1234";

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ShoppingItemCatalogRepository shoppingItemCatalogRepository;

    @PostConstruct
    public void initDatabase() {
        LOG.debug("initialising database with startup values");

        // initialising admin and normal user if dont exist
        User adminUser = new User();
        adminUser.setUsername(DEFAULT_ADMIN_USERNAME);
        adminUser.setFirstName("default administrator");
        adminUser.setPassword(DEFAULT_ADMIN_PASSWORD);
        adminUser.setUserRole(UserRole.ADMINISTRATOR);

        List<User> users = userRepository.findByUsername(DEFAULT_ADMIN_USERNAME);
        if (users.isEmpty()) {
            userRepository.save(adminUser);
            LOG.info("creating new default admin user:" + adminUser);
        } else {
            LOG.info("default admin user already exists. Not creating new :" + adminUser);
        }

        User defaultUser = new User();
        defaultUser.setUsername(DEFAULT_USER_USERNAME);
        defaultUser.setFirstName("default user");
        defaultUser.setPassword(DEFAULT_USER_PASSWORD);
        defaultUser.setUserRole(UserRole.CUSTOMER);

        users = userRepository.findByUsername(DEFAULT_USER_USERNAME);
        if (users.isEmpty()) {
            userRepository.save(defaultUser);
            LOG.info("creating new default user:" + defaultUser);
        } else {
            LOG.info("defaultuser already exists. Not creating new :" + defaultUser);
        }
        
        List<ShoppingItem> itemlist = Arrays.asList(new ShoppingItem("house", 20000.00, 20),
            new ShoppingItem("hen", 5.00, 10),
            new ShoppingItem("car", 5000.00, 10),
            new ShoppingItem("penguin", 15000.00, 10),
            new ShoppingItem("Boat", 10.00, 5),
            new ShoppingItem("pet alligator", 65.00, 5));

        for(ShoppingItem item:itemlist){
            shoppingItemCatalogRepository.save(item);
        }
        
        
        String name = propertiesDao.getProperty("name");
        String cardnumber = propertiesDao.getProperty("cardnumber");
        String issuenumber = propertiesDao.getProperty("issuenumber");
        String expirydate = propertiesDao.getProperty("expirydate");
        String cvv = propertiesDao.getProperty("cvv");
        
        
        CreditCard toCard = new CreditCard();
        toCard.setCardnumber(cardnumber);
        toCard.setCvv(cvv);
        toCard.setEndDate(expirydate);
        toCard.setIssueNumber(issuenumber);
        toCard.setName(name);
        
        String bankurl;
        bankurl = BANK_URL;
        
        
        

        LOG.debug("database initialised");
    }
}
