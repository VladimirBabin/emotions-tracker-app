package com.github.vbabin.emocheck.state;

import com.github.vbabin.emocheck.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StateLogServiceImplTest {

    private StateLogService stateLogService;

    @Mock
    private StateLogRepository stateLogRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        stateLogService = new StateLogServiceImpl(
                stateLogRepository,
                userRepository
        );
    }


}