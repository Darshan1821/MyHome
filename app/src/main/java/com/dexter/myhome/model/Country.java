package com.dexter.myhome.model;

import java.io.Serializable;
import java.util.List;

public class Country implements Serializable {
    private String name;
    private List<String> callingCodes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCallingCodes() {
        return callingCodes;
    }

    public void setCallingCodes(List<String> callingCodes) {
        this.callingCodes = callingCodes;
    }
}
