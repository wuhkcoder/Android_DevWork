package com.wuhk.devworklib.http;

import com.wuhk.devworklib.http.callback.DWHttpDownloadListener;

import java.io.File;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求对象
 *
 * Created by wuhk on 2016/5/28.
 */
public class DWRequest {
    /**请求地址*/
    private String url;

    /**普通参数*/
    private Map<String , String> paramMap;
    /**文件参数*/
    private Map<String , File> fileParamMap;
    /**头部参数*/
    private Map<String , String> headerMap;
    /**用请求提JSON方式提交*/
    private String bodyJson;
    /**提交|获取的编码方式*/
    private String encode = "utf-8";
    /**连接超时*/
    private int connectionTimeout = 1000*30;
    /**读取超时*/
    private int readTimeout = 1000*30;

    /**结果返回回调，只有下载文件时会被调用*/
    private DWHttpDownloadListener downloadListener;
    /**下载文件存放路径*/
    private String downloadFileName;

    public DWRequest(){
        init();
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public DWHttpDownloadListener getDownloadListener() {
        return downloadListener;
    }

    public void setDownloadListener(DWHttpDownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }

    /**放入请求体Json串
     *
     * @param bodyJson
     */
    public void putBodyJson(String bodyJson){
        if (null != bodyJson){
            this.bodyJson = bodyJson;
        }
    }

    /**添加普通参数
     *
     * @param key
     * @param value
     */
    public void putParam(String key , String value){
        if (key != null && value != null){
            paramMap.put(key , value);
        }
    }

    /**添加文件参数
     *
     * @param key
     * @param file
     */
    public void putFile(String key , File file){
        fileParamMap.put(key , file);
    }

    /**添加头部
     *
     * @param key
     * @param value
     */
    public void putHeader(String key , String value){
        headerMap.put(key ,value);
    }

    /**删除普通参数
     *
     * @param key
     */
    public void removeParam(String key){
        paramMap.remove(key);
    }

    /**删除文件参数
     *
     * @param key
     */
    public void removeFile(String key){
        fileParamMap.remove(key);
    }

    /**删除头部
     *
     * @param key
     */
    public void removeHeader(String key){
        headerMap.remove(key);
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    /**
     * 返回参数的拼接
     *
     * @return
     */
    public String getParamsStr(){
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            sb.append(entry.getKey()).append("=")
                    .append(URLEncoder.encode(entry.getValue())).append("&");
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 获取GET参数串
     *
     * @return
     */
    public String getGetUrl() {
        if(!getUrl().contains("?")){
            return getUrl() + "?" + getParamsStr();
        }else{
            return getUrl() + "&" + getParamsStr();
        }
    }

    public Map<String, File> getFileParamMap() {
        return fileParamMap;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public String getBodyJson() {
        return bodyJson;
    }

    // 初始化MAP
    private void init() {
        paramMap = new ConcurrentHashMap<String, String>();
        fileParamMap = new ConcurrentHashMap<String, File>();
        headerMap = new ConcurrentHashMap<String, String>();
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (ConcurrentHashMap.Entry<String, String> entry : paramMap
                .entrySet()) {
            if (result.length() > 0) {
                result.append("&");
            }

            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }

        for (ConcurrentHashMap.Entry<String, File> entry : fileParamMap
                .entrySet()) {
            if (result.length() > 0) {
                result.append("&");
            }

            result.append(entry.getKey());
            result.append("=");
            result.append("FILE");
        }

        if(!getUrl().contains("?")){
            return getUrl() + "?" + result.toString();
        }else{
            return getUrl() + "&" + result.toString();
        }
    }
}


