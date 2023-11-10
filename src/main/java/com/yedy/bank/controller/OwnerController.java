package com.yedy.bank.controller;

import com.yedy.bank.dto.OwnerDto;
import com.yedy.bank.service.OwnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("owner/v1")
public class OwnerController {
    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping("/{ownerId}")
    public ResponseEntity<OwnerDto> getOwnerById(@PathVariable UUID ownerId){
        return ResponseEntity.ok(ownerService.getCustomerById(ownerId));
    }

    @GetMapping
    public ResponseEntity<List<OwnerDto>> getAllCustomers() {
        return ResponseEntity.ok(ownerService.getAllCustomer());
    }
}
