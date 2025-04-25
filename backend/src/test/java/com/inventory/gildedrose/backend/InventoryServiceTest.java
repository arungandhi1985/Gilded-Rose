package com.inventory.gildedrose.backend;

import com.inventory.gildedrose.model.Item;
import com.inventory.gildedrose.exception.InventoryException;
import com.inventory.gildedrose.service.InventoryService;
import com.inventory.gildedrose.service.InventoryServiceImpl;
import com.inventory.gildedrose.service.updater.UpdateStrategyFactory;
import com.inventory.gildedrose.util.JsonFileHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private JsonFileHandler jsonFileHandler;

    @Mock
    private UpdateStrategyFactory strategyFactory;

    private InventoryService inventoryService;

    @BeforeEach
    public void setUp() {
        inventoryService = new InventoryServiceImpl(jsonFileHandler, strategyFactory);
    }

    @Test
    public void testGetInventory() {
        // Arrange
        List<Item> expectedItems = Arrays.asList(
                new Item("Aged Brie", 1, 1),
                new Item("Normal Item", 2, 2)
        );

        when(jsonFileHandler.readInventory()).thenReturn(expectedItems);

        // Act
        List<Item> result = inventoryService.getInventory();

        // Assert
        assertEquals(expectedItems, result);
        verify(jsonFileHandler).readInventory();
    }

    @Test
    public void testUpdateInventory() {
        // Arrange - Setup test data
        List<Item> inputItems = Arrays.asList(
                new Item("Aged Brie", 1, 1),
                new Item("Normal Item", 2, 2),
                new Item("INVALID ITEM", 2, 2)
        );

        // Setup strategy factory to return null for invalid item
        when(strategyFactory.getStrategy("Aged Brie")).thenReturn(mock(com.inventory.gildedrose.service.updater.ItemUpdateStrategy.class));
        when(strategyFactory.getStrategy("Normal Item")).thenReturn(mock(com.inventory.gildedrose.service.updater.ItemUpdateStrategy.class));
        when(strategyFactory.getStrategy("INVALID ITEM")).thenReturn(null);

        // Act
        List<Item> result = inventoryService.updateInventory(inputItems);

        // Assert
        assertEquals(3, result.size());
        assertEquals("NO SUCH ITEM", result.get(2).getName());

        verify(jsonFileHandler).writeInventory(any());
        verify(strategyFactory, times(3)).getStrategy(any());
    }

    @Test
    public void testUpdateInventoryWithNullInput() {
        // Act & Assert
        assertThrows(InventoryException.class, () -> {
            inventoryService.updateInventory(null);
        });
    }

    @Test
    public void testUpdateInventoryWithEmptyList() {
        // Act & Assert
        assertThrows(InventoryException.class, () -> {
            inventoryService.updateInventory(new ArrayList<>());
        });
    }

    @Test
    public void testSaveInventory() {
        // Arrange
        List<Item> items = Arrays.asList(
                new Item("Aged Brie", 1, 1),
                new Item("Normal Item", 2, 2)
        );

        // Act
        inventoryService.saveInventory(items);

        // Assert
        verify(jsonFileHandler).writeInventory(items);
    }
}