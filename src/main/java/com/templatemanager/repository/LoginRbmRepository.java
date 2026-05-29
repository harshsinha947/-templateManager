package com.templatemanager.repository;

import com.templatemanager.model.LoginRbm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LoginRbmRepository extends JpaRepository<LoginRbm, Long> {

    @Query("SELECT u FROM LoginRbm u WHERE LOWER(u.username) = LOWER(:username) AND u.pass = :pass")
    Optional<LoginRbm> findByUsernameAndPass(@Param("username") String username, @Param("pass") String pass);

    @Query("SELECT u FROM LoginRbm u WHERE LOWER(u.username) = LOWER(:username)")
    Optional<LoginRbm> findByUsername(@Param("username") String username);
}
