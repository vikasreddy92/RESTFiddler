package com.vikasreddy.restfiddler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import okhttp3.Request;
import okhttp3.Response;

public class ResponseActivity
        extends AppCompatActivity
        implements RequestCallback {
    private static final String TAG = "ResponseActivity";
    private RelativeLayout mMainView;
    private NetworkFragment networkFragment;
    private boolean isRequestInProgress = false;

    private TextView tvResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        this.mMainView = findViewById(R.id.response_container);
        this.tvResponse = findViewById(R.id.tvResponse);

        this.networkFragment = NetworkFragment.getInstance(getSupportFragmentManager()
                , getParcelableRequest(savedInstanceState));

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
            tvResponse.setText(result.exception.getMessage());
        } else if(result.response != null) {
            tvResponse.setText(result.response.toString());
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
        return parcelableRequest;
    }

    private void startRequest() {
        if(!isRequestInProgress && networkFragment != null) {
            networkFragment.startRequest();
            isRequestInProgress = true;
        }
    }
}
