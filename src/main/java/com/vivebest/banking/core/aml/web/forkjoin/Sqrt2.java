package com.vivebest.banking.core.aml.web.forkjoin;

public class Sqrt2 {

	    public static void main(String[] args) {    
	        //手写二分估值
	        System.out.println(myCalculate(1.4,1.5));
	        //调用函数输出
	        System.out.println("----------sqrt= "+Math.sqrt(2.0));
	    }
	    //根号2约等于 1.414
	    private static double myCalculate(double low,double high) {
	        double mid = 0;
	        // 二分法,结束条件：差值小于等于1e-10即可
	        while(high-low>1e-10){
	            
	             mid = (high+low)/2.0;
	            System.out.println("-----------mid= "+mid+"  mid*mid= "+mid*mid);
	            //二分，逐步向中间值收拢
	            if(mid*mid <= 2.0){
	                low=mid;
	            }
	            else{
	                high=mid;
	            }
	            
	        }
	        
	        return mid;
	    }
	    
}
