package com.siemens.nextwork.tag.repo;

import com.siemens.nextwork.tag.dto.tags.TagsGetResponseDTO;
import com.siemens.nextwork.tag.model.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class TagsRepositoryImpl implements TagRepositoryExt {

    public static final String TAGS_COLLECTION = "Tags_Data";
    public static final String TAG_CATEGORY_KEY = "category";
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Tags> findByTagsIds(List<String> ids) {
        return mongoTemplate.find(Query.query(where("_id").in(ids)),
                Tags.class);
    }

    public List<TagsGetResponseDTO.SearchData> findGlobalTagsData() {
        ProjectionOperation projection = Aggregation.project("name", "id", TAG_CATEGORY_KEY, "tagEntities");
        MatchOperation matchGlobal = match(where(TAG_CATEGORY_KEY).is("Global"));
        Aggregation aggregation = Aggregation.newAggregation(matchGlobal, projection);
        AggregationResults<TagsGetResponseDTO.SearchData> results = mongoTemplate.aggregate(aggregation, TAGS_COLLECTION, TagsGetResponseDTO.SearchData.class);
        return results.getMappedResults();
    }

    public List<TagsGetResponseDTO.SearchData> findLocalTagsDataByWsId(String wsId) {
        ProjectionOperation projection = Aggregation.project("name", "id", TAG_CATEGORY_KEY, "tagEntities");
        MatchOperation matchLocal = match(where(TAG_CATEGORY_KEY).is("Local").and("wsId").is(wsId).and("isDeleted").is(false));
        Aggregation aggregation = Aggregation.newAggregation(matchLocal, projection);
        AggregationResults<TagsGetResponseDTO.SearchData> results = mongoTemplate.aggregate(aggregation, TAGS_COLLECTION, TagsGetResponseDTO.SearchData.class);
        return results.getMappedResults();
    }

    public List<Tags> findAllLocalTagsByWsId(String wsId) {
        String category = "Local";
        Aggregation aggregation = newAggregation(
                Aggregation.match(
                        new Criteria().andOperator(
                                where(TAG_CATEGORY_KEY).is(category),
                                where("wsId").is(wsId)
                        )
                )
        );
        return mongoTemplate.aggregate(aggregation, TAGS_COLLECTION, Tags.class).getMappedResults();
    }

    public List<Tags> findAllGlobalTags() {
        String category = "Global";
        Aggregation aggregation = newAggregation(
                Aggregation.match(
                        new Criteria().andOperator(
                                where(TAG_CATEGORY_KEY).is(category)
                        )
                )
        );
        return mongoTemplate.aggregate(aggregation, TAGS_COLLECTION, Tags.class).getMappedResults();
    }
}
