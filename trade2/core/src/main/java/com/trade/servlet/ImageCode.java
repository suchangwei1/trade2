package com.trade.servlet;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.trade.Enum.CountLimitTypeEnum;
import com.trade.code.AuthCode;
import com.trade.util.Constants;
import com.trade.util.VerifyCodeUtils;

public class ImageCode extends HttpServlet {

	private static final long serialVersionUID = -4247259762852343659L;

	private Font imgFont = new Font("Times New Roman", Font.BOLD, 17);

	@Override
	public void init() throws ServletException {
		super.init();
	}

	Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        response.setHeader("Pragma", "No-cache");  
        response.setHeader("Cache-Control", "no-cache");  
        response.setDateHeader("Expires", 0);  
        response.setContentType("image/jpeg");  

        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);

        HttpSession session = request.getSession(true);
		AuthCode authCode = new AuthCode(verifyCode, CountLimitTypeEnum.IMAGE_CAPTCHA);
        session.setAttribute(Constants.IMAGE_CODE_KEY, authCode);

        int w = 200, h = 80;  
        VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode); 
	}

	@Override
	public void destroy() {
	}
	
}