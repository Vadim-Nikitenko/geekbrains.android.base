
package ru.kiradev.weather.rest.entities.city;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Suggestion {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("unrestricted_value")
    @Expose
    private String unrestrictedValue;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnrestrictedValue() {
        return unrestrictedValue;
    }

    public void setUnrestrictedValue(String unrestrictedValue) {
        this.unrestrictedValue = unrestrictedValue;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
