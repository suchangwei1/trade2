package com.trade.interceptor;

import com.alibaba.fastjson.JSON;
import com.trade.dto.ResultBean;
import com.trade.util.Constants;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class UserLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!isLogin(request)){
            PrintWriter out = response.getWriter();
            response.setContentType("application/json;charset=utf-8");
            out.write(JSON.toJSONString(ResultBean.E401));
            return false;
        }
        return true;
    }

    private boolean isLogin(HttpServletRequest request){
        return null != request.getSession().getAttribute(Constants.USER_LOGIN_SESSION);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
