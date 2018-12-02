package com.vikasreddy.restfiddler;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class HeadersRecyclerViewAdapter
        extends RecyclerView.Adapter<HeadersRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "HeadersRecyclerViewAdap";
    private HeadersViewModel headersViewModel;
    private Context context;
    private HeaderListener listener;
    private ArrayAdapter<String> headersArrayAdapter;
    private Drawable addDrawable;
    private Drawable removeDrawable;

    public interface HeaderListener {
        void onHeaderViewModelUpdated(HeadersViewModel headersViewModel);
    }

    HeadersRecyclerViewAdapter(Context context, HeadersViewModel headersViewModel) {
        this.headersViewModel = headersViewModel;
        this.context = context;
        String[] headers = this.context.getResources().getStringArray(R.array.http_headers);
        this.headersArrayAdapter =
                new ArrayAdapter<>(this.context
                        , android.R.layout.simple_list_item_1, headers);
        this.addDrawable = ContextCompat.getDrawable(context, R.drawable.ic_add_24px);
        this.removeDrawable = ContextCompat.getDrawable(context, R.drawable.ic_remove_24px);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.headers
                , viewGroup, false);
        return new HeadersRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final int adapterPos = viewHolder.getAdapterPosition();
        viewHolder.actvKey.setText(headersViewModel.get(adapterPos).key);
        viewHolder.etValue.setText(headersViewModel.get(adapterPos).value);

        if(headersViewModel.get(adapterPos).isKeyNullOrWhiteSpace()
                && adapterPos != 0) {
            viewHolder.actvKey.requestFocus();
        }

        if (headersViewModel.get(adapterPos).isKeyNullOrWhiteSpace()
                || headersViewModel.get(adapterPos).isValueNullOrWhiteSpace()) {
            viewHolder.ibAction.setTag("add");
            viewHolder.ibAction.setImageDrawable(this.addDrawable);
        } else {
            viewHolder.ibAction.setTag("remove");
            viewHolder.ibAction.setImageDrawable(this.removeDrawable);
        }

        viewHolder.actvKey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
                    String key = viewHolder.actvKey.getText().toString();
                    String value = viewHolder.etValue.getText().toString();
                    if(!key.isEmpty() && !value.isEmpty()) {
                        headersViewModel.addAt(key, value, adapterPos);
                        headersViewModel.add("", "");
                        notifyDataSetChanged();
                        listener.onHeaderViewModelUpdated(headersViewModel);
                    }
                    handled = true;
                }
                return handled;
            }
        });

        viewHolder.ibAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().toString().equals("add")) {
                    String key = viewHolder.actvKey.getText().toString();
                    String value = viewHolder.etValue.getText().toString();
                    if (!key.isEmpty() && !value.isEmpty()) {
                        headersViewModel.addAt(key, value, adapterPos);
                        headersViewModel.add("", "");
                        notifyDataSetChanged();
                        listener.onHeaderViewModelUpdated(headersViewModel);
                    }
                } else {
                    headersViewModel.removeAt(adapterPos);
                    notifyDataSetChanged();
                    listener.onHeaderViewModelUpdated(headersViewModel);
                    if (headersViewModel.size() <= 0) {
                        headersViewModel.add("", "");
                        notifyDataSetChanged();
                        listener.onHeaderViewModelUpdated(headersViewModel);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return headersViewModel.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (context instanceof HeaderListener) {
            listener = (HeaderListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement HeaderListener interface");
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        listener = null;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private AutoCompleteTextView actvKey;
        private EditText etValue;
        private ImageButton ibAction;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            actvKey = itemView.findViewById(R.id.header_key);
            etValue = itemView.findViewById(R.id.header_value);
            ibAction = itemView.findViewById(R.id.action_header);

            actvKey.setAdapter(headersArrayAdapter);
        }
    }
}
