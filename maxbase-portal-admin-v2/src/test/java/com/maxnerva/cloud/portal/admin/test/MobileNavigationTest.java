//package com.maxnerva.cloud.portal.admin.test;
//
//
//
//import lombok.extern.slf4j.Slf4j;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import tk.mybatis.mapper.entity.Example;
//
//import com.maxnerva.cloud.common.base.util.SearchParameter;
//import com.maxnerva.cloud.portal.admin.PortalApplication;
//import com.maxnerva.cloud.portal.admin.dao.entity.MobileNavigationEntity;
//import com.maxnerva.cloud.portal.admin.service.MobileNavigationService;
//import com.maxnerva.cloud.portal.admin.service.vo.MobileNavigationVoService;
//import com.maxnerva.cloud.portal.admin.vo.MobileNavigationVo;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = PortalApplication.class)
//@Slf4j
//public class MobileNavigationTest {
//	
//	@Autowired
//	private MobileNavigationVoService mobileNavigationVoService;
//	
//	@Autowired
//	private MobileNavigationService service;
//	
//	@Test
//	public void searchMobileNavigation(){
//		Example otherExample =new Example(MobileNavigationVo.class);
//		otherExample.orderBy("orderNo").asc();
//		SearchParameter sp = new SearchParameter();
//		sp.setKeywords("TPM");
//		log.info("{}",
//				mobileNavigationVoService.search(MobileNavigationVo.class, sp, otherExample));
//		 
//	 }
//	
//	@Test
//	public void addMobileNavigation() {
////		MobileNavigationEntity entity = MobileNavigationEntity.builder()
////				.applicationCode("appCode")
////				.businessTypeId(1)
////				.description("单元测试描述")
////				.iconPath("")
////				.mobileNavigationCode("test")
////				.mobileNavigationName("单元测试")
////				.orderNo(3)
////				.url("https://xxxx.com")
////				.urlScheme("").build();
////		entity.setDeleteFlag(false);
////        service.addMobileNavigation(entity);
//    }
//	
//	@Test
//    public void remove() {
//
//	}
//	@Test
//    public void update( ) {
//		MobileNavigationEntity entity = service.getAll().get(0);
//		service.fullUpdateMobileNavigation(entity.getId(),entity);
//    }
//    @Test
//    public void updateSelective() {
//    	MobileNavigationEntity entity = service.getAll().get(0);
//		service.partlyUpdateMobileNavigation(entity.getId(),entity);
//    }
//
//    @Test
//    public void quryOrderNo() {
//		log.info("{}", service.quryOrderNo(1,1));
//    }
//
//}
