package melon.jsoup.domain;

import lombok.Data;
import org.omg.CORBA.INTERNAL;

/**
 * @user: melon.zhao
 * @date: 2018/8/20
 * movie 属性
 *
 * <dd>
 *     <div class="movie-item">
 *       <a href="/films/1175253" target="_blank" data-act="movie-click" data-val="{movieid:1175253}">
 *       <div class="movie-poster">
 *         <img class="poster-default" src="//ms0.meituan.net/mywww/image/loading_2.e3d934bf.png">
 *         <img src="http://p1.meituan.net/movie/5709b4f83a669c0dd8c78461da4ece33208095.jpg@160w_220h_1e_1c">
 *       </div>
 *       </a>
 *         <div class="channel-action channel-action-sale">
 *   <a>购票</a>
 * </div>
 *
 *       <div class="movie-ver"></div>
 *     </div>
 *     <div class="channel-detail movie-item-title" title="爱情公寓">
 *       <a href="/films/1175253" target="_blank" data-act="movies-click" data-val="{movieId:1175253}">爱情公寓</a>
 *     </div>
 * <div class="channel-detail channel-detail-orange"><i class="integer">6.</i><i class="fraction">7</i></div>
 *
 *   </dd>
 */
@Data
public class MovieItem {
    private Integer movieId;
    private String title;
    private String imgUrl;
    private String detailUrl;
    private String score;
}
