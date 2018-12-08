package com.vikasreddy.restfiddler;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ResponseCookiesViewModel extends ViewModel {
    private MutableLiveData<String> cookies;

    public ResponseCookiesViewModel() {
        cookies = new MutableLiveData<>();
    }

    public ResponseCookiesViewModel(String cookies) {
        new ResponseCookiesViewModel().cookies.setValue(cookies);
    }

    public String getCookies() {
        return this.cookies.getValue();
    }

    public void setCookies(String cookies) {
        this.cookies.setValue(cookies);
    }
}
