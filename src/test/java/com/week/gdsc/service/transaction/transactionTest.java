package com.week.gdsc.service.transaction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class transactionTest {

    @Autowired
    private ServiceCode serviceCode;

    @Autowired
    private SomeRepository repository;

    @Test
    public void test() throws CustomException{
        // checked Exception이 발생
        Assertions.assertThrows(CustomException.class, ()->serviceCode.firstMethod());
        Optional<SomeEntity> byId = repository.findById(1L);
        Assertions.assertEquals(null,byId.get().getId());
    }
}
