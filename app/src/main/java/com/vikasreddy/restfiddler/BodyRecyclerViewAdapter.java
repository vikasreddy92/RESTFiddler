package com.vikasreddy.restfiddler;

import android.content.Context;
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

public class BodyRecyclerViewAdapter extends RecyclerView.Adapter<BodyRecyclerViewAdapter.ViewHolder> {
    private BodyViewModel bodyViewModel;
    private Context context;
    private BodyListener listener;

    public interface BodyListener {
        void onBodyViewModelUpdated(BodyViewModel bodyViewModel);
    }

    BodyRecyclerViewAdapter(Context context, BodyViewModel bodyViewModel) {
        this.bodyViewModel = bodyViewModel;
        this.context = context;
    }

    @NonNull
    @Override
    public BodyRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.body
                , viewGroup, false);
        return new BodyRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final int adapterPos = viewHolder.getAdapterPosition();
        viewHolder.etKey.setText(bodyViewModel.get(adapterPos).key);
        viewHolder.etValue.setText(bodyViewModel.get(adapterPos).value);

        if(bodyViewModel.get(adapterPos).isKeyNullOrWhiteSpace()
                && adapterPos != 0) {
            viewHolder.etKey.requestFocus();
        }

        if (bodyViewModel.get(adapterPos).isKeyNullOrWhiteSpace()
                || bodyViewModel.get(adapterPos).isValueNullOrWhiteSpace()) {
            viewHolder.ibAction.setTag("add");
            viewHolder.ibAction.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_add_24px));
        } else {
            viewHolder.ibAction.setTag("remove");
            viewHolder.ibAction.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_remove_24px));
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
                        bodyViewModel.addAt(key, value, adapterPos);
                        bodyViewModel.add("", "");
                        notifyDataSetChanged();
                        listener.onBodyViewModelUpdated(bodyViewModel);
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
                    String key = viewHolder.etKey.getText().toString();
                    String value = viewHolder.etValue.getText().toString();
                    if (!key.isEmpty() && !value.isEmpty()) {
                        bodyViewModel.addAt(key, value, adapterPos);
                        bodyViewModel.add("", "");
                        notifyDataSetChanged();
                        listener.onBodyViewModelUpdated(bodyViewModel);
                    }
                } else {
                    bodyViewModel.removeAt(adapterPos);
                    notifyDataSetChanged();
                    listener.onBodyViewModelUpdated(bodyViewModel);
                    if (bodyViewModel.size() <= 0) {
                        bodyViewModel.add("", "");
                        notifyDataSetChanged();
                        listener.onBodyViewModelUpdated(bodyViewModel);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bodyViewModel.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (context instanceof BodyListener) {
            listener = (BodyListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement BodyListener interface");
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
            etKey = itemView.findViewById(R.id.body_key);
            etValue = itemView.findViewById(R.id.body_value);
            ibAction = itemView.findViewById(R.id.action_body);
        }
    }
}
