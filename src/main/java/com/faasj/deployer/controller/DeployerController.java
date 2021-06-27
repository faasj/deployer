package com.faasj.deployer.controller;

import com.faasj.deployer.dto.DeployedFunctionsCount;
import com.faasj.deployer.dto.FunctionDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/")
public class DeployerController {

    Map<UUID, FunctionDefinition> deployedFunctions = new HashMap<>();
    Map<LocalDateTime, String> logs = new HashMap<>();

    @DeleteMapping("{functionId}")
    public void uninstallFunction(@PathVariable UUID functionId) {
        deployedFunctions.remove(functionId);

        logs.put(LocalDateTime.now(), "[INFO] function uninstalled ID " + functionId + '\n');
    }

    @GetMapping(value = ("functions"), produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeployedFunctionsCount> getDeployedFunctions() {
        DeployedFunctionsCount deployedFunctionsCount = new DeployedFunctionsCount(deployedFunctions.size());

        return new ResponseEntity<>(deployedFunctionsCount, HttpStatus.OK);
    }

    @PostMapping
    public void deployFunction(@RequestBody FunctionDefinition funDeployRequest) {
        deployedFunctions.put(funDeployRequest.getFunctionId(), funDeployRequest);

        logs.put(LocalDateTime.now(), "[INFO] function deployed ID " + funDeployRequest.getFunctionId() + '\n');
    }

    @GetMapping(value = ("logs"), produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getLogs(@RequestParam(required = false) String name,
                                          @RequestParam(required = false)
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        StringBuilder stringBuilder = new StringBuilder();

        logs.entrySet().forEach(stringBuilder::append);

        return new ResponseEntity<>(stringBuilder.toString(), HttpStatus.OK);
    }
}
