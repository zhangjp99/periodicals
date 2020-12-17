package com.periodicals.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class baokai_new_ch
{
  public baokai_new_ch()
  {
  }

  utility ut = new utility();
  String[] zimu = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
  HorizonDriver hManager = new HorizonDriver();
  private static Logger log = Logger.getLogger(baokai_new_ch.class.getClass());
 
  public static void main(String[] agrs)
  {
    baokai_new_ch bk = new baokai_new_ch();
    rwfile rwf = new rwfile();
    String fileloc="d:\\result_zw_2020bib";
    String[] bibinfo = rwf.ReadFile(fileloc+".txt");
    String filename=fileloc+"_info.txt";
    long startTime = System.currentTimeMillis();
    log.info("ch-开始时间："+startTime);
    
    for (int k = 0; k < bibinfo.length; k++)
    {
      int bib = Integer.parseInt(bibinfo[k]);

      String title = bk.GetTitle(bib);
      String ISSN = bk.GetBibInfo(bib, "011", "$a");
      String DGH = bk.GetBibInfo(bib, "092", "$b");
      String FLH = bk.GetBibInfo(bib, "690", "$a");
      String SQH = bk.GetBibInfo(bib, "905", "$f");
//      System.out.println(bib + "\t" + title + "\t" + ISSN + "\t" + DGH + "\t" + FLH + "\t" + SQH);
 //   System.out.println(bib);
    
//  int begindate = 17897;  //20190101
//  int enddate = 18261;  //20191231
    
//    int begindate = 17532;  //20180101
//    int enddate = 17896;  //20181231

//  int enddate = 17651;  //20180430

    int begindate = 18262;  //20200101
    int enddate = 18568;  //20200601
  

//    int begindate = 17167;  //20170101
//    int enddate = 17531;  //20171231
//    int enddate = 17347;  //20170630

//      int begindate = 16436;  //20150101
//      int enddate = 16800;  //20151231




    String fbinfo = "";
      Map<String, Object>map = bk.getSubscribe(bib);
      if(map==null) {
    	  map =  new HashMap<String, Object>();
      }
      String fbrec = bk.GetFBrec2(bib,map.get("copyId")!=null ? Integer.parseInt(map.get("copyId").toString()) : null,title);
//      for (int i = 0; i < fbrec.length; i++) {
//    	  fbinfo += fbrec[i];
//      }
      String prescki=bk.GetPreScki(bib);
      
//      System.out.println(bib + "\t" + title + "\t" + ISSN + "\t" + DGH + "\t" + FLH + "\t" + SQH + "\t" + fbinfo);
      try
      {
        //rwf.AddtoFile(bib + "\t" + title + "\t" + ISSN + "\t" + DGH + "\t" + FLH + "\t" + SQH+ "\t" +prescki + "\t" + fbinfo, filename);
        String sql = "insert into periodicals_log values("+bib+",'"+checkString(title)+"','"+checkString(ISSN)+"','"+checkString(DGH)+"','"+checkString(FLH)+"','"
        + checkString(SQH)+"','"+checkString(prescki)+"','"+checkString(fbinfo)+"', getdate(),'zw','"
        + checkString(map.get("location")+"")+"','"+checkString(map.get("collection")+"")+"','"+checkString(map.get("call")+"")+"','"+checkString(map.get("copyNumber")+"")+"','"
        +checkString(map.get("mediaType")+"")+"','"+checkString(map.get("acqStatus")+"")+"','"+checkString(map.get("pacNote")+"")+"','"+checkString(fbrec)+"')";
        SQLDriver rsqManage = new SQLDriver();
        rsqManage.OpenStatement();
        rsqManage.ExecuteSQL(sql,1);
        rsqManage.CloseStatement();
        rsqManage.Disconnect();
      //可以设置不同的大小；如50，100，500，1000等等
//        if(k%1000==0){
//        	rsqManage.executeBatch();
//        	rsqManage.commit();
//        	rsqManage.clearBatch();
//        	log.info("ch-时间："+System.currentTimeMillis());
//        }
//        if(k == bibinfo.length-1) {
//        	rsqManage.executeBatch();
//        	rsqManage.commit();
//        	rsqManage.clearBatch();
//        	log.info("ch-时间："+System.currentTimeMillis());
//        }
      }
      catch(Exception E)
      {
    	  E.printStackTrace();
        System.out.println(E.getMessage());
      }
    }
    log.info("ch-结束时间："+(System.currentTimeMillis()-startTime));
  }

  
  /**
   * 检查字符串是否有单引号
   * @param str
   * @return
   */
  public static String checkString(String str)
  {
	  String returnStr = "";
      if (str !=null && !str.isEmpty() && str.indexOf("'") != -1) //判断字符串是否含有单引号
      {
          returnStr = str.replace("'", "''");
      }else {
    	  returnStr = str;
      }
      return returnStr;
  }
  
  public String GetPreScki(int bib)
  {
	  hManager.OpenStatement();
	  hManager.ExecuteSQL("select pre_scki_note from pre_scki_note "
	  		+ "where serial# in ( select serial# from copy where bib# ="
			+ bib+")", 0);
	  String result = hManager.FieldByIndex(1);
	  hManager.CloseStatement();
   
	  return ReplacelineStr(result);
  }
  
  public String GetTitle(int bib)
  {
	  hManager.OpenStatement();
	  hManager.ExecuteSQL("select reconst from title where bib# = " + bib, 0);
	  String result = hManager.FieldByIndex(1);
	  hManager.CloseStatement();
   
	  return ReplaceTenStr(ReplaceHorizonStr(result));
  }

  public String GetBibInfo(int bib, String tag, String tag_ord)
  {
    String temp = "";
    String result = "";
    String bibinfo = "";
    int i = 0;

    hManager.OpenStatement();
    hManager.ExecuteSQL("select text from bib where bib# = " + bib + " and tag = '" + tag + "'", 0);
    while(hManager.bHasResult)
    {
      temp = ReplaceEStr(ReplaceHorizonStr(hManager.FieldByIndex(1)));

      int iBegin = temp.indexOf(tag_ord);
      if(iBegin != -1)
      {
        result = temp.substring(iBegin + 2);
        int iEnd = result.indexOf("$");
        if(iEnd != -1)
        {
          result = result.substring(0, iEnd);
        }
      }

      if(i == 0)
      {
        bibinfo += result;
      }
      else
      {
        bibinfo += "; " + result;
      }

      i++;
      hManager.Next();
    }
    hManager.CloseStatement();

    return bibinfo;
  }


  public int GetFBcnt(int bib)
  {
	  hManager.OpenStatement();
	  hManager.ExecuteSQL("select count(*) from copy where bib# = " + bib + " and acq_status = 4", 0);
	  int cnt = hManager.IntegerByIndex(1);
	  hManager.CloseStatement();
	  return cnt;
  }

  public int[][] GetFB(int bib)
  {
    int cnt = GetFBcnt(bib);

    int[][] fuben = new int[cnt][2];
    int i = 0;

    hManager.OpenStatement();
    hManager.ExecuteSQL("select copy#, serial# from copy where bib# = " + bib + " and acq_status = 4", 0);
    while(hManager.bHasResult)
    {
      fuben[i][0] = hManager.IntegerByName("copy#");
      fuben[i][1] = hManager.IntegerByName("serial#");
      hManager.Next();
      i++;
    }
    hManager.CloseStatement();
    return fuben;
  }
  /**
   * 获取主类信息
   * @param copyId
   * @return
   */
  public String getMainClass(Integer copyId,String t)
  {
	  List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	if(copyId!=null) {
		hManager.OpenStatement();
		hManager.ExecuteSQL("select start_enum,start_date,end_enum,end_date,free_enum,free_chron from holding_summary where copy# = "+copyId, 0);
		while(hManager.bHasResult)
		{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("startDate",hManager.IntegerByName("start_date")); 
			map.put("endDate",hManager.IntegerByName("end_date")); 
			map.put("startEnum",hManager.FieldByName("start_enum")); 
			map.put("endEnum",hManager.FieldByName("end_enum")); 
			map.put("freeEnum",hManager.FieldByName("free_enum")); 
			map.put("freeChron",hManager.FieldByName("free_chron")); 
			list.add(map);
			hManager.Next();
		}
}
    String mainClass ="";
    hManager.CloseStatement();
    for (Map<String, Object> map : list) {
    	String startEnum = map.get("startEnum").toString();
    	String endEnum = map.get("endEnum").toString();
    	String freeChron = map.get("freeChron").toString();
    	String freeEnum = map.get("freeEnum").toString();
    	Integer startDate = Integer.parseInt(map.get("startDate").toString());
    	Integer endDate = Integer.parseInt(map.get("endDate").toString());
    	try {
    		String staStr = "";
    		String staCount ="";
    		String endStr ="";
    		String endCount ="";
    		if(!startEnum.trim().isEmpty() && !endEnum.trim().isEmpty()) {
    			if(startEnum.indexOf(";")!=-1) {
    				String[] startnum =  startEnum.split(";");
    				String[] sEnum = startnum[0].split(",");
    				if(sEnum.length>3) {
    					System.out.println(startnum);
    				}
    				staStr = getStr(startnum[0]);
    				staCount =  " 总 "+ startnum[1];
    			}else {
    				staStr = getStr(startEnum);
    			}
    			if(endEnum.indexOf(";")!=-1) {
    				String[] endnum =  endEnum.split(";");
    				String[] eEnum = endnum[0].split(",");
    				if(eEnum.length>3) {
    					System.out.println("eeeeee-"+endnum);
    				}
    				endStr = getStr(endnum[0]);
    				endCount = " 总 "+endnum[1];
    			}else {
    				endStr =	getStr(endEnum);
    			}	
    			
    			String str = "";
    			if(!staStr.equalsIgnoreCase(endStr)) {
    				str = staStr+" - "+endStr+"; ";
    			}else {
    				str = staStr+" ";
    			}
    			String count ="";
    			if(!staCount.isEmpty() && !endCount.isEmpty()) {
    				count = staCount+" - "+endCount;
    			}
    			String date ="";
    			if(!DateUtils.addYear("19700101", startDate).equals(DateUtils.addYear("19700101", endDate))) {
    				date = " ("+DateUtils.addYear("19700101", startDate)+" - "+DateUtils.addYear("19700101", endDate)+")";
    			}else {
    				date = " ("+DateUtils.addYear("19700101", startDate)+")";
    			}
    			mainClass += str+count+date+"\r";
    		//	mainClass +=staStr+" - "+endStr+"; "+staCount+" - "+endCount+" ("+DateUtils.addYear("19700101", startDate)+" - "+DateUtils.addYear("19700101", endDate)+")"+"\r";
    		}else {
    			mainClass +=freeEnum +" "+freeChron+" ("+DateUtils.addYear("19700101", endDate)+")"+"\r";
    		}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(t+"--------"+copyId);
			System.out.println(t);
			System.out.println(startEnum);
			System.out.println(endEnum);
		}
    	
	}
    return mainClass;
  }
  /**
   * 拼接字符串
   * @param enumstr
   * @return
   */
  public String getStr(String enumstr) {
	  String str ="";
		String[] eEnum = enumstr.split(",");
		if(eEnum.length>2) {
			str = eEnum[0]+", no. "+eEnum[1]+",总 "+eEnum[2];
		}else if(eEnum.length==2){
			if(eEnum[0].length()<=2) {
				str = "v. "+eEnum[0]+" ,no. "+eEnum[1];
			}else {
				str = eEnum[0]+", no. "+eEnum[1];
			}
		}else {
			if(eEnum[0].length()<=2) {
				str = "v. "+eEnum[0];
			}else {
				str = eEnum[0];
			}
		}
	  return str;
  }
  
  
  
  public int[][] GetFB(int bib,int copyId)
  {
    int cnt = GetFBcnt(bib);

    int[][] fuben = new int[cnt][2];
    int i = 0;

    hManager.OpenStatement();
    hManager.ExecuteSQL("select copy#, serial# from copy where bib# = " + bib + " and copy#="+copyId+"  and acq_status = 4", 0);
    while(hManager.bHasResult)
    {
      fuben[i][0] = hManager.IntegerByName("copy#");
      fuben[i][1] = hManager.IntegerByName("serial#");
      hManager.Next();
      i++;
    }
    hManager.CloseStatement();
    return fuben;
  }

  public String[] GetFBrec(int bib, int begindate, int enddate)
  {
    int[][] fuben = GetFB(bib);

    int len = 1;
    if(fuben.length != 0)
    {
      len = fuben.length;
    }
    String[] result = new String[len];

    if(fuben.length == 0)
    {
      result[0] = "没有馆藏";
    }
    else
    {
      int serial = fuben[0][1]; //所有复本的serial#都一样

      hManager.OpenStatement();
      hManager.ExecuteSQL("select count(*) from scki_issue where serial# = " + serial + " and end_date >= " + begindate + " and end_date <= " + enddate, 0);
      int cnt = hManager.IntegerByIndex(1);
      hManager.CloseStatement();

      if(cnt == 0)
      {
        for (int ri = 0; ri < result.length; ri++) {
          result[ri] = "该时间段无馆藏" + "\t";
        }
      }
      else
      {
        String[][] qici = new String[cnt][3];

        hManager.OpenStatement();
        hManager.ExecuteSQL(
            "select count(*) from scki_issue where pending is not null and serial# = " +
            serial + " and end_date >= " + begindate + " and end_date <= " +
            enddate, 0);
        int pending = hManager.IntegerByIndex(1);
        hManager.CloseStatement();

        hManager.OpenStatement();
        hManager.ExecuteSQL(
            "select enum, free_enum, issue_date, pending from scki_issue where serial# = " +
            serial + " and end_date >= " + begindate + " and end_date <= " +
            enddate, 0);
        int i = 0;
        while (hManager.bHasResult) {
          String enum_s = hManager.FieldByName("enum");
          if (enum_s.equals("")) {
            qici[i][0] = hManager.FieldByName("free_enum");
          }
          else {
            qici[i][0] = DecodeEnum(hManager.FieldByName("enum"));
          }
          qici[i][2] = hManager.FieldByName("issue_date");
          qici[i][1] = hManager.FieldByName("pending");
          i++;
          hManager.Next();
        }
        hManager.CloseStatement();

        if (pending == 0) { //没有缺期，则所有复本一起处理
          for (int ri = 0; ri < result.length; ri++) {
            result[ri] = fuben[ri][0] + "\t" + qici[0][0] + " ~ " +
                qici[qici.length - 1][0] + "\t";
          }
        }
        else { //有缺期，分复本处理
          for (int ri = 0; ri < result.length; ri++) {
            String[] temp = new String[2];
            temp[0] = "";
            temp[1] = "";
            String endtemp = "";
            String begintemp = "";

            for (int qi = 0; qi < qici.length; qi++) {
            	hManager.OpenStatement();
            	hManager.ExecuteSQL(
                  "select status from copy_issue where copy# = " + fuben[ri][0] +
                  " and serial# = " + serial + " and issue_date = " +
                  Integer.parseInt(qici[qi][2]), 0);
              int status = hManager.IntegerByIndex(1);
              hManager.CloseStatement();

              if (status != 0) { //缺期
                if (begintemp.equals("")) {
                  begintemp = qici[qi][0];
                }
                endtemp = qici[qi][0];
              }
              else {
                if (begintemp == endtemp) {
                  temp[0] += begintemp + "; ";
                }
                else {
                  temp[0] += begintemp + " ~ " + endtemp + "; ";
                }
                begintemp = "";
                endtemp = "";
                temp[1] += qici[qi][0] + "; ";
              }
            }
            result[ri] = fuben[ri][0] + "\t" + temp[0] + "缺 " + temp[1] + "\t";
          }
        }
      }
    }
    return result;
  }
  
  public String GetFBrec2(int bib,Integer copyId,String tilte)
  {
    int[][] fuben = GetFB(bib);

    int len = 1;
    if(fuben.length != 0)
    {
      len = fuben.length;
    }
    String[] result = new String[len];

    if(fuben.length == 0)
    {
      result[0] = "没有馆藏";
    }
    else
    {
      int serial = fuben[0][1]; //所有复本的serial#都一样

      hManager.OpenStatement();
      hManager.ExecuteSQL("select count(*) from scki_issue where serial# = " + serial, 0);
      int cnt = hManager.IntegerByIndex(1);
      hManager.CloseStatement();

      if(cnt == 0)
      {
        for (int ri = 0; ri < result.length; ri++) {
          result[ri] = "该时间段无馆藏" + "\t";
        }
      }
      else
      {
    	return   getMainClass(copyId, tilte);
      }
    }
    return "";
  }

  public String DecodeEnum(String enum_s)
  {
    String result = "";

    if (enum_s.indexOf(";") != -1)
    {
      String[] first = ut.FHString(enum_s);
      String[] second = ut.DHString(first[0]);

      if (second.length < 2)
      {
        result = enum_s;
      }
      else if (second.length == 2)
      {
        result = second[0] + ", 总" + first[1];
      }
      else if (second.length == 3)
      {
        result = second[0] + " no." + second[1] + ", 总" + first[1];
      }
      else if (second.length == 4)
      {
        try
        {
          result = second[0] + " no." + second[1] + " " +
              zimu[Integer.parseInt(second[2]) - 1] + ", 总" + first[1];
        }
        catch(Exception e)
        {
          result = second[0] + " no." + second[1] + " " + second[2] + ", 总" + first[1];
        }
      }
    }
    else
    {
      result = enum_s;
    }

    return result;
  }

/*
    public int[] GetFB(int bib)
    {
      int cnt = GetFBcnt(bib);
      int[] fuben = new int[cnt];
      int i = 0;

      HorizonDriver sqlManager = new HorizonDriver();
      sqlManager.OpenStatement();
      sqlManager.ExecuteSQL("select copy# from copy where bib# = " + bib + " and acq_status = 4", 0);
      while(sqlManager.bHasResult)
      {
        fuben[i] = sqlManager.IntegerByName("copy#");
        sqlManager.Next();
        i++;
      }
      sqlManager.CloseStatement();
      sqlManager.Disconnect();
      return fuben;
    }

  public String GetFBrec(int copy, int begindate, int enddate)
  {
    String result = "";
    HorizonDriver sqlManager = new HorizonDriver();
    sqlManager.OpenStatement();
    sqlManager.ExecuteSQL("select *, dateadd(day,issue_date,'19700101') as idate, dateadd(day,last_update_date,'19700101') as ldate from copy_issue where copy# = " + copy + " and last_update_date >= " + begindate + " and last_update_date <= " + enddate, 0);
    while(sqlManager.bHasResult)
    {
//      System.out.println(sqlManager.FieldByName("idate") + "\t" + sqlManager.FieldByName("ldate"));
      result += sqlManager.FieldByName("idate").substring(0, 10) + "; ";
      sqlManager.Next();
    }
    sqlManager.CloseStatement();
    sqlManager.Disconnect();
    return result;
  }
*/
  public int GetGCCnt(int bib)
  {
	  hManager.OpenStatement();
	  hManager.ExecuteSQL("select text from bib where bib# = " + bib + " and tag = '905'", 0);
	  int v = ut.GetSubNum(ReplaceHorizonStr(hManager.FieldByIndex(1)), "$v");
	  hManager.CloseStatement();

    return v;
  }

  public String GetGC(int bib, int i)
  {
	  hManager.OpenStatement();
	  hManager.ExecuteSQL("select text from bib where bib# = " + bib + " and tag = '905'", 0);
   	  String temp = ReplaceHorizonStr(hManager.FieldByIndex(1));
   	  hManager.CloseStatement();

    String result = "";
    int iBegin = 0;
    for(int k = 0; k < (i + 1); k++)
    {
      iBegin = temp.indexOf("$v");
      temp = temp.substring(iBegin + 2);
    }
    int iEnd = temp.indexOf("$v");
    if(iEnd != -1)
    {
      temp = temp.substring(0, iEnd);
    }

    int y = temp.indexOf("$y");
    if(y != -1)
    {
      temp = temp.substring(0, y) + "\t" + temp.substring(y + 2);
    }
    return temp;
  }

  //将字符串中的控制字符(31)全部转为$
  public String ReplaceHorizonStr(String sLine)
  {
    int iPosition;
    iPosition = sLine.indexOf(31, 0);
    String sLeft, sRight;
    while (iPosition != -1)
    {
      sLeft = sLine.substring(0, iPosition);
      sRight = sLine.substring(iPosition + 1);
      sLine = sLeft + "$" + sRight;
      iPosition = iPosition++;
      iPosition = sLine.indexOf(31, iPosition);
    }
    return sLine;
  }

  //将字符串中的控制字符(10)全部转为''
  public String ReplaceTenStr(String sLine)
  {
    int iPosition;
    iPosition = sLine.indexOf(16, 0);
    String sLeft, sRight;
    while (iPosition != -1)
    {
      sLeft = sLine.substring(0, iPosition);
      sRight = sLine.substring(iPosition + 1);
      sLine = sLeft + "" + sRight;
      iPosition = iPosition++;
      iPosition = sLine.indexOf(16, iPosition);
    }
    return sLine;
  }

  //将字符串中的控制字符(1E)全部转为''
  public String ReplaceEStr(String sLine)
  {
    int iPosition;
    iPosition = sLine.indexOf(30, 0);
    String sLeft, sRight;
    while (iPosition != -1)
    {
      sLeft = sLine.substring(0, iPosition);
      sRight = sLine.substring(iPosition + 1);
      sLine = sLeft + "" + sRight;
      iPosition = iPosition++;
      iPosition = sLine.indexOf(30, iPosition);
    }
    return sLine;
  }
  

//将字符串中的控制字符回车换行全部转为''，回车换行两个符号一起才能在txt中达到换行效果，两个都删除才去除换行
  
  public String ReplacelineStr(String sLine)
  {
    int iPosition;
    iPosition = sLine.indexOf(13, 0);
    String sLeft, sRight;
    while (iPosition != -1)
    {
      sLeft = sLine.substring(0, iPosition);
      sRight = sLine.substring(iPosition + 1);
      sLine = sLeft + " " + sRight;
      iPosition = iPosition++;
      iPosition = sLine.indexOf(13, iPosition);
    }
    
    iPosition = sLine.indexOf(10, 0);

    while (iPosition != -1)
    {
      sLeft = sLine.substring(0, iPosition);
      sRight = sLine.substring(iPosition + 1);
      sLine = sLeft + " " + sRight;
      iPosition = iPosition++;
      iPosition = sLine.indexOf(10, iPosition);
    }

    return sLine;
  }
  /**
   * 根据bib获取订阅摘要
   * @return
   */
  public Map<String, Object> getSubscribe(Integer bib) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			String sql = "select l.name as location,co.descr as collection,call,copy_number,media_type,acq_status,pac_note,c.copy# from copy c" + 
					" inner join location l on c.location=l.location inner join collection co on  co.collection=c.collection " + 
					" where bib#=%s and c.descr='%s'";
			sql = String.format(sql, bib,"阅");
			hManager.OpenStatement();
			hManager.ExecuteSQL(sql, 0);
			while (hManager.bHasResult) {
				Map<String, Object> rowData = new HashMap<String, Object>();//声明Map
				rowData.put("location",hManager.FieldByName("location"));
				rowData.put("collection",hManager.FieldByName("collection"));
				rowData.put("call",hManager.FieldByName("call"));
				rowData.put("copyNumber",hManager.FieldByName("copy_number"));
				rowData.put("mediaType",hManager.FieldByName("media_type"));
				rowData.put("acqStatus",hManager.FieldByName("acq_status"));
				rowData.put("pacNote",hManager.FieldByName("pac_note"));
				rowData.put("copyId",hManager.FieldByName("copy#"));
	            list.add(rowData);
	            hManager.Next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			hManager.CloseStatement();
		}
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

  
}

