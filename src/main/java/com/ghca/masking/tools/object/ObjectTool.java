/**
 * @Title: ObjectUtils.java
 * @Package cn.com.ccms.common
 * @Description: TODO
 * Copyright: Copyright (c) 2011 
 * Company:永乐科技
 * 
 * @author  
 * @date 2015年4月10日 下午2:13:12
 * @version V1.0
 */

package com.ghca.masking.tools.object;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

/**
 * @ClassName: ObjectUtils
 * @Description: TODO
 * @author  
 * @date 2015年4月10日 下午2:13:12
 *
 */

public class ObjectTool {

	/**
	 * @Title: fieldCoty @Description: 将制定对象的属性值复制给目标对象 @param @param
	 * source @param @param target @param @throws
	 * NoSuchMethodException @param @throws SecurityException @param @throws
	 * IllegalAccessException @param @throws
	 * IllegalArgumentException @param @throws InvocationTargetException
	 * 设定文件 @return void 返回类型 @throws
	 */
	public static void fieldCoty(Object source, Object target) throws Exception {
		// 得到源对象的Class
		Class sourceClass = source.getClass();
		// 得到目标对象的Class
		Class targetClass = target.getClass();
		
		// 得到源对象的所有属性
		List<Field> sourceFields = getAllField(sourceClass);
		// 得到目标对象的所有属性
		List<Field> targetFields = getAllField(targetClass);
		
		for (Field sourceField : sourceFields) {
			String name = sourceField.getName();// 属性名
			Class type = sourceField.getType();// 属性类型
			String methodName = name.substring(0, 1).toUpperCase() + name.substring(1);
			Method getMethod = sourceClass.getMethod("get" + methodName);// 得到属性对应get方法
			Object value = getMethod.invoke(source);// 执行源对象的get方法得到属性值
			for (Field targetField : targetFields) {
				String targetName = targetField.getName();// 目标对象的属性名
				if (targetName.equals(name)) {
					Method setMethod = targetClass.getMethod("set" + methodName, type);// 属性对应的set方法
					setMethod.invoke(target, value);// 执行目标对象的set方法
					break;
				}
			}
		}
	}

	/**
	 * @Title: listConvert
	 * @Description: list对象转换
	 * @param source
	 * @param targetClass
	 * @return
	 * @throws Exception
	 * @return: List
	 */
	public static List listConvert(List source, Class targetClass) throws Exception {

		List target = new ArrayList();
		Constructor constructor = targetClass.getDeclaredConstructor();

		for (Object object : source) {
			Object temp = constructor.newInstance();
			fieldCoty(object, temp);

			target.add(temp);
		}

		return target;
	}

	/**
	 * @Title: input2byte @Description: 将输入流转换成字节数组 @param @param
	 * inStream @param @return @param @throws IOException 设定文件 @return byte[]
	 * 返回类型 @throws
	 */
	public static byte[] input2byte(InputStream inStream) throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] in2b = swapStream.toByteArray();
		return in2b;
	}

	/**
	 * @Title: getObjectFieldValue
	 * @Description: 获取指定的对象的属性值
	 * @param obj
	 * @return
	 * @return: Map<String,String>
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static Map<String, String> getObjectFieldValue(Object obj) throws Exception {
		Map<String, String> map = new HashMap<String, String>();

		// 获得对象所有属性
		Field fields[] = obj.getClass().getDeclaredFields();
		Field field = null;

		for (int i = 0; i < fields.length; i++) {
			field = fields[i];
			field.setAccessible(true);// 修改访问权限

			if (field.get(obj) != null) {
				map.put(field.getName(), field.get(obj).toString());
			} else {
				map.put(field.getName(), "");
			}
		}
		return map;
	}

	/**
	 * @Title: getObjectByMap
	 * @Description: 根据map的key与对象属性的对应关系，将map里的key对应的value设置到对象属性上
	 * @param c
	 * @param map
	 * @return
	 * @return: Object
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static Object getObjectByMap(Class c, Map<String, Object> map) {
		Object o = null;
		try {
			o = c.newInstance();
			List<Field> fieldList = new ArrayList<>();
			while (c != null){
				fieldList.addAll(new ArrayList<>(Arrays.asList(c.getDeclaredFields())));
				c = c.getSuperclass();
			}
			Field[] fields = new Field[fieldList.size()];
			fields = fieldList.toArray(fields);

			for (Field field : fields) {
				if (map.get(field.getName()) == null) {
					continue;
				}

				Object value = map.get(field.getName());

				field.setAccessible(true);// 修改访问权限
				field.set(o, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return o;
	}

	public static void setObjectFieldByMap(Object bean, Map<String, Object> map) throws Exception {

		if (bean == null) {
			return;
		}

		for (Entry<String, Object> item : map.entrySet()) {
			String field = item.getKey();
			Object fieldvalue = item.getValue();

			String setfieldmethod = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);

			Method m = bean.getClass().getMethod(setfieldmethod);

			if (fieldvalue != null) {
				m.invoke(bean, fieldvalue);
			}
		}
	}
	
	
	/**
	* @Title: getAllField
	* @Description: 获取制定类的所有属性，包括父类里的属性
	* @param c
	* @return
	* @return: List<Field>
	*/
	public static List<Field> getAllField(Class c){
		List<Field> list = new ArrayList<Field>();
		list.addAll(Arrays.asList(c.getDeclaredFields()));
		Class sc = c.getSuperclass();

		while(!sc.getName().equals("java.lang.Object")){
			list.addAll(Arrays.asList(sc.getDeclaredFields()));
			sc = sc.getSuperclass();
		}
		
		return list;
	}
	
	/**
	* @Title: callObjectMethod
	* @Description: 调用对象用的指定方法
	* @param objClass
	* @param method
	* @param args
	* @return
	* @return: Object
	*/
	public static Object callObjectMethod(Class objClass,Method method,Object...args){
		
		if(objClass == null || method == null){
			return null;
		}
		
		Object result = null;
        try {
            Object obj = objClass.newInstance();
            result = method.invoke(obj,args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
		
	/**
	* @Title: getObjectMethod
	* @Description: 获取对象指定方法
	* @param objClass
	* @param methodName
	* @param argsClass
	* @return
	* @return: Method
	*/
	public static Method getObjectMethod(Class objClass,String methodName,Class...argsClass){
		
		if(objClass == null || methodName == null || "".equals(methodName.trim())){
			return null;
		}
		
		Method m = null;
		try {
			m = objClass.getMethod(methodName,argsClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return m;
	}
	
}





