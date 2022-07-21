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
@Table(name = "users")
public class Users {

	@Id
	@Column(nullable = false, length = 10)
	private String staffCode;

	@Column(name = "first_name", length = 150)
	private String firstName;

	@Column(name = "last_name", length = 150)
	private String lastName;

	@Column(name = "username", length = 250)
	private String userName;

	@Column(name = "password", length = 250)
	private String password;

	@Column(name = "joined_date" )
	private Date joinedDate;

	@Column(name = "birth_date")
	private Date birthDate;

	@Column
	private Boolean gender;

	@Column
	private Boolean state;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	private Role role;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	private Location location;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assignedBy")
	private List<Assignment> assignments;

}
