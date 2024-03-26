package io.github.stylesmile.tool;


/**
 * @author chenye
 */
public class StringUtil {
    /**
     * 判断为空，不去除空格
     *
     * @param str 自促穿
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        return (str == null || "".equals(str));
    }

    /**
     * 判断为非空，不去除空格
     *
     * @param str 自促穿
     * @return boolean
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断为非空空，去除 空格
     *
     * @param str 自促穿
     * @return boolean
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 判断为空，去除空格
     *
     * @param str 自促穿
     * @return boolean
     */
    public static boolean isBlank(String str) {
        if (null == str) {
            return true;
        }
        return isEmpty(str.trim());
    }

}
