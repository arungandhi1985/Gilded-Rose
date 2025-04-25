package com.inventory.gildedrose.service;

import com.inventory.gildedrose.model.Item;

import java.util.List;

public interface InventoryService {
    List<Item> getInventory();
    List<Item> updateInventory(List<Item> items);
    void saveInventory(List<Item> items);
}