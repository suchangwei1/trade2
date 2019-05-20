package com.trade.filter;

import com.alibaba.fastjson.JSON;
import com.trade.dto.ResultBean;
import com.trade.model.Fuser;
import com.trade.mq.OperateUserActionListener;
import com.trade.util.Constants;
import com.trade.util.SpringContextUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

@WebFilter(filterName = "forceOfflineFilter", urlPatterns = "/api/*")
public class ForceOfflineFilter implements Filter {
    private OperateUserActionListener operateUserActionListener;

    public static final String ALREADY_FILTERED_SUFFIX = ".FILTERED";

    protected String getName() {
        return "forceOfflineFilter";
    }

    protected String getAlreadyFilteredAttributeName() {
        String name = getName();
        if (name == null) {
            name = getClass().getName();
        }
        return name + ALREADY_FILTERED_SUFFIX;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.operateUserActionListener = SpringContextUtils.getBean(OperateUserActionListener.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (Objects.isNull(req.getAttribute(getAlreadyFilteredAttributeName()))) {
            this.doFilterInternal(req, (HttpServletResponse) response, chain);
        } else {
            this.skip(request, response, chain);
        }
    }

    protected void skip(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setAttribute(getAlreadyFilteredAttributeName(), true);
        if (operateUserActionListener.isEmpty()) {
            this.skip(request, response, chain);
            return;
        }

        HttpSession session = request.getSession();
        if (Objects.isNull(session)) {
            this.skip(request, response, chain);
            return;
        }

        Fuser fuser = (Fuser) session.getAttribute(Constants.USER_LOGIN_SESSION);
        if (Objects.isNull(fuser)) {
            this.skip(request, response, chain);
            return;
        }

        String userId = String.valueOf(fuser.getFid());
        if (operateUserActionListener.isExist(userId)) {
            // 删除登录信息
            session.removeAttribute(Constants.USER_LOGIN_SESSION);

            // 强迫下线
            PrintWriter out = response.getWriter();
            response.setContentType("application/json;charset=utf-8");
            out.write(JSON.toJSONString(ResultBean.E403));
        } else {
            this.skip(request, response, chain);
        }
    }

    @Override
    public void destroy() {

    }
}
