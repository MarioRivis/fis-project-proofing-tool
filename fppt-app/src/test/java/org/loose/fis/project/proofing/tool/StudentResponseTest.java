package org.loose.fis.project.proofing.tool;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentResponseTest {

    private StudentResponse studentResponse;

    @BeforeEach
    void setUp() {
        studentResponse = new StudentResponse();
        studentResponse.setRepoUrl("https://github.com/gogmaster/javaFX-gradle/dsa/dsa");
    }

    @Test
    void getOwner() {
        assertEquals("gogmaster", studentResponse.getOwner());
    }

    @Test
    void getRepo() {
        assertEquals("javaFX-gradle", studentResponse.getRepo());
    }
}
