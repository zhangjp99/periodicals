package com.periodicals.util;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;

public class rwfile {
  utility ut = new utility();

  //得到指定目录下的指定的文件名列表
  public String[] GetDirFilename(String dir) {
    String[] filename;
    File f = new File(dir);
    filename = f.list();
    return filename;
  }

  //将数组分行写入文件中
  public void WriteFile(String[] data, String filename) {
    try {
      BufferedWriter wf = new BufferedWriter(new FileWriter(new File(filename)));
      int i = 0;
      for (i = 0; i < data.length; i++)
        wf.write(data[i] + "\r\n");
      wf.close();
    }
    catch (Exception e) {
      System.out.println("WriteFile error:" + filename);
    }
  }

  //将字符串写入文件中
  public void WriteFile(String data, String filename) {
    try {
      BufferedWriter wf = new BufferedWriter(new FileWriter(new File(filename)));
      wf.write(data);
      wf.close();
    }
    catch (Exception e) {
      System.out.println("WriteFile error:" + filename);
    }
  }

  //创建一个文件
  public void CreateFile(String data, String filename) {
    try {
      BufferedWriter wf = new BufferedWriter(new FileWriter(new File(filename)));
      int i = 0;
      wf.write(data);
      wf.close();
    }
    catch (Exception e) {
      System.out.println("CreateFile error:" + filename);
    }
  }

  //创建一个文件夹
  public void CreateDir(String dirname)
  {
    try
    {
      File dirFile = new File(dirname);
      boolean bFile = dirFile.exists();

      if(bFile == false)
      {
        dirFile.mkdir();
      }
    }
    catch (Exception e) {
      System.out.println("CreateDir error:" + dirname);
    }
  }

//将文件中的内容分行读入数组中
public String[] ReadFile(String filename)
{
  try {
    String tempstring = "";
    String lenstring = "";
    String temp = "";
    BufferedReader rf = new BufferedReader(new FileReader(new File(filename)));
    int i = 0, j = 0, len = 0;
    temp = rf.readLine();
    while (temp != null) {
      if (!temp.equals("")) {
        i++;
      }
      temp = rf.readLine();
    }
    rf.close();
    String[] data = new String[i];
    rf = new BufferedReader(new FileReader(new File(filename)));
    for (j = 0; j < i; j++)
    {
      data[j] = rf.readLine();
      data[j] = data[j].trim();
    }
    rf.close();
    return data;
  }
  catch (Exception e) {
    System.out.println("ReadFile error:" + filename);
    String[] data = new String[1];
    return data;
  }
}

//在文件中续写内容字符串
public void AddtoFile(String data, String filename) {
  try {
      RandomAccessFile rf = new RandomAccessFile(filename, "rw");
      rf.seek(rf.length());
      rf.write(data.getBytes());
      rf.write("\r\n".getBytes());
      rf.close();//关闭文件流
  }
  catch (Exception e) {
    System.out.println("AddtoFile error:" + filename);
  }
}

  public int LeIntByte2BeInt(byte[] bInt)
  {
    byte temp = bInt[0];
    bInt[0] = bInt[3];
    bInt[3] = temp;
    temp = bInt[1];
    bInt[1] = bInt[2];
    bInt[2] = temp;

    int iInt0, iInt1, iInt2, iInt3;
    if (bInt[0]<0)
    iInt0=bInt[0]+256;
    else
         iInt0=bInt[0];
    if (bInt[1]<0)
         iInt1=bInt[1]+256;
    else
         iInt1=bInt[1];
    if (bInt[2]<0)
         iInt2=bInt[2]+256;
    else
         iInt2=bInt[2];
    if (bInt[3]<0)
    iInt3=bInt[3]+256;
    else
         iInt3=bInt[3];

    int iRetInt = iInt0*256*256*256 + iInt1*256*256 +  iInt2*256+iInt3;
    return iRetInt;
  }

}