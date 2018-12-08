package com.vikasreddy.restfiddler;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Response;

public class ParcelableResponse implements Parcelable {
    private ParcelableRequest request;
    private int statusCode;
    private String statusMessage;
    private String body;
    private HashMap<String,List<String>> headers;
    private HashMap<String, String> cookies;

    ParcelableResponse(Response response) throws IOException {
        this.request = new ParcelableRequest(response.request());
        this.statusCode = response.code();
        this.statusMessage = response.message();
        if(response.body() != null) {
            this.body = response.body().string();
        } else {
            this.body = "";
        }
        this.headers = toHashMap(response.headers().toMultimap());
        this.cookies = new HashMap<>();
        if(this.headers.containsKey("set-cookie")) {
            for(String cookie: headers.get("set-cookie")) {
                String[] cookieContents = cookie.split("=");
                cookies.put(cookieContents[0], cookieContents[1]);
            }
            this.headers.remove("set-cookie");
        }
        response.close();
    }

    private ParcelableResponse(Parcel in) {
        request = in.readParcelable(ParcelableRequest.class.getClassLoader());
        statusCode = in.readInt();
        statusMessage = in.readString();
        body = in.readString();
    }

    public static final Creator<ParcelableResponse> CREATOR = new Creator<ParcelableResponse>() {
        @Override
        public ParcelableResponse createFromParcel(Parcel in) {
            return new ParcelableResponse(in);
        }

        @Override
        public ParcelableResponse[] newArray(int size) {
            return new ParcelableResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(request, flags);
        dest.writeInt(statusCode);
        dest.writeString(statusMessage);
        dest.writeString(body);
        dest.writeSerializable(headers);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nHTTP Code: ");
        stringBuilder.append(this.statusCode);
        stringBuilder.append("\nHttp Message: ");
        stringBuilder.append(this.statusMessage);
        stringBuilder.append("\nHeaders\n");
        stringBuilder.append(getMultiMapString(headers));
        stringBuilder.append("\nCookies\n");
        stringBuilder.append(getMapString(cookies));
        stringBuilder.append("\nBody\n");
        stringBuilder.append(body);
        return stringBuilder.toString();
    }

    private String getMapString(HashMap<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<String, String> entry : map.entrySet() ) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append(" --> ");
            stringBuilder.append(entry.getValue());
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    private String getMultiMapString(HashMap<String, List<String>> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<String, List<String>> entry : map.entrySet() ) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append(" --> ");
            stringBuilder.append(entry.getValue().toString());
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    private HashMap<String, List<String>> toHashMap(Map<String, List<String>> map) {
        HashMap<String, List<String>> hashMap = new HashMap<>();
        for(Map.Entry<String, List<String>> entry: map.entrySet()) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        return hashMap;
    }

    //region Getters & Setters

    public ParcelableRequest getRequest() {
        return request;
    }

    public void setRequest(ParcelableRequest request) {
        this.request = request;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, List<String>> headers) {
        this.headers = headers;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(HashMap<String, String> cookies) {
        this.cookies = cookies;
    }
    //endregion
}
