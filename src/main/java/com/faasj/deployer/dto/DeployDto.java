package com.faasj.deployer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeployDto {

    private UUID functionId;
    private String serviceName;
    private String version;
    private String image;
    private String envProcess;
    private Map<String, String> envVars;
    private Map<String, String> labels;
    private Map<String, String> annotations;
    private Map<String, String> limits;
    private Map<String, String> requests;
}
