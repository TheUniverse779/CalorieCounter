package com.caloriecounter.calorie.ui.search.model;

import java.util.ArrayList;

public class SearchResponse {
    private ArrayList<SearchContent> items;

    public ArrayList<SearchContent> getItems() {
        return items;
    }

    public void setItems(ArrayList<SearchContent> items) {
        this.items = items;
    }
}
