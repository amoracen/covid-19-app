package fau.amoracen.covid_19update.data;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.net.URL;
import java.util.Locale;

public class GlobalStats implements Serializable {
    public final static String URL = "https://corona.lmao.ninja/v2/all";
    private String updated;
    private String cases;
    private String todayCases;
    private String deaths;
    private String todayDeaths;
    private String recovered;
    private String active;
    private String critical;
    private String casesPerOneMillion;
    private String deathsPerOneMillion;
    private String tests;
    private String testsPerOneMillion;
    private String affectedCountries;

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

    public String getURL() {
        return URL;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCases() {
        return formatNumber(cases);
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public String getTodayCases() {
        return formatNumber(todayCases);
    }

    public void setTodayCases(String todayCases) {
        this.todayCases = todayCases;
    }

    public String getRecovered() {
        return formatNumber(recovered);
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    public String getActive() {
        return formatNumber(active);
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCritical() {
        return formatNumber(critical);
    }

    public void setCritical(String critical) {
        this.critical = critical;
    }

    public String getCasesPerOneMillion() {
        return formatNumberTwoDecimalPlaces(casesPerOneMillion);
    }

    public void setCasesPerOneMillion(String casesPerOneMillion) {
        this.casesPerOneMillion = casesPerOneMillion;
    }

    public String getDeathsPerOneMillion() {
        return formatNumberTwoDecimalPlaces(deathsPerOneMillion);
    }

    public void setDeathsPerOneMillion(String deathsPerOneMillion) {
        this.deathsPerOneMillion = deathsPerOneMillion;
    }

    public String getTests() {
        return formatNumber(tests);
    }

    public void setTests(String tests) {
        this.tests = tests;
    }

    public String getTestsPerOneMillion() {
        return formatNumberTwoDecimalPlaces(testsPerOneMillion);
    }

    public void setTestsPerOneMillion(String testsPerOneMillion) {
        this.testsPerOneMillion = testsPerOneMillion;
    }

    public String getAffectedCountries() {
        return formatNumber(affectedCountries);
    }

    public void setAffectedCountries(String affectedCountries) {
        this.affectedCountries = affectedCountries;
    }

    public String getDeaths() {
        return formatNumber(deaths);
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getTodayDeaths() {
        return formatNumber(todayDeaths);
    }

    public void setTodayDeaths(String todayDeaths) {
        this.todayDeaths = todayDeaths;
    }

    public String formatNumberTwoDecimalPlaces(String number) {
        if (number == null) {
            return "0";
        }
        double num = Double.parseDouble(number);
        return String.format(Locale.getDefault(), "%,.2f", num);
    }

    public String formatNumber(String number) {
        if (number == null) {
            return "0";
        }
        double num = Double.parseDouble(number);
        return String.format(Locale.getDefault(), "%,.0f", num);
    }
}
