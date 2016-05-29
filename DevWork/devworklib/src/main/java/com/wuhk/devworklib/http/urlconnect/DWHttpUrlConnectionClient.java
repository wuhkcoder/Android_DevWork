package com.wuhk.devworklib.http.urlconnect;

import com.wuhk.devworklib.http.DWHttpClient;
import com.wuhk.devworklib.http.DWRequest;
import com.wuhk.devworklib.http.DWResponse;

import java.net.URLConnection;
import java.util.Map;

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

    /**请求参数设置
     *
     * @param connection
     * @param dwRequest
     */
    private void initRequest(URLConnection connection , DWRequest dwRequest){
        //头部设置
        for (Map.Entry<String , String> entry : dwRequest.getHeaderMap().entrySet()){
            connection.addRequestProperty(entry.getKey() , entry.getValue());
        }

        //超时设置
        connection.setConnectTimeout(dwRequest.getConnectionTimeout());
        connection.setReadTimeout(dwRequest.getReadTimeout());
    }


}
