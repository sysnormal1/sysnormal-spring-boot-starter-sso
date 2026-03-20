package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sysnormal.data.base_data_model.entities.BaseCommonEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * base entity of sso
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseSsoEntity<T extends BaseSsoEntity<T>>  extends BaseCommonEntity<T> {

    @Column(name = "record_status_id", nullable = false)
    @ColumnDefault(RecordStatus.ACTIVE_ID+"")
    private Long recordStatusId = RecordStatus.ACTIVE_ID;

    @CreatedBy
    @Column(name = "creator_agent_id", nullable = false, updatable = false)
    @ColumnDefault(Agent.SYSTEM_ID+"")
    private Long creatorAgentId = Agent.SYSTEM_ID;

    @LastModifiedBy()
    @Column(name = "updater_agent_id", insertable = false)
    private Long updaterAgentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_status_id", insertable = false, updatable = false)
    @JsonIgnore
    private RecordStatus recordStatus = RecordStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_agent_id", insertable = false, updatable = false)
    @JsonIgnore
    private Agent creatorAgent = Agent.SYSTEM;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updater_agent_id", insertable = false, updatable = false)
    @JsonIgnore
    private Agent updaterAgent;


}