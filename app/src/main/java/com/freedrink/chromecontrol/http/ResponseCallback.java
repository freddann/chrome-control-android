package com.freedrink.chromecontrol.http;

import android.util.Log;

public interface ResponseCallback {
    public static final ResponseCallback LOG = new LogResponseCallback();

    public void call(String result);

    public class LogResponseCallback implements ResponseCallback {
        @Override
        public void call(String result) {
            Log.d("HttpResponse", result);
        }
    }
}
