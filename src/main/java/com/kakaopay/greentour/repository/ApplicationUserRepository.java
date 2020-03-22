package com.kakaopay.greentour.repository;

import com.kakaopay.greentour.domain.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long>{
    Optional<ApplicationUser> findByUsername(String username);
}
