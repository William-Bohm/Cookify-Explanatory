package FullTextSearch.repository;


import FullTextSearch.models.EsFoodItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EsFoodItemRepository extends ElasticsearchRepository<EsFoodItem, String> {
    List<EsFoodItem> findByName(String name);
    Page<EsFoodItem> findByName(String name, Pageable pageable);
    List<EsFoodItem> findByNameAndUnit(String name, String unit);

    boolean existsByName(String name);
}
