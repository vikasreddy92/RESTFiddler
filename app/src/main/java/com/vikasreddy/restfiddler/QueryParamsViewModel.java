package com.vikasreddy.restfiddler;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class QueryParamsViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<Param>> queryParams;

    public QueryParamsViewModel() {
        queryParams = new MutableLiveData<>();
        queryParams.setValue(new ArrayList<Param>());
        Objects.requireNonNull(queryParams.getValue()).add(new Param("", ""));
    }

    public void add(String key, String value) {
        Objects.requireNonNull(queryParams.getValue()).add(new Param(key, value));
    }

    void addAt(String key, String value, int pos) {
        Objects.requireNonNull(queryParams.getValue()).remove(pos);
        queryParams.getValue().add(pos, new Param(key, value));
    }

    public Param get(int pos) {
        return Objects.requireNonNull(queryParams.getValue()).get(pos);
    }

    void removeAt(int pos) {
        if(pos > -1 && pos < Objects.requireNonNull(queryParams.getValue()).size()) {
            queryParams.getValue().remove(pos);
        }
    }

    ArrayList<Param> toList() {
        ArrayList<Param> params = this.queryParams.getValue();
        assert params != null;
        params.remove(new Param("", ""));
        return params;
    }

    public int size() {
        return Objects.requireNonNull(this.queryParams.getValue()).size();
    }
}
