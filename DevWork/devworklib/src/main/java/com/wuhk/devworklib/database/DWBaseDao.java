package com.wuhk.devworklib.database;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.wuhk.devworklib.database.callback.MultiRowMapper;
import com.wuhk.devworklib.database.callback.SingleRowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 对原生数据库操作进行了一些轻量级的封装。
 * Created by wuhk on 2016/5/27.
 */
public class DWBaseDao {
    /**
     * 子类小心使用该锁，如果要使用请确保不会死锁，不然后果不堪
     */
    protected final static ReentrantLock lock = new ReentrantLock();// 保证多线程访问数据库的安全，性能有所损失

    /**获取数据库，对应调用这个一次必须调用close关闭资源
     *
     * @return
     */
    public SQLiteDatabase openSQLiteDatabase(){
        return DBHelper.getInstance().getWritableDatabase();
    }

    /**
     * 使用完后请Close数据库连接，dbHelper的close其实内部就是sqliteDatabase的close，
     * 并且源码内部会判断null和open的状态
     */
    public void closeSQLiteDatabase() {
        DBHelper.getInstance().close();
    }

    /**使用原生的sqlite执行语法
     *
     * @param onGrammarExcute
     * @return
     */
    protected Object execute(OnGrammarExcute onGrammarExcute){
        if (null == onGrammarExcute){
            return null;
        }

        Object object = null;
        lock.lock();
        try {
            SQLiteDatabase sqLiteDatabase = openSQLiteDatabase();
            object = onGrammarExcute.onExcute(sqLiteDatabase);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeSQLiteDatabase();
            lock.unlock();
        }
        return object;
    }

    /**SQL语句执行方法
     *
     * @param sql
     */
    protected void excuteSQL(String sql){
        lock.lock();
        try {
            openSQLiteDatabase().execSQL(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSQLiteDatabase();
            lock.unlock();
        }
    }

    /**SQL语句执行方法
     *
     * @param sql
     * @param bindArgs
     */
    protected void excuteSQL(String sql , Object[] bindArgs){
        lock.lock();
        try {
            openSQLiteDatabase().execSQL(sql , bindArgs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSQLiteDatabase();
            lock.unlock();
        }
    }

    /**批量操作
     *
     * @param sql
     * @param bindArgsList
     */
    protected void excuteSQLBatch(String sql , List<Object[]> bindArgsList){
        lock.lock();
        SQLiteDatabase sqLiteDatabase = null;

        try {
            sqLiteDatabase = openSQLiteDatabase();
            sqLiteDatabase.beginTransaction();

            for (int i = 0 ; i <bindArgsList.size() ; i++){
                Object[] bindArgs = bindArgsList.get(i);
                sqLiteDatabase.execSQL(sql ,bindArgs);
            }

            sqLiteDatabase.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (null != sqLiteDatabase){
                sqLiteDatabase.endTransaction();
            }
            closeSQLiteDatabase();
            lock.unlock();
        }

    }

    /**插入或者更新
     *
     * @param sql
     */
    protected void dwUpdate(String sql){
        lock.lock();
        try {
            openSQLiteDatabase().execSQL(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSQLiteDatabase();
            lock.unlock();
        }
    }

    /**插入或者更新，带参数
     *
     * @param sql
     * @param args
     */
    protected void dwUpdate(String sql , Object[] args){
        if (null == args){
            dwUpdate(sql);
        }else{
            lock.lock();
            try {
                openSQLiteDatabase().execSQL(sql , args);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeSQLiteDatabase();
                lock.unlock();
            }
        }
    }

    /**插入或者更新，批量
     *
     * @param sql
     * @param argsList
     */
    protected void dwUpdateBatch(String sql , List<Object[]> argsList){
        if (null == argsList){
            return;
        }
        lock.lock();
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = openSQLiteDatabase();
            sqLiteDatabase.beginTransaction();
            for (Object[] args : argsList){
                sqLiteDatabase.execSQL(sql , args);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(null != sqLiteDatabase){
                sqLiteDatabase.endTransaction();
            }
            closeSQLiteDatabase();
            lock.unlock();
        }
    }

    /**查询，发挥多条记录
     *
     * @param sql
     * @param args
     * @param multiRowMapper
     * @param <T>
     * @return
     */
    protected <T> List<T> dwQuery(String sql , String[] args ,
                                  MultiRowMapper<T> multiRowMapper){
        List<T> ret = new ArrayList<T>();
        lock.lock();

        Cursor cursor = null;

        try {
            cursor = openSQLiteDatabase().rawQuery(sql ,args);
            int i = 0 ;
            while (cursor.moveToNext()){
                T t = multiRowMapper.mapRow(cursor , i);
                ret.add(t);
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeCursor(cursor);
            closeSQLiteDatabase();
            lock.unlock();
        }

        return ret;
    }

    /**查询， 返回单挑记录
     *
     * @param sql
     * @param args
     * @param singleRowMapper
     * @param <T>
     * @return
     */
    protected <T> T dwQuery(String sql , String[] args ,
                            SingleRowMapper<T> singleRowMapper){
        T ret = null;
        lock.lock();
        Cursor cursor = null;
        try {
            cursor = openSQLiteDatabase().rawQuery(sql ,args);
            if (cursor.moveToNext()){
                ret = singleRowMapper.mapRow(cursor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeCursor(cursor);
            closeSQLiteDatabase();
            lock.unlock();
        }

        return ret;
    }


    /**执行语法回调接口
     *
     */
    public interface OnGrammarExcute {
        /**
         * 执行语法的逻辑由用户自己实现
         *
         * @param sqliteDatabase
         * @return
         */
        Object onExcute(SQLiteDatabase sqliteDatabase);
    }

    // 关闭cursor
    private void closeCursor(Cursor cursor) {
        if (null != cursor && !cursor.isClosed()) {
            cursor.close();
        }
    }
}
