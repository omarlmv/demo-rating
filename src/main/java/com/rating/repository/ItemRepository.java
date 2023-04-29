package com.rating.repository;

import com.rating.model.ItemEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, Long> {
    @Query("SELECT i FROM ItemEntity i WHERE (SELECT COALESCE(AVG(r.rating), 0) FROM ReviewEntity r WHERE r.itemEntity.id = i.id) < :rating")
    List<ItemEntity> findItemsWithAverageRatingLowerThan(Double rating);

}
