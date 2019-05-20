package com.trade.api.controller;

import com.trade.api.APIResultCode;
import com.trade.model.Fuser;
import com.trade.service.front.FrontUserService;
import com.trade.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class ApiBaseController {
    @Autowired(required = false)
    private HttpServletRequest request;
    @Autowired(required = false)
    private HttpServletResponse response;
    @Autowired
    private FrontUserService frontUserService;

    protected Fuser getDbFuser(){
        Fuser fuser = (Fuser) request.getAttribute(Constants.USER_LOGIN_SESSION);
        if(null == fuser){
            Integer userId = (Integer) request.getAttribute(Constants.USER_ID);
            fuser = frontUserService.findById(userId);
            request.setAttribute(Constants.USER_LOGIN_SESSION, fuser);
        }

        return fuser;
    }

    protected Fuser getFuser(){
        int userId = (int) request.getAttribute(Constants.USER_ID);
        Fuser fuser = new Fuser();
        fuser.setFid(userId);
        return fuser;
    }

    protected int getUserId() {
        return (int) request.getAttribute(Constants.USER_ID);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    protected Object forSuccessResult(Object data){
        Map map = new HashMap<>();
        map.put("code", APIResultCode.Code_200.getCode());
        map.put("data", data);
        return map;
    }

    protected Object forSuccessResult(){
        Map map = new HashMap<>();
        map.put("code", APIResultCode.Code_200.getCode());
        return map;
    }

    protected Object forFailureResult(APIResultCode resultCode, Object data){
        Map map = new HashMap<>();
        map.put("code", resultCode.getCode());
        map.put("data", data);
        return map;
    }

    protected Object forFailureResult(APIResultCode resultCode){
        return forFailureResult(resultCode, null);
    }

    /**
     * api异常
     *
     * @return
     */
    @ExceptionHandler(Exception.class)
    protected Object exceptionHandler(Exception e){
        e.printStackTrace();
        return forFailureResult(APIResultCode.Code_402);
    }

}
