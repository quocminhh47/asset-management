package com.nashtech.assetmanagement.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

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
    private Users accepted_by;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "asset_code"),
            @JoinColumn(name = "assigned_date"),
            @JoinColumn(name = "assgined_to")
    })
    private Assignment assignment;

    @Column(name = "returned_date")
    private Date returnedDate;

    @Column(name = "state",length = 50)
    private String state;

}
