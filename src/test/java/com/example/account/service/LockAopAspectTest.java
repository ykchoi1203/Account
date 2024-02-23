package com.example.account.service;

import com.example.account.dto.UseBalance;
import com.example.account.exception.AccountException;
import com.example.account.type.ErrorCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.account.type.ErrorCode.ACCOUNT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LockAopAspectTest {
    @Mock
    private LockService lockService;

    @Mock
    private ProceedingJoinPoint pjp;

    @InjectMocks
    private LockAopAspect lockAopAspect;

    @Test
    void lockAndUnLock() throws Throwable {
        // given
        ArgumentCaptor<String> lockArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> unlockArgumentCaptor = ArgumentCaptor.forClass(String.class);
        UseBalance.Request request = new UseBalance.Request(123L, "1234567890", 1000L);

        // when
        lockAopAspect.aroundMethod(pjp, request);

        // then
        verify(lockService, times(1))
                .lock(lockArgumentCaptor.capture());
        verify(lockService, times(1))
                .unlock(unlockArgumentCaptor.capture());
        assertEquals("1234567890", lockArgumentCaptor.getValue());
        assertEquals("1234567890", unlockArgumentCaptor.getValue());

    }

    @Test
    void lockAndUnlock_evenIfThrow() throws Throwable {
        // given
        ArgumentCaptor<String> lockArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> unlockArgumentCaptor = ArgumentCaptor.forClass(String.class);
        UseBalance.Request request = new UseBalance.Request(123L, "1234567890", 1000L);
        given(pjp.proceed())
                .willThrow(new AccountException(ACCOUNT_NOT_FOUND));

        // when
        assertThrows(AccountException.class,
                () -> lockAopAspect.aroundMethod(pjp, request));
        // then
        verify(lockService, times(1))
                .lock(lockArgumentCaptor.capture());
        verify(lockService, times(1))
                .unlock(unlockArgumentCaptor.capture());
        assertEquals("1234567890", lockArgumentCaptor.getValue());
        assertEquals("1234567890", unlockArgumentCaptor.getValue());

    }


}