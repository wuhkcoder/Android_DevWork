package com.wuhk.devworklib.http.urlconnect;

import com.wuhk.devworklib.http.DWHttpClient;
import com.wuhk.devworklib.http.DWRequest;
import com.wuhk.devworklib.http.DWResponse;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * 用UrlConnect方式实现
 *
 * Created by wuhk on 2016/5/29.
 */
public class DWHttpUrlConnectionClient implements DWHttpClient {

    @Override
    public DWResponse postJson(DWRequest dwRequest) {
        try {
            URL url = new URL(dwRequest.getUrl());

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");//POST模式
            connection.setDoOutput(true);//发送POST请求必须设置如下

            initRequest(connection , dwRequest);

            //设置POST参数
            putParamsToOutputStreamForJson(connection , dwRequest);

            return readResponseForString(connection , dwRequest);
        } catch (Exception e) {
            e.printStackTrace();
            DWResponse response = new DWResponse();
            response.setStatusCode(-1);
            response.setReasonPhrase(e.getMessage());
            return response;
        }
    }

    @Override
    public DWResponse post(DWRequest dwRequest) {
        try {
            URL url = new URL(dwRequest.getUrl());

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");//POST模式
            connection.setDoOutput(true);//发送POST请求必须设置如下
            connection.setUseCaches(false);

            initRequest(connection , dwRequest);
            //设置POST参数
            putParamsToOutputStream(connection , dwRequest);

            return readResponseForString(connection , dwRequest);
        } catch (Exception e) {
            e.printStackTrace();
            DWResponse response = new DWResponse();
            response.setStatusCode(-1);
            response.setReasonPhrase(e.getMessage());
            return response;
        }

    }

    @Override
    public DWResponse get(DWRequest dwRequest) {
        try {
            URL url = new URL(dwRequest.getGetUrl());
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            initRequest(connection , dwRequest);

            return readResponseForString(connection , dwRequest);
        } catch (IOException e) {
            e.printStackTrace();
            DWResponse response = new DWResponse();
            response.setStatusCode(-1);
            response.setReasonPhrase(e.getMessage());
            return response;
        }
    }

    @Override
    public DWResponse getDownload(DWRequest dwRequest) {
        try {
            URL url = new URL(dwRequest.getGetUrl());
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            initRequest(connection , dwRequest);

            return readResponseFoeFile(connection ,dwRequest);
        } catch (IOException e) {
            e.printStackTrace();
            DWResponse response = new DWResponse();
            response.setStatusCode(-1);
            response.setReasonPhrase(e.getMessage());
            return response;
        }
    }

    @Override
    public DWResponse postDownload(DWRequest dwRequest) {
        try {
            URL url = new URL(dwRequest.getUrl());
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            initRequest(connection , dwRequest);
            putParamsToOutputStream(connection , dwRequest);

            return readResponseFoeFile(connection , dwRequest);
        } catch (IOException e) {
            e.printStackTrace();
            DWResponse response = new DWResponse();
            response.setStatusCode(-1);
            response.setReasonPhrase(e.getMessage());
            return response;
        }
    }

    @Override
    public DWResponse upload(DWRequest dwRequest) {
        try {
            URL url = new URL(dwRequest.getUrl());
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            initRequest(connection , dwRequest);
            purBodyEntityToOutputStream(connection , dwRequest);

            return readResponseForString(connection , dwRequest);
        } catch (IOException e) {
            e.printStackTrace();
            DWResponse response = new DWResponse();
            response.setStatusCode(-1);
            response.setReasonPhrase(e.getMessage());
            return response;
        }
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

    /**把请求提放到输出流中
     *
     * @param connection
     * @param dwRequest
     */
    private static void purBodyEntityToOutputStream(HttpURLConnection connection , DWRequest dwRequest){
        try {
            DWUrlMultiPartEntity bodyEntity = new DWUrlMultiPartEntity();
            bodyEntity.writeDataToBody(connection , dwRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**已Json的格式放入请求体
     *
     * @param connection
     * @param dwRequest
     */
    private static void putParamsToOutputStreamForJson(HttpURLConnection connection , DWRequest dwRequest){
        String bodyJson = dwRequest.getBodyJson();
        DataOutputStream dos = null;

        try {
            dos = new DataOutputStream(connection.getOutputStream());
            dos.writeBytes(bodyJson);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dos.flush();
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**把参数当做普通表单放到OutputStream中
     *
     * @param connection
     * @param dwRequest
     */
    private static void putParamsToOutputStream(HttpURLConnection connection , DWRequest dwRequest){
        String paramStr = dwRequest.getParamsStr();
        DataOutputStream dos = null;

        try {
            dos = new DataOutputStream(connection.getOutputStream());
            dos.writeBytes(paramStr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dos.flush();
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /**读取结果最为字符串
     *
     * @param connection
     * @param dwRequest
     * @return
     */
    private static DWResponse readResponseForString(HttpURLConnection connection , DWRequest dwRequest){
        DWResponse response = new DWResponse();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        InputStream inputStream = null;

        try {
            inputStream = connection.getInputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = inputStream.read(buffer)) != -1){
                outputStream.write(buffer , 0 , len);
            }
            byte[] data = outputStream.toByteArray();

            response.setStatusCode(connection.getResponseCode());
            response.setReasonPhrase(connection.getResponseMessage());
            response.setResultStr(new String(data , dwRequest.getEncode()));
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatusCode(-1);
            response.setReasonPhrase(e.getMessage());
        } finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return response;
    }


    /**读取结果作为字符串
     *
     * @param connection
     * @param dwRequest
     * @return
     */
    private static DWResponse readResponseFoeFile(HttpURLConnection connection , DWRequest dwRequest){
        boolean hasListener = false;
        if (null != dwRequest.getDownloadListener()){
            hasListener = true;
        }

        DWResponse response = new DWResponse();
        ByteArrayOutputStream outputStream  = new ByteArrayOutputStream();

        InputStream inputStream = null;

        try {
            inputStream = connection.getInputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            int curCount = 0;//当前字节量
            int total = -1;//总字节量
            if (hasListener){
                total = connection.getContentLength();
            }
            while ((len = inputStream.read(buffer)) != -1){
                curCount += len;
                if (hasListener){
                    //加载中回调
                    dwRequest.getDownloadListener().callback(total ,curCount , false);
                }
                outputStream.write(buffer , 0 , len);
            }
            if (hasListener){
                //加载中回调
                dwRequest.getDownloadListener().callback(total , curCount , true);
            }

            response.setStatusCode(connection.getResponseCode());
            response.setReasonPhrase(connection.getResponseMessage());

            byte[] data = outputStream.toByteArray();

            File file = new File(dwRequest.getDownloadFileName());
            if (file.exists()) {
                if (file.isDirectory()) {
                    throw new IOException("File '" + file
                            + "' exists but is a directory");
                }
                if (file.canWrite() == false) {
                    throw new IOException("File '" + file
                            + "' cannot be written to");
                }
            } else {
                File parent = file.getParentFile();
                if (parent != null) {
                    if (!parent.mkdirs() && !parent.isDirectory()) {
                        throw new IOException("Directory '" + parent
                                + "' could not be created");
                    }
                }
            }
            OutputStream out = null;
            try {
                out = new FileOutputStream(file , false);
                out.write(data);
            } finally {
                try {
                    if (null != out){
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            response.setResultFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatusCode(-1);
            response.setReasonPhrase(e.getMessage());
        } finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return response;
    }
}
