package org.kll.app.mamapper.Model;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class OverpassQueryResult {
    @SerializedName("elements")
    public List<Element> elements = new ArrayList<>();

    public static class Element {

        @SerializedName("type")
        public String type;

        @SerializedName("id")
        public long id;

        @SerializedName("lat")
        public double lat;

        @SerializedName("lon")
        public double lon;

        @SerializedName("nodes")
        public ArrayList<Long> nodes;

        @SerializedName("tags")
        public Tags tags = new Tags();

        public static class Tags {
            @SerializedName("type")
            public String type;

            @SerializedName("amenity")
            public String amenity;

            @SerializedName("name")
            public String name;

            @SerializedName("name:ne")
            public String nepaliName;

            @SerializedName("operator:type")
            public String operatorType;

            @SerializedName("facility:icu")
            public String facilityIcu;

            @SerializedName("facility:nicu")
            public String facilityNicu;

            @SerializedName("facility:operating_theatre")
            public String facilityOT;

            @SerializedName("facility:ventilator")
            public String facilityventilator;

            @SerializedName("facility:x-ray")
            public String facilityXray;

            @SerializedName("emergency")
            public String emergency;

            @SerializedName("emergency_service")
            public String emergencyService;

            @SerializedName("capacity:beds")
            public String capacityBeds;

            @SerializedName("personnel:count")
            public String personnelCount;

            @SerializedName("contact:phone")
            public String phone;

            @SerializedName("email")
            public String contactEmail;

            @SerializedName("website")
            public String website;

            @SerializedName("operator")
            public String operator;

            @SerializedName("student:count")
            public String studentCount;

            @SerializedName("opening_hours")
            public String openingHours;

            @SerializedName("atm")
            public String atm;

        }
    }
}

