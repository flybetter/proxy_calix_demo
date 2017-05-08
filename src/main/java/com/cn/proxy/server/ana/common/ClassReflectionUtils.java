/** 
 * Project Name:socket_netty 
 * File Name:ClassReflectionUtils.java 
 * Package Name:com.calix.bseries.server.ana.common 
 * Date:18 Sep, 2016
 * Copyright (c) 2016, Calix All Rights Reserved. 
 * 
 */
package com.cn.proxy.server.ana.common;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * ClassName:ClassReflectionUtils <br/>
 * Function: a)get Class with className <br/>
 * b)get all parameters of class<br/>
 * c)set parameter of all parameters on class<br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 18 Sep, 2016 <br/>
 * 
 * @author Tony Ben
 * @version
 * @since JDK 1.6
 * @see
 */
public class ClassReflectionUtils {
	private static final String POINT_CHAR = ".";
	private static final Logger log = Logger
			.getLogger(ClassReflectionUtils.class);

	/**
	 * Function:getClassObject<br/>
	 * Conditions:TODO<br/>
	 * WorkFlow:TODO<br/>
	 * UserGuide:TODO<br/>
	 * Remark:TODO<br/>
	 * 
	 * @author Tony Ben
	 * @param packageName
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @since JDK 1.6
	 */
	public static Object getClassObject(String packageName, String className)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		String name = packageName + POINT_CHAR + className;
		Class cls = Class.forName(name);
		return cls.newInstance();
	}

	/**
	 * Function:getDeclaredMethod<br/>
	 * Conditions:TODO<br/>
	 * WorkFlow:TODO<br/>
	 * UserGuide:TODO<br/>
	 * Remark:TODO<br/>
	 * 
	 * @author Tony Ben
	 * @param object
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 * @since JDK 1.6
	 */
	public static Method getDeclaredMethod(Object object, String methodName,
			Class<?>... parameterTypes) {
		Method method = null;
		for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz
				.getSuperclass()) {
			try {
				method = clazz.getDeclaredMethod(methodName, parameterTypes);
				return method;
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		return null;
	}

	/**
	 * Function:invokeMethod<br/>
	 * Conditions:TODO<br/>
	 * WorkFlow:TODO<br/>
	 * UserGuide:TODO<br/>
	 * Remark:TODO<br/>
	 * 
	 * @author Tony Ben
	 * @param object
	 * @param methodName
	 * @param parameterTypes
	 * @param parameters
	 * @return
	 * @since JDK 1.6
	 */
	public static Object invokeMethod(Object object, String methodName,
			Class<?>[] parameterTypes, Object[] parameters) {
		Method method = getDeclaredMethod(object, methodName, parameterTypes);

		method.setAccessible(true);

		try {
			if (null != method) {
				return method.invoke(object, parameters);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Function:getDeclaredField<br/>
	 * Conditions:TODO<br/>
	 * WorkFlow:TODO<br/>
	 * UserGuide:TODO<br/>
	 * Remark:TODO<br/>
	 * 
	 * @author Tony Ben
	 * @param object
	 * @param fieldName
	 * @return
	 * @since JDK 1.6
	 */
	public static Field getDeclaredField(Object object, String fieldName) {
		Field field = null;
		Class<?> clazz = object.getClass();
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				field = clazz.getDeclaredField(fieldName);
				return field;
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * Function:setFieldValue<br/>
	 * Conditions:TODO<br/>
	 * WorkFlow:TODO<br/>
	 * UserGuide:TODO<br/>
	 * Remark:TODO<br/>
	 * 
	 * @author Tony Ben
	 * @param object
	 * @param fieldName
	 * @param value
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @since JDK 1.6
	 */
	public static void setFieldValue(Object object, String fieldName,
			Object value) throws IllegalArgumentException, IllegalAccessException {
		if (value == null) {
			return;
		}
		Field field = getDeclaredField(object, fieldName);
		if (field == null) {
			log.error("the parameter [" + fieldName + "] is not exist on "
					+ object.getClass().getName());
			return;
		}
		field.setAccessible(true);
		// Integer
		if (field.getGenericType().toString().equals("class java.lang.Integer")) {
			if (String.valueOf(value).trim().toLowerCase().equals("true")) {
				field.set(object, 1);
			} else if (String.valueOf(value).trim().toLowerCase()
					.equals("false")) {
				field.set(object, 0);
			} else {
				field.set(object, Integer.parseInt(String.valueOf(value)));
			}
		}
		// Double
		else if (field.getGenericType().toString()
				.equals("class java.lang.Double")) {
			field.set(object, Double.parseDouble(String.valueOf(value)));
		}
		// Boolean
		else if (field.getGenericType().toString()
				.equals("class java.lang.Boolean")) {
			field.set(object, Double.parseDouble(String.valueOf(value)));
		} else {
			field.set(object, value);
		}
	}

	/**
	 * Function:getFieldValue<br/>
	 * 
	 * @author Tony Ben
	 * @param object
	 * @param fieldName
	 * @return
	 * @since JDK 1.6
	 */
	public static Object getFieldValue(Object object, String fieldName) {

		Field field = getDeclaredField(object, fieldName);
		if(field==null){
			return null;
		}
		field.setAccessible(true);
		try {
			return field.get(object);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}
		return null;
	}

	/**
	 * Function:getAllClassByInterface<br/>
	 * 
	 * @author Tony Ben
	 * @param c
	 * @return
	 * @since JDK 1.6
	 */
	public static List<Class> getAllClassByInterface(Class c) {
		List<Class> returnClassList = null;
		if (c.isInterface()) {
			String packageName = c.getPackage().getName();
			List<Class<?>> allClass = getClasses(packageName);
			if (allClass != null) {
				returnClassList = new ArrayList<Class>();
				for (Class classes : allClass) {
					if (c.isAssignableFrom(classes)) {
						if (!c.equals(classes)) {
							returnClassList.add(classes);
						}
					}
				}
			}
		}

		return returnClassList;
	}

	/**
	 * Function:getPackageAllClassName<br/>
	 * 
	 * @author Tony Ben
	 * @param classLocation
	 * @param packageName
	 * @return
	 * @since JDK 1.6
	 */
	public static String[] getPackageAllClassName(String classLocation,
			String packageName) {
		String[] packagePathSplit = packageName.split("[.]");
		String realClassLocation = classLocation;
		int packageLength = packagePathSplit.length;
		for (int i = 0; i < packageLength; i++) {
			realClassLocation = realClassLocation + File.separator
					+ packagePathSplit[i];
		}
		File packeageDir = new File(realClassLocation);
		if (packeageDir.isDirectory()) {
			String[] allClassName = packeageDir.list();
			return allClassName;
		}
		return null;
	}

	public static List<Class<?>> getClasses(String packageName) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		boolean recursive = true;
		String packageDirName = packageName.replace('.', '/');
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader()
					.getResources(packageDirName);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				if ("file".equals(protocol)) {
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					findAndAddClassesInPackageByFile(packageName, filePath,
							recursive, classes);
				} else if ("jar".equals(protocol)) {
					JarFile jar;
					try {
						jar = ((JarURLConnection) url.openConnection())
								.getJarFile();
						Enumeration<JarEntry> entries = jar.entries();
						while (entries.hasMoreElements()) {
							JarEntry entry = entries.nextElement();
							String name = entry.getName();
							if (name.charAt(0) == '/') {
								name = name.substring(1);
							}
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								if (idx != -1) {
									packageName = name.substring(0, idx)
											.replace('/', '.');
								}
								if ((idx != -1) || recursive) {
									if (name.endsWith(".class")
											&& !entry.isDirectory()) {
										String className = name.substring(
												packageName.length() + 1,
												name.length() - 6);
										try {
											classes.add(Class
													.forName(packageName + '.'
															+ className));
										} catch (ClassNotFoundException e) {
											e.printStackTrace();
										}
									}
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return classes;
	}

	/**
	 * findAndAddClassesInPackageByFile
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	public static void findAndAddClassesInPackageByFile(String packageName,
			String packagePath, final boolean recursive, List<Class<?>> classes) {
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		File[] dirfiles = dir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return (recursive && file.isDirectory())
						|| (file.getName().endsWith(".class"));
			}
		});
		for (File file : dirfiles) {
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(
						packageName + "." + file.getName(),
						file.getAbsolutePath(), recursive, classes);
			} else {
				String className = file.getName().substring(0,
						file.getName().length() - 6);
				try {
					classes.add(Class.forName(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
