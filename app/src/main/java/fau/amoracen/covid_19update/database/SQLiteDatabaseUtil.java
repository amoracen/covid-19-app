package fau.amoracen.covid_19update.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fau.amoracen.covid_19update.data.CountryData;
import fau.amoracen.covid_19update.data.GlobalStats;
import fau.amoracen.covid_19update.data.USStatesData;

/**
 * Class responsible for creating table, inserting values, updating rows, and reading rows
 */
public class SQLiteDatabaseUtil {

    private SQLiteDatabase myDatabase;

    /**
     * Constructor
     *
     * @param context application context
     * @param name    database name
     */
    public SQLiteDatabaseUtil(Context context, String name) {
        myDatabase = context.openOrCreateDatabase(name, Context.MODE_PRIVATE, null);
    }

    /**
     * Close Database
     */
    public void close() {
        myDatabase.close();
    }

    /**
     * Create Table
     *
     * @param query query to execute
     */
    public void createTable(String query) {
        try {
            myDatabase.execSQL(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if table is Empty
     *
     * @param table table name
     * @return true if table is empty, false otherwise
     */
    private boolean isTableEmpty(String table) {
        boolean empty = true;
        Cursor cur = myDatabase.rawQuery("SELECT COUNT(*) FROM " + table, null);
        if (cur != null && cur.moveToFirst()) {
            int count = cur.getInt(0);
            Log.i("Count", String.valueOf(count));
            empty = (count == 0);
        }
        assert cur != null;
        cur.close();
        return empty;
    }

    /**
     * Insert to table if table is empty
     * Update table if not Empty
     *
     * @param globalStats an instance of GlobalStats
     */
    public void checkGlobalStatsTable(GlobalStats globalStats) {
        if (isTableEmpty("GlobalStats")) {
            Log.i("Tag", "GlobalStats Empty Table -> Go to Insert");
            insertToGlobalStatsTable(globalStats);
            return;
        }
        Log.i("Tag", "GlobalStats Table not Empty -> Go to Update");
        updateGlobalStatsTable(globalStats);
    }

    /**
     * Insert to table if table is empty
     * Update table if not Empty
     *
     * @param countryData an instance of CountryData
     */
    public void checkCountryDataTable(List<CountryData> countryData) {
        if (!isTableEmpty("CountryData")) {
            try {
                Log.i("Tag", "CountryData Table not Empty -> Delete");
                myDatabase.execSQL("DELETE FROM CountryData");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Log.i("Tag", "CountryData Empty Table -> Go to Insert");
        for (int i = 0; i < countryData.size(); i++) {
            insertToCountryDataTable(countryData.get(i));
        }
    }

    /**
     * Insert to table if table is empty
     * Update table if not Empty
     *
     * @param response an instance of USStatesData
     */
    public void checkUSStatesDataTable(List<USStatesData> response) {
        if (!isTableEmpty("USStatesData")) {
            try {
                Log.i("Tag", "USStatesData Table not Empty -> Delete");
                myDatabase.execSQL("DELETE FROM USStatesData");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Log.i("Tag", "USStatesData Empty Table -> Go to Insert");
        for (int i = 0; i < response.size(); i++) {
            insertToUSStatesDataTable(response.get(i));
        }
    }

    /**
     * Insert to table if table is empty
     * Update table if not Empty
     *
     * @param response a string from a JSONObject
     */
    public void checkHistoricalAllTable(String days, String response) {
        if (!isTableEmpty("HistoricalAll") && !isTableEmpty("HistoricalAllDays")) {
            try {
                Log.i("Tag", "HistoricalAll Table not Empty -> Delete");
                myDatabase.execSQL("DELETE FROM HistoricalAll");
                Log.i("Tag", "HistoricalAllDays Table not Empty -> Delete");
                myDatabase.execSQL("DELETE FROM HistoricalAllDays");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Log.i("Tag", "HistoricalAll Empty Table -> Go to Insert");
        insertToHistoricalAllTable(response);
        Log.i("Tag", "HistoricalAllDays Empty Table -> Go to Insert");
        insertToHistoricalAllDaysTable(days);
    }

    /**
     * Insert to table if table is empty
     * Update table if not Empty
     *
     * @param response a string from a JSONObject
     */
    public void checkHistoricalCountryTable(String days, String country, String response) {
        if (!isTableEmpty("HistoricalCountry") && !isTableEmpty("HistoricalCountryDays")) {
            try {
                Log.i("Tag", "HistoricalCountry Table not Empty -> Delete");
                myDatabase.execSQL("DELETE FROM HistoricalCountry WHERE country = '" + country + "'");
                Log.i("Tag", "HistoricalCountryDays Table not Empty -> Delete");
                myDatabase.execSQL("DELETE FROM HistoricalCountryDays");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Log.i("Tag", "HistoricalCountry Empty Table -> Go to Insert");
        insertToHistoricalCountryTable(country, response);
        Log.i("Tag", "HistoricalCountryDays Empty Table -> Go to Insert");
        insertToHistoricalCountryDaysTable(days);
    }

    /**
     * Inset to HistoricalAll Table
     *
     * @param response a string
     */
    private void insertToHistoricalAllTable(String response) {
        String query = "Insert into HistoricalAll (dates_values) Values" +
                "('" + response + "')";
        try {
            myDatabase.execSQL(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inset to HistoricalCountry Table
     *
     * @param response a string
     */
    private void insertToHistoricalCountryTable(String country, String response) {
        String query = "Insert into HistoricalCountry (dates_values,country) Values" +
                "('" + response + "','" + country + "')";
        try {
            myDatabase.execSQL(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inset to HistoricalAllDays Table
     *
     * @param days a string
     */
    private void insertToHistoricalAllDaysTable(String days) {
        String query = "Insert into HistoricalAllDays (days) Values" +
                "('" + days + "')";
        try {
            myDatabase.execSQL(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inset to HistoricalCountryDays Table
     *
     * @param days a string
     */
    private void insertToHistoricalCountryDaysTable(String days) {
        String query = "Insert into HistoricalCountryDays (days) Values" +
                "('" + days + "')";
        try {
            myDatabase.execSQL(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get Data from HistoricalAll Table
     */
    public String getDataFromHistoricalAllTable() {
        String dates_values = null;
        try {
            Cursor c = myDatabase.rawQuery("SELECT * FROM HistoricalAll", null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                dates_values = c.getString(c.getColumnIndex("dates_values"));
                c.moveToNext();
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dates_values;
    }

    /**
     * Get Data from HistoricalCountry Table
     */
    public String getDataFromHistoricalCountryTable(String country) {
        String dates_values = null;
        try {
            Cursor c = myDatabase.rawQuery("SELECT * FROM HistoricalCountry  WHERE country = '" + country + "'", null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                dates_values = c.getString(c.getColumnIndex("dates_values"));
                c.moveToNext();
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dates_values;
    }

    /**
     * Get Data from HistoricalAllDays Table
     */
    public String getDataFromHistoricalAllDaysTable() {
        String days = null;
        try {
            Cursor c = myDatabase.rawQuery("SELECT * FROM HistoricalAllDays", null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                days = c.getString(c.getColumnIndex("days"));
                c.moveToNext();
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * Get Data from HistoricalCountryDays Table
     */
    public String getDataFromHistoricalCountryDaysTable() {
        String days = null;
        try {
            Cursor c = myDatabase.rawQuery("SELECT * FROM HistoricalCountryDays", null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                days = c.getString(c.getColumnIndex("days"));
                c.moveToNext();
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * Update GlobalStats Table
     *
     * @param globalStats an instance of GlobalStats
     */
    private void updateGlobalStatsTable(GlobalStats globalStats) {
        String query = "UPDATE GlobalStats  SET updated = '" + globalStats.getUpdated() + "', " +
                "cases = '" + removeFormat(globalStats.getCases()) + "', " +
                "todayCases = '" + removeFormat(globalStats.getTodayCases()) + "', " +
                "deaths = '" + removeFormat(globalStats.getDeaths()) + "', " +
                "todayDeaths = '" + removeFormat(globalStats.getTodayDeaths()) + "', " +
                "recovered = '" + removeFormat(globalStats.getRecovered()) + "', " +
                "active = '" + removeFormat(globalStats.getActive()) + "', " +
                "critical = '" + removeFormat(globalStats.getCritical()) + "', " +
                "casesPerOneMillion = '" + removeFormat(globalStats.getCasesPerOneMillion()) + "', " +
                "deathsPerOneMillion = '" + removeFormat(globalStats.getDeathsPerOneMillion()) + "', " +
                "tests = '" + removeFormat(globalStats.getTests()) + "', " +
                "testsPerOneMillion = '" + removeFormat(globalStats.getTestsPerOneMillion()) + "', " +
                "affectedCountries = '" + removeFormat(globalStats.getAffectedCountries()) + "'";
        try {
            myDatabase.execSQL(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Inset to GlobalStats Table
     *
     * @param globalStats an instance of GlobalStats
     */
    private void insertToGlobalStatsTable(GlobalStats globalStats) {
        String query = "Insert into GlobalStats (updated,cases,todayCases,deaths,todayDeaths,recovered,active" +
                ",critical,casesPerOneMillion,deathsPerOneMillion,tests,testsPerOneMillion,affectedCountries) Values" +
                "('" + globalStats.getUpdated() + "'," + "'" + removeFormat(globalStats.getCases()) + "'," +
                "'" + removeFormat(globalStats.getTodayCases()) + "'," + "'" + removeFormat(globalStats.getDeaths()) + "'," +
                "'" + removeFormat(globalStats.getTodayCases()) + "'," + "'" + removeFormat(globalStats.getRecovered()) + "'," +
                "'" + removeFormat(globalStats.getActive()) + "'," + "'" + removeFormat(globalStats.getCritical()) + "'," +
                "'" + removeFormat(globalStats.getCasesPerOneMillion()) + "'," + "'" + removeFormat(globalStats.getDeathsPerOneMillion()) + "'," +
                "'" + removeFormat(globalStats.getTests()) + "'," + "'" + removeFormat(globalStats.getTestsPerOneMillion()) + "'," +
                "'" + removeFormat(globalStats.getAffectedCountries()) + "')";
        try {
            myDatabase.execSQL(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inset to CountryData Table
     *
     * @param countryData an instance of CountryData
     */
    private void insertToCountryDataTable(CountryData countryData) {
        String query = "Insert into CountryData (country,updated,cases,todayCases,deaths,todayDeaths,recovered," +
                "active,critical,casesPerOneMillion,deathsPerOneMillion,tests,testsPerOneMillion,flag,iso2,iso3) Values" +
                "('" + countryData.getCountry().replace("'", "''") + "'," + "'" + countryData.getUpdated() + "'," +
                "'" + removeFormat(countryData.getCases()) + "'," + "'" + removeFormat(countryData.getTodayCases()) + "'," +
                "'" + removeFormat(countryData.getDeaths()) + "'," + "'" + removeFormat(countryData.getTodayDeaths()) + "'," +
                "'" + removeFormat(countryData.getRecovered()) + "'," + "'" + removeFormat(countryData.getActive()) + "'," +
                "'" + removeFormat(countryData.getCritical()) + "'," + "'" + removeFormat(countryData.getCasesPerOneMillion()) + "'," +
                "'" + removeFormat(countryData.getDeathsPerOneMillion()) + "'," + "'" + removeFormat(countryData.getTests()) + "'," +
                "'" + removeFormat(countryData.getTestsPerOneMillion()) + "'," + "'" + countryData.getCountryInfo().getFlag() + "'," +
                "'" + countryData.getCountryInfo().getIso2() + "'," + "'" + countryData.getCountryInfo().getIso3() + "')";
        try {
            myDatabase.execSQL(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inset to USStatesData Table
     *
     * @param usStatesData an instance of USStatesData
     */
    private void insertToUSStatesDataTable(USStatesData usStatesData) {
        String query = "Insert into USStatesData (state,cases,todayCases,todayDeaths,deaths,active,tests," +
                "testsPerOneMillion,recovered) Values" +
                "('" + usStatesData.getState().replace("'", "''") + "'," + "'" + removeFormat(usStatesData.getCases()) + "'," +
                "'" + removeFormat(usStatesData.getTodayCases()) + "'," + "'" + removeFormat(usStatesData.getTodayDeaths()) + "'," +
                "'" + removeFormat(usStatesData.getDeaths()) + "'," + "'" + removeFormat(usStatesData.getActive()) + "'," +
                "'" + removeFormat(usStatesData.getTests()) + "'," + "'" + removeFormat(usStatesData.getTestsPerOneMillion()) + "'," +
                "'" + removeFormat(usStatesData.getRecovered()) + "')";
        try {
            myDatabase.execSQL(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update CountryData Table
     *
     * @param countryData an instance of CountryData
     */
    private void updateCountryDataTable(CountryData countryData) {
        String query = "UPDATE CountryData  SET country = '" + countryData.getCountry().replace("'", "''") + "', " +
                "updated = '" + countryData.getUpdated() + "', " +
                "cases = '" + removeFormat(countryData.getCases()) + "', " +
                "todayCases = '" + removeFormat(countryData.getTodayCases()) + "', " +
                "deaths = '" + removeFormat(countryData.getDeaths()) + "', " +
                "todayDeaths = '" + removeFormat(countryData.getTodayDeaths()) + "', " +
                "recovered = '" + removeFormat(countryData.getRecovered()) + "', " +
                "active = '" + removeFormat(countryData.getActive()) + "', " +
                "critical = '" + removeFormat(countryData.getCritical()) + "', " +
                "casesPerOneMillion = '" + removeFormat(countryData.getCasesPerOneMillion()) + "', " +
                "deathsPerOneMillion = '" + removeFormat(countryData.getDeathsPerOneMillion()) + "', " +
                "tests = '" + removeFormat(countryData.getTests()) + "', " +
                "testsPerOneMillion = '" + removeFormat(countryData.getTestsPerOneMillion()) + "', " +
                "flag = '" + countryData.getCountryInfo().getFlag() + "', " +
                "iso2 = '" + countryData.getCountryInfo().getIso2() + "', " +
                "iso3= '" + countryData.getCountryInfo().getIso3() + "'" +
                "WHERE country = '" + countryData.getCountry().replace("'", "''") + "'";
        try {
            myDatabase.execSQL(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get Data from GlobalStats table
     *
     * @return an instance of GlobalStats
     */
    public GlobalStats getDataFromGlobalStatsTable() {
        GlobalStats globalStats = new GlobalStats();
        try {
            Cursor c = myDatabase.rawQuery("SELECT * FROM GlobalStats", null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                globalStats.setUpdated(c.getString(c.getColumnIndex("updated")));
                globalStats.setCases(c.getString(c.getColumnIndex("cases")));
                globalStats.setTodayCases(c.getString(c.getColumnIndex("todayCases")));
                globalStats.setDeaths(c.getString(c.getColumnIndex("deaths")));
                globalStats.setTodayDeaths(c.getString(c.getColumnIndex("todayDeaths")));
                globalStats.setRecovered(c.getString(c.getColumnIndex("recovered")));
                globalStats.setActive(c.getString(c.getColumnIndex("active")));
                globalStats.setCritical(c.getString(c.getColumnIndex("critical")));
                globalStats.setCasesPerOneMillion(c.getString(c.getColumnIndex("casesPerOneMillion")));
                globalStats.setDeathsPerOneMillion(c.getString(c.getColumnIndex("deathsPerOneMillion")));
                globalStats.setTests(c.getString(c.getColumnIndex("tests")));
                globalStats.setTestsPerOneMillion(c.getString(c.getColumnIndex("testsPerOneMillion")));
                globalStats.setAffectedCountries(c.getString(c.getColumnIndex("affectedCountries")));
                c.moveToNext();
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return globalStats;
    }

    /**
     * Get Data from CountryData table
     *
     * @return an instance of CountryData
     */
    public List<CountryData> getDataFromCountryDataTable() {
        List<CountryData> countries = new ArrayList<>();
        try {
            Cursor c = myDatabase.rawQuery("SELECT * FROM CountryData", null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                CountryData country = new CountryData();
                country.setCountry(c.getString(c.getColumnIndex("country")).replace("''", "'"));
                country.setUpdated(c.getString(c.getColumnIndex("updated")));
                country.setCases(c.getString(c.getColumnIndex("cases")));
                country.setTodayCases(c.getString(c.getColumnIndex("todayCases")));
                country.setDeaths(c.getString(c.getColumnIndex("deaths")));
                country.setTodayDeaths(c.getString(c.getColumnIndex("todayDeaths")));
                country.setRecovered(c.getString(c.getColumnIndex("recovered")));
                country.setActive(c.getString(c.getColumnIndex("active")));
                country.setCritical(c.getString(c.getColumnIndex("critical")));
                country.setCasesPerOneMillion(c.getString(c.getColumnIndex("casesPerOneMillion")));
                country.setDeathsPerOneMillion(c.getString(c.getColumnIndex("deathsPerOneMillion")));
                country.setTests(c.getString(c.getColumnIndex("tests")));
                country.setTestsPerOneMillion(c.getString(c.getColumnIndex("testsPerOneMillion")));
                CountryData.countryInfo countryInfo = new CountryData.countryInfo();
                countryInfo.setFlag(c.getString(c.getColumnIndex("flag")));
                countryInfo.setIso2(c.getString(c.getColumnIndex("iso2")));
                countryInfo.setIso3(c.getString(c.getColumnIndex("iso3")));
                country.setCountryInfo(countryInfo);
                countries.add(country);
                c.moveToNext();
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countries;
    }

    /**
     * Get Data from USStatesData table
     *
     * @return an instance of USStatesData
     */
    public List<USStatesData> getDataFromUSStatesDataTable() {
        List<USStatesData> states = new ArrayList<>();
        try {
            Cursor c = myDatabase.rawQuery("SELECT * FROM USStatesData", null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                USStatesData state = new USStatesData();
                state.setState(c.getString(c.getColumnIndex("state")).replace("''", "'"));
                state.setCases(c.getString(c.getColumnIndex("cases")));
                state.setTodayCases(c.getString(c.getColumnIndex("todayCases")));
                state.setTodayDeaths(c.getString(c.getColumnIndex("todayDeaths")));
                state.setDeaths(c.getString(c.getColumnIndex("deaths")));
                state.setActive(c.getString(c.getColumnIndex("active")));
                state.setTests(c.getString(c.getColumnIndex("tests")));
                state.setTestsPerOneMillion(c.getString(c.getColumnIndex("testsPerOneMillion")));
                state.setRecovered(c.getString(c.getColumnIndex("recovered")));
                states.add(state);
                c.moveToNext();
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return states;
    }

    /**
     * Remove extra ',' from string
     *
     * @param number a string
     * @return a string
     */
    private String removeFormat(String number) {
        return number.replace(",", "");
    }
}
