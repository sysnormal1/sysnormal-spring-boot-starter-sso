package com.sysnormal.starters.security.sso.sso_starter.database.entities.sso;

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
        name = "resource_types",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "resource_types_u1",
                        columnNames = {
                                "(coalesce(parent_id, -1))","record_status_id",
                                "name"
                        }
                )
        }
)
public class ResourceType extends BaseSsoEntity<ResourceType> {

    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;

    public static final long SYSTEM_ID = 1;
    public static final long DATA_ID = 2;
    public static final long TABLE_RESOURCE_ID = 3;
    public static final long COLUMN_ID = 4;
    public static final long MODULE_ID = 5;
    public static final long PACKAGE_ID = 6;
    public static final long CLASS_ID = 7;
    public static final long METHOD_ID = 8;
    public static final long ENDPOINT_ID = 9;
    public static final long URL_ID = 10;
    public static final long SCREEN_ID = 11;
    public static final long COMPONENT_ID = 12;

    public static  final ResourceType SYSTEM = new ResourceType(){{
        setId(SYSTEM_ID);
        setIsSysRec((byte) 1);
        setName("SYSTEM");
    }};

    public static  final ResourceType DATA = new ResourceType(){{
        setId(DATA_ID);
        setIsSysRec((byte) 1);
        setName("DATA");
    }};

    public static  final ResourceType TABLE = new ResourceType(){{
        setId(TABLE_RESOURCE_ID);
        setIsSysRec((byte) 1);
        setName("TABLE");
    }};

    public static  final ResourceType COLUMN = new ResourceType(){{
        setId(COLUMN_ID);
        setIsSysRec((byte) 1);
        setName("COLUMN");
    }};

    public static  final ResourceType MODULE = new ResourceType(){{
        setId(MODULE_ID);
        setIsSysRec((byte) 1);
        setName("MODULE");
    }};

    public static  final ResourceType PACKAGE = new ResourceType(){{
        setId(PACKAGE_ID);
        setIsSysRec((byte) 1);
        setName("PACKAGE");
    }};

    public static  final ResourceType CLASS = new ResourceType(){{
        setId(CLASS_ID);
        setIsSysRec((byte) 1);
        setName("CLASS");
    }};

    public static  final ResourceType METHOD = new ResourceType(){{
        setId(METHOD_ID);
        setIsSysRec((byte) 1);
        setName("METHOD");
    }};

    public static  final ResourceType ENDPOINT = new ResourceType(){{
        setId(ENDPOINT_ID);
        setIsSysRec((byte) 1);
        setName("ENDPOINT");
    }};

    public static  final ResourceType URL = new ResourceType(){{
        setId(URL_ID);
        setIsSysRec((byte) 1);
        setName("URL");
    }};

    public static  final ResourceType SCREEN = new ResourceType(){{
        setId(SCREEN_ID);
        setIsSysRec((byte) 1);
        setName("SCREEN");
    }};

    public static  final ResourceType COMPONENT = new ResourceType(){{
        setId(COMPONENT_ID);
        setIsSysRec((byte) 1);
        setName("COMPONENT");
    }};

}