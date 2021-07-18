package com.faasj.deployer.service;

import com.faasj.deployer.dao.DeployRepository;
import com.faasj.deployer.dto.DeployDto;
import com.faasj.deployer.dto.DeployedFunctionsCount;
import com.faasj.deployer.dto.FunctionDefinition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeployServiceImpl implements DeployService {

    private final DeployRepository repository;

    @Override
    public void deployFunction(FunctionDefinition funDeployRequest) {
        DeployDto deployDto = DeployDto.builder()
                .functionId(funDeployRequest.getFunctionId())
                .version("v1")
                .serviceName(funDeployRequest.getName())
                .image(funDeployRequest.getImage())
                .envProcess("envProcess")
                .envVars(funDeployRequest.getEnvironmentVariables())
                .labels(Map.of("label", "label"))
                .annotations(funDeployRequest.getAnnotations())
                .limits(Map.of("limit", "limit"))
                .requests(Map.of("request", "request"))
                .build();

        repository.save(funDeployRequest.getFunctionId(), deployDto);
    }

    @Override
    public void uninstallFunction(UUID functionId) {
        repository.delete(functionId);
    }

    @Override
    public DeployedFunctionsCount getDeployedFunctions() {
        return new DeployedFunctionsCount(repository.getDeployCount());
    }

    @Override
    public String getLogs(String name, LocalDateTime since) {
        return repository.findLogs(name, since);
    }

    @Override
    public DeployDto getDeployedFunction(String serviceName) {
        return repository.findDeploy(serviceName).orElse(null);
    }
}
