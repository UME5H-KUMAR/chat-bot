package com.dev.tomato.chat_bot.service;

import reactor.core.publisher.Flux;

public interface ChatService {

    String chat(String query, String userId);
    
    public String chatTemplate();

    Flux<String> streamChat(String query, String userId);
}
