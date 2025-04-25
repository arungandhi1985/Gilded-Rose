package com.inventory.gildedrose.service;

import com.inventory.gildedrose.model.Item;
import com.inventory.gildedrose.exception.InventoryException;
import com.inventory.gildedrose.service.updater.ItemUpdateStrategy;
import com.inventory.gildedrose.service.updater.UpdateStrategyFactory;
import com.inventory.gildedrose.util.JsonFileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final JsonFileHandler jsonFileHandler;
    private final UpdateStrategyFactory strategyFactory;

    @Autowired
    public InventoryServiceImpl(JsonFileHandler jsonFileHandler, UpdateStrategyFactory strategyFactory) {
        this.jsonFileHandler = jsonFileHandler;
        this.strategyFactory = strategyFactory;
    }

    @Override
    public List<Item> getInventory() {
        return jsonFileHandler.readInventory();
    }

    @Override
    public List<Item> updateInventory(List<Item> items) {
        if (items == null || items.isEmpty()) {
            throw new InventoryException(
                    "No inventory items found to update",
                    InventoryException.ErrorType.VALIDATION_ERROR
            );
        }

        for (Item item : items) {
            if (item == null) {
                continue; // Skip null items
            }

            ItemUpdateStrategy strategy = strategyFactory.getStrategy(item.getName());
            if (strategy != null) {
                strategy.updateQuality(item);
            } else {
                // Handle invalid items
                item.setName("NO SUCH ITEM");
            }
        }

        saveInventory(items);
        return items;
    }

    @Override
    public void saveInventory(List<Item> items) {
        if (items == null) {
            throw new InventoryException(
                    "Cannot save null inventory",
                    InventoryException.ErrorType.VALIDATION_ERROR
            );
        }

        jsonFileHandler.writeInventory(items);
    }
}