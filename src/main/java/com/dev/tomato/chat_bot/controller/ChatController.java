package com.dev.tomato.chat_bot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.tomato.chat_bot.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.var;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // public ChatController(@Qualifier("openAiChatClient") ChatClient openAiChatClient, 
    //                     @Qualifier("ollamaChatClient") ChatClient ollamaChatClient,
    //                     @Qualifier("geminiChatClient") ChatClient geminiChatClient){
    //     this.openAiChatClient = openAiChatClient;
    //     this.ollamaChatClient = ollamaChatClient;
    //     this.geminiChatClient = geminiChatClient;
    // }


    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam(value="query") String query){
        var response= chatService.chat(query);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/stream-chat")
    public ResponseEntity<Flux<String>> streamChat(@RequestParam("query") String query) {
        return ResponseEntity.ok(chatService.streamChat(query));
    }
    

}
