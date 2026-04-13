package com.dev.tomato.chat_bot.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping
public class ChatController {

    private ChatClient chatClient;

    public ChatController(ChatClient.Builder builder){
        chatClient= builder.build();
    }


    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam(value="q") String q){


        String response= chatClient.prompt(q)
                .call()
                .content();
        return ResponseEntity.ok(response);
    }

}
