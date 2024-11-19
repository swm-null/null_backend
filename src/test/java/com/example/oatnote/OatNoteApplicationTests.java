package com.example.oatnote;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.oatnote.config.TestConfig;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class})
class OatNoteApplicationTests {

    @Test
    void contextLoads() {
    }
}
