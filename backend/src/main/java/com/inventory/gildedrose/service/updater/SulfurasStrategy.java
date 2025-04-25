package com.inventory.gildedrose.service.updater;

import com.inventory.gildedrose.model.Item;
import org.springframework.stereotype.Component;

@Component
public class SulfurasStrategy implements ItemUpdateStrategy {
    @Override
    public void updateQuality(Item item) {
        // Legendary item - no changes to quality or sellIn
    }
}
