package com.inventory.gildedrose.service.updater;

import com.inventory.gildedrose.model.Item;
import org.springframework.stereotype.Component;

@Component
public class NormalItemStrategy implements ItemUpdateStrategy {
    @Override
    public void updateQuality(Item item) {
        // Decrease sellIn
        item.setSellIn(item.getSellIn() - 1);

        // Decrease quality
        int qualityDecrease = item.getSellIn() < 0 ? 2 : 1;
        int newQuality = Math.max(0, item.getQuality() - qualityDecrease);
        item.setQuality(Math.min(50, newQuality));
    }
}