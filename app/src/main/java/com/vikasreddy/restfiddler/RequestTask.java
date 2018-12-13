package com.vikasreddy.restfiddler;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@SuppressWarnings("unchecked")
public class RequestTask extends AsyncTask<Request, Integer, RequestTask.Result> {
    private static final String TAG = "RequestTask";
    private RequestCallback requestCallback;

    private OkHttpClient httpClient = new OkHttpClient();

    RequestTask(RequestCallback requestCallback) {
        this.requestCallback = requestCallback;
    }

    @Override
    protected RequestTask.Result doInBackground(Request... requests) {
        try {
            if(requests == null) {
                return new Result(new Exception("Request is null"));
            }
            Response response = httpClient.newCall(requests[0]).execute();
            return new Result(response);
        } catch (Exception e) {
            Log.d(TAG, "doInBackground: ", e);
            return new Result(e);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (requestCallback != null) {
            NetworkInfo networkInfo = requestCallback.getActiveNetworkInfo();
            if (networkInfo == null
                    || !networkInfo.isConnected()
                    || (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                    && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                requestCallback.updateFromRequest(new Result(new Exception("No internet available.")));
                cancel(true);
            }
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (result != null && requestCallback != null) {
            if (result.exception != null) {
                requestCallback.updateFromRequest(result);
            } else if (result.response != null) {
                requestCallback.updateFromRequest(result);
            }
            requestCallback.finishDownloading();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Result result) {
        super.onCancelled(result);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    static class Result {
        Response response;
        Exception exception;

        Result(Response responseObj) {
            response = responseObj;
        }

        Result(Exception exceptionObj) {
            exception = exceptionObj;
        }
    }
}
