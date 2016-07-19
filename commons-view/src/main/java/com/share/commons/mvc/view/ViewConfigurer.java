package com.share.commons.mvc.view;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.List;

public class ViewConfigurer extends FreeMarkerConfigurer{
	
	protected void postProcessTemplateLoaders(List<TemplateLoader> templateLoaders) {
		
	  // super.postProcessTemplateLoaders(templateLoaders);
	    
		templateLoaders.add(new ClassTemplateLoader(ViewConfigurer.class, ""));
	}

	protected void postProcessConfiguration(Configuration config) throws IOException, TemplateException {
	 
		 
		 config.setCacheStorage(new  freemarker.cache.MruCacheStorage(100,5000));
	     
		 DirectiveUtils.exposeRapidMacros(config);
		 config.setClassicCompatible(true);
		
  	}
}
