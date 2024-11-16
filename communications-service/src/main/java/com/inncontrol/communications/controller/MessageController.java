package com.inncontrol.communications.controller;

import com.inncontrol.communications.application.MessageService;
import com.inncontrol.communications.domain.Message;
import com.inncontrol.communications.dto.MessageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/messages")
@Tag(name = "Messages Management", description = "Operations pertaining to messages management")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Operation(summary = "Create a new message")
    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody MessageDto messageDto) {
        Message createdMessage = messageService.createMessage(messageDto);
        return ResponseEntity.ok(createdMessage);
    }

    @Operation(summary = "Get all messages")
    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        return new ResponseEntity<>(messageService.getAllMessages(), HttpStatus.OK);
    }

    @Operation(summary = "Update message")
    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable Long id, @RequestBody MessageDto messageDto) {
        return messageService.updateMessage(id, messageDto)
                .map(updatedMessage -> new ResponseEntity<>(updatedMessage, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Delete messages by receiver")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok(Map.of("message", "Question with given id successfully deleted"));
    }
}
