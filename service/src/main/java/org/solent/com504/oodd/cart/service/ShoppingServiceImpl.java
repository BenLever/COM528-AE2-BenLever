/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.oodd.cart.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.solent.com504.oodd.cart.dao.impl.ShoppingItemCatalogRepository;
import org.solent.com504.oodd.cart.model.service.ShoppingCart;
import org.solent.com504.oodd.cart.model.dto.ShoppingItem;
import org.solent.com504.oodd.cart.model.service.ShoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.solent.com504.oodd.bank.CreditCard;
import org.solent.com504.oodd.bank.BankTransaction;
import org.solent.com504.oodd.cart.model.dto.User;
import javax.transaction.Transactional;

/**
 *
 * @author cgallen
 */
@Component
public class ShoppingServiceImpl implements ShoppingService {
    
    @Autowired
    private ShoppingItemCatalogRepository shoppingItemRepo;
    
    
    public ShoppingServiceImpl() {

        // initialised the hashmap
//        for (ShoppingItem item : itemlist) {
//            itemMap.put(item.getName(), item);
//        }
    }

    @Override
    public List<ShoppingItem> getAvailableItems() {
        return shoppingItemRepo.findAll();
    }

    @Override
    public boolean purchaseItems(ShoppingCart shoppingCart) {
        System.out.println("purchased items");
        for (ShoppingItem shoppingItem : shoppingCart.getShoppingCartItems()) {
            System.out.println(shoppingItem);
        }

        return true;
    }

    @Override
    public ShoppingItem getNewItemByName(String name) {

        ShoppingItem item = null;
        List<ShoppingItem> items = shoppingItemRepo.getItemByName(name);

        if (!items.isEmpty()) {
            item = items.get(0);
        }
        return item;
    }
    
    @Transactional
    public ShoppingItem addNewItemByNamePrice(String name, Double price) {
        ShoppingItem item = new ShoppingItem();
        item.setName(name);
        item.setPrice(price);
        shoppingItemRepo.save(item);

        return item;
    }

    @Override
    public ShoppingItem addNewItem(ShoppingItem shoppingItem) {

        ShoppingItem item = null;
        List<ShoppingItem> items = shoppingItemRepo.getItemByName(shoppingItem.getName());

        if (!items.isEmpty()) {
            throw new IllegalArgumentException("That item is already present in database" + shoppingItem);
        }
        item = shoppingItemRepo.save(shoppingItem);
        return item;
    }

    @Override
    public void removeStock(){
        
        shoppingItemRepo.removeStock("name");
    }


}
