package com.wuhk.devworklib.http.urlconnect;

import com.wuhk.devworklib.http.DWHttpClient;
import com.wuhk.devworklib.http.DWRequest;
import com.wuhk.devworklib.http.DWResponse;

import java.net.URLConnection;

/**
 * Created by wuhk on 2016/5/29.
 */
public class DWHttpUrlConnectionClient implements DWHttpClient {

    private static final String TAG = "DWHttpUrlConnectionCl..";

    @Override
    public DWResponse postJson(DWRequest dwRequest) {
        return null;
    }

    @Override
    public DWResponse post(DWRequest dwRequest) {
        return null;
    }

    @Override
    public DWResponse get(DWRequest dwRequest) {
        return null;
    }

    @Override
    public DWResponse getDownload(DWRequest dwRequest) {
        return null;
    }

    @Override
    public DWResponse postDownload(DWRequest dwRequest) {
        return null;
    }

    @Override
    public DWResponse upload(DWRequest dwRequest) {
        return null;
    }

    private void initRequest(URLConnection conn , DWRequest dwRequest){

    }
}
