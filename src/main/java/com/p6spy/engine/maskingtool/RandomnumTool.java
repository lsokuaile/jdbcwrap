/**
* Copyright © 2017 . All rights reserved.
*
* @Title: RandomnumTool.java
* @Prject: ghca-platform-desensitization-engine
* @Package: com.ghca.dataplatform.tools.string
* @Description: TODO
* @author:  
* @date: 2020年7月27日
* @version: 
*/
package com.p6spy.engine.maskingtool;

import java.util.Random;

/**
 * @className: RandomnumTool.java
 * @explain:
 * @author:  
 * @date: 2020年7月27日
 * @version: 
 */
public class RandomnumTool {
	
	int count = 0;
	int start = 0;
	int end = 0;
	boolean[] bool;
	
	public RandomnumTool() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RandomnumTool(int start,int end){
		this.start=start;
		this.end = end;
		this.bool = new boolean[end];
	}

	/**
     * 生成start to end之前的随机数
     * @author：
     * @methodsName：@param start
     * @methodsName：@param end
     * @methodsName：@param num
     * @methodsName：@return
     * @date：2020年7月27日
     */
    public int randomnum(){
    	if(end-start ==count){
    		bool = new boolean[end];
    		count=0;
    	}
    	Random rand = new Random();
    	int randInt=0;
		do {
			randInt = rand.nextInt(end)%(end-start+1) + start;
		}while(bool[randInt]);
		bool[randInt] = true;
		count++;
		return randInt;
    	
    }
    
	public static void main(String[] args){
		RandomnumTool rand = new RandomnumTool(0, 10);
		rand.randomnum();
		rand.randomnum();
		rand.randomnum();
		rand.randomnum();
		rand.randomnum();
		rand.randomnum();
		rand.randomnum();
		rand.randomnum();
		rand.randomnum();
		rand.randomnum();
		rand.randomnum();
		rand.randomnum();

	}
}
