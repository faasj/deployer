package com.faasj.deployer.dao;

import com.faasj.deployer.dto.DeployDto;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface DeployRepository {

    void save(UUID functionId, DeployDto deployDto);

    void delete(UUID functionId);

    String findLogs(String name, LocalDateTime since);

    Optional<DeployDto> findDeploy(String serviceName);

    int getDeployCount();
}
