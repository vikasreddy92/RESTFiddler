package com.vikasreddy.restfiddler;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ResponseHeaderViewModel extends ViewModel {
    private MutableLiveData<String> headers;

    public ResponseHeaderViewModel() {
        headers = new MutableLiveData<>();
    }

    public ResponseHeaderViewModel(String headers) {
        new ResponseHeaderViewModel().headers.setValue(headers);
    }

    public String getHeaders() {
        return this.headers.getValue();
    }

    public void setHeaders(String headers) {
        this.headers.setValue(headers);
    }}
