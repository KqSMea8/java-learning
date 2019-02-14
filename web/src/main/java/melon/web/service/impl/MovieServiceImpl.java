package melon.web.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import melon.common.utils.HttpClientUtils;
import melon.web.mapper.MovieMapper;
import melon.web.model.Movie;
import melon.web.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {
    private static final String url = "https://movie.douban.com/j/new_search_subjects?sort=U&range=6,10&tags=&start=";

    @Autowired
    MovieMapper mapper;

    private List<Movie> doFetch(int start) {
        List<Movie> movies = null;
        try {
            String res = HttpClientUtils.doGet(url, new HashMap<>());
            if (StringUtils.isEmpty(res)) return Collections.emptyList();
            movies = JSON.parseArray(res, Movie.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return movies;
    }

    @Override
    public void startFetch() {
        Executors.newFixedThreadPool(1).submit(() -> {
            int page = 1;
            while (page <= 100) {
                List<Movie> movies = doFetch(page++ * 20);
                mapper.insertList(movies);
            }
            log.info("done...");
        });
    }
}
