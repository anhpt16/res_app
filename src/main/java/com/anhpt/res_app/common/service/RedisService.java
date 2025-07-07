package com.anhpt.res_app.common.service;

public interface RedisService {
    // Set Object Value (No TTL)
    <T> void set(String key, T value);
    // Set Object Value (TTL)
    <T> void set(String key, T value, long timeoutInSeconds);
    // Get Object (with class)
    <T> T get (String key, Class<T> clazz);
    // Delete key
    boolean delete(String key);
    // Check key exist
    boolean hasKey(String key);
    // Set TTL
    void expire (String key, long timeoutInSeconds);
    // Get TTL
    Long getExpire(String key);
}
