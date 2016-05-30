package com.wuhk.devworklib.http;

import android.util.Log;

import com.wuhk.devworklib.http.callback.DWHttpDownloadListener;

import java.io.File;
import java.util.Map;

/**
 * Http工具类
 *
 * Created by wuhk on 2016/5/30.
 */
public class DwHttpUtils {
    private static final String TAG = "DevWork.DWHttpUtils";
    public static final boolean DEBUG = false;

    /**POST请求，JSON提交
     *
     * @param request
     * @return
     */
    public static DWResponse postJson(DWRequest request){
        DWHttpClient client = DWHttpFactory.getURLConnectionHttpClien();
        printLog(request);
        return client.postJson(request);
    }

    /**POST请求，JSON提交
     *
     * @param url
     * @param bodyJson
     * @return
     */
    public static DWResponse postJson(String url , String bodyJson){
        DWRequest request = new DWRequest();
        request.setUrl(url);
        request.putBodyJson(bodyJson);
        return postJson(request);
    }

    /**POST请求，普通参数提交
     *
     * @param request
     * @return
     */
    public static DWResponse post(DWRequest request){
        DWHttpClient client = DWHttpFactory.getURLConnectionHttpClien();
        printLog(request);
        return client.post(request);
    }

    /**POST请求，普通参数提交
     *
     * @param url
     * @param paramMap
     * @return
     */
    public static DWResponse post(String url , Map<String , String> paramMap){
        DWRequest request = new DWRequest();
        request.setUrl(url);

        if (null != paramMap){
            for(Map.Entry<String, String> entry : paramMap.entrySet()){
                request.putParam(entry.getKey() , entry.getValue());
            }
        }

        return post(request);
    }

    /**GET请求
     *
     * @param request
     * @return
     */
    public static DWResponse get(DWRequest request){
        DWHttpClient client = DWHttpFactory.getURLConnectionHttpClien();
        printLog(request);
        return client.get(request);
    }

    /**GET请求
     *
     * @param url
     * @param paramsMap
     * @return
     */
    public static DWResponse get(String url , Map<String , String> paramsMap){
        DWRequest request = new DWRequest();
        request.setUrl(url);

        if (null != paramsMap){
            for (Map.Entry<String , String> entry : paramsMap.entrySet()){
                request.putParam(entry.getKey() , entry.getValue());
            }
        }

        return get(request);
    }

    /**POST方式下载
     *
     * @param request
     * @return
     */
    public static DWResponse postDownload(DWRequest request){
        DWHttpClient client = DWHttpFactory.getURLConnectionHttpClien();
        printLog(request);

        return client.postDownload(request);
    }

    /**POST方式下载
     *
     * @param url
     * @param paramsMap
     * @param saveFileName
     * @param downloadListener
     * @return
     * @throws Exception
     */
    public static DWResponse postDownload(String url , Map<String , String> paramsMap ,
                                          String saveFileName , DWHttpDownloadListener downloadListener)throws Exception{
        DWRequest request = new DWRequest();
        request.setUrl(url);
        request.setDownloadFileName(saveFileName);
        request.setDownloadListener(downloadListener);
        if (null != paramsMap){
            for (Map.Entry<String , String> entry : paramsMap.entrySet()){
                request.putParam(entry.getKey() , entry.getValue());
            }
        }

        printLog(request);
        return postDownload(request);
    }

    /**GET请求下载
     *
     * @param request
     * @return
     */
    public static DWResponse getDownload(DWRequest request){
        DWHttpClient client = DWHttpFactory.getURLConnectionHttpClien();
        printLog(request);
        return client.getDownload(request);
    }

    /**GET请求下载
     *
     * @param url
     * @param paramsMap
     * @param saveFileName
     * @param downloadListener
     * @return
     * @throws Exception
     */
    public static DWResponse getDownload(String url , Map<String , String> paramsMap ,
                                         String saveFileName , DWHttpDownloadListener downloadListener)throws Exception{
        DWRequest request = new DWRequest();
        request.setUrl(url);
        request.setDownloadFileName(saveFileName);
        request.setDownloadListener(downloadListener);

        if (null != paramsMap){
            for(Map.Entry<String , String> entry : paramsMap.entrySet()){
                request.putParam(entry.getKey(), entry.getValue());
            }
        }

        return getDownload(request);
    }

    /**上传文件
     *
     * @param request
     * @return
     */
    public static DWResponse upload(DWRequest request){
        DWHttpClient client = DWHttpFactory.getURLConnectionHttpClien();
        printLog(request);
        return client.upload(request);
    }

    /**上传文件
     *
     * @param url
     * @param fileMap
     * @param paramMap
     * @return
     */
    public static DWResponse uoload(String url , Map<String , File> fileMap ,
                                    Map<String ,String> paramMap){
        DWRequest request = new DWRequest();
        request.setUrl(url);
        if (null != fileMap){
            for(Map.Entry<String , File> entry : fileMap.entrySet()){
                request.putFile(entry.getKey() , entry.getValue());
            }
        }

        if (null != paramMap){
            for (Map.Entry<String , String> entry : paramMap.entrySet()){
                request.putParam(entry.getKey() , entry.getValue());
            }
        }

        return upload(request);
    }
    //打印请求参数
    private static void printLog(DWRequest request){
        if(DEBUG){
            Log.d(TAG, request.toString());
        }
    }
}
