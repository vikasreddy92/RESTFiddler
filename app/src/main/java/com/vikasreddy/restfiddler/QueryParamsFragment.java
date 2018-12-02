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

public class QueryParamsFragment extends Fragment {
    private static final String TAG = "QueryParamsFragment";
    private RecyclerView recyclerView;
    private QueryParamsViewModel mViewModel;
    private QueryParamsRecyclerViewAdapter adapter;

    public static QueryParamsFragment newInstance() {
        return new QueryParamsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.query_params_fragment, container, false);
        recyclerView = view.findViewById(R.id.query_params_recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(QueryParamsViewModel.class);
        adapter = new QueryParamsRecyclerViewAdapter(getContext(), mViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
