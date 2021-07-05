package com.faasj.deployer.service;

import com.faasj.deployer.dto.FunctionDefinition;
import io.fabric8.kubernetes.api.model.EnvVarBuilder;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KubernetesServiceImpl implements KubernetesService {

    @Override
    public Deployment deployment(FunctionDefinition function) {
          return new DeploymentBuilder()
                .withKind("Deployment")
                .withNewMetadata()
                    .withName(function.getImageName() + "-deployment")
                .endMetadata()
                .withNewSpec()
                    .withNewSelector()
                        .withMatchLabels(Map.of("func", function.getImageName()))
                    .endSelector()
                    .withReplicas(1)
                    .withNewStrategy()
                        .withType("RollingUpdate")
                        .withNewRollingUpdate()
                            .withMaxUnavailable(new IntOrString(0))
                            .withMaxSurge(new IntOrString(1))
                        .endRollingUpdate()
                    .endStrategy()
                    .withNewTemplate()
                        .withNewMetadata()
                            .withAnnotations(function.getAnnotations())
                            .withLabels(Map.of("func", function.getImageName()))
                        .endMetadata()
                        .withNewSpec()
                            .addNewContainer()
                                .withImage(function.getImageName())
                                .withImagePullPolicy("Always")
                                .withName(function.getImageName() + function.getFunctionId())
                                .withEnv(function.getEnvironmentVariables().entrySet().stream()
                                        .map(x -> new EnvVarBuilder().withName(x.getKey()).withValue(x.getValue()).build())
                                        .collect(Collectors.toList()))
                                .addNewPort()
                                    .withContainerPort(8080)
                                .endPort()
                            .endContainer()
                        .endSpec()
                    .endTemplate()
                .endSpec()
                .build();
    }

    @Override
    public io.fabric8.kubernetes.api.model.Service service(FunctionDefinition function) {
        return new ServiceBuilder()
                .withKind("Service")
                .withNewMetadata()
                    .withName(function.getImageName() + "-service")
                .endMetadata()
                .withNewSpec()
                    .withType("LoadBalancer")
                    .addNewPort()
                        .withPort(80)
                        .withProtocol("TCP")
                        .withTargetPort(new IntOrString(8080))
                    .endPort()
                    .withSelector(Map.of("func", function.getImageName()))
                .endSpec()
                .build();
    }
}
