package com.example.oatnote;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
class OatNoteApplicationTests {

    @MockBean
    private RedissonClient redissonClient;

    @Test
    void contextLoads() {
    }
}
