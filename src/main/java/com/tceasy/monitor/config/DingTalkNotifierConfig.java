package com.tceasy.monitor.config;

import com.tceasy.monitor.notifier.DingTalkNotifier;
import de.codecentric.boot.admin.server.config.AdminServerNotifierAutoConfiguration;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DingTalkNotifierConfig {

    @Configuration
    @ConditionalOnProperty(prefix = "spring.cloud.admin.notify.dingtalk", name = "api-url")
    @AutoConfigureBefore({AdminServerNotifierAutoConfiguration.NotifierTriggerConfiguration.class, AdminServerNotifierAutoConfiguration.CompositeNotifierConfiguration.class})
    public static class DingTalkNotifierConfiguration {

        @Bean
        @ConditionalOnMissingBean
        @ConfigurationProperties("spring.cloud.admin.notify.dingtalk")
        public DingTalkNotifier dingTalkNotifier(InstanceRepository repository) {
            DingTalkNotifier  dingTalkNotifier = new DingTalkNotifier(repository);
            return dingTalkNotifier;
        }
    }


}
