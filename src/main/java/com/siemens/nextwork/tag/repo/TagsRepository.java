package com.siemens.nextwork.tag.repo;


import com.siemens.nextwork.tag.model.Tags;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TagsRepository extends MongoRepository<Tags, String>, TagRepositoryExt {

    List<Tags> findByNameAndCategory(String name, String category);

}