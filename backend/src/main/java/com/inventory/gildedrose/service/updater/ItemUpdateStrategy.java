package com.inventory.gildedrose.service.updater;


import com.inventory.gildedrose.model.Item;

public interface ItemUpdateStrategy {
    void updateQuality(Item item);
}