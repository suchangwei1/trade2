package com.trade.controller.admin;

import com.trade.model.Fadmin;
import com.trade.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class BaseController {
    @Autowired(required = false)
    private HttpServletRequest request;

    public HttpServletRequest getRequest() {
        return this.request;
    }

    public HttpSession getSession() {
        return this.request.getSession(true);
    }

    public Fadmin getAdmin() {
        return (Fadmin) this.getSession().getAttribute(Constants.LOGIN_ADMIN);
    }

}
