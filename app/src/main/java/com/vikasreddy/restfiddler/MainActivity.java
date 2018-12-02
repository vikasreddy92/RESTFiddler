package com.vikasreddy.restfiddler;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
        TabLayout tabLayout = findViewById(R.id.tl_params);
        FloatingActionButton fabSend = findViewById(R.id.fab_send);
        viewPager = findViewById(R.id.view_pager);

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
            for (int i = 0; i < bodyViewModel.size(); i++) {
                Log.d(TAG, "onClick: bVM: " + i + ") " + bodyViewModel.get(i).key + ", " + bodyViewModel.get(i).value);
            }

            if(headersViewModel != null)
            for (int i = 0; i < headersViewModel.size(); i++) {
                Log.d(TAG, "onClick: hVM: " + i + ") " + headersViewModel.get(i).key + ", " + headersViewModel.get(i).value);
            }

            if(queryParamsViewModel != null)
            for (int i = 0; i < queryParamsViewModel.size(); i++) {
                Log.d(TAG, "onClick: qPVM: " + i + ") " + queryParamsViewModel.get(i).key + ", " + queryParamsViewModel.get(i).value);
            }
            Snackbar.make(mainLayout, "Sending request", Snackbar.LENGTH_SHORT).show();
            makeRequest();
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

    class MakeRequest extends AsyncTask<Request, Integer, Long> {
        OkHttpClient httpClient = new OkHttpClient();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Long doInBackground(Request... requests) {
            try {
                Response response = httpClient.newCall(requests[0]).execute();
                Log.d(TAG, "makeRequest: Response Status code: " + response.code());
                Log.d(TAG, "makeRequest: Response Headers:\n");
                for (Map.Entry<String, List<String>> entry:response.headers().toMultimap().entrySet()) {
                    Log.d(TAG, "makeRequest: " + entry.getKey() + " -> " + entry.getValue().toString());
                }
                Log.d(TAG, "makeRequest: " + response.body().string());
            } catch (Exception e) {
                Log.d(TAG, "doInBackground: ", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
    private void makeRequest()
    {
        StringBuilder url = new StringBuilder(etUrl.getText().toString());
        String method = getMethod().toUpperCase();
        try {
            Request.Builder request = new Request.Builder();
            request.get();
            if(headersViewModel != null && headersViewModel.size() > 0)
            for (Param p: headersViewModel.toList()) {
                request.addHeader(p.key, p.value);
            }
            url.append('?');
            if(queryParamsViewModel != null && queryParamsViewModel.size() > 0)
                for (Param p: queryParamsViewModel.toList()) {
                url.append(p.key);
                url.append('=');
                url.append(p.value);
                url.append('&');
            }
            request.url(url.toString());
            new MakeRequest().execute(request.build());
        } catch (Exception e) {
            Log.d(TAG, "makeRequest: ", e);
        }
    }

    private void getRequest(String url) {

    }

    private String getMethod() {
        int selectedMethod = rbMethodGroup.getCheckedRadioButtonId();
        rbGetMethod = (RadioButton) findViewById(selectedMethod);
        return rbGetMethod.getText().toString();
    }
}
