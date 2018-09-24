package com.scandirectoryforxml.repository;

import com.scandirectoryforxml.model.XmlFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface XmlFileRepository extends JpaRepository<XmlFile, Long> {
}
