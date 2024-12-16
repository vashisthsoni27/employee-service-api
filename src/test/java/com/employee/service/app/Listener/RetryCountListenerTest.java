package com.employee.service.app.Listener;

import com.employee.service.app.service.RetryCountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@SpringBootTest
class RetryCountListenerTest {

    @Mock
    private RetryCountService retryCountService;

    private RetryCountListener retryCountListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        retryCountListener = new RetryCountListener(retryCountService);
    }

    @Test
    void onSuccess_ShouldResetRetryCount() {
        RetryContext context = mock(RetryContext.class);
        RetryCallback<Object, Throwable> callback = mock(RetryCallback.class);

        // Act
        retryCountListener.onSuccess(context, callback, "SuccessResult");

        // Assert
        verify(retryCountService, times(1)).resetRetryCount();
    }

    @Test
    void onError_RetryCountBelowLimit_ShouldIncrementRetryCount() {
        // Arrange
        RetryContext context = mock(RetryContext.class);
        RetryCallback<Object, Throwable> callback = mock(RetryCallback.class);
        when(retryCountService.incrementRetryCount()).thenReturn(2); // Simulate retry count below limit

        // Act
        retryCountListener.onError(context, callback, new RuntimeException("TestException"));

        // Assert
        verify(retryCountService, times(1)).incrementRetryCount();
        verify(retryCountService, never()).resetRetryCount();
    }

    @Test
    void onError_RetryCountAtLimit_ShouldThrowIllegalStateException() {
        // Arrange
        RetryContext context = mock(RetryContext.class);
        RetryCallback<Object, Throwable> callback = mock(RetryCallback.class);
        when(retryCountService.incrementRetryCount()).thenReturn(3); // Simulate retry count at limit

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
                retryCountListener.onError(context, callback, new RuntimeException("TestException"))
        );

        verify(retryCountService, times(1)).incrementRetryCount();
        verify(retryCountService, times(1)).resetRetryCount(); // Ensure resetRetryCount is called after reaching limit
    }
}

