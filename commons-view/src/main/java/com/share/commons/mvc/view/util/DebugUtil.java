package com.share.commons.mvc.view.util;

import java.io.FileNotFoundException;
import java.io.IOException;


public class DebugUtil {

	public static boolean isDebug() throws ClassNotFoundException, FileNotFoundException, IOException
	{
//		ResourceUtil.getWebProperties().get("debug")
		 return Boolean.parseBoolean("true");
	}
}
