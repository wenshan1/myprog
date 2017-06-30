package me.wenshan.constants;

import java.util.ArrayList;

public class StockConstants {
    /* 300 - 500 模型名字 */
    public static final String MODEL_300_500 = "model_300_500";

    /* 300 - 创业板 模型名字 */
    public static final String MODEL_300_CHUANGYEBAN = "model_300_chuangyeban";

    /* 50 - 创业板 模型名字 */
    public static final String MODEL_50_CHUANGYEBAN = "model_50_chuangyeban";

    /* 创业板增强 模型名字 */
    public static final String MODEL_CHUANGYEBANEXEX = "chuangyebanex";

    public static final String MODEL_CUSTOME = "model_custom";

    public static ArrayList<String> getSockNames() {
        ArrayList<String> list = new ArrayList<String>();

        list.add("sh000001"); // 上证指数

        list.add("sz399001"); // 深圳指数

        list.add("sh000300"); // 沪深300

        list.add("sh000905"); // 中证500

        list.add("sz399006"); // 创业板

        list.add("sz399005"); // 中小板指数

        list.add("sh000010"); // 上证180

        list.add("sh000016"); // 上证50

        return list;
    }
}
