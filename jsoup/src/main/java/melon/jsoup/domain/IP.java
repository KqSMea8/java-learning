package melon.jsoup.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @user: melon.zhao
 * @date: 2018/8/21
 * IP实体
 */
@Data
public class IP {
    //ip
    private String ip;
    //端口
    private String port;
    //服务器地址
    private String location;
    //是否匿名
    private String isAnonymous;
    //http/https
    private String type;
    //速度 扩大1000倍 0.064 --> 64
    private BigDecimal speed;
    //连接时间 同上 扩大1000倍
    private BigDecimal establishedCost;
    //存活时间
    private long beAlive;
    //验证时间
    private long checkDate;
}
