package com.faasj.deployer.service;

import com.faasj.deployer.dto.DeployDto;
import com.faasj.deployer.dto.FunctionDefinition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeployServiceImplTest {

    @Autowired
    private DeployService service;

    private static FunctionDefinition functionDefinition;

    @BeforeAll
    static void setUp() {
        functionDefinition = FunctionDefinition.builder()
                .functionId(UUID.fromString("f28f60d9-60f7-4e13-9602-1a3fc4298d0c"))
                .name("name")
                .image("image")
                .environmentVariables(Map.of("env", "env"))
                .annotations(Map.of("annotation", "annotation"))
                .build();
    }

    @BeforeEach
    void set() {
        service.deployFunction(functionDefinition);
    }

    @Test
    void test_deployFunction_expected_count_1() {
        assertEquals(1, service.getDeployedFunctions().count());
    }

    @Test
    void test_uninstallFunction_expected_count_0() {
        service.uninstallFunction(functionDefinition.getFunctionId());
        assertEquals(0, service.getDeployedFunctions().count());
    }

    @Test
    void test_getDeployedFunctions_expected_count_1() {
        assertEquals(1, service.getDeployedFunctions().count());
    }

    @Test
    void test_getLogs_expected_not_null() {
        assertNotNull(service.getLogs(functionDefinition.getName(), LocalDateTime.MIN));
    }

    @Test
    void test_getDeployedFunction_expected_equals_functionId() {
        DeployDto deployedFunction = service.getDeployedFunction(functionDefinition.getName());
        assertEquals(deployedFunction.getFunctionId(), functionDefinition.getFunctionId());
    }
}
