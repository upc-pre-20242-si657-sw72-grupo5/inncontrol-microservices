package com.inncontrol.inventory;

import com.inncontrol.inventory.application.commandservices.InventoryCommandServiceImpl;
import com.inncontrol.inventory.domain.model.aggregates.Inventory;
import com.inncontrol.inventory.domain.model.commands.CreateItemsCommand;
import com.inncontrol.inventory.domain.model.commands.DeleteItemsCommand;
import com.inncontrol.inventory.domain.model.commands.UpdateInventoryCommand;
import com.inncontrol.inventory.infrastructure.persistence.jpa.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class InventoryServiceTest {

    @Mock
    private ItemRepository inventoryRepository;

    @InjectMocks
    private InventoryCommandServiceImpl inventoryCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleCreateItemsCommand_WithValidData() {
        CreateItemsCommand createItemsCommand = new CreateItemsCommand("itemName", "description", 10, "brand");
        Inventory inventory = new Inventory(createItemsCommand);

        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        Optional<Inventory> result = inventoryCommandService.handle(createItemsCommand);

        assertTrue(result.isPresent());
        assertEquals("itemName", result.get().getProductTitle());
        assertEquals("brand", result.get().getBrand());
        assertEquals(10, result.get().getProductQuantity());
        assertEquals("description", result.get().getProductDescription());
    }

    @Test
    void handleUpdateInventoryCommand_WithValidData() {
        Long inventoryId = 1L;
        UpdateInventoryCommand updateInventoryCommand = new UpdateInventoryCommand(inventoryId, "newItemName", "newDescription", 20, "newBrand");
        Inventory existingInventory = new Inventory(new CreateItemsCommand("itemName", "description", 10, "brand"));
        existingInventory.setId(inventoryId);

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(existingInventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(existingInventory.updateInformation("newItemName", "newDescription", 20, "newBrand"));

        Optional<Inventory> result = inventoryCommandService.handle(updateInventoryCommand);

        assertTrue(result.isPresent());
        assertEquals("newItemName", result.get().getProductTitle());
        assertEquals("newBrand", result.get().getBrand());
        assertEquals(20, result.get().getProductQuantity());
        assertEquals("newDescription", result.get().getProductDescription());
    }
}