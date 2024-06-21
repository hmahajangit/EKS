package com.siemens.nextwork.tag.repo;

import com.siemens.nextwork.tag.model.Roles;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RolesRepository extends MongoRepository<Roles, String> {

}
