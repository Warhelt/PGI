package com.example.mariusz.pgi;

import org.junit.Test;
import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void addFalse() {
        assertEquals(8, 4 + 2);
    }
    @Test
    public void addtrue() {
        assertEquals(8, 4 + 4);
    }

    @Test
    public void generateFlagsList_isCorrect() {
        Context context = ApplicationProvider.getApplicationContext();
        List<Integer> flagList;
        ArrayList<HashMap<String, String>> exchangeRatesList = new ArrayList<>();
        ExchangeRatesFlagsAdapter exchangeRatesFlagsAdapter = new ExchangeRatesFlagsAdapter(context, exchangeRatesList);
        exchangeRatesFlagsAdapter.generateFlagsList();
        assertTrue(exchangeRatesFlagsAdapter.getFlagList().size() > 5);
    }
}