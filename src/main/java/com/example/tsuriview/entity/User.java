package com.example.tsuriview.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "m_user")
public class User {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "password")
	private String password;

	@Column(name = "role")
	private String role;

	@Column(name = "display_name")
	private String displayName;
}
