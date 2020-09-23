
package ru.kiradev.weather.rest.entities.city;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityRequest {

    @SerializedName("suggestions")
    @Expose
    private List<Suggestion> suggestions = null;

    public List<Suggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }

}
