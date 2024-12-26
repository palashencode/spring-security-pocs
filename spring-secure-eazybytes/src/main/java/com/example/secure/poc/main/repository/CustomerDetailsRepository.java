package com.example.secure.poc.main.repository;

import com.example.secure.poc.main.entity.CustomerDetailsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDetailsRepository extends CrudRepository<CustomerDetailsEntity, String> {

}
