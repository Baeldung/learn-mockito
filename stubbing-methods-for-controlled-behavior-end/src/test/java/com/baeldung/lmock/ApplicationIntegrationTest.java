package com.baeldung.lmock;

import org.junit.jupiter.api.Test;

class ApplicationIntegrationTest {

    @Test
    void mainAppMethodIntegrationTest() {
        LmockApp.main(new String[] {});
    }
}
