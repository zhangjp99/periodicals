package com.periodicals.util;

import org.apache.log4j.Logger;

public class ForeignQK
{
	private static Logger log = Logger.getLogger(ForeignQK.class.getClass());
  public ForeignQK()
  {
  }

  public static void main(String[] agrs)
  {
    utility ut = new utility();

    int daochunian = 2020;
//   String filename = "d:\\result_xw_" + daochunian + ".txt";
//    String filename = " d:\\result_xw_" + daochunian + "bib.txt";
   
   String filename = "d:\\result_xw_" + daochunian + "bib"+".txt";
   
    ForeignQK sqk = new ForeignQK();
    rwfile rwf = new rwfile();
    rwf.CreateFile("", filename);

    HorizonDriver hManager = new HorizonDriver();
    hManager.OpenStatement();
    long startTime = System.currentTimeMillis();
    log.info("xw-开始时间："+startTime);
   // hManager.ExecuteSQL("select distinct bib# from bib where bib# in(select bib# from bib where tag='001' and text like'1[2-4]%') and bib# not in (select bib# from bib where tag='092' and text like'%D03%') and bib# in (select bib# from copy where acq_status = 4)", 0);
    hManager.ExecuteSQL("select distinct bib# from bib b where exists(select bib# from bib b1 where tag='001' and text like'1[2-4]%' and b.bib#=b1.bib#) " + 
    		"and not exists(select bib# from bib b2 where tag='092' and text like'%D03%' and b.bib#=b2.bib#) " + 
    		"and exists(select bib# from copy c where acq_status = 4 and b.bib#=c.bib#)",0);
    log.info("bib-xw-sql结束时间："+(System.currentTimeMillis()-startTime));
//中文11；西文12；日文13，俄文14
//外文报纸 D03
    while(hManager.bHasResult)
    {
      int bib = hManager.IntegerByIndex(1);
//System.out.println(bib);

      String year = sqk.GetBibInfo(bib, "210", "$d");
      int beginyear = 0;
      int endyear = 0;
      String result_year = "";
      String result = "";

      try
      {
        int hpos = year.indexOf("-");
        if (hpos == -1)
        {
          int yearnum = sqk.returnNUM(year.trim());

          if (yearnum == 19)
            yearnum = 1900;

          if (yearnum >= 1900 & yearnum < 2100)
          {
            beginyear = yearnum;
            endyear = yearnum;
            result_year = beginyear + "-" + endyear;
          }
          else {
        	 System.out.println("yearnum:"+yearnum);
            result_year = "待决";
            hManager.Next();
          }
        }
        else
        {
          if (year.substring(hpos + 1).length() > 1)
          {
            int yearnum1 = sqk.returnNUM(ut.ReplaceAllStr("?", "0", year.substring(0, hpos).trim()));
            int yearnum2 = sqk.returnNUM(ut.ReplaceAllStr("?", "9", year.substring(hpos + 1).trim()));

            if (yearnum1 == 19)
              yearnum1 = 1900;
            if (yearnum2 == 19)
              yearnum2 = 1999;
            if (yearnum1 > 19 & yearnum1 <= 99)
              yearnum1 = 1900 + yearnum1;
            if (yearnum2 > 19 & yearnum2 <= 99)
              yearnum2 = 1900 + yearnum2;
            if (yearnum1 >= 1 & yearnum1 < 19)
              yearnum1 = 2000 + yearnum1;
            if (yearnum2 >= 1 & yearnum2 < 19)
              yearnum2 = 2000 + yearnum2;

            if ((yearnum1 >= 1800 & yearnum1 < 2100) & (yearnum2 >= 1800 & yearnum2 < 2100))
            {
              beginyear = yearnum1;
              endyear = yearnum2;
              result_year = beginyear + "-" + endyear;
            }
            else
            {
              if((yearnum2 >= 1800 & yearnum2 < 2100) & (yearnum2 < daochunian))  //有确切终止年份，且小于导出年
              {
                beginyear = yearnum1;
                endyear = yearnum2;
                result_year = beginyear + "-" + endyear;
              }
              else
              {
            	 System.out.println("yearnum1:"+yearnum1+"---yearnum2"+yearnum2);  
                result_year = "待决";
                hManager.Next();
              }
            }
          }
          else
          {
            beginyear = sqk.returnNUM(year.substring(0, hpos));
            endyear = daochunian;
            result_year = beginyear + "-" + endyear;
          }
        }
      }
      catch (Exception E)
      {
        result_year = "待决";
        hManager.Next();
      }

      if(result_year == "待决")
      {
        result = "输出";
      }
      else
      {
        int ypos = result_year.indexOf("-");
        beginyear = Integer.parseInt(result_year.substring(0, ypos));
        endyear = Integer.parseInt(result_year.substring(ypos + 1));

        if(daochunian == endyear)
        {
          result = "输出";
        }
        else if((beginyear <= daochunian) & (daochunian < endyear))
        {
          result = "输出";
        }
        else
        {
         System.out.println("beginyear:"+beginyear+"---endyear"+endyear+"----daochunian"+daochunian);  
          result = "不输出";
          hManager.Next();
        }
     }
//     rwf.AddtoFile(bib + "\t" + year + "\t" + result_year + "\t" + result, filename);
      rwf.AddtoFile(bib+"", filename);
      hManager.Next();
    }
    hManager.CloseStatement();
    hManager.Disconnect();
    log.info("xw-结束时间："+(System.currentTimeMillis()-startTime));
  }

  public String GetTitle(int bib)
  {
    HorizonDriver sqlManager = new HorizonDriver();
    sqlManager.OpenStatement();
    sqlManager.ExecuteSQL("select reconst from title where bib# = " + bib, 0);
    String result = sqlManager.FieldByIndex(1);
    sqlManager.CloseStatement();
    sqlManager.Disconnect();
    return ReplaceTenStr(ReplaceHorizonStr(result));
  }

  public String GetBibInfo(int bib, String tag, String tag_ord)
  {
    HorizonDriver sqlManager = new HorizonDriver();
    sqlManager.OpenStatement();
    sqlManager.ExecuteSQL("select text from bib where bib# = " + bib + " and tag = '" + tag + "'", 0);
    String temp = ReplaceHorizonStr(sqlManager.FieldByIndex(1));
    sqlManager.CloseStatement();
    sqlManager.Disconnect();

    String result = "";

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

    return result;
  }

  public int GetGCCnt(int bib)
  {
    utility ut = new utility();

    HorizonDriver sqlManager = new HorizonDriver();
    sqlManager.OpenStatement();
    sqlManager.ExecuteSQL("select text from bib where bib# = " + bib + " and tag = '905'", 0);
    int v = ut.GetSubNum(ReplaceHorizonStr(sqlManager.FieldByIndex(1)), "$v");
    sqlManager.CloseStatement();
    sqlManager.Disconnect();
    return v;
  }

  public String GetGC(int bib, int i)
  {
    HorizonDriver sqlManager = new HorizonDriver();
    sqlManager.OpenStatement();
    sqlManager.ExecuteSQL("select text from bib where bib# = " + bib + " and tag = '905'", 0);
    String temp = ReplaceHorizonStr(sqlManager.FieldByIndex(1));
    sqlManager.CloseStatement();
    sqlManager.Disconnect();

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

  //去掉字符串中所含有的所有非数字符号
  public int returnNUM(String s)
  {
    String result = "";
    int t = s.length();
    if(t == 0)
      return 0;
    int i = 0;
    while (i < t)
    {
      try
      {
        Integer.parseInt(s.substring(i,i+1));
        result += s.substring(i,i+1);
      }
      catch (NumberFormatException e)
      {
      }
      i++;
    }
    if(result.length() == 0)
      return 0;
    return Integer.parseInt(result);
  }

}

