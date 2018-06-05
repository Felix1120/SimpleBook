package com.felix.simplebook.model;

import android.database.Cursor;

import com.felix.simplebook.callback.ICallBacking;
import com.felix.simplebook.database.InfoBean;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Created by chaofei.xue on 2017/12/11.
 */

public class BackUpModel implements IBackUpModel {
    @Override
    public void databaseToFile(final ICallBacking<String> callBack, final String... path) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    File file = new File(path[0], path[1] + ".bu");
                    if (file.exists()) {
                        callBack.error("文件已存在");
                        callBack.error("备份失败");
                        return;
                    }
                    final FileOutputStream fos = new FileOutputStream(file);
                    List<InfoBean> infoBeans = DataSupport.findAll(InfoBean.class);
                    JSONObject json = new JSONObject();
                    JSONArray array = new JSONArray();
                    for (InfoBean infoBean : infoBeans) {
                        JSONObject object = new JSONObject();
                        String info = infoBean.getTime() + " "
                                + infoBean.getType() + " " + infoBean.getMoney() + " "
                                + infoBean.getStatus();
                        callBack.updateInfo(info);
                        object.put("id", infoBean.getId());
                        object.put("year", infoBean.getYear());
                        object.put("month", infoBean.getMonth());
                        object.put("day", infoBean.getDay());
                        object.put("time", infoBean.getTime());
                        object.put("money", infoBean.getMoney());
                        object.put("in_or_out", infoBean.getInOrOut());
                        object.put("type", infoBean.getType());
                        object.put("status", infoBean.getStatus());
                        array.put(object);
                    }
                    json.put("info", array);
                    String value = json.toString();
                    fos.write(value.getBytes());
                    fos.flush();
                    fos.close();
                    callBack.successful("备份完成");
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.error("备份失败");
                }
            }
        }.start();
    }

    @Override
    public void fileToDatabase(final ICallBacking<String> callBack, final String... path) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    StringBuffer stringBuffer = new StringBuffer();
                    File file = new File(path[0]);
                    if (!file.exists()) {
                        callBack.error("文件不存在");
                        callBack.error("还原失败");
                        return;
                    }
                    FileInputStream fis = new FileInputStream(file);
                    byte[] bytes = new byte[1024];
                    int number;
                    while ((number = fis.read(bytes)) != -1) {
                        stringBuffer.append(new String(bytes, 0, number));
                    }
                    fis.close();
                    JSONObject jsonObject = new JSONObject(stringBuffer.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("info");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        long id = object.getLong("id");
                        String year = object.getString("year");
                        String month = object.getString("month");
                        String day = object.getString("day");
                        String time = object.getString("time");
                        String money = object.getString("money");
                        String inOrOut = object.getString("in_or_out");
                        String type = object.getString("type");
                        String status = object.getString("status");
                        InfoBean infoBean = new InfoBean(id, year, month, day, time, type, money, status,
                                inOrOut);
                        String sql = "select * from InfoBean where year = ? and month = ? and day = ?" +
                                "and time = ? and money = ? and inorout = ? and type = ? and status = ?";
                        Cursor cursor = DataSupport.findBySQL(sql, year, month, day, time, money, inOrOut, type, status);
                        if (!cursor.moveToNext()) {
                            if (infoBean.save()) {
                                callBack.updateInfo("已还原 " + time + " " + type + " " + money + " " + status);
                            } else {

                            }
                        } else {
                            callBack.updateInfo("已存在 " + time + " " + type + " " + money + " " + status);
                        }
                        //置空
                        infoBean = null;
                    }
                    callBack.successful("还原完成");
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.error("还原失败");
                }
            }
        }.start();
    }

    @Override
    public void databaseToExcel(final ICallBacking<String> callBack, final String... path) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    // 打开文件
                    WritableWorkbook book = Workbook.createWorkbook(new File(path[0], path[1] + ".xls"));
                    // 生成名为“简帐”的工作表，参数0表示这是第一页
                    WritableSheet sheet = book.createSheet("简帐", 0);
                    //设置行高列宽
                    sheet.setColumnView(0, 25);
                    sheet.setColumnView(1, 25);
                    sheet.setColumnView(2, 25);
                    sheet.setColumnView(3, 25);
                    //设置字体格式
                    WritableFont fontHead = new WritableFont(WritableFont.createFont("黑体"),
                            15, WritableFont.BOLD);
                    fontHead.setColour(Colour.WHITE);
                    //设置表格格式
                    WritableCellFormat formatHead = new WritableCellFormat(fontHead);
                    //设置对齐方式为水平居中
                    formatHead.setAlignment(Alignment.CENTRE);
                    //设置对齐方式为垂直居中
                    formatHead.setVerticalAlignment(VerticalAlignment.CENTRE);
                    formatHead.setBackground(Colour.GREEN);
                    //创建表头
                    Label labelType = new Label(0, 0, "类型", formatHead);
                    sheet.addCell(labelType);
                    Label labelStatus = new Label(1, 0, "用途", formatHead);
                    sheet.addCell(labelStatus);
                    Label labelMoney = new Label(2, 0, "金额", formatHead);
                    sheet.addCell(labelMoney);
                    Label labelTime = new Label(3, 0, "日期", formatHead);
                    sheet.addCell(labelTime);
                    Label labelInOrOut = new Label(4, 0, "入/出", formatHead);
                    sheet.addCell(labelInOrOut);

                    /**
                     * 获取数据库内容并保存
                     * */
                    //设置字体格式
                    WritableFont fontBody = new WritableFont(WritableFont.createFont("黑体"),
                            13);
                    fontBody.setColour(Colour.WHITE);
                    //设置表格格式
                    WritableCellFormat formatBodyIn = new WritableCellFormat(fontBody);
                    //设置对齐方式为水平居中
                    formatBodyIn.setAlignment(Alignment.CENTRE);
                    //设置对齐方式为垂直居中
                    formatBodyIn.setVerticalAlignment(VerticalAlignment.CENTRE);
                    //设置背景颜色
                    formatBodyIn.setBackground(Colour.ORANGE);

                    //设置表格格式
                    WritableCellFormat formatBodyOut = new WritableCellFormat(fontBody);
                    //设置对齐方式为水平居中
                    formatBodyOut.setAlignment(Alignment.CENTRE);
                    //设置对齐方式为垂直居中
                    formatBodyOut.setVerticalAlignment(VerticalAlignment.CENTRE);
                    //设置背景颜色
                    formatBodyOut.setBackground(Colour.GREY_80_PERCENT);
                    List<InfoBean> infoBeans = DataSupport.findAll(InfoBean.class);

                    int Row = 1;
                    for (InfoBean infoBean : infoBeans) {
                        if (infoBean.getInOrOut().equals("in")) {
                            //创建内容
                            Label label1 = new Label(0, Row, infoBean.getType(), formatBodyIn);
                            sheet.addCell(label1);
                            Label label2 = new Label(1, Row, infoBean.getStatus(), formatBodyIn);
                            sheet.addCell(label2);
                            Label label3 = new Label(2, Row, infoBean.getMoney(), formatBodyIn);
                            sheet.addCell(label3);
                            Label label4 = new Label(3, Row, infoBean.getTime(), formatBodyIn);
                            sheet.addCell(label4);
                            Label label5 = new Label(4, Row, "收入", formatBodyIn);
                            sheet.addCell(label5);
                        } else {
                            //创建内容
                            Label label1 = new Label(0, Row, infoBean.getType(), formatBodyOut);
                            sheet.addCell(label1);
                            Label label2 = new Label(1, Row, infoBean.getStatus(), formatBodyOut);
                            sheet.addCell(label2);
                            Label label3 = new Label(2, Row, infoBean.getMoney(), formatBodyOut);
                            sheet.addCell(label3);
                            Label label4 = new Label(3, Row, infoBean.getTime(), formatBodyOut);
                            sheet.addCell(label4);
                            Label label5 = new Label(4, Row, "支出", formatBodyOut);
                            sheet.addCell(label5);
                        }
                        Row++;
                    }
                    //写入数据并关闭
                    book.write();
                    book.close();
                    callBack.successful("备份完成");

                } catch (WriteException e) {
                    e.printStackTrace();
                    callBack.successful("备份失败");
                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.successful("备份失败");
                }
            }
        }.start();
    }

    @Override
    public void excelToDatabase(final ICallBacking<String> callBack, final String... path) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Workbook workbook = Workbook.getWorkbook(new File(path[0]));
                    //获取第一个表格“简帐”
                    Sheet sheet = workbook.getSheet(0);

                    int row = sheet.getRows(); //19

                    for (int i = 1; i < row; i++) { //第一行为标题 从第二行开始
                        // year,  month,  day,  time,  type,  money,  status,  inOrOut
                        //getCell(int i, int j) i:列 j:行
                        String time = sheet.getCell(3, i).getContents();
                        String inOrOut;
                        if (sheet.getCell(4, i).getContents().equals("收入")) {
                            inOrOut = "in";
                        } else if (sheet.getCell(4, i).getContents().equals("支出")) {
                            inOrOut = "out";
                        } else {
                            return;
                        }
                        String[] times = time.split("\\.");
                        InfoBean infoBean = new InfoBean(times[0], times[1], times[2], time,
                                sheet.getCell(0, i).getContents(),
                                sheet.getCell(2, i).getContents(),
                                sheet.getCell(1, i).getContents(), inOrOut
                        );
                        infoBean.save();
                    }

                    //读取数据关闭
                    workbook.close();
                    callBack.successful("还原完成");

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.error("还原失败");
                } catch (BiffException e) {
                    e.printStackTrace();
                    callBack.error("还原失败");
                }
            }
        }.start();
    }
}
