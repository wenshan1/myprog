package me.wenshan.web;

import java.io.PrintWriter;
import me.wenshan.backend.form.DataOption;
import me.wenshan.beijing.service.FetchData;
import me.wenshan.biz.OptionManager;
import me.wenshan.constants.StockConstants;
import me.wenshan.stockmodel.service.StockModelManager;

public class UpdateHourlyThread implements Runnable {
	private static int stockModleCount = 0;
	private static int beijingFandicanCount = 0;

	private StockModelManager smmManager;
	private OptionManager opm;
	private FetchData fetchData;
	
	public UpdateHourlyThread(StockModelManager smmManager, OptionManager opm, 
			FetchData fetchData) {
		this.opm = opm;
		this.smmManager = smmManager;
		this.fetchData = fetchData;
	}

	/*
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
	}*/

	public static void updateMinute(StockModelManager smmManager, OptionManager opm, 
			FetchData fetchData , PrintWriter out) {
        try {
   
            DataOption dataop = opm.getDataOption();
            
            //Calendar cal = Calendar.getInstance();
            //int d = cal.get(Calendar.HOUR_OF_DAY);

            stockModleCount++;
            if ((stockModleCount >= dataop.getStockUpdateCycle())) {
                stockModleCount = 0;

                smmManager.weekly(StockConstants.MODEL_300_500, "sh000300", "sh000905", 20);
                smmManager.weekly(StockConstants.MODEL_300_CHUANGYEBAN, "sh000300", "sz399006", 20);
                smmManager.weekly(StockConstants.MODEL_50_CHUANGYEBAN, "sh000016", "sz399006", 20);
                smmManager.weekly(StockConstants.MODEL_CHUANGYEBANEXEX, "sz399006", "", 20);

            }

            beijingFandicanCount++;
            if (beijingFandicanCount >= 60 * 4) {
                beijingFandicanCount = 0;
                fetchData.fetchAll_FandDiCan(); // 更新北京房地产数据
            }

        } catch (Exception e) {
            if (out != null)
                e.printStackTrace(out);
        }
    }

	@Override
	public void run() {
		UpdateHourlyThread.updateMinute(smmManager, opm, fetchData, null);
	}

}
