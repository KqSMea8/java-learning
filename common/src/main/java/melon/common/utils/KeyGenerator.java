package melon.common.utils;

public class KeyGenerator {
    public static final String PREFIX = "mq-key:";

    public static String genTimestampKey(){
        return PREFIX+System.currentTimeMillis();
    }
}
