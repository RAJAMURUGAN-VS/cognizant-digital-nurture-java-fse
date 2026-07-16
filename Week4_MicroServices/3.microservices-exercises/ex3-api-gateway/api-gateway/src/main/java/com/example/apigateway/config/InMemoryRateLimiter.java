package com.example.apigateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A simple in-memory token-bucket RateLimiter, used here purely so this
 * gateway can demonstrate/rate-limit WITHOUT requiring a running Redis
 * instance. Swap in Spring Cloud Gateway's built-in RedisRateLimiter for a
 * production, multi-instance-safe deployment (uncomment the redis dependency
 * in pom.xml and reference "redis-rate-limiter" in application.yml instead).
 *
 * Config per route key (set via RouteLocator or application.yml args):
 *   replenishRate — tokens added back per second
 *   burstCapacity — bucket size / max burst
 */
@Component
public class InMemoryRateLimiter implements RateLimiter<InMemoryRateLimiter.Config> {

    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();
    private Config defaultConfig = new Config(5, 10); // 5 req/sec, burst of 10

    @Override
    public Mono<Response> isAllowed(String routeId, String id) {
        Config config = defaultConfig;
        Bucket bucket = buckets.computeIfAbsent(id, k -> new Bucket(config.getBurstCapacity()));
        bucket.refill(config.getReplenishRate(), config.getBurstCapacity());

        boolean allowed = bucket.tryConsume();
        int remaining = bucket.tokens.get();

        Response response = new Response(allowed, java.util.Map.of(
                "X-RateLimit-Remaining", String.valueOf(Math.max(remaining, 0)),
                "X-RateLimit-Burst-Capacity", String.valueOf(config.getBurstCapacity()),
                "X-RateLimit-Replenish-Rate", String.valueOf(config.getReplenishRate())
        ));
        return Mono.just(response);
    }

    @Override
    public Config newConfig() {
        return new Config(5, 10);
    }

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }

    @Override
    public Config getDefaultConfig() {
        return defaultConfig;
    }

    public static class Config {
        private int replenishRate;
        private int burstCapacity;

        public Config() {}

        public Config(int replenishRate, int burstCapacity) {
            this.replenishRate = replenishRate;
            this.burstCapacity = burstCapacity;
        }

        public int getReplenishRate() { return replenishRate; }
        public void setReplenishRate(int replenishRate) { this.replenishRate = replenishRate; }
        public int getBurstCapacity() { return burstCapacity; }
        public void setBurstCapacity(int burstCapacity) { this.burstCapacity = burstCapacity; }
    }

    private static class Bucket {
        private final AtomicInteger tokens;
        private final AtomicLong lastRefillTimestamp = new AtomicLong(System.currentTimeMillis());

        Bucket(int initialTokens) {
            this.tokens = new AtomicInteger(initialTokens);
        }

        synchronized void refill(int replenishRate, int burstCapacity) {
            long now = System.currentTimeMillis();
            long elapsedMs = now - lastRefillTimestamp.get();
            int tokensToAdd = (int) ((elapsedMs / 1000.0) * replenishRate);
            if (tokensToAdd > 0) {
                tokens.set(Math.min(burstCapacity, tokens.get() + tokensToAdd));
                lastRefillTimestamp.set(now);
            }
        }

        synchronized boolean tryConsume() {
            if (tokens.get() > 0) {
                tokens.decrementAndGet();
                return true;
            }
            return false;
        }
    }
}
