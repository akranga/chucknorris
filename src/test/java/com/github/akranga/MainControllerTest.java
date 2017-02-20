package com.github.akranga;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MainControllerTest {

    @InjectMocks
    private MainController controller;

    @Mock
    private FactGenerator factGenerator;

    @Test
    public void testGimmeFact() {
        when(factGenerator.gimme()).thenReturn("Test fact");

        String fact = controller.gimmeFact();

        assertThat(fact, equalTo("Test fact\n"));
    }

}
