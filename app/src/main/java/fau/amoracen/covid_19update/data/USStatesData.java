package fau.amoracen.covid_19update.data;

import java.io.Serializable;
import java.util.Locale;

/**
 * Stats on United States of America States with COVID-19,
 * including cases, new cases, deaths, new deaths, active cases, tests, and test per million
 */
public class USStatesData implements Serializable {
    /**
     * Request URL
     */
    public final static String URL = "https://corona.lmao.ninja/v2/states?sort=cases";
    /**
     * Request One Country
     */
    public final static String URLState = "https://corona.lmao.ninja/v2/states/";
    /*Data returned by the API*/
    private String state, cases, todayCases, todayDeaths, deaths;
    private String active, tests, testsPerOneMillion;

    /**
     * Get State Name
     *
     * @return a string
     */
    public String getState() {
        return state;
    }

    /**
     * Set State Name
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Get Total number of Cases
     *
     * @return a string
     */
    public String getCases() {
        return formatNumber(cases);
    }

    /**
     * Get Total number of Cases
     *
     * @return a string
     */
    public String getCasesNoFormat() {
        return cases;
    }

    /**
     * Set Total number of Cases
     */
    public void setCases(String cases) {
        this.cases = cases;
    }

    /**
     * Get new Cases
     *
     * @return a string
     */
    public String getTodayCases() {
        return formatNumber(todayCases);
    }

    /**
     * Set new Cases
     */
    public void setTodayCases(String todayCases) {
        this.todayCases = todayCases;
    }

    /**
     * Get Total number of deaths
     *
     * @return a string
     */
    public String getDeaths() {
        return formatNumber(deaths);
    }

    /**
     * Get Total number of deaths
     *
     * @return a string
     */
    public String getDeathsNoFormat() {
        return deaths;
    }

    /**
     * Set Total number of deaths
     */
    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    /**
     * Get new deaths
     *
     * @return a string
     */
    public String getTodayDeaths() {
        return formatNumber(todayDeaths);
    }

    /**
     * Set new deaths
     */
    public void setTodayDeaths(String todayDeaths) {
        this.todayDeaths = todayDeaths;
    }

    /**
     * Get active cases
     *
     * @return a string
     */
    public String getActive() {
        return formatNumber(active);
    }

    /**
     * Get active cases
     *
     * @return a string
     */
    public String getActiveNoFormat() {
        return active;
    }

    /**
     * Get active cases
     *
     * @return a string
     */
    public String getRecoveredNoFormat() {
        return String.valueOf(Integer.parseInt(getCasesNoFormat()) - Integer.parseInt(getActiveNoFormat()) - Integer.parseInt(getDeathsNoFormat()));
    }

    /**
     * Set active cases
     */
    public void setActive(String active) {
        this.active = active;
    }

    /**
     * Get total number of tests
     *
     * @return a string
     */
    public String getTests() {
        return formatNumber(tests);
    }

    /**
     * Set total number of tests
     */
    public void setTests(String tests) {
        this.tests = tests;
    }

    /**
     * Get total number of tests per one million
     *
     * @return a string
     */
    public String getTestsPerOneMillion() {
        return formatNumberTwoDecimalPlaces(testsPerOneMillion);
    }

    /**
     * Set total number of tests per one million
     */
    public void setTestsPerOneMillion(String testsPerOneMillion) {
        this.testsPerOneMillion = testsPerOneMillion;
    }

    /**
     * Format Value to Two decimal places
     *
     * @param number a string
     * @return string of the formatted value
     */
    private String formatNumberTwoDecimalPlaces(String number) {
        if (number == null) return "0";
        return String.format(Locale.getDefault(), "%,.2f", Double.parseDouble(number));
    }

    /**
     * Format Value
     *
     * @param number a string
     * @return string of the formatted value
     */
    private String formatNumber(String number) {
        if (number == null) return "0";
        return String.format(Locale.getDefault(), "%,.0f", Double.parseDouble(number));
    }
}
