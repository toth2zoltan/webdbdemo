package com.example.webdbdemo.model_spring;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<Address,Integer>{ // interface, nem class!
}
