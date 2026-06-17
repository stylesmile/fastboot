package io.github.stylesmile.openai;

import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import io.github.stylesmile.tool.PropertyUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static io.github.stylesmile.test.util.AssertUtils.assertNotNull;
import static org.junit.Assert.*;

/**
 * Unit tests for OpenAIService.
 */
public class OpenAIServiceTest {

    private Properties oldProps;
    private OpenAIService openAIService;

    @Before
    public void setUp() {
        oldProps = PropertyUtil.props;
        PropertyUtil.props = new Properties();
        OpenAIConfig config = OpenAIConfig.load();
        openAIService = new OpenAIService(config);
    }

    @After
    public void tearDown() {
        PropertyUtil.props = oldProps;
    }

    // ========== Constructor tests ==========

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorRejectsNullConfig() {
        new OpenAIService(null);
    }

    // ========== Message creation tests ==========

    @Test
    public void testCreateMessage() {
        ChatMessage message = openAIService.createMessage("user", "Hello");

        assertNotNull("Message should not be null", message);
        assertEquals("user", message.getRole());
        assertEquals("Hello", message.getContent());
    }

    @Test
    public void testCreateUserMessage() {
        ChatMessage message = openAIService.createUserMessage("What is AI?");

        assertNotNull("User message should not be null", message);
        assertEquals(ChatMessageRole.USER.value(), message.getRole());
        assertEquals("What is AI?", message.getContent());
    }

    @Test
    public void testCreateSystemMessage() {
        ChatMessage message = openAIService.createSystemMessage("You are a helpful assistant.");

        assertNotNull("System message should not be null", message);
        assertEquals(ChatMessageRole.SYSTEM.value(), message.getRole());
        assertEquals("You are a helpful assistant.", message.getContent());
    }

    @Test
    public void testCreateAssistantMessage() {
        ChatMessage message = openAIService.createAssistantMessage("I can help with that.");

        assertNotNull("Assistant message should not be null", message);
        assertEquals(ChatMessageRole.ASSISTANT.value(), message.getRole());
        assertEquals("I can help with that.", message.getContent());
    }

    @Test
    public void testCreateMessageWithNullContent() {
        ChatMessage message = openAIService.createMessage("user", null);

        assertNotNull("Message should not be null even with null content", message);
        assertEquals("user", message.getRole());
    }

    @Test
    public void testCreateMessageWithEmptyContent() {
        ChatMessage message = openAIService.createMessage("system", "");

        assertNotNull("Message should not be null with empty content", message);
        assertEquals("system", message.getRole());
        assertEquals("", message.getContent());
    }

    // ========== Error handling tests (no API key configured) ==========

    @Test
    public void testChatWithoutApiKeyReturnsError() {
        String result = openAIService.chat("Hello");

        assertNotNull("Result should not be null", result);
        assertTrue("Result should contain error message",
                result.startsWith("Error:") || result.contains("Error"));
    }

    @Test
    public void testChatWithSystemPromptWithoutApiKeyReturnsError() {
        String result = openAIService.chat("Hello", "You are a helper.");

        assertNotNull("Result should not be null", result);
        assertTrue("Result should contain error message",
                result.startsWith("Error:") || result.contains("Error"));
    }

    @Test
    public void testChatWithNullSystemPromptWithoutApiKeyReturnsError() {
        String result = openAIService.chat("Hello", null);

        assertNotNull("Result should not be null", result);
        assertTrue("Result should contain error message",
                result.startsWith("Error:") || result.contains("Error"));
    }

    @Test
    public void testChatWithEmptySystemPromptWithoutApiKeyReturnsError() {
        String result = openAIService.chat("Hello", "  ");

        assertNotNull("Result should not be null", result);
        assertTrue("Result should contain error message",
                result.startsWith("Error:") || result.contains("Error"));
    }

    @Test
    public void testChatWithMessagesWithoutApiKeyReturnsError() {
        List<ChatMessage> messages = Arrays.asList(
                new ChatMessage(ChatMessageRole.USER.value(), "Hello")
        );

        String result = openAIService.chatWithMessages(messages);

        assertNotNull("Result should not be null", result);
        assertTrue("Result should contain error message",
                result.startsWith("Error:") || result.contains("Error"));
    }

    @Test
    public void testCompleteWithoutApiKeyReturnsError() {
        String result = openAIService.complete("Once upon a time");

        assertNotNull("Result should not be null", result);
        assertTrue("Result should contain error message",
                result.startsWith("Error:") || result.contains("Error"));
    }

    @Test
    public void testGetEmbeddingsWithoutApiKeyReturnsEmptyList() {
        List<String> texts = Arrays.asList("Hello", "World");

        List<List<Double>> result = openAIService.getEmbeddings(texts);

        assertNotNull("Result should not be null", result);
        assertTrue("Result should be empty when API key is not configured", result.isEmpty());
    }
}
