package com.faasj.deployer.service;

import com.faasj.deployer.config.TestConfig;
import com.faasj.deployer.dto.FunctionDefinition;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.server.mock.EnableKubernetesMockClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = TestConfig.class)
@EnableKubernetesMockClient(crud = true)
class KubernetesServiceImplTest {

    private static KubernetesClient client;
    private static KubernetesServiceImpl service;
    private static FunctionDefinition function;
    private static String NAMESPACE = "default";

    @BeforeAll
    static void set() {
        service = new KubernetesServiceImpl(client, NAMESPACE);
        function = FunctionDefinition.builder()
                .functionId(UUID.randomUUID())
                .name("name")
                .image("test")
                .environmentVariables(Map.of("envVar", "envVar"))
                .annotations(Map.of("ann", "ann"))
                .build();
    }

    @Test
    void test_deployment_all_important_fields_have_values() {
        Deployment deployment = service.deployment(function);
        assertEquals("Deployment", deployment.getKind());
        assertEquals(function.getName(), deployment.getMetadata().getName());
        assertNotNull(deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getEnv());
        assertNotNull(deployment.getSpec().getTemplate().getMetadata().getAnnotations());
    }

    @Test
    void test_service_all_important_fields_have_values() {
        Service testService = service.service(function);
        assertEquals("Service", testService.getKind());
        assertEquals(function.getName(), testService.getMetadata().getName());
        assertNotNull(testService.getSpec().getSelector());
    }
}
