package com.vikasreddy.restfiddler;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class QueryParamsRecyclerViewAdapter
        extends RecyclerView.Adapter<QueryParamsRecyclerViewAdapter.ViewHolder>{
    private QueryParamsViewModel queryParamsViewModel;
    private Context context;
    private QueryParamsListener listener;
    private Drawable addDrawable;
    private Drawable removeDrawable;

    public interface QueryParamsListener {
        void onQueryParamsViewModelUpdated(QueryParamsViewModel queryParamsViewModel);
    }

    QueryParamsRecyclerViewAdapter(Context context, QueryParamsViewModel viewModel) {
        this.context = context;
        this.queryParamsViewModel = viewModel;
        this.addDrawable = ContextCompat.getDrawable(context, R.drawable.ic_add_24px);
        this.removeDrawable = ContextCompat.getDrawable(context, R.drawable.ic_remove_24px);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.query_params
                , viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final int adapterPos = viewHolder.getAdapterPosition();
        viewHolder.etKey.setText(queryParamsViewModel.get(adapterPos).key);
        viewHolder.etValue.setText(queryParamsViewModel.get(adapterPos).value);

        if(queryParamsViewModel.get(adapterPos).isKeyNullOrWhiteSpace()
                && adapterPos != 0) {
            viewHolder.etKey.requestFocus();
        }

        if(queryParamsViewModel.get(adapterPos).isKeyNullOrWhiteSpace()
                || queryParamsViewModel.get(adapterPos).isValueNullOrWhiteSpace()) {
            viewHolder.ibAction.setTag("add");
            viewHolder.ibAction.setImageDrawable(this.addDrawable);
        } else {
            viewHolder.ibAction.setTag("remove");
            viewHolder.ibAction.setImageDrawable(this.removeDrawable);
        }

        viewHolder.etKey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_NEXT) {
                    viewHolder.etValue.requestFocus();
                    handled = true;
                }
                return handled;
            }
        });

        viewHolder.etValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_NEXT) {
                    String key = viewHolder.etKey.getText().toString();
                    String value = viewHolder.etValue.getText().toString();
                    if(!key.isEmpty() && !value.isEmpty()) {
                        queryParamsViewModel.addAt(key, value, adapterPos);
                        queryParamsViewModel.add("", "");
                        notifyDataSetChanged();
                        listener.onQueryParamsViewModelUpdated(queryParamsViewModel);
                    }
                    handled = true;
                }
                return handled;
            }
        });

        viewHolder.ibAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag().toString().equals("add")) {
                    String key = viewHolder.etKey.getText().toString();
                    String value = viewHolder.etValue.getText().toString();
                    if(!key.isEmpty() && !value.isEmpty()) {
                        queryParamsViewModel.addAt(key, value, adapterPos);
                        queryParamsViewModel.add("", "");
                        notifyDataSetChanged();
                        listener.onQueryParamsViewModelUpdated(queryParamsViewModel);
                    }
                } else {
                    queryParamsViewModel.removeAt(adapterPos);
                    notifyDataSetChanged();
                    listener.onQueryParamsViewModelUpdated(queryParamsViewModel);
                    if(queryParamsViewModel.size() <= 0) {
                        queryParamsViewModel.add("", "");
                        notifyDataSetChanged();
                        listener.onQueryParamsViewModelUpdated(queryParamsViewModel);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return queryParamsViewModel.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if(context instanceof QueryParamsListener) {
            listener = (QueryParamsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement QueryParamsListener interface");
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        listener = null;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private EditText etKey;
        private EditText etValue;
        private ImageButton ibAction;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            etKey = itemView.findViewById(R.id.query_param_key);
            etValue = itemView.findViewById(R.id.query_param_value);
            ibAction = itemView.findViewById(R.id.action_query_parameter);
        }
    }
}
