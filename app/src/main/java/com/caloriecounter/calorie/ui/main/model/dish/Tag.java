package com.caloriecounter.calorie.ui.main.model.dish;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Tag implements Serializable {

    @Expose
    @SerializedName("priority")
    private String priority;
    @Expose
    @SerializedName("scope")
    private String scope;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("id")
    private String id;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
