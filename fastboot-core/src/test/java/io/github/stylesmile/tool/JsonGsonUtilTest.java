package io.github.stylesmile.tool;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for JsonGsonUtil utility class.
 */
public class JsonGsonUtilTest {

    // Test data class
    static class User {
        private String name;
        private int age;
        private String email;

        public User() {}

        public User(String name, int age, String email) {
            this.name = name;
            this.age = age;
            this.email = email;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return age == user.age &&
                   (name != null ? name.equals(user.name) : user.name == null) &&
                   (email != null ? email.equals(user.email) : user.email == null);
        }
    }

    /**
     * Test: objectToJson with null object
     * Note: Gson converts null to the string "null"
     */
    @Test
    public void testObjectToJsonWithNull() {
        String json = JsonGsonUtil.objectToJson(null);
        // Gson returns "null" string for null objects
        assertEquals("null", json);
    }

    /**
     * Test: objectToJson with simple string
     */
    @Test
    public void testObjectToJsonWithString() {
        String json = JsonGsonUtil.objectToJson("hello");
        assertEquals("\"hello\"", json);
    }

    /**
     * Test: objectToJson with integer
     */
    @Test
    public void testObjectToJsonWithInteger() {
        String json = JsonGsonUtil.objectToJson(42);
        assertEquals("42", json);
    }

    /**
     * Test: objectToJson with User object
     */
    @Test
    public void testObjectToJsonWithObject() {
        User user = new User("张三", 25, "zhangsan@example.com");
        String json = JsonGsonUtil.objectToJson(user);
        assertNotNull(json);
        assertTrue(json.contains("\"name\":\"张三\""));
        assertTrue(json.contains("\"age\":25"));
        assertTrue(json.contains("\"email\":\"zhangsan@example.com\""));
    }

    /**
     * Test: jsonToObject with null string
     */
    @Test
    public void testJsonToObjectWithNull() {
        User user = JsonGsonUtil.jsonToObject(null, User.class);
        assertNull(user);
    }

    /**
     * Test: jsonToObject with valid JSON
     */
    @Test
    public void testJsonToObjectWithValidJson() {
        String json = "{\"name\":\"李四\",\"age\":30,\"email\":\"lisi@example.com\"}";
        User user = JsonGsonUtil.jsonToObject(json, User.class);
        assertNotNull(user);
        assertEquals("李四", user.getName());
        assertEquals(30, user.getAge());
        assertEquals("lisi@example.com", user.getEmail());
    }

    /**
     * Test: jsonToObject round-trip
     */
    @Test
    public void testJsonToObjectRoundTrip() {
        User original = new User("王五", 28, "wangwu@example.com");
        String json = JsonGsonUtil.objectToJson(original);
        User restored = JsonGsonUtil.jsonToObject(json, User.class);
        assertEquals(original.getName(), restored.getName());
        assertEquals(original.getAge(), restored.getAge());
        assertEquals(original.getEmail(), restored.getEmail());
    }

    /**
     * Test: jsonToList with null string
     */
    @Test
    public void testJsonToListWithNull() {
        List<User> list = JsonGsonUtil.jsonToList(null, User.class);
        assertNull(list);
    }

    /**
     * Test: jsonToList with empty array
     */
    @Test
    public void testJsonToListWithEmptyArray() {
        List<User> list = JsonGsonUtil.jsonToList("[]", User.class);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    /**
     * Test: jsonToList with valid JSON array
     * Note: Due to type erasure, this method has limitations with custom objects
     */
    @Test
    public void testJsonToListWithValidJson() {
        String json = "[{\"name\":\"用户1\",\"age\":20,\"email\":\"user1@test.com\"}," +
                      "{\"name\":\"用户2\",\"age\":22,\"email\":\"user2@test.com\"}]";
        // Due to Gson type erasure issue, jsonToList returns List<Map> instead of List<User>
        // This is a known limitation documented in the source code
        List list = JsonGsonUtil.jsonToList(json, User.class);
        assertNotNull(list);
        assertEquals(2, list.size());
        // Elements are actually LinkedTreeMap, not User objects
        assertTrue(list.get(0) instanceof java.util.Map);
    }

    /**
     * Test: jsonToMaps with null string
     */
    @Test
    public void testJsonToMapsWithNull() {
        Map<String, Object> map = JsonGsonUtil.jsonToMaps(null);
        assertNull(map);
    }

    /**
     * Test: jsonToMaps with valid JSON object
     */
    @Test
    public void testJsonToMapsWithValidJson() {
        String json = "{\"key1\":\"value1\",\"key2\":123,\"key3\":true}";
        Map<String, Object> map = JsonGsonUtil.jsonToMaps(json);
        assertNotNull(map);
        assertEquals("value1", map.get("key1"));
        assertEquals(123.0, map.get("key2")); // Gson parses numbers as Double
        assertEquals(true, map.get("key3"));
    }

    /**
     * Test: jsonToListMaps with null string
     */
    @Test
    public void testJsonToListMapsWithNull() {
        List<Map<String, Object>> list = JsonGsonUtil.jsonToListMaps(null);
        assertNull(list);
    }

    /**
     * Test: jsonToListMaps with valid JSON array of objects
     */
    @Test
    public void testJsonToListMapsWithValidJson() {
        String json = "[{\"id\":1,\"name\":\"Item1\"},{\"id\":2,\"name\":\"Item2\"}]";
        List<Map<String, Object>> list = JsonGsonUtil.jsonToListMaps(json);
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals(1.0, list.get(0).get("id"));
        assertEquals("Item1", list.get(0).get("name"));
        assertEquals(2.0, list.get(1).get("id"));
        assertEquals("Item2", list.get(1).get("name"));
    }

    /**
     * Test: Complex nested object serialization
     */
    @Test
    public void testComplexNestedObject() {
        String json = "{\"user\":{\"name\":\"嵌套测试\",\"age\":35},\"scores\":[95,87,92]}";
        Map<String, Object> map = JsonGsonUtil.jsonToMaps(json);
        assertNotNull(map);
        assertNotNull(map.get("user"));
        assertNotNull(map.get("scores"));
    }

    /**
     * Test: Special characters in JSON
     */
    @Test
    public void testSpecialCharacters() {
        User user = new User("测试\"引号\"", 25, "test@example.com");
        String json = JsonGsonUtil.objectToJson(user);
        assertNotNull(json);
        User restored = JsonGsonUtil.jsonToObject(json, User.class);
        assertEquals("测试\"引号\"", restored.getName());
    }

    /**
     * Test: Unicode characters
     */
    @Test
    public void testUnicodeCharacters() {
        String json = "{\"name\":\"日本語テスト\",\"emoji\":\"😀🎉\"}";
        Map<String, Object> map = JsonGsonUtil.jsonToMaps(json);
        assertNotNull(map);
        assertEquals("日本語テスト", map.get("name"));
        assertEquals("😀🎉", map.get("emoji"));
    }
}
