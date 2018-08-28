package melon.jsoup.fetch.ip;

import melon.jsoup.domain.IP;
import melon.jsoup.fetch.AbstractFetchUrl;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @user: melon.zhao
 * @date: 2018/8/24
 * 快代理
 */
public class KDLFecthResolver extends AbstractFetchUrl {
    private static final String BASE_URL = "https://www.kuaidaili.com";

    public KDLFecthResolver() {
        this(10,1000);
    }

    public KDLFecthResolver(int totalPage, int interval) {
        this(totalPage,interval,1);
    }

    public KDLFecthResolver(int totalPage, int interval,int currentPage){
        this.totalPage = totalPage;
        this.interval = interval;
        this.currentPage = currentPage ;
    }

    /**
     *
     * @param html 解析文档
     * @return
     * <tr>
     *                     <td data-title="IP">115.211.35.197</td>
     *                     <td data-title="PORT">9000</td>
     *                     <td data-title="匿名度">高匿名</td>
     *                     <td data-title="类型">HTTP</td>
     *                     <td data-title="位置">浙江省金华市  电信</td>
     *                     <td data-title="响应速度">0.9秒</td>
     *                     <td data-title="最后验证时间">2018-08-24 12:31:05</td>
     *                 </tr>
     */
    @Override
    public List pasreHtml(Document html) {
        List res = new ArrayList();
        Elements select = html.getElementById("list").select("tr");
        select.iterator().forEachRemaining(element -> {
            IP item = new IP();
            if (element.select("th").size()<=0) {
                item.setIp(element.child(0).text());
                item.setPort(element.child(1).text());
                item.setIsAnonymous(element.child(2).text());
                item.setType(element.child(3).text());
                item.setLocation(element.child(4).text());
                String speedStr = element.child(5).text();
                BigDecimal tmp = new BigDecimal(speedStr.substring(0,speedStr.length()-1));
                item.setSpeed(tmp.multiply(big));
                res.add(item);
            }
        });
        return null;
    }

    @Override
    public String getUrl() {
        if (hasNextPage()) return BASE_URL+"/free/inha/"+currentPage;
        return null;
    }
}
