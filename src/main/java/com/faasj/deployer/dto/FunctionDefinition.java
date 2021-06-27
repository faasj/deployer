package com.faasj.deployer.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
public class FunctionDefinition {

    private UUID functionId;
    private String imageName;
    private Map<String, String> environmentVariables;
    private Map<String, String> annotations;
}
