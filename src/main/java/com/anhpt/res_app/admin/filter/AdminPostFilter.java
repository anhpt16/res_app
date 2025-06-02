package com.anhpt.res_app.admin.filter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminPostFilter {

    @PersistenceContext
    private EntityManager entityManager;

}
