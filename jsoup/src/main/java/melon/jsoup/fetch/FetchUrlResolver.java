package melon.jsoup.fetch;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @user: melon.zhao
 * @date: 2018/8/16
 */
@Component
@Slf4j
public class FetchUrlResolver {
    public static Document fetchDoc(String url) {
        log.info("fetchDoc...url:{}",url);
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("www.baidu.com")
                    .timeout(8000)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    public static String getNextPage(Document document) {
        if (document == null) return null;
        return document.selectFirst("ul.list-pager").children().last().children().last().attr("href");
    }
 }
