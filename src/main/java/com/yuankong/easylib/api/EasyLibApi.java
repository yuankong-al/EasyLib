package com.yuankong.easylib.api;

import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.action.query.QueryAction;
import com.yuankong.easylib.EasyLib;
import com.yuankong.easylib.util.EasyTool;
import com.yuankong.easylib.util.UseDate;

public class EasyLibApi {
    public static SQLManager getSQLManager(){
        return EasyLib.getSqlManager();
    }


    public static void querySQL(SQLManager sqlManager,EasyTool easyTool, UseDate useDate){
        QueryAction queryAction = sqlManager.createQuery()
                .inTable(easyTool.getTableName())
                .selectColumns(easyTool.getColumnNames())
                .addCondition((String) easyTool.getCondition()[0],easyTool.getCondition()[1])
                .build();
        queryAction.executeAsync(successQuery -> useDate.onResult(successQuery.getResultSet()),(failQuery1, failQuery2) -> useDate.onFail());
    }
}
