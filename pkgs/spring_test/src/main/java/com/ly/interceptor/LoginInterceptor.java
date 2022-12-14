package com.ly.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor  implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

//        System.out.println("LoginInterceptor.preHandle"+"=========");

        String uri = request.getRequestURI();

        System.out.println(uri+"----------------------");
        if(uri.indexOf("login.do")>0){

            return  true;
        }

        HttpSession session = request.getSession();

        System.out.println(session.getAttribute("stuinfo")+"--------");
        if(session.getAttribute("stuinfo")!=null){

            return  true;
        }

//        request.getRequestDispatcher("/login.html").forward(request,response);

            response.sendRedirect("login.html");

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

        System.out.println("LoginInterceptor.postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

        System.out.println("LoginInterceptor.afterCompletion");
    }
}
