package com.example.tsuriview.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.Data;

@Data
@MappedSuperclass
public class AbstractEntity {
    @Column(name = "created_datetime")
    private Date createdDatetime;

    @Column(name = "updated_datetime")
    private Date updatedDatetime;

    @PrePersist
    public void onPrePersist() {
        setCreatedDatetime(new Date());
        setUpdatedDatetime(new Date());
    }

    @PreUpdate
    public void onPreUpdate() {
        setUpdatedDatetime(new Date());
    }
}
