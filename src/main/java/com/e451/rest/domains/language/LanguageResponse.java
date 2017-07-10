package com.e451.rest.domains.language;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by l659598 on 7/7/2017.
 */
public class LanguageResponse {

    private List<String> languages;

    public LanguageResponse() {
        languages = new ArrayList<>();
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }
}


