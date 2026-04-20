package com.dev.tomato.chat_bot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dev.tomato.chat_bot.service.ChatService;

@SpringBootTest
class ChatBotApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private ChatService chatService;
	@Test
	public void testTemplateRender(){
		System.out.println("Testing template render");
		var output = this.chatService.chatTemplate();

		System.out.println(output);
	}
 
}
