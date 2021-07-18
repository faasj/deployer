package com.faasj.deployer.dao;

import com.faasj.deployer.dto.DeployDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class MemoryBasedDeployRepository implements DeployRepository {

    Map<UUID, DeployDto> deployedFunctions = new ConcurrentHashMap<>();
    Map<LocalDateTime, String> logs = new ConcurrentHashMap<>();

    @Override
    public void save(UUID functionId, DeployDto deployDto) {
        deployedFunctions.put(functionId, deployDto);
        logs.put(LocalDateTime.now(),
                " [INFO] function deployed ID " + functionId + '/' + deployDto.getServiceName() + '\n');
    }

    @Override
    public void delete(UUID functionId) {
        deployedFunctions.remove(functionId);
        logs.put(LocalDateTime.now(),
                " [INFO] function uninstalled. ID " + functionId + '\n');
    }

    @Override
    public String findLogs(String name, LocalDateTime since) {
        var stream = logs.entrySet().stream();
        if (Objects.nonNull(name)) {
            stream = stream.filter(el -> el.getValue().contains('/' + name));
        }
        if (Objects.nonNull(since)) {
            stream = stream.filter(el -> el.getKey().isAfter(since));
        }
        return stream.map(el -> String.join("", el.getKey().toString(), el.getValue()))
                .collect(Collectors.joining("\n"));
    }

    @Override
    public Optional<DeployDto> findDeploy(String serviceName) {
        return deployedFunctions.values().stream()
                .filter(el -> el.getServiceName().equalsIgnoreCase(serviceName))
                .findFirst();
    }

    @Override
    public int getDeployCount() {
        return deployedFunctions.size();
    }
}
