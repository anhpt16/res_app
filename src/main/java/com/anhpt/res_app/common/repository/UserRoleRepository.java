package com.anhpt.res_app.common.repository;

import com.anhpt.res_app.common.entity.User;
import com.anhpt.res_app.common.entity.UserRole;
import com.anhpt.res_app.common.entity.key.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    @Transactional
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.id.userId = :userId AND ur.id.roleId = :roleId")
    void deleteByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Integer roleId);

    List<UserRole> findByUser(User user);

    @Query("""
        SELECT ur FROM UserRole ur
        WHERE ur.id.userId = :userId
    """)
    List<UserRole> findByUserId(@Param("userId") Long userId);
}
