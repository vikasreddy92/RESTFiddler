package com.vikasreddy.restfiddler;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ResponseCookiesFragment extends Fragment {
    private static final String TAG = "ResponseCookiesFragment";
    private static final String RESPONSE = "response";
    private TextView tvResponseCookies;

    public static ResponseCookiesFragment newInstance(FragmentManager fragmentManager
            , ParcelableResponse response) {
        ResponseCookiesFragment responseCookiesFragment = new ResponseCookiesFragment();
        Bundle args = new Bundle();
        args.putParcelable(RESPONSE, response);
        responseCookiesFragment.setArguments(args);
        return responseCookiesFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.response_cookies_fragment, container, false);
        tvResponseCookies = view.findViewById(R.id.tv_response_cookies);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ResponseCookiesViewModel mViewModel = ViewModelProviders.of(this).get(ResponseCookiesViewModel.class);
        if(getArguments() != null) {
            ParcelableResponse response = getArguments().getParcelable(RESPONSE);
            if(response != null && response.getBody() != null) {
                mViewModel.setCookies(Utility.getMapString(response.getCookies()));
            }
        }
        tvResponseCookies.setText(mViewModel.getCookies());
        Log.d(TAG, "onActivityCreated: " + mViewModel.getCookies());
    }
}
