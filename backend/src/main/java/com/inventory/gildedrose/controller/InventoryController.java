package com.inventory.gildedrose.controller;

import com.inventory.gildedrose.model.Item;
import com.inventory.gildedrose.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*") // For development purposes
public class InventoryController {
    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<Item>> getInventory() {
        List<Item> inventory = inventoryService.getInventory();
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/update")
    public ResponseEntity<List<Item>> updateInventory(@RequestBody List<Item> items) {
        List<Item> updatedInventory = inventoryService.updateInventory(items);
        return ResponseEntity.ok(updatedInventory);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is running");
    }
}