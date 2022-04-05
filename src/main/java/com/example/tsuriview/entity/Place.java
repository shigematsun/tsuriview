package com.example.tsuriview.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "m_place")
public class Place {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "prefecture")
	private String prefecture;

	@Column(name = "map_url")
	private String mapUrl;

	@Column(name = "tide_pc")
	private String tidePc;

	@Column(name = "tide_hc")
	private String tideHc;
}