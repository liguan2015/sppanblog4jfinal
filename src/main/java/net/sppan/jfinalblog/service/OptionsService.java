package net.sppan.jfinalblog.service;

import java.util.List;

import net.sppan.jfinalblog.model.Options;

public class OptionsService {
	private final Options optionsDao = new Options().dao();
	public static final String optionsCacheName = "optionsCacheName";
	public static final OptionsService me  = new OptionsService();
	
	public List<Options> findAll(){
		return optionsDao.findByCache(optionsCacheName, "FINDALL", "SELECT * FROM tb_options");
	}

}
