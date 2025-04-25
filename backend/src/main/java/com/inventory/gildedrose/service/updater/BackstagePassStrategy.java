package com.inventory.gildedrose.service.updater;

import com.inventory.gildedrose.model.Item;
import org.springframework.stereotype.Component;

@Component
public class BackstagePassStrategy implements ItemUpdateStrategy {
    @Override
    public void updateQuality(Item item) {
        // Decrease sellIn
        item.setSellIn(item.getSellIn() - 1);

        // Check if concert is over
        if (item.getSellIn() < 0) {
            item.setQuality(0);
            return;
        }

        // Increase quality based on days remaining
        int qualityIncrease = 1;
        if (item.getSellIn() < 5) {
            qualityIncrease = 3;
        } else if (item.getSellIn() < 10) {
            qualityIncrease = 2;
        }

        int newQuality = item.getQuality() + qualityIncrease;
        item.setQuality(Math.min(50, newQuality));
    }
}