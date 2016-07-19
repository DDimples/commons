package com.share.commons.mvc.view;

import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ViewPage extends FreeMarkerView{

	@Override
	protected void processTemplate(Template template, SimpleHash model, HttpServletResponse response)
			throws IOException, TemplateException {
    
		template.process(model, response.getWriter());
	
	
	}
	
}
