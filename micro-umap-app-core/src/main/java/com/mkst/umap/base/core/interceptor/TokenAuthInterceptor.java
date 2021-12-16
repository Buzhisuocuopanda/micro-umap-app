package com.mkst.umap.base.core.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.mkst.mini.systemplus.util.ApiUtils;
import com.mkst.umap.base.core.annotation.AuthToken;
import com.mkst.umap.base.core.exception.SysApiException;

/**
 * Token验证
 */
public class TokenAuthInterceptor implements HandlerInterceptor {

	public static final String USER_KEY = "userId";
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    	AuthToken annotation;

        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(AuthToken.class);
        } else {
            return true;
        }

        if (annotation == null) {
            return true;
        }

        //获取用户凭证
        String token = request.getHeader("token");

        //凭证为空
        if (StringUtils.isBlank(token)) {
            throw new SysApiException("token不能为空", HttpStatus.UNAUTHORIZED.value());
        }

        Map<String, Object> tokenMap = ApiUtils.validationToken(token);
		if (!"0".equals(tokenMap.get("code"))) {
			throw new SysApiException("token无效", HttpStatus.UNAUTHORIZED.value());
		}

		//设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(USER_KEY, tokenMap.get("openId"));
		
        return true;

    }
}