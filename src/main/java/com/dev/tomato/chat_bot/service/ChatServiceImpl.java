package com.dev.tomato.chat_bot.service;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.dev.tomato.chat_bot.advisor.TokenPrintAdvisor;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;


@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    @Qualifier("openAiChatClient")
    private final ChatClient openAiChatClient;
    @Qualifier("geminiChatClient")
    private final ChatClient geminiChatClient;
    private final ChatClient ollamChatClient;

    @Value("classpath:prompts/system-message.st")
    private Resource systemMessage;
    @Value("classpath:prompts/user-message.st")
    private Resource userMessage;



    @Override
    public String chat(String query) {
        

        return ollamChatClient
                .prompt()
                .advisors(new TokenPrintAdvisor(), new SafeGuardAdvisor(List.of("game")))
                .system(system -> system.text(this.systemMessage))
                .user(user -> user.text(userMessage).param("concept", query))
                .options(ChatOptions.builder()
                        .maxTokens(300)
                        .build())
                .call()
                .content();
    }

    @Override
    public String chatTemplate() {
        // PromptTemplate strTemplate= PromptTemplate.builder().template("What is {techName}? also tell me about {extraTech}").build();
        
        // String renderedMessage=strTemplate.render(Map.of(
        //     "techName", "Spring",
        //     "extraTech", "Spring Exception"
        // ));

        // Prompt prompt= new Prompt(renderedMessage);


        PromptTemplate userPromptTemplate= PromptTemplate.builder().template("What is {techName}? also tell me about {extraTech}").build();
        var userMessage= userPromptTemplate.createMessage(Map.of(
            "techName", "Spring",
            "extraTech", "Spring Exception"
        ));

        SystemPromptTemplate systemPromptTemplate= SystemPromptTemplate.builder().template("You are a helpful coding assistant. you are an expert in coding").build();
        
        var systemMessage= systemPromptTemplate.createMessage();

        Prompt prompt= new Prompt(systemMessage , userMessage );
        return openAiChatClient
                .prompt(prompt)
                .call() 
                .content();
                
    }

    @Override
    public Flux<String> streamChat(String query) {
        
        return ollamChatClient
                .prompt()
                .system(system-> system.text(systemMessage))
                .user(user -> user.text(userMessage).param("concept",query))
                .options(ChatOptions.builder()
                    .maxTokens(300)
                    .build())
                .stream()
                .content();
    }
}
