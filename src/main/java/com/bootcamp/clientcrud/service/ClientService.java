package com.bootcamp.clientcrud.service;

import com.bootcamp.clientcrud.dto.ClientDTO;
import com.bootcamp.clientcrud.entities.Client;
import com.bootcamp.clientcrud.repositories.ClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

  @Autowired
  ClientRepository repository;

  @Transactional(readOnly = true)
  public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
    Page<Client> list = repository.findAll(pageRequest);
    return list.map(ClientDTO::new);
  }
}
