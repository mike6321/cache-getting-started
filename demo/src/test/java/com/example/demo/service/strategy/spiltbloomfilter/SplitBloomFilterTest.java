package com.example.demo.service.strategy.spiltbloomfilter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SplitBloomFilterTest {

    @Test
    void create() {
        SplitBloomFilter splitBloomFilter = SplitBloomFilter.create("testId", 1000, 0.01);
        System.out.println("splitBloomFilter = " + splitBloomFilter);

        long bitSize = splitBloomFilter.getBloomFilter().getBitSize();
        System.out.println("bitSize = " + bitSize); // 9586

        // (9586 -1) / 1024 + 1
        long splitCount = splitBloomFilter.getSplitCount();
        assertThat(splitCount).isEqualTo(10);
    }

    @Test
    void findSplitIndex() {
        // given
        SplitBloomFilter splitBloomFilter = SplitBloomFilter.create("testId", 1000, 0.01);

        // when, then
        assertThat(
                splitBloomFilter.findSplitIndex(0L)
        ).isZero();

        assertThat(
                splitBloomFilter.findSplitIndex(1023L)
        ).isZero();

        assertThat(
                splitBloomFilter.findSplitIndex(1024L)
        ).isEqualTo(1);

        assertThat(
                splitBloomFilter.findSplitIndex(9585L)
        ).isEqualTo(9);
    }

    @Test
    void findSplitIndex_shouldThrowException_whenHashedIndexExceedsCapacity() {
        SplitBloomFilter splitBloomFilter = SplitBloomFilter.create("testId", 1000, 0.01);
        assertThatThrownBy(() -> splitBloomFilter.findSplitIndex(9586L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void calculateBitSize() {
        SplitBloomFilter splitBloomFilter = SplitBloomFilter.create("testId", 1000, 0.01);
        long splitCount = splitBloomFilter.getSplitCount(); // 10
        for (int splitIndex = 0; splitIndex < splitCount - 1; splitIndex++) {
            assertThat(
                    splitBloomFilter.calculateSplitBitSize(splitIndex)
            ).isEqualTo(SplitBloomFilter.BIT_SPLIT_UNIT);
        }

        long bitSize = splitBloomFilter.getBloomFilter().getBitSize(); // 9586
        assertThat(splitBloomFilter.calculateSplitBitSize(splitCount - 1))
                .isEqualTo(bitSize - SplitBloomFilter.BIT_SPLIT_UNIT * (splitCount - 1));
    }
    
}
