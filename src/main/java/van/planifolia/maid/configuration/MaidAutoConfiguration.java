package van.planifolia.maid.configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import van.planifolia.maid.core.MaidPlatformUtil;
import van.planifolia.maid.core.model.builder.MaidModelBuilder;


import java.util.concurrent.TimeUnit;

/**
 * @Description: stater包Bean统一管理
 * @Author: Planifolia.Van
 * @Date: 2024/12/6 11:40
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MaidProperties.class)
@ConditionalOnProperty(prefix = "thirdapp.gjp", name = "enabled", havingValue = "true", matchIfMissing = false)
public class MaidAutoConfiguration {



    /**
     * 构建内部缓存工具
     * @return 构建的缓存工具Bean
     */
    @Bean
    public Cache<Object,Object> cacheUtil(MaidProperties maidProperties){
        log.info("MaidPlatform-开始装配单机缓存工具");
        return Caffeine.newBuilder()
                .expireAfterWrite(maidProperties.getCacheTimeout(), TimeUnit.SECONDS)
                .maximumSize(10)
                .build();
    }

    /**
     * 注册MaidModelBuilder Bean
     * @return 构建结果
     */
    @Bean
    public MaidModelBuilder maidModelBuilder(MaidProperties maidProperties){
        log.info("MaidPlatform-开始装配请求构建器。");
        return new MaidModelBuilder(maidProperties);
    }

    /**
     * 构建MaidPlatformUtil
     * @param maidModelBuilder 请求Model构建器
     * @param cacheUtil 缓存工具
     * @return 构建结果
     */
    @Bean
    public MaidPlatformUtil maidPlatformUtil(MaidProperties maidProperties,MaidModelBuilder maidModelBuilder, Cache<Object,Object> cacheUtil){
        log.info("MaidPlatform-开始装配核心SDK工具");
        return new MaidPlatformUtil(maidModelBuilder,maidProperties,cacheUtil);
    }
}
