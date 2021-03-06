package me.wenshan.backend.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import me.wenshan.backend.form.DataOption;
import me.wenshan.backend.form.GeneralOption;
import me.wenshan.backend.form.PostOption;
import me.wenshan.backend.service.OptionService;
import me.wenshan.biz.OptionManager;
import me.wenshan.constants.StockConstants;
import me.wenshan.stockmodel.service.StockModelManager;
import me.wenshan.util.Global;
import me.wenshan.util.MapContainer;
import me.wenshan.util.ShowHelp;

@Controller
@RequestMapping("/backend/options")
public class OptionsController {
	@Autowired
	private OptionManager optionManager;
	@Autowired
	private OptionService optionsService;
	@Autowired 
	private StockModelManager  stockMM;

	@RequestMapping(value = "/general", method = RequestMethod.GET)
	public String general(Model model) {
		model.addAttribute("form", optionManager.getGeneralOption());
		return "backend/options/general";
	}

	@RequestMapping(value = "/general", method = RequestMethod.POST)	
	public String updateGeneral(@ModelAttribute GeneralOption form, Model model) {
		model.addAttribute("form", form);
		MapContainer result = OptionManager.validateGeneral(form);
		if (!result.isEmpty()) {
			model.addAllAttributes(result);
			return "backend/options/general";
		}
		
		optionManager.updateGeneralOption(form);
		model.addAttribute("success", true);
		Global.getDataFromDB(optionsService);
		return "backend/options/general";
	}
	
	@RequestMapping(value = "/post", method = RequestMethod.GET)
	public String post(Model model) {
	    model.addAttribute("form", optionManager.getPostOption());
	    return "backend/options/post";
	  }
	
	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public String updatePost(@ModelAttribute PostOption form, Model model) {
	    model.addAttribute("form", form);
	    MapContainer result = OptionManager.validatePost(form);
	    if(!result.isEmpty()){
	      model.addAllAttributes(result);
	      return "backend/options/post";
	    }

	    model.addAttribute("success", true);
	    optionManager.updatePostOption(form);
	    
	    return "backend/options/post";
	  }
	
	@RequestMapping(value = "/data", method = RequestMethod.GET)
	public String data(Model model) {
	    model.addAttribute("form", optionManager.getDataOption ());
	    model.addAttribute("stocks", StockConstants.getSockNames());
	    model.addAttribute("showhelp", new ShowHelp ());
	    
	    return "backend/options/data";
	  }
	
	@RequestMapping(value = "/data", method = RequestMethod.POST)
	public String updateData(@ModelAttribute DataOption  form, Model model) {
	    model.addAttribute("form", form);
	    model.addAttribute("stocks", StockConstants.getSockNames());
	    model.addAttribute("showhelp", new ShowHelp ());
	    
	    MapContainer result = OptionManager.validateData(form);
	    if(!result.isEmpty()){
	      model.addAllAttributes(result);
	      return "backend/options/data";
	    }

	    model.addAttribute("success", true);
	    optionManager.updateDataOption(form);
	    stockMM.genModelData (StockConstants.MODEL_CUSTOME, form.getStockModelName1(), 
	            form.getStockModelName2(), form.getStockModelCycle());
	    	    
	    return "backend/options/data";
	  }

}
