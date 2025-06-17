package com.ureca.yoajungserver.config; // 패키지 경로는 맞게 수정하세요.

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 동시에 실행시킬 스레드의 수 (최소 3 이상으로 설정)
        executor.setMaxPoolSize(20); // 최대 스레드 수
        executor.setQueueCapacity(40); // 대기 큐의 크기
        executor.setThreadNamePrefix("yoajung-async-"); // 스레드 이름 접두사
        executor.initialize();
        return executor;
    }
}