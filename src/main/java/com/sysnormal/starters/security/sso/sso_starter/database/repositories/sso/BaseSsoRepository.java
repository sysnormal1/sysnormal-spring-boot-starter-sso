package com.sysnormal.starters.security.sso.sso_starter.database.repositories.sso;

import com.sysnormal.libs.db.repositories.base_repositories.BaseCommonRepositoryWithParent;
import com.sysnormal.starters.security.sso.sso_starter.database.entities.sso.BaseSsoEntity;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * base sso repository
 *
 * @author aalencarvz1
 * @version 1.0.0
 */


@NoRepositoryBean
public interface BaseSsoRepository<E extends BaseSsoEntity<E>, ID> extends BaseCommonRepositoryWithParent<E, ID> {
    <T> List<T> findAllBy(Class<T> type);

    <T> Optional<T> findOneById(Long id, Class<T> type);

}