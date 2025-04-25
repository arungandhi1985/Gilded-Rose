package com.inventory.gildedrose.model;

public enum ItemType {
    AGED_BRIE("Aged Brie"),
    BACKSTAGE_PASS("Backstage passes"),
    SULFURAS("Sulfuras"),
    CONJURED("Conjured"),
    NORMAL("Normal Item"),
    UNKNOWN("Unknown");

    private final String prefix;

    ItemType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public static ItemType fromName(String itemName) {
        if (itemName == null) {
            return UNKNOWN;
        }

        for (ItemType type : values()) {
            if (itemName.startsWith(type.prefix)) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
