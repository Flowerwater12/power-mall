package com.dqw.util;


import com.dqw.constant.HttpConstants;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 响应工具类
 */
public class ResponseUtils {

    public static void writer(HttpServletResponse response,String jsonStr) {
        // 设置响应头信息
        response.setContentType(HttpConstants.APPLICATION_JSON);
        response.setCharacterEncoding(HttpConstants.UTF_8);
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writer.write(jsonStr);
        writer.flush();
        writer.close();
    }
}
