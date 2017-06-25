/**
 * Package: com.qingwei.common.util
 * Description: 
 */
package com.qingwei.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qingwei.common.exception.BeanException;

/**
 * Description:  
 * Date: 2017年6月25日 下午3:03:06
 * @author wufenyun 
 */
public class BeanUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtil.class);
	
	public static <T> T convert(Object source,Class<T> clazz) {
		if(null == source || null == clazz) {
			throw new IllegalArgumentException("source objcet or target class is null");
		}
		
		T target = null;
		try {
			target = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new BeanException("clazz get instance error",e);
		}
		
		copyProperties(source,target);
		return target;
	}
	
	public static void copyProperties(Object source,Object target) {
		if(null == source || null == target) {
			throw new IllegalArgumentException("source objcet or target object is null");
		}
	}
}
