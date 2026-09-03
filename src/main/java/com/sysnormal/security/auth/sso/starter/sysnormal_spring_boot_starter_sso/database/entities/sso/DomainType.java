package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
        name = "domain_types",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "domain_types_u1",
                        columnNames = {
                                "(coalesce(parent_id, -1))","record_status_id",
                                "name"
                        }
                )
        }
)
public class DomainType extends BaseSsoEntity<DomainType> {

    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;

    public static final long ECOSYSTEM_ID = 1;
    public static final long APPLICATION_ID = 2;
    public static final long SERVICE_ID = 3;
    public static final long DATABASE_ID = 4;
    public static final long CLUSTER_ID = 5;

    public static  final DomainType ECOSYSTEM = new DomainType(){{
        setId(ECOSYSTEM_ID);
        setIsSysRec((byte) 1);
        setName("ECOSYSTEM");
        setDescription("Conjunto de dados e sistemas de um mesmo dominio de negocio");
    }};

    public static  final DomainType APPLICATION = new DomainType(){{
        setId(APPLICATION_ID);
        setIsSysRec((byte) 1);
        setName("APPLICATION");
        setDescription("Programa com que uma pessoa interage");
    }};

    public static  final DomainType SERVICE = new DomainType(){{
        setId(SERVICE_ID);
        setIsSysRec((byte) 1);
        setName("SERVICE");
        setDescription("Programa consumido por outro programa");
    }};

    public static  final DomainType DATABASE = new DomainType(){{
        setId(DATABASE_ID);
        setIsSysRec((byte) 1);
        setName("DATABASE");
        setDescription("Banco de dados ou schema");
    }};

    public static  final DomainType CLUSTER = new DomainType(){{
        setId(CLUSTER_ID);
        setIsSysRec((byte) 1);
        setName("CLUSTER");
        setDescription("Agrupamento de infraestrutura");
    }};

}
