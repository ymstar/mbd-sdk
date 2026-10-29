package com.mbdpay;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedHashMap;
import java.util.Map;

class SignTest {

    @Test
    void testSignProducesValidMd5() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("app_id", "194024590982810");
        data.put("amount_total", 100);
        data.put("channel", "h5");
        data.put("description", "312");
        String key = "920ee68b4e16df01d0cd6b2ca161195d";

        String result = Sign.sign(data, key);
        assertEquals(32, result.length());
        assertTrue(result.matches("[0-9a-f]{32}"));
    }

    @Test
    void testSignOrderIndependent() {
        String key = "test_key";

        Map<String, Object> data1 = new LinkedHashMap<>();
        data1.put("a", "1");
        data1.put("b", "2");
        data1.put("c", "3");

        Map<String, Object> data2 = new LinkedHashMap<>();
        data2.put("c", "3");
        data2.put("b", "2");
        data2.put("a", "1");

        assertEquals(Sign.sign(data1, key), Sign.sign(data2, key));
    }
}

class ClientTest {

    @Test
    void testGetOpenidRedirectUrl() {
        Client client = new Client("test_app_id", "test_app_key");
        String url = client.getOpenidRedirectUrl("https://example.com/cb");
        assertTrue(url.startsWith("https://mbd.pub/openid?"));
        assertTrue(url.contains("app_id=test_app_id"));
    }
}
