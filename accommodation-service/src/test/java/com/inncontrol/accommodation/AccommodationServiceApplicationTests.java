package com.inncontrol.accommodation;

import com.inncontrol.accommodation.application.RoomService;
import com.inncontrol.accommodation.domain.Room;
import com.inncontrol.accommodation.domain.RoomRepository;
import com.inncontrol.accommodation.dto.RoomDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccommodationServiceApplicationTests {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test CREATE operation
    @Test
    void registerRoom_WithValidData() {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setRoomNumber(101);
        roomDTO.setRoomType("Deluxe");

        Room room = new Room();
        room.setRoomNumber(101);
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        Room savedRoom = roomService.registerRoom(roomDTO);

        assertNotNull(savedRoom);
        assertEquals(101, savedRoom.getRoomNumber());
    }

    // Test READ operation
    @Test
    void findRoomByRoomNumber() {
        Room room = new Room();
        room.setRoomNumber(101);
        when(roomRepository.findByRoomNumber(101)).thenReturn(Optional.of(room));

        Optional<Room> foundRoom = roomService.findRoomByRoomNumber(101);

        assertTrue(foundRoom.isPresent());
        assertEquals(101, foundRoom.get().getRoomNumber());
    }

    // Test UPDATE operation
    @Test
    void updateRoom() {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setRoomNumber(101);
        roomDTO.setRoomType("Deluxe");

        Room existingRoom = new Room();
        existingRoom.setRoomNumber(101);

        when(roomRepository.findByRoomNumber(101)).thenReturn(Optional.of(existingRoom));
        when(roomRepository.save(any(Room.class))).thenReturn(existingRoom);

        Optional<Room> updatedRoom = roomService.updateRoom(101, roomDTO);

        assertTrue(updatedRoom.isPresent());
        assertEquals("DELUXE", updatedRoom.get().getRoomType().name());
    }

    // Test GET ALL operation
    @Test
    void getAllRooms() {
        Room room1 = new Room();
        room1.setRoomNumber(101);
        Room room2 = new Room();
        room2.setRoomNumber(102);

        when(roomRepository.findAll()).thenReturn(Arrays.asList(room1, room2));

        List<Room> rooms = roomService.getAllRooms();

        assertNotNull(rooms);
        assertEquals(2, rooms.size());
        assertEquals(101, rooms.get(0).getRoomNumber());
        assertEquals(102, rooms.get(1).getRoomNumber());
    }

    @Test
    void getRoomsByRoomNumber() {
        Room room1 = new Room();
        room1.setRoomNumber(101);
        Room room2 = new Room();
        room2.setRoomNumber(101);

        when(roomRepository.findByRoomNumber(101)).thenReturn(Optional.of(room1), Optional.of(room2));

        Optional<Room> foundRoom1 = roomService.findRoomByRoomNumber(101);
        Optional<Room> foundRoom2 = roomService.findRoomByRoomNumber(101);

        assertTrue(foundRoom1.isPresent());
        assertTrue(foundRoom2.isPresent());
        assertEquals(101, foundRoom1.get().getRoomNumber());
        assertEquals(101, foundRoom2.get().getRoomNumber());
    }
}