package com.queue;

/**
 * 事件处理器接口（书中 EventHandler，p307）。
 *
 * <p>实现模式：</p>
 * <pre>
 * public void onEvent(String key, String eventType, EventQueue queue) {
 *     try {
 *         doBusiness(key);
 *         queue.success(key);
 *     } catch (Exception e) {
 *         queue.fail(key);
 *     }
 * }
 * </pre>
 */
public interface EventHandler {

    void onEvent(String key, String eventType, EventQueue queue);
}
