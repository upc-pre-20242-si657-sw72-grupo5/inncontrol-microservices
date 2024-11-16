package com.inncontrol.communications;

import com.inncontrol.communications.application.MessageService;
import com.inncontrol.communications.domain.Message;
import com.inncontrol.communications.domain.MessageRepository;
import com.inncontrol.communications.dto.MessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class CommunicationsServiceApplicationTests {

	@Mock
	private MessageRepository messageRepository;

	@InjectMocks
	private MessageService messageService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createMessage_WithValidData() {
		MessageDto messageDto = new MessageDto();
		messageDto.setSender(1L);
		messageDto.setReceiver(2L);
		messageDto.setStatus("SEND");
		messageDto.setContent("Hello Pedro");

		when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
			Message savedMessage = invocation.getArgument(0);
			savedMessage.setId(1L); // Simula el ID generado
			return savedMessage;
		});


		Message savedMessage = messageService.createMessage(messageDto);

		assertNotNull(savedMessage);
		assertEquals(1L, savedMessage.getId());
	}

	@Test
	void getAllMessages() {
		// Arrange
		Message message = new Message();
		message.setId(1L);

		when(messageRepository.findAll()).thenReturn(List.of(message));

		// Act
		List<Message> foundMessages = messageService.getAllMessages();

		// Assert
		assertNotNull(foundMessages);
		assertEquals(1, foundMessages.size());
		assertEquals(1L, foundMessages.get(0).getId());
	}
	@Test
	void updateMessage_WithValidData() {
		MessageDto messageDto = new MessageDto();
		messageDto.setSender(3L);
		messageDto.setReceiver(4L);
		messageDto.setStatus("SEND");
		messageDto.setContent("Hello PABLO");

		Message message = new Message();
		message.setId(1L);

		when(messageRepository.findById(1L)).thenReturn(Optional.of(message));
		when(messageRepository.save(message)).thenReturn(message);

		Message updatedMessage = messageService.updateMessage(1L, messageDto).get();

		assertNotNull(updatedMessage);
		assertEquals(1L, updatedMessage.getId());
	}


	@Test
	void deleteMessage() {
		// Act
		messageService.deleteMessage(1L);

		// Assert
		verify(messageRepository).deleteById(1L);
	}
}
