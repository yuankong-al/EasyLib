package com.yuankong.easylib.util.datebase;

import java.sql.ResultSet;

public interface UseDate {
    void onResult(ResultSet resultSet);
    void onFail();
}
