package com.week.gdsc.service;

import org.mockito.Mockito;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class MockHttpServletRequest {
    private HttpServletRequest request;
    private Map<String, Object> attributes;

    public MockHttpServletRequest() {
        this.request = Mockito.mock(HttpServletRequest.class);
        this.attributes = new HashMap<>();
    }

    public void setAttribute(String attributeName, Object attributeValue) {
        attributes.put(attributeName, attributeValue);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void applyAttributes() {
        Mockito.when(request.getAttribute(Mockito.anyString())).thenAnswer(invocation -> {
            String attributeName = invocation.getArgument(0);
            return attributes.get(attributeName);
        });
    }
}

