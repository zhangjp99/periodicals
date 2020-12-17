package com.periodicals.util;

import java.sql.*;

//sql2000
//修改jdbc直接连接sql
public class SQLDriver {
  private Connection conActive;
  private Statement stmtActive;
  public ResultSet rsQuery;
  public boolean bHasResult;
  private String url = "";
  private String classforname = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
  
  private String uid = "";
  private String pwd = "";

  public SQLDriver() { 
    try {
    	
      
//      url = "jdbc:microsoft:sqlserver://gj-obj-server:1433;DatabaseName=IC_CARD_ZZFW";
      url = "jdbc:sqlserver://10.1.30.178 :1433;DatabaseName=FolioDataSync";
      
      uid = "sa";
      pwd = "sysnet3516";
      Class.forName(classforname);
      conActive = DriverManager.getConnection(url,uid,pwd);
      bHasResult = false;
      
      /*
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      conActive = DriverManager.getConnection("jdbc:odbc:zzbz", "zzbz", "sysnetzzbz");
      bHasResult = false;

      */
    }
    catch (ClassNotFoundException E) {
    	E.printStackTrace();
      return;
    }
    catch (SQLException E) {
    	E.printStackTrace();
      return;
    }
  }
  

  public void Disconnect() {
    //关闭数据库连接
    try {
      conActive.close();
    }
    catch (SQLException E) {
      return;
    }
  }
  
  public void commit() {
	    //提交批处理
	    try {
	      conActive.commit();
	    }
	    catch (SQLException E) {
	      return;
	    }
	  }
  
  public void OpenStatement() {
    //打开一个 Statement
    try {
      stmtActive = conActive.createStatement();
      bHasResult = false;
    }
    catch (SQLException E) {
      bHasResult = false;
    }
  }

  public void CloseStatement() {
    //关闭一个 Statement
    try {
      stmtActive.close();
      bHasResult = false;
    }
    catch (SQLException E) {
      bHasResult = false;
    }
  }
  
  public void addBatch(String sql) {
	    //添加批处理sql
	    try {
	      stmtActive.addBatch(sql);
	      bHasResult = false;
	    }
	    catch (SQLException E) {
	      bHasResult = false;
	    }
	  }
  public void executeBatch() {
	    //执行批处理sql
	    try {
	      stmtActive.executeBatch();
	      bHasResult = false;
	    }
	    catch (SQLException E) {
	      bHasResult = false;
	      System.out.println(E.getMessage());
	    }
	  }
  public void clearBatch() {
	    //清除批处理sql
	    try {
	      stmtActive.clearBatch();
	      bHasResult = false;
	    }
	    catch (SQLException E) {
	      bHasResult = false;
	    }
	  }

  public void ExecuteSQL(String sSQL, int iInfo) {
    try {

      //执行一句SQL语句，返回结果集
      if (iInfo == 0) {
        rsQuery = stmtActive.executeQuery(sSQL);
        if (rsQuery.next()) {
          bHasResult = true;
        }
        else {
          bHasResult = false;
        }
      }
      //执行一句SQL语句，不返回结果集
      if (iInfo == 1) {
        stmtActive.executeUpdate(sSQL);
        bHasResult = false;
      }
    }
    catch (SQLException E) {
      System.out.println(E.getMessage());
      System.out.println("出错语句为：" + sSQL);
      bHasResult = false;
    }
  }

  public String FieldByName(String sFieldName) {
    //如果结果集为空，返回空字符串
    if (rsQuery == null) {
      return "";
    }

    //得到指定字段的值
    String sValue;
    try {
      sValue = rsQuery.getString(sFieldName);
    }
    catch (SQLException E) {
      sValue = "";
    }

    //如果该字段值为空，返回空字符串
    if (sValue == null) {
      sValue = "";
    }
    sValue = sValue.trim();
    return sValue;
  }

  public String FieldByIndex(int iIndex) {
    //如果结果集为空，返回空字符串
    if (rsQuery == null) {
      return "";
    }

    //得到指定字段的值
    String sValue;
    try {
      sValue = rsQuery.getString(iIndex);
    }
    catch (SQLException E) {
      sValue = "";
    }

    //如果该字段值为空，返回空字符串
    if (sValue == null) {
      sValue = "";
    }
    sValue = sValue.trim();
    return sValue;
  }

  public int IntegerByName(String sFieldName) {
    //得到指定字段的值的整形值，如果字段值为空，返回零
    String sValue = FieldByName(sFieldName);
    if (sValue.equals("")) {
      return 0;
    }
    else {
      return Integer.parseInt(sValue);
    }
  }

  public int IntegerByIndex(int iIndex) {
    //得到指定字段的值的整形值，如果字段值为空，返回零
    String sValue = FieldByIndex(iIndex);
    if (sValue.equals("")) {
      return 0;
    }
    else {
      return Integer.parseInt(sValue);
    }
  }

  public Timestamp DateTimeByName(String sFieldName) {
    //如果结果集为空，返回空字符串
    if (rsQuery == null) {
      return null;
    }

    //得到指定字段的时间类型的值
    Timestamp tsValue;
    try {
      tsValue = rsQuery.getTimestamp(sFieldName);
    }
    catch (SQLException E) {
      tsValue = null;
    }
    return tsValue;
  }

  public Timestamp DateTimeByIndex(int iIndex) {
    //如果结果集为空，返回空字符串
    if (rsQuery == null) {
      return null;
    }

    //得到指定字段的时间类型的值
    Timestamp tsValue;
    try {
      tsValue = rsQuery.getTimestamp(iIndex);
    }
    catch (SQLException E) {
      tsValue = null;
    }
    return tsValue;
  }

  public boolean Next() {
    //结果集后移一条记录
    try {
      bHasResult = rsQuery.next();
      return bHasResult;
    }
    catch (SQLException E) {
      return false;
    }
  }
}