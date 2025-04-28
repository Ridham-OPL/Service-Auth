package com.telephone.directory.service.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

public interface MapperHelper {

    static <T, U> T convertor(U value, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(value, clazz);
    }

    static <T, U> List<T> convertor(List<U> values, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        return values.stream().map(value -> mapper.convertValue(value, clazz)).collect(Collectors.toList());
    }

}
