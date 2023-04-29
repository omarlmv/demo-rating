package com.rating.service;

import com.rating.model.ItemEntity;
import com.rating.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@EnableScheduling
@EnableCaching
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;

    @Cacheable(value = "titlesCache", key = "#rating")
    @Override
    public List<String> getTitles(Double rating) {
        List<ItemEntity> items = itemRepository.findItemsWithAverageRatingLowerThan(rating);
        return items.stream()
                .map(ItemEntity::getTitle)
                .collect(Collectors.toList());
    }

    @Scheduled(cron = "0 * * ? * *")//Ejecuta cada minuto la limpieza de la cache, con posibilidad a configurarse
    @CacheEvict(value = "titlesCache", allEntries = true)
    public void clearTitlesCache() {
        log.info("Clean Cache getTitles()");
    }
}
