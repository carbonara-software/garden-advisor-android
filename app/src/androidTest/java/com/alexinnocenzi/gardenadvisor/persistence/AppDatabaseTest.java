package com.alexinnocenzi.gardenadvisor.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;


import com.alexinnocenzi.gardenadvisor.persistence.entity.FunFact;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class AppDatabaseTest {

    private AppDatabase appDatabase;

    @Before
    public void setup() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        appDatabase = AppDatabase.getDatabase(appContext);
    }

    @Test
    public void appDatabase() {
        assertNotNull(appDatabase);
        assertNotNull(appDatabase.funFactDao());
        assertNotNull(appDatabase.gardenDao());
    }

    @Test
    public void funFactsPopulated() {
        List<FunFact> populatedFunFacts = appDatabase.funFactDao().getFunFacts().blockingFirst();
        assertNotNull(populatedFunFacts);
        assertEquals(100, populatedFunFacts.size());
    }

    @After
    public void teardown() {
        appDatabase.close();
    }

}
