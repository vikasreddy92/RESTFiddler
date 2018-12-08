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

public class ResponseBodyFragment extends Fragment {
    private static final String TAG = "ResponseBodyFragment";
    private static final String RESPONSE = "response";
    private TextView tvResponseBody;

    public static ResponseBodyFragment newInstance(FragmentManager fragmentManager
            , ParcelableResponse response) {
        ResponseBodyFragment responseBodyFragment = new ResponseBodyFragment();
        Bundle args = new Bundle();
        args.putParcelable(RESPONSE, response);
        responseBodyFragment.setArguments(args);
        return responseBodyFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.response_body_fragment, container, false);
        tvResponseBody = view.findViewById(R.id.response_body);
        if(getArguments() != null) {
            ParcelableResponse response = getArguments().getParcelable(RESPONSE);
            if(response != null && response.getBody() != null) {
                if(savedInstanceState != null) {
                    savedInstanceState.putParcelable(RESPONSE, response);
                }
                tvResponseBody.setText(response.getBody());
                Log.d(TAG, "onCreateView: " + response.getBody());
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ResponseBodyViewModel mViewModel = ViewModelProviders.of(this).get(ResponseBodyViewModel.class);

        if(getArguments() != null) {
            ParcelableResponse response = getArguments().getParcelable(RESPONSE);
            if(response != null && response.getBody() != null) {
                mViewModel.setBody(response.getBody());
            }
        }
        tvResponseBody.setText(mViewModel.getBody());
        Log.d(TAG, "onActivityCreated: " + mViewModel.getBody());
    }
}
