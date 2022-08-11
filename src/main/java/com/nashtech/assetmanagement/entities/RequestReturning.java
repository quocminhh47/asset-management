package com.nashtech.assetmanagement.entities;

import com.nashtech.assetmanagement.enums.RequestReturningState;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Table(name = "request_returning")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestReturning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "requested_by")
    private Users requestedBy;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "accepted_by")
    private Users acceptedBy;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumns({
            @JoinColumn(name = "asset_code"),
            @JoinColumn(name = "assigned_date"),
            @JoinColumn(name = "assigned_to")
    })
    private Assignment assignment;

    @Column(name = "returned_date")
    private Date returnedDate;

    @Column
    @Enumerated(EnumType.STRING)
    private RequestReturningState state;

}
