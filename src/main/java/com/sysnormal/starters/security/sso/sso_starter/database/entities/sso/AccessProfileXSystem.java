package com.sysnormal.starters.security.sso.sso_starter.database.entities.sso;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(
        name = "access_profilies_x_systems",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "access_profilies_x_systems_u1",
                        columnNames = {
                                "(coalesce(parent_id, -1))","record_status_id",
                                "access_profile_id",
                                "system_id"
                        }
                )
        }
)
public class AccessProfileXSystem extends BaseSsoEntity<AccessProfileXSystem> {

    @Column(name = "access_profile_id", nullable = false)
    private Long accessProfileId;

    @Column(name = "system_id", nullable = false)
    private Long systemId;

    @Column(name = "json_data", length = Integer.MAX_VALUE)
    private String jsonData;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "access_profile_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private AccessProfile accessProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private System system;



}
