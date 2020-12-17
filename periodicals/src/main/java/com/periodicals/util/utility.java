package com.periodicals.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class utility {

  //读入配置文件
  public utility() {
    try {

    }
    catch (Exception E) {

    }
  }
  //系统总目录
//  public final static String mainpath = "d:\\Tomcat 4.1\\webapps\\zzbz\\";
  public final static String mainpath = "d:\\Apache Group\\Tomcat 4.1\\webapps\\zzbz\\";
  //网页模板目录
  public final static String TEMPLATEPATH = mainpath+"template\\";
  //临时文件目录
  public final static String tempfilepath = mainpath+"tempfile\\";
  //资源目录
  public final static String sourcepath = "d:\\zzbz\\";
  // 日志文件所在路径
  public final static String LOG_FILE_PATH =sourcepath + "log\\";
  // 读者所能借书最大数量

  public String ReplaceStr(String sOld, String sNew, String sLine) {
    int iPosition;
    iPosition = sLine.indexOf(sOld);
    if (iPosition == -1) {
      return sLine;
    }
    String sLeft, sRight;
    sLeft = sLine.substring(0, iPosition);
    sRight = sLine.substring(iPosition + sOld.length());
    return sLeft + sNew + sRight;
  }

  //字符串完全替换函数
  public String ReplaceAllStr(String sOld, String sNew, String sLine) {
    int iPosition;
    iPosition = sLine.indexOf(sOld, 0);
    String sLeft, sRight;
    while (iPosition != -1) {
      sLeft = sLine.substring(0, iPosition);
      sRight = sLine.substring(iPosition + sOld.length());
      sLine = sLeft + sNew + sRight;
      iPosition = iPosition + sNew.length();
      iPosition = sLine.indexOf(sOld, iPosition);
    }
    return sLine;
  }

  //从文本文件中读取整个字符串，并给每行后面加一个回车字符
  public String ReadAllLine(String sFilename) {
    try {
      String sLine = "", sAllLine = "";
      java.io.File fpFile = new java.io.File(sFilename);
      BufferedReader brIn = new BufferedReader(new FileReader(fpFile));
      while ( (sLine = brIn.readLine()) != null) {
        sAllLine = sAllLine + sLine+"\n";
      }
      brIn.close();
      return sAllLine;
    }
    catch (FileNotFoundException e) {
      System.out.println(e);
      return "";
    }
    catch (IOException e) {
      System.out.println(e);
      return "";
    }
  }

//从文本文件中读取整个字符串，并给每行后面加一个回车字符
  public String ReadAllLineNon(String sFilename) {
    try {
      String sLine = "", sAllLine = "";
      java.io.File fpFile = new java.io.File(sFilename);
      BufferedReader brIn = new BufferedReader(new FileReader(fpFile));
      while ( (sLine = brIn.readLine()) != null) {
        sAllLine = sAllLine + sLine;
      }
      brIn.close();
      return sAllLine;
    }
    catch (FileNotFoundException e) {
      System.out.println(e);
      return "";
    }
    catch (IOException e) {
      System.out.println(e);
      return "";
    }
  }

  //字符串格式转换函数：Native到Unicode
  public String NativeToUnicode(String sOld) {
    if (sOld == null || sOld.length() == 0) {
      return "";
    }
    byte[] byBuffer = new byte[sOld.length()];
    for (int i = 0; i < sOld.length(); i++) {
      byBuffer[i] = (byte) sOld.charAt(i);
    }
    return new String(byBuffer);
  }

  //字符串格式转换函数：Unicode到Native
  public String UnicodeToNative(String sOld) {

    if (sOld == null || sOld.length() == 0) {
      return "";
    }
    char[] cBuffer = new char[sOld.length() * 2];
    char cTemp = ' ';
    int n = 0;
    try {
      for (int i = 0; i < sOld.length(); i++) {
        if (sOld.charAt(i) >= 128) {
          cTemp = sOld.charAt(i);
          byte[] byTemp = ("" + cTemp).getBytes();
          cBuffer[n++] = (char) byTemp[0];
          cBuffer[n - 1] -= 65280;
          cBuffer[n++] = (char) byTemp[1];
          cBuffer[n - 1] -= 65280;
        }
        else {
          cBuffer[n++] = sOld.charAt(i);
        }
      }
    }
    catch (Exception e) {
    }
    return new String(cBuffer, 0, n);
  }


  public String DBCTransferToSBC(String stringInputText)
  {
      char[] resutChars = stringInputText.toCharArray();
      for (int i = 0; i < resutChars.length;i++ )
      {
          if (resutChars[i] == 32)
          {
              resutChars[i] = (char)12288;
              continue;
          }
          if (resutChars[i] < 127)
          {
              resutChars[i] = (char)(resutChars[i]+65248);
          }
      }
      return new String(resutChars);
  }
  public static String SBCtransferToDBC(String stringInputText)
  {
      char[] resutChars = stringInputText.toCharArray();
      for (int i = 0; i < resutChars.length;i++ )
      {
          if (resutChars[i] == 12288)
          {
              resutChars[i] = (char)32;
              continue;
          }
          if (resutChars[i] > 65280 && resutChars[i] < 65375)
          {
              resutChars[i] = (char)(resutChars[i]-65248);
          }
      }
      return new String(resutChars);
  }

  //获取指定格式的当前日期
  public static String CurrentDate(String sFormat) {
    Date dateNow = new Date();
    SimpleDateFormat sdfInstance = new SimpleDateFormat(sFormat);
    return sdfInstance.format(dateNow);
  }

  // 计算两个日期之间的差
  // 注意两个日期格式必须相同
  // @param startDate 字符串开始时间
  // @param endDate 字符串结束时间
  // @param format 时间格式
  // @return int  相差天数

  public int countDays(String startDate, String endDate, String format)

      {

        try{

          SimpleDateFormat sf = new SimpleDateFormat(format);

          Date sDate = sf.parse(startDate);

          Date eDate = sf.parse(endDate);

          Calendar c = Calendar.getInstance();



          c.setTime(sDate);

          long ls = c.getTimeInMillis();



          c.setTime(eDate);

          long le = c.getTimeInMillis();



          return (int) ( (le - ls) / (24 * 3600 * 1000));

        }
        catch(Exception e){

          return 0;

        }

      }


  //读写无汉字log文件--张磊
  public void RWEnLogFile(String writestring, String filename) {
    try {
      File f = new File(filename);
      RandomAccessFile raf = new RandomAccessFile(f, "rw");
      raf.seek(raf.length());
      raf.writeBytes(writestring);
      raf.close();
    }
    catch (IOException e) {
      System.out.println("RWEnLogFile");
    }
  }

  //拆分用分号分割的字符串
  public String[] FHString(String collec)
  {
    int count = GetFHNum(collec);
    String[] ask_num = new String[count+2];
    int i = 0;
    int pos = collec.indexOf(";");
    do
    {
      ask_num[i] = GetFHString(collec);
      i++;
      collec = CutFHString(collec);
      pos = collec.indexOf(";");
    }while (pos != -1);
    ask_num[i] = collec;
    return ask_num;
  }

  public int GetFHNum(String st)
  {
    int pos = 0;
    int count = 0;
    String temp = st;
    if (temp.equals(""))
    {
      count = 0;
    }
    else
    {
      pos = temp.indexOf(";");
      while (pos != -1)
      {
        if (pos != (temp.length() - 1))
          count++;
        temp = temp.substring(pos + 1, temp.length());
        pos = temp.indexOf(";");
      }
      pos = st.indexOf(";");
      if (pos == 0)
        count--;
    }
    return count;
  }

  public String GetFHString(String st)
  {
    int pos = st.indexOf(";");
    String re = "";
    if (pos == -1)
      re = st;
    else
      re = st.substring(0, pos);
    return re;
  }

  public String CutFHString(String st)
  {
    int pos = st.indexOf(";");
    String re = "";
    if (pos == -1)
      re = "";
    else
      re = st.substring(pos + 1, st.length());
    return re;
  }

  //对长度不够的字符串补空格
  public String AddSpace(String oldstring, int orderlength) {
    String space = " ";
    while (oldstring.length() < orderlength) {
      oldstring += space;
    }
    return oldstring;
  }

  //返回字符串中，头到第一个#之间的字符串
  public String GetSharpString(String st) {
    int pos = st.indexOf("#");
    String re = "";
    if (pos == -1)
      re = st;
    else
      re = st.substring(0, pos);
    return re;
  }

  public String GetSharpString(String st, String sharp) {
    int pos = st.indexOf(sharp);
    String re = "";
    if (pos == -1)
      re = st;
    else
      re = st.substring(0, pos);
    return re;
  }

  //返回字符串中，第一个#之后的字符串
  public String CutSharpString(String st) {
    int pos = st.indexOf("#");
    String re = "";
    if (pos == -1)
      re = "";
    else
      re = st.substring(pos + 1, st.length());
    return re;
  }

  public String CutSharpString(String st, String sharp) {
    int pos = st.indexOf(sharp);
    String re = "";
    if (pos == -1)
      re = "";
    else
      re = st.substring(pos + 1, st.length());
    return re;
  }

  //拆分用#分割的字符串,存放到数组中
  public String[] SharpString(String collec) {
    String sharp = "#";
    int count = GetSubNum(collec, sharp);
    String[] ask_num = new String[count + 1];
    int i = 0;
    int pos = 0;
    i = 0;
    pos = collec.indexOf(sharp);
    while (pos != -1) {
      ask_num[i] = GetSharpString(collec);
      i++;
      collec = CutSharpString(collec);
      pos = collec.indexOf(sharp);
    }
    ask_num[i] = collec;
    return ask_num;
  }

  public String[] SharpString(String collec, String sharp) {
    int count = GetSubNum(collec, sharp);
    String[] ask_num = new String[count + 1];
    int i = 0;
    int pos = 0;
    i = 0;
    pos = collec.indexOf(sharp);
    if (pos == 0) {
      collec = collec.substring(1, collec.length());
      pos = collec.indexOf(sharp);
    }
    while (pos != -1) {
      ask_num[i] = GetSharpString(collec, sharp);
      i++;
      collec = CutSharpString(collec, sharp);
      pos = collec.indexOf(sharp);
    }
    if (i < ask_num.length)
      ask_num[i] = collec;
    return ask_num;
  }

  //得到该字符串包含子字符串的个数
  public int GetSubNum(String st, String subst) {
    int pos = 0;
    int count = 0;
    String temp = st;
    if (temp.equals("")) {
      count = 0;
    }
    else {
      pos = temp.indexOf(subst);
      while (pos != -1) {
        if (pos != (temp.length() - 1))
          count++;
        temp = temp.substring(pos + 1, temp.length());
        pos = temp.indexOf(subst);
      }
      pos = st.indexOf(subst);
      if (pos == 0)
        count--;
    }
    return count;
  }

  //比较两个数组的匹配度，返回从第一数组开始有几个一样
  public int compare(String[] t1, String[] t2) {
    int i = 0, j = 1;

    while (i < t1.length && i < t2.length && j == 1) {
      if (t1[i].equals(t2[i])) {
        i++;
      }
      else {
        j = 0;
      }
    }
    return i;
  }

  //得到由#分割的最后一段字符串
  public String LastSharp(String st) {
    String ls = "";
    String sharp = "#";
    int sharp_num = GetSubNum(st, sharp);
    if (sharp_num == 0) {
      ls = "";
    }
    else {
      String[] tt = SharpString(st);
      ls = tt[sharp_num];
    }
    return ls;
  }

  //得到两个#之间的字符串数组
  public String[] BetweenSharp(String st) {
    String[] data;
    String[] data1 = SharpString(st);
    int num = data1.length;
    int i = 0;
    if ( (num % 2) == 1) {
      data = new String[ (num / 2)];
      for (i = 0; i < (num / 2); i++)
        data[i] = data1[i * 2 + 1];
    }
    else {
      data = new String[0];
    }
    return data;
  }

  //去掉指定符号前后的元素
  public String QF(String st, String subst) {
    String rst = "";
    int pos = 0, i = 0;
    pos = st.indexOf(subst);
    if (pos == -1) {
      rst = st;
    }
    else {
      String[] d = SharpString(st, "<");
      for (i = 0; i < d.length; i++) {
        pos = d[i].indexOf(subst);
        if (pos == -1) {
          rst += "<" + d[i];
        }
        else {
          i = i + 1;
        }
      }
    }
    return rst;
  }

  //去掉字符串中所含有的该符号
  public String qtf(String ys, String qs) {
    String tys = ys;
    int t = 0;
    t = tys.indexOf(qs);
    while (t != -1) {
      tys = tys.substring(0, t) + tys.substring(t + qs.length());
      t = tys.indexOf(qs);
    }
    return tys;
  }

  //检验是否为可转成数字的字符串
  public int ifinteger(String s) {
    int i = 0;
    try {
      Integer.parseInt(s);
      i = 1;
    }
    catch (NumberFormatException e) {
      i = 0;
    }
    return i;
  }

  //过滤指定的字符
  public String FilterChar(String s, char c) {
    int i = 0;
    while ( (i = s.indexOf( (int) c)) > 0) {
      s = s.substring(0, i) + s.substring(i + 1, s.length());
    }
    return s;
  }

  public String IfStrIsLegal(String str)
  {
    String Legality = "true";
    int num = 0;
    for (int i = 0; i < str.length(); i++)
    {
      if ((int)str.charAt(i) < 128) {
        num = num + 1;
      }
    }
    if (num > 0 )
    {
      Legality = "false";
    }
    return Legality;
  }

  public String CPToGB(String str) {
      try {
        byte[] temp = str.getBytes("cp850");
        String result = new String(temp, "gb2312");
        return result;
      }
      catch (UnsupportedEncodingException ex) {
        return null;
      }
    }


/*//计算天数
   public int getDays(GregorianCalendar g1, GregorianCalendar g2) {
      int elapsed = 0;
      GregorianCalendar gc1, gc2;

      if (g2.after(g1)) {
         gc2 = (GregorianCalendar) g2.clone();
         gc1 = (GregorianCalendar) g1.clone();
      }
      else   {
         gc2 = (GregorianCalendar) g1.clone();
         gc1 = (GregorianCalendar) g2.clone();
      }

      gc1.clear(Calendar.MILLISECOND);
      gc1.clear(Calendar.SECOND);
      gc1.clear(Calendar.MINUTE);
      gc1.clear(Calendar.HOUR_OF_DAY);

      gc2.clear(Calendar.MILLISECOND);
      gc2.clear(Calendar.SECOND);
      gc2.clear(Calendar.MINUTE);
      gc2.clear(Calendar.HOUR_OF_DAY);

      while ( gc1.before(gc2) ) {
         gc1.add(Calendar.DATE, 1);
         elapsed++;
      }
      return elapsed;
   }*/

//拆分用逗号分割的字符串
public String[] DHString(String collec)
{
  int count = GetDHNum(collec);
  String[] ask_num = new String[count+2];
  int i = 0;
  int pos = collec.indexOf(",");
  do
  {
    ask_num[i] = GetDHString(collec);
    i++;
    collec = CutDHString(collec);
    pos = collec.indexOf(",");
  }while (pos != -1);
  ask_num[i] = collec;
  return ask_num;
}

public int GetDHNum(String st)
{
  int pos = 0;
  int count = 0;
  String temp = st;
  if (temp.equals(""))
  {
    count = 0;
  }
  else
  {
    pos = temp.indexOf(",");
    while (pos != -1)
    {
      if (pos != (temp.length() - 1))
        count++;
      temp = temp.substring(pos + 1, temp.length());
      pos = temp.indexOf(",");
    }
    pos = st.indexOf(",");
    if (pos == 0)
      count--;
  }
  return count;
}

public String GetDHString(String st)
{
  int pos = st.indexOf(",");
  String re = "";
  if (pos == -1)
    re = st;
  else
    re = st.substring(0, pos);
  return re;
}

public String CutDHString(String st)
{
  int pos = st.indexOf(",");
  String re = "";
  if (pos == -1)
    re = "";
  else
    re = st.substring(pos + 1, st.length());
  return re;
}


//////////////////////////////////////////////////
 public String[] TitleString(String collec)
 {
   int count = GetTitleNum(collec);
   String[] ask_num = new String[count+2];
   int i = 0;
   int pos = collec.indexOf(31);
   do
   {
     ask_num[i] = GetTitleString(collec);
     i++;
     collec = CutTitleString(collec);
     pos = collec.indexOf(31);
   }while (pos != -1);
   ask_num[i] = collec;
   return ask_num;
 }

 public int GetTitleNum(String st)
 {
   int pos = 0;
   int count = 0;
   String temp = st;
   if (temp.equals(""))
   {
     count = 0;
   }
   else
   {
     pos = temp.indexOf(31);
     while (pos != -1)
     {
       if (pos != (temp.length() - 1))
         count++;
       temp = temp.substring(pos + 1, temp.length());
       pos = temp.indexOf(31);
     }
     pos = st.indexOf(31);
     if (pos == 0)
       count--;
   }
   return count;
 }

 public String GetTitleString(String st)
 {
   int pos = st.indexOf(31);
   String re = "";
   if (pos == -1)
     re = st;
   else
     re = st.substring(0, pos);
   return re;
 }

 public String CutTitleString(String st)
 {
   int pos = st.indexOf(31);
   String re = "";
   if (pos == -1)
     re = "";
   else
     re = st.substring(pos + 1, st.length());
   return re;
 }
}