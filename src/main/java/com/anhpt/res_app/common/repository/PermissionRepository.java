package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.Permission;
import com.anhpt.res_app.common.entity.Role;
import com.anhpt.res_app.common.entity.key.PermissionId;
import com.anhpt.res_app.common.enums.FeatureMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, PermissionId> {

    @Query("""
        SELECT COUNT(p) FROM Permission p
        WHERE p.id.roleId = :roleId
        AND p.id.featureUri = :featureUri
        AND p.id.featureMethod = :featureMethod
    """)
    long countByRoleIdAndFeatureUriAndFeatureMethod(
        @Param("roleId") Integer roleId,
        @Param("featureUri") String featureUri,
        @Param("featureMethod") FeatureMethod featureMethod
    );

    List<Permission> findByRole(Role role);
}
