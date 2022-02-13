package com.free.commit.configuration.scheduler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Configuration
public class SchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler
                = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize( 2 );
        threadPoolTaskScheduler.setThreadNamePrefix(
                "ThreadPoolTaskScheduler" );
        return threadPoolTaskScheduler;
    }
}
