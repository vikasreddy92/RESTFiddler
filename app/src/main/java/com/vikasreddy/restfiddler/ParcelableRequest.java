package com.vikasreddy.restfiddler;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

public class ParcelableRequest implements Parcelable {
    private String url;
    private String method;
    private Map<String, List<String>> headers;
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

    ParcelableRequest(Request request) {
        this.url = request.url().toString();
        this.method = request.method();
        this.headers = request.headers().toMultimap();
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

    private Map<String, List<String>> populateHeaders(HeadersViewModel headersViewModel) {
        Map<String, List<String>> map = new HashMap<>();
        if(headersViewModel == null) {
            return map;
        }
        for(Param param : headersViewModel.toList()) {
            if(map.containsKey(param.key)) {
                map.get(param.key).add(param.value);
            } else {
                List<String> list = new ArrayList<>();
                list.add(param.value);
                map.put(param.key, list);
            }
        }
        return map;
    }

    //region Getters and Setters
    String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    HashMap<String, String> getBodyParameters() {
        return bodyParameters;
    }

    public void setBodyParameters(HashMap<String, String> bodyParameters) {
        this.bodyParameters = bodyParameters;
    }

    HashMap<String, String> getQueryParameters() {
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
        this.headers = (Map<String, List<String>>) in.readSerializable();
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
        dest.writeSerializable((HashMap)headers);
        dest.writeSerializable(queryParameters);
        dest.writeSerializable(bodyParameters);
    }

    @NonNull
    @Override
    public String toString() {
        return "\nURL: " + this.url
                + "\nMethod: " + this.method
                + "\nQuery Methods\n"
                + getMapString(this.queryParameters)
                + "\nHeaders\n"
                + getMapString(this.headers)
                + "\nParameters\n"
                + getMapString(this.bodyParameters);
    }

    private String getMapString(HashMap<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<String, String> entry : map.entrySet() ) {
            stringBuilder.append("Key: ");
            stringBuilder.append(entry.getKey());
            stringBuilder.append('\t');
            stringBuilder.append("Value: ");
            stringBuilder.append(entry.getValue());
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    private String getMapString(Map<String, List<String>> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<String, List<String>> entry : map.entrySet() ) {
            stringBuilder.append("Key: ");
            stringBuilder.append(entry.getKey());
            stringBuilder.append('\t');
            stringBuilder.append("Value: ");
            stringBuilder.append(entry.getValue().toString());
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }
}
