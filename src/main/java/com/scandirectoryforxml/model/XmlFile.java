package com.scandirectoryforxml.model;

import javax.persistence.*;

@Entity
public class XmlFile {

    @Id
    @GeneratedValue
    private Long id;

    private String fileName;

    @Lob
    @Column(length=100000)
    private byte[] xmlText;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getXmlText() {
        return xmlText;
    }

    public void setXmlText(byte[] xmlText) {
        this.xmlText = xmlText;
    }
}
