package melon.jsoup.fetch.page;

import melon.jsoup.fetch.AbstractFetchUrl;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * @user: melon.zhao
 * @date: 2018/8/28
 */
public class MaoYanFetchResolver extends AbstractFetchUrl {

    private static final String BASE_URL = "http://www.maoyan.com";
    public MaoYanFetchResolver() {
        this(10,1000);
    }

    public MaoYanFetchResolver(int totalPage, int interval) {
        this(totalPage,interval,1);
    }

    public MaoYanFetchResolver(int totalPage, int interval,int currentPage){
        this.totalPage = totalPage;
        this.interval = interval;
        this.currentPage = currentPage ;
    }

    @Override
    public boolean hasNextPage() {

        return super.hasNextPage();
    }

    @Override
    public List pasreHtml(Document html) {
        return null;
    }


    @Override
    public String getUrl() {
        return BASE_URL;
    }
}
