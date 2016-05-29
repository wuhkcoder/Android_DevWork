package com.wuhk.devworklib.http;

/**
 * HTTP访问接口
 * Created by wuhk on 2016/5/29.
 */
public interface DWHttpClient {

    /**POST请求，Json放到请求体里面
     *
     * @param dwRequest
     * @return
     */
    DWResponse postJson(DWRequest dwRequest);

    /**POST请求，普通参数方式提交
     *
     * @param dwRequest
     * @return
     */
    DWResponse post(DWRequest dwRequest);

    /**GET请求
     *
     * @param dwRequest
     * @return
     */
    DWResponse get(DWRequest dwRequest);

    /**下载 ， 用的是GET请求
     *
     * @param dwRequest
     * @return
     */
    DWResponse getDownload(DWRequest dwRequest);

    /**下载，用的是POST请求
     *
     * @param dwRequest
     * @return
     */
    DWResponse postDownload(DWRequest dwRequest);

    /**模拟表单上传文件
     *
     * @param dwRequest
     * @return
     */
    DWResponse upload(DWRequest dwRequest);


}
