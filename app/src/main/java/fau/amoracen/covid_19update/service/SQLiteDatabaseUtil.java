package fau.amoracen.covid_19update.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.database.SQLException;

import fau.amoracen.covid_19update.data.GlobalStats;

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
    public boolean isTableEmpty(String table) {
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
            Log.i("Tag", "Empty Table -> Go to Insert");
            insertToGlobalStatsTable(globalStats);
            return;
        }
        Log.i("Tag", "Table not Empty -> Go to Update");
        updateGlobalStatsTable(globalStats);
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
    public void insertToGlobalStatsTable(GlobalStats globalStats) {
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
     * Remove extra ',' from string
     *
     * @param number a string
     * @return a string
     */
    public String removeFormat(String number) {
        return number.replace(",", "");
    }
}
