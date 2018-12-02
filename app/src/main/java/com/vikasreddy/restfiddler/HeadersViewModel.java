package com.vikasreddy.restfiddler;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class HeadersViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Param>> headersParams;

    public HeadersViewModel() {
        headersParams = new MutableLiveData<>();
        headersParams.setValue(new ArrayList<Param>());
        Objects.requireNonNull(headersParams.getValue()).add(new Param("", ""));
    }

    public void add(String key, String value) {
        Objects.requireNonNull(headersParams.getValue()).add(new Param(key, value));
    }

    void addAt(String key, String value, int pos) {
        Objects.requireNonNull(headersParams.getValue()).remove(pos);
        headersParams.getValue().add(pos, new Param(key, value));
    }

    public Param get(int pos) {
        return Objects.requireNonNull(headersParams.getValue()).get(pos);
    }

    void removeAt(int pos) {
        if(pos > -1 && pos < Objects.requireNonNull(headersParams.getValue()).size()) {
            headersParams.getValue().remove(pos);
        }
    }

    public int size() {
        return Objects.requireNonNull(this.headersParams.getValue()).size();
    }

    ArrayList<Param> toList() {
        ArrayList<Param> params = this.headersParams.getValue();
        assert params != null;
        params.remove(new Param("", ""));
        return params;
    }
}
