package com.example.tsuriview.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper=true)
@Table(name = "entry_fish")
@IdClass(value=EntryFishKey.class)
public class EntryFish extends AbstractEntity{

	@Id
	@Column(name = "entry_id")
	private Integer entryId;
	
	@Id
	@Column(name = "index")
	private Integer index;
	
	@Column(name = "fish")
	private Integer fish;
	
	@Column(name = "count")
	private Integer count;
	
	@Column(name = "min")
	private Integer min;
	
	@Column(name = "max")
	private Integer max;
	
	@Column(name = "method")
	private Integer method;
	
	@Column(name = "time_hour")
	private String timeHour;
	
	@Column(name = "time_minute")
	private String timeMinute;

	@ManyToOne
	@JoinColumn(name = "entry_id", insertable = false, updatable = false)
	@ToString.Exclude // 循環参照の回避
	private Entry entry;
}