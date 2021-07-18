package com.faasj.deployer.service;

import com.faasj.deployer.dto.FunctionDefinition;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.apps.Deployment;

public interface KubernetesService {

    Deployment deployment(FunctionDefinition function);

    Service service(FunctionDefinition function);

    String checkDeployStatus(String functionName);
}
