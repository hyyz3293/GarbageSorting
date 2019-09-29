package com.tool.rss.ui.NetWork;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.tool.rss.utils.ExceptionUtil;
import com.tool.rss.utils.JsonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * 日志拦截器
 *
 * @author jack
 * @date 2018.07.05
 * @note -
 * -------------------------------------------------------------------------------------------------
 * @modified -
 * @date -
 * @note -
 */
public class LoggerInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response oldResponse;
        try {
            oldResponse = chain.proceed(request);
        } catch (IOException e) {
            LogUtils.d("Request(IOException): " + request.url() + "\n" + ExceptionUtil.getStackTrace(e));
            throw e;
        }
        StringBuilder logger = new StringBuilder();

        // 处理请求
        String reqContentType = request.header("content-type");
        if (!filterContentType(reqContentType)) {
            // 请求链接
            logger.append("\nRequest:").append(request.url());

            try {
                RequestBody reqBody = request.body();
                if (reqBody instanceof FormBody) {
                    FormBody body = (FormBody) reqBody;
                    int size = body.size();
                    Map<String, String> map = new HashMap<>();
                    for (int i = 0; i < size; i++) {
                        map.put(body.name(i), body.value(i));
                    }
                    //请求JSON
                    logger.append("\nJson:").append(JsonUtil.objectToString(map));
                } else {
                    Buffer buffer = new Buffer();
                    try {
                        reqBody.writeTo(buffer);
                    } catch (IOException e) {
                    }
                    logger.append("\nJson:").append(new String(buffer.readByteArray()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 处理应答
        String resContentType = oldResponse.header("content-type");
        if (!filterContentType(resContentType)) {
            // 打印请求的Cookie(Session)
            String cookie = oldResponse.header("token");
            logger.append("\nSession:").append(cookie);

            MediaType mediaType = oldResponse.body().contentType();
            byte[] responseBytes = oldResponse.body().bytes();
            String response = new String(responseBytes);
            // 响应数据
            logger.append("\nResponse:").append(response);

            // 打印Check
            String check = oldResponse.header("check");
            logger.append("\nCheck:").append(check);

            Headers.Builder headersBuilder = oldResponse.headers().newBuilder();
            Response.Builder builder = oldResponse.newBuilder();
            builder.headers(headersBuilder.build())
                    .body(ResponseBody.create(mediaType, responseBytes));
            LogUtils.d(logger.toString());

            return builder.build();
        }

        return oldResponse;
    }

    /**
     * 是否过滤ContentType
     *
     * @return true -- 过滤 false -- 不过滤
     */
    private boolean filterContentType(String contentType) {
        if (TextUtils.isEmpty(contentType)) {
            return false;
        }

        // 过滤掉图片类型
        if (contentType.toLowerCase().contains("image")) {
            return true;
        }

        return false;
    }
}
