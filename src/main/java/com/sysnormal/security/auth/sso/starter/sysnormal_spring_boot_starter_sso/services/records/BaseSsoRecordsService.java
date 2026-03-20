package com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.services.records;

import com.sysnormal.commons.core.DefaultDataSwap;
import com.sysnormal.commons.spring.spring_data_utils.DatabaseUtils;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.entities.sso.BaseSsoEntity;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.projections.IdAndNameProjection;
import com.sysnormal.security.auth.sso.starter.sysnormal_spring_boot_starter_sso.database.repositories.sso.BaseSsoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSsoRecordsService<E extends BaseSsoEntity<E>,R extends BaseSsoRepository<E, Long>> {

    private static final Logger logger = LoggerFactory.getLogger(BaseSsoRecordsService.class);

    protected final Class<E> entityClass;
    protected final R repository;

    //private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    ObjectMapper objectMapper;


    public BaseSsoRecordsService(Class<E> entityClass, R repository) {
        this.repository = repository;
        this.entityClass = entityClass;
    }

    public boolean checkIfIsIdAndNameAttributes(JsonNode params) {
        boolean result = false;
        if (params != null && !params.isEmpty()) {
            JsonNode queryParams = params.get("queryParams");
            if (queryParams != null && !queryParams.isEmpty()) {
                JsonNode attributesNode = queryParams.get("attributes");
                List<String> attributes = new ArrayList<>();
                if (attributesNode != null && attributesNode.isArray()) {
                    attributesNode.forEach(n -> attributes.add(n.asText()));
                }
                if (attributes.size() > 0) {
                    if (attributes.size() == 2) {
                        if (attributes.get(0).equalsIgnoreCase("id") &&  attributes.get(1).equalsIgnoreCase("name")) {
                            result = true;
                        }
                    }
                }
            }
        }
        return result;
    }

    public Specification<E> getSpecificationFromJsonNode(JsonNode params) {
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "getSpecificationFromJsonNode");
        Specification<E> result = (root, query, cb) -> null;
        try {
            JsonNode queryParams = params.path("queryParams");
            logger.debug("queryParams: {}", queryParams.toString());
            if (queryParams != null && !queryParams.isEmpty()) {
                JsonNode where = queryParams.path("where");
                logger.debug("where: {}", where.toString());
                if (where != null && !where.isEmpty()) {
                    result = DatabaseUtils.fromWhere(where);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "getSpecificationFromJsonNode");
        return result;
    }

    public DefaultDataSwap get(JsonNode params){
        logger.debug("INIT {}.{}", this.getClass().getSimpleName(), "get");
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            result.data = repository.findAll(getSpecificationFromJsonNode(params));
            result.success = true;
        } catch (Exception e) {
            e.printStackTrace();
            result.setException(e);
        }
        logger.debug("END {}.{}", this.getClass().getSimpleName(), "get");
        return result;
    }

    public DefaultDataSwap getById(Long id, JsonNode params){
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            boolean finded = false;
            boolean isIdAndNameAttributes = checkIfIsIdAndNameAttributes(params);
            if (isIdAndNameAttributes) {
                result.data = repository.findOneById(id, IdAndNameProjection.class);
                finded = true;
            }

            if (!finded) {
                result.data = repository.findById(id);
            }
            result.success = true;
        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    }

    public DefaultDataSwap put(JsonNode params){
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            if (params == null || params.isEmpty()) {
                throw new IllegalArgumentException("missing data");
            }

            // Converte JSON → Entity concreta
            E entity = objectMapper.convertValue(params, entityClass);

            // Persistência
            E saved = repository.save(entity);

            result.data = saved;
            result.success = true;
        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    }

    public DefaultDataSwap patch(Long id, JsonNode params){
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            if (params == null || params.isEmpty() || id == null) {
                throw new IllegalArgumentException("missing data");
            }

            // Converte JSON → Entity concreta
            E entity = objectMapper.convertValue(params, entityClass);
            entity.setId(id);

            E managed = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found"));

            objectMapper.readerForUpdating(managed).readValue(params);
            // Persistência

            E saved = repository.save(managed);

            result.data = saved;
            result.success = true;

        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    }

    public DefaultDataSwap deleteById(Long id, JsonNode params){
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            if (params == null || params.isEmpty() || id == null) {
                throw new IllegalArgumentException("missing data");
            }

            // Converte JSON → Entity concreta
            E entity = objectMapper.convertValue(params, entityClass);
            entity.setId(id);

            E managed = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found"));

            // Persistência
            repository.delete(managed);

            result.success = true;

        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    }

    public DefaultDataSwap delete(JsonNode params){
        DefaultDataSwap result = new DefaultDataSwap();
        try {
            if (params == null || params.isEmpty()) {
                throw new IllegalArgumentException("missing data");
            }

            JsonNode identifiersNode = params.get("identifiers");
            List<Long> identifiers = new ArrayList<>();
            if (identifiersNode != null && identifiersNode.isArray()) {
                identifiersNode.forEach(n -> identifiers.add(n.asLong()));
            }

            // Converte JSON → Entity concreta
            repository.deleteAllById(identifiers);

            result.success = true;

        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    }
}
