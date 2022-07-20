package com.nashtech.assetmanagement.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "assignment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Assignment {
	
	@EmbeddedId
	AssignmentId id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assigned_by", nullable = false)
	private Account assignedBy;
	
	@ManyToOne
    @MapsId("assignedTo")
    @JoinColumn(name = "assigned_to", columnDefinition = "VARCHAR(10)")
	private Account assignedTo;

	@ManyToOne
    @MapsId("assetCode")
    @JoinColumn(name = "asset_code", columnDefinition = "VARCHAR(10)")
	private Asset assetCode;
	
	@Column(length = 50, nullable = false)
	private String state;
	
	@Column(nullable = false)
	private String note;
}
