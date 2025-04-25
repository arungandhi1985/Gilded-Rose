package com.inventory.gildedrose.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.gildedrose.exception.InventoryException;
import com.inventory.gildedrose.model.Item;
import com.inventory.gildedrose.model.ItemType;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JsonFileHandler {
    private static final String INVENTORY_FILE = "inventory.json";
    private final ObjectMapper objectMapper;

    public JsonFileHandler() {
        this.objectMapper = new ObjectMapper();
    }

    public List<Item> readInventory() {
        try {
            File file = new File(INVENTORY_FILE);
            if (!file.exists()) {
                return initializeDefaultInventory();
            }
            return objectMapper.readValue(file, new TypeReference<List<Item>>() {
            });
        } catch (IOException e) {
            throw new InventoryException(
                    "Failed to read inventory file: " + e.getMessage(),
                    e,
                    InventoryException.ErrorType.FILE_IO_ERROR
            );
        } catch (Exception e) {
            throw new InventoryException(
                    "Error parsing inventory data: " + e.getMessage(),
                    e,
                    InventoryException.ErrorType.JSON_PARSE_ERROR
            );
        }
    }

    public void writeInventory(List<Item> items) {
        if (items == null) {
            throw new InventoryException(
                    "Cannot write null inventory",
                    InventoryException.ErrorType.VALIDATION_ERROR
            );
        }

        try {
            objectMapper.writeValue(new File(INVENTORY_FILE), items);
        } catch (IOException e) {
            throw new InventoryException(
                    "Failed to write inventory file: " + e.getMessage(),
                    e,
                    InventoryException.ErrorType.FILE_IO_ERROR
            );
        }
    }

    private List<Item> initializeDefaultInventory() {
        List<Item> defaultItems = new ArrayList<>();
        defaultItems.add(new Item(ItemType.AGED_BRIE.getPrefix(), 1, 1));
        defaultItems.add(new Item(ItemType.BACKSTAGE_PASS.getPrefix(), -1, 2));
        defaultItems.add(new Item(ItemType.BACKSTAGE_PASS.getPrefix(), 9, 2));
        defaultItems.add(new Item(ItemType.SULFURAS.getPrefix(), 2, 2));
        defaultItems.add(new Item(ItemType.NORMAL.getPrefix(), -1, 55));
        defaultItems.add(new Item(ItemType.NORMAL.getPrefix(), 2, 2));
        defaultItems.add(new Item("INVALID ITEM", 2, 2));
        defaultItems.add(new Item(ItemType.CONJURED.getPrefix(), 2, 2));
        defaultItems.add(new Item(ItemType.CONJURED.getPrefix(), -1, 5));

        writeInventory(defaultItems);
        return defaultItems;
    }
}
