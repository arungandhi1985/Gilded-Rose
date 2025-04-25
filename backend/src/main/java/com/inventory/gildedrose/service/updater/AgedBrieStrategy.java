package com.inventory.gildedrose.service.updater;

import com.inventory.gildedrose.model.Item;
import org.springframework.stereotype.Component;

@Component
public class AgedBrieStrategy implements ItemUpdateStrategy {
    @Override
    public void updateQuality(Item item) {
        // Decrease sellIn
        item.setSellIn(item.getSellIn() - 1);

        // Increase quality as it gets older
        int qualityIncrease = item.getSellIn() < 0 ? 2 : 1;
        int newQuality = item.getQuality() + qualityIncrease;
        item.setQuality(Math.min(50, newQuality));
    }
}