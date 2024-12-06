package van.planifolia.maid.util.http;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: Planifolia.Van
 * @Date: 2024/12/3 11:46
 */
@Slf4j
public class HttpClientUtil {


    /**
     * Media类型JSON
     */
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    /**
     * 构建OKHttp请求的Client
     */
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * 发送一条OKHTTP的Post请求
     *
     * @param body 请求参数
     * @param url  请求连接
     * @return 结果
     */
    public static String doOkHttpPost(String url, JSONObject body, Map<String, String> headers) {
        log.info("请求Host:\n{}\n请求参数JSON\n{}",url,body);
        RequestBody requestBody = RequestBody.create(body.toJSONString(), JSON);
        Request.Builder requestBuilder = new Request.Builder().url(url).post(requestBody);
        if (headers != null) {
            headers.forEach(requestBuilder::addHeader);
        }

        Request request = requestBuilder.build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String result = response.body().string();
                log.info("响应:{}",result);
                return result;
            } else {
                throw new IOException(String.format("code: %s 远端服务host: %s 异常，请联系服务提供商。",response.code(),url));
            }
        }catch (Exception e){
            log.info("OkHTTP-POST-JSON 请求发送失败，错误消息:{}",e.getMessage());
            throw new RuntimeException("请求失败！");
        }
    }

    /**
     * 发送OkHttpPost请求 表单版
     * @param url 请求URL
     * @param headers 请求头
     * @param params 请求表单数据
     * @return 响应结果
     */
    public static String doOkHttpPost(String url, Map<String, String> params, Map<String, String> headers){
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (params != null) {
            params.forEach(formBodyBuilder::add);
        }

        RequestBody body = formBodyBuilder.build();

        Request.Builder requestBuilder = new Request.Builder().url(url).post(body);
        if (headers != null) {
            headers.forEach(requestBuilder::addHeader);
        }

        Request request = requestBuilder.build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException(String.format("code: %s 远端服务host: %s 异常，请联系服务提供商。",response.code(),url));
            }
        } catch (IOException e) {
            log.info("OkHTTP-POST-FORM 请求发送失败，错误消息:{}",e.getMessage());
            throw new RuntimeException("请求失败！");
        }

    }
    /**
     * 发送 GET 请求
     *
     * @param url     请求地址
     * @param headers 自定义请求头
     * @param params  URL 参数
     * @return 响应结果
     * @throws IOException 请求异常
     */
    public static String doOkHttpGet(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        // 拼接 URL 参数
        if (params != null && !params.isEmpty()) {
            StringBuilder urlBuilder = new StringBuilder(url).append("?");
            params.forEach((key, value) -> urlBuilder.append(key).append("=").append(value).append("&"));
            url = urlBuilder.substring(0, urlBuilder.length() - 1); // 去掉最后的 &
        }

        // 构建请求
        Request.Builder requestBuilder = new Request.Builder().url(url);
        if (headers != null) {
            headers.forEach(requestBuilder::addHeader); // 添加请求头
        }

        Request request = requestBuilder.get().build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                throw new IOException(String.format("code: %s 远端服务host: %s 异常，请联系服务提供商。",response.code(),url));
            }
        }
    }


    /**
     * 解析返回值
     *
     * @param responseBody 响应结果
     * @return 解析成字符串
     */
    private static String resolver(ResponseBody responseBody) {
        InputStream is = null;
        String result = null;
        try {
            is = responseBody.byteStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String body = null;
            StringBuilder sb = new StringBuilder();
            while ((body = br.readLine()) != null) {
                sb.append(body);
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                log.info("解析OkHttp响应-关闭资源异常！");
            }
        }
        return result;
    }
}
