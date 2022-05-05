package com.example.tsuriview.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper=true)
@Table(name = "entry")
public class Entry extends AbstractEntity{

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
    @Column(name = "date")
    private Date date;
	
	@Column(name = "month")
	private Integer month;

	@Column(name = "start_time")
	private String startTime;

	@Column(name = "end_time")
	private String endTime;
	
	@Column(name = "prefecture")
	private String prefecture;
	
	@Column(name = "place")
	private Integer place;

	@Column(name = "memo")
	private String memo;

	@Column(name = "user_id")
	private String userId;
	
	@OneToMany(mappedBy = "entryId")
	@ToString.Exclude // 循環参照の回避
	private List<EntryFish> fishList;
}