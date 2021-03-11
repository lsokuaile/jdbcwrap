package com.p6spy.engine.maskingtool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;  

/** 
 * ZLib压缩工具 
 *  
 * @author <a href="mailto:zlex.dongliang@gmail.com">梁栋</a> 
 * @version 1.0 
 * @since 1.0 
 */  
public class ZLibUtils {
	
	
	/** 
     * 压缩 
     *  
     * @param data 待压缩的数据 
     * @return String 压缩后的数据 (输入值的已解压缩二进制值。
     */  
    public static String compressStr(String data) {  
    	byte[] dataBytesArr = data.getBytes();
    	byte[]  reDataBytesArr = compress(dataBytesArr);
	    String str= byteArrToBinStr(reDataBytesArr);
        return str;  
    }
    
    /** 
     * 解压缩 
     *  
     * @param data 待解压缩的数据 
     * @return byte[] 解压缩后的数据 
     */  
    public static String decompressStr(String data) {
    	byte[] dataBytesArr = binStrToByteArr(data);
		byte[] reDataBytesArr = decompress(dataBytesArr);
		String str= new String(reDataBytesArr);
        return str;  
    }
    
    
    
    
    
    /**
     * byte数组转换为二进制字符串,每个字节以","隔开
     **/
    public static String byteArrToBinStr(byte[] b) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
//            result.append(Long.toString(b[i] & 0xff, 2) + ",");
        	String bit = Long.toString(b[i] & 0xff, 2);
        	int len = 8-bit.length();
        	//不足8位的补零
        	for(int j=0;j<len;j++){
        		bit = "0"+bit;
        	}
            result.append(bit );
        }
        return result.toString();
    }

    /**
     * 二进制字符串转换为byte数组,每个字节以8位隔开
     **/
    public static byte[] binStrToByteArr(String binStr) {
//        String[] temp = binStr.split(",");
        int len = binStr.length()/8;
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[i] = Long.valueOf(binStr.substring(i*8,(i+1)*8), 2).byteValue();
        }
        return b;
    }
    
    /**
     * 二进制字符串转换为byte数组,每个字节以“,”隔开
     **/
    public static byte[] binStrToByteArr11(String binStr) {
        String[] temp = binStr.split(",");
        byte[] b = new byte[temp.length];
        for (int i = 0; i < b.length; i++) {
            b[i] = Long.valueOf(temp[i], 2).byteValue();
        }
        return b;
    }
    
    public static void main(String[] args) {
    	decompressStr(compressStr("abc") );
//    	String str= new String("abc".getBytes());
//    	"abc".getBytes();
	}
    
	/** 
     * 压缩 
     *  
     * @param data 
     *            待压缩数据 
     * @return byte[] 压缩后的数据 
     */  
    public static byte[] compress(byte[] data) {  
        byte[] output = new byte[0];  
  
        Deflater compresser = new Deflater();  
  
        compresser.reset();  
        compresser.setInput(data);  
        compresser.finish();  
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);  
        try {  
            byte[] buf = new byte[1024];  
            while (!compresser.finished()) {  
                int i = compresser.deflate(buf);  
                bos.write(buf, 0, i);  
            }  
            output = bos.toByteArray();  
        } catch (Exception e) {  
            output = data;  
            e.printStackTrace();  
        } finally {  
            try {  
                bos.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        compresser.end();  
        return output;  
    }  
  
    /** 
     * 压缩 
     *  
     * @param data 
     *            待压缩数据 
     *  
     * @param os 
     *            输出流 
     */  
    public static void compress(byte[] data, OutputStream os) {  
        DeflaterOutputStream dos = new DeflaterOutputStream(os);  
  
        try {  
            dos.write(data, 0, data.length);  
  
            dos.finish();  
  
            dos.flush();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * 解压缩 
     *  
     * @param data 
     *            待压缩的数据 
     * @return byte[] 解压缩后的数据 
     */  
    public static byte[] decompress(byte[] data) {  
        byte[] output = new byte[0];  
  
        Inflater decompresser = new Inflater();  
        decompresser.reset();  
        decompresser.setInput(data);  
  
        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);  
        try {  
            byte[] buf = new byte[1024];  
            while (!decompresser.finished()) {  
                int i = decompresser.inflate(buf);  
                o.write(buf, 0, i);  
            }  
            output = o.toByteArray();  
        } catch (Exception e) {  
            output = data;  
            e.printStackTrace();  
        } finally {  
            try {  
                o.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
  
        decompresser.end();  
        return output;  
    }  
  
    /** 
     * 解压缩 
     *  
     * @param is 
     *            输入流 
     * @return byte[] 解压缩后的数据 
     */  
    public static byte[] decompress(InputStream is) {  
        InflaterInputStream iis = new InflaterInputStream(is);  
        ByteArrayOutputStream o = new ByteArrayOutputStream(1024);  
        try {  
            int i = 1024;  
            byte[] buf = new byte[i];  
  
            while ((i = iis.read(buf, 0, i)) > 0) {  
                o.write(buf, 0, i);  
            }  
  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return o.toByteArray();  
    }  
    
    
    
    
    /**
     * 将二进制转换成字符
     * @param binStr
     * @return
     */
    public static String BinstrToStr(String binStr){
        char[] tempChar=new char[binStr.length()/8];
        int n=0;
        for(int i=0; i<binStr.length();i+=8){
            String bit = binStr.substring(i, i+8);
            tempChar[n] = BinstrToChar(bit);
            n++;
        }
        String bitStr = String.valueOf(tempChar);
        return bitStr;
   }

    //将二进制转换成字符
    public static char BinstrToChar(String binStr){
        int[] temp=BinstrToIntArray(binStr);
        int sum=0;
        for(int i=0; i<temp.length;i++){
            sum +=temp[temp.length-1-i]<<i;
        }   
        return (char)sum;
   }
    
    /**
     * 将二进制字符串转换成int数组
     * @param binStr
     * @return
     */
    public static int[] BinstrToIntArray(String binStr) {       
        char[] temp=binStr.toCharArray();
        int[] result=new int[temp.length];   
        for(int i=0;i<temp.length;i++) {
            result[i]=temp[i]-48;
        }
        return result;
    }
}
