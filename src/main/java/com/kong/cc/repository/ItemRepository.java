package com.kong.cc.repository;

import com.kong.cc.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, String> {

    Item findByItemCode(String itemCode);

    @Query("select i from Item i where i.itemName like '%:keyword%'")
    List<Item> findMenuListByKeyword(@Param("keyword") String keyword);

    //상민
    List<Item> findByItemCodeIn(List<String> itemCodes);

    public List<Item> findByItemNameContaining(String itemName) throws Exception;
}


