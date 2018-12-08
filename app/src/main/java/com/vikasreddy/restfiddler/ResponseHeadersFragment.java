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

public class ResponseHeadersFragment extends Fragment {
    private static final String TAG = "ResponseHeadersFragment";
    private static final String RESPONSE = "response";
    private TextView tvResponseHeaders;

    public static ResponseHeadersFragment newInstance(FragmentManager fragmentManager
            , ParcelableResponse response) {
        ResponseHeadersFragment responseHeadersFragment = new ResponseHeadersFragment();
        Bundle args = new Bundle();
        args.putParcelable(RESPONSE, response);
        responseHeadersFragment.setArguments(args);
        return responseHeadersFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.response_header_fragment, container, false);
        tvResponseHeaders = view.findViewById(R.id.tv_response_headers);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ResponseHeaderViewModel mViewModel = ViewModelProviders.of(this).get(ResponseHeaderViewModel.class);
        if(getArguments() != null) {
            ParcelableResponse response = getArguments().getParcelable(RESPONSE);
            if(response != null && response.getBody() != null) {
                mViewModel.setHeaders(Utility.getMultiMapString(response.getHeaders()));
            }
        }
        tvResponseHeaders.setText(mViewModel.getHeaders());
        Log.d(TAG, "onActivityCreated: " + mViewModel.getHeaders());    }

}
