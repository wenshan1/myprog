package me.wenshan.stock.mtw;

import java.util.List;

import me.wenshan.stock.domain.StockIndex;
import me.wenshan.stock.service.StockServiceImp;

public abstract class Stock28 {
    protected double dbIndex = 1000; /* 2011-1-1 */
    protected String stock2Name ;
    protected String stock8Name ;
    protected String nextstockName = "";
    protected String nnextstockName = "";
    protected String currentstockName = "";
    
    private double getM20(String riqi, String stockname) {

        List<StockIndex> lst = StockServiceImp.getInstance().getDataRecord(stockname, riqi, 20);
        /*
         * for (int i = 0; i < 20; i++) { StockIndex ind = lst.get(i); m20 +=
         * ind.getCloseprice(); }
         * 
         * return m20 / 20;
         */

        return lst.get(19).getCloseprice();
    }

    protected boolean getCurrentIndex(String strriqi) {

        // 1. 得到 2股票 和 8股票 当日数据

        StockIndex stock2 = StockServiceImp.getInstance().getStockIndex(strriqi, stock2Name);
        StockIndex stock8 = StockServiceImp.getInstance().getStockIndex(strriqi, stock8Name);
        if (stock2 == null || stock8 == null)
            return false;

        // 2. 得到 2 股票 和 8股票 前20日数据

        double stock2m20 = getM20(strriqi, stock2Name);
        double stock8m20 = getM20(strriqi, stock8Name);

        // 计算指数
        if (!currentstockName.isEmpty()) {
            StockIndex curr;
            StockIndex pre;
            List<StockIndex> lst;
            if (currentstockName.compareTo(stock2Name) == 0) {
                curr = stock2;
            } else {
                curr = stock8;
            }
            lst = StockServiceImp.getInstance().getDataRecord(currentstockName, strriqi, 1);
            pre = lst.get(0);
            dbIndex = dbIndex * (1 + (curr.getCloseprice() - pre.getCloseprice()) / pre.getCloseprice());
        }

        // 计算下一个指数

        double zhanghu2;
        double zhanghu8;
        zhanghu2 = (stock2.getCloseprice() - stock2m20) / stock2m20;
        zhanghu8 = (stock8.getCloseprice() - stock8m20) / stock8m20;

        if (zhanghu2 < 0 && zhanghu8 < 0) {
            nnextstockName = "";
        } else {
            if (zhanghu2 > zhanghu8)
                nnextstockName = stock2Name;
            else
                nnextstockName = stock8Name;
        }
        return true;
    }
    
}
