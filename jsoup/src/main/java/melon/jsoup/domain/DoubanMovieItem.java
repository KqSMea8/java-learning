package melon.jsoup.domain;

import lombok.Data;

import java.util.List;

/**
 * @user: melon.zhao
 * @date: 2018/8/28
 */
@Data
public class DoubanMovieItem {
/**
 * {
 * "directors": [
 * "詹姆斯·霍尼伯内"
 * ],
 * "rate": "9.9",
 * "cover_x": 1530,
 * "star": "50",
 * "title": "蓝色星球2",
 * "url": "https://movie.douban.com/subject/26979545/",
 * "casts": [
 * "戴维·阿滕伯勒"
 * ],
 * "cover": "https://img1.doubanio.com/view/photo/s_ratio_poster/public/p2518413249.webp",
 * "id": "26979545",
 * "cover_y": 2176
 * },
 */
    //id
    String id;
    //导演
    List<String> directors;
    //评分
    String rate;
    //星星 50/10 五星
    String star;
    //片名
    String title;
    //影片详情
    String url;
    //主演
    List<String> casts;
    //海报
    String cover;

    String cover_x;

    String cover_y;


}
