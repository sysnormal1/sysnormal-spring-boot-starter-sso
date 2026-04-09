package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Getter
@Setter
@Entity
@Table(
        name = "resources",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "resources_u1",
                        columnNames = {
                                "(coalesce(parent_id, -1))","record_status_id",
                                "system_id",
                                "resource_type_id",
                                "name"
                        }
                )
        }
)
public class Resource extends BaseSsoEntity<Resource> {

    @Column(name = "system_id", nullable = false)
    private Long systemId;

    @Column(name = "resource_type_id", nullable = false)
    private Long resourceTypeId;

    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "resource_path", length = Integer.MAX_VALUE)
    private String resourcePath;

    @Column(name = "icon", length = Integer.MAX_VALUE)
    private String icon;

    @Column(name = "numeric_order", nullable = false)
    @ColumnDefault("0")
    private Integer numericOrder = 0;

    @Column(name = "show_in_menu", nullable = false)
    @ColumnDefault("1")
    @Check(constraints = "show_in_menu in (0,1)")
    private byte showInMenu = 1;

    @Column(name = "json_data", length = Integer.MAX_VALUE)
    private String jsonData;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_id", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private System system;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "resource_type_id", insertable = false, updatable = false)
    @JsonIgnore
    private ResourceType resourceType;

    @OneToMany(mappedBy = "resource", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ResourcePermission> resourcePermissions;




    public static final long SYSTEMS_ID = 10;
    public static final long ACCESS_PROFILES_ID = 15;
    public static final long AGENTS_ID = 20;
    public static final long RESOURCES_ID = 25;
    public static final long RESOURCE_PERMISSIONS_ID = 30;
    public static final long RELATIONSHIPS_ID = 40;
    public static final long AGENTS_X_ACCESS_PROFILES_X_SYSTEMS_ID = 41;

    public static  final Resource SYSTEMS = new Resource(){{
        setId(SYSTEMS_ID);
        setIsSysRec((byte) 1);
        setSystemId(System.SSO_WEBCLIENT_ID);
        setResourceTypeId(ResourceType.URL_ID);
        setName("SYSTEMS");
        setResourcePath("/records/systems");
        setNumericOrder(Integer.valueOf(String.valueOf(SYSTEMS_ID)));
    }};

    public static  final Resource ACCESS_PROFILES = new Resource(){{
        setId(ACCESS_PROFILES_ID);
        setIsSysRec((byte) 1);
        setSystemId(System.SSO_WEBCLIENT_ID);
        setResourceTypeId(ResourceType.URL_ID);
        setName("ACCESS_PROFILES");
        setResourcePath("/records/access_profiles");
        setNumericOrder(Integer.valueOf(String.valueOf(ACCESS_PROFILES_ID)));
    }};

    public static  final Resource AGENTS = new Resource(){{
        setId(AGENTS_ID);
        setIsSysRec((byte) 1);
        setSystemId(System.SSO_WEBCLIENT_ID);
        setResourceTypeId(ResourceType.URL_ID);
        setName("AGENTS");
        setResourcePath("/records/agents");
        setNumericOrder(Integer.valueOf(String.valueOf(AGENTS_ID)));
    }};

    public static  final Resource RESOURCES = new Resource(){{
        setId(RESOURCES_ID);
        setIsSysRec((byte) 1);
        setSystemId(System.SSO_WEBCLIENT_ID);
        setResourceTypeId(ResourceType.URL_ID);
        setName("RESOURCES");
        setResourcePath("/records/resources");
        setNumericOrder(Integer.valueOf(String.valueOf(RESOURCES_ID)));
    }};

    public static  final Resource RESOURCE_PERMISSIONS = new Resource(){{
        setId(RESOURCE_PERMISSIONS_ID);
        setIsSysRec((byte) 1);
        setSystemId(System.SSO_WEBCLIENT_ID);
        setResourceTypeId(ResourceType.URL_ID);
        setName("RESOURCE_PERMISSIONS");
        setResourcePath("/records/resource_permissions");
        setNumericOrder(Integer.valueOf(String.valueOf(RESOURCE_PERMISSIONS_ID)));
    }};

    public static  final Resource RELATIONSHIPS = new Resource(){{
        setId(RELATIONSHIPS_ID);
        setIsSysRec((byte) 1);
        setSystemId(System.SSO_WEBCLIENT_ID);
        setResourceTypeId(ResourceType.URL_ID);
        setName("RELATIONSHIPS");
        setResourcePath("/records/relationships");
        setNumericOrder(Integer.valueOf(String.valueOf(RELATIONSHIPS_ID)));
    }};

    public static  final Resource AGENTS_X_ACCESS_PROFILES_X_SYSTEMS = new Resource(){{
        setId(AGENTS_X_ACCESS_PROFILES_X_SYSTEMS_ID);
        setIsSysRec((byte) 1);
        setParentId(RELATIONSHIPS_ID);
        setSystemId(System.SSO_WEBCLIENT_ID);
        setResourceTypeId(ResourceType.URL_ID);
        setName("AGENTS_X_ACCESS_PROFILES_X_SYSTEMS");
        setResourcePath("/records/relationships/agents_x_access_profiles_x_systems");
        setNumericOrder(Integer.valueOf(String.valueOf(AGENTS_X_ACCESS_PROFILES_X_SYSTEMS_ID)));
    }};



}
