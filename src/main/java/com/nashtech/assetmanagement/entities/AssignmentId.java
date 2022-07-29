package com.nashtech.assetmanagement.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
public class AssignmentId implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "assigned_to", columnDefinition = "VARCHAR(10)")
    String assignedTo;

    @Column(name = "asset_code", columnDefinition = "VARCHAR(10)")
    String assetCode;
    
    @SuppressWarnings("unused")
    @Column(name = "assigned_date")
	private Date assignedDate;
}
