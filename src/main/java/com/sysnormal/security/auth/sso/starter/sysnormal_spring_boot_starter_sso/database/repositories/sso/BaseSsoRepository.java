package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso;

import com.sysnormal.data.base_data_model.repositories.BaseCommonRepository;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.BaseSsoEntity;
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
public interface BaseSsoRepository<E extends BaseSsoEntity<E>, ID> extends BaseCommonRepository<E, ID> {
    <T> List<T> findAllBy(Class<T> type);

    <T> Optional<T> findOneById(Long id, Class<T> type);

}