package me.wenshan.util;

public class ShowHelp {
 public String getStockStr (String name) {
        
        if (name.compareTo("sh000001") == 0)
            return "上证指数";
        if (name.compareTo("sz399001") == 0)
            return "深圳指数";
        if (name.compareTo("sh000300") == 0)
            return "沪深300";
        if (name.compareTo("sh000905") == 0)
            return "中证500";
        if (name.compareTo("sz399006") == 0)
            return "创业板";
        if (name.compareTo("sz399005") == 0)
            return "中小板指数";
        if (name.compareTo("sh000010") == 0)
            return "上证180";
        if (name.compareTo("sh000016") == 0)
            return "上证50";
        return name;
    }
}
