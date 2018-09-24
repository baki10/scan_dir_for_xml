package com.scandirectoryforxml.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WatcherConfigEntity {

    @Id
    private Long id;
    private String source;

    public WatcherConfigEntity() {
    }

    public WatcherConfigEntity(String source) {
        this.source = source;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
