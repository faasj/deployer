package com.faasj.deployer.controller;

import com.faasj.deployer.dto.DeployDto;
import com.faasj.deployer.dto.DeployedFunctionsCount;
import com.faasj.deployer.dto.FunctionDefinition;
import com.faasj.deployer.service.DeployService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class DeployerController {

    private final DeployService deployService;

    @DeleteMapping("{functionId}")
    public void uninstallFunction(@PathVariable UUID functionId) {
        deployService.uninstallFunction(functionId);
    }

    @GetMapping(value = ("functions"), produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeployedFunctionsCount> getDeployedFunctions() {
        DeployedFunctionsCount deployedFunctions = deployService.getDeployedFunctions();

        return new ResponseEntity<>(deployedFunctions, HttpStatus.OK);
    }

    @PostMapping
    public void deployFunction(@RequestBody FunctionDefinition funDeployRequest) {
        deployService.deployFunction(funDeployRequest);
    }

    @GetMapping(value = ("logs"), produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getLogs(@RequestParam(required = false) String name,
                                          @RequestParam(required = false)
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        String logs = deployService.getLogs(name, since);

        return new ResponseEntity<>(logs, HttpStatus.OK);
    }

    @GetMapping(value = "{serviceName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeployDto> getDeployedFunction(@PathVariable String serviceName) {
        DeployDto deployedFunction = deployService.getDeployedFunction(serviceName);

        return Objects.nonNull(deployedFunction) ?
                new ResponseEntity<>(deployedFunction, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
