package com.queue;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Arrays;

/**
 * Lua 脚本包装（书中用 Spring Data Redis 的 RedisScript，此处自行封装）。
 *
 * <p>持有脚本原文与 sha1（真实部署可用 EVALSHA 避免重复传输）；
 * 与书中一致，所有参数都通过 KEYS 传入。</p>
 */
public class EventQueueScript {

    private final String script;
    private final String sha1;

    public EventQueueScript(String script) {
        this.script = script;
        this.sha1 = DigestUtils.sha1Hex(script);
    }

    public Object exec(QueueRedis queueRedis, String... keys) {
        return queueRedis.eval(script, Arrays.asList(keys));
    }

    public String getScript() {
        return script;
    }

    public String getSha1() {
        return sha1;
    }
}
