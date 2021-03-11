package com.p6spy.engine.maskingtool;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.jexl3.internal.Engine;
import org.apache.commons.lang.StringUtils;

import com.p6spy.engine.maskingtool.*;

/**
 * 脱敏规则-表达式组件信息解析
 */
/**
 * @className: ExpressionTool.java
 * @explain:
 * @author:  
 * @date: 2020年7月27日
 * @version: 
 */
public class ExpressionTool {
	
	private static ExpressionTool et = null;
	//所有需要JAVA实现的函数名LIST
	private static List<String> funcLst = new ArrayList<String>();
	Map<String, Object> map = new HashMap<>();
	
	//字符串表达式转JAVA方法引擎实例
	private static JexlEngine jexlEngine = new Engine();
	private Map<String, JexlExpression> jexlExpMap = new HashMap<>();
	//随机类
	private static RandomnumTool randomnum = null;
	
	public ExpressionTool(){
	}
	
	/**
	 * 初始化表达式方法工具类
	 */
	/**
	 * @author YeMing.wang 2019年5月17日 上午10:51:02
	 * @description 
	 */
	public void init(){
		try{
			jexlEngine = new Engine();
			funcLst = new ArrayList<String>();
			//1.字符串函数
			funcLst.add("indexof");
			funcLst.add("reverse");
			funcLst.add("ascii");
			funcLst.add("concat");
			funcLst.add("instr");
			funcLst.add("length");
			funcLst.add("lower");
			funcLst.add("upper");
			funcLst.add("lpad");
			funcLst.add("ltrim");
			funcLst.add("replacechr");
			funcLst.add("replacestr");
			funcLst.add("rpad");
			funcLst.add("rtrim");
			funcLst.add("substr");
			funcLst.add("reg_extract");
			funcLst.add("reg_match");
			funcLst.add("reg_replace");
			//2.日期函数
			funcLst.add("add_to_date");
			funcLst.add("date_compare");
			funcLst.add("date_diff");
			funcLst.add("get_date_part");
//			funcLst.add("round");
			funcLst.add("trunc");
			//3.加密/解密
			funcLst.add("aes_decrypt");
			funcLst.add("aes_encrypt");
			funcLst.add("dec_base64");
			funcLst.add("enc_base64");
			funcLst.add("md5");
			funcLst.add("decompress");
			funcLst.add("compress");
			//4.数字型函数
			funcLst.add("convert_base");
			funcLst.add("rand");
			funcLst.add("abs");
			funcLst.add("floor");
			funcLst.add("mod");
			funcLst.add("round");
			//5.判断函数
			funcLst.add("is_number");
			funcLst.add("is_spaces");
			funcLst.add("isnull");
			funcLst.add("to_bigint");
			funcLst.add("to_char");
			funcLst.add("to_date");
			funcLst.add("to_decimal");
			funcLst.add("to_float");
			funcLst.add("to_integer");
			//6.判断逻辑
			funcLst.add("iif");
			funcLst.add("decode");
			
			funcLst.add("chr");
			//RSA加密 解密
			funcLst.add("rsa_encrypt");
			funcLst.add("rsa_decrypt");
			//7.水印
			funcLst.add("watermark");
			//密钥水印
			funcLst.add("key_water_mark");
			//密钥数字水印
			funcLst.add("key_digital_water_mark");
		}catch(Exception e){
			
		}
	}
	
	/**
	 * indexof函数的实现 在值的列表中查找值的索引。默认情况下，此匹配区分大小写。
	 * 从 objects数组中查找 是否有和value相同值的元素，如果没有返回0，如果有 返回该元素在objects数组中的位置（从1开始）
	 * 
	 * @param value
	 * @return
	 */
	public Integer indexof(String value, Object... objects) {
		Integer returnValue = 0;
		if (StringUtils.isNotBlank(value)) {
			int whileSize=objects.length;
			String lastParamType="";
			Object lastObj = objects[objects.length - 1];
			lastParamType = lastObj.getClass().getName();
			//大小写不敏感 false 
			Boolean ignorecaseFlg = false;
			if(null==lastObj||lastParamType.contains("Interger")||"0"==String.valueOf(lastObj)){
				whileSize=whileSize-1;
				ignorecaseFlg = true;
			}
		
			for (int i = 0; i < whileSize; i++) {
				if (ignorecaseFlg) {
					if (value.equalsIgnoreCase(String.valueOf(objects[i]))) {
						returnValue = i + 1;
						break;
					} 
				}else{
					if (value.equals(String.valueOf(objects[i]))) {
						returnValue = i + 1;
						break;
					} 
				}
			}
		}else{
			returnValue = null;
		}
		return returnValue;
	}
	

	/**
	 * 反转字符串
	 * @description 
	 * @param value
	 * @return
	 */
	public String reverse(String value){
		String reStr = "";
		if(StringUtils.isNotBlank(value)){
			char []c=new char[value.length()];
			for(int i=0;i<value.length();i++){
				c[i]=value.charAt(value.length()-1-i);
			}
			reStr=String.valueOf(c);
		}
		return reStr;
	}
	/**
	 * 返回传入值的第一个字符的 ASCII 或 Unicode 值：
	 * @param value
	 * @param divisor
	 * @return
	 */
	public Integer ascii(String value){
		//递至函数的值为 NULL 时返回 NULL，如果数量为 0，则函数返回 NULL： 
		if(null==value){
			return null;
		}
		Integer re=0;
		char[] charValue=value.toCharArray();
		if(null!=charValue&&charValue.length>0)
		re = Integer.valueOf(charValue[0]);
		return re;
	}
	
	/**
	 * 返回传入值的第一个字符的 ASCII 或 Unicode 值：
	 * @param value
	 * @param divisor
	 * @return
	 */
	public String chr(Integer value){
		//递至函数的值为 NULL 时返回 NULL，如果数量为 0，则函数返回 NULL： 
		if(null==value){
			return null;
		}
		char a;
		a=(char) value.intValue();
		return String.valueOf(a);
	}
	
	/**
	 * 连接两个对象
	 * 
	 * @description 
	 * @param a 除了二进制以外的任何数据类型。
	 * @param b 除了二进制以外的任何数据类型。
	 * 
	 * @return String 返回两个对象转换成字符串后的拼接
	 */
	public String concat(Object a,Object b){
		String reStr = "";
		if(null==a && null==b){
			reStr=null;
		}else if(null!=a && null==b){
			reStr=a.toString();
		}else if(null==a && null!=b){
			reStr=b.toString();
		}else if(null!=a && null!=b){
			reStr=(a.toString()).concat(b.toString());
		}
		return reStr;
	}

	/**
	 * 按照从左到右计数，返回字符串中字符集的位置。
	 * @description 
	 * @param value
	 * @param str 
	 * @return
	 */
	public int instr1(String value,String str){
		int reInt = 0;
		if(StringUtils.isNotBlank(value)){
			if(value.indexOf(str)==-1){
				reInt= 0;
				
			}
			reInt=value.indexOf(str)+1;
			
		}
		return reInt;
	}
	
	/**
	 * 按照从左到右计数，返回字符串中字符集的位置。
	 * @description 
	 * @param valueObj String 此字符串必须为字符字符串。传递要计算的值。可输入任何有效的转换表达式。表达式结果必须为字符字符串。如果不是，INSTR 将值转换为字符串后再计算它
	 * @param str 任何值。搜索值区分大小写。要搜索的字符集。search_value 必须与字符串的一部分相匹配。例如，如果编写 INSTR('Alfred Pope', 'Alfred Smith')，则函数返回 0。 
				可输入任何有效的转换表达式。如果要搜索字符字符串，请用单引号将要搜索的字符括起来，例如，'abc'。
	 * @param ints [0]:检索起始位置  必须为整数值。要在字符串中开始搜索的位置。可输入任何有效的转换表达式。默认值为 1，这意味着 INSTR 从字符串的第一个字符开始搜索。 
	 * 			       如果起始位置为 0，则 INSTR 从字符串的第一个字符搜索。如果起始位置为正数，则 INSTR 从字符串的开头计数以查找起始位置。如果起始位置为负数，则 INSTR 从字符串的末尾计数以查找起始位置。如果忽略此参数，则函数使用默认值 1。
	 * @param ints [1]:搜索到的第几次 ;
	 * @param "[2]":TODO
	 * @return
	 */
	public static Integer instr(Object valueObj,String str,Integer...ints){
		//搜索不成功时返回 0。 
		Integer reInt = 0;
		int i=0;
		if(null!=valueObj){
			//如果不是，INSTR 将值转换为字符串后再计算它。 
			String value = valueObj.toString();
			if(ints.length==0){
				reInt=value.indexOf(str)+1;
			}
			//检索位置参数：0||正数：从起始位置开始检索；负数：从尾部开始检索；如果忽略此参数，则函数使用默认值 1（正数）。 
			else if(ints.length==1){
				//如果起始位置为正数，则 INSTR 从字符串的开头计数以查找起始位置。
				if(ints[0]>=0){
					reInt=value.indexOf(str,ints[0]-1)+1;
				//如果起始位置为负数，则 INSTR 从字符串的末尾计数以查找起始位置。	
				}else if(ints[0]<0){
					reInt=value.lastIndexOf(str)+1;
				}
			}
			//搜索到的第几次，忽略此参数，则默认值为1（查找第一次出现的位置）
			else if(ints.length==2){
				int index=0;
				String strValue=value;
				while(i<ints[1]){
					if(ints[0]>=0){
						if(i==0){
							index=strValue.indexOf(str,index+ints[0]-1);
						}else{
							index=strValue.indexOf(str,index);
						}
					}else if(ints[0]<0){
						index=strValue.lastIndexOf(str);
						if(-1==index){
							index = 0;
							break;
						}
						strValue=value.substring(0, index);
					}
					i++;
					index++;
				}
				reInt=index;
			}else if(ints.length==3){
				
			}
		}else{
			//传递至函数的值为 NULL 时返回 NULL。 
			reInt = null;
		}
		return reInt;
	}
	
	/**
	 * length函数实现
	 * 返回字符串中的字符数，其中包括尾随空白。 
	 * @param String data 字符串数据类型 
	 * @return Integer 表示字符串长度的整数。 传递至函数的值为 NULL 时返回 NULL。
	 */
	public Integer length(String value){
		Integer reLen = null;
		if(null!=value){
			reLen = value.length();
		}
		return reLen;
	}
	
	/**
	 * 将大写字符串字符转换为小写。
	 * @description 
	 * @param value
	 * @return
	 */
	public String lower(String value){
		String reStr=null;
		if(null!=value){
			reStr=value.toLowerCase();
		}
		return reStr;
	}	
	
	
	/**
	 * 将小写字符串字符转换为大写。 
	 * @description 
	 * @param value
	 * @return
	 */
	public String upper(String value){
		String reStr=null;
		if(null!=value){
			reStr=value.toUpperCase();
		}
		return reStr;
	}
	
	/**
	 * 将一组[空白]添加到字符串的开头，以将该字符串设置为指定长度。 
	 * @description 
	 * @param value 源字符串
	 * @param length int 返回字符串的长度
	 * @return reStr String 返回字符串
	 */
	public String lpad(String value,int length){
		//当传递至函数的值为 NULL 或长度为负数时返回 NULL。 
		if(null==value||length<0){
			return null;
		}else{
			String reStr="";
			if(value.length()<length){
				for (int i = 0; i < length-value.length(); i++) {
					reStr+=" ";
				}
				reStr=reStr+value;
			}else{
				//如果第一个字符串长于指定长度,则 LPAD 从右至左截断字符串。
				reStr=value.substring(0,length);
			}
			return reStr;
		}
	}
	
	/**
	 * 将一组字符添加到字符串的开头，以将该字符串设置为指定长度。
	 * @description 
	 * @param value
	 * @param length
	 * @param addstr
	 * @return 
	 */
	public String lpad(String value,int length,String addstr){
		//当传递至函数的值为 NULL 或长度为负数时返回 NULL。 
		if(null==value||length<0){
			return null;
		}
		//添加字符串为空时，返回源字符串
		if(StringUtils.isBlank(addstr)){
			return value;
		}
		String reStr="";
		char[] addchar=addstr.toCharArray();
		//第二字符串的长度
		int charLength=addstr.length();
		//如果返回长度大于第一字符串长度
		if(value.length()<length){
			//第一字符串需要新增的长度
			int strDif=length-value.length();
			//如果新增长度大于第二字符串长度
			if(strDif>charLength){
				//addstr*N填充+value
				for (int i = 0; i < strDif/charLength; i++) {
					reStr+=addstr;
				}
			}
			//charLength[0]+...+charLength[i]填充+value
			for (int i = 0; i < strDif%charLength; i++) {
				reStr+=addchar[i];
			}
			reStr=reStr+value;
		}else{
			//如果第一个字符串长于指定长度,则 LPAD 从右至左截断字符串。
			reStr=value.substring(0,length);
		}
		return reStr;
	}
	
	
	/**
	 * 从字符串的开头删除空白
	 * @description 
	 * @param value
	 * @return
	 */
	public String ltrim(String value){
		String reStr=null;
		if(null!=value){
			String resultStr=value;
			while(true){
				if(!resultStr.startsWith(" ")&&!resultStr.startsWith("\t")&&!resultStr.startsWith("\\s")){
					break;	
				}
				resultStr=resultStr.replaceFirst("\\s*", "");
			}
			reStr=resultStr; 
		}
		return reStr;
	}
	
	
	/**
	 * 从字符串的开头删除字符。
	 * @description 
	 * @param value
	 * @param str
	 * @return
	 */
	public String ltrim(String value,String str){
		if(null==value||null==str){
			return null;
		}
		String reStr="";
		String strValue=value;
		if(StringUtils.isNotBlank(value)||StringUtils.isNotBlank(str)){
			char[] charValue=value.toCharArray();
			char[] charStr = str.toCharArray();
			reStr="";
			List<Character> list = new ArrayList<Character>();
		    for (int i=0; i<charValue.length; i++) {
		        list.add(charValue[i]);
		    }
		    int j=0;
		    int z=0;
			while(true){
				if(j>=charValue.length){
					break;
				}
				if(z>=charStr.length){//当字符从左开始能完全匹配时，下一个索引重新配置字符
					z=0;
				}
				if(charValue[j]==charStr[z]){
					list.remove(0);	
				}else{
					break;
				}
				j++;
				z++;
			}
			for (int i = 0; i < list.size(); i++) {
				reStr=reStr+list.get(i).toString();
			}
//			for (char c : charStr) {
//				strValue=strValue.replaceAll(String.valueOf(c),"");
//			}
		}
		return reStr;
	}
	
	/**
	 * 将字符串中的字符替换为单个字符、多个字符或无字符。
	 * @description  该函数的 String oldCharSet 不支持 传递[符号
	 * @param CaseFlag 必须为整数。确定此函数中的参数是否区分大小写。 0 以外的数字时，函数区分大小写。  CaseFlag 为空值或 0 时，函数不区分大小写
	 * @param value 必须是字符串。 传递要搜索的字符串。 
	 * @param strings
	 * @return
	 */
	public static String replacecharJava(Integer caseFlag,String value,String oldCharSet,String newChar){
		//如果 InputString 为 NULL，则 REPLACECHR 返回 NULL。
		if(null==value){
			return null;
		}
		//如果 OldCharSet 为 NULL 或空，则 REPLACECHR 返回 InputString。 
		if(StringUtils.isBlank(oldCharSet)){
			return value;
		}
		//是否区分大小写，为空时区分大小写，为(?i)时不区分大小写
		String isCaseSensitive="";
		//当 CaseFlag 为空值或 0 时，函数不区分大小写。 其他时 函数区分大小写。 
		if(null == caseFlag|| 0==caseFlag){
			isCaseSensitive="(?i)";
		}
		char[] oldCharArr = oldCharSet.toCharArray();
		for(int i=0;i<oldCharArr.length;i++){
			char oldChar = oldCharArr[i];
			//如果 NewChar 为 NULL 或空，则 REPLACECHR 删除 OldCharSet （在 InputString 中）中所有字符的所有出现。 
			if(StringUtils.isBlank(newChar)){
				value = value.replaceAll(isCaseSensitive+oldChar, "");
			}else{
				//如果 NewChar 包含多个字符，则 REPLACECHR 使用第一个字符替换 OldCharSet。 
				char[] newCharArr = newChar.toCharArray();
				value=value.replaceAll(isCaseSensitive+oldChar, newCharArr[0]+"");
			}
		}
		
		return value;
	}
	
	/**
	 * 将字符串中的字符替换为单个字符、多个字符或无字符。
	 * @description 
	 * @param CaseFlag 必须为整数。确定此函数中的参数是否区分大小写。
	 * @param value 必须是字符串。 传递要搜索的字符串。 
	 * @param strings
	 * @return
	 */
//	public static String replacestr(Integer caseFlag,String value,String... strings){
//		if(null==value){
//			return null;
//		}
//		String reStr="";
//		String resultstr=value;
//		String regular="";
//		String newStr = strings[strings.length-1];
//		//当 CaseFlag 为空值或 0 时，函数不区分大小写。 其他时 函数区分大小写。 
//		if(null == caseFlag|| 0==caseFlag){
//			regular="(?i)";
//		}
//		//如果 NewString 为 NULL 或空，则 REPLACESTR 删除 OldString 在 InputString 中的所有出现。 
//		if(null==newStr){
//			newStr = "";
//		}
//		//遍历OldString数组，将所有 OldString替换成newStr
//		for (int i = 0; i < strings.length-1; i++) {
//			resultstr=resultstr.replaceAll(regular+strings[i], newStr);
//		}
//		reStr=resultstr;
//		return reStr;
//	}
	
	public static String replacestr(Integer caseFlag, String value, String... strings) {
		if(null==value){
			return null;
		}
		String newAllStr="";
		String reStr = "";
		String resultstr = value;
		String regular = "";
		String newStr = strings[strings.length - 1];
		// 当 CaseFlag 为空值或 0 时，函数不区分大小写。 其他时 函数区分大小写。
		if (null == caseFlag || 0 == caseFlag) {
			regular = "(?i)";
		}
		// 如果 NewString 为 NULL 或空，则 REPLACESTR 删除 OldString 在 InputString
		// 中的所有出现。
		if (null == newStr) {
			newStr = "";
		}
		String substringpre = "";
		// 遍历OldString数组，将所有 OldString替换成newStr
		// 判断oldString是否为空
		if (resultstr != "") {
			for (int i = 0; i < strings.length - 1; i++) {
				strings[i] = strings[i].trim();
				// 找到要替换的字符的索引
				int newStrindex=0; 
				if(null == caseFlag || 0 == caseFlag){
					newStrindex= resultstr.toUpperCase().indexOf(strings[i].toUpperCase());
				}else{
					newStrindex= resultstr.indexOf(strings[i]);
				}
				// 表示有可以替换的值
				if (newStrindex >= 0) {
					// 得到替换字符之前的字符串,包括要替换的字符串
					substringpre = resultstr.substring(0, newStrindex + strings[i].length());
					// 得到替换字符之后的字符串
					resultstr = resultstr.substring(newStrindex + strings[i].length());
					// 将之前的,包含要替换的字符串替换
					substringpre = substringpre.replaceAll(regular + strings[i], newStr);
					// 将被替换后得到的新字符串给newAllStr
					newAllStr = newAllStr + substringpre;
					// //将截取后面的字符串赋值给前面的,下次循环时,继续替换
					// substringpre=resultstrlast;
					// 将后面没有被替换过得字符串作为新的字符串,重新截取替换.
				}
			}
			// 当循环完毕后,如果还有最后还有剩余的,要接在新的字符串后面
			if (resultstr != "") {
				newAllStr = newAllStr + resultstr;
			}
		}
		reStr = newAllStr;
		return reStr;
	}
	
	/**
	 * 通过将空白添加到字符串的末尾，将字符串转换为指定长度
	 * @description 
	 * @param value
	 * @param length
	 * @return
	 */
	public String rpad(String value,int length){
		//当传递至函数的值为 NULL 或长度为负数时返回 NULL。 
		if(null==value||length<0){
			return null;
		}
		String reStr="";
		if(value.length()<length){
			for (int i = 0; i < length-value.length(); i++) {
				reStr+=" ";
			}
			reStr=value+reStr;
		}else{
			reStr=value.substring(0,length);
		}
		return reStr;
	}
	
	/**
	 * 通过将字符添加到字符串的末尾，将字符串转换为指定长度
	 * @description 
	 * @param value
	 * @param length
	 * @param str
	 * @return
	 */
	public String rpad(String value,int length,String addstr){
		//当传递至函数的值为 NULL 或长度为负数时返回 NULL。 
		if(null==value||length<0){
			return null;
		}
		//添加字符串为空时，返回源字符串
		if(StringUtils.isBlank(addstr)){
			return value;
		}
		String reStr="";
		char[] addchar=addstr.toCharArray();
		int charLength=addstr.length();
		int strDif=length-value.length();
		if(value.length()<length){
			if(charLength<=strDif){
				for (int i = 0; i < strDif/charLength; i++) {
					reStr+=addstr;
				}
			}
			
			for (int i = 0; i < strDif%charLength; i++) {
				reStr+=addchar[i];
			}
			//右侧填补字符串
			reStr=value+reStr;
		}else{
			reStr=value.substring(0,length);
		}
		return reStr;
	}
	
	
	/**
	 * 从字符串的末尾删除空白。
	 * @description 
	 * @param value
	 * @return
	 */
	public String rtrim(String value){
		if(null==value){
			return null;
		}
		String reStr="";
		if(StringUtils.isNotBlank(value)){
			String resultStr=value;
			while(true){
				if(!resultStr.endsWith(" ")&&!resultStr.startsWith("\t")&&!resultStr.startsWith("\\s")){
					break;	
				}
				resultStr=resultStr.substring(0, resultStr.length()-1);
			}
			reStr=resultStr;
		}
		return reStr;
	}
	
	
	/**
	 * 从字符串的末尾删除字符。
	 * @description 
	 * @param value 任何字符串值。 传递要裁减的值。 可输入任何有效的转换表达式。 使用运算符执行比较或连接字符串，然后再删除字符串末尾的空白。
	 * @param str 任何字符串值。 传递要从字符串末尾删除的字符。 也可输入文本。 然而，必须用单引号将要从字符串末尾删除的字符引起来，
	 * 			    例如， 'abc'。 如果忽略第二个字符串，则函数删除第一个字符串末尾的空白。 RTRIM 区分大小写。 
	 * @return String
	 */
	public String rtrim(String value,String str){
		if(null==value){
			return null;
		}
		StringBuilder reStr = new StringBuilder();
		if(StringUtils.isNotBlank(value)||StringUtils.isNotBlank(str)){
			List<Character> valList = new ArrayList<Character>();
			char[] valueArr = value.toCharArray();
			for (int i=0; i<valueArr.length; i++) {
				valList.add(valueArr[i]);
			}
			char[] replaceCharArr = str.toCharArray();
			//遍历 替换字符串
			for (int i=0; i<replaceCharArr.length; i++) {
				for (int j=valList.size()-1; j>=0; j--) {
					if(valList.get(j) == replaceCharArr[i]){
						valList.remove(j);
					}else{
						break;
					}
				}
			}
	        for (Character s : valList) {
	        	reStr.append(s);
	        }
		}
		return reStr.toString();
	}
	
	/**
	 * substr函数实现
	 * 返回字符串的一部分。SUBSTR 从字符串的开头开始计数所有字符，其中包括空白。 
	 * @param data
	 * @return
	 */
	public static String substr(String value,int start){
		//传递至函数的值为 NULL 时返回 NULL。 
		if(null==value){
			return null;
		}
		if(start>0){
			start--;
			if(start>value.length()){
				return "";
			}
		}else if(start<0){
			start = value.length()+start;
			if(0>start){
				start = 0;
			}
		}
		value = value.substring(start);
		return value;
	}
	
	/**
	 * substr函数实现
	 * 返回字符串的一部分。SUBSTR 从字符串的开头开始计数所有字符，其中包括空白。 
	 * @param data
	 * @return
	 */
	public static String substr(String value,int start,int len){
		//如果传递负数或 0 长度值，则返回空字符串。
		if(len<=0){
			return "";
		}
		//传递至函数的值为 NULL 时返回 NULL。 
		if(null==value){
			return null;
		}
		if(start>0){
			start--;
			if(start>value.length()){
				return "";
			}
		}else if(start<0){
			start = value.length()+start;
			if(0>start){
				start = 0;
			}	
		}
		int endIndex = ((start+len)<=value.length())?(start+len):value.length();
		value = value.substring(start, endIndex);
		
		return value;
	}
	
	/**
	 * 输入值内提取正则表达式的子模式。例如，可从全名的正则表达式模式提取名字或姓氏1。 
	 * @param value 字符串数据类型。 传递要与正则表达式模式比较的值。
	 * @param divisor 字符串数据类型。 要匹配的正则表达式模式。
	 * @return
	 */
	public String reg_extract(String value,String divisor){
		//传递至函数的值为 NULL 时返回 NULL。 
		if(StringUtils.isBlank(value)||null==divisor){
			return null;
		}
		String reStr="";
		//创建匹配格式
		Pattern pattern = Pattern.compile(divisor);  
		Matcher matcher = pattern.matcher(value);
		while(matcher.find()){
			//无值或 1。 提取第一个正则表达式子模式。 
			reStr = matcher.group(1);
		}
		return reStr;
		
	}
	
	/**
	 * 输入值内提取正则表达式的子模式。例如，可从全名的正则表达式模式提取名字或姓氏2。 
	 * @param value 字符串数据类型。 传递要与正则表达式模式比较的值。
	 * @param divisor 字符串数据类型。 要匹配的正则表达式模式。
	 * @param subPatternNum 整数值。要匹配的正则表达式的子模式数。 
	 * @return
	 */
	public String reg_extract(String value,String divisor,int subPatternNum){
		//传递至函数的值为 NULL 时返回 NULL。 
		if(StringUtils.isBlank(value)||null==divisor){
			return null;
		}
		String reStr="";
		//创建匹配格式
		Pattern pattern = Pattern.compile(divisor);  
		Matcher matcher = pattern.matcher(value);
		while(matcher.find()){
			//返回由以前匹配操作所匹配的输入子序列。
			reStr = matcher.group(subPatternNum);
		}
		return reStr;
	}
	
	/**
	 * 输入值内提取正则表达式的子模式。例如，可从全名的正则表达式模式提取名字或姓氏3。 
	 * @param value 字符串数据类型。 传递要与正则表达式模式比较的值。
	 * @param divisor 字符串数据类型。 要匹配的正则表达式模式。
	 * @param subPatternNum 整数值。要匹配的正则表达式的子模式数。 
	 * @param match_from_start 数值。 从字符串的开头找到匹配项时返回子字符串。
	 * @return
	 */
	public String reg_extract(String value,String divisor,int subPatternNum,int match_from_start){
		//传递至函数的值为 NULL 时返回 NULL。 
		if(StringUtils.isBlank(value)||null==divisor){
			return null;
		}
		value = substr(value,match_from_start);
		String reStr="";
		//创建匹配格式
		Pattern pattern = Pattern.compile(divisor);  
		Matcher matcher = pattern.matcher(value);
		while(matcher.find()){
			//返回由以前匹配操作所匹配的输入子序列。
			reStr = matcher.group(subPatternNum);
		}
		return reStr;
	}

	/**
	 * reg_match函数实现
	 * 返回值是否匹配正则表达式模式。
	 * @param data
	 * @return
	 */
	public Boolean reg_match(String value,String pattern){
		//传递至函数的值为 NULL 时返回 NULL。 
		if(StringUtils.isBlank(value)||null==pattern){
			return null;
		}
		boolean isMatch = Pattern.matches(pattern, value);
		return isMatch;
	}
	
	/**
	 * reg_replace函数实现-用其他字符模式替换字符串中的字符。
	 * @param value 字符串数据类型。 传递要搜索的字符串。 
	 * @param pattern 字符串数据类型。 传递要替换的字符串。
	 * @param replace 字符串数据类型。 传递新字符串。
	 * @return
	 */
	public String reg_replace(String value,String pattern,String replace ){
		String reStr=value.replaceAll(pattern, replace);
		return reStr;
	}
	
	/**
	 * reg_replace函数实现-用其他字符模式替换字符串中的字符。
	 * @param value 字符串数据类型。 传递要搜索的字符串。 
	 * @param pattern 字符串数据类型。 传递要替换的字符串。
	 * @param replace 字符串数据类型。 传递新字符串。
	 * @param numReplacements 数值数据类型。 指定要替换的出现次数。 如果忽略此选项，则 REG_REPLACE 将替换字符串的所有出现。 
	 * @return
	 */
	public String reg_replace(String value,String pattern,String replace,int numReplacements){
		String reStr="";
		int i=0;
		int index =0;
		String subvalue=value;
		int patternl=pattern.length();
		int local=0;
		String beforeStr="";
		while(true){
			if(numReplacements>1&&i<numReplacements){
				local=subvalue.indexOf(pattern);
				if(local==-1){
					reStr=value;
					break;
				}else{
					index+=subvalue.indexOf(pattern)+patternl;
					subvalue=value.substring(index);
					i++;
				}
			}else if(numReplacements<1){
				reStr=value;
				break;
			}
			else if(numReplacements==1){
				reStr=value.replaceFirst(pattern, replace);
				break;
			}else{
				subvalue=subvalue.replaceFirst(pattern,replace);
				beforeStr=value.substring(0,index+1).replace(pattern, replace);
				reStr=beforeStr+value.substring(index+1);
				break;
			}
		}
		
		return reStr;
	}
	
	//=================2.日期函数==========================================
	/**
	 * add_to_date函数实现-添加指定量至日期时间值的一部分，并返回与您传递给函数的日期格式相同的日期。ADD_TO_DATE 接受正负整数值。使用 ADD_TO_DATE 更改日期的以下部分： 
	 * 
	 * @param data Date/Time 数据类型。 传递要更改的值。 可输入任何有效的转换表达式。 
	 * @param format String用于指定日期值中要更改的那一部分的格式字符串。 用单引号将格式字符串括起来，例如，'mm'。 格式字符串不区分大小写。
	 * @param amount int 此整数值指定年份、月份、日、小时等的数量，将其作为您想要更改日期值的依据。 可输入任何有效的转换表达式以计算整数。 
	 * @return  Date 格式与传递至此函数的日期相同的日期。 
	 */
	public static Date add_to_java_date(Object dataObj,String format,int amount){
		//如果传递空值至函数，则它返回 NULL
		if(null==dataObj){
			return null;
		}
		//参数初始化为JAVA Date 类型
		Date data = Object2Date(dataObj);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
//		String reStr1 = sdf.format(data);  
		Date reDate = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		//格式字符串不区分大小写
		if("Y".equalsIgnoreCase(format)||"YY".equalsIgnoreCase(format)||"YYY".equalsIgnoreCase(format)||"YYYY".equalsIgnoreCase(format)){
			cal.add(Calendar.YEAR, amount);
		}else if("MM".equalsIgnoreCase(format)||"MON".equalsIgnoreCase(format)||"MONTH".equalsIgnoreCase(format)){
			cal.add(Calendar.MONTH, amount);
		}else if("D".equalsIgnoreCase(format)||"DD".equalsIgnoreCase(format)||"DDD".equalsIgnoreCase(format)||"DY".equalsIgnoreCase(format)||"DAY".equalsIgnoreCase(format)){
			cal.add(Calendar.DATE, amount);
		}else if("HH".equalsIgnoreCase(format)||"HH12".equalsIgnoreCase(format)||"HH24".equalsIgnoreCase(format)){
			cal.add(Calendar.HOUR, amount);
		}else if("MI".equalsIgnoreCase(format)){
			cal.add(Calendar.MINUTE, amount);
		}else if("SS".equalsIgnoreCase(format)){
			cal.add(Calendar.SECOND, amount);
		}else if("MS".equalsIgnoreCase(format)){
			cal.add(Calendar.MILLISECOND, amount);
		}else if("US".equalsIgnoreCase(format)){
			cal.add(Calendar.MILLISECOND, (int)(amount/1000));
		}
		reDate = cal.getTime();
//		String reStr = sdf.format(reDate);  
		return reDate;
	}
	
	/**
	 * date_compare函数实现-返回指示两个日期中哪一个更早的整数。DATE_COMPARE 返回整数值，而非日期值。
	 * 
	 * @param data1 Date/Time 数据类型。 传递要更改的值。 可输入任何有效的转换表达式。 
	 * @param data1 Date/Time 数据类型。 传递要更改的值。 可输入任何有效的转换表达式。 
	 * @return  Date 格式与传递至此函数的日期相同的日期。 
	 */
	public static Integer date_compare(Object dataObj1,Object dataObj2){
		//如果传递空值至函数，则它返回 NULL
		if(null==dataObj1||null==dataObj2){
			return null;
		}
		Date data1 = Object2Date(dataObj1);
		Date data2 = Object2Date(dataObj2);
		return data1.compareTo(data2);
	}
	
	/**
	 * add_to_date函数实现-添加指定量至日期时间值的一部分，并返回与您传递给函数的日期格式相同的日期。ADD_TO_DATE 接受正负整数值。使用 ADD_TO_DATE 更改日期的以下部分： 
	 * 
	 * @param data1 Date/Time 数据类型。 传递要比较的第一个日期的值。 可输入任何有效的转换表达式。 
	 * @param data2 Date/Time 数据类型。 传递要比较的第二个日期的值。 可输入任何有效的转换表达式。
	 * @param format String 指定日期或时间度量的格式字符串。 可指定年份、月份、日、小时、分钟、秒数、毫秒、微秒或纳秒。 只能指定日期的一个部分，例如，'mm'。 用单引号将格式字符串括起来。 格式字符串不区分大小写。 例如，格式字符串 'mm' 与 'MM'、'Mm' 或 'mM'相同
	 * @return  Date 格式与传递至此函数的日期相同的日期。 
	 */
	public static Double date_diff(Object dataObj1,Object dataObj2,String format){
		//如果传递空值至函数，则它返回 NULL
		if(null==dataObj1||null==dataObj2){
			return null;
		}
		Date data1 = Object2Date(dataObj1);
		Date data2 = Object2Date(dataObj2);
		Double reTime = null;
		long dateMillis1 = data1.getTime();
		long dateMillis2 = data2.getTime();
		//格式字符串不区分大小写
		if("Y".equalsIgnoreCase(format)||"YY".equalsIgnoreCase(format)||"YYY".equalsIgnoreCase(format)||"YYYY".equalsIgnoreCase(format)){
			reTime = new Double(((dateMillis1 - dateMillis2)/1000) / (3600*24*365));
		}else if("MM".equalsIgnoreCase(format)||"MON".equalsIgnoreCase(format)||"MONTH".equalsIgnoreCase(format)){
			reTime = new Double(((dateMillis1 - dateMillis2)/1000) / (3600*24*30));
		}else if("D".equalsIgnoreCase(format)||"DD".equalsIgnoreCase(format)||"DDD".equalsIgnoreCase(format)||"DY".equalsIgnoreCase(format)||"DAY".equalsIgnoreCase(format)){
			reTime = new Double(((dateMillis1 - dateMillis2)/1000) / (3600*24));
		}else if("HH".equalsIgnoreCase(format)||"HH12".equalsIgnoreCase(format)||"HH24".equalsIgnoreCase(format)){
			reTime = new Double(((dateMillis1 - dateMillis2)/1000)  / (3600));
		}else if("MI".equalsIgnoreCase(format)){
			reTime = new Double(((dateMillis1 - dateMillis2)/1000) / (60));
		}else if("SS".equalsIgnoreCase(format)){
			reTime = new Double((dateMillis1 - dateMillis2) / (1000));
		}else if("MS".equalsIgnoreCase(format)){
			reTime = new Double((dateMillis1 - dateMillis2));
		}else if("US".equalsIgnoreCase(format)){
			reTime = new Double((dateMillis1*1000 - dateMillis2*1000));
		}
		return reTime;
	}
	
	/**
	 * get_date_part函数实现-将日期的指定部分返回为整数值。因此，如果您创建的表达式返回日期的月份部分，且传递 Apr 1 1997 00:00:00 之类的日期，则 GET_DATE_PART 返回 4。
	 * 
	 * @param data Date/Time 数据类型。 可输入任何有效的转换表达式
	 * @param format String 指定日期或时间度量的格式字符串。 可指定年份、月份、日、小时、分钟、秒数、毫秒、微秒或纳秒。 只能指定日期的一个部分，例如，'mm'。 用单引号将格式字符串括起来。 格式字符串不区分大小写。 例如，格式字符串 'mm' 与 'MM'、'Mm' 或 'mM'相同
	 * @return  Integer 表示日期中指定部分的整数。
	 */
	public static Integer get_date_part(Object dataObj1,String format){
		//如果传递空值至函数，则它返回 NULL
		if(null==dataObj1){
			return null;
		}
		Date data = Object2Date(dataObj1);
		Integer reTime = null;
		//指定日期  
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		//格式字符串不区分大小写
		if("Y".equalsIgnoreCase(format)||"YY".equalsIgnoreCase(format)||"YYY".equalsIgnoreCase(format)||"YYYY".equalsIgnoreCase(format)){
			reTime = cal.get(Calendar.YEAR);
		}else if("MM".equalsIgnoreCase(format)||"MON".equalsIgnoreCase(format)||"MONTH".equalsIgnoreCase(format)){
			reTime = cal.get(Calendar.MONTH)+1;
		}else if("D".equalsIgnoreCase(format)||"DD".equalsIgnoreCase(format)||"DDD".equalsIgnoreCase(format)||"DY".equalsIgnoreCase(format)||"DAY".equalsIgnoreCase(format)){
			reTime = cal.get(Calendar.DAY_OF_MONTH);
		}else if("HH".equalsIgnoreCase(format)||"HH12".equalsIgnoreCase(format)||"HH24".equalsIgnoreCase(format)){
			reTime = cal.get(Calendar.HOUR_OF_DAY);
		}else if("MI".equalsIgnoreCase(format)){
			reTime = cal.get(Calendar.MINUTE);
		}else if("SS".equalsIgnoreCase(format)){
			reTime = cal.get(Calendar.SECOND);
		}else if("MS".equalsIgnoreCase(format)){
			reTime = cal.get(Calendar.MILLISECOND);
		}else if("US".equalsIgnoreCase(format)){
			reTime = 0;
		}
		return reTime;
	}
	
	/**
	 * round函数实现-舍入日期的一部分。 也可使用 ROUND 舍入数字。 
	 * 
	 * @param data Date/Time 数据类型。 在舍入之前，可嵌套 TO_DATE 以将字符串转换为日期。
	 * @param formatArr String[] 输入有效的格式字符串。 这是日期中要舍入的那部分。 只能舍入日期的一个部分。 如果忽略格式字符串，函数将日期舍入为最接近的日期。
	 * @return  Date 指定部分已舍入的日期。 ROUND 返回与源日期格式相同的日期。 可将此函数的结果链接至数据类型为 Date/Time 的任何端口。
	 */
	public static Date round(Object dataObj1,String... formatArr){
		//如果传递空值至函数，则它返回 NULL
		if(null==dataObj1){
			return null;
		}
		Date data1 = Object2Date(dataObj1);
		Date reDate = null;
		String format = "";
		if(null==formatArr||formatArr.length<=0){
			format = "DD";
		}else{
			format = formatArr[0];
		}
		
		//指定日期  
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(data1);
		Integer intTime = 0;
		//格式字符串不区分大小写
		if("Y".equalsIgnoreCase(format)||"YY".equalsIgnoreCase(format)||"YYY".equalsIgnoreCase(format)||"YYYY".equalsIgnoreCase(format)){
			intTime = get_date_part(dataObj1,"MM");
			//舍入的部分为年,当月份大于6则为当前年加一,否则为当前年
			if(intTime<=6){
				cal.add(Calendar.YEAR, 0);
			}else{
				cal.add(Calendar.YEAR, 1);
			}
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY,12);
			cal.set(Calendar.MINUTE,00);
			cal.set(Calendar.SECOND,00);
		}else if("MM".equalsIgnoreCase(format)||"MON".equalsIgnoreCase(format)||"MONTH".equalsIgnoreCase(format)){
			intTime = get_date_part(dataObj1,"DD");
			if(intTime<=15){
				cal.add(Calendar.MONTH, 0);
			}else{
				cal.add(Calendar.MONTH, 1);
			}
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY,12);
			cal.set(Calendar.MINUTE,00);
			cal.set(Calendar.SECOND,00);
		}else if("D".equalsIgnoreCase(format)||"DD".equalsIgnoreCase(format)||"DDD".equalsIgnoreCase(format)||"DY".equalsIgnoreCase(format)||"DAY".equalsIgnoreCase(format)){
			intTime = get_date_part(dataObj1,"HH");
			Integer intTime1 = get_date_part(dataObj1,"MI");
			Integer intTime2 = get_date_part(dataObj1,"SS");
			if(intTime>=12||(intTime==11&&intTime1==59&&intTime2==59)){
				cal.add(Calendar.DATE, 1);
			}else{
				cal.add(Calendar.DATE, 0);
			}
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE,00);
			cal.set(Calendar.SECOND,00);
		}else if("HH".equalsIgnoreCase(format)||"HH12".equalsIgnoreCase(format)||"HH24".equalsIgnoreCase(format)){
			intTime = get_date_part(dataObj1,"MI");
			Integer intTime1=get_date_part(dataObj1,"HH");
			if(intTime<30){
				cal.add(Calendar.HOUR, 0);
			}else{
				cal.add(Calendar.HOUR, 1);
				//当小时为11时,加一个小时,日期也要增加一天
				if(intTime1==11){
					cal.add(Calendar.DATE, 1);
				}
			}
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND,00);
		}else if("MI".equalsIgnoreCase(format)){
			Integer intTime1 = get_date_part(dataObj1,"MI");
			Integer intTime2 = get_date_part(dataObj1,"HH");
			intTime = get_date_part(dataObj1,"SS");
			if(intTime<30){
				cal.add(Calendar.MINUTE, 0);
			}else{
				cal.add(Calendar.MINUTE, 1);
				if(intTime2==11&&intTime1==59){
					cal.add(Calendar.DATE, 1);	
				}
				
			}
			cal.set(Calendar.SECOND, 0);
		}else if("SS".equalsIgnoreCase(format)){
			intTime = get_date_part(dataObj1,"MS");
			if(intTime<500){
				cal.add(Calendar.SECOND, 0);
			}else{
				cal.add(Calendar.SECOND, 1);
			}
			cal.set(Calendar.MILLISECOND, 0);
		}else if("MS".equalsIgnoreCase(format)){
			intTime = get_date_part(dataObj1,"US");
			if(intTime<500){
				cal.add(Calendar.MILLISECOND, 0);
			}else{
				cal.add(Calendar.MILLISECOND, 1);
			}
//			cal.set(Calendar.MILLISECOND, 0);
		}else if("US".equalsIgnoreCase(format)){
//			cal.set(Calendar.MILLISECOND, 0);
		}
		reDate = cal.getTime();
		return reDate;
	}
	
	
	/**
	 * systimestamp函数实现-将日期的指定部分返回为整数值。因此，如果您创建的表达式返回日期的月份部分，且传递 Apr 1 1997 00:00:00 之类的日期，则 GET_DATE_PART 返回 4。
	 * 
	 * @param data Date/Time 数据类型。 在舍入之前，可嵌套 TO_DATE 以将字符串转换为日期。 
	 * @param format String 输入有效的格式字符串。 这是日期中要舍入的那部分。 只能舍入日期的一个部分。 如果忽略格式字符串，函数将日期舍入为最接近的日期。
	 * @return  Integer 指定部分已舍入的日期。 ROUND 返回与源日期格式相同的日期。 可将此函数的结果链接至数据类型为 Date/Time 的任何端口
	 */
	public static Timestamp systimestamp(String... formatArr){
		//如果传递空值至函数，则它返回 NULL
		Date data = new Date();
		Timestamp reTimestamp = null;
		String format = "";
		if(null==formatArr||formatArr.length<=0){
			format = "US";
		}else{
			format = formatArr[0];
		}
		//指定日期  
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		//格式字符串不区分大小写
		if("SS".equalsIgnoreCase(format)){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				reTimestamp = new Timestamp((df.parse(df.format(new Date()))).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else if("MS".equalsIgnoreCase(format)){
			reTimestamp = new Timestamp(System.currentTimeMillis());
		}else if("US".equalsIgnoreCase(format)){
			reTimestamp = new Timestamp(System.currentTimeMillis());
		}else if("NS".equalsIgnoreCase(format)){
			reTimestamp = new Timestamp(System.currentTimeMillis());
		}
		return reTimestamp;
	}
	
	/**
	 * trunc函数实现-将日期截断为特定年份、月份、日、小时、分种、秒、毫秒或微秒。 也可使用 TRUNC 截断数字。 
	 *
	 * @param object Date/Time 数据类型。 在舍入之前，可嵌套 TO_DATE 以将字符串转换为日期。
	 * @param formatArr String[] 输入有效的格式字符串。 这是日期中要舍入的那部分。 只能舍入日期的一个部分。 如果忽略格式字符串，函数将日期舍入为最接近的日期。
	 * @return  Date 指定部分已舍入的日期。 ROUND 返回与源日期格式相同的日期。 可将此函数的结果链接至数据类型为 Date/Time 的任何端口。
	 */
	public static Date trunc(Object object,String... formatArr){
		//如果传递空值至函数，则它返回 NULL
		if(null==object){
			return null;
		}
		Date data = Object2Date(object);
		Date reDate = null;
		String format = "";
		if(null==formatArr||formatArr.length<=0){
			format = "DD";
		}else{
			format = formatArr[0];
		}
		
		//指定日期  
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		//格式字符串不区分大小写
		if("Y".equalsIgnoreCase(format)||"YY".equalsIgnoreCase(format)||"YYY".equalsIgnoreCase(format)||"YYYY".equalsIgnoreCase(format)){
			cal.add(Calendar.YEAR, 0);
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY,12);
			cal.set(Calendar.MINUTE,00);
			cal.set(Calendar.SECOND,00);
		}else if("MM".equalsIgnoreCase(format)||"MON".equalsIgnoreCase(format)||"MONTH".equalsIgnoreCase(format)){
			cal.add(Calendar.MONTH, 0);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY,12);
			cal.set(Calendar.MINUTE,00);
			cal.set(Calendar.SECOND,00);
		}else if("D".equalsIgnoreCase(format)||"DD".equalsIgnoreCase(format)||"DDD".equalsIgnoreCase(format)||"DY".equalsIgnoreCase(format)||"DAY".equalsIgnoreCase(format)){
			cal.add(Calendar.DATE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.HOUR_OF_DAY,12);
			cal.set(Calendar.MINUTE,00);
			cal.set(Calendar.SECOND,00);
		}else if("HH".equalsIgnoreCase(format)||"HH12".equalsIgnoreCase(format)||"HH24".equalsIgnoreCase(format)){
			cal.add(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.MINUTE,00);
			cal.set(Calendar.SECOND,00);
		}else if("MI".equalsIgnoreCase(format)){
			cal.add(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
		}else if("SS".equalsIgnoreCase(format)){
			cal.add(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
		}else if("MS".equalsIgnoreCase(format)){
			cal.add(Calendar.MILLISECOND, 0);
		}
		reDate = cal.getTime();
		return reDate;
	}
	//=================3.加密/解密=========================================
	
	/**
	 * aes_encrypt函数实现-返回加密格式的数据。 PowerCenter 集成服务使用高级加密标准 (AES) 算法进行 128 位编码。 AES 算法为 FIPS 批准的加密算法。。 
	 * 使用此函数防止向每个人显示敏感数据。
	 * @param data String 字符串数据类型。 想要加密的值
	 * @param key String 字符串数据类型。 精度为 16 个或更少字符。 映射变量可用于健。
	 * @return  Date 加密的二进制值。
	 */
	public static String aes_encrypt(String data,String key){
		//如果传递空值至函数，则它返回 NULL
		if(null==data){
			return null;
		}
		String enStr = AesUtil.encrypt(data, key);
		return enStr;
	}
	
	/**
	 * aes_decrypt函数实现-将加密数据返回字符串格式。 PowerCenter 集成服务使用高级加密标准 (AES) 算法进行 128 位编码。 AES 算法为 FIPS 批准的加密算法。 
	 * 
	 * @param data String 二进制数据类型。 想要解密的值
	 * @param key String 字符串数据类型。 精度为 16 个或更少字符。 映射变量可用于健。 使用同一键解密您曾加密过的值。
	 * @return  Date 解密的二进制值。
	 */
	public static String aes_decrypt(String data,String key){
		//如果传递空值至函数，则它返回 NULL
		if(null==data){
			return null;
		}
		String enStr = AesUtil.decrypt(data, key);
		return enStr;
	}
	
	/**
	 * enc_base64函数实现-通过使用多用途 Internet 邮件扩展(MIME)编码方式将二进制数据转换为字符串数据来编码数据。
	 * 想要在不允许使用二进制数据的数据库或文件中存储数据时编码数据。 也可编码数据，以便通过转换以字符串格式传递二进制数据。
	 *  编码数据比原始数据大约长 33%。 它显示为一组随机字符
	 * 
	 * @param data String Binary 或 String 数据类型。 要编码的数据。
	 * @return  String 编码值。 
	 */
	public static String enc_base64(String data){
		//如果传递空值至函数，则它返回 NULL
		if(null==data){
			return null;
		}
		String reStr = "";
		byte[] byteContent;
		try {
			byteContent = data.getBytes("utf-8");
			reStr = new Base64().encodeToString(byteContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return reStr; 
	}
	
	/**
	 * dec_base64函数实现-解码 base 64 编码值，返回二进制数据字符串。 如果使用 ENC_BASE64 编码数据，且想要使用 DEC_BASE64 解码数据，则必须使用相同的数据移动模式运行解码会话。
	 *  否则，解码数据的输出可能不同于原始数据
	 * 
	 * @param data 字符串数据类型。 要解码的数据。
	 * @return  String 二进制解码值 在 Unicode 模式而非 ASCII 模式中运行会话时，返回值不同。 
	 */
	public static String dec_base64(String data){
		//如果传递空值至函数，则它返回 NULL
		if(null==data){
			return null;
		}
		String reStr = "";
		byte[] byteContent;
		try {
			byteContent = Base64.decodeBase64(data);
			reStr = new String(byteContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return reStr; 
	}
	
	/**
	 * md5函数实现-计算输入值的校验和。函数使用消息摘要算法 5(MD5)。MD5 是单向加密哈希函数，有一个 128 位哈希值。 可得出结论，当输入值校验和不同时，输入值就不同。 使用 MD5 验证数据完整性。 
	 * 
	 * @param data String 或 Binary 数据类型。 想要为其计算校验和的值。 输入值的大小写影响返回值。 例如，MD5(informatica) 和 MD5 (Informatica) 返回不同的值。
	 * @return  String 十六进制位数 0-9 和 a-f 的唯一 32 位字符串。 
	 */
	public static String md5(String data){
		//如果传递空值至函数，则它返回 NULL
		if(null==data){
			return null;
		}
		String reStr = StringTool.encodeByMD5(data);
        return reStr; 
	}
	
	/**
	 * compress函数实现-使用 zlib 1.2.1 压缩算法压缩数据。 通过广域网发送大量数据之前，请使用 COMPRESS 函数
	 * 
	 * @param data String字符串数据类型。 要压缩的数据
	 * @return  String 输入值的压缩二进制值。
	 */
	public static String compress(String data){
		//如果传递空值至函数，则它返回 NULL
		if(null==data){
			return null;
		}
		String reStr = ZLibUtils.compressStr(data);
        return reStr; 
	}
	
	
	/**
	 * decompress函数实现-使用 zlib 1.2.1 压缩算法解压缩数据。
	 * 对通过 COMPRESS 函数或压缩工具(使用 zlib 1.2.1 算法)压缩的数据使用 DECOMPRESS 函数。
	 * 如果数据解压缩会话使用的数据移动模式不同于数据压缩会话，则解压缩数据的输出可能不同于原始数据。
	 * 
	 * @param data String二进制数据类型。 要解压缩的数据
	 * @return  String 输入值的已解压缩二进制值。 
	 */
	public static String decompreJava(String btyeStr){
		//如果传递空值至函数，则它返回 NULL
		if(null==btyeStr){
			return null;
		}
		String reStr = ZLibUtils.decompressStr(btyeStr);
        return reStr; 
	}
	
	/** TODO
	 * decompress函数实现-使用 zlib 1.2.1 压缩算法解压缩数据。
	 * 对通过 COMPRESS 函数或压缩工具(使用 zlib 1.2.1 算法)压缩的数据使用 DECOMPRESS 函数。
	 * 如果数据解压缩会话使用的数据移动模式不同于数据压缩会话，则解压缩数据的输出可能不同于原始数据。
	 * 
	 * @param btyeStr String二进制数据类型。 要解压缩的数据
	 * @param precision Integer Integer 数据类型。 
	 * @return  String 输入值的已解压缩二进制值。 
	 */
	public static String decompreJava(String btyeStr,Integer precision){
		//如果传递空值至函数，则它返回 NULL
		if(null==btyeStr){
			return null;
		}
		String reStr = ZLibUtils.decompressStr(btyeStr);
        return reStr; 
	}
	
	//=================4.数字类型==========================================
	/**
	 * 将一个基数值的数字转换为另一个基数值 
	 * 例如：CONVERT_BASE( "2222", 10, 2 )  以下表达式将 2222 从十进制基数值 10 转换为二进制基数值 2： 
	 * @param value 字符串数据类型。 想要从一个其数转换为另一个基数的值。
	 * @param source_base 数值数据类型。 想要转换的数据的当前基数值。
	 * @param dest_base 数值数据类型。 想要将数据转换为的基数值。 
	 * @return
	 */
	public String convert_base(String value,int source_base,int dest_base ){
		String reInt="";
		if(null!=value){
			char chs[] = new char[36];
			for(int i = 0; i < 10 ; i++) {
	            chs[i] = (char)('0' + i);
	        }
	        for(int i = 10; i < chs.length; i++) {
	            chs[i] = (char)('A' + (i - 10));
	        }
	        int number = Integer.valueOf(value, source_base);
	        StringBuilder sb = new StringBuilder();
	        while (number != 0) {
	            sb.append(chs[number%dest_base]);
	            number = number / dest_base;
	        }
	        reInt=sb.reverse().toString();
		}
		return reInt;
	}
	
	/**
	 * 返回 0 到 1 之间的一个随机数
	 * @param value
	 * @return
	 */
	public double rand(int value ){

		Random r=new Random(value);
		double re=r.nextDouble();
		return re;
	}
	
	public double rand(){

		Random r=new Random();
		double re=r.nextDouble();
		return re;
	}
	
	/**
	 * 返回数值的绝对值:如果传递整数，则它返回整数。 
	 * @param value Double
	 * @return
	 */
	public Integer abs(Integer value ){
		//如果传递空值至函数，则它返回 NULL
		if(null==value){
			return null;
		}
		return Math.abs(value);
	}
	
	/**
	 * 返回数值的绝对值: 如果传递 Double，则它返回 Double。 
	 * @param value Double
	 * @return
	 */
	public Double abs(Double value ){
		//如果传递空值至函数，则它返回 NULL
		if(null==value){
			return null;
		}
		return Math.abs(value);
	}
	
	/**
	 * 返回数值的绝对值: 如果传递 Double，则它返回 Double。 
	 * @param value Double
	 * @return
	 */
	public Double abs(BigDecimal value){
		//如果传递空值至函数，则它返回 NULL
		if(null==value){
			return null;
		}
		return Math.abs(value.doubleValue());
	}
	
	/**
	 * 返回小于或等于传递至此函数的数值的最大整数
	 * @param value
	 * @return
	 */
	public Object floor(Object value ){
		Object re=null;
		if(value==null){
			return re;
		}
		//
		re=Math.floor(Double.parseDouble(value.toString()));
		Double reD = Double.valueOf(re.toString());
		//如果取整后的数值在INT型范围之内，转为INT，否则返回Double
		if(reD<=Integer.MAX_VALUE&&reD>=Integer.MIN_VALUE){
			re= reD.intValue();
		}
		
		return re;
	}
	
	/**
	 * mod()方法返回除法计算的余数
	 * 由于mod是JAVA关键字 所有改为 modJava方法
	 * @param value
	 * @param divisor
	 * @return
	 */
	public Integer modJava(Object value,Integer divisor){
		//递至函数的值为 NULL 时返回 NULL，如果数量为 0/NULL，则函数返回 NULL： 
		if(null==value||null==divisor||0==divisor){
			return null;
		}
		int re=0;
		re=(int)(Double.parseDouble(value.toString())%divisor);
		return re;
	}
	
	/**
	 * 将数字舍入为指定位数或小数位
	 * @param value
	 * @return
	 */
	public Object round(Object value){
		Object re=null;
		if(value==null){
			re=null;
		}else{
			
			int index=value.toString().indexOf(".");//查看是否有小数点
			int l=0;//长度
			if(index==-1){
				l=value.toString().length();
			}else{
				l=value.toString().substring(value.toString().indexOf(".")).length();
			}
			if(l>15){
				re=Math.round(Double.parseDouble(value.toString()));
			}else{
				re= Math.round(Float.parseFloat(value.toString()));
			}
		}
		return re;
	}
	
	/**
	 * 将数字舍入为指定位数或小数位
	 * @param value
	 * @param precision
	 * @return
	 */
	public Object round(Object value,Double precision){
		Object re=null;
		if(value==null||null==precision){
			re=null;
		}else{
			BigDecimal pcs=new BigDecimal(precision);
			int precisionInt = pcs.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
			int index=value.toString().indexOf(".");//查看是否有小数点
			int l=0;//长度
			if(index==-1){
				l=value.toString().length();
			}else{
				l=value.toString().substring(value.toString().indexOf(".")).length();
			}
			if(l>15){
				BigDecimal b=new BigDecimal(Double.parseDouble(value.toString()));
				re= b.setScale(precisionInt, BigDecimal.ROUND_HALF_UP).doubleValue();
			}else{
				BigDecimal b=new BigDecimal(Float.parseFloat(value.toString()));
				re= b.setScale(precisionInt, BigDecimal.ROUND_HALF_UP).floatValue();
				
			}
		}
		return re;
	}
	
	/**
	 * 将数字舍入为指定位数或小数位
	 * @param value
	 * @param precision
	 * @return
	 */
	public Object round(Object value,double precision){
		Object re=null;
		if(value==null){
			re=null;
		}else{
			BigDecimal pcs=new BigDecimal(precision);
			int precisionInt = pcs.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
			int index=value.toString().indexOf(".");//查看是否有小数点
			int l=0;//长度
			if(index==-1){
				l=value.toString().length();
			}else{
				l=value.toString().substring(value.toString().indexOf(".")).length();
			}
			if(l>15){
				BigDecimal b=new BigDecimal(Double.parseDouble(value.toString()));
				re= b.setScale(precisionInt, BigDecimal.ROUND_HALF_UP).doubleValue();
			}else{
				BigDecimal b=new BigDecimal(Float.parseFloat(value.toString()));
				re= b.setScale(precisionInt, BigDecimal.ROUND_HALF_UP).floatValue();
				
			}
		}
		return re;
	}
	
	//=================5.判断函数==========================================
	
	/**
	 * is_number函数实现
	 * 返回字符串是否为有效数字。
	 * @param data
	 * @return
	 */
	public static Boolean is_number(String data){
		if(null==data){
			return null;
		}
		data = data.trim();
		String os = System.getProperty("os.name");  
		if(os.toLowerCase().startsWith("win")){  
			data = data.replaceAll("(?i)"+"d", "e");
		} 
		
		try {  
			BigDecimal bd = new BigDecimal(data);
//	        Double num2 = Double.valueOf(data);  
//	        System.out.println("Is Number!" + num2);
	        return true;
		} catch (Exception e) {  
		    return false;
		}
	}
	
	/**
	 * is_spaces函数实现
	 * 返回字符串值是否完全由空格组成。
	 * 空格可以运用空格键，换页键，换行键，回车键，制表键或垂直制表键输入。
	 * \t 空格 ('\u0009') \f 换页符  \n 换行 ('\u000A') \r 回车 ('\u000D')  \s 空白符号 [\t\n\x0B\f\r] \v 垂直制表符
	 * @param data IS_SPACES 将空字符串计算为 FALSE
	 * @return
	 */
	public static Boolean is_spaces(String data){
		Boolean isFlg = false;
		if(null==data){
			return null;
		}
		if(!"".equals(data)){
//			Pattern p = Pattern.compile("\\s*|\\t|\\f|\\n|\\r|\\v");
			Pattern p = Pattern.compile("\\t|\\f|\\n|\\r|\\v");
			Matcher m = p.matcher(data);
			String dest = m.replaceAll("");
			if(StringUtils.isBlank(dest)){
				isFlg = true;
			}
		}
		
		return isFlg;
	}

	/**
	 * ISNULL函数实现
	 * 返回值是否为 NULL。ISNULL 将空字符串计算为 FALSE。
	 * @param data
	 * @return
	 */
	public Boolean isnull(Object data){
		if(null==data){
			return true;
		}
		return false;
	}
	
	/**
	 * to_bigint函数实现 - flag 当您忽略此参数时
	 * 将字符串或数值转换为长整型值。TO_BIGINT 语法包含可选参数，您可以选择该参数将数值舍入为最接近的整数或截断小数部分。 TO_BIGINT 忽略前导空白。 
	 * #flag 当您忽略此参数时，TO_BIGINT 将值舍入为最接近的整数。 
	 * TODO 9223372036854775805.9 -> 9223372036854775808 BUG
	 * @param data String 或 numeric 数据类型。 传递要转换为长整型值的值。 可输入任何有效的转换表达式。
	 * @return Object
	 */
	public static Object to_bigint(Object data){
		BigInteger reBigInt=new BigInteger("0");
		//传递至函数的值为 NULL 时返回 NULL。 
		if(null==data){
			return null;
		}
		String dataStr = data.toString();
		dataStr = dataStr.trim();
		//当传递至函数的值包含字母字符时返回 0。 
		if(is_number(dataStr)){
			BigDecimal bigNum = null;
			if (data instanceof Double) {
				bigNum=new BigDecimal((double)data);
			}else{
				bigNum=new BigDecimal(dataStr);
			}
			// 四舍五入 -向上取整
			bigNum  = bigNum.setScale( 0, BigDecimal.ROUND_HALF_UP );
			reBigInt=new BigInteger(bigNum.toString());
		}
		return reBigInt;
	}
	
	
	/**
	 * to_bigint函数实现 - flag 当0 或者非零时
	 * 将字符串或数值转换为长整型值。TO_BIGINT 语法包含可选参数，您可以选择该参数将数值舍入为最接近的整数或截断小数部分。 TO_BIGINT 忽略前导空白。 
	 * #flag当标志为 TRUE 或非 0 数字时，TO_BIGINT 截断小数部分。 
	 * #flag当标志为 FALSE 或 0 或当您忽略此参数时，TO_BIGINT 将值舍入为最接近的整数。
	 * TODO 9223372036854775805.9 -> 9223372036854775808 BUG
	 * @param data String 或 numeric 数据类型。 传递要转换为长整型值的值。 可输入任何有效的转换表达式。
	 * @return Object
	 */
	public static Object to_bigint(Object data,Integer flag){
		BigInteger reBigInt=new BigInteger("0");
		//传递至函数的值为 NULL 时返回 NULL。 
		if(null==data){
			return null;
		}
		String dataStr = data.toString();
		dataStr = dataStr.trim();
		//当传递至函数的值包含字母字符时返回 0。 
		if(is_number(dataStr)){
			BigDecimal bigNum = null;
			if (data instanceof Double) {
				bigNum=new BigDecimal((double)data);
			}else{
				bigNum=new BigDecimal(dataStr);
			}
			if(0!=flag){
				// 截断 -向上取整
				bigNum  = bigNum.setScale( 0, BigDecimal.ROUND_UP );
			}else{
				// 四舍五入 -向上取整
				bigNum  = bigNum.setScale( 0, BigDecimal.ROUND_HALF_UP );
			}
			reBigInt=new BigInteger(bigNum.toString());
		}
		return reBigInt;
	}
	
	/**
	 * to_bigint函数实现 - flag当标志为 TRUE、FALSE
	 * 将字符串或数值转换为长整型值。TO_BIGINT 语法包含可选参数，您可以选择该参数将数值舍入为最接近的整数或截断小数部分。 TO_BIGINT 忽略前导空白。 
	 * #当标志为 TRUE 或非 0 数字时，TO_BIGINT 截断小数部分。 
	 * 当标志为 FALSE 或 0 或当您忽略此参数时，TO_BIGINT 将值舍入为最接近的整数。
	 * TODO 9223372036854775805.9 -> 9223372036854775808 BUG
	 * @param data String 或 numeric 数据类型。 传递要转换为长整型值的值。 可输入任何有效的转换表达式。
	 * @return Object
	 */
	public static Object to_bigint(Object data,Boolean flag){
		BigInteger reBigInt=new BigInteger("0");
		//传递至函数的值为 NULL 时返回 NULL。 
		if(null==data){
			return null;
		}
		String dataStr = data.toString();
		dataStr = dataStr.trim();
		//当传递至函数的值包含字母字符时返回 0。 
		if(is_number(dataStr)){
			BigDecimal bigNum = null;
			if (data instanceof Double) {
				bigNum=new BigDecimal((double)data);
			}else{
				bigNum=new BigDecimal(dataStr);
			}
			if(flag){
				// 截断 -向上取整
				bigNum  = bigNum.setScale( 0, BigDecimal.ROUND_DOWN );
			}else{
				// 四舍五入 -向上取整
				bigNum  = bigNum.setScale( 0, BigDecimal.ROUND_HALF_UP );
			}
			reBigInt=new BigInteger(bigNum.toString());
		}
		return reBigInt;
	}
	
	/**
	 * TODO
	 * to_char函数实现 - 将日期转换为字符串。 TO_CHAR 也将数值转换为字符串。
	 * #将双精度值转换为长达 16 位的字符串，并精确到 15 位。 如果传递超过 15 位数的数值，则 TO_CHAR 将该数值舍入为第十六位数。 
	 * #为范围内的数值返回小数表示法（-1e16、-1e-16] 和 [1e-16, 1e16）。 TO_CHAR 为这些范围之外的数值返回科学表示法。 
	 * 传递至函数的值为 NULL 时返回 NULL。  
	 * 1.234567890123456789e-10 ->0.0000000001234567890123457 (greater than 1e-16 but less than 1e16) BUG 
	 * 10842764968208837340 BUG
	 * @param data 必需 数值数据类型。 要转换为字符串的数值。 可输入任何有效的转换表达式。
	 * @return Object
	 */
	public static String to_char(Object dataObj){
		if(null==dataObj){
			return null;
		}
		String dataStr = "";
		if ((dataObj instanceof Double)||dataObj instanceof Integer||dataObj instanceof BigInteger) {
			dataStr = dataObj.toString();
		}else{
			Date data = Object2Date(dataObj);
			SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss:SSSSSS");
			dataStr  = fmt.format(data); 
			
		}
		return dataStr;
	}
	
	/**
	 * 参数初始化为JAVA Date 类型
	 * 函数如果是Date类型时，需要用Object接然后通过默认格式，格式化成JAVA日期类型
	 * @param data
	 * @return
	 */
	@SuppressWarnings("finally")
	public static Date Object2Date(Object data){
		Date reDate = null;
		String dataStr = data.toString();
		String defFormatStr = "yyyy-MM-dd HH:mm:ss:SSSSSS";
		SimpleDateFormat fmt = new SimpleDateFormat(defFormatStr);
		try {
			reDate = fmt.parse(dataStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}finally{
			return reDate;
		}
	}
	
	/**
	 * to_char函数实现 - 将日期转换为字符串。也可使用 TO_CHAR 格式字符串将日期转换为任何格式。 
	 * #将双精度值转换为长达 16 位的字符串，并精确到 15 位。 如果传递超过 15 位数的数值，则 TO_CHAR 将该数值舍入为第十六位数。 
	 * #为范围内的数值返回小数表示法（-1e16、-1e-16] 和 [1e-16, 1e16）。 TO_CHAR 为这些范围之外的数值返回科学表示法。 
	 * 传递至函数的值为 NULL 时返回 NULL。  
	 * 1.234567890123456789e-10 ->0.0000000001234567890123457 (greater than 1e-16 but less than 1e16) BUG 
	 * 10842764968208837340 BUG
	 * @param data 必需 数值数据类型。 要转换为字符串的数值。 可输入任何有效的转换表达式。
	 * @return Object
	 */
	public static String to_char(Object object,String formatStrPar){
		//传递至函数的值为 NULL 时返回 NULL。 
		if(null==object){
			return null;
		}
		//返回的结果
		String dataResult="";
		String dataStr = object.toString();
		formatStrPar = formatStrPar.toUpperCase();
		//参数初始化为JAVA Date 类型
		Date date = Object2Date(object);
		try {
//			Date date = fmt.parse(dataStr);
			String formatStr = formatStrPar;
			formatStr = formatStr.replaceAll("MON", "MMMM");
			formatStr = formatStr.replaceAll("DAY", "E");
			formatStr = formatStr.replaceAll("DDD", "CCC");
			formatStr = formatStr.replaceAll("DD", "dd");
			formatStr = formatStr.replaceAll("D", "E");
			formatStr = formatStr.replaceAll("CCC", "D");
			formatStr = formatStr.replaceAll("MI", "mm");
			formatStr = formatStr.replaceAll("SSSSS", "sssss");
			formatStr = formatStr.replaceAll(".US", ".SSSSSS");
			formatStr = formatStr.replaceAll("HH24", "HH");
			formatStr = formatStr.replaceAll("HH12", "hh");
			formatStr = formatStr.replaceAll("RR", "YY");
			//年中的周数
			formatStr = formatStr.replaceAll("WW", "w");
			//月份中的周数
			formatStr = formatStr.replaceAll("W", "W");
			int week_index = 0;
			SimpleDateFormat format = null;
			if(formatStrPar.indexOf("MON")>=0){
				format = new SimpleDateFormat(formatStr,Locale.ENGLISH); 
			}else if(formatStrPar.indexOf("DAY")>=0){
				format = new SimpleDateFormat(formatStr,Locale.ENGLISH); 
			}else if(formatStrPar.equalsIgnoreCase("J")){
			}else{
				format = new SimpleDateFormat(formatStr); 
			}
			//以下是对某些format的特殊处理
			//当formatStrPar为"D"时,这个为了获取这是这周的第几天
			if(formatStrPar.equalsIgnoreCase("D")){
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				week_index = cal.get(Calendar.DAY_OF_WEEK) ;
				dataResult = String.valueOf(week_index);
			//当formatStrPar为"MM/DD/YY"时,获取年月日
			}else if(formatStrPar.equalsIgnoreCase("MM/DD/YY")||formatStrPar.equalsIgnoreCase("MM/DD/RR")){
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				int year = cal.get(Calendar.YEAR);
				//获取年的后两位
				String yearstr = String.valueOf(year).substring(2);
				int month = cal.get(Calendar.MONTH);
				//国际得到的月要比中国实际的月少一个月
				month=month+1;
				int day = cal.get(Calendar.DATE);
				dataResult=month+"/"+day+"/"+yearstr;
			//当formatStrPar为"SSSSSS"时,换算自午夜起,一共有多少秒	
			}else if(formatStrPar.equalsIgnoreCase("SSSSS")){
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				int min = cal.get(Calendar.MINUTE);
				int sec = cal.get(Calendar.SECOND);
				int secondall = hour*3600+(min)*60+sec;
				dataResult = String.valueOf(secondall);
			//当formatStrPar为"HH24"时,得出小时
			}else if(formatStrPar.equalsIgnoreCase("HH24")){
				dataResult = format.format(date);
				if(dataStr.toUpperCase().contains("PM")){
					int parseInt = Integer.parseInt(dataResult);
					dataResult = String.valueOf(parseInt+12);
				}
				//国际上中午的12:00是00:00
				if(dataStr.toUpperCase().contains("AM")&&dataResult.equals("12")){
					dataResult ="00";
				}
			}else if(formatStrPar.equalsIgnoreCase("J")){
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH)+1;
				int day = cal.get(Calendar.DAY_OF_YEAR);
				int l; l=0; 
				if(month==1||month==2){
					l=1; 
				}
				dataResult = String.valueOf((14956+day+(int)(((float)year-(float)l)*365.25)+(int)(((float)month+1+l*12)*30.6001))); 
			}else{
				dataResult = format.format(date); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataResult;
	}
	
	/**
	 * to_date函数实现 - 将字符串转换为 Date/Time 数据类型。 您使用 TO_DATE 格式字符串指定源字符串的格式。 
	 * #输出端口必须为 TO_DATE 表达式的 Date/Time。 
	 * #如果通过 TO_DATE 转换两位数年份，请使用 RR 或 YY 格式字符串。 不要使用 YYYY 格式字符串。 
	 * 传递至函数的值为 NULL 时返回 NULL。  
	 *
	 * @param data 须是 string 数据类型。 传递想要转换为日期的值。 可输入任何有效的转换表达式。 
	 * @return Date
	 */
	public static Date to_date(String data,String... formatStrArr){
		//传递至函数的值为 NULL 时返回 NULL。 
		if(null==data){
			return null;
		}
		Date reDate = null;
		String defFormatStr = "YYYY-MM-dd HH:mm:ss:SSSSSS";
		if(null!=formatStrArr&&formatStrArr.length>0){
			defFormatStr = formatStrArr[0];
			defFormatStr = defFormatStr.toUpperCase();
		}
		String formatStr =defFormatStr;
		try {
			formatStr = formatStr.replaceAll("MON", "MMMM");
			formatStr = formatStr.replaceAll("DAY", "E");
			formatStr = formatStr.replaceAll("DDD", "CCC");
			formatStr = formatStr.replaceAll("DD", "dd");
			formatStr = formatStr.replaceAll("D", "dd");
			formatStr = formatStr.replaceAll("CCC", "D");
			formatStr = formatStr.replaceAll("MI", "mm");
			formatStr = formatStr.replaceAll("SS", "ss");
			formatStr = formatStr.replaceAll(".US", ".SSSSSS");
			formatStr = formatStr.replaceAll("HH24", "HH");
			formatStr = formatStr.replaceAll("HH12", "hh");
//			formatStr = formatStr.replaceAll("RR", "yy");
//			formatStr = formatStr.replaceAll("YY", "yy");
			formatStr = formatStr.replaceAll("YYYY", "yyyy");
			//年中的周数
			formatStr = formatStr.replaceAll("WW", "w");
			//月份中的周数
			formatStr = formatStr.replaceAll("W", "W");
			formatStr = formatStr.trim();
			SimpleDateFormat format = null;
			//如果日期没有年的话，给他一个当前系统的年
			if(formatStr.indexOf("yyyy")<0&&defFormatStr.indexOf("Y")<0&&defFormatStr.indexOf("J")<0&&defFormatStr.indexOf("MM/DD/RR")<0){
				formatStr = "yyyy-"+formatStr;
				String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				data = year+"-"+data;
			//当formatStr为MM/DD/YY或者MM/DD/RR时的特殊处理	
			}else if(defFormatStr.equalsIgnoreCase("MM/DD/YY")||defFormatStr.equalsIgnoreCase("MM/DD/RR")){
				String[] split = data.split("/");
				Date date=new Date();
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				int year = cal.get(Calendar.YEAR);
				int nowTime = Integer.parseInt(String.valueOf(year).substring(2));
				int putTime = Integer.parseInt(split[2]);
				String dateStr="";
				if(putTime>nowTime){
					dateStr = split[0]+" "+split[1]+" 19"+split[2]+" 00:00:00";
				}else{
					dateStr = split[0]+" "+split[1]+" 20"+split[2]+" 00:00:00";
				}
				format = new SimpleDateFormat("MM dd yyyy HH:mm:ss"); 
				reDate = format.parse(dateStr);
			//当formatStr为MON DD YYYY HH12:MI:SSAM时的特殊处理
			}else if(defFormatStr.equalsIgnoreCase("MON DD YYYY HH12:MI:SSAM")){
				format = new SimpleDateFormat("MM dd yyyy HH:mm:ss");
				String datestr=(String)data;
				reDate = format.parse(datestr);
			//将秒转化成时分秒	
			}else if(defFormatStr.equalsIgnoreCase("MM/DD/YYYY SSSSS")){
				String[] split = data.split(" ");
				String timeStr=split[1];
				int time = Integer.parseInt(timeStr);
				String truetime="";
				if(time<60){
					truetime="00:00:"+time;
				}else if(time<3600){
					Integer min=3600/60;
					Integer sec=3600%60;
					truetime="00:"+min+":"+sec;
				}else if(time<3600 * 24){
					Integer hour=time/3600;
					Integer min=time / 60 % 60;
					Integer sec=time%3600%60;
					if(sec==0){
						truetime=hour+":"+min+":00";
						
					}else{
						truetime=hour+":"+min+":"+sec;
					}
				}
			String alltime=split[0] + " "+truetime;
			format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"); 
			reDate = format.parse(alltime);
			//当formatStr为MM/DD/YYY时的特殊处理
			}else if(defFormatStr.equalsIgnoreCase("MM/DD/YYY")){
				String[] split = data.split("/");
				Date date=new Date();
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				int year = cal.get(Calendar.YEAR);
				int nowTime = Integer.parseInt(String.valueOf(year).substring(2));
				int putTime = Integer.parseInt(split[2]);
				putTime = Integer.parseInt(String.valueOf(putTime).substring(1));
				String dateStr="";
				if(putTime>nowTime){
					dateStr = split[0]+" "+split[1]+" 1"+split[2]+" 00:00:00";
				}else{
					dateStr = split[0]+" "+split[1]+" 2"+split[2]+" 00:00:00";
				}
				format = new SimpleDateFormat("MM dd yyyy HH:mm:ss"); 
				reDate = format.parse(dateStr);
			//当formatStr为MM/DD/Y时的特殊处理
			}else if(defFormatStr.equalsIgnoreCase("MM/DD/Y")){
				String[] split = data.split("/");
				String dateStr = split[0]+" "+split[1]+" 199"+split[2]+" 00:00:00";
				format = new SimpleDateFormat("MM dd yyyy HH:mm:ss"); 
				reDate = format.parse(dateStr);
			//当formatStr为J时的特殊处理
			}else if(defFormatStr.equalsIgnoreCase("J")){
//				int h = data[offset++];
//			    int mi = data[offset++];
//			    int s = data[offset++];
//				String dataStr=(String)data;
//				int mjd = Integer.parseInt(dataStr);
//				 int y, m, d, k;
//			        y = (int) ((mjd - 15078.2) / 365.25);
//			        m = (int) ((mjd - 14956.1 - (int) (y * 365.25)) / 30.6001);
//			        d = (int) (mjd - 14956 - (int) (y * 365.25) - (int) (m * 30.6001));
//			        if (m == 14 || m == 15)
//			            k = 1;
//			        else
//			            k = 0;
//			        y = y + k;
//			        m = m - 1 - k * 12;
//			        Calendar c = GregorianCalendar.getInstance();
//			        c.set(y + 1900, m - 1, d, h, mi, s);
//			        return c.getTime();
//				
				
				
				
				String dataStr=(String)data;
				int dataInt = Integer.parseInt(dataStr);
			    int year = (dataInt / 1000) + 1900;
			    int dayOfYear = dataInt % 1000;
			    Calendar calendar = Calendar.getInstance();
			    calendar.set(Calendar.YEAR, year);
			    calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
			    reDate = calendar.getTime();
			//其他特殊情况	
			}else{
				formatStr = ""+formatStr;
	//			formatStr = "yyyy MM dd";
				format = new SimpleDateFormat(formatStr); 
				reDate = format.parse(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reDate;
	}
	
	/**
	 * to_decimal函数实现 - 将字符串或数值转换为十进制值。TO_DECIMAL 忽略前导空白。 
	 * #如果字符串包含非数字字符，请转换字符串中直到第一个非数字字符的数字部分。 
	 * #当第一个数值字符为非数值时返回 0。 
	 * 传递至函数的值为 NULL 时返回 NULL。  
	 * Note: 如果返回值为精度大于 15 的小数，则可启用高精度，以确保小数精确至 28 位。 
	 * @param data 必须是 string 或 numeric 数据类型。 传递要转换为小数的值。 可输入任何有效的转换表达式。 
	 * @param scale int 必须是 0 与 28 之间（包括 0 和 28）的整数文字。 指定小数点后允许的位数。 如果忽略此参数，则函数返回小数位数与输入值相同的值。 
	 * @return Date
	 */
	public static BigDecimal to_decimal(String data,int... scaleArr){
		//传递至函数的值为 NULL 时返回 NULL。 
		if(null==data){
			return null;
		}
		BigDecimal bigNum = new BigDecimal(0);
		if(is_number(data)){
			bigNum = new BigDecimal(data);
			if(data.indexOf(".")>0){
				int scale = 0;
				if(null!=scaleArr&&scaleArr.length>0){
					scale = scaleArr[0];
					bigNum  = bigNum.setScale(scale, BigDecimal.ROUND_HALF_UP);
				}
			}
		}else{
			String numStr = "";
			//如果字符串包含非数字字符，请转换字符串中直到第一个非数字字符的数字部分。 
			for(int i=0;i<data.length();i++){
				//掩码CODE
				String codeChar = data.substring(i, i+1);
				if(isCellInteger(codeChar)||".".equals(codeChar)){
					numStr+=codeChar;
				}else{
					break;
				}
			}
			//当第一个数值字符为非数值时返回 0。 
			if(StringUtils.isNotBlank(numStr)){
				bigNum = to_decimal(numStr,scaleArr);
			}
		}
		
		return bigNum;
	}
	
	/**
	 * to_float函数实现 - 将字符串或数值转换为双精度浮点数字（Double 数据类型）。 TO_FLOAT 忽略前导空白。 
	 * #端口中的值为空白或非数字字符时返回 0。 
	 * 传递至函数的值为 NULL 时返回 NULL。  
	 * Note: 双精度值。 
	 * @param data 必须是 string 或 numeric 数据类型。 传递要转换为双精度值的值。 可输入任何有效的转换表达式。 
	 * @return Date
	 */
	public static Double to_float(String data){
		//传递至函数的值为 NULL 时返回 NULL。 
		if(null==data){
			return null;
		}
		data = data.trim();
		Double doubleNum = new Double(0);
		if(is_number(data)){
			doubleNum = new Double(data);
		}
		
//		System.out.println(doubleNum.toString());
		return doubleNum;
	}
	
	
	/**
	 * to_integer函数实现 - 将数值或字符串值转换为整数。 TO_INTEGER 语法包含可选参数，您可以选择该参数将数值舍入为最接近的整数或截断小数部分。 TO_INTEGER 忽略前导空白。 
	 * #端口中的值为空白或非数字字符时返回 0。 
	 * 传递至函数的值为 NULL 时返回 NULL。  
	 * Note: 双精度值。 
	 * @param data String 或 numeric 数据类型。 传递要转换为整数的值。 可输入任何有效的转换表达式。 
	 * @param flag 指定截断还是舍入小数部分。 标记必须为整数文字或常量 TRUE 或 FALSE。 
					当标志为 TRUE 或非 0 数字时，TO_INTEGER 截断小数部分。 
					当标志为 FALSE 或 0 或忽略此参数时，TO_INTEGER 将值舍入为最接近的整数。
	 * @return Date
	 */
	public static Integer to_integer(Object data){
		//传递至函数的值为 NULL 时返回 NULL。 
		if(null==data){
			return null;
		}
		Integer reNum = 0;
		String dataStr = data.toString().trim();
		//当传递至函数的值包含字母字符时返回 0。 
		if(is_number(dataStr)){
			BigDecimal intNum = null;
			if (data instanceof Double) {
				intNum=new BigDecimal((double)data);
			}else{
				intNum=new BigDecimal(dataStr);
			}
			// 四舍五入
			intNum  = intNum.setScale( 0, BigDecimal.ROUND_HALF_UP );
			//Integer MAX_VALUE	2147483647
//			if(new BigDecimal(Integer.MAX_VALUE).compareTo(intNum)==-1){
//				return reNum;
//			}
			if(isIntegerStr(intNum.toString())){
				reNum = new Integer(intNum.toString());
			}
		}
		return reNum;
	}
	
	/**
	 * to_integer函数实现 - 将数值或字符串值转换为整数。 TO_INTEGER 语法包含可选参数，您可以选择该参数将数值舍入为最接近的整数或截断小数部分。 TO_INTEGER 忽略前导空白。 
	 * #端口中的值为空白或非数字字符时返回 0。 
	 * 传递至函数的值为 NULL 时返回 NULL。  
	 * Note: 双精度值。 
	 * @param data String 或 numeric 数据类型。 传递要转换为整数的值。 可输入任何有效的转换表达式。 
	 * @param flag 指定截断还是舍入小数部分。 标记必须为整数文字或常量 TRUE 或 FALSE。 
					当标志为 TRUE 或非 0 数字时，TO_INTEGER 截断小数部分。 
					当标志为 FALSE 或 0 或忽略此参数时，TO_INTEGER 将值舍入为最接近的整数。
	 * @return Date
	 */
	public static Integer to_integer(Object data,Object flag){
		//传递至函数的值为 NULL 时返回 NULL。 
		if(null==data){
			return null;
		}
		Integer reNum = 0;
		String dataStr = data.toString().trim();
		Boolean roundFlg = true;
		if(null!=flag){
			if("0".equals(flag.toString())||"FALSE".equalsIgnoreCase(flag.toString())){
				roundFlg = false;
			}
		}
		//数字中带逗号分隔符的情况：'5,000,000,000'
		dataStr = dataStr.replace(",", "");
		//当传递至函数的值包含字母字符时返回 0。 
		if(is_number(dataStr)){
			BigDecimal intNum = null;
			if (data instanceof Double) {
				intNum=new BigDecimal((double)data);
			}else{
				intNum=new BigDecimal(dataStr);
			}
			if(roundFlg){
				// 截断 -向上取整
				intNum  = intNum.setScale( 0, BigDecimal.ROUND_DOWN );
			}else{
				// 四舍五入 -向上取整
				intNum  = intNum.setScale( 0, BigDecimal.ROUND_HALF_UP );
			}
			if(isIntegerStr(intNum.toString())){
				reNum = new Integer(intNum.toString());
			}
		}
		return reNum;
	}
	
	 private static boolean isNumber(String str){
        String reg = "^[0-9]+(.[0-9]+)?$";
        return str.matches(reg);
	 }
	
		
	/*推荐，速度最快
	  * 判断是否为整数单字符串
	  * @param str 传入的字符串 
	  * @return 是整数返回true,否则返回false 
	*/
	  public static boolean isCellInteger(String str) {  
	        Pattern pattern = Pattern.compile("[\\d]");  
	        return pattern.matcher(str).matches();  
	  }
	  
	  /*推荐，速度最快
		  * 判断是否为整数字符串 
		  * @param str 传入的字符串 
		  * @return 是整数返回true,否则返回false 
		*/
	  public static boolean isIntegerStr(String value) {
        try {  
            Integer.parseInt(value);  
        } catch (NumberFormatException e) {  
            return false;  
        }  
        return true;  
     }
	  
	//=================6.判断逻辑==========================================
	/**
	 * iif函数实现
	 * 根据条件的结果返回指定的两个值中的一个。 
	 * @param condition 要计算的条件。 
	 * @param val1 除了二进制以外的任何数据类型。 条件为 TRUE 时要返回的值。 
	 * @param val2 除了二进制以外的任何数据类型。 条件为 FALSE 时要返回的值。
	 * @return
	 */
	public static Object iif(Boolean condition,Object val1,Object val2){
		if(null==condition||condition){
			return val1;
		}
		return val2;
	}
	
	/**
	 * iif函数实现
	 * 根据条件的结果返回指定的两个值中的一个。 
	 * @param condition 要计算的条件。 
	 * @param val1 除了二进制以外的任何数据类型。 条件为 TRUE 时要返回的值。 
	 * @param val2 除了二进制以外的任何数据类型。 条件为 FALSE 时要返回的值。
	 * @return
	 */
	public Object iif(Boolean condition,Object val1){
		//当 val1 为 Date/Time 数据类型时返回 NULL。 
		Object reObj = null;
		if(null==condition||condition){
			return val1;
		}else{
			if(null==val1){
				return reObj;
			}else{
				//当 val1 为 Numeric 数据类型时，返回 0。 
				if(val1 instanceof Number){
					reObj = 0;
				//当 val1 为 String 数据类型时返回空字符串	
				}else if(val1 instanceof String){
					reObj = "";
				}
			}
		}
		return reObj;
	}
	
	/**
	 *  decode函数实现
	 *  为指定的值搜索端口。如果函数找到值，则会返回您定义的结果值。可以在 DECODE 函数内构建无限多个搜索。 
	 *	如果使用 DECODE 在字符串端口中搜索值，则可使用 RTRIM 函数裁减尾随空白，或在搜索字符串中包括空白。
	 * @param value 除了二进制以外的任何数据类型。 传递要搜索的值。
	 * @param cond：result  除了二进制以外的任何数据类型。  传递要搜索的值：搜索到匹配值时要返回的值
	 * @param def  未搜索到匹配值时要返回的值
	 * @return
	 */
	public Object decode(Object value,Object... cond){
		//如果忽略默认参数且未搜索到匹配值，则返回 NULL。 
		Object reStr = null;
		Boolean matchFlg = false;
		//如果忽略默认参数且未搜索到匹配值，则返回 NULL。 
		if(null!=cond&&cond.length>=2){
			Object def =  null;
			if(0!=cond.length%2){
				def = cond[cond.length-1];
			}
			if(null==value){
				return def;
			}
			for(int i=0;i<cond.length;i=i+2){
				if(value.toString().equals(cond[i].toString())){
					reStr = cond[i+1];
					matchFlg = true;
					break;
				}
			}
			//如果忽略默认参数且未搜索到匹配值，则返回 NULL。 
			if(!matchFlg){
				reStr = def;
			}
		}
		return reStr;
	}
	
	/** 
	 * RSA公钥加密 
	 *  
	 * @param value 
	 *            加密字符串
	 * @param publicKey 
	 *            公钥 
	 * @return 密文 
	 * @throws Exception 
	 *             加密过程中的异常信息 
	 */  
	public Object rsa_encrypt(Object value,String publicKey) throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{
		//base64编码的公钥
		byte[] decoded = Base64.decodeBase64(publicKey);
		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
		//RSA加密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		String outStr = Base64.encodeBase64String(cipher.doFinal(String.valueOf(value).getBytes("UTF-8")));
		return outStr;
	}
	
	/** 
	 * RSA私钥解密
	 *  
	 * @param value 
	 *            加密字符串
	 * @param privateKey 
	 *            私钥 
	 * @return 铭文
	 * @throws Exception 
	 *             解密过程中的异常信息 
	 */  
	public static String rsa_decrypt(Object value, String privateKey) throws Exception{
		//64位解码加密后的字符串
		byte[] inputByte = Base64.decodeBase64(String.valueOf(value).getBytes("UTF-8"));
		//base64编码的私钥
		byte[] decoded = Base64.decodeBase64(privateKey);  
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));  
		//RSA解密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		String outStr = new String(cipher.doFinal(inputByte));
		return outStr;
	}
	
	/**
	 * 将特殊运算符替换成JAVA运算符号
	 * @param data
	 * @return
	 */
	public static String operatorConversion(String expressionStr){
		//(?i)不区分大小写
//		expressionStr = expressionStr.replaceAll("(?i)"+"NULL", "null");
//		expressionStr = expressionStr.replaceAll("(?i)"+"TRUE", "true");
//		expressionStr = expressionStr.replaceAll("(?i)"+"FALSE", "false");
//		expressionStr = expressionStr.replace("||", "+");
//		expressionStr = expressionStr.replace("=", "==");
//		expressionStr = expressionStr.replace(">==", ">=");
//		expressionStr = expressionStr.replace("<==", "<=");
//		expressionStr = expressionStr.replace("!==", "!=");
//		expressionStr = expressionStr.replaceAll("(?i)"+"AND", "&&");
//		expressionStr = expressionStr.replaceAll("(?i)"+"OR", "||");
		
		expressionStr = StringUtils.replace(expressionStr, "TRUE","true");
		expressionStr = StringUtils.replace(expressionStr, "NULL","''");
		expressionStr = StringUtils.replace(expressionStr, "||null", "||''");
		expressionStr = StringUtils.replace(expressionStr, "||", "+");
		expressionStr = StringUtils.replace(expressionStr, "=", "==");
		expressionStr = StringUtils.replace(expressionStr, ">==", ">=");
		expressionStr = StringUtils.replace(expressionStr, "<==", "<=");
		expressionStr = StringUtils.replace(expressionStr, "!==", "!=");
		expressionStr = StringUtils.replace(expressionStr, "AND", "&&");
		expressionStr = StringUtils.replace(expressionStr, "OR", "||");


		return expressionStr;
	}
	
	/**
	 * 通过Java方法字符串 执行JAVA方法
	 * @param jexlExpression
	 * @param map
	 * @return
	 */
	public static Object executeExpression(String jexlExpression, Map<String, Object> map) {
		//创建解析表达式
		JexlExpression expression = jexlEngine.createExpression(jexlExpression);
//	    return expression.evaluate(context);
	    return null;
	}
	
	
	public static Object executeExpression1(String jexlExpression, Map<String, Object> map) {
	    JexlExpression expression = jexlEngine.createExpression(jexlExpression);
	    JexlContext context = new MapContext();
	    if (null!=map) {
	        map.forEach(context::set);
	    }
	    return expression.evaluate(context);
	}
	
	public static void main11(String[] args) {
	    JexlContext ctxt = new MapContext();
	    JexlEngine jexl = new Engine();
	    // 表达式
//	    String exps = "(c1>c2)? {c3=1}:{c3=2}";
	    String exps = "if(c1>c2){c3=1}else{c3=2}";
//	    JexlExpression expr = jexl.createExpression(exps);
	    if(2>1){
	    	int c3=1;
	    }
	    ctxt.set("c1", 2);
	    ctxt.set("c2", 1);
//	    expr.evaluate(ctxt);
	    
	    System.out.println(ctxt.get("c3"));
	  }
	
	/**
	 * 执行表达式
	 * @param expressStr
	 * @return
	 */
	public Object execExpress(String expressStr){
		Object reObj = null;
		expressStr = operatorConversion(expressStr);
		//遍历表达式方法 替换成JAVA 方法
		for(int i=0;i<funcLst.size();i++){
			String funcName = funcLst.get(i);
			String reFuncName = funcName;
			if("mod".equalsIgnoreCase(reFuncName)){
				reFuncName = "modJava";
			}else if("replacechr".equalsIgnoreCase(reFuncName)){
				reFuncName = "replacecharJava";
			}else if("decompress".equalsIgnoreCase(reFuncName)){
				reFuncName = "decompreJava";
			}else if("add_to_date".equalsIgnoreCase(reFuncName)){
				reFuncName = "add_to_java_date";
			}
//				expressStr = expressStr.replaceAll("(?i)"+funcName, "et."+reFuncName);
				//表达式字符串 + this类
				expressStr = StringUtils.replace(expressStr, funcName, "et."+reFuncName);
		}
		
	    executeExpression(expressStr, map);
    	return reObj;
	}
	
	
	/**
	 * 执行表达式
	 * @param expressStr
	 * @return
	 */
	public Object execExpress(String expressStr,JexlContext ctxt){
		Object reObj = null;
		expressStr = operatorConversion(expressStr);
		//遍历表达式方法 替换成JAVA 方法
		for(int i=0;i<funcLst.size();i++){
			String funcName = funcLst.get(i);
			String reFuncName = funcName;
			if("mod".equalsIgnoreCase(reFuncName)){
				reFuncName = "modJava";
			}else if("replacechr".equalsIgnoreCase(reFuncName)){
				reFuncName = "replacecharJava";
			}else if("decompress".equalsIgnoreCase(reFuncName)){
				reFuncName = "decompreJava";
			}else if("add_to_date".equalsIgnoreCase(reFuncName)){
				reFuncName = "add_to_java_date";
			}
//				expressStr = expressStr.replaceAll("(?i)"+funcName, "et."+reFuncName);
				//表达式字符串 + this类
				expressStr = StringUtils.replace(expressStr, funcName, "et."+reFuncName);
		}
		//创建解析表达式
		JexlExpression jexlExpressionObj;
		if(jexlExpMap.containsKey(expressStr)){
			jexlExpressionObj = jexlExpMap.get(expressStr);
		}else{
			jexlExpressionObj = jexlEngine.createExpression(expressStr);
			jexlExpMap.put(expressStr, jexlExpressionObj);
		}
		ctxt.set("et", this);
		reObj = jexlExpressionObj.evaluate(ctxt);
    	return reObj;
	}
	
	public static void main22(String[] args) {
		et = new ExpressionTool();
		et.init();
		String expressStr5 = "iif(false,'2019-02-11','ASFDASDF')";
		Object str = execExpress(expressStr5,et);
	
	}
	
	public static void main(String[] args) {
		et = new ExpressionTool();
		et.init();
//		String expressStr = "iif(is_null(in_put),in_put||' ',in_put)";
//		expressStr = expressStr.replaceAll("in_put", "'1212'");
//		String reStr = execExpress(expressStr,et);
//		System.out.println("reStr is "+reStr);
//		
//		String expressStr1 = "decode(value,'1','111','2','222','3','333','2')";
//		expressStr1 = expressStr1.replaceAll("value", "'1'");
//		String reStr1 = execExpress(expressStr1,et);
////		String reStr1 = execExpress("decode('1','1','111','2','222','3','333','2')");
//		System.out.println(">>>>>reStr1 is "+reStr1);
		
//		String expressStr2 = "IIF(REG_MATCH(input, '[a-zA-Z]+'),'1',iif(reg_match(input, '[\\x{4e00}-\\x{9fa5}]+'),'2','3'))";
//		expressStr2 = expressStr2.replaceAll("input", "'你好'");
//		String reStr2 = execExpress(expressStr2,et);
//		System.out.println(">>>>>reStr2 is "+reStr2);
		
//		String expressStr3 = "substr(input,2,length(input)-1)";
//		String expressStr3 = "substr(input,2)";
//		expressStr3 = expressStr3.replaceAll("input", "'1234567'");
//		String reStr3 = execExpress(expressStr3,et);
//		System.out.println(">>>>>reStr3 is "+reStr3);
		
//		String expressStr4 = "mod(10.1,3)";
//		String expressStr4 = "rpad('sdafdsfasd',5,'******........******')";
//		String expressStr3 = "substr(input,2)";
//		String reStr4 = execExpress(expressStr4,et);
//		System.out.println(">>>>>reStr4 is:"+reStr4+">>>>>>>>>>");
//		String expressStr5 = "ltrim(null)";
//		String expressStr3 = "substr(input,2)";
//		String reStr5 = execExpress(expressStr5,et);
//		System.out.println(">>>>>reStr5 is "+reStr5);
		
		
//		String expressStr4 = "REG_EXTRACT('Stephen Graham Smith','(\\w+)\\s+(\\w+)\\s+(\\w+)',2)";
//		String reStr4 = execExpress(expressStr4,et);
//		System.out.println(">>>>>reStr4 is:"+reStr4+">>>>>>>>>>");
		
//		
//		String expressStr5 = "iif(instr('李富贵','*') = 0,iif(length('李富贵')=2,Substr('李富贵',0,1)||'*',iif(length('李富贵')>=3,Substr('李富贵',0,1)||'*'||Substr('李富贵',3,length('李富贵')),Substr('李富贵',0,1)||'**')),'李富贵')";
//		
//			   expressStr5 = "iif(instr('李富贵','*') = 0,iif(length('李富贵')=2,Substr('李富贵',0,1)||'*',iif(length('李富贵')>=3,Substr('李富贵',0,1)||'*'||Substr('李富贵',3,length('李富贵')),Substr('李富贵',0,1)||'**')),'李富贵')";
//		String reStr5 = execExpress(expressStr5,et);
//		System.out.println(">>>>>reStr5 is "+reStr5);
//		String a=" 1231232131231";
//		System.out.println(a.startsWith(" "));
		
		
//		String str = et.iif(et.length("李富贵")>=3,et.substr("李富贵",0,1)+"*"+et.substr("李富贵",3,et.length("李富贵")),et.substr("李富贵",0,1)+"**");
//		System.out.println(">>>>>str is "+str);
//		String expressStr5 = "decode(IIF(REG_MATCH('李富贵', '[a-zA-Z]+'),'1',IIF(REG_MATCH('李富贵', '[\\x{4e00}-\\x{9fa5}]+'),'2','3')),'1','1','2','2','3','3','2')";
//		String str = execExpress(expressStr5,et);
		
//		String expressStr5 = "iif(-126.00<0,0--126.00,126.00)";
//		String str = execExpress(expressStr5,et);
//		System.out.println(">>>>>str is "+str);
		
		
//		concat(1,2);
//		String expressStr5 = "instr('aaabbc','a',0,2)";
//		String str = execExpress(expressStr5,et);	
//		int index = ExpressionTool.instr("adsadadfsdfabbc","a",-1,3);
//		int index = "bacdef".indexOf("a",-1);
//		System.out.println(">>>>>index is "+index);
		
//		String str = rtrim("s.sMacDonadldld","dl");
//		System.out.println(">>>>>str is "+str);
		
		
//		String str =  substr("12345",0);
//		String str =  substr("12345",-11,1);
//		System.out.println(">>>>>str is "+str);
		
//		String str =  reg_replace("111111","1","a",2 );
//		String str =  reg_replace("abbbab","ab","ba",2 );
//		abbbab ab ba
//		System.out.println(">>>>>str is "+str);
		
//		double str =  rand(1);
//		System.out.println(">>>>>str is "+str);
		
//		double asd = Double.parseDouble("1212".toString());
		
//		Integer str = ascii("张23");
//		String expressStr5 = "round("+Float.MAX_VALUE+",1)";
//		String expressStr5 = "round(121212,1)";
//		Object str = execExpress(expressStr5,et);
//		String str =  chr(24352);
//		System.out.println(">>>>>str is "+str);
//		Integer.valueOf(paramInt)
//		Long.BYTES
//		BigInteger.ONE
//		String expressStr5 = "iif(false,'2019-02-11','ASFDASDF')";
//		Object str = execExpress(expressStr5,et);
		
//		Object str = iif(false,"2019-02-11","ASFDASDF");
//		System.out.println(">>>>>str is "+str);
		
//		String str =  replacechr(1,"AAaaBb123456111","a","张v");
//		System.out.println(">>>>>str is "+str);
		
//		Boolean bool =  is_number("+123abc");
//		System.out.println(">>>>>str is "+bool);
		
//		Boolean bool =  is_spaces("  ");
//		Object obj = to_bigint("111.12121");
//		System.out.println(">>>>>str is "+obj);
		
		
//		String company = "Blue Fin Aqua Center";
//		Object obj = instr(company,"a");
//		System.out.println(">>>>>str is "+obj);
//		company = "Maco Shark Shop";
//		obj = instr(company,"a");
//		System.out.println(">>>>>str is "+obj);
//		company = "Scuba Gear";
//		obj = instr(company,"a");
//		System.out.println(">>>>>str is "+obj);
//		company = "Frank's Dive Shop";
//		obj = instr(company,"a");
//		company = "VIP Diving Club";
//		System.out.println(">>>>>str is "+obj);
//		obj = instr(company,"a");
//		System.out.println(">>>>>str is "+obj);
		
		
//		company = "Frank's Dive Shop";
//		obj = instr(company,"a", 1, 2);
		
//		company = "VIP Diving Club";
//		obj = instr(company,"a",-1,2);
//		
//		company = "SS #712403399";
//		obj = instr(company,"Blue Fin Aqua Center",-1,1);
//		obj = substr( company,1,instr( company," " ,-1,1 ));
//		obj = substr( company, 1, instr(company, "#")-1 ) +substr( company, instr(company,"#")+1 );
//		System.out.println(">>>>>str is "+obj);
//		String expressStr = "INSTR('Frank's Dive Shop','a',1,2)";
//		String reStr = execExpress(expressStr,et);
//		System.out.println(">>>>>reStr is:"+reStr+">>>>>>>>>>");
		
		
//		String expressStr = "REPLACECHR( 0,'\"GET /news/index.html HTTP/1.1\"','\"',NULL )";
//			   expressStr = "REPLACECHR( 0,'\"GET /companyinfo/index.html HTTP/1.1\"','\"',NULL )";
//			   expressStr = "REPLACECHR( 0,'GET /companyinfo/index.html HTTP/1.1','\"',NULL )";
//			   expressStr = "REPLACECHR( 0,NULL,'\"',NULL )";
//			   
//			   expressStr = "REPLACECHR( 1,'[29/Oct/2001:14:13:50 -0700]',']\"',NULL )";
//			   expressStr = "REPLACECHR( 1,'[29/Oct/2001:14:13:50 -0700]',']\"',NULL )";
//			   
//			   expressStr = "REPLACECHR ( 1, 'ABA', 'A', 'M' )";
//			   expressStr = "REPLACECHR ( 1, 'abA', 'A', 'M' )";
//			   expressStr = "REPLACECHR ( 1, 'BBC', 'A', 'M' )";
//			   expressStr = "REPLACECHR ( 1, 'ACC', 'A', 'M' )";
//			   expressStr = "REPLACECHR ( 1, NULL, 'A', 'M' )";
//			   
//			   expressStr = "REPLACECHR ( 0, 'ABA', 'A', NULL)";
//			   expressStr = "REPLACECHR ( 0, 'BBC', 'A', NULL)";
//			   expressStr = "REPLACECHR ( 0, 'ACC', 'A', NULL)";
//			   expressStr = "REPLACECHR ( 0, 'AAA', 'A', NULL)";
//			   expressStr = "REPLACECHR ( 0, 'aaa', 'A', NULL)";
//			   expressStr = "REPLACECHR ( 0, NULL, 'A', NULL)";
//			   
//			   expressStr = "REPLACECHR ( 1, '12345', '14', NULL )";
//			   expressStr = "REPLACECHR ( 1, '4141', '14', NULL )";
//			   expressStr = "REPLACECHR ( 1, '111115', '14', NULL )";
//			   expressStr = "REPLACECHR ( 1, NULL, '14', NULL )";
//			   
//			   expressStr = "REPLACECHR (1, 'Tom Smith  Laura Jones', CHR(39), NULL )";
//			   
//			   expressStr = "to_char('2019-02-02','YYYY-MM-DD')";
//			   expressStr = "mod(20,3)";
//			   expressStr = "DECODE('ssd','dfd','dfsdf','ssd','jjjj','null')";
//			   expressStr = "TO_INTEGER('10.2')";
//			   expressStr = "REPLACESTR(1,'333',chr(13),chr(10),' ','')";
//			   expressStr = "REPLACESTR(0,'abABabccabCCdd','AB','ab',' ','ff')";
//			   expressStr = "REPLACESTR(1,3336633,66,33,' ','dd')";
//			   expressStr = "to_date('04/01/98','MM/DD/RR')";
//			   expressStr = "IIF(length('6255852645856962')=9,REG_REPLACE('6255852645856962','.','*'),'6255852645856962')";
//			   expressStr = "TO_DATE(to_char('2021-05-01-03-43-50-000000','MM-DD'),'MM-DD')";
//			   expressStr = "TO_DATE('2021-05-01 03:43:50:000000','MM-DD')";
//			   expressStr = "TO_DATE('05-01','MM-DD')";
			   				
//			   expressStr = "REPLACESTR( 1,'GET', '\"', 'GET ', ' HTTP/1.1', NULL )";
//			   expressStr = "REPLACESTR ( 1, 'abc', 'ab', 'bc', 'b' )";
//			   expressStr = "REPLACESTR ( 1, 'its', CONCAT('it', CONCAT(CHR(39), 's' )), 'its' ) ";
			   
//			   expressStr = "RPAD( 'Safety Knife', 16, '*..*' )";
//			   expressStr = "RPAD( 'Flashlight', 16, '*..*' )";
//			   expressStr = "RPAD( 'Compass', 16, '*..*' )";
//			   expressStr = "RPAD( 'Regulator System', 16, '*..*' )";
			   
//			   expressStr = "RTRIM( 'Page', 're')";
//			   expressStr = "RTRIM( 'Nelson', 're')";
//			   expressStr = "RTRIM( 'Osborne', 're')";
//			   expressStr = "RTRIM( NULL, 're')";
//			   expressStr = "RTRIM( 'Sawyer', 're')";
//			   expressStr = "RTRIM( 'H. Bender', 're')";
//			   expressStr = "RTRIM( 'Steadman', 're')";
			   
			   
//			  expressStr = "COMPRESS('123')";
//			  expressStr = "COMPRESS('asfafadsfasdf战三')";
//			  expressStr = "decompress('0111100010011100010010110010110001001110010010110100110001001011010011000000000110010010110001010010100101101001110011110011101001100110001111001101100111010001000010010000000001010011001001000000100101101001')";
			  
			  
//			  expressStr = "ABS(169.95-69.95)";
//			  Double dbl = Math.abs(169.95-69.95);//TODO
//			  expressStr = "ABS(59.95-NULL)";//TODO
			   
//			  expressStr = "MOD(25, IIF( 0 = 0, NULL, 0))";
//			  expressStr = "MOD(25, null)";
			   
//			   expressStr = "round(15.9949,3)";
//			   expressStr = "round(15.9949,3)";
//			   expressStr = "round(NULL)";
//			   expressStr = "round(56.9561,3)";
//			   expressStr = "round(-18.8678,3)";
//			   expressStr = "round(-108.95,-2)";
//			   expressStr = "round(12.99,0.8)";
//			   expressStr = "round(56.34,0.8)";
//			   expressStr = "round(NULL,NULL)";
//			   expressStr = "round(-15.99)";
			   
			   
//			   expressStr = "IS_NUMBER('  123')";
//			   expressStr = "IS_NUMBER('+123abc')";
//			   expressStr = "IS_NUMBER('    ')";
//			   expressStr = "IS_NUMBER('3.45E-')";
//			   expressStr = "IS_NUMBER('-3.45D-3')";
//			   expressStr = "IS_NUMBER('-3.45e+3')";
//			   expressStr = "IS_NUMBER('123.00')";
//			   expressStr = "IIF( IS_NUMBER ( '123.00' ), TO_FLOAT( '123.00' ), 0.00 )";
//			   expressStr = "IIF( IS_NUMBER ( '3.45E-3'), TO_FLOAT('3.45E-3' ), 0.00 )";
//			   expressStr = "IIF( IS_NUMBER ( null), TO_FLOAT(null), 0.00 )";
//			   expressStr = "IS_SPACES( '' )";
			   
//			   expressStr = "TO_BIGINT('7245176201123435.2.48',TRUE)";
//			   expressStr = "TO_BIGINT('    176201123435.87',TRUE)";
//			   expressStr = "TO_BIGINT('-7245176201123435.2',TRUE)";
//			   expressStr = "TO_BIGINT('-7245176201123435.23',TRUE)";
//			   expressStr = "TO_BIGINT(-9223372036854775806.9,TRUE)";
//			   expressStr = "TO_BIGINT(-9223372036854775806.9)";
//			   expressStr = "TO_CHAR(10842764968208837340)";
//			   expressStr = "TO_FLOAT('A12.3Grove')";
			   
//			   expressStr = "TO_INTEGER('15.6789')";
//			   expressStr = "TO_INTEGER('15.6789',1)";
//			   expressStr = "TO_INTEGER('15.6789',FALSE)";
//			   expressStr = "TO_INTEGER('15.6789',0)";
//			   expressStr = "TO_INTEGER('15.6789',null)";
//			   expressStr = "TO_INTEGER('15.6789',TRUE)";
//			   expressStr = "TO_INTEGER('60.2',TRUE)";
//			   expressStr = "TO_INTEGER('118.348',TRUE)";
//			   expressStr = "TO_INTEGER('5,000,000,000',TRUE)";
//			   expressStr = "TO_INTEGER(NULL,TRUE)";
//			   expressStr = "TO_INTEGER('A12.3Grove',TRUE)";
//			   expressStr = "TO_INTEGER('    123.87',TRUE)";
//			   expressStr = "TO_INTEGER('-15.6789',TRUE)";
//			   expressStr = "TO_INTEGER('-15.23',TRUE)";
//
//			   expressStr = "TO_INTEGER('15.6789',FALSE)";
//			   expressStr = "TO_INTEGER('60.2',FALSE)";
//			   expressStr = "TO_INTEGER('118.348',FALSE)";
//			   expressStr = "TO_INTEGER('5,000,000,000',FALSE)";
//			   expressStr = "TO_INTEGER(NULL,FALSE)";
//			   expressStr = "TO_INTEGER('A12.3Grove',FALSE)";
//			   expressStr = "TO_INTEGER('    123.87',FALSE)";
//			   expressStr = "TO_INTEGER('-15.6789',FALSE)";
//			   expressStr = "TO_INTEGER('-15.23',FALSE)";
			   
//			   expressStr = "DECODE(10,10,'Flashlight',14,'Regulator',20,'Knife',40,'Tank','NONE')";
//			   expressStr = "DECODE(14,10,'Flashlight',14,'Regulator',20,'Knife',40,'Tank','NONE')";
//			   expressStr = "DECODE(17,10,'Flashlight',14,'Regulator',20,'Knife',40,'Tank','NONE')";
//			   expressStr = "DECODE(20,10,'Flashlight',14,'Regulator',20,'Knife',40,'Tank','NONE')";
//			   expressStr = "DECODE(25,10,'Flashlight',14,'Regulator',20,'Knife',40,'Tank','NONE')";
//			   expressStr = "DECODE(NULL,10,'Flashlight',14,'Regulator',20,'Knife',40,'Tank','NONE')";
//			   expressStr = "DECODE(40,10,'Flashlight',14,'Regulator',20,'Knife',40,'Tank','NONE')";
//			   
//			   expressStr = "DECODE( TRUE,21 = 22, 'Variable 1 was 22!',47 = 49, 'Variable 2 was 49!',21 < 23, 'Variable 1 was less than 23.',47 > 30, 'Variable 2 was more than 30.','Variables were out of desired ranges.')";
//			   expressStr = "DECODE( TRUE,22 = 22, 'Variable 1 was 22!',49 = 49, 'Variable 2 was 49!',22 < 23, 'Variable 1 was less than 23.',49 > 30, 'Variable 2 was more than 30.','Variables were out of desired ranges.')";
//			   expressStr = "DECODE( TRUE,23 = 22, 'Variable 1 was 22!',49 = 49, 'Variable 2 was 49!',23 < 23, 'Variable 1 was less than 23.',49 > 30, 'Variable 2 was more than 30.','Variables were out of desired ranges.')";
//			   expressStr = "DECODE( TRUE,25 = 22, 'Variable 1 was 22!',50 = 49, 'Variable 2 was 49!',25 < 23, 'Variable 1 was less than 23.',50 > 30, 'Variable 2 was more than 30.','Variables were out of desired ranges.')";

//			   expressStr = "IIF(to_integer('12') <= 20,'20',IIF(to_integer('12')>20 AND to_integer('12')<30,'30','40'))";
			   
			   //Fri May 01 03:44:07 CST 2021
//			   expressStr = "ADD_TO_DATE('Jan 12 1998 12:00:30AM','MM',1)";
//			   expressStr = "substr(TO_CHAR('2021-05-01 03:44:07:000000','YYYY-MM-DD'),0,4)";
//			   expressStr = "ADD_TO_DATE('2021-05-01 03:44:07:000000','MM',1)";
//			   expressStr = "ADD_TO_DATE('2021-05-01 03:44:07:000000','MON',1)";
//			   expressStr = "ADD_TO_DATE('2021-05-01 03:44:07:000000','MONTH',1)";
			   
//			   expressStr = "ADD_TO_DATE('2021-05-20 03:44:07:000000','D', -10 )";
//			   expressStr = "ADD_TO_DATE('2021-05-20 03:44:07:000000','DD', -10 )";
//			   expressStr = "ADD_TO_DATE('2021-05-20 03:44:07:000000','DDD', -10 )";
//			   expressStr = "ADD_TO_DATE('2021-05-20 03:44:07:000000','DY', -10 )";
//			   expressStr = "ADD_TO_DATE('2021-05-20 03:44:07:000000','DAY', -10 )";
			   
//			   expressStr = "ADD_TO_DATE('2021-05-20 12:44:07:000000','HH', -15 )";
//			   expressStr = "ADD_TO_DATE('2021-05-20 12:44:07:000000','HH12', -15 )";
//			   expressStr = "ADD_TO_DATE('2021-05-20 12:44:07:000000','HH24', -15 )";
			   
//			   expressStr = "DATE_COMPARE( '2021-05-20 12:44:07:000000', '2021-05-21 12:44:07:000000' )";
//			   expressStr = "DATE_COMPARE( '2021-05-22 12:44:07:000000', '2021-05-21 12:44:07:000000' )";
//			   expressStr = "DATE_COMPARE( '2021-05-21 12:44:07:000000', '2021-05-21 12:44:07:000000' )";
//			  
//			   expressStr = "date_diff('2017-05-20 12:44:07:000000','2021-05-20 12:44:07:000000','Y')";
//			   expressStr = "date_diff('2017-05-20 12:44:07:000000','2021-05-20 12:44:07:000000','MM')";
//			   expressStr = "date_diff('2017-05-20 12:44:07:000000','2021-05-20 12:44:07:000000','D')";
//			   
//			   expressStr = "date_diff('2017-05-20 12:44:07:000000','2021-05-20 12:44:07:000000','HH')";
//			   expressStr = "date_diff('2017-05-20 12:44:07:000000','2017-05-20 11:44:07:000000','MI')";
//			   expressStr = "date_diff('2017-05-20 12:44:07:000000','2017-05-20 11:44:07:000000','SS')";
//			   expressStr = "date_diff('2017-05-20 12:44:07:000000','2017-05-20 11:44:07:000000','MS')";
//	   
//			   expressStr = "get_date_part('1997-05-13 12:00:00:000000','HH')";
//			   expressStr = "get_date_part('1997-11-02 2:00:01:000000','HH')";
//			   expressStr = "get_date_part('1997-08-22 12:00:00:000000','HH12')";
//			   expressStr = "get_date_part('1997-06-03 11:30:44:000000','HH12')";
//			   expressStr = "get_date_part('1997-05-13 12:00:00:000000','D')";
//			   expressStr = "get_date_part('1997-06-03 11:30:44:000000','DD')";
//			   expressStr = "get_date_part('1997-08-22 12:00:00:000000','DDD')";
//			   expressStr = "get_date_part(NULL,'DDD')";
//			   expressStr = "get_date_part('1997-03-13 12:00:00:000000','MM')";
//			   expressStr = "get_date_part('1997-06-03 11:30:44:000000','MON')";
//			   expressStr = "get_date_part('1997-03-13 12:00:00:000000','Y')";
//			   expressStr = "get_date_part('1997-06-03 11:30:44:000000','YY')";		
//			   expressStr = "get_date_part('1997-03-13 12:00:00:000000','YYYY')";
//			   expressStr = "get_date_part(null,'YYY')";
//			  
//			   expressStr = "round('1998-01-15 2:10:30:000000','Y')";
//			   expressStr = "round('1998-04-19 1:31:20:000000','Y')";
//			   expressStr = "round('1998-12-20 3:29:55:000000','Y')";
//			   expressStr = "round(null,'Y')";
//			   expressStr = "round('1998-01-15 2:10:30:000000','MM')";
//			   expressStr = "round('1998-04-19 1:31:20:000000','MM')";
//			   expressStr = "round('1998-12-20 3:29:55:000000','MM')";
//			   
//			   expressStr = "round('1998-01-15 2:10:30:000000','D')";
//			   expressStr = "round('1998-04-19 13:31:20:000000','D')";
//			   expressStr = "round('1998-12-20 13:29:55:000000','D')";
//			   expressStr = "round('1998-12-31 11:59:59:000000','D')";
//			   
//			    expressStr = "round('1998-01-15 2:10:30:000000','HH')";
//			   expressStr = "round('1998-04-19 13:31:20:000000','HH')";
//			   expressStr = "round('1998-12-20 3:29:55:000000','HH')";
//			   expressStr = "round('1998-12-31 11:59:59:000000','HH')";
//			   
//			   expressStr = "round('1998-01-15 2:10:30:000000','MI')";
//			   expressStr = "round('1998-04-19 13:31:20:000000','MI')";
//			   expressStr = "round('1998-12-20 3:29:55:000000','MI')";
//			   expressStr = "round('1998-12-31 11:59:59:000000','MI')";
			   
			  
			   
//			   expressStr = "TO_CHAR('1998-12-31 11:59:59:000000')";
			   
//			   expressStr = "TO_CHAR('1998-04-1 12:00:10:000000','MON DD YYYY')";
//			   expressStr = "TO_CHAR('1998-02-22 01:31:10:000000','MON DD YYYY')";
//			   expressStr = "TO_CHAR('1998-10-24 02:12:30:000000','MON DD YYYY')";
//			   expressStr = "TO_CHAR(null,'MON DD YYYY')";
//			   expressStr = "TO_CHAR('1997-04-1 12:00:10:000000','D')";
//			   expressStr = "TO_CHAR('1997-02-22 01:31:10:000000','D')";
//			   expressStr = "TO_CHAR('1997-10-24 02:12:30:000000','D')";
//			   expressStr = "TO_CHAR('2008-12-31 11:59:59:000000')";
//			   expressStr = "TO_CHAR('2008-12-31 11:59:59:000000','DAY')";
			   
//			   expressStr = "TO_CHAR('1997-01-04 12:00:10:000000PM','HH24')";
//			   expressStr = "TO_CHAR('1997-02-22 4:31:10:000000PM','HH24')";
//			   expressStr = "TO_CHAR('1997-02-24 12:12:30:000000AM','HH24')";
			   
//			   expressStr = "TO_CHAR('1999-12-26 11:11:58:000000PM','MM/DD/YY')";
//			   expressStr = "TO_CHAR('1999-12-26 11:11:58:000000PM','MM/DD/RR')"; 
//			   
//			   expressStr = "TO_CHAR('1997-04-01 12:00:10:000000','WW')";
//			   expressStr = "TO_CHAR('1997-02-22 01:31:10:000000','WW')";
//			   expressStr = "TO_CHAR('1997-10-24 02:12:30:000000','WW')";
			   
//			   expressStr = "TO_CHAR('1999-12-31 01:02:03:000000','SSSSS')";
//			   expressStr = "TO_CHAR('1999-9-15 15:59:59:000000','SSSSS')";
			   
			   	
//			   expressStr = "TO_CHAR('1999-12-31 15:59:59:000000','MM/DD/RR')";
//			   expressStr = "TO_CHAR('1996-09-29 03:59:59:000000','MM/DD/RR')";
//			   expressStr = "TO_CHAR('2003-05-17 12:13:14:000000','MM/DD/RR')";
//			   expressStr = "TO_CHAR('2003-09-17 12:13:14:000000','MM/DD/RR')";
//			   expressStr = "TO_CHAR('2003-09-17 12:13:14:000000')";
//			   expressStr = "TO_CHAR('1999-12-31 15:59:59:000000','J')";
			   
//			   expressStr = "TRUNC('1998-01-15 2:10:30:000000','Y')";
//			   expressStr = "TRUNC('1998-04-19 1:31:20:000000','Y')";
//			   expressStr = "TRUNC('1998-01-20 3:50:04:000000','Y')";
//			   expressStr = "TRUNC('1998-12-20 3:29:55:000000','Y')"; 
			   
			
			   
//			   expressStr = "TRUNC('1998-01-15 2:10:30:000000','MM')";
//			   expressStr = "TRUNC('1998-04-19 1:31:20:000000','MM')";
//			   expressStr = "TRUNC('1998-06-20 3:50:04:000000','MM')";
//			   expressStr = "TRUNC('1998-12-31 3:29:55:000000','MM')"; 
//			   expressStr = "TRUNC(null,'MM')";
//			   
//			   expressStr = "TRUNC('1998-01-15 2:10:30:000000','D')";
//			   expressStr = "TRUNC('1998-04-19 1:31:20:000000','D')";
//			   expressStr = "TRUNC('1998-06-20 3:50:04:000000','D')";
//			   expressStr = "TRUNC('1998-12-31 3:29:55:000000','D')";
			   
			   
//			   expressStr = "TRUNC('1998-01-15 2:10:30:000000','HH')";
//			   expressStr = "TRUNC('1998-04-19 1:31:20:000000','HH')";
//			   expressStr = "TRUNC('1998-06-20 3:50:04:000000','HH')";
//			   expressStr = "TRUNC('1998-12-20 3:29:55:000000','HH')"; 
//			   expressStr = "TRUNC('1998-12-31 11:59:59:000000','HH')";
			   
//			   expressStr = "TRUNC('1998-12-31 11:59:59:000000','MI')";
//			   System.out.println("ooooooooooooo");
			   
//			   expressStr = "TO_DATE('01/22/20','MM/DD/YY')";
//			   expressStr = "TO_DATE('1998-04-19 1:31:20:000000','MM/DD/YY')";
//			   expressStr = "TO_DATE('1998-06-20 3:50:04:000000','MM/DD/YY')";
//			   expressStr = "TO_DATE('1998-12-20 3:29:55:000000','MM/DD/YY')"; 
//			   expressStr = "TO_DATE('1998-12-31 11:59:59:000000','MM/DD/YY')";
//			   expressStr = "TO_DATE('1 22 1998 12:21:12','MON DD YYYY HH12:MI:SSAM')";
//			   expressStr = "TO_DATE('2451544','J')";
			   
//			   expressStr = "TO_DATE('04/01/1998 3783','MM/DD/YYYY SSSSS')";
//			   expressStr = "TO_DATE('04/01/8','MM/DD/Y')";
//			   expressStr = "TO_DATE('04/01/8','MM/DD/YYY')";
//			   expressStr = "TO_DATE('04/01/98','MM/DD/RR')";
			   
//			   expressStr = "TO_DATE('04/01/98','MM/DD/RR')";
			   
			   
//			   expressStr = "IIF(REG_MATCH(input, '[a-zA-Z]+'),'1',IIF(REG_MATCH(input, '[\\x{4e00}-\\x{9fa5}]+'),'2','3'))";
			   
//			   expressStr = expressStr.replace("in_name", "'1'");
			   
			   
//			   expressStr = "TO_DATE(1)";
//			   expressStr = "TRUNC('1','1','1')";
//			   expressStr = "encrypt('sadf','MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMeUAJ8b/csd1J7VnunqpYj8WQVVKIlsXszpXR485LylIDuaDZDi/2nlensuanLb6y+6lWiDiQFsQ6LB6LLqqryiF0oMpY3Bk+6U2A6PbaJofN49nOkVwPmk5Nfs9hvi+ojVT7JipxkTjGCD9sSzlZn/9EPd1hjqGkK97xjPk00QIDAQAB')";
//			   expressStr = "decrypt('DC3BbTKW6PcPSXfRPp41NNh9O/G11T0t2GCDN3UlAZuH93ne7gbMHJCMvOJlICQQ6JzfButkHM3Uw7Qh4AAtbOBDLXRwfcWnXaXP5mAS4BaO1Q8ptqAh8Xhuv6mUN0HzS2f/xZqpul+cL5B6qOVxYgfa1/sZEKHP/BK5XwJMGvw=','MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIx5QAnxv9yx3UntWe6eqliPxZBVUoiWxezOldHjzkvKUgO5oNkOL/aeV6ey5qctvrL7qVaIOJAWxDosHosuqqvKIXSgyljcGT7pTYDo9tomh83j2c6RXA+aTk1+z2G+L6iNVPsmKnGROMYIP2xLOVmf/0Q93WGOoaQr3vGM+TTRAgMBAAECgYAEaCliJntmaQ4QK6oOmlutu5TvGj4kmtZKv5BjrUnjhs1b4I7zQEaw7fbCBBLIW98Hrm15kXx0yMIPMSMkKqqf/NgWzezDbB8vI7eiWgiioHMcw6PylO9PwfJD6Feax1kMpmfliR3wiDZE+eQqj/A4L8EF0Nxatt5F0qQeO55aAQJBAO7Lfcwzck7hteR5WnI/PCE6Qt0uxjUune9h6XlnHQh7hh+4EsUSVutVHJvsvYv8Ojm6KmrWqBhvbZMBlzogNTkCQQCWmD/LLiQzPhoW6U8Ytgp2JQys7f5KAwXuaCzpFDTnCE5TJeFHwju/7/cZgPH4qq3YrACv5GlfKakJLuzI0FRZAkAFqnhipvnYW0yojiSSP2Pa2foKK3jyfBetPWJ/ye2LbPrit8hiOqwrS0qxfG2G68Yp5nvpKwyz/7jW2xvOekMpAkEAgZOC4IpVL5oxzKD3I+YO3lVGsM0ezgQH6HjOm8Z9NoUZD8Y6Lwpbau1JCjK/AR2q9BIbjTzVxcIM5g/wQWGwEQJBAKkNlawPrGVKFtVH0XDEAuAf21cCKwpxVfCt+QbOl9BWKxKaL7ly8cf0a0TEUWHrBtLc65/foLZyHfOAxw1NcMQ=')";
			   //			   expressStr = "iif(instr(in_name,'市') = 0,in_name,substr(in_name,0,instr(in_name,'市')))";
//			   expressStr = expressStr.replace("in_put", "'1'");
					   
//			   expressStr = "iif(isnull(in_put),in_put||' ',in_put)";
//			   expressStr = "iif(instr(in_name,'市') = 0,in_name,substr(in_name,0,instr(in_name,'市')))";
//			   expressStr = expressStr.replace("in_put", "'1'");
//			   
			   
		 
			   String expressStr ="key_water_mark('上海市宝山区共江路1-299(单号)','6547535215','1','')";
			   Object str = execExpress(expressStr,et);
			   System.out.println(">>>>>>>>>>>reStr is:"+str);
			   
//			   String str = (String) ExpressionTool.iif(to_integer("12") <= 20,"20",ExpressionTool.iif(to_integer("12")>20 && to_integer("12")<30,"30","40"));
//			   String str = (String) ExpressionTool.iif(true,"20",ExpressionTool.iif(false,"30","40"));
//			   System.out.println(">>>>>>>>>>>reStr is:"+str);
		
//		int idx = "GET".indexOf("GET");
//		System.out.println(idx);
//		String weblog = "\"GET /news/index.html HTTP/1.1\"";
//		replacechr( 0, weblog, "\"", null );

	}

	/**
	 * 执行表达式
	 * @param expressStr
	 * @return
	 */
	public static String execExpress(String expressStr,Object expressClass){
		String reStr = "";
		expressStr = operatorConversion(expressStr);
		//遍历表达式方法 替换成JAVA 方法
		for(int i=0;i<funcLst.size();i++){
			String funcName = funcLst.get(i);
			String reFuncName = funcName;
			if("mod".equalsIgnoreCase(reFuncName)){
				reFuncName = reFuncName+"Java";
			}else if("replacechr".equalsIgnoreCase(reFuncName)){
				reFuncName = "replacechar"+"Java";
			}else if("decompress".equalsIgnoreCase(reFuncName)){
				reFuncName = "decompre"+"Java";
			}else if("add_to_date".equalsIgnoreCase(reFuncName)){
				reFuncName = "add_to_java_date";
			}
			if(expressStr.toLowerCase().indexOf(funcName)>=0){
				expressStr = expressStr.replaceAll("(?i)"+funcName, "et."+reFuncName);
//				expressStr = StringUtils.replace(expressStr, funcName, "et."+reFuncName);
			}
		}
		
		Map<String, Object> map = new HashMap<>();
	    map.put("et", expressClass);
    	Object reObj = executeExpression1(expressStr, map);
    	if(null!=reObj){
    		reStr = reObj.toString();
    	}
    	return reStr;
	}
	
	/**演算法說明
	 * 
	 * @param str 資料
	 * @param patten 要換掉的文字
	 * @param replacement 要改成的文字
	 * @param pos 那段文字在哪
	 * @return 
	 * 1. 從頭開始找出符合的字串，並且標示第幾個字放進pos
	 * 2. 先把在pos前面的字放進結果區(newContent)
	 * 3. 再放進replacement
	 * 4. 再去找下一個字的位置放進pos 
	 * 5. 接續步驟三直到全部找完為止
	 */
	public static String str_replace(String str, String patten,
			String replacement, int pos) {
		int len = str.length();
		int plen = patten.length();
		StringBuilder newContent = new StringBuilder(len);

		int lastPos = 0;

		do {
			newContent.append(str, lastPos, pos);
			newContent.append(replacement);
			lastPos = pos + plen;
			pos = str.indexOf(patten, lastPos);
		} while (pos > 0);
		newContent.append(str, lastPos, len);
		return newContent.toString();
	}
	
	
	public static String watermark(String value,String seed,String start,String end){
		int seedint = 1;
		int startint = 1;
		int endint = value.length();
		if(StringUtils.isNotBlank(seed)){
			seedint = Integer.valueOf(seed);
		}
		if(StringUtils.isNotBlank(start)){
			startint = Integer.valueOf(start);
		}
		if(StringUtils.isNotBlank(end)){
			endint = Integer.valueOf(end);
		}
		if(startint>value.length()){
			return value;
		}
		if(endint>value.length()){
			endint=value.length();
		}
		//汉字正则
		String reg = "^[\u4e00-\u9fa5]+$";
		//数字正则
		Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
		//字母正则
		String regex = "^[a-zA-Z]+$";
		//符号正则
		//String patPunc ="[`~!@#$^&*()+-=|{}':;''\"\"'‘’,\\[\\].<>/?~！@#￥……&*（）——|{}【】 '‘；：”“'。，、？]";
		Pattern patPunc = Pattern.compile("\\pP");
		//1.先根据start和end先做截取
		if(start!=null&&end!=null){
			String prefix =value.substring(0,startint-1);
			String suffix =value.substring(endint);
			//截取后的数据为要做偏移的数据
			value=value.substring(startint-1, endint);
			String newvalue="";
			//将做偏移的数据循环
			for(int i=0;i<value.length();i++) {
				//判断当前字符类型
				String strchar = String.valueOf(value.charAt(i));
				//是汉字
				if(strchar.matches(reg)){
					int intvalue = Integer.valueOf(value.charAt(i)).intValue();
					intvalue=intvalue+seedint;
					while(intvalue>40869){
						intvalue=intvalue-20721;
					}
					newvalue +=((char)intvalue);
				}
				//是数字
				Matcher isNum = pattern.matcher(strchar);
				if(isNum.matches()){
					int intvalue = Integer.valueOf(value.charAt(i)).intValue();
					intvalue=intvalue+seedint;
					while(intvalue>57){
						intvalue=intvalue-10;
					}
					newvalue +=((char)intvalue);
				}
				//是字母
				if(strchar.matches(regex)){
					int intvalue = Integer.valueOf(value.charAt(i)).intValue();
					intvalue=intvalue+seedint;
					while(intvalue>122){
						intvalue=intvalue-26;
					}
					newvalue +=((char)intvalue);
				}
				//是符号
				Matcher fuhao = patPunc.matcher(strchar);
				if(fuhao.matches()){
					newvalue+=strchar;
				}
			}
			return prefix+newvalue+suffix;
		}else if(start!=null){
			String prefix =value.substring(0,startint-1);
			//截取后的数据为要做偏移的数据
			value=value.substring(startint-1);	
			String newvalue="";
			//将做偏移的数据循环
			for(int i=0;i<value.length();i++) {
				//判断当前字符类型
				String strchar = String.valueOf(value.charAt(i));
				//是汉字
				if(strchar.matches(reg)){
					int intvalue = Integer.valueOf(value.charAt(i)).intValue();
					intvalue=intvalue+seedint;
					if(intvalue>40869){
						intvalue=intvalue-40869+19968;
					}
					newvalue +=((char)intvalue);
				}
				//是数字
				Matcher isNum = pattern.matcher(strchar);
				if(isNum.matches()){
					int intvalue = Integer.valueOf(value.charAt(i)).intValue();
					intvalue=intvalue+seedint;
					while(intvalue>57){
						intvalue=intvalue-10;
					}
					newvalue +=((char)intvalue);
				}
				//是字母
				if(strchar.matches(regex)){
					int intvalue = Integer.valueOf(value.charAt(i)).intValue();
					intvalue=intvalue+seedint;
					while(intvalue>122){
						intvalue=intvalue-26;
					}
					newvalue +=((char)intvalue);
				}
				//是符号
				Matcher fuhao = patPunc.matcher(strchar);
				if(fuhao.matches()){
					newvalue+=strchar;
				}
			}
			return prefix+newvalue;
		}else if(end!=null){
			String suffix =value.substring(endint);
			//截取后的数据为要做偏移的数据
			value=value.substring(0,endint);
			String newvalue="";
			//将做偏移的数据循环
			for(int i=0;i<value.length();i++) {
				//判断当前字符类型
				String strchar = String.valueOf(value.charAt(i));
				//是汉字
				if(strchar.matches(reg)){
					int intvalue = Integer.valueOf(value.charAt(i)).intValue();
					intvalue=intvalue+seedint;
					if(intvalue>40869){
						intvalue=intvalue-40869+19968;
					}
					newvalue +=((char)intvalue);
				}
				//是数字
				Matcher isNum = pattern.matcher(strchar);
				if(isNum.matches()){
					int intvalue = Integer.valueOf(value.charAt(i)).intValue();
					intvalue=intvalue+seedint;
					while(intvalue>57){
						intvalue=intvalue-10;
					}
					newvalue +=((char)intvalue);
				}
				//是字母
				if(strchar.matches(regex)){
					int intvalue = Integer.valueOf(value.charAt(i)).intValue();
					intvalue=intvalue+seedint;
					while(intvalue>122){
						intvalue=intvalue-26;
					}
					newvalue +=((char)intvalue);
				}
				//是符号
				Matcher fuhao = patPunc.matcher(strchar);
				if(fuhao.matches()){
					newvalue+=strchar;
				}
			}
			return newvalue+suffix;
		}else{
			String newvalue="";
			for(int i=0;i<value.length();i++) {
				//判断当前字符类型
				String strchar = String.valueOf(value.charAt(i));
				//是汉字
				if(strchar.matches(reg)){
					int intvalue = Integer.valueOf(value.charAt(i)).intValue();
					intvalue=intvalue+seedint;
					if(intvalue>40869){
						intvalue=intvalue-40869+19968;
					}
					newvalue +=((char)intvalue);
				}
				//是数字
				Matcher isNum = pattern.matcher(strchar);
				if(isNum.matches()){
					int intvalue = Integer.valueOf(value.charAt(i)).intValue();
					intvalue=intvalue+seedint;
					while(intvalue>57){
						intvalue=intvalue-10;
					}
					newvalue +=((char)intvalue);
				}
				//是字母
				if(strchar.matches(regex)){
					int intvalue = Integer.valueOf(value.charAt(i)).intValue();
					intvalue=intvalue+seedint;
					while(intvalue>122){
						intvalue=intvalue-26;
					}
					newvalue +=((char)intvalue);
				}
				//是符号
				Matcher fuhao = patPunc.matcher(strchar);
				if(fuhao.matches()){
					newvalue+=strchar;
				}
			}
			return newvalue;
		}
	}
	
	
	/**
	 * 密钥水印(通过把密钥的值放入数据中进行水印)
	 * @author：
	 * @methodsName：@param value
	 * @methodsName：@param key
	 * @methodsName：@param start
	 * @methodsName：@param end
	 * @methodsName：@return
	 * @date：2020年7月27日
	 */
	public static Object key_water_mark(Object value,String key,String start,String end){
		if(StringUtils.isBlank(value.toString())){
			return value;
		}
		int startint = 1;
		int endint = value.toString().length();
		String substrval="";
		String newsubstr="";
		//随机数的值
		int randomnumval = 0;
		if(StringUtils.isBlank(key)||endint <=0 ){
			return (Object)value;
		}
		if(StringUtils.isNotBlank(end)){
			if(Integer.valueOf(start)>=Integer.valueOf(end)){
				return (Object)value;
			}
		}
		//汉字正则
		String reg = "^[\u4e00-\u9fa5]+$";
		//数字正则
		Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
		//字母正则
		String regex = "^[a-zA-Z]+$";
		//符号正则
		//String patPunc ="[`~!@#$^&*()+-=|{}':;''\"\"'‘’,\\[\\].<>/?~！@#￥……&*（）——|{}【】 '‘；：”“'。，、？]";
		Pattern patPunc = Pattern.compile("\\pP");
		if(!StringUtils.isNotBlank(start)||Integer.valueOf(start)<=0){
			startint=0; 
		}else{
			startint = Integer.valueOf(start)-1;
		}
		if(!StringUtils.isNotBlank(end)||Integer.valueOf(end) > endint){
			endint = value.toString().length();
		}else{
			endint = Integer.valueOf(end);
		}
		//截取不需要加密的值（前缀）
		String prefixsubstr = value.toString().substring(0,startint);
		//截取不需要加密的值（后缀）
		String suffixsubstr = value.toString().substring(endint);
		//获取需要加密的值
		substrval = value.toString().substring(startint, endint);
		//判断加密的值长度。如果少于2个 没有办法进行改方式的水印
		if(substrval.length() < 2){
			return (Object)value; 
		}
		//循环每个字符 进行水印处理
		for (int i = 0; i < substrval.length(); i++) {
			//判断当前字符类型
			String strchar = String.valueOf(substrval.charAt(i));
			//如果字符是数字类型
			Matcher isNum = pattern.matcher(strchar);
			if(i==0){
				if(randomnum==null){
					//创建生成随机数实例
					randomnum =new RandomnumTool(0, key.length());
				}
				//获得随机值
				randomnumval = randomnum.randomnum();
			}
			if(isNum.matches()){
				if(i==0){
					newsubstr += String.valueOf(randomnumval);
				}else{
					String beforestr = String.valueOf(newsubstr.charAt(i-1));
					Matcher beforeIsNum = pattern.matcher(beforestr);
					//如果前一个是数字类型
					if(beforeIsNum.matches()){
						newsubstr += String.valueOf(key.charAt(Integer.valueOf(beforestr)));
					}else{//前一个是字母或者汉字
						String strToBinstr = BinaryConversion.StrToBinstr(beforestr.charAt(0));
						strToBinstr = strToBinstr.substring(strToBinstr.length()-4);
						String index = BinaryConversion.binaryToDecimal(strToBinstr);
						newsubstr += String.valueOf(key.charAt(Integer.valueOf(index)));
					}
				}
			}else{
			//如果是字母或者是汉字
//			if(strchar.matches(regex)){
				if(i==0){
					String strToBinstr = BinaryConversion.StrToBinstr(strchar.charAt(0));
					//二进制0到倒数后4位
					String beforesub =  strToBinstr.substring(0,strToBinstr.length()-4);
					//二进制的后四位字符串
//					String affersub = strToBinstr.substring(strToBinstr.length()-4);
//					String index = BinaryConversion.binaryToDecimal(affersub);
//					String replacestr = String.valueOf(key.charAt(Integer.valueOf(index)));
					String replacestr=BinaryConversion.decimalToBinary(randomnumval);
					newsubstr +=BinaryConversion.BinstrToStr(beforesub+replacestr);
				}else{
					String beforestr = String.valueOf(newsubstr.charAt(i-1));
					Matcher beforeIsNum = pattern.matcher(beforestr);
					//如果前一个是数字类型
					if(beforeIsNum.matches()){
						String strToBinstr = BinaryConversion.StrToBinstr(strchar.charAt(0));
						//二进制0到倒数后4位
						String beforesub =  strToBinstr = strToBinstr.substring(0,strToBinstr.length()-4);
						//二进制的后四位字符串
						String affersub = strToBinstr.substring(strToBinstr.length()-4);
//						String index = BinaryConversion.binaryToDecimal(affersub);
						String replacestr = BinaryConversion.decimalToBinary(Integer.valueOf(String.valueOf(key.charAt(Integer.valueOf(beforestr)))));
						newsubstr +=BinaryConversion.BinstrToStr(beforesub+replacestr);
						
						
					}else{//前一个是字母或者汉字
						String bestrToBinstr = BinaryConversion.StrToBinstr(beforestr.charAt(0));
						bestrToBinstr = bestrToBinstr.substring(bestrToBinstr.length()-4);
						String beindex = BinaryConversion.binaryToDecimal(bestrToBinstr);
//						newsubstr += String.valueOf(key.charAt(Integer.valueOf(index)));
						String strToBinstr = BinaryConversion.StrToBinstr(strchar.charAt(0));
						//二进制0到倒数后4位
						String beforesub =  strToBinstr = strToBinstr.substring(0,strToBinstr.length()-4);
						//二进制的后四位字符串
						String affersub = strToBinstr.substring(strToBinstr.length()-4);
//						String index = BinaryConversion.binaryToDecimal(affersub);
						String replacestr = BinaryConversion.decimalToBinary(Integer.valueOf(String.valueOf(key.charAt(Integer.valueOf(beindex)))));
						newsubstr +=BinaryConversion.BinstrToStr(beforesub+replacestr);
					}
				}
			}
		}
		newsubstr = prefixsubstr+newsubstr+suffixsubstr;
		return newsubstr;
	}
	
	
	
	/**
	 * 密钥水印(通过把密钥的值放入数据中进行水印)
	 * @author：
	 * @methodsName：@param value
	 * @methodsName：@param key
	 * @methodsName：@param start
	 * @methodsName：@param end
	 * @methodsName：@return
	 * @date：2020年7月27日
	 */
	public static Object key_digital_water_mark(Object value,String key,String start,String end){
		if(StringUtils.isBlank(value.toString())){
			return value;
		}
		int startint = 1;
		int endint = value.toString().length();
		String substrval="";
		String newsubstr="";
		//随机数的值
		int randomnumval = 0;
		if(StringUtils.isBlank(key)||endint <=0 ){
			return (Object)value;
		}
		if(StringUtils.isNotBlank(end)){
			if(Integer.valueOf(start)>=Integer.valueOf(end)){
				return (Object)value;
			}
		}
		//字符串是否是全数字正则
		Pattern pattern = Pattern.compile("^[0-9]*$");
		
		if(!StringUtils.isNotBlank(start)||Integer.valueOf(start)<=0){
			startint=0; 
		}else{
			startint = Integer.valueOf(start)-1;
		}
		if(!StringUtils.isNotBlank(end)||Integer.valueOf(end) > endint){
			endint = value.toString().length();
		}else{
			endint = Integer.valueOf(end);
		}
		//截取不需要加密的值（前缀）
		String prefixsubstr = value.toString().substring(0,startint);
		//截取不需要加密的值（后缀）
		String suffixsubstr = value.toString().substring(endint);
		//获取需要加密的值
		substrval = value.toString().substring(startint, endint);
		//判断加密的值长度。如果少于2个 没有办法进行改方式的水印
		if(substrval.length() < 2){
			return (Object)value; 
		}
		//如果字符串是数字类型
		Matcher isNum = pattern.matcher(substrval);
		if(isNum.matches()){
			//循环每个字符 进行水印处理
			for (int i = 0; i < substrval.length(); i++) {
				//判断当前字符类型
				String strchar = String.valueOf(substrval.charAt(i));
				if(i==0){
					if(randomnum==null){
						//创建生成随机数实例
						randomnum =new RandomnumTool(0, key.length());
					}
					//获得随机值
					randomnumval = randomnum.randomnum();
				}
				if(i==0){
					newsubstr += String.valueOf(randomnumval);
				}else{
					String beforestr = String.valueOf(newsubstr.charAt(i-1));
					newsubstr += String.valueOf(key.charAt(Integer.valueOf(beforestr)));
				}
			}
			newsubstr = prefixsubstr+newsubstr+suffixsubstr;
		}else{
			return value;
		}
		return newsubstr;
	}
	
}

