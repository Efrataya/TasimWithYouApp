package com.example.tasimwithyouapp.models;

import android.content.Context;
import android.os.Build;

import com.example.tasimwithyouapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.InputStream;

public class CountryData {

    private CountryFlag flags;
    private CountryName name;

    public CountryData(CountryFlag flags) {
        this.flags = flags;
    }

    public CountryData() {

    }

    public static void asyncFromJson(Context context,
                                String airportCode,
                                OnSuccessListener<CountryData> onSuccessListener,
                                OnFailureListener onFailureListener) {
        new Thread(() -> {
            try (InputStream codesInputStream = context.getResources().openRawResource(R.raw.airportcodes)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    String jsonAirportCodes = new String(codesInputStream.readAllBytes());
                    CountryAirPortCode[] countryAirPortCodes = new Gson().fromJson(jsonAirportCodes, CountryAirPortCode[].class);
                    for (CountryAirPortCode countryAirPortCode : countryAirPortCodes) {
                        if(countryAirPortCode.getAirport_Code() == null) continue;
                        if (countryAirPortCode.getAirport_Code().equals(airportCode)) {
                            String country = countryAirPortCode.getCountry();
                            String region = countryAirPortCode.getRegion();
                            try (InputStream countryDataStream = context.getResources().openRawResource(R.raw.countrydata)) {
                                String jsonCountryData = new String(countryDataStream.readAllBytes());
                                CountryData[] countryData = new Gson().fromJson(jsonCountryData, CountryData[].class);
                                for (CountryData data : countryData) {
                                    if (data.getName().getCommon().equals(country)
                                            || data.getName().getCommon().equals(region)) {
                                        onSuccessListener.onSuccess(data);
                                        return;
                                    }
                                }
                            } catch (Exception e) {
                                onFailureListener.onFailure(e);
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
                onFailureListener.onFailure(new Exception("No country found for airport code: " + airportCode));
            } catch (Exception e) {
                onFailureListener.onFailure(e);
                e.printStackTrace();
            }
        }).start();
    }


    public static class CountryAirPortCode {
        private String Region;
        private String Country;
        private String City;
        private String Airport_Code;

        public CountryAirPortCode(String region, String country, String city, String airport_Code) {
            Region = region;
            Country = country;
            City = city;
            Airport_Code = airport_Code;
        }

        public CountryAirPortCode() {

        }

        public String getAirport_Code() {
            return Airport_Code;
        }

        public String getCity() {
            return City;
        }

        public String getCountry() {
            return Country;
        }

        public String getRegion() {
            return Region;
        }

        public void setAirport_Code(String airport_Code) {
            Airport_Code = airport_Code;
        }

        public void setCity(String city) {
            City = city;
        }

        public void setCountry(String country) {
            Country = country;
        }

        public void setRegion(String region) {
            Region = region;
        }
    }

    public static class CountryName {
        private String common;

        public CountryName(String common) {
            this.common = common;
        }

        public CountryName() {
        }

        public String getCommon() {
            return common;
        }

        public void setCommon(String common) {
            this.common = common;
        }
    }

    public static class CountryFlag {
        private String png;
        private String svg;
        private String alt;

        public CountryFlag(String png, String svg, String alt) {
            this.png = png;
            this.svg = svg;
            this.alt = alt;
        }

        public String getAlt() {
            return alt;
        }

        public String getPng() {
            return png;
        }

        public String getSvg() {
            return svg;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public void setPng(String png) {
            this.png = png;
        }

        public void setSvg(String svg) {
            this.svg = svg;
        }
    }

    public CountryFlag getFlags() {
        return flags;
    }

    public void setFlags(CountryFlag flags) {
        this.flags = flags;
    }

    public CountryName getName() {
        return name;
    }

    public void setName(CountryName name) {
        this.name = name;
    }
}
