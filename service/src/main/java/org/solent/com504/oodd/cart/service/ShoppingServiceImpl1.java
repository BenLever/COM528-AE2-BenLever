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

/**
 *
 * @author cgallen
 */
@Component
public class ShoppingServiceImpl1 implements ShoppingService {
    
    @Autowired
    ShoppingItemCatalogRepository shoppingItemRepo;
    
    public ShoppingServiceImpl1() {

        // initialised the hashmap
//        for (ShoppingItem item : itemlist) {
//            itemMap.put(item.getName(), item);
//        }
    }

    @Override
    public List<ShoppingItem> getAvailableItems() {
        
        List<ShoppingItem> itemList = shoppingItemRepo.findAll();
        return itemList;
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
//        ShoppingItem templateItem = itemMap.get(name);
//        
//        if(templateItem==null) return null;
//        
//        ShoppingItem item = new ShoppingItem();
//        item.setName(name);
//        item.setPrice(templateItem.getPrice());
//        item.setQuantity(0);
//        item.setUuid(UUID.randomUUID().toString());
        return null;
    }
    
    @Override 
    public String checkStock(ShoppingCart cart){
        for (ShoppingItem shoppingItem : cart.getShoppingCartItems()) {
                
                ShoppingItem ShoppingItem = shoppingItemRepo.findByName(ShoppingItem.getItem().getName()).get(0);
                if(shoppingItem != null){
                    
                    if((shoppingItem.getQuantity() - shoppingItem.getQuantity()) < 0){
                        return "Error for Item: " + shoppingItem.getName() + "   -  We only have " + shoppingItem.getQuantity() + " in stock and you're trying to order " + orderItem.getQuantity();
                    }
                }
        }
        return "";
    }

}
