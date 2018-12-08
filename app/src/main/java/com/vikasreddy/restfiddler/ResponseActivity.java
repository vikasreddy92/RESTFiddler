package com.vikasreddy.restfiddler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ResponseActivity
        extends AppCompatActivity
        implements RequestCallback {
    private static final String TAG = "ResponseActivity";
    private RelativeLayout mMainView;
    private NetworkFragment networkFragment;
    private boolean isRequestInProgress = false;
    private String body;

    private TabLayout tlResponse;
    private ViewPager viewPager;
    private ResponsePageAdapter responsePageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_layout);
        this.networkFragment = NetworkFragment.getInstance(getSupportFragmentManager()
                , getParcelableRequest(savedInstanceState));
    }

    private void initUI(ParcelableResponse response) {
        setContentView(R.layout.activity_response);
        this.mMainView = findViewById(R.id.response_container);
        this.tlResponse = findViewById(R.id.tl_response);
        this.viewPager = findViewById(R.id.vp_response);
        this.tlResponse.addOnTabSelectedListener(tabSelectedListener);
        responsePageAdapter =
                new ResponsePageAdapter(getSupportFragmentManager()
                        , tlResponse.getTabCount(), response);
        viewPager.setAdapter(responsePageAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlResponse));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        startRequest();
    }

    //region RequestCallback implementations
    @Override
    public void updateFromRequest(Object resultObj) {
        Log.d(TAG, "updateFromRequest: Finished Request");
        if(resultObj == null) {
            Log.d(TAG, "updateFromRequest: null result");
            return;
        }

        RequestTask.Result result = ((RequestTask.Result)resultObj);
        if(result.exception != null) {
//            tvResponse.setText(result.exception.getMessage());
        } else if(result.response != null) {
            try {
                ParcelableResponse res = new ParcelableResponse(result.response);
                body = res.getBody();
                Log.d(TAG, "updateFromRequest: " + res.toString());
                Log.d(TAG, "updateFromRequest: Rendering Response Activity");
                setContentView(R.layout.activity_response);
                initUI(res);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {

    }

    @Override
    public void finishDownloading() {
        if(networkFragment != null) {
            networkFragment.cancelDownload();
        }
    }
    //endregion

    private TabLayout.OnTabSelectedListener tabSelectedListener =
            new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());
            if(tab.getPosition() == 0) {
                Snackbar.make(mMainView, "Body", Snackbar.LENGTH_LONG).show();
            } else if(tab.getPosition() == 1) {
                Snackbar.make(mMainView, "Cookies", Snackbar.LENGTH_LONG).show();
            } else if(tab.getPosition() == 1) {
                Snackbar.make(mMainView, "Headers", Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };


    private ParcelableRequest getParcelableRequest(Bundle savedInstanceState) {
        ParcelableRequest parcelableRequest;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                parcelableRequest = null;
            } else {
                parcelableRequest = extras.getParcelable(Constants.REQUEST);
            }
        } else {
            parcelableRequest = savedInstanceState.getParcelable(Constants.REQUEST);
        }
        if (parcelableRequest != null) {
            Log.d(TAG, "\ngetParcelableRequest: " + parcelableRequest.toString());
        } else {
            Log.d(TAG, "\ngetParcelableRequest: parcelableRequest is null.");
        }
        return parcelableRequest;
    }

    private void startRequest() {
        if(!isRequestInProgress && networkFragment != null) {
            networkFragment.startRequest();
            isRequestInProgress = true;
        }
    }

    public String getBody() {
        if(body != null && !body.isEmpty()) {
            return body;
        } else {
            return "body is not ready yet!";
        }
    }
}
