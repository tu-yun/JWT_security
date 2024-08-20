package com.xi.fmcs.models.sendAuth;

import com.xi.fmcs.models.sendAuth.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailAuthRepository extends JpaRepository<EmailAuth, Integer> {
    public Optional<EmailAuth> findByEMAIL(String email);

}
