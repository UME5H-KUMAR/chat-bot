package com.dev.tomato.chat_bot.config;

import java.util.List;

import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.dev.tomato.chat_bot.advisor.TokenPrintAdvisor;

@Configuration
@EnableScheduling
public class AppConfig {

    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository){
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(10)
                .build();
    }

    Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public ChatClient chatClient(OllamaChatModel ollamaChatModel, ChatMemory chatMemory) {
        

        this.logger.info("ChatMemory Implementation class: "+ chatMemory.getClass().getName());
        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        return ChatClient.builder(ollamaChatModel)
            .defaultAdvisors( messageChatMemoryAdvisor , new TokenPrintAdvisor(), new SafeGuardAdvisor(List.of("games")))
            .defaultOptions(OllamaChatOptions.builder()
                .model("llama3.2:3b")
                .temperature(0.3)
                .maxTokens(300)
                .build())
            .build();
    }

    // @Bean(name = "openAiChatClient")
    // public ChatClient openAiChatClient(OpenAiChatModel openAiChatModel){
    //     return ChatClient.builder(openAiChatModel).build();
    // }

    // @Bean(name = "ollamaChatClient")
    // @Primary
    // public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel){
    //     return ChatClient.builder(ollamaChatModel).build();
    // }

    // @Bean(name= "geminiChatClient")
    // public ChatClient geminiChatClient(GoogleGenAiChatModel geminiChatModel){
    //     return ChatClient.builder(geminiChatModel).build();
    // }
}
