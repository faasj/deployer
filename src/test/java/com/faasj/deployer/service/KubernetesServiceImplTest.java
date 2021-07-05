package com.faasj.deployer.service;

import com.faasj.deployer.DeployerApplicationTests;
import com.faasj.deployer.dto.FunctionDefinition;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class KubernetesServiceImplTest extends DeployerApplicationTests {

    @Autowired
    KubernetesService service;
    public static FunctionDefinition function = new FunctionDefinition();

    @BeforeAll
    static void set() {
        function.setFunctionId(UUID.randomUUID());
        function.setImageName("test");
        function.setEnvironmentVariables(Map.of("envVar", "envVar"));
        function.setAnnotations(Map.of("ann", "ann"));
    }

    @Test
    void test_deployment() {
        Deployment deployment = service.deployment(function);
        assertEquals("Deployment", deployment.getKind());
        assertEquals(function.getImageName() + "-deployment", deployment.getMetadata().getName());
    }

    @Test
    void test_service() {
        Service service = this.service.service(function);
        assertEquals("Service", service.getKind());
        assertEquals(function.getImageName() + "-service", service.getMetadata().getName());
    }
}
