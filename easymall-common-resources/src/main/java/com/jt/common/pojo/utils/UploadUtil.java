package com.jt.common.pojo.utils;



public class UploadUtil {
	
	public static String getUploadPath(String fileName,String upload){
		
		//根据文件名称,生成hash字符串,截取前8位
		//1k2k2k3l,1/k/2/k/2/k/3/l
		String hash = Integer.toHexString(fileName.hashCode());
		while(hash.length()<8){
			hash += "0";
		}
		for (int i = 0; i < hash.length(); i++) {
			upload += "/"+hash.charAt(i);
		}
		
		
		return upload;///upload/3/d/2/2/d/g/h/j
	}
}
