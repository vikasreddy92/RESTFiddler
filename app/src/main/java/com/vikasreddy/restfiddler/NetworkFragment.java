package com.vikasreddy.restfiddler;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import okhttp3.Request;

public class NetworkFragment extends Fragment {
    private static final String TAG = "NetworkFragment";

    private RequestCallback requestCallback;
    private RequestTask requestTask;

    public static NetworkFragment getInstance(FragmentManager fragmentManager, Request request) {
        NetworkFragment networkFragment = (NetworkFragment) fragmentManager
                .findFragmentByTag(NetworkFragment.TAG);

        if (networkFragment != null) {
            return networkFragment;
        }

        networkFragment = new NetworkFragment();
        fragmentManager.beginTransaction().add(networkFragment, TAG).commit();
        return networkFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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

    public void startDownload(Request request) {
        requestTask = new RequestTask(requestCallback);
        requestTask.execute(request);
    }

    public void cancelDownload() {
        if (requestTask != null) {
            requestTask.cancel(true);
        }
    }
}