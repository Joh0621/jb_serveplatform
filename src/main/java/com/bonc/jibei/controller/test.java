package com.bonc.jibei.controller;

import java.util.ArrayList;
import java.util.List;
import com.alibaba;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
public class Test {
    public static void main(String [] args){
        String fileName="D:/报表.xlsx";
        Excle
        ExcelWriter excelWriter = EasyExcel.write(fileName).build();
        List<Student> studentList=new ArrayList<Student>();
        Student student=new Student("1","张三","2000-01-01");
        studentList.add(student);
        //这里 需要指定写用哪个class去写
        WriteSheet writeSheet = EasyExcel.writerSheet(0, "学生信息1").head(Student.class).build();
        excelWriter.write(studentList, writeSheet);
        writeSheet = EasyExcel.writerSheet(1, "学生信息2").head(Student.class).build();
        excelWriter.write(studentList, writeSheet);
        //千万别忘记finish 会帮忙关闭流
        excelWriter.finish();
    }
}