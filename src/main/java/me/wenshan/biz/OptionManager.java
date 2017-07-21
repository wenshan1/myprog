package me.wenshan.biz;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.wenshan.backend.form.DataOption;
import me.wenshan.backend.form.GeneralOption;
import me.wenshan.backend.form.PostOption;
import me.wenshan.backend.service.OptionService;
import me.wenshan.constants.OptionConstants;
import me.wenshan.constants.StockConstants;
import me.wenshan.util.*;

@Component
public class OptionManager {
    @Autowired
    private OptionService optionsService;

    public GeneralOption getGeneralOption() {
        GeneralOption form = new GeneralOption();
        form.setTitle(optionsService.getOptionValue(OptionConstants.TITLE));

        if (form.getTitle().length() != 0) {
            form.setSubtitle(optionsService.getOptionValue(OptionConstants.SUBTITLE));
            form.setDescription(optionsService.getOptionValue(OptionConstants.DESCRIPTION));
            form.setKeywords(optionsService.getOptionValue(OptionConstants.KEYWORDS));
            form.setWeburl(optionsService.getOptionValue(OptionConstants.WEB_URL));
            form.setEmail(optionsService.getOptionValue(OptionConstants.EMAIL));
        } else {
            form = null;
        }
        return form;
    }

    public PostOption getPostOption() {
        PostOption option = new PostOption();
        option.setMaxshow(NumberUtils.toInteger(optionsService.getOptionValue(OptionConstants.MAXSHOW), 10));
        option.setAllowComment(Boolean.parseBoolean(optionsService.getOptionValue(OptionConstants.ALLOW_COMMENT)));

        return option;
    }

    public void updatePostOption(PostOption form) {
        optionsService.updateOptionValue(OptionConstants.MAXSHOW, form.getMaxshow() + "");
        optionsService.updateOptionValue(OptionConstants.ALLOW_COMMENT, form.isAllowComment() + "");
    }

    public void updateGeneralOption(GeneralOption form) {
        optionsService.updateOptionValue(OptionConstants.TITLE, form.getTitle());
        optionsService.updateOptionValue(OptionConstants.SUBTITLE, form.getSubtitle());
        optionsService.updateOptionValue(OptionConstants.DESCRIPTION, form.getDescription());
        optionsService.updateOptionValue(OptionConstants.KEYWORDS, form.getKeywords());
        optionsService.updateOptionValue(OptionConstants.EMAIL, form.getEmail());
        optionsService.updateOptionValue(OptionConstants.WEB_URL, form.getWeburl());
    }

    public static MapContainer validateGeneral(GeneralOption option) {
        MapContainer form = new MapContainer();
        if (StringUtils.isBlank(option.getTitle())) {
            form.put("title", "需填写站点名称名称");
        }
        if (StringUtils.isBlank(option.getSubtitle())) {
            form.put("subtitle", "需填写副标题");
        }
        if (StringUtils.isBlank(option.getDescription())) {
            form.put("description", "需填写站点描述");
        }
        if (StringUtils.isBlank(option.getKeywords())) {
            form.put("keywords", "需填写站点关键字");
        }
        if (StringUtils.isBlank(option.getWeburl())) {
            form.put("weburl", "需填写网站url");
        }
        return form;
    }

    public static MapContainer validatePost(PostOption option) {
        MapContainer form = new MapContainer();
        if (option.getMaxshow() < 1) {
            form.put("maxshow", "格式错误");
        }

        return form;
    }

    public static MapContainer validateData(DataOption option) {
        MapContainer form = new MapContainer();
        boolean bStock1 = false;
        boolean bStock2 = false;
        ArrayList<String> stockList = StockConstants.getSockNames();

        if (option.getBeijingQuality() < 1) {
            form.put("beijingQuality", "数据应该大于1");
        }
        if (option.getNewsmthNum() < 0) {
            form.put("newsmthNum", "数据不能为负数");
        }
        if (option.getStockDataNum() < 0) {
            form.put("stockDataNum", "数据不能为负数");
        }

        if (option.getStockModelCycle() < 2) {
            form.put("stockModelCycle", "数据应该大于2");
        }

        if (option.getStockUpdateCycle() < 1) {
            form.put("StockUpdateCycle", "数据应该大于1");
        }

        if (option.getStockModelName2().isEmpty())
            bStock2 = true;
        for (int i = 0; i < stockList.size(); i++) {
            String str = stockList.get(i);
            if (str.compareTo(option.getStockModelName1()) == 0) {
                bStock1 = true;
            }
            if (str.compareTo(option.getStockModelName2()) == 0) {
                bStock2 = true;
            }
        }

        if (!bStock1) {
            form.put("stockModelName1", "指数名字不对");
        }
        if (!bStock2) {
            form.put("stockModelName2", "指数名字不对");
        }
        return form;
    }

    public void updateDataOption(DataOption form) {
        optionsService.updateOptionValue(OptionConstants.BEIJING_QUALITY_MAX, form.getBeijingQuality() + "");
        optionsService.updateOptionValue(OptionConstants.NEWSMTH_MAX, form.getNewsmthNum() + "");
        optionsService.updateOptionValue(OptionConstants.STOCKDATA_YEAR_MAX, form.getStockDataNum() + "");

        optionsService.updateOptionValue(OptionConstants.STOCK_MODEL_CYCLE, form.getStockModelCycle() + "");
        optionsService.updateOptionValue(OptionConstants.STOCK_MODEL_NAME_1, form.getStockModelName1() + "");
        optionsService.updateOptionValue(OptionConstants.STOCK_MODEL_NAME_2, form.getStockModelName2() + "");
        optionsService.updateOptionValue(OptionConstants.STOCK_UPDATE_CYCLE, form.getStockUpdateCycle() + "");
    }

    public DataOption getDataOption() {
        DataOption dataOption = new DataOption();
        dataOption.setBeijingQuality(
                NumberUtils.toInteger(optionsService.getOptionValue(OptionConstants.BEIJING_QUALITY_MAX), 1000000));
        dataOption
                .setNewsmthNum(NumberUtils.toInteger(optionsService.getOptionValue(OptionConstants.NEWSMTH_MAX), 1000));
        dataOption.setStockDataNum(
                NumberUtils.toInteger(optionsService.getOptionValue(OptionConstants.STOCKDATA_YEAR_MAX), 10));

        dataOption.setStockModelCycle(
                NumberUtils.toInteger(optionsService.getOptionValue(OptionConstants.STOCK_MODEL_CYCLE), 20));
        dataOption.setStockUpdateCycle(
                NumberUtils.toInteger(optionsService.getOptionValue(OptionConstants.STOCK_UPDATE_CYCLE), 60));

        dataOption.setStockModelName1(optionsService.getOptionValue(OptionConstants.STOCK_MODEL_NAME_1));
        dataOption.setStockModelName2(optionsService.getOptionValue(OptionConstants.STOCK_MODEL_NAME_2));

        return dataOption;
    }
}
