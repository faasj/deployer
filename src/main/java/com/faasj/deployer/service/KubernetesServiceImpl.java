package com.faasj.deployer.service;

import com.faasj.deployer.dto.FunctionDefinition;
import io.fabric8.kubernetes.api.model.EnvVarBuilder;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentStatus;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KubernetesServiceImpl implements KubernetesService {

    private KubernetesClient client;
    private String NAMESPACE;

    public KubernetesServiceImpl(@Autowired KubernetesClient client,
                                 @Value("${namespace}") String NAMESPACE) {
        this.client = client;
        this.NAMESPACE = NAMESPACE;
    }

    @Override
    public Deployment deployment(FunctionDefinition function) {
          Deployment deploymentTemplate = new DeploymentBuilder()
                .withKind("Deployment")
                .withNewMetadata()
                    .withName(function.getName())
                .endMetadata()
                .withNewSpec()
                    .withNewSelector()
                        .withMatchLabels(Map.of("func", function.getName()))
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
                            .withLabels(Map.of("func", function.getName()))
                        .endMetadata()
                        .withNewSpec()
                            .addNewContainer()
                                .withImage(function.getImage())
                                .withImagePullPolicy("Always")
                                .withName(function.getName())
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

        return client.apps().deployments().inNamespace(NAMESPACE).create(deploymentTemplate);
    }

    @Override
    public io.fabric8.kubernetes.api.model.Service service(FunctionDefinition function) {
         io.fabric8.kubernetes.api.model.Service serviceTemplate = new ServiceBuilder()
                .withKind("Service")
                .withNewMetadata()
                    .withName(function.getName())
                .endMetadata()
                .withNewSpec()
                    .withType("ClusterIP")
                    .addNewPort()
                        .withPort(80)
                        .withProtocol("TCP")
                        .withTargetPort(new IntOrString(8080))
                    .endPort()
                    .withSelector(Map.of("func", function.getName()))
                .endSpec()
                .build();

        return client.services().inNamespace(NAMESPACE).create(serviceTemplate);
    }

    @Override
    @SneakyThrows
    public String checkDeployStatus(String functionName) {
        DeploymentStatus deploymentStatus =
                client.apps().deployments().inNamespace("default").withName(functionName).get().getStatus();

        if (deploymentStatus.getReadyReplicas() == null) {
            return String.format("Function \"%s\" is deploying...", functionName);
        }
        return String.format("Function \"%s\" deployed and ready!", functionName);
    }
}
