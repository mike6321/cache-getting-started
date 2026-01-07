package com.example.demo.service.strategy.spiltbloomfilter;

import com.example.demo.service.strategy.bloomfilter.BloomFilter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SplitBloomFilter {

    private String id;
    private BloomFilter bloomFilter;
    private long splitCount;

    // public static final long BIT_SPLIT_UNIT = 1L << 32 // 2^132
    public static final long BIT_SPLIT_UNIT = 1L << 10; // 2^10 = 1024

    public static SplitBloomFilter create(String id, long dataCount , double falsePositiveRate) {
        BloomFilter bloomFilter = BloomFilter.create(id, dataCount, falsePositiveRate);
        /**
         * 비트 사이즈가 1024 라면? (1024 - 1) / 1024 + 1 = 1개의 SPLIT
         * 비트 사이즈가 1025 라면? (1025 - 1) / 1024 + 1 = 2개의 SPLIT
         * */
        long splitCount = (bloomFilter.getBitSize() - 1) / BIT_SPLIT_UNIT + 1;

        SplitBloomFilter splitBloomFilter = new SplitBloomFilter();
        splitBloomFilter.id = id;
        splitBloomFilter.bloomFilter = bloomFilter;
        splitBloomFilter.splitCount = splitCount;

        return splitBloomFilter;
    }

    public long findSplitIndex(Long hashedIndex) {
        /**
         * hashedIndex == 1023 이라면? 0 번째 SPLIT
         * hashedIndex == 1024 이라면? 1  번째 SPLIT
         * */
        if (hashedIndex >= bloomFilter.getBitSize()) {
            throw new IllegalArgumentException("hashedIndex out of range");
        }
        return hashedIndex / BIT_SPLIT_UNIT;
    }

    public long calculateSplitBitSize(long splitIndex) {
        if (splitIndex == splitCount - 1) {
            /**
             * bitSize = 1025, splitCount = 2, splitIndex = 1
             * 1025 - (1024 * 1)
             * */
            return bloomFilter.getBitSize() - (BIT_SPLIT_UNIT * splitIndex);
        }

        return BIT_SPLIT_UNIT;
    }
}
