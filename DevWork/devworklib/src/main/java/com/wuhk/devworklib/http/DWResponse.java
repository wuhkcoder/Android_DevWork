package com.wuhk.devworklib.http;

import java.io.File;

/**
 * 请求响应
 *
 * Created by wuhk on 2016/5/28.
 */
public class DWResponse {
    /**返回状态码，成功200*/
    private int statusCode;
    /**返回状态短语*/
    private String reasonPhrase;
    /**返回结果，当返回是字符串时才有*/
    private String resultStr;
    /**返回结果，当返回是文件时才有*/
    private File resultFile;

    public DWResponse(){
        this(-1 , null);
    }

    public DWResponse(int statusCode){
        this(statusCode , null);
    }

    public DWResponse(int statusCode, String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    /**判断网络返回是否正常，正常码=200
     *
     * @return
     */
    public boolean isStatusOk(){
        return 200 == statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public String getResultStr() {
        return resultStr;
    }

    public void setResultStr(String resultStr) {
        this.resultStr = resultStr;
    }

    public File getResultFile() {
        return resultFile;
    }

    public void setResultFile(File resultFile) {
        this.resultFile = resultFile;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("statusCode[" + statusCode + "]");
        sb.append("reasonPhrase[" + reasonPhrase + "]");
        sb.append("resultStr[" + resultStr + "]");
        return sb.toString();
    }
}
