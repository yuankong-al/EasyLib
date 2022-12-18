package com.yuankong.easylib.util;

@Deprecated
public class EasyTool {
    private final String tableName;
    private String[] columnNames;
    private Object[] condition;
    public EasyTool(String tableName){
        this.tableName = tableName;
    }

    public EasyTool setColumnNames(String... columnNames) {
        this.columnNames = columnNames;
        return this;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public EasyTool setCondition(String column,Object value){
        this.condition = new Object[]{column, value};
        return this;
    }

    public Object[] getCondition() {
        return condition;
    }

    public String getTableName() {
        return tableName;
    }
}
