package com.arextest.schedule.beans;

import com.arextest.common.model.classloader.RemoteJarClassLoader;
import com.arextest.common.utils.RemoteJarLoaderUtils;
import com.arextest.config.model.dto.system.DesensitizationJar;
import com.arextest.extension.desensitization.DataDesensitization;
import com.arextest.extension.desensitization.DefaultDataDesensitization;
import com.arextest.schedule.service.ConfigurationService;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;


@Configuration
@Slf4j
public class DataDesensitizationConfiguration {

  @Resource
  ConfigurationService configurationService;

  @Bean
  @ConditionalOnMissingBean(DataDesensitization.class)
  DataDesensitization desensitizationService() {
    List<DesensitizationJar> uploaded = configurationService.desensitization();
    if (CollectionUtils.isEmpty(uploaded)) {
      return new DefaultDataDesensitization();
    } else {
      try {
        DesensitizationJar selected = uploaded.get(0);
        RemoteJarClassLoader classLoader = RemoteJarLoaderUtils.loadJar(selected.getJarUrl());
        return RemoteJarLoaderUtils.loadService(DataDesensitization.class, classLoader).get(0);
      } catch (Throwable t) {
        LOGGER.error("Load remote desensitization jar failed, application startup blocked", t);
        throw new RuntimeException("Load remote desensitization jar failed");
      }
    }
  }
}
