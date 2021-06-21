package com.lb.mykijava;

import com.lb.mykijava.utils.PasswordManager;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void password_isValid() {
        String x = PasswordManager.generatePassword(PasswordManager.complexity.HIGH, 10);
        System.out.println(x);
        assertTrue(PasswordManager.isValid(x));

    }
}