package com.vikasreddy.restfiddler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

public class MainActivity
        extends AppCompatActivity
        implements BodyRecyclerViewAdapter.BodyListener
        , HeadersRecyclerViewAdapter.HeaderListener
        , QueryParamsRecyclerViewAdapter.QueryParamsListener {

    private static final String TAG = "MainActivity";
    private RelativeLayout mainLayout;
    private EditText etUrl;
    private RadioGroup rbMethodGroup;
    private RadioButton rbGetMethod;
    private ViewPager viewPager;

    private BodyViewModel bodyViewModel;
    private HeadersViewModel headersViewModel;
    private QueryParamsViewModel queryParamsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        mainLayout = findViewById(R.id.main_layout);
        etUrl = findViewById(R.id.et_url);
        rbMethodGroup = findViewById(R.id.method);
        rbGetMethod = findViewById(R.id.rb_get);
        viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tl_params);
        FloatingActionButton fabSend = findViewById(R.id.fab_send);

        fabSend.setOnClickListener(fabSendOnClickListener);
        tabLayout.addOnTabSelectedListener(tabSelectedListener);

        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    //region Listeners
    private View.OnClickListener fabSendOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(bodyViewModel != null)
            {
                for (int i = 0; i < bodyViewModel.size(); i++) {
                    Log.d(TAG, "onClick: bodyViewModel: " + i + ") "
                            + bodyViewModel.get(i).key + ", " + bodyViewModel.get(i).value);
                }
            }
            if(headersViewModel != null)
            {
                for (int i = 0; i < headersViewModel.size(); i++) {
                    Log.d(TAG, "onClick: headersViewModel: " + i + ") "
                            + headersViewModel.get(i).key + ", " + headersViewModel.get(i).value);
                }
            }
            if(queryParamsViewModel != null)
            {
                for (int i = 0; i < queryParamsViewModel.size(); i++) {
                    Log.d(TAG, "onClick: queryParamsViewModel: " + i + ") "
                            + queryParamsViewModel.get(i).key + ", "
                            + queryParamsViewModel.get(i).value);
                }
            }
            Snackbar.make(mainLayout, "Sending request", Snackbar.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), ResponseActivity.class);
            intent.putExtra(Constants.REQUEST, getParcelableRequest());
            startActivity(intent);
        }
    };

    private TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());
            if(tab.getPosition() == 0) {
                Snackbar.make(mainLayout, "HeadersFragment", Snackbar.LENGTH_LONG).show();
            } else if(tab.getPosition() == 1) {
                Snackbar.make(mainLayout, "Query Params", Snackbar.LENGTH_LONG).show();
            } else if(tab.getPosition() == 2) {
                Snackbar.make(mainLayout, "Body", Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    @Override
    public void onBodyViewModelUpdated(BodyViewModel bodyViewModel) {
        this.bodyViewModel = bodyViewModel;
    }

    @Override
    public void onHeaderViewModelUpdated(HeadersViewModel headersViewModel) {
        this.headersViewModel = headersViewModel;
    }

    @Override
    public void onQueryParamsViewModelUpdated(QueryParamsViewModel queryParamsViewModel) {
        this.queryParamsViewModel = queryParamsViewModel;
    }
    //endregion

    private ParcelableRequest getParcelableRequest() {
        ParcelableRequest parcelableRequest = null;
        try {
            String url = etUrl.getText().toString();
            String method = getMethod().toUpperCase();
            parcelableRequest = new ParcelableRequest(url, method, this.headersViewModel
                    , this.queryParamsViewModel, this.bodyViewModel);
        } catch (Exception e) {
            Log.d(TAG, "getParcelableRequest: ", e);
        }
        return parcelableRequest;
    }

    private String getMethod() {
        int selectedMethod = rbMethodGroup.getCheckedRadioButtonId();
        rbGetMethod = (RadioButton) findViewById(selectedMethod);
        return rbGetMethod.getText().toString();
    }
}
