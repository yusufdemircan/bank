package com.yedy.bank.service;

import com.yedy.bank.dto.OwnerDto;
import com.yedy.bank.dto.converter.OwnerDtoConverter;
import com.yedy.bank.exception.OwnerNotFoundException;
import com.yedy.bank.model.Owner;
import com.yedy.bank.repository.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final OwnerDtoConverter ownerDtoConverter;

    public OwnerService(OwnerRepository ownerRepository, OwnerDtoConverter ownerDtoConverter) {
        this.ownerRepository = ownerRepository;
        this.ownerDtoConverter = ownerDtoConverter;
    }

    protected Owner findOwnerById(UUID id) {
        return ownerRepository.findById(id)
                .orElseThrow(
                        () -> new OwnerNotFoundException("Bu id ' ye sahip kullanıcı bulunamadı: " + id));

    }

    public OwnerDto getCustomerById(UUID ownerId) {
        return ownerDtoConverter.convertToOwnerDto(findOwnerById(ownerId));
    }

    public List<OwnerDto> getAllCustomer() {

        return ownerRepository.findAll()
                .stream()
                .map(ownerDtoConverter::convertToOwnerDto)
                .collect(Collectors.toList());
    }
}
