package com.bootcamp.clientcrud.service;

import com.bootcamp.clientcrud.dto.ClientDTO;
import com.bootcamp.clientcrud.entities.Client;
import com.bootcamp.clientcrud.repositories.ClientRepository;
import com.bootcamp.clientcrud.resources.exceptions.ResourceNotFoundException;
import com.bootcamp.clientcrud.service.exceptions.EntityNotFoundException;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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
    return list.map(x -> new ClientDTO(x));
  }

  @Transactional(readOnly=true)
  public ClientDTO findById(Long id) {
    Optional<Client> obj = repository.findById(id);
    Client entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
    return new ClientDTO(entity);
  }

  @Transactional
  public ClientDTO insert(ClientDTO dto) {
    Client entity = new Client();
    transferDtoToEntity(dto, entity);
    entity = repository.save(entity);
    return new ClientDTO(entity);
  }



  @Transactional
  public ClientDTO update(Long id, ClientDTO dto) {
    try {
      Client entity = repository.getOne(id);
      transferDtoToEntity(dto, entity);
      entity = repository.save(entity);
      return new ClientDTO(entity);
    }
    catch (EntityNotFoundException e) {
      throw new ResourceNotFoundException("Id not found " + id);
    }
  }

  public void delete(Long id) {
    try {
      repository.deleteById(id);
    }
    catch (EmptyResultDataAccessException e) {
      throw new ResourceNotFoundException("Id not found " + id);
    }
    catch (DataIntegrityViolationException e) {
      throw new DataIntegrityViolationException("Integrity violation");
    }
  }

  private void transferDtoToEntity(ClientDTO dto, Client entity) {
    entity.setName(dto.getName());
    entity.setCpf(dto.getCpf());
    entity.setIncome(dto.getIncome());
    entity.setBirthDate(dto.getBirthDate());
    entity.setChildren(dto.getChildren());
  }
}
