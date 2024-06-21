package com.siemens.nextwork.tag.repo;

import com.siemens.nextwork.tag.model.Workstream;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WorkStreamRepository extends MongoRepository<Workstream, String> {


}
