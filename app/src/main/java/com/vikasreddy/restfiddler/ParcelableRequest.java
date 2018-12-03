package com.vikasreddy.restfiddler;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class ParcelableRequest implements Parcelable {
    private String url;
    private String method;
    private HashMap<String, String> headers;
    private HashMap<String, String> bodyParameters;
    private HashMap<String, String> queryParameters;

    ParcelableRequest(String url, String method, HeadersViewModel headersViewModel
            , QueryParamsViewModel queryParamsViewModel, BodyViewModel bodyViewModel) {
        this.url = url;
        this.method = method;
        this.headers = populateHeaders(headersViewModel);
        this.bodyParameters = populateBody(bodyViewModel);
        this.queryParameters = populateQueryParams(queryParamsViewModel);
    }

    private HashMap<String,String> populateQueryParams(QueryParamsViewModel queryParamsViewModel) {
        HashMap<String, String> map = new HashMap<>();
        if(queryParamsViewModel == null) {
            return map;
        }
        for(Param param : queryParamsViewModel.toList()) {
            if(map.containsKey(param.key)) {
                //throw error
            } else {
                map.put(param.key, param.value);
            }
        }
        return map;
    }

    private HashMap<String,String> populateBody(BodyViewModel bodyViewModel) {
        HashMap<String, String> map = new HashMap<>();
        if(bodyViewModel == null) {
            return map;
        }
        for(Param param : bodyViewModel.toList()) {
            if(map.containsKey(param.key)) {
                //throw error
            } else {
                map.put(param.key, param.value);
            }
        }
        return map;
    }

    private HashMap<String,String> populateHeaders(HeadersViewModel headersViewModel) {
        HashMap<String, String> map = new HashMap<>();
        if(headersViewModel == null) {
            return map;
        }
        for(Param param : headersViewModel.toList()) {
            if(map.containsKey(param.key)) {
                //throw error
            } else {
                map.put(param.key, param.value);
            }
        }
        return map;
    }

    //region Getters and Setters
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public HashMap<String, String> getBodyParameters() {
        return bodyParameters;
    }

    public void setBodyParameters(HashMap<String, String> bodyParameters) {
        this.bodyParameters = bodyParameters;
    }

    public HashMap<String, String> getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(HashMap<String, String> queryParameters) {
        this.queryParameters = queryParameters;
    }

    //endregion

    @SuppressWarnings("unchecked")
    private ParcelableRequest(Parcel in) {
        this.url = in.readString();
        this.method = in.readString();
        this.headers = (HashMap<String, String>) in.readSerializable();
        this.queryParameters = (HashMap<String, String>) in.readSerializable();
        this.bodyParameters = (HashMap<String, String>) in.readSerializable();
    }

    public static final Creator<ParcelableRequest> CREATOR = new Creator<ParcelableRequest>() {
        @Override
        public ParcelableRequest createFromParcel(Parcel in) {
            return new ParcelableRequest(in);
        }

        @Override
        public ParcelableRequest[] newArray(int size) {
            return new ParcelableRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(method);
        dest.writeSerializable(headers);
        dest.writeSerializable(queryParameters);
        dest.writeSerializable(bodyParameters);
    }
}
