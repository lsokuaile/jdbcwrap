
/**
* Copyright © 2021 . All rights reserved.
*
* @Title: StringTool.java
* @Prject: ghca-tools
* @Package: com.ghca.tools.string
* @Description: TODO
* @author: 程明
* @date: 2021年11月10日 下午2:35:58
* @version: V1.0
*/
package com.p6spy.engine.maskingtool;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * @ClassName: StringTool
 * @Description: TODO
 * @author: 程明
 * @date: 2021年11月10日 下午2:35:58
 */
public class StringTool {

	// 十六进制下数字到字符的映射数组
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	/**
	 * 把inputString加密
	 */
	public static String generatePassword(String inputString) {
		return encodeByMD5(inputString);
	}

	public static String getTablePrimaryKey() {

		String uuid = UUID.randomUUID().toString().replace("-", "");

		return uuid;
	}

	/** 对字符串进行MD5加密 */
	public static String encodeByMD5(String originString) {
		if (originString != null) {
			try {
				// 创建具有指定算法名称的信息摘要
				MessageDigest md = MessageDigest.getInstance("MD5");
				// 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
				byte[] results = md.digest(originString.getBytes());
				// 将得到的字节数组变成字符串返回
				String resultString = byteArrayToHexString(results);
				return resultString.toUpperCase();
			} catch (Exception ex) {
			}
		}
		return null;
	}

	/**
	 * 转换字节数组为十六进制字符串
	 * 
	 * @param b 字节数组
	 * @return 十六进制字符串
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/** 将一个字节转化成十六进制形式的字符串 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String charTransfor(String s) {
		String str = null;

		switch (s) {
		case ".":
			str = "u002E";
			break;
		case "$":
			str = "u0024";
			break;
		case "^":
			str = "u005E";
			break;
		case "{":
			str = "u007B";
			break;
		case "[":
			str = "u005B";
			break;
		case "(":
			str = "u0028";
			break;
		case "|":
			str = "\\|";
			break;
		case ")":
			str = "u0029";
			break;
		case "*":
			str = "u002A";
			break;
		case "+":
			str = "u002B";
			break;
		case "?":
			str = "u003F";
			break;
		default:
			break;
		}

		if (str != null) {
			return str;
		}

		return s;
	}

	/**
	 * @Title: hanziToPinyin @Description: 汉字转换成拼音 @param @param
	 * s @param @return 设定文件 @return String 返回类型 @throws
	 */
	public static String hanziToPinyin(String s) {
		String pinyinName = "";
		char[] nameChar = s.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				pinyinName += nameChar[i];
			}
		}
		return pinyinName;
	}
	/**
	 * @Title: hanziToPinyin @Description: 汉字转换成拼音 @param @param
	 * s @param @return 设定文件 @return String 返回类型 @throws
	 */
	public static String hanziToPinyinFirst(String s) {
		String pinyinName = "";
		char[] nameChar = s.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				pinyinName += nameChar[i];
			}
		}
		return pinyinName;
	}
	public static String stringToAscii(String str) {

		char[] chars = str.toCharArray(); // 把字符中转换为字符数组

		System.out.println("\n\n汉字 ASCII\n----------------------");
		for (int i = 0; i < chars.length; i++) {// 输出结果

			System.out.println(" " + chars[i] + " " + (int) chars[i]);
		}

		return null;
	}


	/**
	* @Title: emptyOrNull
	* @Description: 判断字符串是空或者是null
	* @param str
	* @return
	* @return: boolean
	*/
	public static boolean emptyOrNull(String str) {
		
		if (str == null || "".equals(str)) {
			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}
	
	
	/**
	* @Title: getSubStringIndex
	* @Description: 获取子字符串在字符串中的位置列表
	* @param str
	* @param substring
	* @return
	* @return: List<Integer>
	*/
	public static List<Integer> getSubStringIndex(String str,String substring){
		int length = substring.length();
		
		if(length == 0){
			return null;
		}
		
		List<Integer> list = new ArrayList<Integer>();
		int index = 0;
		
		for(int i=0;i<str.length();i++){
			
			String temp = str.substring(index, index + length);
			
			if(temp.equals(substring)){
				list.add(index);
				i +=length;
			}
			index = i;
		}
		
		return list;
	}
	
	
	
	
	
	
	
	

	/**
	 * @Title: widthSpliter
	 * @Description: 按照固定宽度分割字符串
	 * @param inputString
	 * @return
	 * @return: List<String>
	 */
	public static List<String> widthSpliter(String inputString, int byteLength) {
		List list = null;

		int length = inputString.length();
		int size = length / byteLength;

		for (int i = 0; i < size; i++) {

			if (list == null) {
				list = new ArrayList<String>();
			}

			String substring = inputString.substring(i * byteLength, i * byteLength + byteLength);
			list.add(substring);
		}

		return list;
	}

	/**
	 * @Title: charSpliter
	 * @Description: 指定分隔符来对字符串进行分割
	 * @param inputString
	 * @param ch
	 * @return
	 * @return: List<String>
	 */
	public static List<String> charSpliter(String inputString, String ch) {

		String[] array = StringUtils.split(inputString, ch);
		List<String> list = Arrays.asList(array);

		return list;
	}

	/**
	 * @Title: charSpliter
	 * @Description: 分隔字符串
	 * @param inputString：要分隔的字符串
	 * @param ch：分隔符
	 * @param seria：连续是否为一
	 * @return
	 * @return: List<String>
	 */
	public static List<String> charSpliter(String inputString, String ch, String seria) {

		List<String> list = new ArrayList<String>();

		if (inputString.indexOf(ch) < 0) {
			list.add(inputString);
			return list;
		}

		if ("0".equals(seria)) {// 连续不为一
			String[] array = StringUtils.splitPreserveAllTokens(inputString, ch);
			list = Arrays.asList(array);
			return list;
		}

		if ("1".equals(seria)) {// 连续为一
			if (inputString.indexOf(ch) == 0) {
				list.add(0, "");
			}
			String[] array = StringUtils.splitByWholeSeparator(inputString, ch);
			list.addAll(Arrays.asList(array));

			return list;
		}

		return list;
	}

	/**
	 * @Title: StringSpliter
	 * @Description: 分割title
	 * @param inputString
	 * @param ch
	 * @param multi
	 * @return
	 * @return: List<String>
	 */
	public static List<String> StringSpliter(String inputString, String ch, String multi) {

		List<String> list = new ArrayList<String>();

		if ("0".equals(multi)) {// 多个不相加
			String[] array = StringUtils.splitPreserveAllTokens(inputString, ch);
			list = Arrays.asList(array);

			return list;
		}

		if ("1".equals(multi)) {// 多个相加
			char[] c = ch.toCharArray();
			String temp = "";

			for (int i = 0; i < c.length; i++) {
				temp += (String.valueOf(c[i]).equals("|") ? "\\|" : String.valueOf(c[i]));
			}

			String[] array = inputString.split(temp);
			list = Arrays.asList(array);
			return list;
		}

		return list;
	}

	/**
	 * @Title: widthSpliter
	 * @Description: 按固定宽度分割一个字符串
	 * @param str
	 * @param width
	 * @return
	 * @return: List<String>
	 */
	public static List<String> widthSpliter(String str, String w) {
		List<String> list = new ArrayList<String>();
		String[] width = w.split(",");
		int start = 0;
		for (int i = 0; i < width.length; i++) {
			int end = Integer.valueOf(width[i]);
			list.add(str.substring(start, end));

			start = end;
		}
 
		return list;
	}
	
	/**
	* @Title: widthSpliter
	* @Description: 按固定宽度拆分字符串
	* @param str
	* @param w
	* @return
	* @return: List<String>
	*/
	public static List<String> widthSpliter(String str, List<String> w) {

		StringBuffer sb = new StringBuffer();
		int temp = 0;
		
		for (int i = 0; i < w.size(); i++) {
			if (i == 0) {
				sb.append(String.valueOf(Integer.valueOf(w.get(i))));
				temp = Integer.valueOf(w.get(i));
			} else {
				sb.append(",");
				temp += Integer.valueOf(w.get(i));
				sb.append(String.valueOf(temp));
			}
		}
		/*sb.append(",");
		sb.append(String.valueOf(str.length() - Integer.valueOf(w.get(w.size() - 1)) - 1));*/
		return widthSpliter(str, sb.toString());
	}
	
	/**
	* @Title: widthSpliter
	* @Description: 按固定宽度拆分字符串
	* @param str
	* @param w
	* @return
	* @return: List<List<String>>
	*/
	public static List<List<String>> widthSpliter(List<String> str, List<String> w) {
		List<List<String>> list = new ArrayList<List<String>>();

		for(String item : str){
			List<String> temp = widthSpliter(item, w);
			list.add(temp);
		}
		
		List<List<String>> result = new ArrayList<List<String>>();
		
		for(int i=0;i<w.size();i++){
			List<String> temp = new ArrayList<String>();
			for(List<String> item : list){
				temp.add(item.get(i));
			}
			result.add(temp);
		}
		
		return result;
	}
	

	/**
	 * @Title: stringNumbers
	 * @Description: 获取字符串中子串数量
	 * @param str
	 * @param substring
	 * @return
	 * @return: int
	 */
	public static int substringNumbers(String str, String substring) {

		if (str.indexOf(substring) == -1) {

			return 0;
		} else if (str.indexOf(substring) != -1) {
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			String s = str.replaceAll(substring, uuid);

			String[] arr = s.split(uuid);
			return arr.length - 1;
		}
		return 0;
	}

	
	
	/**
	* @Title: splitString
	* @Description: 按照指定字符拆分字符串
	* @param str
	* @param s
	* @return
	* @return: List<String>
	*/
	public static List<String> splitString(String str,char split){
		List<String> list = new ArrayList<String>();
		
		int index = 0;
		
		for(int i = 0;i<str.length();i++){
			if(str.charAt(i) == split){
				list.add(str.substring(index, i));
				index = i+1;
			}
		}
		list.add(str.substring(index,str.length()));
		return list;
	}
	
	/**
	* @Title: splitString
	* @Description: 按照指定字符拆分字符串
	* @param str
	* @param s
	* @return
	* @return: List<String>
	*/
	public static List<List<String>> splitString(List<String> str,String split,int num){
		List<List<String>> list = new ArrayList<List<String>>();
		List<List<String>> result = new ArrayList<List<String>>();
	
		for(String item : str){
			List<String> temp = splitString(item, split, false);
			if(temp.size()==num){
				list.add(temp);
			}
		}
		
		int size = list.get(0).size();
		
	 
		
		for(int i=0;i<size;i++){
			List<String> temp = new ArrayList<String>();
			for(List<String> item : list){
				temp.add(item.get(i));
			}
			result.add(temp);
		}
	
		
		return result;
	}
	
	/**
	* @Title: splitString
	* @Description: 根据字符串中任意字符进行拆分字符串
	* @param str
	* @param split
	* @return
	* @return: List<String>
	*/
	public static List<String> splitString(String str,String split,boolean group){
		List<String> list = new ArrayList<String>();
		int index = 0;
		
		if(group){
			
			for(int i = 0;i<str.length();i++){
				if(split.length()+i > str.length()){
					break;
				}
				
				StringBuffer ac = new StringBuffer();
		    	for (int j = 0; j < split.length(); j++) {
		    		if(!isLetterDigitOrChinese(split.substring(j, j+1))){//如果是特殊符号
		    			ac.append("\\");
		    		}
		    	 
		    		ac.append(split.substring(j, j+1));
		    	 }
 
				String arr[] = str.split(ac.toString());
				 
				list = Arrays.asList(arr);
				}
			
		}else{
			char[] c = split.toCharArray();
			Arrays.sort(c);
			for(int i = 0;i<str.length();i++){
				if(Arrays.binarySearch(c, str.charAt(i)) >= 0){
					list.add(str.substring(index, i));
					index = i+1;
				}
			}
			list.add(str.substring(index,str.length()));
		}
		
		return list;
	}
	
	
	/**
	* @Title: splitString
	* @Description: 根据字符串中任意字符进行拆分字符串
	* @param str
	* @param split
	* @param group
	* @param escape
	* @return
	* @return: List<String>
	*/
	public static List<String> splitString(String str,String split,boolean group,String escape){
		//如果没有转义符
		if(escape == null || "".equals(escape)){
			return splitString(str,split,group);
		}
		
		List<String> list = new ArrayList<String>();
		int index = 0;
		
		if(group){
			for(int i = 0;i<str.length();i++){
				if(split.length()+i > str.length()){
					break;
				}
				StringBuffer ac = new StringBuffer();
		    	for (int j = 0; j < split.length(); j++) {
		    		if(!isLetterDigitOrChinese(split.substring(j, j+1))){//如果是特殊符号
		    			ac.append("\\");
		    		}
		    		ac.append(split.substring(j, j+1));
		    	 }
 
				String arr[] = str.split(ac.toString());
				 
				list = Arrays.asList(arr);
				}
				//判断当前循环的字符是否是转义符
				/*if(i > 0){
					String last = str.substring(i-1, i);
					if(last.equals(escape)){
						continue;
					}
				}
				
				String temp = str.substring(i,split.length()+i);
				
				if(temp.equals(split)){
					list.add(str.substring(index, i));
					index = i+split.length();
				}
			}
			list.add(str.substring(index,str.length()));*/
			
		}else{
			char[] c = split.toCharArray();
			Arrays.sort(c);
			for(int i = 0;i<str.length();i++){
				
				//判断当前循环的字符是否是转义符
				if(i > 0){
					String last = str.substring(i-1, i);
					if(last.equals(escape)){
						continue;
					}
				}
				
				if(Arrays.binarySearch(c, str.charAt(i)) >= 0){
					list.add(str.substring(index, i));
					index = i+1;
				}
			}
			list.add(str.substring(index,str.length()));
		}
		
		return list;
	}
	
	   public static boolean isLetterDigitOrChinese(String str) {
	    	  String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";//其他需要，直接修改正则表达式就好
	    	  return str.matches(regex);
	    	 }
	/**
	* @Title: splitString
	* @Description: 根据字符串中任意字符进行拆分字符串
	* @param str
	* @param split
	* @param group
	* @param escape
	* @param limit
	* @return
	* @return: List<String>
	*/
	public static List<String> splitString(String str,String split,boolean group,String escape,String limit){
		//如果没有转义符
		if(limit == null || "".equals(limit)){
			return splitString(str,split,group,escape);
		}
		
		List<Integer> indexlist = getSubStringIndex(str, limit);
		
		List<String> list = new ArrayList<String>();
		int index = 0;
		
		if(group){
			for(int i = 0;i<str.length();i++){
				if(split.length()+i > str.length()){
					break;
				}
				StringBuffer ac = new StringBuffer();
		    	for (int j = 0; j < split.length(); j++) {
		    		if(!isLetterDigitOrChinese(split.substring(j, j+1))){//如果是特殊符号
		    			ac.append("\\");
		    		}
		    		ac.append(split.substring(j, j+1));
		    	 }
 
				String arr[] = str.split(ac.toString());
				 
				list = Arrays.asList(arr);
			}
				//判断当前循环的字符是否是转义符
		/*		if(i > 0){
					String last = str.substring(i-1, i);
					if(last.equals(escape)){
						continue;
					}
				}
				
				String temp = str.substring(i,split.length()+i);
				
				if(temp.equals(split)){
					//判断当前的分隔符是否在两个文本限定符之间
					if(checkLimit(i,indexlist)){
						continue;
					}
					
					list.add(str.substring(index, i));
					index = i+split.length();
				}
			}
			list.add(str.substring(index,str.length()));*/
			
		}else{
			char[] c = split.toCharArray();
			Arrays.sort(c);
			for(int i = 0;i<str.length();i++){
				
				//判断当前循环的字符是否是转义符
				if(i > 0){
					String last = str.substring(i-1, i);
					if(last.equals(escape)){
						continue;
					}
				}
				
				if(Arrays.binarySearch(c, str.charAt(i)) >= 0){
					//判断当前的分隔符是否在两个文本限定符之间
					if(checkLimit(i,indexlist)){
						continue;
					}
					
					list.add(str.substring(index, i));
					index = i+1;
				}
			}
			list.add(str.substring(index,str.length()));
		}
		
		return list;
	}
	
	
	/**
	* @Title: checkLimit
	* @Description: 判断分隔符内是否存在于两个文本限定符中
	* @param index
	* @param indexlist
	* @return
	* @return: boolean
	*/
	public static boolean checkLimit(int index,List<Integer> indexlist){
		boolean result = false;
		
		for(int i=0;i<indexlist.size();i++){
			if(index > indexlist.get(i) && index < indexlist.get(i+1)){
				result = true;
				break;
			}
			i++;
		}
		
		return result;
	}
	
	/**
	* @Title: toUpperCaseList
	* @Description: List<String>中的String转换成大写
	* @param list
	* @return
	* @return: List<String>
	*/
	public static List<String> toUpperCaseList(List<String> list) {
		List<String> newlist = new ArrayList<>();
		for (String str : list) {
			newlist.add(str.toUpperCase());
		}
		return newlist;	
	}
	
	
	
//	public static void main(String[] args){
//		String str="abc,十分士大夫,dsdddfsddsd";
//		String split = "";
//		String limit = "\\d\\d";
//		
//		List<String> list = splitString(str,limit, true,null,null);
//		System.out.println(list);
//	}
	
	/**
     * 解析数据文件包含符规则
     * @param quoteCharacter
     * @return
     */
    public static String parseQuoteCharacter(String quoteCharacter){
	   	 if("none".equalsIgnoreCase(quoteCharacter)){
	   		 quoteCharacter = "";
	   	 }else if("Single".equalsIgnoreCase(quoteCharacter)){
	   		 quoteCharacter = "\'";
	   	 }else if("Double".equalsIgnoreCase(quoteCharacter)){
	   		 quoteCharacter = "\"";
	   	 }else{
	   		 quoteCharacter = "";
	   	 }
	   	 return quoteCharacter;
    }
	



	/**
	 * 字符串是否全部为英文字母组成
	 * @param str
	 * @return
	 */
	public static boolean isEnglish(String charaString){
	
	    return charaString.matches("^[a-zA-Z]*");
	}
	
	/**
	 * 字符串是否全部为中文组成
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str){
	    String regEx = "[\\u4e00-\\u9fa5]+";
	    Pattern p = Pattern.compile(regEx);
	    Matcher m = p.matcher(str);
	   if(m.find()){
		   return true;
	   }
	   else{
		   return false; 
	   }
	 }

	
	/**
	 * 查找子字符串在主字符串第N次出现的位置（索引位置）
	 * @param String str  主字符串
	 * @param String paramString 子字符串
	 * @param int paramInt 子字符串在主字符串第N此出现
	 * @return
	 */
	public static int getCharacterPosition(String str,String paramString, int paramInt){
	    //这里是获取"/"符号的位置
	    Matcher slashMatcher = Pattern.compile(paramString).matcher(str);
	    int mIdx = 0;
	    while(slashMatcher.find()) {
	       mIdx++;
	       //当"/"符号第三次出现的位置
	       if(mIdx == paramInt){
	          break;
	       }
	    }
	    return slashMatcher.start();
	 }
	
	/**
	 * 字符串数组按 字母的ASCII码值排序
	 * @param input
	 * @return
	 */
	public static String[] arraySort(String[] input){       
        for (int i=0;i<input.length-1;i++){
            for (int j=0;j<input.length-i-1;j++) {
            	if(input[j].compareTo(input[j+1])>0){
                    String temp=input[j];
                    input[j]=input[j+1];
                    input[j+1]=temp;
                }
            }
        }
        return input;
    }
	
	
	
	
	 private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
	        // Performance tuned for 2.0 (JDK1.4)
	        // Direct code is quicker than StringTokenizer.
	        // Also, StringTokenizer uses isSpace() not isWhitespace()

	        if (str == null) {
	            return null;
	        }
	        int len = str.length();
	        if (len == 0) {
	            return ArrayUtils.EMPTY_STRING_ARRAY;
	        }
	        List list = new ArrayList();
	        int sizePlus1 = 1;
	        int i = 0, start = 0;
	        boolean match = false;
	        boolean lastMatch = false;
//	        int scLen = separatorChars.length();
	        if (separatorChars == null) {
	            // Null separator means use whitespace
	            while (i < len) {
	                if (Character.isWhitespace(str.charAt(i))) {
	                    if (match || preserveAllTokens) {
	                        lastMatch = true;
	                        if (sizePlus1++ == max) {
	                            i = len;
	                            lastMatch = false;
	                        }
	                        list.add(str.substring(start, i));
	                        match = false;
	                    }
	                    start = ++i;
	                    continue;
	                }
	                lastMatch = false;
	                match = true;
	                i++;
	            }
	        } else if (separatorChars.length() == 1) {
	            // Optimise 1 character case
	            char sep = separatorChars.charAt(0);
	            while (i < len) {
	                if (str.charAt(i) == sep) {
	                    if (match || preserveAllTokens) {
	                        lastMatch = true;
	                        if (sizePlus1++ == max) {
	                            i = len;
	                            lastMatch = false;
	                        }
	                        list.add(str.substring(start, i));
	                        match = false;
	                    }
	                    start = ++i;
	                    continue;
	                }
	                lastMatch = false;
	                match = true;
	                i++;
	            }
	        } else {
	            // standard case  i < len&&
	            while ((i+separatorChars.length())<=len) {
	            	
//	                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
	                if (separatorChars.indexOf(str.substring(i,i+separatorChars.length())) >= 0) {
	                    if (match || preserveAllTokens) {
	                        lastMatch = true;
	                        if (sizePlus1++ == max) {
	                            i = len;
	                            lastMatch = false;
	                        }
	                        list.add(str.substring(start, i));
	                        match = false;
	                    }
	                    i=i+separatorChars.length();
	                    start = i;
	                    continue;
	                }
	                lastMatch = false;
	                match = true;
	                i++;
//	                i=+separatorChars.length();
	            }
	        }
	        if (match || (preserveAllTokens && lastMatch)) {
//	            list.add(str.substring(start, i));
	            list.add(str.substring(start, len));
	        }
	        return (String[]) list.toArray(new String[list.size()]);
	    }
	 
	    public static String[] splitPreserveAllTokens(String str, String separatorChars,int i) {
	        return splitWorker(str, separatorChars, i, true);
	    }
	    
}






