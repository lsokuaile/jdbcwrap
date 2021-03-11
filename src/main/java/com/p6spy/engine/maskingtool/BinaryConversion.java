/**
* Copyright © 2017 . All rights reserved.
*
* @Title: BinaryConversion.java
* @Prject: ghca-platform-desensitization-engine
* @Package: com.ghca.dataplatform.tools.string
* @Description: TODO
* @author:  
* @date: 2020年7月27日
* @version: 
*/
package com.p6spy.engine.maskingtool;


/**
 * 进制转换类
 * @className: BinaryConversion.java
 * @explain:
 * @author:  
 * @date: 2020年7月27日
 * @version: 
 */
public class BinaryConversion {
	  /**
	   * 字符转成二进制
	 * @author：
	 * @methodsName：@param strChar
	 * @methodsName：@return
	 * @date：2020年7月27日
	 */
	public static String StrToBinstr(char strChar) { 
	        String result = ""; 
            result = Integer.toBinaryString(strChar); 
            int length = result.length();
            length = length%4 == 0 ? (length/4) : (length/4)+1;
            while(result.length() <length*4){
            	result="0"+result;
            }
//            result = String.format("%0"+length*4+"s", result); 
	        return result; 
	    } 
	  
	
	
	/**
	 * 数字转成二进制
	 * @author：
	 * @methodsName：@param n
	 * @methodsName：@return
	 * @date：2020年7月27日
	 */
	public static String decimalToBinary(int n){
	      int t = 0;  //用来记录位数
	      int bin = 0; //用来记录最后的二进制数
	      int r = 0;  //用来存储余数
	      while(n != 0){
	          r = n % 2;
	          n = n / 2;
	          bin += r * Math.pow(10,t);
	          t++;
	     }
	     String str = String.format("%04d", bin);     
	     return str;
	 }
	
	
	

	public static  String binaryToDecimal(String n) {
			String res = Integer.valueOf(n,2).toString();
//	        String res = Integer.toBinaryString(Integer.valueOf(n));
	 
	        // 将字符串转换为数字进行打印，但不建议这么做，当为负数时，int型的表示不了32的一个整数，另外，int的位数有大小，打印的值超出范围就会报错
//	        int in = Integer.parseInt(res);
//	        System.out.print(res + "\n");
	        return res;
	}
	
	// 将二进制字符串转换成Unicode字符串 
    public static  String BinstrToStr(String binStr) { 
        char[] tempChar = new char[1]; 
        tempChar[0] = BinstrToChar(binStr); 
        String s = new String(tempChar);
        return s; 
    }
	
    private static char BinstrToChar(String tempStr) { 
        int[] temp = BinstrToIntArray(tempStr); 
        int sum = 0; 
        for (int i = 0; i < temp.length; i++) { 
            sum += temp[temp.length - 1 - i] << i; 
        } 
        return (char) sum; 
    } 
    
    
    // 将二进制字符串转换成int数组 
    private static int[] BinstrToIntArray(String binStr) { 
        char[] temp = binStr.toCharArray(); 
        int[] result = new int[temp.length]; 
        for (int i = 0; i < temp.length; i++) { 
            result[i] = temp[i] - 48; 
        } 
        return result; 
    } 
	
	
	  public static void main(String[] args) {
		  String strToBinstr = StrToBinstr('昆');
//		  binaryToDecimal(1);
		  String binstrToStr = BinstrToStr("1000110111101001");
//		  String strToBinstr = decimalToBinary(1);
		  System.out.println(binstrToStr);
	}
}
