package com.wuhk.devworklib.database.callback;

import android.database.Cursor;
import android.database.SQLException;

/**
 * 处理单行记录集的接口
 * Created by wuhk on 2016/5/27.
 */
public interface SingleRowMapper<T> {
    /**把结果集的这行记录映射成一个对象
     *
     * @param cursor
     * @return
     * @throws SQLException
     */
    T mapRow(Cursor cursor) throws SQLException;

}
