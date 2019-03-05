package ru.javawebinar.topjava;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class ParentTestClass {

    @Rule
    public TimeForTestsLogging stopwatch = new TimeForTestsLogging();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

}
