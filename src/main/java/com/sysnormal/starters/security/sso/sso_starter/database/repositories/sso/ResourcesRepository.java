package com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso;

import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.Resource;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Resources repository
 *
 * @author aalencarvz1
 * @version 1.0.0
 */
@Repository
public interface ResourcesRepository extends BaseSsoRepository<Resource, Long>, ResourcesRepositoryCustom  {


}

