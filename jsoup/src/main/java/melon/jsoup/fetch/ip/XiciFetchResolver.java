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
 * @date: 2018/8/21
 */
public class XiciFetchResolver extends AbstractFetchUrl {
    private static final String BASE_URL = "http://www.xicidaili.com";
    public XiciFetchResolver() {
        this(10,1000);
    }

    public XiciFetchResolver(int totalPage, int interval) {
        this(totalPage,interval,1);
    }

    public XiciFetchResolver(int totalPage, int interval,int currentPage){
        this.totalPage = totalPage;
        this.interval = interval;
        this.currentPage = currentPage ;
    }

    /**
     * <tr class="odd">
     *  <td class="country"><img src="http://fs.xicidaili.com/images/flag/cn.png" alt="Cn"></td>
     *  <td>61.135.217.7</td>
     *  <td>80</td>
     *  <td> <a href="/2016-05-13/beijing">北京</a> </td>
     *  <td class="country">高匿</td>
     *  <td>HTTP</td>
     *  <td class="country">
     *   <div title="0.324秒" class="bar">
     *    <div class="bar_inner fast" style="width:94%">
     *    </div>
     *   </div> </td>
     *  <td class="country">
     *   <div title="0.064秒" class="bar">
     *    <div class="bar_inner fast" style="width:99%">
     *    </div>
     *   </div> </td>
     *  <td>830天</td>
     *  <td>18-08-21 12:11</td>
     * </tr>
     * @param html 解析文档
     * @return
     */
    @Override
    public List pasreHtml(Document html) {
        List res = new ArrayList();
        Elements select = html.getElementById("ip_list").select("tr");
        select.iterator().forEachRemaining(element -> {
            IP item = new IP();
            if (element.select("th").size()<=0) {
                item.setIp(element.child(1).text());
                item.setPort(element.child(2).text());
                item.setLocation(element.child(3).text());
                item.setIsAnonymous(element.child(4).text());
                item.setType(element.child(5).text());
                String speedStr = element.child(6).selectFirst("div").attr("title");
                String establishedCostStr = element.child(7).selectFirst("div").attr("title");
                BigDecimal tmp = new BigDecimal(speedStr.substring(0,speedStr.length()-1));
                item.setSpeed(tmp.multiply(big));
                tmp = new BigDecimal(establishedCostStr.substring(0,establishedCostStr.length()-1));
                item.setEstablishedCost(tmp.multiply(big));
                res.add(item);
            }
        });
        return res;
    }

    @Override
    public String getUrl() {
        if (hasNextPage()) return BASE_URL+"/nn/"+currentPage;
        return null;
    }

}
