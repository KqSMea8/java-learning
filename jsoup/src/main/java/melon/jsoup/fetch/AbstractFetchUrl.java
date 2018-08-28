package melon.jsoup.fetch;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


/**
 * @user: melon.zhao
 * @date: 2018/8/21
 */
@Slf4j
public abstract class AbstractFetchUrl<T> implements FetchUrl {
    private static final String[][] HEADERS = new String[][]{
            {"Connection", "keep-alive"},
            {"Cache-Control", "max-age=0"},
            {"Upgrade-Insecure-Requests", "1"},
            {"User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko)"},
            {"Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"},
            {"Accept-Encoding", "gzip, deflate, sdch"},
            {"Accept-Language", "zh-CN,zh;q=0.8"},
            {"Referer","http://www.baidu.com"}
    };
    public static final BigDecimal big = new BigDecimal(1000);

    protected int interval;
    protected int currentPage;
    protected int totalPage;

    /**
     * 解析下一页
     * @return true/false
     */
    public boolean hasNextPage() {
        return currentPage <= totalPage;
    }

    /**
     * 解析页面
     * @param html 解析文档
     * @return 泛型list
     */
    public abstract List<T> pasreHtml(Document html);

    public abstract String getUrl();

    /**
     * 获取下一页
     * @return 网页文档
     */
    public Document fetchNextPage() {
        String url = getUrl();
        log.info("---->currentPage:{}  url:{}",currentPage,url);
        Connection connect = Jsoup.connect(url);
        currentPage++;
        for (String[] strs : HEADERS) {
            connect.header(strs[0], strs[1]);
        }
        Document res = null;
        try {
            res = connect.timeout(interval).execute().parse();
        } catch (IOException e) {
            log.error("fetch next page error--->", e);
            currentPage--;
        }
        return res;
    }

    public List<T> fetchAll() {
        List list = new ArrayList();
        while (hasNextPage()) {
            Document document = fetchNextPage();
            if (document == null) return null;
            list.add(pasreHtml(document));
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

}
