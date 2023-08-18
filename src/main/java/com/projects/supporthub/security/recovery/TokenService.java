package com.projects.supporthub.security.recovery;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TokenService 
{
    private final TokenRepository tokenRepo;
    private final static Logger log = LoggerFactory.getLogger(TokenService.class);

    public TokenService(TokenRepository tokenRepo)
    {
        this.tokenRepo = tokenRepo;
    }

    public Optional<RecoveryToken> getTokenFromString(String string)
    {
        return tokenRepo.findByTokenString(string);
    }

    public void saveToken(RecoveryToken token)
    {
        tokenRepo.save(token);
    }

    public RecoveryToken createRecoveryToken(String userId)
    {
        log.info("Inside service method createRecoveryToken.");
        if (!tokenRepo.findByUserId(userId).isEmpty())
        {
            log.info("Purging previously created tokens.");
            tokenRepo.deleteByUserId(userId);
        }
        String token = generate();
        RecoveryToken recoveryToken = new RecoveryToken();
        recoveryToken.setUserId(userId);
        recoveryToken.setTokenString(token);
        recoveryToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        recoveryToken.setIsUsed(false);
        log.info("Saving and returning the token for recovery.");
        tokenRepo.save(recoveryToken);
        return recoveryToken;
    }

    private String generate()
    {
        log.info("Generating string for the recovery token.");
        SecureRandom raw = new SecureRandom();
        byte[] bytes = new byte[32];
        raw.nextBytes(bytes);
        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        log.info("Returning the string.");
        return DigestUtils.sha256Hex(rawToken);
    }

    @Scheduled(cron = "0 */30 * * * *")
    public void purge()
    {
        tokenRepo.deleteInvalidTokens();
    }
}