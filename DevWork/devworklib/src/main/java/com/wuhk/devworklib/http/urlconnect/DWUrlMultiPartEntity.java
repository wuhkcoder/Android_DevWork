package com.wuhk.devworklib.http.urlconnect;

import com.wuhk.devworklib.http.DWRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.Map;
import java.util.Random;

/**
 * 文件上传请求实体
 *
 * Created by wuhk on 2016/5/30.
 */
public class DWUrlMultiPartEntity {
    /**
     * Standard CLRF line ending
     */
    private static final char[] CLRF = new char[] { '\r', '\n' };

    /**
     * Double CLRF line ending
     */
    private static final char[] DOUBLE_CLRF = new char[] { '\r', '\n', '\r', '\n' };

    /**
     * Boundary start token
     */
    private static final String BOUNDARY_START = "---------------------------HttpAPIFormBoundary";

    public void writeDataToBody(HttpURLConnection connection , DWRequest request) throws Exception{
        String boundary = BOUNDARY_START + new Random().nextLong();

        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setRequestMethod("POST");// POST模式
        connection.setUseCaches(false);

        boundary = "--" + boundary;

        OutputStream os = connection.getOutputStream();
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(os);
            for (Map.Entry<String , String> entry : request.getParamMap().entrySet()){
                writer.write(boundary);
                writer.write(CLRF);
                writer.write("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"");
                writer.write(DOUBLE_CLRF);
                writer.write(entry.getValue());
                writer.write(CLRF);
            }

            for (Map.Entry<String , File> entry : request.getFileParamMap().entrySet()){
                writer.write(boundary);
                writer.write(CLRF);
                writer.write("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"");
                writer.write("; filename=\"" + entry.getValue().getName() + "\"");
                writer.write(CLRF);

                String type = URLConnection.guessContentTypeFromName(entry.getValue().getName());
                if (type == null){
                    type = "application/octet-stream";
                }

                writer.write("Content-Type: ");
                writer.write(type);
                writer.write(DOUBLE_CLRF);

                writer.flush();

                InputStream input = null;

                try {
                    input = new FileInputStream(entry.getValue());
                    byte[] buffer =new byte[1024];
                    while (true){
                        int read = input.read(buffer , 0 , buffer.length);
                        if (read == -1){
                            break;
                        }
                        os.write(buffer , 0 , read);
                    }
                    os.flush();
                } catch (IOException e) {
                    throw new Exception(e);
                } finally {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                writer.write(CLRF);
            }
            // Set the final boundary
            boundary = boundary + "--";
            // Write a boundary to let the server know the previous content area is finished
            writer.write(boundary);
            // Write a final newline
            writer.write(CLRF);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
