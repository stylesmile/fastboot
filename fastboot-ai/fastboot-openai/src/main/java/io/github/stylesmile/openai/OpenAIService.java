package io.github.stylesmile.openai;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import io.github.stylesmile.ioc.annotation.Component;
import io.github.stylesmile.ioc.annotation.Autowired;

import java.util.*;

/**
 * OpenAI服务类
 * 提供OpenAI API调用功能
 */
@Component
public class OpenAIService {
    
    @Autowired
    private OpenAIConfig openAIConfig;
    
    /**
     * 聊天对话
     * @param message 用户消息
     * @return AI回复
     */
    public String chat(String message) {
        return chat(message, null);
    }
    
    /**
     * 聊天对话
     * @param message 用户消息
     * @param systemPrompt 系统提示词
     * @return AI回复
     */
    public String chat(String message, String systemPrompt) {
        try {
            List<ChatMessage> messages = new ArrayList<>();
            
            // 添加系统提示词
            if (systemPrompt != null && !systemPrompt.trim().isEmpty()) {
                messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(), systemPrompt));
            }
            
            // 添加用户消息
            messages.add(new ChatMessage(ChatMessageRole.USER.value(), message));
            
            return chatWithMessages(messages);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
    
    /**
     * 多轮对话
     * @param messages 消息列表
     * @return AI回复
     */
    public String chatWithMessages(List<ChatMessage> messages) {
        try {
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                    .model(openAIConfig.getDefaultModel())
                    .messages(messages)
                    .maxTokens(openAIConfig.getMaxTokens())
                    .temperature(openAIConfig.getTemperature())
                    .build();
                    
            ChatCompletionResult result = openAIConfig.getOpenAiService().createChatCompletion(request);
            
            if (result.getChoices() != null && !result.getChoices().isEmpty()) {
                return result.getChoices().get(0).getMessage().getContent();
            }
            
            return "No response from OpenAI";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
    
    /**
     * 文本补全
     * @param prompt 提示文本
     * @return 补全结果
     */
    public String complete(String prompt) {
        try {
            CompletionRequest request = CompletionRequest.builder()
                    .model("text-davinci-003")
                    .prompt(prompt)
                    .maxTokens(openAIConfig.getMaxTokens())
                    .temperature(openAIConfig.getTemperature())
                    .build();
                    
            CompletionResult result = openAIConfig.getOpenAiService().createCompletion(request);
            
            if (result.getChoices() != null && !result.getChoices().isEmpty()) {
                return result.getChoices().get(0).getText();
            }
            
            return "No completion from OpenAI";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
    
    /**
     * 获取文本嵌入向量
     * @param text 输入文本
     * @return 嵌入向量
     */
    public List<Double> getEmbedding(String text) {
        return getEmbeddings(Arrays.asList(text)).get(0);
    }
    
    /**
     * 批量获取文本嵌入向量
     * @param texts 输入文本列表
     * @return 嵌入向量列表
     */
    public List<List<Double>> getEmbeddings(List<String> texts) {
        try {
            EmbeddingRequest request = EmbeddingRequest.builder()
                    .model("text-embedding-ada-002")
                    .input(texts)
                    .build();
                    
            EmbeddingResult result = openAIConfig.getOpenAiService().createEmbeddings(request);
            
            List<List<Double>> embeddings = new ArrayList<>();
            if (result.getData() != null) {
                result.getData().forEach(embedding -> {
                    embeddings.add(embedding.getEmbedding());
                });
            }
            
            return embeddings;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * 创建对话消息
     * @param role 角色（system, user, assistant）
     * @param content 消息内容
     * @return ChatMessage
     */
    public ChatMessage createMessage(String role, String content) {
        return new ChatMessage(role, content);
    }
    
    /**
     * 创建用户消息
     * @param content 消息内容
     * @return ChatMessage
     */
    public ChatMessage createUserMessage(String content) {
        return new ChatMessage(ChatMessageRole.USER.value(), content);
    }
    
    /**
     * 创建系统消息
     * @param content 消息内容
     * @return ChatMessage
     */
    public ChatMessage createSystemMessage(String content) {
        return new ChatMessage(ChatMessageRole.SYSTEM.value(), content);
    }
    
    /**
     * 创建助手消息
     * @param content 消息内容
     * @return ChatMessage
     */
    public ChatMessage createAssistantMessage(String content) {
        return new ChatMessage(ChatMessageRole.ASSISTANT.value(), content);
    }
}