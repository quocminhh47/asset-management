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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "location")
public class Location {

	@Id
	@Column(length = 10, nullable = false)
	private String code;
	
	@Column(length = 100, nullable = false)
	private String name;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "locationId")
	private List<Asset> assets;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "locationId")
	private List<Account> accounts;
}
