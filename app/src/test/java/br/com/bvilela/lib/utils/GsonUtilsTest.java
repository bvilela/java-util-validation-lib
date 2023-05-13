package br.com.bvilela.lib.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class GsonUtilsTest {

    @Test
    void shouldGetGson() {
        assertNotNull(GsonUtils.getGson());
    }
}
