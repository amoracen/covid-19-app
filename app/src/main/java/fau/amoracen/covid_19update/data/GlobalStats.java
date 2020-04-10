package fau.amoracen.covid_19update.data;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.net.URL;

public class GlobalStats implements Serializable {
    private final static String URL = "https://corona.lmao.ninja/all";
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
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public String getTodayCases() {
        return todayCases;
    }

    public void setTodayCases(String todayCases) {
        this.todayCases = todayCases;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCritical() {
        return critical;
    }

    public void setCritical(String critical) {
        this.critical = critical;
    }

    public String getCasesPerOneMillion() {
        return casesPerOneMillion;
    }

    public void setCasesPerOneMillion(String casesPerOneMillion) {
        this.casesPerOneMillion = casesPerOneMillion;
    }

    public String getDeathsPerOneMillion() {
        return deathsPerOneMillion;
    }

    public void setDeathsPerOneMillion(String deathsPerOneMillion) {
        this.deathsPerOneMillion = deathsPerOneMillion;
    }

    public String getTests() {
        return tests;
    }

    public void setTests(String tests) {
        this.tests = tests;
    }

    public String getTestsPerOneMillion() {
        return testsPerOneMillion;
    }

    public void setTestsPerOneMillion(String testsPerOneMillion) {
        this.testsPerOneMillion = testsPerOneMillion;
    }

    public String getAffectedCountries() {
        return affectedCountries;
    }

    public void setAffectedCountries(String affectedCountries) {
        this.affectedCountries = affectedCountries;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getTodayDeaths() {
        return todayDeaths;
    }

    public void setTodayDeaths(String todayDeaths) {
        this.todayDeaths = todayDeaths;
    }
}
