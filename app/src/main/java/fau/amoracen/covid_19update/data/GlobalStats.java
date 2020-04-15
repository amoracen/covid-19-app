package fau.amoracen.covid_19update.data;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Locale;

/**
 * Global stats: cases, deaths, recovered, time last updated, and active cases.
 */
public class GlobalStats implements Serializable {
    /**
     * Request URL
     */
    public final static String URL = "https://corona.lmao.ninja/v2/all";
    /*Data returned by the API*/
    private String updated, cases, todayCases, deaths, todayDeaths;
    private String recovered, active, critical, casesPerOneMillion, deathsPerOneMillion;
    private String tests, testsPerOneMillion, affectedCountries;


    /**
     * Get Request URL
     *
     * @return a string
     */
    public String getURL() {
        return URL;
    }

    /**
     * Get Date and Time the Data was updated
     *
     * @return a string
     */
    public String getUpdated() {
        return updated;
    }

    /**
     * Set Date and Time the Data was updated
     *
     * @param updated a string
     */
    public void setUpdated(String updated) {
        this.updated = updated;
    }

    /**
     * Get total confirmed cases
     *
     * @return a string
     */
    public String getCases() {
        return formatNumber(cases);
    }

    /**
     * Set total confirmed cases
     *
     * @param cases a string
     */
    public void setCases(String cases) {
        this.cases = cases;
    }

    /**
     * Get new cases
     *
     * @return a string
     */
    public String getTodayCases() {
        return formatNumber(todayCases);
    }

    /**
     * Set new cases
     *
     * @param todayCases a string
     */
    public void setTodayCases(String todayCases) {
        this.todayCases = todayCases;
    }

    /**
     * Get total number of recovered
     *
     * @return a string
     */
    public String getRecovered() {
        return formatNumber(recovered);
    }

    /**
     * Set total number of recovered
     *
     * @param recovered a string
     */
    public void setRecovered(String recovered) {
        this.recovered = recovered;
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
     * Set active cases
     *
     * @param active a string
     */
    public void setActive(String active) {
        this.active = active;
    }

    /**
     * Get critical cases
     *
     * @return a string
     */
    public String getCritical() {
        return formatNumber(critical);
    }

    /**
     * Set critical cases
     *
     * @param critical a string
     */
    public void setCritical(String critical) {
        this.critical = critical;
    }

    /**
     * Get number of Cases Per One Million
     *
     * @return a string
     */
    public String getCasesPerOneMillion() {
        return formatNumberTwoDecimalPlaces(casesPerOneMillion);
    }

    /**
     * Set number of Cases Per One Million
     *
     * @param casesPerOneMillion a string
     */
    public void setCasesPerOneMillion(String casesPerOneMillion) {
        this.casesPerOneMillion = casesPerOneMillion;
    }

    /**
     * Get number of Deaths Per One Million
     *
     * @return a string
     */
    public String getDeathsPerOneMillion() {
        return formatNumberTwoDecimalPlaces(deathsPerOneMillion);
    }

    /**
     * Set number of Deaths Per One Million
     *
     * @param deathsPerOneMillion a string
     */
    public void setDeathsPerOneMillion(String deathsPerOneMillion) {
        this.deathsPerOneMillion = deathsPerOneMillion;
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
     *
     * @param tests a string
     */
    public void setTests(String tests) {
        this.tests = tests;
    }

    /**
     * Get number of Test Per One Million
     *
     * @return a string
     */
    public String getTestsPerOneMillion() {
        return formatNumberTwoDecimalPlaces(testsPerOneMillion);
    }

    /**
     * Set number of Test Per One Million
     *
     * @param testsPerOneMillion a string
     */
    public void setTestsPerOneMillion(String testsPerOneMillion) {
        this.testsPerOneMillion = testsPerOneMillion;
    }

    /**
     * Get number of affected Countries
     *
     * @return a string
     */
    public String getAffectedCountries() {
        return formatNumber(affectedCountries);
    }

    /**
     * Set number of affected Countries
     *
     * @param affectedCountries a string
     */
    public void setAffectedCountries(String affectedCountries) {
        this.affectedCountries = affectedCountries;
    }

    /**
     * Get number of deaths
     *
     * @return a string
     */
    public String getDeaths() {
        return formatNumber(deaths);
    }

    /**
     * Set number of deaths
     *
     * @param deaths a string
     */
    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    /**
     * Get  number of new deaths
     *
     * @return a string
     */
    public String getTodayDeaths() {
        return formatNumber(todayDeaths);
    }

    /**
     * Set  number of new deaths
     *
     * @param todayDeaths a string
     */
    public void setTodayDeaths(String todayDeaths) {
        this.todayDeaths = todayDeaths;
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

    @NonNull
    @Override
    public String toString() {
        String globalSTR = "";
        globalSTR += "Updated: " + updated;
        globalSTR += "\nTotal Confirmed: " + cases;
        globalSTR += "\nToday Cases: " + todayCases;
        globalSTR += "\nTotal Deaths: " + deaths;
        globalSTR += "\nNew Deaths: " + todayDeaths;
        globalSTR += "\nActive: " + active;
        globalSTR += "\nCritical: " + critical;
        globalSTR += "\nCases Per One Million: " + casesPerOneMillion;
        globalSTR += "\nDeaths Per One Million: " + deathsPerOneMillion;
        globalSTR += "\nTests: " + tests;
        globalSTR += "\nTests Per One Million: " + testsPerOneMillion;
        globalSTR += "\nAffected Countries: " + affectedCountries;
        return globalSTR;
    }
}
