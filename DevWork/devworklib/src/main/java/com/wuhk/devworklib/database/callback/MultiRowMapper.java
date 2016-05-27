package com.wuhk.devworklib.database.callback;

import android.database.Cursor;
import android.database.SQLException;

/**
 * 处理多行记录集的接口
 * Created by wuhk on 2016/5/27.
 */
public interface MultiRowMapper<T> {
    /**把结果集的一行记录映射成一个对象
     *
     * @param cursor
     * @param rowNum
     * @return
     * @throws SQLException
     */
    T mapRow(Cursor cursor , int rowNum) throws SQLException;
}
