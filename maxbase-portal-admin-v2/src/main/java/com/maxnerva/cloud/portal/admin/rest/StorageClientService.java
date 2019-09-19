package com.maxnerva.cloud.portal.admin.rest;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.Map;
//
//
//
//
//
//import java.util.concurrent.TimeUnit;
//
//import com.maxnerva.cloud.common.util.BaseContextHandler;
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.util.ObjectUtils;
//import sun.misc.BASE64Encoder;
//import com.maxnerva.cloud.common.base.msg.RestResponse;
//import com.maxnerva.cloud.portal.admin.service.TicketManagerUtil;
///**
// * 下载文件
// * @author john
// *
// */
//@SuppressWarnings("restriction")
//@Service
//@Slf4j
public class StorageClientService {
//
//	@Autowired
//	protected RestTemplateProvider foreignRestTemplateProvider;
//
//	@Autowired
//	private TicketManagerUtil ticketManagerUtil;
//
//    public	String downLoadByFileId(String fileId){
//
//    	String url = "http://MAXBASE-STORAGE/storage/download/"+fileId;
//    	Map<String,String> headerMap = new HashMap<>();
//    	String tickect = ticketManagerUtil.setTicket(BaseContextHandler.getUserCode(), 300l,TimeUnit.SECONDS,BaseContextHandler.getApplicationCode());
//    	headerMap.put("Ticket", tickect);
//    	if (ObjectUtils.isEmpty(foreignRestTemplateProvider)) {
//            foreignRestTemplateProvider = RestTemplateProviderFactory.creator();
//        }
//    	byte[] obj ;
//    	try {
//    		log.info("开始下载文件，url：{}",url);
//    		obj = foreignRestTemplateProvider.get(
//        			url,byte[].class,MediaType.APPLICATION_OCTET_STREAM, headerMap);
//
//    		String imgFilePath = "/opt/logs/base.jpg";
//    		FileOutputStream out = null;
//			out = new FileOutputStream(imgFilePath);
//			out.write(obj);
//			out.flush();
//	        out.close();
//
//	        InputStream in = null;
//	        byte[] data = null; //读取图片字节数组
//        	in = new FileInputStream(imgFilePath);
//        	data = new byte[in.available()];
//        	in.read(data);
//        	in.close();
//        	new File(imgFilePath).delete();
//	        BASE64Encoder encoder = new BASE64Encoder();
//	        return ("data:image/png;base64,"+encoder.encode(data)).replaceAll("\r\n", "");
//    	} catch (Exception e) {
//			log.error("下载文件失败，",e);
//			return "";
//		}
//    }
//
//
//    /**
//     * 删除文件
//     * @param iconPath
//     */
//	public void deleteByFileId(String iconPath) {
//		log.info("删除文件{}",iconPath);
//		String url = "http://MAXBASE-STORAGE/storage/delete/"+iconPath;
//    	Map<String,String> headerMap = new HashMap<>();
//    	String ticket = ticketManagerUtil.setTicket(BaseContextHandler.getUserCode(), 300l,TimeUnit.SECONDS,BaseContextHandler.getApplicationCode());
//    	headerMap.put("Ticket", ticket);
//    	if (ObjectUtils.isEmpty(foreignRestTemplateProvider)) {
//            foreignRestTemplateProvider = RestTemplateProviderFactory.creator();
//        }
//    	try {
//    		@SuppressWarnings("unchecked")
//			RestResponse<Boolean> r = foreignRestTemplateProvider.delete(url, RestResponse.class, headerMap, null);
//    		if(!r.getData()){
//    			log.error("删除文件"+iconPath+"失败");
//    		}
//    	} catch (Exception e) {
//			log.error("删除文件"+iconPath+"失败",e);
//			//throw new BusinessException("删除文件失败，", 500, iconPath);
//		}
//	}
//
}
