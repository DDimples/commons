package com.share.commons.mvc.view;

import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

public class ViewPageResolver extends FreeMarkerViewResolver {

	@Override
	protected void initApplicationContext() {

		super.initApplicationContext();
		this.setAttributesMap(ViewStaticMethod.getStaticList());
       
	}

}
