package com.ruoyi.base.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author cmw
 */
@Component
public class RepeatLogInterceptor extends HandlerInterceptorAdapter
{
    private static final Logger logger = LoggerFactory.getLogger(RepeatLogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
       try
        {
            logger.info("------------------------拦截器开始");

            String requestUri = request.getRequestURI();
            String method = request.getMethod();

            Map<String, String[]> map = request.getParameterMap();
            StringBuilder stringBuilder = new StringBuilder();
            String agent = request.getHeader("User-Agent");
            stringBuilder.append("User-Agent : " + agent + " ,");
            stringBuilder.append(method);
            stringBuilder.append(",url:【");
            stringBuilder.append(requestUri).append("?");

            //获取前端传递参数
            Set<Entry<String, String[]>> set = map.entrySet();
            Iterator<Entry<String, String[]>> it = set.iterator();
            while (it.hasNext()) {
                Entry<String, String[]> entry = it.next();
                for (String i : entry.getValue()) {
                    request.setAttribute(entry.getKey(), i);
                    stringBuilder.append("" + entry.getKey());
                    stringBuilder.append("=" + i + "&");
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1).append("】");
            logger.info(stringBuilder.toString());
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return super.preHandle(request, response, handler);
        }
    }
}
