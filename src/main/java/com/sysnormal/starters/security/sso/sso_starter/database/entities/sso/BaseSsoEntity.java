package com.sysnormal.starters.security.sso.sso_starter.database.entities.sso;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sysnormal.libs.db.entities.base_entities.BaseCommonEntityWithParent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

/**
 * base entity of sso
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseSsoEntity<T extends BaseSsoEntity<T>>  extends BaseCommonEntityWithParent<T> {

    @Column(name = "record_status_id", nullable = false)
    @ColumnDefault(RecordStatus.ACTIVE_ID+"")
    private Long recordStatusId = RecordStatus.ACTIVE_ID;

    @Column(name = "creator_agent_id", nullable = false)
    @ColumnDefault(Agent.SYSTEM_ID+"")
    private Long creatorAgentId = Agent.SYSTEM_ID;

    @Column(name = "updater_agent_id")
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