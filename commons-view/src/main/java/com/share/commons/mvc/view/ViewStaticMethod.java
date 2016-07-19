package com.share.commons.mvc.view;

import com.share.commons.mvc.view.util.DebugUtil;
import com.share.commons.mvc.view.util.UrlUtil;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateModelException;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class ViewStaticMethod {

	
	private static Class[] defaultStaticClasses = { UrlUtil.class,
			DebugUtil.class,URLEncoder.class,URLDecoder.class};


	public static Map<String, Object> getStaticList() {

		Map<String, Object> methodList = new HashMap<String, Object>();

		for (Class clz : defaultStaticClasses) {
			String name = clz.getSimpleName();
			methodList.put(name, getStaticModel(clz));
		}
		return methodList;
	}
	
	 
    private static Object getStaticModel(Class clz) {  
        BeansWrapper wrapper = BeansWrapper.getDefaultInstance();  
        try {  
            return wrapper.getStaticModels().get(clz.getName());  
        } catch (TemplateModelException e) {  
            //e.printStackTrace();  
        }  
        return null;  
    }  

}
