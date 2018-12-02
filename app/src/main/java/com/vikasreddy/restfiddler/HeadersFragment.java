package com.vikasreddy.restfiddler;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HeadersFragment extends Fragment {
    private static final String TAG = "HeadersFragment";
    private RecyclerView recyclerView;
    private HeadersViewModel mViewModel;
    private HeadersRecyclerViewAdapter adapter;

    public static HeadersFragment newInstance() {
        return new HeadersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.headers_fragment, container, false);
        recyclerView = view.findViewById(R.id.headers_recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HeadersViewModel.class);
        adapter = new HeadersRecyclerViewAdapter(getContext(), mViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
