package com.trade.controller.admin;
import com.trade.util.XlsExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class SqlController {
    @Autowired
    private DataSource dataSource;
    @RequestMapping("ssadmin/export")
    @ResponseBody
    public String export(HttpServletRequest request, HttpServletResponse response) {
//        List<Map<String, Object>> list =new ArrayList();
//        List<String> l=new ArrayList();
//        Map rowData = null;
        String sql=request.getParameter("sql");
//        String sql ="SELECT * FROM  fvirtualcaptualoperation limit 0,10";
        if(sql==null||sql==""){

            return "sql不能为空";
        }
        sql=sql.toLowerCase();
        if(sql.indexOf("update ")!=-1 ||sql.indexOf("delete ")!=-1 ||sql.indexOf("insert ")!=-1){
            return "sql不能含有update，delete，insert";
        }
        try {
            Connection conn= dataSource.getConnection();
            Statement sp=conn.createStatement();
            ResultSet rs =sp.executeQuery(sql);
            HttpSession session=request.getSession();
            session.setAttribute("sql",sql);
//            ResultSetMetaData md = rs.getMetaData();
//            int columnCount = md.getColumnCount();
//            while (rs.next()) {
//                rowData = new HashMap();
//                for (int i = 1; i <= columnCount; i++) {
//                    rowData.put(md.getColumnName(i), rs.getObject(i));
//                }
//                list.add(rowData);
//            }
////            List<Map<String, Object>> list = walletService.findGtZeroByTotal(keyWord);
//            XlsExport xls = new XlsExport();
//            // 标题
//            xls.createRow(0);
//            Map<String, Object> map1 = list.get(0);
//            Set<String> key =map1.keySet();
//            Iterator<String> iter=key.iterator();
//            int j=0;
//            while (iter.hasNext()){
//                String ss=iter.next();
//                l.add(ss);
//                xls.setCell(j, ss);
//                j++;
//            }
//
//            // 填入数据
//            for(int i=1, len=list.size(); i<=len; i++){
//                Map<String, Object> map = list.get(i-1);
//                xls.createRow(i);
//                for(int k=0;k<map.size();k++){
//                    Object s=map.get(l.get(k));
//                    if(s==null){
//                        s="";
//                    }
//
//                    xls.setCell(k, s.toString());
//                }
//
//            }
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//                response.setContentType("application/vnd.ms-excel");
//                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("统计表-", "utf-8") + format.format(new Date()) + ".xls");
//                xls.exportXls(response);
                sp.close();
                conn.close();

        }catch (Exception e){
            e.printStackTrace();
            return "请检查sql";
        }
        return "ok";
    }
    @RequestMapping("ssadmin/export_table")
    public void export_table(HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, Object>> list =new ArrayList();
        List<String> l=new ArrayList();
        Map rowData = null;
//        String sql ="SELECT * FROM  fvirtualcaptualoperation limit 0,10";
        HttpSession session=request.getSession();
        String sql=session.getAttribute("sql").toString();
        try {
            Connection conn= dataSource.getConnection();
            Statement sp=conn.createStatement();
            ResultSet rs =sp.executeQuery(sql);
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            while (rs.next()) {
                rowData = new LinkedHashMap();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnLabel(i), rs.getObject(i));
                }
                list.add(rowData);
            }
//            List<Map<String, Object>> list = walletService.findGtZeroByTotal(keyWord);
            XlsExport xls = new XlsExport();
            // 标题
            xls.createRow(0);
            Map<String, Object> map1 = list.get(0);
            Set<String> key =map1.keySet();
            Iterator<String> iter=key.iterator();
            int j=0;
            while (iter.hasNext()){
                String ss=iter.next();
                l.add(ss);
                xls.setCell(j, ss);
                j++;
            }

            // 填入数据
            for(int i=1, len=list.size(); i<=len; i++){
                Map<String, Object> map = list.get(i-1);
                xls.createRow(i);
                for(int k=0;k<map.size();k++){
                    Object s=map.get(l.get(k));
                    if(s==null){
                        s="";
                    }

                    xls.setCell(k, s.toString());
                }

            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("统计表-", "utf-8") + format.format(new Date()) + ".xls");
            xls.exportXls(response);
            sp.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();

        }
    }

    @RequestMapping("ssadmin/sqlJsp")
    public Object sqlJsp(){
        return "ssadmin/sqlJsp";
    }
}
