package me.wenshan.web;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;

import me.wenshan.beijing.service.FetchData;
import me.wenshan.beijing.service.KongQiZhiLiangService;
import me.wenshan.biz.OptionManager;
import me.wenshan.blog.backend.form.DataOption;
import me.wenshan.constants.StockConstants;
import me.wenshan.newsmth.service.NewsmthFetchData;
import me.wenshan.newsmth.service.NewsmthServiceImp;
import me.wenshan.stock.service.StockServiceImp;
import me.wenshan.stockmodel.service.StockModelManager;

public class UpdateHourlyThread implements Runnable {

    private StockModelManager smmManager;
    private OptionManager opm;

    public UpdateHourlyThread(StockModelManager smmManager, OptionManager opm) {
        this.opm = opm;
        this.smmManager = smmManager;
    }

   

    public static boolean isTodayDataExist() {
        boolean bRet = true;
        Calendar cal = Calendar.getInstance();
        int m = cal.get(Calendar.MONTH) + 1;
        int d = cal.get(Calendar.DAY_OF_MONTH);
        int y = cal.get(Calendar.YEAR);
        String strriqi = String.format("%d-%02d-%02d", y, m, d);

        ArrayList<String> names = StockConstants.getSockNames();
        for (int i = 0; i < names.size(); i++) {
            if (StockServiceImp.getInstance().getStockIndex(strriqi, names.get(i)) == null) {
                bRet = false;
                break;
            }
        }

        return bRet;
    }

    public static void updateHourly(StockModelManager smmManager, OptionManager opm, PrintWriter out) {
        try {
            DataOption dataop = opm.getDataOption();
            Calendar cal = Calendar.getInstance();
            int d = cal.get(Calendar.HOUR_OF_DAY);

            if (d > 15 && isTodayDataExist()) {

                // 因为sina和yanhoo关闭了历史数据接口
                // StockIndexFetcher.get10DayData_Sc(); // 更新指数数据

                // 生产最新m20等数据
                /*
                 * StockM20 m20 = new StockM20(); m20.weeklyIndex(); StockMGEM
                 * tmp = new StockMGEM(); tmp.weeklyIndex(); Stock50GEM nn = new
                 * Stock50GEM(); nn.weeklyIndex();
                 * 
                 * StockGEMEx tmp3 = new StockGEMEx(); tmp3.weeklyIndex();
                 */

                smmManager.weekly(StockConstants.MODEL_300_500, "sh000300", "sh000905", 20);
                smmManager.weekly(StockConstants.MODEL_300_CHUANGYEBAN, "sh000300", "sz399006", 20);
                smmManager.weekly(StockConstants.MODEL_50_CHUANGYEBAN, "sh000016", "sz399006", 20);
                smmManager.weekly(StockConstants.MODEL_CHUANGYEBANEXEX, "sz399006", "", 20);

                if (dataop.getStockDataNum() > 0) {
                    // 因为sina和yanhoo关闭了历史数据接口
                    // 得到股票的数据
                    // StockDataFetcher.getTenDayData_Sc();
                }
            }

            // 删除过多newsmth记录
            NewsmthServiceImp.getInstance().deleteOld(dataop.getNewsmthNum());
            KongQiZhiLiangService.getInstance().deleteOld(dataop.getBeijingQuality());

            FetchData.fetchAll_Sc(); // 更新北京房地产数据和空气质量

            if (dataop.getNewsmthNum() > 0)
                NewsmthFetchData.fetchAll_sc();

        } catch (Exception e) {
            if (out != null)
                e.printStackTrace(out);
        }
    }

    @Override
    public void run() {
        UpdateHourlyThread.updateHourly(smmManager, opm, null);
    }

}
