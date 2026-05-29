package com.templatemanager.repository;

import com.templatemanager.model.LoginMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoginMappingRepository extends JpaRepository<LoginMapping, Long> {

    // Case-insensitive match to handle Username column casing in DB
    @Query("SELECT m FROM LoginMapping m WHERE LOWER(m.username) = LOWER(:username)")
    List<LoginMapping> findByUsername(@Param("username") String username);
}
