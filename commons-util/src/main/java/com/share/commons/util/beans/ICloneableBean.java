/**   
* @Title: IClone.java 
* @Package com.elong.web.core.beans 
* @Description: TODO(深拷贝Bean继承loneable接口) 
* @author zcj   
* @date 2014年12月11日 下午10:33:43 
* @version V1.0   
*/
package com.share.commons.util.beans;

public interface ICloneableBean extends Cloneable {

	public Object clone() ;
}
