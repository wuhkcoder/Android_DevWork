package com.wuhk.devworklib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**调用本地的一些操作，打电话，发短信等
 * Created by wuhk on 2016/5/27.
 */
public class IntentUtils {

    /**
     * 调用短信程序发送短信
     *
     * @param context
     */
    public static void sendSms(Context context) {
        sendSmsByPhoneAndContent(context, null, null);
    }

    /**
     * 根据手机号发送短信
     *
     * @param context
     * @param phone
     */
    public static void sendSmsByPhone(Context context, String phone) {
        sendSmsByPhoneAndContent(context, phone, null);
    }

    /**
     * 根据内容调用手机通讯录
     *
     * @param context
     * @param content
     */
    public static void sendSmsByContent(Context context, String content) {
        sendSmsByPhoneAndContent(context, null, content);
    }


    /**根据手机号和内容发短信
     *
     * @param context
     * @param phone
     * @param content
     */
    public static void sendSmsByPhoneAndContent(Context context, String phone , String content){
        phone = TextUtils.isEmpty(phone) ? "" : phone;
        content = TextUtils.isEmpty(content) ? "" : content;

        Uri uri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        context.startActivity(intent);
    }

    /**根据手机号拨打电话 , 使用时需添加CALL_PHONE权限
     *
     * @param context
     * @param phone
     */
    public static void callByPhone(Context context , String phone){
        if (VerifyUtils.isEmpty(phone)){
            return;
        }

        Intent intent = new Intent(Intent.ACTION_CALL , Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }
}
