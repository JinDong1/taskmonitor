package com.ojd.tsl.core;

import com.ojd.tsl.core.strategy.CronStrategy;
import com.ojd.tsl.core.strategy.FixedDelayStrategy;
import com.ojd.tsl.core.strategy.FixedRateStrategy;
import com.ojd.tsl.core.strategy.ScheduleStrategy;
import com.ojd.tsl.enums.ScheduledType;
import com.ojd.tsl.exception.SuperScheduledException;
import com.ojd.tsl.model.ScheduledSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class ScheduledFutureFactory {
    private static Map<ScheduledType, ScheduleStrategy> strategyCache = new HashMap<>(3);

    /**
     * 策略启动定时任务
     *
     * @param threadPoolTaskScheduler 定时任务线程池
     * @param scheduledSource         定时任务源信息
     * @param runnable                执行逻辑
     */
    public static ScheduledFuture<?> create(ThreadPoolTaskScheduler threadPoolTaskScheduler, ScheduledSource scheduledSource, Runnable runnable) {
        ScheduledType type = scheduledSource.getType();
        ScheduleStrategy scheduleStrategy = scheduleStrategy(type);
        return scheduleStrategy.schedule(threadPoolTaskScheduler, scheduledSource, runnable);
    }

    /**
     * 静态工厂
     *
     * @param type 定时任务的类型
     */
    private static ScheduleStrategy scheduleStrategy(ScheduledType type) {
        ScheduleStrategy scheduleStrategy = strategyCache.get(type);
        if (scheduleStrategy != null) {
            return scheduleStrategy;
        }
        switch (type) {
            case CRON:
                scheduleStrategy = new CronStrategy();
                break;
            case FIXED_DELAY:
                scheduleStrategy = new FixedDelayStrategy();
                break;
            case FIXED_RATE:
                scheduleStrategy = new FixedRateStrategy();
                break;
            default:
                throw new SuperScheduledException("创建定时任务，执行失败，无法确定创建定时任务的类型");
        }
        strategyCache.put(type, scheduleStrategy);
        return scheduleStrategy;
    }
}
