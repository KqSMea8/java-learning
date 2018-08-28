package melon.jsoup.fetch.page;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import melon.jsoup.domain.DoubanMovieItem;
import melon.jsoup.utils.HttpClientUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @user: melon.zhao
 * @date: 2018/8/28
 */
@Service
public class DoubanFetchResolver  {
    private static String BASE_URL = "https://movie.douban.com/j/new_search_subjects";

    public List<DoubanMovieItem> getMovieList (int startSize) {
        try {
            Map map = new HashMap();
            map.put("sort","");
            map.put("range","8,10");
            map.put("tags","");
            map.put("start","0");

            String res = HttpClientUtils.doGet(BASE_URL, map);
            JSONArray array = JSON.parseObject(res).getJSONArray("data");
            return array.toJavaList(DoubanMovieItem.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}
