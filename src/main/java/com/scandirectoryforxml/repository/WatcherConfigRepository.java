package com.scandirectoryforxml.repository;

import com.scandirectoryforxml.model.WatcherConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatcherConfigRepository extends JpaRepository<WatcherConfigEntity, Long> {

}
