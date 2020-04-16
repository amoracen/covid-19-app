package fau.amoracen.covid_19update.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Locale;

public class CountryData implements Serializable {
    /**
     * Request URL
     */
    public final static String URL = "https://corona.lmao.ninja/v2/countries?yesterday=false&sort=cases";
    /*Data returned by the API*/
    private String country, updated, cases, todayCases, deaths;
    private String todayDeaths, recovered, active, critical;
    private String casesPerOneMillion, deathsPerOneMillion, tests, testsPerOneMillion;
    private countryInfo countryInfo;

    /**
     * Get Country Name
     *
     * @return a string
     */
    public String getCountry() {
        return country;
    }

    /**
     * Set Country Name
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Get Country Info
     *
     * @return a string
     */
    public countryInfo getCountryInfo() {
        return countryInfo;
    }

    /**
     * Set Country Info
     */
    public void setCountryInfo(countryInfo countryInfo) {
        this.countryInfo = countryInfo;
    }

    /**
     * Get Date and Time data was updated
     *
     * @return a string
     */
    public String getUpdated() {
        return updated;
    }

    /**
     * Set Date and Time data was updated
     */
    public void setUpdated(String updated) {
        this.updated = updated;
    }

    /**
     * Get Total Cases
     *
     * @return a string
     */
    public String getCases() {
        return formatNumber(cases);
    }

    /**
     * Set Total Cases
     */
    public void setCases(String cases) {
        this.cases = cases;
    }

    /**
     * Get New Cases
     *
     * @return a string
     */
    public String getTodayCases() {
        return formatNumber(todayCases);
    }

    /**
     * Set New Cases
     */
    public void setTodayCases(String todayCases) {
        this.todayCases = todayCases;
    }

    /**
     * Get Total deaths
     *
     * @return a string
     */
    public String getDeaths() {
        return formatNumber(deaths);
    }

    /**
     * Set Total deaths
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
     * Get Recovered cases
     *
     * @return a string
     */
    public String getRecovered() {
        return formatNumber(recovered);
    }

    /**
     * Set Recovered cases
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
     */
    public void setCritical(String critical) {
        this.critical = critical;
    }

    /**
     * Get Cases per one million
     *
     * @return a string
     */
    public String getCasesPerOneMillion() {
        return formatNumberTwoDecimalPlaces(casesPerOneMillion);
    }

    /**
     * Set Cases per one million
     */
    public void setCasesPerOneMillion(String casesPerOneMillion) {
        this.casesPerOneMillion = casesPerOneMillion;
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

    /**
     * Get Deaths per one million
     *
     * @return a string
     */
    public String getDeathsPerOneMillion() {
        return formatNumberTwoDecimalPlaces(deathsPerOneMillion);
    }

    /**
     * Set Deaths per one million
     */
    public void setDeathsPerOneMillion(String deathsPerOneMillion) {
        this.deathsPerOneMillion = deathsPerOneMillion;
    }

    /**
     * Get Total test
     *
     * @return a string
     */
    public String getTests() {
        return formatNumber(tests);
    }

    /**
     * Set total test
     */
    public void setTests(String tests) {
        this.tests = tests;
    }

    /**
     * Get test per one million
     *
     * @return a string
     */
    public String getTestsPerOneMillion() {
        return formatNumberTwoDecimalPlaces(testsPerOneMillion);
    }

    /**
     * Set test per one million
     */
    public void setTestsPerOneMillion(String testsPerOneMillion) {
        this.testsPerOneMillion = testsPerOneMillion;
    }

    /**
     * Class to represent Country Info
     */
    public static class countryInfo implements Serializable {
        /*Data returned by the API*/
        private String lat, flag, iso2, iso3;
        @SerializedName("long")
        private String lon;

        /**
         * Get Latitude
         *
         * @return a string
         */
        public String getLat() {
            return lat;
        }

        /**
         * Set Latitude
         */
        public void setLat(String lat) {
            this.lat = lat;
        }

        /**
         * Get Longitude
         *
         * @return a string
         */
        public String getLon() {
            return lon;
        }

        /**
         * Set Longitude
         */
        public void setLon(String lon) {
            this.lon = lon;
        }

        /**
         * Get Flag Url
         *
         * @return a string
         */
        public String getFlag() {
            return flag;
        }

        /**
         * Set Flag Url
         */
        public void setFlag(String flag) {
            this.flag = flag;
        }

        /**
         * Get Country Name
         *
         * @return a string
         */
        public String getIso3() {
            return iso3;
        }

        /**
         * Set Country Name
         */
        public void setIso3(String iso3) {
            this.iso3 = iso3;
        }

        /**
         * Get Country Name
         *
         * @return a string
         */
        public String getIso2() {
            return iso2;
        }

        /**
         * Set Country Name
         */
        public void setIso2(String iso2) {
            this.iso2 = iso2;
        }
    }//EOF countryInfo
}
