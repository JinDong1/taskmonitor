package com.ojd.tsl.core.strategy;

import com.ojd.tsl.model.ScheduledSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ScheduledFuture;

public interface ScheduleStrategy {
    ScheduledFuture<?> schedule(ThreadPoolTaskScheduler threadPoolTaskScheduler, ScheduledSource scheduledSource, Runnable runnable);
}
