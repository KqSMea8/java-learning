package melon.web.utils;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {
    /**
     * 连接超时时间
     */
    public static final int CONNECTION_TIMEOUT_MS = 360000;

    /**
     * 读取数据超时时间
     */
    public static final int SO_TIMEOUT_MS = 360000;

    public static final String CONTENT_TYPE_JSON_CHARSET = "application/json;charset=utf-8";

    public static final String CONTENT_TYPE_XML_CHARSET = "application/xml;charset=utf-8";

    /**
     * httpclient读取内容时使用的字符集
     */
    public static final String CONTENT_CHARSET = "UTF-8";

    public static final Charset UTF_8 = Charset.forName("UTF-8");

    public static final Charset GBK = Charset.forName(CONTENT_CHARSET);

    /**
     * 简单get调用
     *
     * @param url
     * @param params
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String doGet(String url, Map<String, String> params)
            throws ClientProtocolException, IOException, URISyntaxException {
        return doGet(url, params, CONTENT_CHARSET);
    }

    /**
     * 简单get调用
     *
     * @param url
     * @param params
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String doGet(String url, Map<String, String> params, String charset)
            throws ClientProtocolException, IOException, URISyntaxException {

        HttpClient client = buildHttpClient(false);

        HttpGet get = buildHttpGet(url, params);

        HttpResponse response = client.execute(get);

        assertStatus(response);

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String returnStr = EntityUtils.toString(entity, charset);
            return returnStr;
        }
        return null;
    }

    /**
     * 简单post调用
     *
     * @param url
     * @param params
     * @return
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String doPost(String url, Map<String, String> params)
            throws URISyntaxException, ClientProtocolException, IOException {
        return doPost(url, params, CONTENT_CHARSET);
    }

    /**
     * 简单post调用
     *
     * @param url
     * @param params
     * @return
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String doPost(String url, Map<String, String> params, String charset)
            throws URISyntaxException, ClientProtocolException, IOException {

        HttpClient client = buildHttpClient(false);

        HttpPost postMethod = buildHttpPost(url, params);

        HttpResponse response = client.execute(postMethod);

        assertStatus(response);

        HttpEntity entity = response.getEntity();

        if (entity != null) {
            String returnStr = EntityUtils.toString(entity, charset);
            return returnStr;
        }

        return null;
    }

    /**
     * 创建HttpClient
     *
     * @param isMultiThread
     * @return
     */
    public static HttpClient  buildHttpClient(boolean isMultiThread) {

        CloseableHttpClient client;

        if (isMultiThread)
            client = HttpClientBuilder
                    .create()
                    .setDefaultRequestConfig(buildRequestConfig())
                    .setConnectionManager(
                            new PoolingHttpClientConnectionManager()).build();
        else
            client = HttpClientBuilder.create().setDefaultRequestConfig(buildRequestConfig()).build();

        return client;
    }


    /**
     * 构建httpPost对象
     *
     * @param url
     * @throws UnsupportedEncodingException
     * @throws URISyntaxException
     */
    public static HttpPost buildHttpPost(String url, Map<String, String> params) {
        Assert.notNull(url, "构建HttpPost时,url不能为null");
        HttpPost post = new HttpPost(url);
        setCommonHttpMethod(post);
        HttpEntity he = null;
        if (params != null) {
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                formparams.add(new BasicNameValuePair(key, params.get(key)));
            }
            he = new UrlEncodedFormEntity(formparams, GBK);
            post.setEntity(he);
        }
        // 在RequestContent.process中会自动写入消息体的长度，自己不用写入，写入反而检测报错
        // setContentLength(post, he);
        return post;

    }

    /**
     * 构建httpGet对象
     *
     * @param url
     * @throws URISyntaxException
     */
    public static HttpGet buildHttpGet(String url, Map<String, String> params)
            throws URISyntaxException {
        Assert.notNull(url, "构建HttpGet时,url不能为null");
        HttpGet get = new HttpGet(buildGetUrl(url, params));
        return get;
    }

    /**
     * build getUrl str
     *
     * @param url
     * @param params
     * @return
     */
    private static String buildGetUrl(String url, Map<String, String> params) {
        StringBuffer uriStr = new StringBuffer(url);
        if (params != null) {
            List<NameValuePair> ps = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                ps.add(new BasicNameValuePair(key, params.get(key)));
            }
            uriStr.append("?");
            uriStr.append(URLEncodedUtils.format(ps, UTF_8));
        }
        return uriStr.toString();
    }

    /**
     * 设置HttpMethod通用配置
     *
     * @param httpMethod
     */
    public static void setCommonHttpMethod(HttpPost httpMethod) {
        httpMethod.setHeader(HTTP.CONTENT_ENCODING, CONTENT_CHARSET);// setting
        httpMethod.setHeader(HTTP.USER_AGENT,"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        // contextCoding
//		httpMethod.setHeader(HTTP.CHARSET_PARAM, CONTENT_CHARSET);
        // httpMethod.setHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_JSON_CHARSET);
        // httpMethod.setHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_XML_CHARSET);
    }

    /**
     * 设置成消息体的长度 setting MessageBody length
     *
     * @param httpMethod
     * @param he
     */
    public static void setContentLength(HttpRequestBase httpMethod,
                                        HttpEntity he) {
        if (he == null) {
            return;
        }
        httpMethod.setHeader(HTTP.CONTENT_LEN, String.valueOf(he.getContentLength()));
    }

    /**
     * 构建公用RequestConfig
     *
     * @return
     */
    public static RequestConfig buildRequestConfig() {
        // 设置请求和传输超时时间
        return RequestConfig.custom()
                .setSocketTimeout(SO_TIMEOUT_MS)
                .setConnectTimeout(CONNECTION_TIMEOUT_MS).build();
    }

    public static RequestConfig buildRequestConfig(HttpHost proxy) {
        // 设置代理
        return RequestConfig.custom()
                .setSocketTimeout(SO_TIMEOUT_MS)
                .setConnectTimeout(CONNECTION_TIMEOUT_MS)
                .setProxy(proxy)
                .build();
    }

    /**
     * 强验证必须是200状态否则报异常
     *
     * @param res
     * @throws
     */
    static void assertStatus(HttpResponse res) throws IOException {
        Assert.notNull(res, "http响应对象为null");
        Assert.notNull(res.getStatusLine(), "http响应对象的状态为null");
        switch (res.getStatusLine().getStatusCode()) {
            case HttpStatus.SC_OK:
//		case HttpStatus.SC_CREATED:
//		case HttpStatus.SC_ACCEPTED:
//		case HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION:
//		case HttpStatus.SC_NO_CONTENT:
//		case HttpStatus.SC_RESET_CONTENT:
//		case HttpStatus.SC_PARTIAL_CONTENT:
//		case HttpStatus.SC_MULTI_STATUS:
                break;
            default:
                throw new IOException("服务器响应状态异常,失败.");
        }
    }

    private HttpClientUtils() {
    }

    public static void main(String[] args) throws ClientProtocolException, IOException, URISyntaxException {
        System.out.println(doGet("http://www.baidu.com", new HashMap<String, String>()));
    }
}
