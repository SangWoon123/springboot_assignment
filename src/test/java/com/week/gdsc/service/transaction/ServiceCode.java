package com.week.gdsc.service.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class ServiceCode {

    @Autowired
    private SomeRepository repository;

    @Transactional
    public void firstMethod() throws CustomException{
        repository.save(new SomeEntity("name"));
        secondMethod();
    }

    private void secondMethod() throws CustomException{
        throw new CustomException("Check Exception 발생했습니다.");
    }
}
