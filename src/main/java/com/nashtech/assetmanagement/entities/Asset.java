package com.nashtech.assetmanagement.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Configurable;

import com.nashtech.assetmanagement.enums.AssetState;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "asset")
@Configurable
public class Asset{

	@Id
	@Column(name = "asset_code",length = 10)
	private String assetCode;
	
	@Column(name = "asset_name",length = 200)
	private String assetName;
	
	@Column
	private String specification;
	
	@Column(name = "installed_date")
	private Date installedDate;
	
	@Column
	@Enumerated(EnumType.STRING)
	private AssetState state;
	
	@ManyToOne
	@JoinColumn(name = "location_id")
	private Location location;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Users user;
}
