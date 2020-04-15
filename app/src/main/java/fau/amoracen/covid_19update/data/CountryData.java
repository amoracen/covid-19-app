package fau.amoracen.covid_19update.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Locale;

public class CountryData implements Serializable {


    String country;
    countryInfo countryInfo;
    String updated;
    String cases;
    String todayCases;
    String deaths;
    String todayDeaths;
    String recovered;
    String active;
    String critical;
    String casesPerOneMillion;
    String deathsPerOneMillion;
    String tests;
    String testsPerOneMillion;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public countryInfo getCountryInfo() {
        return countryInfo;
    }

    public void setCountryInfo(countryInfo countryInfo) {
        this.countryInfo = countryInfo;
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

    public class countryInfo implements Serializable {
        String lat;
        @SerializedName("long")
        String lon;
        String flag;

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }
    }
}
