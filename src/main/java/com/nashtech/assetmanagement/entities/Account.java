package com.nashtech.assetmanagement.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "account")
public class Account {

	@Id
	@Column(nullable = false, length = 10)
	private String staffCode;

	@Column(name = "first_name", length = 150, nullable = false)
	private String firstName;

	@Column(name = "last_name", length = 150, nullable = false)
	private String lastName;

	@Column(name = "username", length = 250, nullable = false)
	private String userName;

	@Column(name = "joined_date", nullable = false)
	private Date joinedDate;

	@Column(name = "birth_date", nullable = false)
	private Date birthDate;

	@Column
	private Boolean gender;

	@Column
	private Boolean state;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	private Role roleId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id", nullable = false)
	private Location locationId;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assignedBy")
	private List<Assignment> assignments;

}
