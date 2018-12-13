package com.vikasreddy.restfiddler;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.Request;

public class NetworkFragment extends Fragment {
    private static final String TAG = "NetworkFragment";

    private ParcelableRequest parcelableRequest;
    private RequestCallback requestCallback;
    private RequestTask requestTask;

    public static NetworkFragment getInstance(FragmentManager fragmentManager
            , ParcelableRequest parcelableRequest) {

        NetworkFragment networkFragment = (NetworkFragment) fragmentManager
                .findFragmentByTag(NetworkFragment.TAG);

        if (networkFragment != null) {
            return networkFragment;
        }

        networkFragment = new NetworkFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.REQUEST, parcelableRequest);
        networkFragment.setArguments(args);
        fragmentManager.beginTransaction().add(networkFragment, TAG).commit();
        return networkFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        parcelableRequest = getArguments().getParcelable(Constants.REQUEST);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        requestCallback = (RequestCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        requestCallback = null;
    }

    @Override
    public void onDestroy() {
        cancelDownload();
        super.onDestroy();
    }

    public void startRequest() {
        requestTask = new RequestTask(requestCallback);
        parcelableRequest = getArguments().getParcelable(Constants.REQUEST);
        Request request = populateRequest(parcelableRequest);
        requestTask.execute(request);
    }

    public void cancelDownload() {
        if (requestTask != null) {
            requestTask.cancel(true);
        }
    }

    private Request populateRequest(ParcelableRequest parcelableRequest) {
        Request request = null;
        StringBuilder url = new StringBuilder(parcelableRequest.getUrl());
        String method = parcelableRequest.getMethod();
        try {
            Request.Builder requestBuilder = new Request.Builder();
            if (method.equalsIgnoreCase("get")) {
                requestBuilder.get();
            } else if (method.equalsIgnoreCase("post")) {
                MultipartBody.Builder multiPartBodyBuilder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);
                if (parcelableRequest.getBodyParameters() != null
                        && parcelableRequest.getBodyParameters().size() > 0) {
                    for (Map.Entry<String, String> p : parcelableRequest.getBodyParameters().entrySet()) {
                        multiPartBodyBuilder.addFormDataPart(p.getKey(), p.getValue());
                    }
                }
                requestBuilder.post(multiPartBodyBuilder.build());
            } else if(method.equalsIgnoreCase("put")){
                MultipartBody.Builder multiPartBodyBuilder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);
                if (parcelableRequest.getBodyParameters() != null
                        && parcelableRequest.getBodyParameters().size() > 0) {
                    for (Map.Entry<String, String> p : parcelableRequest.getBodyParameters().entrySet()) {
                        multiPartBodyBuilder.addFormDataPart(p.getKey(), p.getValue());
                    }
                }
                requestBuilder.put(multiPartBodyBuilder.build());
            } else if(method.equalsIgnoreCase("delete")) {
                MultipartBody.Builder multiPartBodyBuilder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM);
                if (parcelableRequest.getBodyParameters() != null
                        && parcelableRequest.getBodyParameters().size() > 0) {
                    for (Map.Entry<String, String> p : parcelableRequest.getBodyParameters().entrySet()) {
                        multiPartBodyBuilder.addFormDataPart(p.getKey(), p.getValue());
                    }
                }
                requestBuilder.delete(multiPartBodyBuilder.build());
            } else {
                Log.d(TAG, "createRequest: Method [ " + method + " ] not supported.");
                request = null;
            }

            if (parcelableRequest.getHeaders() != null && parcelableRequest.getHeaders().size() > 0) {
                for (Map.Entry<String, String> p : parcelableRequest.getHeaders().entrySet()) {
                    requestBuilder.addHeader(p.getKey(), p.getValue());
                }
            }

            if (parcelableRequest.getQueryParameters() != null && parcelableRequest.getQueryParameters().size() > 0) {
                url.append('?');
                for (Map.Entry<String, String> p : parcelableRequest.getQueryParameters().entrySet()) {
                    url.append(p.getKey());
                    url.append('=');
                    url.append(p.getValue());
                    url.append('&');
                }
                url.deleteCharAt(url.toString().length() - 1);
            }

            requestBuilder.url(url.toString());
            request = requestBuilder.build();
        } catch (Exception e) {
            Log.d(TAG, "makeRequest: ", e);
        }
        return request;

    }
}