package com.nashtech.assetmanagement.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
	
	@Id
	@Column(length = 10, nullable = false)
	private String id;
	
	@Column(length = 150, nullable = false)
	private String name;
	
	@Column(name="total_quantity")
	private Long totalQuantity;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
	private List<Asset> assets;
}
