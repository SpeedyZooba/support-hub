package com.projects.supporthub.security.recovery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<RecoveryToken, Integer>
{
    List<RecoveryToken> findByUserId(String userId);
    Optional<RecoveryToken> findByTokenString(String tokenString);
    
    @Modifying
    @Transactional
    void deleteByUserId(String userId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM RecoveryToken token WHERE token.isUsed = true OR token.expiryDate < CURRENT_TIMESTAMP")
    public void deleteInvalidTokens();
}