package com.wuhk.devworklib.utils.Preferences;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.EditText;

import com.wuhk.devworklib.DevWork;

/**
 * SharedPreference存储的工具类
 * Created by wuhk on 2016/5/27.
 */
public class DWPreferences {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static DWPreferences instance;

    private DWPreferences(){
    }

    /**获取单例
     *
     * @return
     */
    public static DWPreferences getInstance(){
        if (null == instance) {
            instance = new DWPreferences();
            instance.preferences = PreferenceManager
                    .getDefaultSharedPreferences(DevWork.getApplication());
        }
        return instance;
    }

    /** 以键值对的方式保存参数
     *
     * @param key
     * @param value
     * @param types
     */
    public void saveParams(String key , Object value, Types types){
        if (editor == null){
            initPrefsEdit();
        }

        switch (types){
            case BOOLEAN:
                editor.putBoolean(key , (Boolean)value);
                break;
            case FLOAT:
                editor.putFloat(key , (Float)value);
                break;
            case INTEGER:
                editor.putInt(key , (Integer)value);
                break;
            case LONG:
                editor.putLong(key , (Long)value);
                break;
            case STRING:
                editor.putString(key , (String)value);
                break;
        }

        commitPrefsEdit();
    }

    /**根据key获取参数
     *
     * @param key
     * @param defValue
     * @param type
     * @return
     */
    public Object getParams(String key , Object defValue , Types type){
        Object curValue = null;
        switch (type){
            case BOOLEAN:
                curValue = preferences.getBoolean(key , (Boolean)defValue);
                break;
            case FLOAT:
                curValue = preferences.getFloat(key , (Float)defValue);
                break;
            case INTEGER:
                curValue = preferences.getInt(key , (Integer)defValue);
                break;
            case LONG:
                curValue = preferences.getLong(key , (Long)defValue);
                break;
            case STRING:
                curValue = preferences.getString(key , (String)defValue);
                break;
        }
        return curValue;
    }
    /**
     * 获取原始的SharedPreferences对象，当封装的方法不够用时，可以用这个对象来操作更丰富的API
     *
     * @return
     */
    public SharedPreferences getDefaultSharedPreferences() {
        return preferences;
    }

    // 为减少创建开销，只有当需要保存参数的时候才初始化prefsEdit
    private synchronized void initPrefsEdit() {
        if (null == editor) {
            editor = preferences.edit();
        }
    }

    // 保存编辑提交
    private void commitPrefsEdit() {
        if (null != editor) {
            editor.commit();
        }
    }

    // ////////////////////////////////////保存参数方法部分////////////////////////////////////////////////////////////
    /**
     * 以键值对的方式保存参数，value是boolean类型
     *
     * @param key
     * @param value
     */
    public void putBoolean(String key, boolean value) {
        saveParams(key, value, Types.BOOLEAN);
    }

    /**
     * 以键值对的方式保存参数，value是float类型
     *
     * @param key
     * @param value
     */
    public void putFloat(String key, float value) {
        saveParams(key, value, Types.FLOAT);
    }

    /**
     * 以键值对的方式保存参数，value是int类型
     *
     * @param key
     * @param value
     */
    public void putInt(String key, int value) {
        saveParams(key, value, Types.INTEGER);
    }

    /**
     * 以键值对的方式保存参数，value是long类型
     *
     * @param key
     * @param value
     */
    public void putLong(String key, long value) {
        saveParams(key, value, Types.LONG);
    }

    /**
     * 以键值对的方式保存参数，value是String类型
     *
     * @param key
     * @param value
     */
    public void putString(String key, String value) {
        saveParams(key, value, Types.STRING);
    }


    // ////////////////////////////////////获取参数部分方法/////////////////////////////////////////////////////
    /**
     * 获取key对应的value值，值是boolean类型
     *
     * @param key
     * @param defValue
     *            当key对应的值不存在时，返回这个默认值
     * @return
     */
    public boolean getBoolean(String key, boolean defValue) {
        return (Boolean) getParams(key, defValue, Types.BOOLEAN);
    }

    /**
     * 获取key对应的value值，值是float类型
     *
     * @param key
     * @param defValue
     *            当key对应的值不存在时，返回这个默认值
     * @return
     */
    public float getFloat(String key, float defValue) {
        return (Float) getParams(key, defValue, Types.FLOAT);
    }

    /**
     * 获取key对应的value值，值是int类型
     *
     * @param key
     * @param defValue
     *            当key对应的值不存在时，返回这个默认值
     * @return
     */
    public int getInt(String key, int defValue) {
        return (Integer) getParams(key, defValue, Types.INTEGER);
    }

    /**
     * 获取key对应的value值，值是long类型
     *
     * @param key
     * @param defValue
     *            当key对应的值不存在时，返回这个默认值
     * @return
     */
    public long getLong(String key, long defValue) {
        return (Long) getParams(key, defValue, Types.LONG);
    }

    /**
     * 获取key对应的value值，值是String类型
     *
     * @param key
     * @param defValue
     *            当key对应的值不存在时，返回这个默认值
     * @return
     */
    public String getString(String key, String defValue) {
        return (String) getParams(key, defValue, Types.STRING);
    }
}
