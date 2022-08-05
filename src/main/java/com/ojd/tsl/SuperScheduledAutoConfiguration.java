package com.ojd.tsl;

import com.ojd.tsl.common.constant.ZooKeeperConstant;
import com.ojd.tsl.core.RunnableInterceptor.strengthen.ColonyStrengthen;
import com.ojd.tsl.core.RunnableInterceptor.strengthen.ExecutionFlagStrengthen;
import com.ojd.tsl.core.RunnableInterceptor.strengthen.LogStrengthen;
import com.ojd.tsl.properties.PlugInProperties;
import com.ojd.tsl.properties.ThreadPoolTaskSchedulerProperties;
import com.ojd.tsl.properties.ZooKeeperProperties;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.io.IOException;
import java.net.InetAddress;

@Configuration
@EnableConfigurationProperties(value = {ThreadPoolTaskSchedulerProperties.class, PlugInProperties.class, ZooKeeperProperties.class})
public class SuperScheduledAutoConfiguration {
    @Autowired
    private ThreadPoolTaskSchedulerProperties threadPoolTaskSchedulerProperties;
    @Autowired
    private PlugInProperties plugInProperties;
    @Autowired
    private ZooKeeperProperties zooKeeperProperties;

    @Bean("threadPoolTaskScheduler")
    @ConditionalOnMissingBean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(threadPoolTaskSchedulerProperties.getPoolSize());
        taskScheduler.setThreadNamePrefix(threadPoolTaskSchedulerProperties.getThreadNamePrefix());
        taskScheduler.setWaitForTasksToCompleteOnShutdown(threadPoolTaskSchedulerProperties.getWaitForTasksToCompleteOnShutdown());
        taskScheduler.setAwaitTerminationSeconds(threadPoolTaskSchedulerProperties.getAwaitTerminationSeconds());
        taskScheduler.initialize();
        return taskScheduler;
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.super.scheduled.plug-in", name = "executionFlag", havingValue = "true")
    @ConditionalOnMissingBean
    public ExecutionFlagStrengthen executionFlagStrengthen() {
        return new ExecutionFlagStrengthen();
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.super.scheduled.plug-in", name = "executionLog", havingValue = "true")
    @ConditionalOnMissingBean
    public LogStrengthen logStrengthen() {
        return new LogStrengthen(plugInProperties.getLogPath());
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.super.scheduled.plug-in", name = "colony", havingValue = "true")
    @ConditionalOnMissingBean
    public ColonyStrengthen colonyStrengthen() {
        return new ColonyStrengthen();
    }

    /**
     * 获取zk连接
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(ColonyStrengthen.class)
    @ConditionalOnProperty(prefix = "spring.super.scheduled.zookeeper", name = "url")
    public ZkClient zooKeeper() {
        ZkClient zk = null;
        try {
            zk = new ZkClient(zooKeeperProperties.getUrl()
                    , zooKeeperProperties.getSessionTimeout()
                    , zooKeeperProperties.getConnectionTimeout());
            if (!zk.exists(ZooKeeperConstant.ROOT_PATH)) {
                zk.createPersistent(ZooKeeperConstant.ROOT_PATH);
            }
            if (!zk.exists(ZooKeeperConstant.ROOT_PATH + "/" + plugInProperties.getColonyName())) {
                zk.createPersistent(ZooKeeperConstant.ROOT_PATH + "/" + plugInProperties.getColonyName());
            }

            String zkPath = zk.createEphemeralSequential(ZooKeeperConstant.ROOT_PATH + "/" + plugInProperties.getColonyName() + ZooKeeperConstant.COLONY_STRENGTHEN
                    , InetAddress.getLocalHost().getHostAddress());
            zooKeeperProperties.setZkPath(zkPath);
            zooKeeperProperties.setZkParentNodePath(ZooKeeperConstant.ROOT_PATH + "/" + plugInProperties.getColonyName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zk;
    }
}
