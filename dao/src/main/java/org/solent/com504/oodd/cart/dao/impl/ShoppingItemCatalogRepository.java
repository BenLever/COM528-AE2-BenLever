package org.solent.com504.oodd.cart.dao.impl;

import java.util.List;
import org.solent.com504.oodd.cart.model.dto.ShoppingItem;
import org.solent.com504.oodd.cart.model.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;

@Repository
public interface ShoppingItemCatalogRepository  extends JpaRepository<ShoppingItem,Long>{
    
    @Query("select i from ShoppingItem i where i.name = :name")
    public List<ShoppingItem> getItemByName(@Param("name") String name);

    @Query("select i from ShoppingItem i where i.active = true")
    public List<ShoppingItem> getActivatedItems();
    @Modifying
    @Query("update ShoppingItem i set i.active = false where uuid = :uuid")
    public List<ShoppingItem> deactivateItems(@Param("uuid") String uuid);
    @Modifying
    @Query("update ShoppingItem i set i.stock = i.stock-1 where i.name = :name")
    public void removeStock(@Param("name") String name);
}
