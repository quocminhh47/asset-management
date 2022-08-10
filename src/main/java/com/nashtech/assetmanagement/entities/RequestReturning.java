package com.nashtech.assetmanagement.entities;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.nashtech.assetmanagement.enums.RequestReturningState;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "request_returning")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestReturning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "requested_by")
    private Users requestedBy;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accepted_by")
    private Users acceptedBy;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "asset_code"),
            @JoinColumn(name = "assigned_date"),
            @JoinColumn(name = "assgined_to")
    })
    private Assignment assignment;

    @Column(name = "returned_date")
    private Date returnedDate;

    @Column
    @Enumerated(EnumType.STRING)
    private RequestReturningState state;

}
