package com.trade.controller.admin;

import com.trade.Enum.*;
import com.trade.model.*;
import com.trade.service.admin.*;
import com.trade.service.front.FrontAccountService;
import com.trade.util.*;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class VirtualCapitaloperationController extends BaseController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private VirtualWalletService virtualWalletService;
    @Autowired
    private VirtualCapitaloperationService virtualCapitaloperationService;
    @Autowired
    private EntrustService entrustService;
    @Autowired
    private VirtualCoinService virtualCoinService;
    @Autowired
    private DateWalletStatisticService dateWalletStatisticService;
    @Autowired
    private FrontAccountService frontAccountService;

    // 每页显示多少条数据
    private int numPerPage = Utils.getNumPerPage();

    //查看交易详情跳转
    @RequestMapping("ssadmin/detailJsp")
    public ModelAndView detailPassword(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        Map typeMap = new HashMap();
        List<Fvirtualcointype> type = this.virtualCoinService.findAll();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        modelAndView.addObject("typeMap", typeMap);
        modelAndView.setViewName("ssadmin/detailJsp");
        return modelAndView;
    }

    //查看交易详情
    @RequestMapping("ssadmin/detail")
    @RequiresPermissions("ssadmin/detail.html")
    public ModelAndView detail(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        Fvirtualcointype fvirtualcointype;
        String xid;
        String uid = request.getParameter("uid");
        if (uid == "" || uid == null) {
            xid = request.getParameter("txid");
            String ftype = request.getParameter("ftype");
            if (xid == "" || xid == null) {
                modelAndView.setViewName("ssadmin/comm/ajaxDone");
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "请填写交易号");
                return modelAndView;
            }
            xid = xid.trim();
            if (ftype == "" || ftype == null) {
                modelAndView.setViewName("ssadmin/comm/ajaxDone");
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "请选择币种");
                return modelAndView;
            }
            fvirtualcointype = virtualCoinService.findById(Integer.valueOf(ftype));

        } else {
            int fid = Integer.parseInt(uid);
            Fvirtualcaptualoperation virtualcaptualoperation = this.virtualCapitaloperationService
                    .findById(fid);
            xid = virtualcaptualoperation.getFtradeUniqueNumber();
            if (xid == "" || xid == null) {
                modelAndView.setViewName("ssadmin/comm/ajaxDone");
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "交易号不能为空");
                return modelAndView;
            }
            fvirtualcointype = virtualcaptualoperation
                    .getFvirtualcointype();
        }

        BTCMessage btcMsg = new BTCMessage();
        btcMsg.setACCESS_KEY(fvirtualcointype.getFaccess_key());
        btcMsg.setSECRET_KEY(fvirtualcointype.getFsecrt_key());
        btcMsg.setIP(fvirtualcointype.getFip());
        btcMsg.setPORT(fvirtualcointype.getFport());
        BTCUtils btcUtils = new BTCUtils(btcMsg);
        try {
            JSONObject json = btcUtils.gettransaction(xid);
//			    error.toString();
            if (json.get("result").toString().equals("null")) {
                modelAndView.setViewName("ssadmin/comm/ajaxDone");
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "没有该交易");
                return modelAndView;
            }

            JSONObject result = (JSONObject) (json.get("result"));
            String fees = result.get("fee").toString();
            double fee = Math.abs(Double.valueOf(fees));
            String time = result.get("timereceived").toString();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long t = Long.valueOf(time);
            String d = format.format(t * 1000);
            Date date = format.parse(d);
            modelAndView.setViewName("ssadmin/detail");
            modelAndView.addObject("statusCode", 200);
            modelAndView.addObject("map", json);
            modelAndView.addObject("coinType", fvirtualcointype.getFname());
            modelAndView.addObject("date", date);
            modelAndView.addObject("fee", fee);
            return modelAndView;
        } catch (Exception e1) {
            System.out.println("error 1 " + e1);
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "查看失败，钱包连接失败");
            return modelAndView;
        }
//		return modelAndView;

    }

    @RequestMapping("/ssadmin/virtualCaptualoperationList")
    @RequiresPermissions("ssadmin/virtualCaptualoperationList.html")
    public ModelAndView Index(HttpServletRequest request) throws Exception {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/virtualCaptualoperationList1");
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            filterSQL.append("and (fuser.floginName like '%" + keyWord
                    + "%' OR \n");
            filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
            filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
            filterSQL.append("withdraw_virtual_address like '%" + keyWord
                    + "%' OR \n");
            filterSQL.append("recharge_virtual_address like '%" + keyWord
                    + "%'");
            filterSQL.append("OR ftradeUniqueNumber like '%" + keyWord
                    + "%' )\n");
            modelAndView.addObject("keywords", keyWord);
        }

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
            }
            modelAndView.addObject("ftype", type);
        } else {
            modelAndView.addObject("ftype", 0);
        }
        filterSQL.append("and ftype=2\n");
        /*if (request.getParameter("oper_ftype") != null) {
            int oper_ftype = Integer.parseInt(request.getParameter("oper_ftype"));
			if (oper_ftype != 0) {
				filterSQL.append("and ftype=2\n");
			}
			modelAndView.addObject("oper_ftype", oper_ftype);
		} else {
			modelAndView.addObject("oper_ftype", 0);
		}*/

        if (request.getParameter("fstatus") != null) {
            int fstatus = Integer.parseInt(request.getParameter("fstatus"));
            if (fstatus != 0) {
                filterSQL.append("and fstatus=" + fstatus + "\n");
            }
            modelAndView.addObject("fstatus", fstatus);
        } else {
            modelAndView.addObject("fstatus", 0);
        }
        filterSQL.append("and fuser.fid is not null \n");

        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        } else {
            filterSQL.append("order by fid \n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        } else {
            filterSQL.append("desc \n");
        }

        List<Fvirtualcointype> type = this.virtualCoinService.findAll();
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(Long.valueOf(fvirtualcointype.getFid()), fvirtualcointype);
        }
        modelAndView.addObject("typeMap", typeMap);

        List<Fvirtualcaptualoperation> list = this.virtualCapitaloperationService
                .list((currentPage - 1) * numPerPage, numPerPage, filterSQL
                        + "", true);
        modelAndView.addObject("virtualCapitaloperationList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "virtualCapitaloperationList");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount(
                "Fvirtualcaptualoperation", filterSQL + ""));
        return modelAndView;
    }

    /**
     * 日提现统计表操作记录
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/ssadmin/virtualCaptualoperationTable")
    @RequiresPermissions("ssadmin/virtualCaptualoperationTable.html")
    public ModelAndView virtualCaptualoperationTable(HttpServletRequest request) throws Exception {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/virtualCaptualoperationTable");
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String notContainkeyWord = request.getParameter("notContainKeyWords");
        //把中文逗号替换成英文逗号
        if(notContainkeyWord!=null ){
            if(notContainkeyWord.contains("，")){
                notContainkeyWord=notContainkeyWord.replace("，",",");
            }
        }
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            filterSQL.append("and (fuser.floginName like '%" + keyWord
                    + "%' OR \n");
            filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
            filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
            filterSQL.append("withdraw_virtual_address like '%" + keyWord
                    + "%' OR \n");
            filterSQL.append("recharge_virtual_address like '%" + keyWord
                    + "%'");
            filterSQL.append("OR ftradeUniqueNumber like '%" + keyWord
                    + "%' )\n");
            modelAndView.addObject("keywords", keyWord);
        }

        if (notContainkeyWord != null && notContainkeyWord.trim().length() > 0) {
            String [] keys= notContainkeyWord.split(",");

            filterSQL.append("AND NOT EXISTS ( \n");
            filterSQL.append(" SELECT 1 FROM   fuser f  WHERE   (\n");
            filterSQL.append(" f.floginName IN ('"+notContainkeyWord+"')\n");
            filterSQL.append(" OR f.fnickName  IN  ('"+notContainkeyWord+"') \n");
            filterSQL.append(" OR f.frealName IN  ('"+notContainkeyWord+"')\n");
            filterSQL.append(" OR withdraw_virtual_address  IN ('"+notContainkeyWord+"')\n");
            filterSQL.append(" OR recharge_virtual_address   IN ('"+notContainkeyWord+"')\n");
            filterSQL.append(" OR ftradeUniqueNumber  IN ('"+notContainkeyWord+"')\n");
            filterSQL.append("  )\n");
            filterSQL.append("  AND f.fid =fvirtualcaptualoperation.FUs_fId2 \n");
            filterSQL.append("  )\n");
            modelAndView.addObject("notContainKeyWords", notContainkeyWord);
        }
        if (StringUtils.hasText(startDate)) {
            filterSQL.append("and DATE_FORMAT(Fvirtualcaptualoperation.flastUpdateTime, '%Y-%m-%d') >= ").append("'" + startDate + "' ");
            modelAndView.addObject("startDate", startDate);
        }
        if (StringUtils.hasText(endDate)) {
            filterSQL.append("and DATE_FORMAT(Fvirtualcaptualoperation.flastUpdateTime, '%Y-%m-%d') <= ").append("'" + endDate + "' ");
            modelAndView.addObject("endDate", endDate);
        }
        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
            }
            modelAndView.addObject("ftype", type);
        } else {
            modelAndView.addObject("ftype", 0);
        }
        //提现
        filterSQL.append("and ftype='"+VirtualCapitalOperationTypeEnum.COIN_OUT+"'\n");
        //提现成功
        filterSQL.append("and fvirtualcaptualoperation.fStatus='"+VirtualCapitalOperationOutStatusEnum.OperationSuccess+"'\n");

        filterSQL.append("and fuser.fid is not null \n");
        filterSQL.append("GROUP BY DATE_FORMAT(fvirtualcaptualoperation.flastUpdateTime,'%Y-%m-%d'),fvirtualcaptualoperation.fVi_fId2 \n");
        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        } else {
            filterSQL.append("ORDER BY Fvirtualcaptualoperation.flastUpdateTime  \n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        } else {
            filterSQL.append("desc \n");
        }

        List<Fvirtualcointype> type = this.virtualCoinService.findAll();
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(Long.valueOf(fvirtualcointype.getFid()), fvirtualcointype);
        }
        modelAndView.addObject("typeMap", typeMap);

        List<Map> list = this.virtualCapitaloperationService
                .sumUserCaptualoperation((currentPage - 1) * numPerPage, numPerPage, filterSQL
                        + "", true);
        modelAndView.addObject("virtualCaptualoperationTable", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "virtualCaptualoperationTable");

        String sql = "select  fuser.floginName ,fuser.fRealName as frealName ,fuser.fTelephone as ftelephone,fvirtualcaptualoperation.fStatus as fstatus_s,SUM(fvirtualcaptualoperation.fAmount) as famount ,\n" +
                " SUM(fvirtualcaptualoperation.ffees) as ffees,DATE_FORMAT(Fvirtualcaptualoperation.flastUpdateTime,'%Y-%m-%d') as flastUpdateTime ,fvirtualcointype.fName as fname from fvirtualcaptualoperation \n" +
                "LEFT JOIN fuser on fuser.fid=fvirtualcaptualoperation.FUs_fId2 \n " +
                "left join fvirtualcointype  on  fvirtualcointype.fId=fvirtualcaptualoperation.fVi_fId2\n"
                + filterSQL;
        // 总数量
        int count = this.virtualCapitaloperationService.findAllListCount(sql);
        modelAndView.addObject("totalCount",count);
        return modelAndView;
    }


    /**
     * 导出日提现统计表操作记录
     *
     * @param request
     * @param response
     */
    @RequestMapping("/ssadmin/exportVirtualCaptualoperationTable")
    @RequiresPermissions("ssadmin/exportVirtualCaptualoperationTable.html")
    public void exportVirtualCaptualoperationTable(HttpServletRequest request, HttpServletResponse response) {
        String keyWord = request.getParameter("keywords");
        String notContainkeyWord = request.getParameter("notContainKeyWords");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            filterSQL.append("and (fuser.floginName like '%" + keyWord
                    + "%' OR \n");
            filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
            filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
            filterSQL.append("withdraw_virtual_address like '%" + keyWord
                    + "%' OR \n");
            filterSQL.append("recharge_virtual_address like '%" + keyWord
                    + "%'");
            filterSQL.append("OR ftradeUniqueNumber like '%" + keyWord
                    + "%' )\n");
        }
        if (notContainkeyWord != null && notContainkeyWord.trim().length() > 0) {
            filterSQL.append("AND NOT EXISTS ( \n");
            filterSQL.append(" SELECT 1 FROM   fuser f  WHERE   (\n");
            filterSQL.append(" f.floginName IN ('"+notContainkeyWord+"')\n");
            filterSQL.append(" OR f.fnickName  IN  ('"+notContainkeyWord+"') \n");
            filterSQL.append(" OR f.frealName IN  ('"+notContainkeyWord+"')\n");
            filterSQL.append(" OR withdraw_virtual_address  IN ('"+notContainkeyWord+"')\n");
            filterSQL.append(" OR recharge_virtual_address   IN ('"+notContainkeyWord+"')\n");
            filterSQL.append(" OR ftradeUniqueNumber  IN ('"+notContainkeyWord+"')\n");
            filterSQL.append("  )\n");
            filterSQL.append("  AND f.fid = Fvirtualcaptualoperation.FUs_fId2 \n");
            filterSQL.append("  )\n");

        }
        if (StringUtils.hasText(startDate)) {
            filterSQL.append("and DATE_FORMAT(Fvirtualcaptualoperation.flastUpdateTime, '%Y-%m-%d') >= ").append("'" + startDate + "' ");
        }
        if (StringUtils.hasText(endDate)) {
            filterSQL.append("and DATE_FORMAT(Fvirtualcaptualoperation.flastUpdateTime, '%Y-%m-%d') <= ").append("'" + endDate + "' ");
        }
        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
            }
        }

        //提现
        filterSQL.append("and ftype='"+VirtualCapitalOperationTypeEnum.COIN_OUT+"'\n");
        //提现成功
        filterSQL.append("and fvirtualcaptualoperation.fStatus='"+VirtualCapitalOperationOutStatusEnum.OperationSuccess+"'\n");
        filterSQL.append("and fuser.fid is not null \n");
        filterSQL.append("GROUP BY DATE_FORMAT(fvirtualcaptualoperation.flastUpdateTime,'%Y-%m-%d'),fvirtualcaptualoperation.fVi_fId2 \n");
        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        } else {
            filterSQL.append("ORDER BY Fvirtualcaptualoperation.flastUpdateTime desc \n");
        }

        List<Fvirtualcointype> type = this.virtualCoinService.findAll();
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        typeMap.put(0, "全部");

        List<Map> list = this.virtualCapitaloperationService
                .sumUserCaptualoperation(0, 0, filterSQL
                        + "", false);
        // 标题
        XlsExport xls = new XlsExport();
        xls.createRow(0);
        xls.setCell(0, "数量");
        xls.setCell(1, "手续费");
        xls.setCell(2, "虚拟币类型");
        xls.setCell(3, "状态");
        xls.setCell(4, "时间");

        // 填入数据
        for (int i = 1, len = list.size(); i <= len; i++) {
            Map map = list.get(i - 1);
            xls.createRow(i);
            xls.setCell(0, map.get("famount") == null ? "" : map.get("famount").toString());
            xls.setCell(1, map.get("ffees") == null ? "" : map.get("ffees").toString());
            xls.setCell(2,  map.get("fname") == null ? "" : map.get("fname").toString());
            xls.setCell(3, map.get("fstatus_s") == null ? "" : Integer.parseInt(map.get("fstatus_s").toString())==  3 ? "提现成功": "" );
            xls.setCell(4, map.get("flastUpdateTime") == null ? "" : map.get("flastUpdateTime").toString());

        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("虚拟币日提现统计表-", "utf-8") + format.format(new Date()) + ".xls");
            xls.exportXls(response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出操作记录
     *
     * @param request
     * @param response
     */
    @RequestMapping("/ssadmin/exportVirtualCaptualoperationList")
    @RequiresPermissions("ssadmin/exportVirtualCaptualoperationList.html")
    public void exportVirtualCaptualoperationList(HttpServletRequest request, HttpServletResponse response) {
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            filterSQL.append("and (fuser.floginName like '%" + keyWord + "%' OR \n");
            filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
            filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
            filterSQL.append("withdraw_virtual_address like '%" + keyWord + "%' OR \n");
            filterSQL.append("recharge_virtual_address like '%" + keyWord + "%' )\n");
        }
        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
            }
        }

        filterSQL.append("and ftype=2\n");
        /*if (request.getParameter("oper_ftype") != null) {
            int type = Integer.parseInt(request.getParameter("oper_ftype"));
			if (type != 0) {
				filterSQL.append("and ftype=" + type + "\n");
			}
		}*/

        if (request.getParameter("fstatus") != null) {
            int fstatus = Integer.parseInt(request.getParameter("fstatus"));
            if (fstatus != 0) {
                filterSQL.append("and fstatus=" + fstatus + "\n");
            }
        }
        filterSQL.append("and fuser.fid is not null \n");

        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        } else {
            filterSQL.append("order by fcreateTime \n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        } else {
            filterSQL.append("desc \n");
        }

        List<Fvirtualcointype> type = this.virtualCoinService.findAll();
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        typeMap.put(0, "全部");
        List<Fvirtualcaptualoperation> list = this.virtualCapitaloperationService.list(0, 0, filterSQL + "", false);

        // 标题
        XlsExport xls = new XlsExport();
        xls.createRow(0);
        xls.setCell(0, "会员登录名");
        xls.setCell(1, "会员昵称");
        xls.setCell(2, "会员邮箱");
        xls.setCell(3, "会员手机号");
        xls.setCell(4, "会员真实姓名");
        xls.setCell(5, "虚拟币类型");
        xls.setCell(6, "状态");
        xls.setCell(7, "交易类型");
        xls.setCell(8, "数量");
        xls.setCell(9, "手续费");
        xls.setCell(10, "提现地址");
        xls.setCell(11, "充值地址");
        xls.setCell(12, "交易ID");
        xls.setCell(13, "创建时间");
        xls.setCell(14, "最后修改时间");

        // 填入数据
        for (int i = 1, len = list.size(); i <= len; i++) {
            Fvirtualcaptualoperation obj = list.get(i - 1);
            xls.createRow(i);
            xls.setCell(0, obj.getFuser().getFloginName());
            xls.setCell(1, obj.getFuser().getFnickName());
            xls.setCell(2, obj.getFuser().getFemail());
            xls.setCell(3, obj.getFuser().getFtelephone());
            xls.setCell(4, obj.getFuser().getFrealName());
            xls.setCell(5, obj.getFvirtualcointype().getFname());
            xls.setCell(6, obj.getFstatus_s());
            xls.setCell(7, obj.getFtype_s());
            xls.setCell(8, obj.getFamount());
            xls.setCell(9, obj.getFfees());
            xls.setCell(10, obj.getWithdraw_virtual_address());
            xls.setCell(11, obj.getRecharge_virtual_address());
            xls.setCell(12, obj.getFtradeUniqueNumber());
            xls.setCell(13, obj.getFcreateTime());
            xls.setCell(14, obj.getFlastUpdateTime());
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("虚拟币操作列表-", "utf-8") + format.format(new Date()) + ".xls");
            xls.exportXls(response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/ssadmin/virtualCapitalInList")
    @RequiresPermissions("ssadmin/virtualCapitalInList.html")
    public ModelAndView virtualCapitalInList(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/virtualCapitalInList");
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 and ftype="
                + VirtualCapitalOperationTypeEnum.COIN_IN + "\n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            filterSQL.append("and (fuser.floginName like '%" + keyWord
                    + "%' OR \n");
            filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
            filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
            filterSQL.append("withdraw_virtual_address like '%" + keyWord
                    + "%' OR \n");
            filterSQL.append("recharge_virtual_address like '%" + keyWord
                    + "%' )\n");
            modelAndView.addObject("keywords", keyWord);
        }

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
            }
            modelAndView.addObject("ftype", type);
        } else {
            modelAndView.addObject("ftype", 0);
        }

        filterSQL.append("and fuser.fid is not null \n");

        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + " \n");
        } else {
            filterSQL.append("order by fid \n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + " \n");
        } else {
            filterSQL.append("desc \n");
        }

        List<Fvirtualcointype> type = this.virtualCoinService.findAll();
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        typeMap.put(0, "全部");
        modelAndView.addObject("typeMap", typeMap);

        List<Fvirtualcaptualoperation> list = this.virtualCapitaloperationService
                .list((currentPage - 1) * numPerPage, numPerPage, filterSQL
                        + "", true);
        modelAndView.addObject("virtualCapitaloperationList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "virtualCapitalInList");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount(
                "Fvirtualcaptualoperation", filterSQL + ""));
        return modelAndView;
    }

    /**
     * 导出虚拟币充值记录
     *
     * @param request
     * @param response
     */
    @RequestMapping("/ssadmin/exportViCoinChargeList")
    @RequiresPermissions("ssadmin/exportViCoinChargeList.html")
    public void exportViCoinChargeList(HttpServletRequest request, HttpServletResponse response) {
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 and ftype="
                + VirtualCapitalOperationTypeEnum.COIN_IN + "\n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            filterSQL.append("and (fuser.floginName like '%" + keyWord
                    + "%' OR \n");
            filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
            filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
            filterSQL.append("withdraw_virtual_address like '%" + keyWord
                    + "%' OR \n");
            filterSQL.append("recharge_virtual_address like '%" + keyWord
                    + "%' )\n");
        }

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
            }
        }

		filterSQL.append("and fuser.fid is not null \n");

        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        }

        List<Fvirtualcointype> type = this.virtualCoinService.findAll();
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        typeMap.put(0, "全部");
        List<Fvirtualcaptualoperation> list = this.virtualCapitaloperationService.list(0, 0, filterSQL + "", false);

        // 标题
        XlsExport xls = new XlsExport();
        xls.createRow(0);
        xls.setCell(0, "会员登录名");
        xls.setCell(1, "会员昵称");
        xls.setCell(2, "会员邮箱");
        xls.setCell(3, "会员手机号");
        xls.setCell(4, "会员真实姓名");
        xls.setCell(5, "虚拟币类型");
        xls.setCell(6, "状态");
        xls.setCell(7, "交易类型");
        xls.setCell(8, "数量");
        xls.setCell(9, "手续费");
        xls.setCell(10, "充值地址");
        xls.setCell(11, "交易ID");
        xls.setCell(12, "创建时间");
        xls.setCell(13, "最后修改时间");

        // 填入数据
        for (int i = 1, len = list.size(); i <= len; i++) {
            Fvirtualcaptualoperation obj = list.get(i - 1);
            xls.createRow(i);
            xls.setCell(0, obj.getFuser().getFloginName());
            xls.setCell(1, obj.getFuser().getFnickName());
            xls.setCell(2, obj.getFuser().getFemail());
            xls.setCell(3, obj.getFuser().getFtelephone());
            xls.setCell(4, obj.getFuser().getFrealName());
            xls.setCell(5, obj.getFvirtualcointype().getFname());
            xls.setCell(6, obj.getFstatus_s());
            xls.setCell(7, obj.getFtype_s());
            xls.setCell(8, obj.getFamount());
            xls.setCell(9, obj.getFfees());
            xls.setCell(10, obj.getRecharge_virtual_address());
            xls.setCell(11, obj.getFtradeUniqueNumber());
            xls.setCell(12, obj.getFcreateTime());
            xls.setCell(13, obj.getFlastUpdateTime());
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("虚拟币充值列表-", "utf-8") + format.format(new Date()) + ".xls");
            xls.exportXls(response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    //日充值成功统计表
    @RequestMapping("/ssadmin/virtualCapitalInTableSum")
    @RequiresPermissions("ssadmin/virtualCapitalInTableSum.html")
    public ModelAndView virtualCapitalInTableSum(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/virtualCapitalInTableSum");

        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String notContainkeyWords=request.getParameter("notContainKeyWords");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String orderField = request.getParameter("orderField");

        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filterSQL = new StringBuffer();


        //充值
        filterSQL.append("where 1=1 and ftype="
                + VirtualCapitalOperationTypeEnum.COIN_IN + "\n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            filterSQL.append("and (fuser.floginName like '%" + keyWord
                    + "%' OR \n");
            filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
            filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
            filterSQL.append("withdraw_virtual_address like '%" + keyWord
                    + "%' OR \n");
            filterSQL.append("recharge_virtual_address like '%" + keyWord
                    + "%'");
            filterSQL.append("OR ftradeUniqueNumber like '%" + keyWord
                    + "%' )\n");
            modelAndView.addObject("keywords", keyWord);
        }

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
            }
            modelAndView.addObject("ftype", type);
        } else {
            modelAndView.addObject("ftype", 0);
        }
        //把中文逗号替换成英文逗号
        if(notContainkeyWords!=null ){
            if(notContainkeyWords.contains("，")){
                notContainkeyWords=notContainkeyWords.replace("，",",");
            }
        }
        if (notContainkeyWords != null && notContainkeyWords.trim().length() > 0) {
            filterSQL.append("AND NOT EXISTS ( \n");
            filterSQL.append(" SELECT 1 FROM   fuser f  WHERE   (\n");
            filterSQL.append(" f.floginName IN ('"+notContainkeyWords+"')\n");
            filterSQL.append(" OR f.fnickName  IN  ('"+notContainkeyWords+"') \n");
            filterSQL.append(" OR f.frealName IN  ('"+notContainkeyWords+"')\n");
            filterSQL.append(" OR withdraw_virtual_address  IN ('"+notContainkeyWords+"')\n");
            filterSQL.append(" OR recharge_virtual_address   IN ('"+notContainkeyWords+"')\n");
            filterSQL.append(" OR ftradeUniqueNumber  IN ('"+notContainkeyWords+"')\n");
            filterSQL.append("  )\n");
            filterSQL.append("  AND f.fid = fvirtualcaptualoperation.FUs_fId2 \n");
            filterSQL.append("  )\n");
            modelAndView.addObject("notContainKeyWords", notContainkeyWords);
        }
        if (StringUtils.hasText(startDate)) {
            filterSQL.append("and DATE_FORMAT(fvirtualcaptualoperation.flastUpdateTime, '%Y-%m-%d') >= ").append("'" + startDate + "' ");
            modelAndView.addObject("startDate", startDate);
        }
        if (StringUtils.hasText(endDate)) {
            filterSQL.append("and DATE_FORMAT(fvirtualcaptualoperation.flastUpdateTime, '%Y-%m-%d') <= ").append("'" + endDate + "' ");
            modelAndView.addObject("endDate", endDate);
        }
        //统计充值成功
        filterSQL.append("and fvirtualcaptualoperation.fstatus='"+VirtualCapitalOperationInStatusEnum.SUCCESS+"'\n");
        filterSQL.append("and fuser.fid is not null \n");
        filterSQL.append("GROUP BY DATE_FORMAT(fvirtualcaptualoperation.flastUpdateTime,'%Y-%m-%d'),fvirtualcaptualoperation.fVi_fId2 \n");
        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        } else {
            filterSQL.append("ORDER BY fvirtualcaptualoperation.flastUpdateTime  \n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        } else {
            filterSQL.append("desc \n");
        }
        List<Fvirtualcointype> type = this.virtualCoinService.findAll();
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(Long.valueOf(fvirtualcointype.getFid()), fvirtualcointype);
        }
        modelAndView.addObject("typeMap", typeMap);

        List<Map> list = this.virtualCapitaloperationService
                .sumUserCaptualoperation((currentPage - 1) * numPerPage, numPerPage, filterSQL
                        + "", true);
        modelAndView.addObject("virtualCapitalInTableSum", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "virtualCapitalInTableSum");
        // 总数量
        String sql = "select  fuser.floginName ,fuser.fRealName as frealName ,fuser.fTelephone as ftelephone,fvirtualcaptualoperation.fStatus as fstatus_s,SUM(fvirtualcaptualoperation.fAmount) as famount ,\n" +
                " SUM(fvirtualcaptualoperation.ffees) as ffees,DATE_FORMAT(Fvirtualcaptualoperation.flastUpdateTime,'%Y-%m-%d') as flastUpdateTime ,fvirtualcointype.fName as fname from fvirtualcaptualoperation \n" +
                "LEFT JOIN fuser on fuser.fid=fvirtualcaptualoperation.FUs_fId2 \n " +
                "left join fvirtualcointype  on  fvirtualcointype.fId=fvirtualcaptualoperation.fVi_fId2\n"
                + filterSQL;
        // 总数量
        int count = this.virtualCapitaloperationService.findAllListCount(sql);
        modelAndView.addObject("totalCount",count);
        return modelAndView;


    }


    /**
     * 导出日充值统计表操作记录
     *
     * @param request
     * @param response
     */
    @RequestMapping("/ssadmin/exportVirtualCapitalInTableSum")
    @RequiresPermissions("ssadmin/exportVirtualCapitalInTableSum.html")
    public void exportVirtualCapitalInTableSum(HttpServletRequest request, HttpServletResponse response) {
        String keyWord = request.getParameter("keywords");
        String notContainkeyWords=request.getParameter("notContainKeyWords");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 \n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            filterSQL.append("and (fuser.floginName like '%" + keyWord
                    + "%' OR \n");
            filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
            filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
            filterSQL.append("withdraw_virtual_address like '%" + keyWord
                    + "%' OR \n");
            filterSQL.append("recharge_virtual_address like '%" + keyWord
                    + "%'");
            filterSQL.append("OR ftradeUniqueNumber like '%" + keyWord
                    + "%' )\n");
        }

        //把中文逗号替换成英文逗号
        if(notContainkeyWords!=null ){
            if(notContainkeyWords.contains("，")){
                notContainkeyWords=notContainkeyWords.replace("，",",");
            }
        }
        if (notContainkeyWords != null && notContainkeyWords.trim().length() > 0) {
            filterSQL.append("AND NOT EXISTS ( \n");
            filterSQL.append(" SELECT 1 FROM   fuser f  WHERE   (\n");
            filterSQL.append(" f.floginName IN ('"+notContainkeyWords+"')\n");
            filterSQL.append(" OR f.fnickName  IN  ('"+notContainkeyWords+"') \n");
            filterSQL.append(" OR f.frealName IN  ('"+notContainkeyWords+"')\n");
            filterSQL.append(" OR withdraw_virtual_address  IN ('"+notContainkeyWords+"')\n");
            filterSQL.append(" OR recharge_virtual_address   IN ('"+notContainkeyWords+"')\n");
            filterSQL.append(" OR ftradeUniqueNumber  IN ('"+notContainkeyWords+"')\n");
            filterSQL.append("  )\n");
            filterSQL.append("  AND f.fid = fvirtualcaptualoperation.FUs_fId2 \n");
            filterSQL.append("  )\n");

        }
        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
            }
        }
        if (StringUtils.hasText(startDate)) {
            filterSQL.append("and DATE_FORMAT(fvirtualcaptualoperation.flastUpdateTime, '%Y-%m-%d') >= ").append("'" + startDate + "' ");

        }
        if (StringUtils.hasText(endDate)) {
            filterSQL.append("and DATE_FORMAT(fvirtualcaptualoperation.flastUpdateTime, '%Y-%m-%d') <= ").append("'" + endDate + "' ");
        }

    /*	if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
			}
		}*/
        //充值
        filterSQL.append("and ftype=" + VirtualCapitalOperationTypeEnum.COIN_IN + "\n");
        //统计充值成功
        filterSQL.append("and fvirtualcaptualoperation.fstatus='"+VirtualCapitalOperationInStatusEnum.SUCCESS+"'\n");
        filterSQL.append(" and fuser.fid is not null \n");
        filterSQL.append("GROUP BY DATE_FORMAT(fvirtualcaptualoperation.flastUpdateTime,'%Y-%m-%d'),fvirtualcaptualoperation.fVi_fId2 \n");
        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        } else {
            filterSQL.append("ORDER BY fvirtualcaptualoperation.fCreateTime desc \n");
        }

        List<Fvirtualcointype> type = this.virtualCoinService.findAll();
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        typeMap.put(0, "全部");

        List<Map> list = this.virtualCapitaloperationService
                .sumUserCaptualoperation(0, 0, filterSQL
                        + "", false);
        // 标题
        XlsExport xls = new XlsExport();
        xls.createRow(0);

        xls.setCell(0, "数量");
      /*  xls.setCell(1, "手续费");*/
        xls.setCell(1, "虚拟币类型");
        xls.setCell(2, "状态");
        xls.setCell(3, "最后修改时间");
      /*  Fvirtualcaptualoperation f=new Fvirtualcaptualoperation();
        f.setFtype(VirtualCapitalOperationTypeEnum.COIN_IN);
        f.setFstatus( map.get("fstatus").toString());*/
        // 填入数据
        for (int i = 1, len = list.size(); i <= len; i++) {
            Map map = list.get(i - 1);
            xls.createRow(i);
            xls.setCell(0, map.get("famount") == null ? "" : map.get("famount").toString());
         /*   xls.setCell(1, map.get("ffees") == null ? "" : map.get("ffees").toString());*/
            xls.setCell(1,  map.get("fname") == null ? "" : map.get("fname").toString());
            xls.setCell(2, map.get("fstatus_s") == null ? "" : Integer.parseInt(map.get("fstatus_s").toString())==  3 ? "充值成功": "" );
            xls.setCell(3, map.get("flastUpdateTime") == null ? "" : map.get("flastUpdateTime").toString());

        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("虚拟币日充值统计表-", "utf-8") + format.format(new Date()) + ".xls");
            xls.exportXls(response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/ssadmin/exportViCoinWithdrawList")
    @RequiresPermissions("ssadmin/exportViCoinWithdrawList.html")
    public void exportViCoinWithdrawList(HttpServletRequest request, HttpServletResponse response) {
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 and ftype="
                + VirtualCapitalOperationTypeEnum.COIN_OUT + "\n");
        filterSQL.append("and fstatus IN("
                + VirtualCapitalOperationOutStatusEnum.WaitForOperation + ","
                + VirtualCapitalOperationOutStatusEnum.OperationLock + ")\n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            filterSQL.append("and (fuser.floginName like '%" + keyWord
                    + "%' OR \n");
            filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
            filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
            filterSQL.append("withdraw_virtual_address like '%" + keyWord
                    + "%' OR \n");
            filterSQL.append("recharge_virtual_address like '%" + keyWord
                    + "%' )\n");
        }

        if (request.getParameter("ftype") != null) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
            }
        }

        filterSQL.append("and fuser.fid is not null \n");

        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + "\n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + "\n");
        }

        List<Fvirtualcointype> type = this.virtualCoinService.findAll();
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        typeMap.put(0, "全部");
        List<Fvirtualcaptualoperation> list = this.virtualCapitaloperationService.list(0, 0, filterSQL + "", false);

        // 标题
        XlsExport xls = new XlsExport();
        xls.createRow(0);
        xls.setCell(0, "会员登录名");
        xls.setCell(1, "会员昵称");
        xls.setCell(2, "会员邮箱");
        xls.setCell(3, "会员手机号");
        xls.setCell(4, "会员真实姓名");
        xls.setCell(5, "虚拟币类型");
        xls.setCell(6, "状态");
        xls.setCell(7, "交易类型");
        xls.setCell(8, "数量");
        xls.setCell(9, "手续费");
        xls.setCell(10, "提现地址");
        xls.setCell(11, "交易ID");
        xls.setCell(12, "创建时间");
        xls.setCell(13, "最后修改时间");

        // 填入数据
        for (int i = 1, len = list.size(); i <= len; i++) {
            Fvirtualcaptualoperation obj = list.get(i - 1);
            xls.createRow(i);
            xls.setCell(0, obj.getFuser().getFloginName());
            xls.setCell(1, obj.getFuser().getFnickName());
            xls.setCell(2, obj.getFuser().getFemail());
            xls.setCell(3, obj.getFuser().getFtelephone());
            xls.setCell(4, obj.getFuser().getFrealName());
            xls.setCell(5, obj.getFvirtualcointype().getFname());
            xls.setCell(6, obj.getFstatus_s());
            xls.setCell(7, obj.getFtype_s());
            xls.setCell(8, obj.getFamount());
            xls.setCell(9, obj.getFfees());
            xls.setCell(10, obj.getWithdraw_virtual_address());
            xls.setCell(11, obj.getFtradeUniqueNumber());
            xls.setCell(12, obj.getFcreateTime());
            xls.setCell(13, obj.getFlastUpdateTime());
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("虚拟币提现列表-", "utf-8") + format.format(new Date()) + ".xls");
            xls.exportXls(response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/ssadmin/virtualCapitalOutList")
    @RequiresPermissions("ssadmin/virtualCapitalOutList.html")
    public ModelAndView virtualCapitalOutList(HttpServletRequest request) throws Exception {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/virtualCapitalOutList");
        // 当前页
        int currentPage = 1;
        // 搜索关键字
        String keyWord = request.getParameter("keywords");
        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");
        if (request.getParameter("pageNum") != null) {
            currentPage = Integer.parseInt(request.getParameter("pageNum"));
        }
        StringBuffer filterSQL = new StringBuffer();
        filterSQL.append("where 1=1 and ftype="
                + VirtualCapitalOperationTypeEnum.COIN_OUT + "\n");
        filterSQL.append("and fstatus IN("
                + VirtualCapitalOperationOutStatusEnum.WaitForOperation + ","
                + VirtualCapitalOperationOutStatusEnum.OperationLock + ")\n");
        if (keyWord != null && keyWord.trim().length() > 0) {
            filterSQL.append("and (fuser.floginName like '%" + keyWord
                    + "%' OR \n");
            filterSQL.append("fuser.fnickName like '%" + keyWord + "%' OR \n");
            filterSQL.append("fuser.frealName like '%" + keyWord + "%' OR \n");
            filterSQL.append("withdraw_virtual_address like '%" + keyWord
                    + "%' OR \n");
            filterSQL.append("recharge_virtual_address like '%" + keyWord
                    + "%' )\n");
            modelAndView.addObject("keywords", keyWord);
        }

        if (StringUtils.hasText(request.getParameter("ftype"))) {
            int type = Integer.parseInt(request.getParameter("ftype"));
            if (type != 0) {
                filterSQL.append("and fvirtualcointype.fid=" + type + "\n");
            }
            modelAndView.addObject("ftype", type);
        } else {
            modelAndView.addObject("ftype", 0);
        }
        if (orderField != null && orderField.trim().length() > 0) {
            filterSQL.append("order by " + orderField + " \n");
        } else {
            filterSQL.append("order by fid \n");
        }
        if (orderDirection != null && orderDirection.trim().length() > 0) {
            filterSQL.append(orderDirection + " \n");
        } else {
            filterSQL.append("desc \n");
        }

        List<Fvirtualcointype> type = this.virtualCoinService.findAll();
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : type) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
        }
        typeMap.put(0, "全部");
        modelAndView.addObject("typeMap", typeMap);

        List<Fvirtualcaptualoperation> list = this.virtualCapitaloperationService
                .list((currentPage - 1) * numPerPage, numPerPage, filterSQL
                        + "", true);
        modelAndView.addObject("virtualCapitaloperationList", list);
        modelAndView.addObject("numPerPage", numPerPage);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("rel", "virtualCapitalOutList");
        // 总数量
        modelAndView.addObject("totalCount", this.adminService.getAllCount(
                "Fvirtualcaptualoperation", filterSQL + ""));
        return modelAndView;
    }

    @RequestMapping("ssadmin/goVirtualCapitaloperationChangeStatus")
    @RequiresPermissions({"ssadmin/goVirtualCapitaloperationChangeStatus.html?type=1", "ssadmin/goVirtualCapitaloperationChangeStatus.html?type=2", "ssadmin/goVirtualCapitaloperationChangeStatus.html?type=3"})
    public ModelAndView goVirtualCapitaloperationChangeStatus(
            @RequestParam(required = true) int type,
            @RequestParam(required = true) int uid) throws Exception {


        ModelAndView modelAndView = new ModelAndView();
        Fvirtualcaptualoperation fvirtualcaptualoperation = this.virtualCapitaloperationService
                .findById(uid);
        fvirtualcaptualoperation.setFlastUpdateTime(Utils.getTimestamp());

        int userId = fvirtualcaptualoperation.getFuser().getFid();
        Fvirtualcointype fvirtualcointype = fvirtualcaptualoperation
                .getFvirtualcointype();
        int coinTypeId = fvirtualcointype.getFid();
        List<Fvirtualwallet> virtualWallet = this.virtualWalletService
                .findByTwoProperty("fuser.fid", userId, "fvirtualcointype.fid",
                        coinTypeId);
        if (virtualWallet == null || virtualWallet.size() == 0) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "审核失败，会员虚拟币钱包信息异常!");
            return modelAndView;
        }

        int status = fvirtualcaptualoperation.getFstatus();
        String tips = "";
        switch (type) {
            case 1:
                tips = "锁定";
                if (status != CapitalOperationOutStatus.WaitForOperation) {
                    modelAndView.setViewName("ssadmin/comm/ajaxDone");
                    modelAndView.addObject("statusCode", 300);
                    String status_s = CapitalOperationOutStatus
                            .getEnumString(CapitalOperationOutStatus.WaitForOperation);
                    modelAndView.addObject("message", "锁定失败,只有状态为:‘" + status_s
                            + "’的充值记录才允许锁定!");
                    return modelAndView;
                }
                fvirtualcaptualoperation
                        .setFstatus(VirtualCapitalOperationOutStatusEnum.OperationLock);
                break;
            case 2:
                tips = "取消锁定";
                if (status != CapitalOperationOutStatus.OperationLock) {
                    modelAndView.setViewName("ssadmin/comm/ajaxDone");
                    modelAndView.addObject("statusCode", 300);
                    String status_s = CapitalOperationOutStatus
                            .getEnumString(CapitalOperationOutStatus.OperationLock);
                    modelAndView.addObject("message", "取消锁定失败,只有状态为:‘" + status_s
                            + "’的充值记录才允许取消锁定!");
                    return modelAndView;
                }
                fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationOutStatusEnum.WaitForOperation);
                break;
            case 3:
                tips = "取消提现";
                if (status == CapitalOperationOutStatus.Cancel) {
                    modelAndView.setViewName("ssadmin/comm/ajaxDone");
                    modelAndView.addObject("statusCode", 300);
                    modelAndView.addObject("message", "取消提现失败,该记录已处于取消状态!");
                    return modelAndView;
                }
                fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationOutStatusEnum.Cancel);
                break;
        }

        boolean flag = false;
        try {
            if (VirtualCapitalOperationOutStatusEnum.Cancel == fvirtualcaptualoperation.getFstatus()) {
                this.frontAccountService.updateCancelWithdrawBtc(fvirtualcaptualoperation);
            } else {
                this.virtualCapitaloperationService.updateObj(fvirtualcaptualoperation);
            }
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (flag) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 200);
            modelAndView.addObject("message", tips + "成功！");
        } else {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "未知错误，请刷新列表后重试！");
        }

        return modelAndView;
    }

    @RequestMapping("ssadmin/goVirtualCapitaloperationJSP")
    public ModelAndView goVirtualCapitaloperationJSP(HttpServletRequest request) throws Exception {

        String url = request.getParameter("url");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(url);
        String xid = "";
        BTCMessage msg = new BTCMessage();
        if (request.getParameter("uid") != null) {
            int fid = Integer.parseInt(request.getParameter("uid"));
            Fvirtualcaptualoperation virtualcaptualoperation = this.virtualCapitaloperationService
                    .findById(fid);
            xid = virtualcaptualoperation.getFtradeUniqueNumber();
            msg.setACCESS_KEY(virtualcaptualoperation.getFvirtualcointype()
                    .getFaccess_key());
            msg.setIP(virtualcaptualoperation.getFvirtualcointype().getFip());
            msg.setPORT(virtualcaptualoperation.getFvirtualcointype()
                    .getFport());
            msg.setSECRET_KEY(virtualcaptualoperation.getFvirtualcointype()
                    .getFsecrt_key());
            modelAndView.addObject("virtualCapitaloperation",
                    virtualcaptualoperation);
        }
        if (request.getParameter("type") != null
                && request.getParameter("type").equals("ViewStatus")) {
            BTCUtils btc = new BTCUtils(msg);
            BTCInfo btcInfo = btc.gettransactionValue(xid, "send");
            modelAndView.addObject("confirmations", btcInfo.getConfirmations());
        }
        return modelAndView;
    }

    @RequestMapping("ssadmin/reVirtualCapitalOutAudit")
    @RequiresPermissions("ssadmin/reVirtualCapitalOutAudit.html")
    public ModelAndView reVirtualCapitalOutAudit(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fvirtualcaptualoperation virtualcaptualoperation = this.virtualCapitaloperationService
                .findById(fid);
        int status = virtualcaptualoperation.getFstatus();
        if (status != VirtualCapitalOperationOutStatusEnum.OperationSuccess) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            String status_s = VirtualCapitalOperationOutStatusEnum
                    .getEnumString(VirtualCapitalOperationOutStatusEnum.OperationSuccess);
            modelAndView.addObject("message", "重新审核失败,只有状态为:" + status_s
                    + "的提现记录才允许重新审核!");
            return modelAndView;
        }
        int userId = virtualcaptualoperation.getFuser().getFid();

        Fvirtualcointype fvirtualcointype = virtualcaptualoperation
                .getFvirtualcointype();
        int coinTypeId = fvirtualcointype.getFid();
        List<Fvirtualwallet> virtualWallet = this.virtualWalletService
                .findByTwoProperty("fuser.fid", userId, "fvirtualcointype.fid", coinTypeId);

        Fvirtualwallet virtualWalletInfo = virtualWallet.get(0);
        double amount = Utils.getDouble(virtualcaptualoperation.getFamount() - virtualcaptualoperation.getFfees(), 4);

        BTCMessage btcMsg = new BTCMessage();
        btcMsg.setACCESS_KEY(fvirtualcointype.getFaccess_key());
        btcMsg.setSECRET_KEY(fvirtualcointype.getFsecrt_key());
        btcMsg.setIP(fvirtualcointype.getFip());
        btcMsg.setPASSWORD(request.getParameter("fpassword"));
        btcMsg.setPORT(fvirtualcointype.getFport());
        BTCUtils btcUtils = new BTCUtils(btcMsg);
        try {
            double balance = btcUtils.getbalanceValue();
            if (balance < amount) {
                modelAndView.setViewName("ssadmin/comm/ajaxDone");
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "重新审核失败，钱包余额：" + balance + "小于"
                        + amount);
                return modelAndView;
            }
        } catch (Exception e1) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "重新审核失败，钱包连接失败");
            return modelAndView;
        }
        virtualcaptualoperation.setFlastUpdateTime(Utils.getTimestamp());
        String errMsg = "重新审核失败";
        boolean flag = false;
        try {
            this.virtualCapitaloperationService.updateCapitalRe(virtualcaptualoperation, btcUtils);
            flag = true;
        } catch (Exception e) {
            errMsg = e.getMessage();
            System.out.println("error " + e);
        }
        if (!flag) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", errMsg);
            return modelAndView;
        }
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "重新审核成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;

    }

    @RequestMapping("ssadmin/virtualCapitalOutAudit")
    @RequiresPermissions("ssadmin/virtualCapitalOutAudit.html")
    public ModelAndView virtualCapitalOutAudit(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        int fid = Integer.parseInt(request.getParameter("uid"));
        Fvirtualcaptualoperation virtualcaptualoperation = this.virtualCapitaloperationService
                .findById(fid);
        int status = virtualcaptualoperation.getFstatus();
        if (status != VirtualCapitalOperationOutStatusEnum.OperationLock) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            String status_s = VirtualCapitalOperationOutStatusEnum
                    .getEnumString(VirtualCapitalOperationOutStatusEnum.OperationLock);
            modelAndView.addObject("message", "审核失败,只有状态为:" + status_s
                    + "的提现记录才允许审核!");
            return modelAndView;
        }
        // 根据用户查钱包最后修改时间
        int userId = virtualcaptualoperation.getFuser().getFid();
        Fvirtualcointype fvirtualcointype = virtualcaptualoperation
                .getFvirtualcointype();
        int coinTypeId = fvirtualcointype.getFid();
        List<Fvirtualwallet> virtualWallet = this.virtualWalletService
                .findByTwoProperty("fuser.fid", userId, "fvirtualcointype.fid", coinTypeId);
        if (virtualWallet == null || virtualWallet.size() == 0) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "审核失败，会员虚拟币钱包信息异常!");
            return modelAndView;
        }

        Fvirtualwallet virtualWalletInfo = virtualWallet.get(0);
        double amount = Utils.getDouble(virtualcaptualoperation.getFamount() - virtualcaptualoperation.getFfees(), 4);
//		double frozenRmb = Utils.getDouble(virtualWalletInfo.getFfrozen(), 4);
//		if (frozenRmb < amount) {
//			modelAndView.setViewName("ssadmin/comm/ajaxDone");
//			modelAndView.addObject("statusCode", 300);
//			modelAndView.addObject("message", "审核失败,冻结数量:" + frozenRmb
//					+ "小于提现数量:" + amount + "，操作异常!");
//			return modelAndView;
//		}
        BTCMessage btcMsg = new BTCMessage();
        btcMsg.setACCESS_KEY(fvirtualcointype.getFaccess_key());
        btcMsg.setSECRET_KEY(fvirtualcointype.getFsecrt_key());
        btcMsg.setIP(fvirtualcointype.getFip());
        btcMsg.setPASSWORD(request.getParameter("fpassword"));
        btcMsg.setPORT(fvirtualcointype.getFport());
        BTCUtils btcUtils = new BTCUtils(btcMsg);

        try {
            double balance = btcUtils.getbalanceValue();
            if (balance < amount) {
                modelAndView.setViewName("ssadmin/comm/ajaxDone");
                modelAndView.addObject("statusCode", 300);
                modelAndView.addObject("message", "审核失败，钱包余额：" + balance + "小于"
                        + amount);
                return modelAndView;
            }
        } catch (Exception e1) {
            System.out.println("error 1 " + e1);
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "审核失败，钱包连接失败");
            return modelAndView;
        }

        // 充值操作
        virtualcaptualoperation.setFstatus(VirtualCapitalOperationOutStatusEnum.OperationSuccess);
        virtualcaptualoperation.setFlastUpdateTime(Utils.getTimestamp());
        boolean flag = false;
        String errMsg = "审核失败";
        try {
            this.virtualCapitaloperationService.updateCapital(virtualcaptualoperation, btcUtils);
            flag = true;
        } catch (Exception e) {
            errMsg = e.getMessage();
            System.out.println("error " + e);
            flag = false;
        }
        if (!flag) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", errMsg);
            return modelAndView;
        }
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "审核成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }


    @RequestMapping("ssadmin/virtualCapitalOutAuditTrade")
    @RequiresPermissions("ssadmin/virtualCapitalOutAuditTrade.html")
    public ModelAndView virtualCapitalOutAuditTrade(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        int fid = Integer.parseInt(request.getParameter("uid"));
        String tradeNo = request.getParameter("tradeNo");
        Fvirtualcaptualoperation virtualcaptualoperation = this.virtualCapitaloperationService
                .findById(fid);
        int status = virtualcaptualoperation.getFstatus();
        if (status != VirtualCapitalOperationOutStatusEnum.OperationLock) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            String status_s = VirtualCapitalOperationOutStatusEnum
                    .getEnumString(VirtualCapitalOperationOutStatusEnum.OperationLock);
            modelAndView.addObject("message", "修改状态失败,只有状态为:" + status_s
                    + "的提现记录才允许审核!");
            return modelAndView;
        }
        // 根据用户查钱包最后修改时间
        int userId = virtualcaptualoperation.getFuser().getFid();
        Fvirtualcointype fvirtualcointype = virtualcaptualoperation
                .getFvirtualcointype();
        int coinTypeId = fvirtualcointype.getFid();
        List<Fvirtualwallet> virtualWallet = this.virtualWalletService
                .findByTwoProperty("fuser.fid", userId, "fvirtualcointype.fid", coinTypeId);
        if (virtualWallet == null || virtualWallet.size() == 0) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "修改状态失败，会员虚拟币钱包信息异常!");
            return modelAndView;
        }
        Fvirtualwallet virtualWalletInfo = virtualWallet.get(0);
        double amount = Utils.getDouble(virtualcaptualoperation.getFamount() + virtualcaptualoperation.getFfees(), 4);
        // 充值操作
        virtualcaptualoperation
                .setFstatus(VirtualCapitalOperationOutStatusEnum.OperationSuccess);
        virtualcaptualoperation.setFlastUpdateTime(Utils.getTimestamp());
        virtualcaptualoperation.setVersion(virtualcaptualoperation.getVersion() + 1);
        virtualcaptualoperation.setFtradeUniqueNumber(tradeNo);

        // 钱包操作
        virtualWalletInfo.setFlastUpdateTime(Utils.getTimestamp());
        virtualWalletInfo.setFfrozen(MathUtils.subtract(virtualWalletInfo.getFfrozen(), amount));
        boolean flag = false;
        try {
            this.virtualCapitaloperationService.updateOperateAndWallet(virtualcaptualoperation, virtualWalletInfo);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!flag) {
            modelAndView.setViewName("ssadmin/comm/ajaxDone");
            modelAndView.addObject("statusCode", 300);
            modelAndView.addObject("message", "修改状态失败，数据库操作失败");
            return modelAndView;
        }
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.addObject("statusCode", 200);
        modelAndView.addObject("message", "审核成功");
        modelAndView.addObject("callbackType", "closeCurrent");
        return modelAndView;
    }

    /**
     * 钱包信息
     *
     * @param request
     * @return
     */
    @RequestMapping("ssadmin/walletReport")
    @RequiresPermissions("ssadmin/walletReport.html")
    public ModelAndView walletReport(HttpServletRequest request,
                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date curDate) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.setViewName("ssadmin/walletReport");

        if (null == curDate) {
            curDate = new Date();
        }

        if (DateUtils.isSameDate(curDate, new Date())) {
            // 全站总人民币
//			Map walletMap = this.walletService.getTotalMoney();
//			modelAndView.addObject("wallet", walletMap);

            // 全站总币数量
            List<Map> virtualQtyList = this.virtualWalletService.getTotalQty();
            modelAndView.addObject("virtualQtyList", virtualQtyList);
        } else {
            List<DateWalletStatistic> list = dateWalletStatisticService.findByDate(curDate);

            HashMap coinMap = null;
            Fvirtualcointype fvirtualcointype;
            List virtualQtyList = new ArrayList<>(list.size());
            for (DateWalletStatistic log : list) {
                if (log.isRMB()) {
                    Map walletMap = new HashMap<>();
                    walletMap.put("totalRmb", log.getTotalBalance());
                    walletMap.put("frozenRmb", log.getTotalFreeze());
                    modelAndView.addObject("wallet", walletMap);
                } else {
                    fvirtualcointype = virtualCoinService.findById(log.getCoinType());
                    if (null == coinMap) {
                        coinMap = new HashMap();
                    } else if (!coinMap.isEmpty()) {
                        coinMap = (HashMap) coinMap.clone();
                    }
                    coinMap.put("fName", fvirtualcointype.getFname());
                    coinMap.put("totalQty", log.getTotalBalance());
                    coinMap.put("frozenQty", log.getTotalFreeze());
                    virtualQtyList.add(coinMap);
                }
            }
            modelAndView.addObject("virtualQtyList", virtualQtyList);
        }

        modelAndView.addObject("curDate", curDate);
        modelAndView.addObject("rel", "walletReport");
        return modelAndView;
    }

    /**
     * 当前委托
     *
     * @param request
     * @return
     */
    @RequestMapping("ssadmin/curEntrustReport")
    @RequiresPermissions("ssadmin/curEntrustReport.html")
    public ModelAndView curEntrustReport(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.setViewName("ssadmin/curEntrustReport");

        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String isAll = request.getParameter("isAll");
        int type = StringUtils.hasText(request.getParameter("type")) ? Integer.valueOf(request.getParameter("type")) : EntrustTypeEnum.BUY;

        if (StringUtils.hasText(isAll)) {
            startDate = null;
            endDate = null;
        } else if (!StringUtils.hasText(startDate) && !StringUtils.hasText(endDate)) {
            endDate = DateUtils.formatDate(new Date());
            startDate = DateUtils.formatDate(DateUtils.getDaysBefore(30));
        }

        // 当前委托卖出
        List entrustSellMap = this.entrustService.getTotalQty(type, startDate, endDate);
        modelAndView.addObject("entrustMap", entrustSellMap);

        modelAndView.addObject("startDate", startDate);
        modelAndView.addObject("endDate", endDate);
        modelAndView.addObject("isAll", isAll);
        modelAndView.addObject("type", type);
        modelAndView.addObject("rel", "curEntrustReport");
        return modelAndView;
    }

    @RequestMapping("ssadmin/totalReport")
    @RequiresPermissions("ssadmin/totalReport.html")
    public ModelAndView totalReport(HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        modelAndView.setViewName("ssadmin/totalReport");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 总会员数
        modelAndView.addObject("totalUser",
                this.adminService.getAllCount("Fuser", ""));
        // 总认证数
        modelAndView.addObject("totalValidateUser", this.adminService.getAllCount("Fuser", "where fhasRealValidate=1"));

        // 今天注册数
        modelAndView.addObject("todayTotalUser", this.adminService.getAllCount("Fuser", "where date_format(fregisterTime,'%Y-%m-%d')=date_format(now(),'%Y-%m-%d')"));
        // 今天认证数
        modelAndView.addObject("todayValidateUser", this.adminService.getAllCount("Fuser", "where date_format(fhasRealValidateTime,'%Y-%m-%d')=date_format(now(),'%Y-%m-%d')"));

        // 今日充值虚拟币总数量
        List virtualInMap = this.virtualCapitaloperationService.getTotalAmount(
                VirtualCapitalOperationTypeEnum.COIN_IN, "("
                        + VirtualCapitalOperationInStatusEnum.SUCCESS + ")",
                true);
        modelAndView.addObject("virtualInMap", virtualInMap);

        // 今日提现虚拟币
        List virtualOutSuccessMap = this.virtualCapitaloperationService
                .getTotalAmount(VirtualCapitalOperationTypeEnum.COIN_OUT, "("
                        + VirtualCapitalOperationOutStatusEnum.OperationSuccess
                        + ")", true);
        modelAndView.addObject("virtualOutSuccessMap", virtualOutSuccessMap);

        // 充值虚拟币总数量
        List virtualInAllMap = this.virtualCapitaloperationService.getTotalAmount(
                VirtualCapitalOperationTypeEnum.COIN_IN, "("
                        + VirtualCapitalOperationInStatusEnum.SUCCESS + ")",
                false);
        modelAndView.addObject("virtualInAllMap", virtualInAllMap);

        // 提现虚拟币总数量
        List virtualOutAllSuccessMap = this.virtualCapitaloperationService
                .getTotalAmount(VirtualCapitalOperationTypeEnum.COIN_OUT, "("
                        + VirtualCapitalOperationOutStatusEnum.OperationSuccess
                        + ")", false);
        modelAndView.addObject("virtualOutAllSuccessMap", virtualOutAllSuccessMap);

        modelAndView.addObject("rel", "totalReport");
        return modelAndView;
    }
}
