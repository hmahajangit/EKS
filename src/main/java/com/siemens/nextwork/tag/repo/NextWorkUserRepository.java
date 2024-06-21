package com.siemens.nextwork.tag.repo;

import com.siemens.nextwork.tag.model.NextWorkUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NextWorkUserRepository extends MongoRepository<NextWorkUser, String> {

	Optional<NextWorkUser> findByEmail(String email);


}