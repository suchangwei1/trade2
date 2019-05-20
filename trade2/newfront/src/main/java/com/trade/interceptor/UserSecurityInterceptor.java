package com.trade.interceptor;

import com.alibaba.fastjson.JSON;
import com.trade.dto.ResultBean;
import com.trade.model.Fuser;
import com.trade.util.Constants;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class UserSecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Fuser fuser = (Fuser) request.getSession().getAttribute(Constants.USER_LOGIN_SESSION);
        /*if(!fuser.getFpostRealValidate()){
            // 未提交实名认证
            PrintWriter out = response.getWriter();
            response.setContentType("application/json;charset=utf-8");
            out.write(JSON.toJSONString(ResultBean.E402));
            return false;
        }*/

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
