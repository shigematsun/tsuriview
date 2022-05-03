package com.example.tsuriview.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=true)
@Table(name = "image")
public class Image extends AbstractEntity{

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "key")
	private String key;
	
	@Column(name = "entry_id")
	private Integer entryId;
	
	@Column(name = "entry_index")
	private Integer entryIndex;

	@Column(name = "user_id")
	private String userId;
}
