package com.faasj.deployer.config;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class Config {

    @Bean
    @Lazy
    public KubernetesClient kubernetesClient() {
        return new DefaultKubernetesClient();
    }
}
