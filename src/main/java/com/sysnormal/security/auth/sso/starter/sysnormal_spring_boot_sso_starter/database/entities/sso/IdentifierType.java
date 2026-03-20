package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_sso_starter.database.entities.sso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "identifier_types",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "identifier_types_u1",
                        columnNames = {
                                "(coalesce(parent_id, -1))","record_status_id",
                                "name"
                        }
                )
        }
)
public class IdentifierType extends BaseSsoEntity<IdentifierType> {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "process_to_validate", length = Integer.MAX_VALUE)
    private String processToValidate;

    @Column(name = "notes", length = Integer.MAX_VALUE)
    private String notes;


    public static final long IDENTIFIER_ID = 1;
    public static final long CODE_ID = 2;
    public static final long CNPJ_ID = 3;
    public static final long CPF_ID = 4;
    public static final long EMAIL_ID = 6;
    public static final long PLATE_ID = 12;

    public static final IdentifierType IDENTIFIER = new IdentifierType(){{
        setId(IDENTIFIER_ID);
        setIsSysRec((byte) 1);
        setName("IDENTIFIER");
    }};
    public static final IdentifierType CODE = new IdentifierType(){{
        setId(CODE_ID);
        setIsSysRec((byte) 1);
        setName("CODE");
    }};
    public static final IdentifierType CNPJ = new IdentifierType(){{
        setId(CNPJ_ID);
        setIsSysRec((byte) 1);
        setName("CNPJ");
    }};
    public static final IdentifierType CPF = new IdentifierType(){{
        setId(CPF_ID);
        setIsSysRec((byte) 1);
        setName("CPF");
    }};
    public static final IdentifierType EMAIL = new IdentifierType(){{
        setId(EMAIL_ID);
        setIsSysRec((byte) 1);
        setName("EMAIL");
    }};
    public static final IdentifierType PLATE = new IdentifierType(){{
        setId(PLATE_ID);
        setIsSysRec((byte) 1);
        setName("PLATE");
    }};

}


