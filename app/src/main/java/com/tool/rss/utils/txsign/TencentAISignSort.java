package com.tool.rss.utils.txsign;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 签名生成算法方法类
 * 更新时间:2017年12月4日14:33:20
 * 不再需要自己排序了。直接使用TreeMap进行参数排序
 * @author 小帅丶
 *
 */
public class TencentAISignSort {
	/**
	 * SIGN签名生成算法-JAVA版本
	 * @param HashMap<String,String> params 请求参数集，所有参数必须已转换为字符串类型
	 * @return 签名
	 * @throws IOException
	 */
	public static String getSignature(HashMap<String,String> params) throws IOException {
	        // 先将参数以其参数名的字典序升序进行排序
	        Map<String, String> sortedParams = new TreeMap<>(params);
	        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();
	        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
	        StringBuilder baseString = new StringBuilder();
	        for (Map.Entry<String, String> param : entrys) {
	            //sign参数 和 空值参数 不加入算法
	            if(param.getValue()!=null && !"".equals(param.getKey().trim()) && !"sign".equals(param.getKey().trim()) && !"".equals(param.getValue().trim())) {
	                baseString.append(param.getKey().trim()).append("=").append(URLEncoder.encode(param.getValue().trim(),"UTF-8")).append("&");
	            }
	        }
	        if(baseString.length() > 0 ) {
	            baseString.deleteCharAt(baseString.length()-1).append("&app_key="+TencentAPI.APP_KEY_AI);
	        }
	        System.out.println("加密的参数字符串："+baseString.toString());
	        // 使用MD5对待签名串求签
	        try {
	        	String sign = MD5.getMD5(baseString.toString());
	        	return sign;
	        } catch (Exception ex) {
	            throw new IOException(ex);
	        }
	    }
	/**
	 * SIGN签名生成算法针对文本进行翻译的处理-JAVA版本
	 * @param HashMap<String,String> params 请求参数集，所有参数必须已转换为字符串类型
	 * @return 签名
	 * @remark 针对于输入的源语言和目标语言进行了判断
	 * @throws Exception 
	 */
	public static String getSignature4TransText(HashMap<String,String> params) throws Exception {
	        // 先将参数以其参数名的字典序升序进行排序
	        Map<String, String> sortedParams = new TreeMap<>(params);
	        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();
	        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
	        StringBuilder baseString = new StringBuilder();
        for (Map.Entry<String, String> param : entrys) {
        	if("source".equals(param.getKey().trim())&&"en".equals(param.getValue().trim())){
        		for (Map.Entry<String, String> targetParam : entrys) {
        		if("target".equals(targetParam.getKey().trim())){
        			if(!Arrays.asList(TransConstant.EN_TARGET).contains(targetParam.getValue().trim())){
        				throw new Exception("en源语言不支持目标语言:"+targetParam.getValue());
	        			}
	        		}
	        		}
	        	}
        	if("source".equals(param.getKey().trim())&&"zh".equals(param.getValue().trim())){
        		for (Map.Entry<String, String> targetParam : entrys) {
        		if("target".equals(targetParam.getKey().trim())){
        			if(!Arrays.asList(TransConstant.ZH_TARGET).contains(targetParam.getValue().trim())){
        				throw new Exception("zh源语言不支持目标语言:"+targetParam.getValue());
	        			}
	        		}
	        	}
	        }
        	if("source".equals(param.getKey().trim())&&"fr".equals(param.getValue().trim())){
        		for (Map.Entry<String, String> targetParam : entrys) {
        		if("target".equals(targetParam.getKey().trim())){
        			if(!Arrays.asList(TransConstant.FR_TARGET).contains(targetParam.getValue().trim())){
        				throw new Exception("fr源语言不支持目标语言:"+targetParam.getValue());
	        			}
	        		}
	        	}
	        }
        	if("source".equals(param.getKey().trim())&&"es".equals(param.getValue().trim())){
        		for (Map.Entry<String, String> targetParam : entrys) {
        		if("target".equals(targetParam.getKey().trim())){
        			if(!Arrays.asList(TransConstant.ES_TARGET).contains(targetParam.getValue().trim())){
        				throw new Exception("es源语言不支持目标语言:"+targetParam.getValue());
	        			}
	        		}
	        	}
	        }
        	if("source".equals(param.getKey().trim())&&"it".equals(param.getValue().trim())){
        		for (Map.Entry<String, String> targetParam : entrys) {
        		if("target".equals(param.getKey().trim())){
        			if(!Arrays.asList(TransConstant.IT_TARGET).contains(targetParam.getValue().trim())){
        				throw new Exception("it源语言不支持目标语言:"+targetParam.getValue());
	        			}
	        		}
	        	}
	        }
        	if("source".equals(param.getKey().trim())&&"de".equals(param.getValue().trim())){
        		for (Map.Entry<String, String> targetParam : entrys) {
        		if("target".equals(param.getKey().trim())){
        			if(!Arrays.asList(TransConstant.DE_TARGET).contains(targetParam.getValue().trim())){
        				throw new Exception("de源语言不支持目标语言:"+targetParam.getValue());
	        			}
	        		}
	        	}
	        }
        	if("source".equals(param.getKey().trim())&&"tr".equals(param.getValue().trim())){
        		for (Map.Entry<String, String> targetParam : entrys) {
        		if("target".equals(param.getKey().trim())){
        			if(!Arrays.asList(TransConstant.TR_TARGET).contains(targetParam.getValue().trim())){
        				throw new Exception("tr源语言不支持目标语言:"+targetParam.getValue());
	        			}
	        		}
	        	}
	        }
        	if("source".equals(param.getKey().trim())&&"ru".equals(param.getValue().trim())){
        		for (Map.Entry<String, String> targetParam : entrys) {
        		if("target".equals(param.getKey().trim())){
        			if(!Arrays.asList(TransConstant.RU_TARGET).contains(targetParam.getValue().trim())){
        				throw new Exception("ru源语言不支持目标语言:"+targetParam.getValue());
	        			}
	        		}
	        	}
	        }
        	if("source".equals(param.getKey().trim())&&"pt".equals(param.getValue().trim())){
        		for (Map.Entry<String, String> targetParam : entrys) {
        		if("target".equals(param.getKey().trim())){
        			if(!Arrays.asList(TransConstant.PT_TARGET).contains(targetParam.getValue().trim())){
        				throw new Exception("pt源语言不支持目标语言:"+targetParam.getValue());
	        			}
	        		}
	        	}
	        }
        	if("source".equals(param.getKey().trim())&&"vi".equals(param.getValue().trim())){
        		for (Map.Entry<String, String> targetParam : entrys) {
        		if("target".equals(param.getKey().trim())){
        			if(!Arrays.asList(TransConstant.VI_TARGET).contains(targetParam.getValue().trim())){
        				throw new Exception("vi源语言不支持目标语言:"+param.getValue());
	        			}
	        		}
	        	}
	        }
        	if("source".equals(param.getKey().trim())&&"id".equals(param.getValue().trim())){
        		for (Map.Entry<String, String> targetParam : entrys) {
        		if("target".equals(param.getKey().trim())){
        			if(!Arrays.asList(TransConstant.ID_TARGET).contains(targetParam.getValue().trim())){
        				throw new Exception("id源语言不支持目标语言:"+param.getValue());
	        			}
	        		}
	        	}
	        }
        	if("source".equals(param.getKey().trim())&&"ms".equals(param.getValue().trim())){
        		for (Map.Entry<String, String> targetParam : entrys) {
        		if("target".equals(param.getKey().trim())){
        			if(!Arrays.asList(TransConstant.MS_TARGET).contains(targetParam.getValue().trim())){
        				throw new Exception("ms源语言不支持目标语言:"+param.getValue());
	        			}
	        		}
	        	}
	        }
        	if("source".equals(param.getKey().trim())&&"th".equals(param.getValue().trim())){
        		for (Map.Entry<String, String> targetParam : entrys) {
        		if("target".equals(param.getKey().trim())){
        			if(!Arrays.asList(TransConstant.TH_TARGET).contains(targetParam.getValue().trim())){
        				throw new Exception("th源语言不支持目标语言:"+param.getValue());
	        			}
	        		}
	        	}
	        }
        	if("source".equals(param.getKey().trim())&&"jp".equals(param.getValue().trim())){
        		for (Map.Entry<String, String> targetParam : entrys) {
        		if("target".equals(param.getKey().trim())){
        			if(!Arrays.asList(TransConstant.JP_TARGET).contains(targetParam.getValue().trim())){
        				throw new Exception("jp源语言不支持目标语言:"+param.getValue());
	        			}
	        		}
	        	}
	        }
        	if("source".equals(param.getKey().trim())&&"kr".equals(param.getValue().trim())){
        		for (Map.Entry<String, String> targetParam : entrys) {
        		if("target".equals(param.getKey().trim())){
        			if(!Arrays.asList(TransConstant.KR_TARGET).contains(targetParam.getValue().trim())){
        				throw new Exception("kr源语言不支持目标语言:"+targetParam.getValue());
	        			}
	        		}
	        	}
	        }
			if ("source".equals(param.getKey().trim())&& "auto".equals(param.getValue().trim())) {
				for (Map.Entry<String, String> targetParam : entrys) {
					if ("target".equals(targetParam.getKey().trim())) {
						if (!Arrays.asList(TransConstant.AUTO_TARGET).contains(targetParam.getValue().trim())) {
							throw new Exception("auto源语言不支持目标语言:"+ targetParam.getValue());
						}
					}
				}
			}
            //sign参数 和 空值参数 不加入算法
            if(param.getValue()!=null && !"".equals(param.getKey().trim()) && !"sign".equals(param.getKey().trim()) && !"".equals(param.getValue().trim())) {
                baseString.append(param.getKey().trim()).append("=").append(URLEncoder.encode(param.getValue().trim(),"UTF-8")).append("&");
            	}
	        }
	        if(baseString.length() > 0 ) {
	            baseString.deleteCharAt(baseString.length()-1).append("&app_key="+TencentAPI.APP_KEY_AI);
	        }
	        System.out.println("加密的参数字符串："+baseString.toString());
	        // 使用MD5对待签名串求签
	        try {
	        	String sign = MD5.getMD5(baseString.toString());
	        	return sign;
	        } catch (Exception ex) {
	            throw new IOException(ex);
	        }
	    }
	/**
	 * SIGN签名生成算法-JAVA版本
	 * @param HashMap<String,String> params 请求参数集，所有参数必须已转换为字符串类型
	 * @return 签名
	 * @throws IOException
	 */
	public static String getSignatureforNLP(HashMap<String,String> params) throws IOException {
	        // 先将参数以其参数名的字典序升序进行排序
	        Map<String, String> sortedParams = new TreeMap<>(params);
	        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();
	        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
	        StringBuilder baseString = new StringBuilder();
	        for (Map.Entry<String, String> param : entrys) {
	        	
	            //sign参数 和 空值参数 不加入算法
	            if(param.getValue()!=null && !"".equals(param.getKey().trim()) && !"sign".equals(param.getKey().trim()) && !"".equals(param.getValue().trim())) {
	            	if(param.getKey().equals("text")){
	            		baseString.append(param.getKey().trim()).append("=").append(URLEncoder.encode(param.getValue().trim(),"GBK")).append("&");
	            	}else{
	            		baseString.append(param.getKey().trim()).append("=").append(URLEncoder.encode(param.getValue().trim(),"UTF-8")).append("&");
	            	}
	            }
	        }
	        if(baseString.length() > 0 ) {
	            baseString.deleteCharAt(baseString.length()-1).append("&app_key="+TencentAPI.APP_KEY_AI);
	        }
	        // 使用MD5对待签名串求签
	        try {
	        	String sign = MD5.getMD5(baseString.toString());
	        	return sign;
	        } catch (Exception ex) {
	            throw new IOException(ex);
	        }
	    }
	/**
	 * 获取拼接的参数
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String getParams(HashMap<String,String> params) throws IOException {
		//  先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap<>(params);
        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder baseString = new StringBuilder();
        for (Map.Entry<String, String> param : entrys) {
            //sign参数 和 空值参数 不加入算法
           baseString.append(param.getKey().trim()).append("=").append(URLEncoder.encode(param.getValue().trim(),"UTF-8")).append("&");
        }
       return baseString.toString();
    }
	/**
	 * 获取拼接的参数forNLP
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String getParamsforNLP(HashMap<String,String> params) throws IOException {
		//  先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap<>(params);
        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder baseString = new StringBuilder();
        for (Map.Entry<String, String> param : entrys) {
            //sign参数 和 空值参数 不加入算法
        	if(param.getKey().equals("text")){
        		baseString.append(param.getKey().trim()).append("=").append(URLEncoder.encode(param.getValue().trim(),"GBK")).append("&");
        	}else{
        		baseString.append(param.getKey().trim()).append("=").append(URLEncoder.encode(param.getValue().trim(),"UTF-8")).append("&");
        	}
        }
       return baseString.toString();
    }
}
