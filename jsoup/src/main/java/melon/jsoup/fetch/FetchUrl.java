package melon.jsoup.fetch;

import org.jsoup.nodes.Document;

/**
 * @user: melon.zhao
 * @date: 2018/8/21
 */
public interface FetchUrl {
    boolean hasNextPage();
    Document fetchNextPage();
}
