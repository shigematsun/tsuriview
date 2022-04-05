package com.example.tsuriview.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "m_prefecture")
public class Prefecture {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;
}