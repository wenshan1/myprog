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
    private static int stockModleCount = 0;
    private static int newsmthCount = 0;
    private static int beijingFandicanCount = 0;
    private static int beijingKongQiCount = 0;

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

    public static void updateMinute(StockModelManager smmManager, OptionManager opm, PrintWriter out) {
        try {
            stockModleCount++;
            newsmthCount++;
            beijingFandicanCount++;
            beijingKongQiCount++;

            DataOption dataop = opm.getDataOption();
            
            //Calendar cal = Calendar.getInstance();
            //int d = cal.get(Calendar.HOUR_OF_DAY);

            if ((stockModleCount > dataop.getStockUpdateCycle())) {
                stockModleCount = 0;

                smmManager.weekly(StockConstants.MODEL_300_500, "sh000300", "sh000905", 20);
                smmManager.weekly(StockConstants.MODEL_300_CHUANGYEBAN, "sh000300", "sz399006", 20);
                smmManager.weekly(StockConstants.MODEL_50_CHUANGYEBAN, "sh000016", "sz399006", 20);
                smmManager.weekly(StockConstants.MODEL_CHUANGYEBANEXEX, "sz399006", "", 20);

            }

            if (beijingFandicanCount > 60 * 4) {
                beijingFandicanCount = 0;
                FetchData.fetchAll_FandDiCan(); // 更新北京房地产数据
            }

            if (beijingKongQiCount > 60) {
                beijingKongQiCount = 0;
                KongQiZhiLiangService.getInstance().deleteOld(dataop.getBeijingQuality());
                FetchData.fetchAll_KongQi(); // 更新空气质量
            }

            if (newsmthCount > 60) {
                newsmthCount = 0;
                // 删除过多newsmth记录
                NewsmthServiceImp.getInstance().deleteOld(dataop.getNewsmthNum());
                if (dataop.getNewsmthNum() > 0)
                    NewsmthFetchData.fetchAll_sc();
            }

        } catch (Exception e) {
            if (out != null)
                e.printStackTrace(out);
        }
    }

    @Override
    public void run() {
        UpdateHourlyThread.updateMinute(smmManager, opm, null);
    }

}
