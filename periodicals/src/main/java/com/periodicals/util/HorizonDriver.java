 package com.periodicals.util;

/**
 * 连接Sybase数据库
 * horizon测试库f50
 * 用户名：sa
 * 密码：sybassys
 * ip:172.16.103.12,4100
 * 数据库名称：shcl12_f50
 *
 *
 * horizon正式库m80
 * 用户名：yang
 * 密码：yangpac
 * ip:172.16.103.8,2048
 * 数据库名称：shcl
 *
 */

import java.sql.*;

public class HorizonDriver {

  private Connection conActive;
  private Statement stmtActive;
  public ResultSet rsQuery;
  public boolean bHasResult;

  private String DB_SYBASE_DRIVER = "com.sybase.jdbc4.jdbc.SybDataSource";
  private String SYBASE_CONNECTION_URL = "jdbc:sybase:Tds:";

  ///////////////////测试库////////////////////////////////////////////
  /*String dbname="shcl12_f50";
  String sURL = SYBASE_CONNECTION_URL + "172.16.103.12:4100/" + dbname;
  String username="sa";
  String password="sybassys";*/

  ////////////////////////正式库//////////////////////////////////////////
//  String dbname = "shcl";
//  String sURL = SYBASE_CONNECTION_URL + "172.16.103.8:2048/" + dbname;
//  String username = "yang";
//  String password = "yangpac";
  //测试库
  String dbname = "shclsym_p570b";
  String sURL = SYBASE_CONNECTION_URL + "172.16.103.6:4100/" + dbname;
  String username = "folio_datasync";
  String password = "Wf0lk5Vd1";

  public HorizonDriver() {
    try {
      Class.forName(DB_SYBASE_DRIVER);
      conActive = DriverManager.getConnection(sURL+"?charset=utf8", username, password);
      bHasResult = false;
    }
    catch (ClassNotFoundException E) {
      return;
    }
    catch (SQLException E) {
      System.out.println("Horizon database connect error");
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
      System.out.println("error sql：" + sSQL);
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
    //sValue = sValue.trim();

    //sValue=CP850ToGB2312(sValue);
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
      sValue =rsQuery.getString(iIndex);
    }
    catch (Exception E) {
      sValue = "";
    }

    //如果该字段值为空，返回空字符串
    if (sValue == null) {
      sValue = "";
    }
    //sValue = sValue.trim();
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

  public boolean IsConnectError() {
    //如果结果集为空，返回空字符串
    if (rsQuery == null) {
      return true;
    }else{
      return false;
    }
  }
}

