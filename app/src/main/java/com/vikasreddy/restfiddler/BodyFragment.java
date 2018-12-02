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

public class BodyFragment extends Fragment {
    private static final String TAG = "BodyFragment";
    private RecyclerView recyclerView;
    private BodyViewModel mViewModel;
    private BodyRecyclerViewAdapter adapter;

    public static BodyFragment newInstance() {
        return new BodyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.body_fragment, container, false);
        recyclerView = view.findViewById(R.id.body_recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(BodyViewModel.class);
        adapter = new BodyRecyclerViewAdapter(getContext(), mViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
