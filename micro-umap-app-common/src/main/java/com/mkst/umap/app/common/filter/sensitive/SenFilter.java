package com.mkst.umap.app.common.filter.sensitive;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @ClassName SenFilter
 * @Description 敏感词过滤器
 * @Author wangyong
 * @Modified By:
 * @Date 2020-08-20 14:26
 */
public class SenFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("敏感词过滤器启动！！！");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
       try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            boolean flag = true;
            SensitiveFilter filter = SensitiveFilter.DEFAULT;
            // GET请求
            if (httpServletRequest.getMethod().equals(RequestMethod.GET.name())) {
                //获取前端传递的所有参数名的枚举
                Enumeration pNames = httpServletRequest.getParameterNames();
                //遍历枚举
                while(pNames.hasMoreElements()){
                    //获取参数名
                    String name=(String)pNames.nextElement();
                    //获取参数值
                    String value =httpServletRequest.getParameter(name);
                    //对参数值进行敏感词处理,并重新设置到request
                    String str = filter.filter(value, '*');
                    if(value != str){
                        returnJson(servletRequest,servletResponse);
                        flag = false;
                        break;
                    }
                    httpServletRequest.setAttribute(name,str);
                }
                if(flag){
                    filterChain.doFilter(servletRequest, servletResponse);
                }
                // POST请求：
            } else if (httpServletRequest.getMethod().equals(RequestMethod.POST.name())) {
                SenRequestWrapper senRequestWrapper = new SenRequestWrapper(httpServletRequest);
                if(senRequestWrapper == null) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    String body = senRequestWrapper.getBody();
                    String filted = filter.filter(body, '*');
                    if (!body.equals(filted)) {
                        returnJson(senRequestWrapper,servletResponse);
                    } else {
                        filterChain.doFilter(senRequestWrapper, servletResponse);
                    }
                }
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }

        }catch (IOException e){
            e.getMessage();
        }

    }

    @Override
    public void destroy() {
        System.out.println("敏感词过滤器已销毁！！！");
    }


    private void returnJson(ServletRequest request, ServletResponse response) throws IOException {
        //把返回值输出到客户端
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        JSONObject resultJson = new JSONObject(2);
        resultJson.put("code",301);
        resultJson.put("msg","输入的内容包含敏感信息");
        outputStream.write(JSONArray.toJSONBytes(resultJson));
        outputStream.flush();
        outputStream.close();
    }

}
