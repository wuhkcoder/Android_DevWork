package com.wuhk.devworklib.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.wuhk.devworklib.utils.LogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by wuhk on 2016/5/27.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance;// 单例

    /**
     * 用于初始化或升级数据库的文件名格式
     */
    private static final String DB_INIT_OR_UPGRADE_FILENAME = "db_${db.version}.sql";

    /**
     * 数据库版本号，程序初始化时必须初始化此值
     */
    private static int databaseVersion = -1;

    /**
     * 数据库名，使用时可以根据自己项目定义名称
     */
    public static String databaseName = "devwork";

    private final Context context;

    /**
     * 初始化数据库，必须操作
     *
     * @param databaseVersion    数据库版本号
     * @param databaseName       数据库名称
     * @param applicationContext Application实例
     */
    public static void init(int databaseVersion, String databaseName,
                            Context applicationContext) {
        DBHelper.databaseVersion = databaseVersion;
        DBHelper.databaseName = databaseName;
        instance = new DBHelper(applicationContext);
    }

    public DBHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
        this.context = context;
    }

    /**
     * 获取DBHelper的单例，必须先调用init
     *
     * @return
     */
    public static DBHelper getInstance() {
        return instance;
    }

    /**数据第一次被创建的时候被调用
     * 触发时机：不在构造时发生，而是在调用getWritableDatabase或getReadableDatabase时被调用时。
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        if(databaseVersion <= 0){
            throw new RuntimeException("DBHelper.DATABASE_VERSION必须初始化，请调用init方法初始化");
        }

        LogUtils.d("开始初始化数据库...");

        executeSqlFromFile(db , DB_INIT_OR_UPGRADE_FILENAME.replace("${db.version}", "1"));

        if (databaseVersion > 1){
            onUpgrade(db ,1 ,databaseVersion);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (databaseVersion <= 0){
            throw new RuntimeException("DBHelper.DATABASE_VERSION必须初始化，请调用init方法初始化");
        }

        if (newVersion <= oldVersion){
            return;
        }

        for(int i = oldVersion = 1; i <= newVersion ; i++){
            executeSqlFromFile(db,
                    DB_INIT_OR_UPGRADE_FILENAME.replace("${db.version}", String.valueOf(i)));
        }
    }

    /**从文件读取sql并执行
     *
     * @param database
     * @param fileName
     */
    private void executeSqlFromFile(SQLiteDatabase database , String fileName){
        LogUtils.d("开始执行在assets中的数据库文件" + fileName);

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    context.getAssets().open(fileName)));
            String line  = null;
            StringBuilder sql = new StringBuilder();
            while ((line = reader.readLine()) != null){
                if (line.startsWith("--")){//这是注释行
                    continue;
                }
                if (line.trim().equalsIgnoreCase("go")){//表示一句sql语句结束
                    if (!TextUtils.isEmpty(sql.toString())){
                        //执行sql
                        database.execSQL(sql.toString());
                    }
                    sql = new StringBuilder();
                    continue;
                }
                sql.append(line);
            }
            if (!TextUtils.isEmpty(sql.toString())){
                //执行sql
                database.execSQL(sql.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        LogUtils.d("成功执行在assets中的数据库文件：" + fileName);
    }

}
