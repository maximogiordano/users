package com.maximogiordano.users.utils;

import com.maximogiordano.users.entity.InvalidToken;
import com.maximogiordano.users.repository.InvalidTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtUtilsTest {
    static final String KEY = "eD7nH9JHgX1Q2YqVrZG3f7qPp0V57D1jE9vNjT2j2Y8=";
    static final long EXPIRATION = 15 * 60 * 1000;

    JwtUtils jwtUtils;
    InvalidTokenRepository invalidTokenRepository;

    @BeforeEach
    void beforeEach() {
        invalidTokenRepository = mock(InvalidTokenRepository.class);
        jwtUtils = new JwtUtils(KEY, EXPIRATION, invalidTokenRepository);
    }

    @Test
    void testNormalFlow() {
        // generate an access token for a given user
        String token = jwtUtils.generateAccessToken("john.doe@email.com");

        // mock the repo so that the token hasn't been invalidated
        when(invalidTokenRepository.existsById(token)).thenReturn(false);

        // verify the token is valid
        assertTrue(jwtUtils.validateToken(token));

        // check the repo has been used
        verify(invalidTokenRepository).existsById(token);

        // check the user can be extracted
        assertEquals("john.doe@email.com", jwtUtils.extractUsername(token));
    }

    @Test
    void testInvalidatedTokenFlow() {
        // generate an access token for a given user
        String token = jwtUtils.generateAccessToken("john.doe@email.com");

        // mock the repo so that the token has been invalidated
        when(invalidTokenRepository.existsById(token)).thenReturn(true);

        // verify the token is invalid
        assertFalse(jwtUtils.validateToken(token));

        // check the repo has been used
        verify(invalidTokenRepository).existsById(token);
    }

    @Test
    void testCorruptedTokenFlow() {
        // generate an access token for a given user
        String token = jwtUtils.generateAccessToken("john.doe@email.com");

        // mock the repo so that the token hasn't been invalidated
        when(invalidTokenRepository.existsById(token)).thenReturn(false);

        // manipulate the token so that is no longer valid
        token = token.toLowerCase();

        // verify the token is invalid
        assertFalse(jwtUtils.validateToken(token));

        // check the repo has been used
        verify(invalidTokenRepository).existsById(token);
    }

    @Test
    void testInvalidateToken() {
        // given some token
        String token = "some token...";

        // and an entity with the given token
        InvalidToken invalidToken = new InvalidToken();
        invalidToken.setValue(token);

        // when the given token is invalidated
        jwtUtils.invalidateToken(token);

        // then the token is stored as invalid
        verify(invalidTokenRepository).save(invalidToken);
    }
}
