package com.vikasreddy.restfiddler;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ResponseBodyViewModel extends ViewModel {
    private MutableLiveData<String> body;

    public ResponseBodyViewModel() {
        body = new MutableLiveData<>();
    }

    public ResponseBodyViewModel(String body) {
        new ResponseBodyViewModel().body.setValue(body);
    }

    public String getBody() {
        return this.body.getValue();
    }

    public void setBody(String body) {
        this.body.setValue(body);
    }
}
