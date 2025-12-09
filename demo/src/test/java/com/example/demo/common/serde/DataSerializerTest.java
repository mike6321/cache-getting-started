package com.example.demo.common.serde;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class DataSerializerTest  {

    @Test
    void serde() {
        MyData myData = new MyData("id", "data");
        String serialized = DataSerializer.serializeOrException(myData);

        MyData deserialized = DataSerializer.deserializeOrNull(serialized, MyData.class);
        Assertions.assertEquals(deserialized, myData);
    }

    record MyData(String id, String data) {

    }

}
