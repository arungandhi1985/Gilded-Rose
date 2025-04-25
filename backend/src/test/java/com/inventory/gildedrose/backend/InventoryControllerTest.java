package com.inventory.gildedrose.backend;

import com.inventory.gildedrose.controller.InventoryController;
import com.inventory.gildedrose.exception.InventoryException;
import com.inventory.gildedrose.model.Item;
import com.inventory.gildedrose.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class InventoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InventoryService inventoryService;

    @BeforeEach
    public void setUp() {
        InventoryController inventoryController = new InventoryController(inventoryService);
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController)
                .setControllerAdvice(new com.inventory.gildedrose.exception.GlobalExceptionHandler())
                .build();
    }

    @Test
    public void testGetInventory_Success() throws Exception {
        // Arrange
        List<Item> items = Arrays.asList(
                new Item("Aged Brie", 1, 1),
                new Item("Normal Item", 2, 2)
        );
        when(inventoryService.getInventory()).thenReturn(items);

        // Act & Assert
        mockMvc.perform(get("/api/inventory"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Aged Brie"))
                .andExpect(jsonPath("$[1].name").value("Normal Item"));
    }

    @Test
    public void testGetInventory_FileIoError() throws Exception {
        // Arrange
        when(inventoryService.getInventory()).thenThrow(
                new InventoryException("File read error", InventoryException.ErrorType.FILE_IO_ERROR)
        );

        // Act & Assert
        mockMvc.perform(get("/api/inventory"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("File read error"))
                .andExpect(jsonPath("$.details").value("FILE_IO_ERROR"));
    }

    @Test
    public void testUpdateInventory_Success() throws Exception {
        // Arrange
        List<Item> inputItems = Arrays.asList(
                new Item("Aged Brie", 1, 1),
                new Item("Normal Item", 2, 2)
        );
        List<Item> updatedItems = Arrays.asList(
                new Item("Aged Brie", 0, 2),
                new Item("Normal Item", 1, 1)
        );
        when(inventoryService.updateInventory(any())).thenReturn(updatedItems);

        // Act & Assert
        mockMvc.perform(post("/api/inventory/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"name\":\"Aged Brie\",\"sellIn\":1,\"quality\":1},{\"name\":\"Normal Item\",\"sellIn\":2,\"quality\":2}]"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].quality").value(2))
                .andExpect(jsonPath("$[1].sellIn").value(1));
    }

    @Test
    public void testUpdateInventory_ValidationError() throws Exception {
        // Arrange
        when(inventoryService.updateInventory(any())).thenThrow(
                new InventoryException("No inventory items found", InventoryException.ErrorType.VALIDATION_ERROR)
        );

        // Act & Assert
        mockMvc.perform(post("/api/inventory/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[]"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No inventory items found"))
                .andExpect(jsonPath("$.details").value("VALIDATION_ERROR"));
    }

    @Test
    public void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/inventory/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Service is running"));
    }
}