package com.dev.tomato.chat_bot.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.var;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping
public class ChatController {

    private ChatClient openAiChatClient;
    private ChatClient ollamaChatClient;

    public ChatController(OpenAiChatModel openAiChatModel, OllamaChatModel ollamaChatModel){
        this.openAiChatClient = ChatClient.builder(openAiChatModel).build();
        this.ollamaChatClient= ChatClient.builder(ollamaChatModel).build();
        // chatClient= ChatClient.builder(chatModel).build();
    }


    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam(value="query") String query){


        var response= openAiChatClient.prompt(query)
                .call()
                .content();
        return ResponseEntity.ok(response);
    }

}
