package com.faasj.deployer.service;

import com.faasj.deployer.dto.DeployDto;
import com.faasj.deployer.dto.DeployedFunctionsCount;
import com.faasj.deployer.dto.FunctionDefinition;

import java.time.LocalDateTime;
import java.util.UUID;

public interface DeployService {

    void deployFunction(FunctionDefinition funDeployRequest);

    void uninstallFunction(UUID functionId);

    DeployedFunctionsCount getDeployedFunctions();

    String getLogs(String name, LocalDateTime since);

    DeployDto getDeployedFunction(String serviceName);
}
