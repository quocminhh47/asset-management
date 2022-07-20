package com.nashtech.assetmanagement.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "asset")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Asset {
	
	@Id
	@Column(name = "asset_code",length = 10, nullable = false)
	private String assetCode;
	
	@Column(name = "asset_name",length = 200, nullable = false)
	private String assetName;
	
	@Column(nullable = false)
	private String specification;
	
	@Column(name = "installed_date")
	private Date installedDate;
	
	@Column
	private Boolean state;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id", nullable = false)
	private Location locationId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category categoryId;
}
