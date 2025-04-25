package com.inventory.gildedrose.service.updater;

import com.inventory.gildedrose.model.ItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateStrategyFactory {
    private final NormalItemStrategy normalItemStrategy;
    private final AgedBrieStrategy agedBrieStrategy;
    private final BackstagePassStrategy backstagePassStrategy;
    private final SulfurasStrategy sulfurasStrategy;
    private final ConjuredItemStrategy conjuredItemStrategy;

    @Autowired
    public UpdateStrategyFactory(
            NormalItemStrategy normalItemStrategy,
            AgedBrieStrategy agedBrieStrategy,
            BackstagePassStrategy backstagePassStrategy,
            SulfurasStrategy sulfurasStrategy,
            ConjuredItemStrategy conjuredItemStrategy) {
        this.normalItemStrategy = normalItemStrategy;
        this.agedBrieStrategy = agedBrieStrategy;
        this.backstagePassStrategy = backstagePassStrategy;
        this.sulfurasStrategy = sulfurasStrategy;
        this.conjuredItemStrategy = conjuredItemStrategy;
    }

    public ItemUpdateStrategy getStrategy(String itemName) {
        if (itemName == null) {
            return null;
        }

        return switch (ItemType.fromName(itemName)) {
            case AGED_BRIE -> agedBrieStrategy;
            case BACKSTAGE_PASS -> backstagePassStrategy;
            case SULFURAS -> sulfurasStrategy;
            case CONJURED -> conjuredItemStrategy;
            case NORMAL -> normalItemStrategy;
            case UNKNOWN -> null;
        };
    }
}