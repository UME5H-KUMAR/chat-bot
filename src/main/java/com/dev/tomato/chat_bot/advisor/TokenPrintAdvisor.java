package com.dev.tomato.chat_bot.advisor;

import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;

import reactor.core.publisher.Flux;

public class TokenPrintAdvisor implements CallAdvisor, StreamAdvisor {

    private final Logger logger = LoggerFactory.getLogger(TokenPrintAdvisor.class);

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        logRequest(chatClientRequest);

        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);

        logTokenUsage(chatClientResponse);

        return chatClientResponse;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest,
            StreamAdvisorChain streamAdvisorChain) {

        logRequest(chatClientRequest);

        return Flux.defer(() -> {
            AtomicReference<ChatClientResponse> lastResponse = new AtomicReference<>();

            return streamAdvisorChain.nextStream(chatClientRequest)
                    .doOnNext(lastResponse::set)
                    .doOnComplete(() -> logStreamCompletion(lastResponse.get()))
                    .doOnError(error -> logger.error("Stream error: ", error));
        });
    }

    private void logRequest(ChatClientRequest request) {
        logger.info("Request: {}", request.prompt().getContents());
    }

    private void logTokenUsage(ChatClientResponse response) {
        var metadata = response.chatResponse().getMetadata();
        if (metadata != null && metadata.getUsage() != null) {
            var usage = metadata.getUsage();
            logger.info("Prompt Tokens: {}", usage.getPromptTokens());
            logger.info("Completion Tokens: {}", usage.getCompletionTokens());
            logger.info("Total Tokens: {}", usage.getTotalTokens());
        }
    }

    private void logStreamCompletion(ChatClientResponse lastResponse) {
        logger.info("Stream completed");
        if (lastResponse != null) {
            logTokenUsage(lastResponse);
        } else {
            logger.info("Token usage metadata not available");
        }
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
