package com.vikasreddy.restfiddler;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class BodyViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Param>> bodyParams;

    public BodyViewModel() {
        bodyParams = new MutableLiveData<>();
        bodyParams.setValue(new ArrayList<Param>());
        Objects.requireNonNull(bodyParams.getValue()).add(new Param("", ""));
    }

    public void add(String key, String value) {
        Objects.requireNonNull(bodyParams.getValue()).add(new Param(key, value));
    }

    void addAt(String key, String value, int pos) {
        Objects.requireNonNull(bodyParams.getValue()).remove(pos);
        bodyParams.getValue().add(pos, new Param(key, value));
    }

    public Param get(int pos) {
        return Objects.requireNonNull(bodyParams.getValue()).get(pos);
    }

    void removeAt(int pos) {
        if (pos > -1 && pos < Objects.requireNonNull(bodyParams.getValue()).size()) {
            bodyParams.getValue().remove(pos);
        }
    }

    ArrayList<Param> toList() {
        ArrayList<Param> params = this.bodyParams.getValue();
        assert params != null;
        params.remove(new Param("", ""));
        return params;
    }

    public int size() {
        return Objects.requireNonNull(this.bodyParams.getValue()).size();
    }
}
