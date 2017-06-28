/**
 * Package: com.qingwei.common.util
 * Description: 
 */
package com.qingwei.commonl.bean.uti;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qingwei.common.bean.util.support.AssertUtil;
import com.qingwei.common.exception.BeanException;

/**
 * Description:  
 * Date: 2017年6月25日 下午3:03:06
 * @author wufenyun 
 */
public class BeanUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtil.class);
	
	/** 
	 * Description:  
	 * @param source
	 * @param clazz
	 * @return
	 */
	public static <T> T convert(Object source,Class<T> clazz) {
		AssertUtil.notNull(source, "source  object is null");
		AssertUtil.notNull(clazz, "target calss is null");
		
		T target = null;
		try {
			target = clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new BeanException("clazz get instance error",e);
		}
		
		copyProperties(source,target);
		return target;
	}
	
	/** 
	 * Description:  
	 * @param source
	 * @param target
	 */
	public static void copyProperties(Object source,Object target) {
		copyProperties(source,target, null,false);
	}
	
	/** 
	 * Description:  
	 * @param source
	 * @param target
	 * @param ignoreProperties
	 * @param ignoreNullProperties
	 */
	public static void copyProperties(Object source, Object target, String[] ignoreProperties,
			boolean ignoreNullProperties) {
		AssertUtil.notNull(source, "source objcet is null");
		AssertUtil.notNull(target, "target objcet is null");
		try {
			Class<?> editable = target.getClass();
			BeanInfo beanInfo = Introspector.getBeanInfo(editable);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

			List<String> ignorePropertyList = (ignoreProperties == null ? null : Arrays.asList(ignoreProperties));

			for (PropertyDescriptor pd : propertyDescriptors) {
				String propertyName = pd.getName();
				if (ignorePropertyList != null && ignorePropertyList.contains(propertyName)) {
					continue;
				}

				// 获取源对象属性的值
				Method targetWriteMethod = pd.getWriteMethod();
				PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), propertyName);
				if (targetWriteMethod == null || null == sourcePd) {
					continue;
				}

				Method sourceReadMethod = sourcePd.getReadMethod();
				Object value = sourceReadMethod.invoke(source);
				targetWriteMethod.invoke(target, value);
			}
		} catch (Exception e) {
			LOGGER.error("Bean copy failed!", e);
			throw new BeanException("Bean copy failed!", e);
		}
	}

	/** 
	 * Description:  
	 * @param clazz
	 * @param propertyName
	 * @return
	 * @throws IntrospectionException
	 */
	public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz,String propertyName) throws IntrospectionException {
		AssertUtil.notNull(clazz,"Object class is null");
		AssertUtil.hasLength(propertyName,"Property name is blank");
		
		BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
		PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
		for(PropertyDescriptor pd:pds) {
			if(propertyName.equals(pd.getName())) {
				return pd;
			}
		}
		return null;
	}
}
