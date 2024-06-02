package com.carbonara.gardenadvisor.persistence.dao;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.carbonara.gardenadvisor.persistence.AppDatabase;
import com.carbonara.gardenadvisor.persistence.entity.FunFact;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class FunFactDaoTest {

    private AppDatabase inMemoryDb;
    private FunFactDao dao;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        inMemoryDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = inMemoryDb.funFactDao();
    }

    @After
    public void tearDown() {
        inMemoryDb.close();
    }

    @Test
    public void insertFunFact() throws InterruptedException {
        FunFact funFact = new FunFact(1L, "test");

        dao.insertFunFact(funFact).blockingAwait();
        FunFact saved = dao.getFunFact(1L).blockingFirst();

        assertEquals(funFact, saved);
    }

    @Test
    public void insertFunFacts() throws InterruptedException {
        FunFact funFact1 = new FunFact(1L, "test");
        FunFact funFact2 = new FunFact(2L, "test");
        List<FunFact> funFacts = Arrays.asList(funFact1, funFact2);

        dao.insertFunFacts(funFacts).blockingAwait();
        List<FunFact> saved = dao.getFunFacts().blockingFirst();

        assertEquals(funFacts, saved);
    }
}
