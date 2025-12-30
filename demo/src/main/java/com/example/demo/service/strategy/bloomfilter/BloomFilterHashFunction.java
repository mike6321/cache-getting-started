package com.example.demo.service.strategy.bloomfilter;

@FunctionalInterface
public interface BloomFilterHashFunction {

    long hash(String value);

}
