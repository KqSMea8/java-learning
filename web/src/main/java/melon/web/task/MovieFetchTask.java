package melon.web.task;

import melon.common.utils.HttpClientUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

public class MovieFetchTask {

    private static final String url = "https://movie.douban.com/j/new_search_subjects?sort=U&range=6,10&tags=&start=";

    public void doFetch(){

        try {
            String res = HttpClientUtils.doGet(url, new HashMap<>());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
