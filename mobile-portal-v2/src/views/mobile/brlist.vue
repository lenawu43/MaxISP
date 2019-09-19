<template id="elementuicomp">
		<div>
      <intent-filter>
        <action android:name="android.intent.action.VIEW"/>
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data android:scheme="tpm" android:host="index" android:pathPrefix="/openwith"/>
		  </intent-filter>
			<header class="mui-bar-nav">
        <!-- <el-input placeholder="搜索" v-model="searchText" prefix-icon="el-icon-search" suffix-icon="el-icon-search" class="inputClass"> -->
          <!-- <el-button slot="prepend" icon="el-icon-search"></el-button> -->
          <!-- <el-button slot="suffix" icon="el-icon-search"></el-button> -->
       <!-- </el-input> -->
       <!-- <el-input class="search-input" v-model="searchText" :placeholder="搜索" 
                  @focus="clearHolder" @keyup.enter.native="submitForm('')" auto-complete="off">
        </el-input> -->
				<!-- <img class="back-img" src="../../assets/logo/back@2x.png" alt="返回" v-show="isSearch" @click="backPop"> -->
        <!-- <a class="back-img" href="tpm://index/openwith">App</a> -->

				<h3  v-show="isSearch">{{langText.header.mobileMainName}}</h3>
				<img class="search-img" src="../../assets/logo/login_out1.png" alt="login-out" v-show="isSearch" @click="search">
			</header>
			<!-- <el-tabs class="tabs-class" v-model="activeName" @tab-click="handleClick">	
			  <el-tab-pane v-for="(pane,tabPage) in dataJson" :label="pane.label" :name="pane.name" :key="tabPage">
					<section class="driks">
						<el-scrollbar :style="{height: innerHeight + 'px'}">
							<figure v-for="(item ,itemIndex) in pane.imgs" :key='itemIndex' :style="{width:fihureWidth+'px'}" @click="jumpTo(item)">
								<img :src="require('../../assets/logo/'+item.img+'.png')" :alt="item.name" :style="{width:imgWidth+'px',height:imgWidth+'px'}">
								<figcaption :style="{width:imgWidth+20+'px'}">{{item.name}}</figcaption>
							</figure>
						</el-scrollbar>
					</section>
				</el-tab-pane>		
			 </el-tabs>  -->
               <!-- @mousedown="mDown(this)" @mouseup="mUp(this)"  -->

       <el-tabs class="tabs-class" v-model = "activeName" @tab-click="handleClick">	
			  <el-tab-pane v-for="(pane,tabPage) in dataJson" :label="pane.label" :name="pane.name" :key="tabPage">
					<section class="driks">
						<el-scrollbar :style="{height: innerHeight + 'px'}">
							<div class="cell-btom" v-for="(item ,itemIndex) in pane.imgs" :key='itemIndex' :style="{width:screenWidth+'px'}" @click="jumpTo(itemIndex,item)" :id="'CellID'+itemIndex">
                                <img class="cell-img" :src="require('../../assets/logo/'+item.img+'.png')" :alt="item.name" >
                                <span class="cell-title">{{item.name}}</span>
							</div>
						</el-scrollbar>
					</section>
				</el-tab-pane>		
			 </el-tabs> 
		</div>
</template>

<script>

import {
  //Message,
  //MessageBox,
  Notification
} from 'element-ui'

import {setToken,getToken} from '@/utils/auth'
import config from '@/hosts/config'
import VueHeader from '@/components/header'
import productList from '@/components/product-list'
import request from '@/utils/request'
import Cookies from 'js-cookie'
export default {
  created() { 
     this.$store.commit('SET_CivetUrl', ""); 
    // this.$store.dispatch('LogOut'); 
    // console.log(this.$store.state.user.token);
    if(!this.$store.state.user.token){
         this.$router.push({ path: '/browser-login' }); 
    }
  },
  data() {
    return {
      langText: {
        header: this.$t('header'),
        common: this.$t('common')
      },
      tabIndex: 0,
      productsData: [],
      token: Cookies.get('Authorization'),
      windowWidth: this.$store.state.common.windowWidth,
      activeName:'first',
      screenWidth:window.screen.width-20,
			fihureWidth:window.screen.width/4,
			imgWidth:window.screen.width/4-40,
      isSearch:true,
      searchText:"",
      dataJson:[
        {
          label:'全部',
          name:'first',
          imgs:[
                {img:'pc_ico@2x',name:'112電子作業系統',url:'http://112.efoxconn.com/home/index',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'78586員工關愛中心信息平臺',url:'http://78585.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'CA數位憑證管理中心',url:'http://esign.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'CLCS車輛管控系統',url:'http://clcs.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'DPKS',url:'http://10.245.254.36:996',schemeName:'',download_url:'',category:"IPBD"},
                {img:'pc_ico@2x',name:'eCard管理平臺',url:'http://ecard.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'ECS費用管控系統',url:'http://ecs.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'eHR系統',url:'http://hr.pcebg.efoxconn.com/',schemeName:'',download_url:'',category:"DTSA"},
                {img:'pc_ico@2x',name:'EM電子簽核網',url:'http://10.130.14.118',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'EV電子簽核網',url:'http://evouchers.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'Foxconn外包保安稽核/排版/計費系統',url:'http://110.efoxconn.com/paiban/admin/login.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'GSCM',url:'http://globalscm.efoxconn.com/login/Login.aspx',schemeName:'',download_url:'',category:"DTSA"},
                {img:'pc_ico@2x',name:'IDEAS智慧物流管理系統',url:'http://i2.jusdascm.com/ideas/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'IPCD管理平台',url:'http://10.143.2.87/msw/index.jsp',schemeName:'',download_url:'',category:"IPCD"},
                {img:'pc_ico@2x',name:'IPCD戰情看板',url:'http://10.143.2.228:8100/ipcd/#/',schemeName:'',download_url:'',category:"IPCD"},
                {img:'pc_ico@2x',name:'iVMS-8700綜合安防管理平臺軟件',url:'http://10.157.138.156',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'New PCEBG ServiceDesk',url:'http://servicedesk.pcebg.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'New PCEBG 人力資源網',url:'http://hr.pcebg.efoxconn.com/',schemeName:'',download_url:'',category:"IPCD"},
                {img:'pc_ico@2x',name:'New PCEBG提案改善系統',url:'http://ie.cmmsg.efoxconn.com/CMMSGIE/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'NewPCBEG內部調動系統',url:'http://hr3.pcebg.efoxconn.com:8080',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'SFC',url:'http://10.143.2.115/RADISYS/index.html',schemeName:'',download_url:'',category:"IPCD"},
                {img:'pc_ico@2x',name:'TIPTOP國內',url:'http://fthua-erp.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'TIPTOP國外',url:'http://twfi.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'mes_logo8',name:'TPM',url:'http://10.134.226.170/',schemeName:'tpm',download_url:'http://10.134.226.170/UploadFiles/TPM.apk',category:"IPBD"},
                {img:'mes_logo9',name:'TQM',url:'http://10.134.226.176/Manager/Login.aspx',schemeName:'',download_url:'',category:"DTSA"},
                {img:'pc_ico@2x',name:'TW HR員工入口網',url:'http://twemp.efoxconn.com/_layouts/UserLogin/login.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'WMS',url:'http://10.191.19.92',schemeName:'',download_url:'',category:"IPBD"},
                {img:'pc_ico@2x',name:'安全時空',url:'http://110.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'財會協同作業平台',url:'http://fsso.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'餐飲管理平臺',url:'http://10.134.144.13/LoginPage.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'成型-數據採集可視化系統',url:'http://10.202.22.113:8080/Desktop',schemeName:'',download_url:'',category:"DTSA"},
                {img:'pc_ico@2x',name:'衝壓-數據採集可視化系統',url:'http://10.202.22.81:28080/foxconn-b/foxconn/login/login.html',schemeName:'',download_url:'',category:"DTSA"},
                {img:'pc_ico@2x',name:'出入境簽證網',url:'http://visa.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'電子單證數位簽核平台',url:'http://evouchers.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'法務平臺',url:'http://fawu.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'費用轉嫁系統',url:'http://ct.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'富模寶',url:'http://10.245.38.70:890',schemeName:'',download_url:'',category:"DTSA"},
                {img:'pc_ico@2x',name:'富士康飛虎樂購',url:'http://www.efeihu.com/sale/index_new.html',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'富士康富連網商城',url:'https://www.flnet.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'富士康工會聯合會票務在線',url:'http://pw.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'富士康工會網',url:'http://gh.foxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'富士康科技集團公寓管理系統',url:'http://dorm.efoxconn.com/dorm/Login.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'富士康科技集團人資技委會',url:'http://hrtdc.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'富士康內部招募網',url:'http://recruit-ess.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'富士康文化網',url:'http://culture.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'富士康五險一金網',url:'http://insurance.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'富士康隱患管理系統',url:'http://10.138.2.135',schemeName:'',download_url:'',category:"SER&ERC"},
                {img:'pc_ico@2x',name:'富學寶典',url:'http://iedu.foxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'富樂惠',url:'http://admin.efulehui.com.cn',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'幹部招募接待管理系統',url:'http://reception.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'工程E化平臺',url:'http://emdengehua.efoxconn.com/login',schemeName:'',download_url:'',category:"DTSA"},
                {img:'pc_ico@2x',name:'工管資訊短信平臺',url:'http://sms.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'工會會員管理系統',url:'http://ghhy.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'工會經費管控平臺',url:'http://union.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'工業安全事故申報管理系統',url:'http://gsmsp.efoxconn.com',schemeName:'',download_url:'',category:"SER&ERC"},
                {img:'pc_ico@2x',name:'工業互聯網學院',url:'http://ie.foxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'工業互聯網學院電子簽核系統(教室借用)',url:'http://ieconfirm.efoxconn.com/login.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'工業互聯網學院教育訓練系統',url:'http://edu.efoxconn.com/Login.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'供應商管理平臺電子簽核',url:'http://supplier.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'海外ECS',url:'http://ecs.efoxconn.com/tw/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'海外人力統計系統',url:'http://10.57.90.21:8008/Login.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'戶政管理系統',url:'http://10.138.2.7:8000/',schemeName:'',download_url:'',category:"HR"},
                {img:'mes_logo11',name:'花果山',url:'http://tm.iisd.efoxconn.com/#/login',schemeName:'',download_url:'',category:"DTSA"},
                {img:'pc_ico@2x',name:'基礎人力招募管理系統',url:'http://dl.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團表單電子簽核',url:'http://caseflow.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團財會資訊處客服中心',url:'http://cc.efoxconn.com/Main/Main.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團泛集團人事系統',url:'http://10.134.99.105/Login.aspx?errorid=1 ',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團固定資產採購平臺',url:'http://cfa-fixasset.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團固定資產管理系統',url:'http://fixedasset.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團戶籍管理系統',url:'http://household.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團內控自評e化平臺',url:'http://icsa.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團人事資料稽核中心',url:'http://chr.efoxconn.com/Login.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團臺幹晉升評鑑e化系統',url:'http://review.efoxconn.com/TW/ ',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團學歷教育網',url:'http://fedu.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團醫療補助網（集團自保）',url:'http://care.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團用印申請平臺',url:'http://seal.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團員工差旅既費用報銷系統',url:'http://ters.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團員工跨事業群調動e化系統',url:'http://transfer.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團招募/員工跨事業群調動電子簽核系統',url:'http://approve.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團招募管理系統',url:'http://in-recruit.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團租賃管理系統',url:'http://cals.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'績效考核系統',url:'http://10.157.137.76/HR/LoginForm.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'全球客服',url:'http://114.foxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'日結薪資系統',url:'http://10.157.138.215',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'生產力最佳化',url:'',schemeName:'',download_url:'',category:"IPBD"},
                {img:'pc_ico@2x',name:'施工安全管理系統',url:'http://10.148.8.12/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'施工安全管理系統',url:'http://10.148.8.12',schemeName:'',download_url:'',category:"SER&ERC"},
                {img:'pc_ico@2x',name:'施耐德智能製造系統',url:'http://10.202.16.96:60002/Login',schemeName:'',download_url:'',category:"DTSA"},
                {img:'pc_ico@2x',name:'台北總務總處服務平台',url:'http://tpgad-2u.efoxconn.com/isite/s/index.htm',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'台外幹績效考核系統',url:'http://10.62.196.170/ ',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'臺北電子簽核系統',url:'http://wf.foxconn.com/foxconn/login.asp',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'塗裝-數據採集可視化系統',url:'http://10.202.22.160:8080/login',schemeName:'',download_url:'',category:"DTSA"},
                {img:'pc_ico@2x',name:'外干請假系統簽核平台',url:'http://eip.efoxconn.com/WORKFLOW/LoginForm.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'香信生活頻道',url:'http://civetpublic.foxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'隱患管理系統',url:'http://10.138.2.135/system/sys_login.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'職工福利平台',url:'http://welfare.efoxconn.com/iwelfare/s/index?msg=login_first',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'中港津貼報稅管理系統',url:'http://www.taxallowan.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'重慶教育訓練網',url:'http://cqtc.efoxconn.com/index.aspx',schemeName:'',download_url:'',category:"HR"}
            ]
        },
        {
          label:'HR',
          name:'second',
          imgs:[
                {img:'pc_ico@2x',name:'112電子作業系統',url:'http://112.efoxconn.com/home/index',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'78586員工關愛中心信息平臺',url:'http://78585.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'CA數位憑證管理中心',url:'http://esign.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'CLCS車輛管控系統',url:'http://clcs.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                
                {img:'pc_ico@2x',name:'eCard管理平臺',url:'http://ecard.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'ECS費用管控系統',url:'http://ecs.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                
                {img:'pc_ico@2x',name:'EM電子簽核網',url:'http://10.130.14.118',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'EV電子簽核網',url:'http://evouchers.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'Foxconn外包保安稽核/排版/計費系統',url:'http://110.efoxconn.com/paiban/admin/login.aspx',schemeName:'',download_url:'',category:"HR"},
                
                {img:'pc_ico@2x',name:'IDEAS智慧物流管理系統',url:'http://i2.jusdascm.com/ideas/',schemeName:'',download_url:'',category:"HR"},
                
                {img:'pc_ico@2x',name:'iVMS-8700綜合安防管理平臺軟件',url:'http://10.157.138.156',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'New PCEBG ServiceDesk',url:'http://servicedesk.pcebg.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                
                {img:'pc_ico@2x',name:'New PCEBG人力資源網',url:'http://hr.pcebg.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'New PCEBG提案改善系統',url:'http://ie.cmmsg.efoxconn.com/CMMSGIE/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'NewPCBEG內部調動系統',url:'http://hr3.pcebg.efoxconn.com:8080',schemeName:'',download_url:'',category:"HR"},
                
                {img:'pc_ico@2x',name:'TIPTOP國內',url:'http://fthua-erp.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'TIPTOP國外',url:'http://twfi.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                
                {img:'pc_ico@2x',name:'TW HR員工入口網',url:'http://twemp.efoxconn.com/_layouts/UserLogin/login.aspx',schemeName:'',download_url:'',category:"HR"},
                
                {img:'pc_ico@2x',name:'安全時空',url:'http://110.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'財會協同作業平台',url:'http://fsso.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'餐飲管理平臺',url:'http://10.134.144.13/LoginPage.aspx',schemeName:'',download_url:'',category:"HR"},
                
                {img:'pc_ico@2x',name:'出入境簽證網',url:'http://visa.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'電子單證數位簽核平台',url:'http://evouchers.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'法務平臺',url:'http://fawu.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'費用轉嫁系統',url:'http://ct.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                
                {img:'pc_ico@2x',name:'富士康飛虎樂購',url:'http://www.efeihu.com/sale/index_new.html',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'富士康富連網商城',url:'https://www.flnet.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'富士康工會聯合會票務在線',url:'http://pw.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'富士康工會網',url:'http://gh.foxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'富士康科技集團公寓管理系統',url:'http://dorm.efoxconn.com/dorm/Login.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'富士康科技集團人資技委會',url:'http://hrtdc.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'富士康內部招募網',url:'http://recruit-ess.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'富士康文化網',url:'http://culture.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'富士康五險一金網',url:'http://insurance.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                
                {img:'pc_ico@2x',name:'富學寶典',url:'http://iedu.foxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'富樂惠',url:'http://admin.efulehui.com.cn',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'幹部招募接待管理系統',url:'http://reception.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                
                {img:'pc_ico@2x',name:'工管資訊短信平臺',url:'http://sms.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'工會會員管理系統',url:'http://ghhy.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'工會經費管控平臺',url:'http://union.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                
                {img:'pc_ico@2x',name:'工業互聯網學院',url:'http://ie.foxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'工業互聯網學院電子簽核系統(教室借用)',url:'http://ieconfirm.efoxconn.com/login.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'工業互聯網學院教育訓練系統',url:'http://edu.efoxconn.com/Login.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'供應商管理平臺電子簽核',url:'http://supplier.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'海外ECS',url:'http://ecs.efoxconn.com/tw/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'海外人力統計系統',url:'http://10.57.90.21:8008/Login.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'戶政管理系統',url:'http://10.138.2.7:8000/',schemeName:'',download_url:'',category:"HR"},
                
                
                
                {img:'pc_ico@2x',name:'基礎人力招募管理系統',url:'http://dl.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團表單電子簽核',url:'http://caseflow.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團財會資訊處客服中心',url:'http://cc.efoxconn.com/Main/Main.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團泛集團人事系統',url:'http://10.134.99.105/Login.aspx?errorid=1 ',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團固定資產採購平臺',url:'http://cfa-fixasset.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團固定資產管理系統',url:'http://fixedasset.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團戶籍管理系統',url:'http://household.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團內控自評e化平臺',url:'http://icsa.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團人事資料稽核中心',url:'http://chr.efoxconn.com/Login.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團臺幹晉升評鑑e化系統',url:'http://review.efoxconn.com/TW/ ',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團學歷教育網',url:'http://fedu.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團醫療補助網（集團自保）',url:'http://care.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團用印申請平臺',url:'http://seal.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團員工差旅既費用報銷系統',url:'http://ters.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團員工跨事業群調動e化系統',url:'http://transfer.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團招募/員工跨事業群調動電子簽核系統',url:'http://approve.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團招募管理系統',url:'http://in-recruit.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'集團租賃管理系統',url:'http://cals.efoxconn.com',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'績效考核系統',url:'http://10.157.137.76/HR/LoginForm.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'全球客服',url:'http://114.foxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'日結薪資系統',url:'http://10.157.138.215',schemeName:'',download_url:'',category:"HR"},
                
                {img:'pc_ico@2x',name:'施工安全管理系統',url:'http://10.148.8.12/',schemeName:'',download_url:'',category:"HR"},
                
                
                {img:'pc_ico@2x',name:'台北總務總處服務平台',url:'http://tpgad-2u.efoxconn.com/isite/s/index.htm',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'台外幹績效考核系統',url:'http://10.62.196.170/ ',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'臺北電子簽核系統',url:'http://wf.foxconn.com/foxconn/login.asp',schemeName:'',download_url:'',category:"HR"},
                
                {img:'pc_ico@2x',name:'外干請假系統簽核平台',url:'http://eip.efoxconn.com/WORKFLOW/LoginForm.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'香信生活頻道',url:'http://civetpublic.foxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'隱患管理系統',url:'http://10.138.2.135/system/sys_login.aspx',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'職工福利平台',url:'http://welfare.efoxconn.com/iwelfare/s/index?msg=login_first',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'中港津貼報稅管理系統',url:'http://www.taxallowan.efoxconn.com/',schemeName:'',download_url:'',category:"HR"},
                {img:'pc_ico@2x',name:'重慶教育訓練網',url:'http://cqtc.efoxconn.com/index.aspx',schemeName:'',download_url:'',category:"HR"}
                ]
        },
        {
          label:'DTSA',
          name:'three',
          imgs:[
            {img:'pc_ico@2x',name:'eHR系統',url:'http://hr.pcebg.efoxconn.com/',schemeName:'',download_url:'',category:"DTSA"},
            {img:'pc_ico@2x',name:'GSCM',url:'http://globalscm.efoxconn.com/login/Login.aspx',schemeName:'',download_url:'',category:"DTSA"},
            {img:'mes_logo8',name:'TPM',url:'http://10.134.226.170/',schemeName:'tpm',download_url:'http://10.134.226.170/UploadFiles/TPM.apk',category:"DTSA"},
            {img:'mes_logo9',name:'TQM',url:'http://10.134.226.176/Manager/Login.aspx',schemeName:'',download_url:'',category:"DTSA"},
            {img:'pc_ico@2x',name:'成型-數據採集可視化系統',url:'http://10.202.22.113:8080/Desktop',schemeName:'',download_url:'',category:"DTSA"},
            {img:'pc_ico@2x',name:'衝壓-數據採集可視化系統',url:'http://10.202.22.81:28080/foxconn-b/foxconn/login/login.html',schemeName:'',download_url:'',category:"DTSA"},
            {img:'pc_ico@2x',name:'富模寶',url:'http://10.245.38.70:890',schemeName:'',download_url:'',category:"DTSA"},
            {img:'pc_ico@2x',name:'工程E化平臺',url:'http://emdengehua.efoxconn.com/login',schemeName:'',download_url:'',category:"DTSA"},
            {img:'mes_logo11',name:'花果山',url:'http://tm.iisd.efoxconn.com/#/login',schemeName:'',download_url:'',category:"DTSA"},
            {img:'pc_ico@2x',name:'施耐德智能製造系統',url:'http://10.202.16.96:60002/Login',schemeName:'',download_url:'',category:"DTSA"},
            {img:'pc_ico@2x',name:'塗裝-數據採集可視化系統',url:'http://10.202.22.160:8080/login',schemeName:'',download_url:'',category:"DTSA"},
          ]
        },
        {
          label:'IPBD',
          name:'four',
          imgs:[
            {img:'pc_ico@2x',name:'DPKS',url:'http://10.245.254.36:996',schemeName:'',download_url:'',category:"IPBD"},
            {img:'mes_logo8',name:'TPM',url:'http://10.134.226.170/',schemeName:'tpm',download_url:'http://10.134.226.170/UploadFiles/TPM.apk',category:"IPBD"},  
           {img:'mes_logo9',name:'TQM',url:'http://10.134.226.176/Manager/Login.aspx',schemeName:'',download_url:'',category:"IPBD"},
           {img:'pc_ico@2x',name:'WMS',url:'http://10.191.19.92',schemeName:'',download_url:'',category:"IPBD"},
           {img:'mes_logo11',name:'花果山',url:'http://tm.iisd.efoxconn.com/#/login',schemeName:'',download_url:'',category:"IPBD"},
           {img:'pc_ico@2x',name:'生產力最佳化',url:'',schemeName:'',download_url:'',category:"IPBD"},
         ]
        },
        {
          label:'CM&GSSD',
          name:'five',
          imgs:[
            {img:'mes_logo11',name:'花果山',url:'http://tm.iisd.efoxconn.com/#/home',schemeName:'',download_url:'',category:"CM&GSSD"},
          ]
        },
        {
          label:'IPCD',
          name:'six',
          imgs:[
            {img:'pc_ico@2x',name:'IPCD管理平台',url:'http://10.143.2.87/msw/index.jsp',schemeName:'',download_url:'',category:"IPCD"},
            {img:'pc_ico@2x',name:'IPCD戰情看板',url:'http://10.143.2.228:8100/ipcd/#/',schemeName:'',download_url:'',category:"IPCD"},
            {img:'pc_ico@2x',name:'New PCEBG 人力資源網',url:'http://hr.pcebg.efoxconn.com/',schemeName:'',download_url:'',category:"IPCD"},
            {img:'pc_ico@2x',name:'SFC',url:'http://10.143.2.115/RADISYS/index.html',schemeName:'',download_url:'',category:"IPCD"},
          ]
        },
        {
          label:'SER&ERC',
          name:'seven',
          imgs:[
            {img:'pc_ico@2x',name:'富士康隱患管理系統',url:'http://10.138.2.135',schemeName:'',download_url:'',category:"SER&ERC"},
            {img:'pc_ico@2x',name:'工業安全事故申報管理系統',url:'http://gsmsp.efoxconn.com',schemeName:'',download_url:'',category:"SER&ERC"},
            {img:'pc_ico@2x',name:'施工安全管理系統',url:'http://10.148.8.12',schemeName:'',download_url:'',category:"SER&ERC"},
          ]
        }
      ],
    }
  },
  watch: {
      fihureWidth (val) {
				this.fihureWidth = val;
				this.imgWidth = val-40;
      },
      screenWidth(val){
        this.screenWidth = val;
      }
		},
  mounted () {
			const that = this
			//事件会在窗口或框架被调整大小时发生
			window.onresize = () => {
				return (() => {
					 that.fihureWidth = window.screen.width/4;
           that.imgWidth = that.fihureWidth-40;
           that.screenWidth = window.screen.width-20;
				})()
			}
		},
  computed: {
    innerHeight() {
      return  this.$store.getters.mainHeight-28;
    }
  },
  methods: {
    // createIframe(){
		//   var iframe;
		//     return function(){
		//         if(iframe){
		//             return iframe;
		//         }else{
		//             iframe = document.createElement('iframe');
		//             iframe.style.display = 'none';
		//             document.body.appendChild(iframe);
		//             return iframe;      
		//         }
		//     }
		// },
    backPop(){
      window.localStorage.removeItem('CivetUserCode');
      this.$router.push({ path: '/browser-login' });
    },
    search(){
        this.$store.commit('SET_TOKEN', "");
        this.$store.dispatch('LogOut');
        this.$router.push({ path: '/browser-login' });
    },
    //跳转各平台
    jumpTo(index,item){
      console.log(this);
      //this.style.color = 'red';
      if(item.name=="MaxBase"){
        //大平台
        this.$router.push('/');
      }else{
        if(item.schemeName === ""){
          item.url && window.open(item.url);
        }else{
              var cellID= "CellID"+String(index);
              var oDiv=document.getElementById(cellID);
              oDiv.style.cssText = "pointer-events: none; cursor: default; opacity: 0.4";

              var isBlur = false;
              // 通过URL scheme来调起APP,判断是否安装
              window.location.href = item.schemeName+'://index/openwith';
              setTimeout(function() {
                if (!isBlur) {
                  oDiv.style.cssText = "pointer-events: auto; cursor: default; opacity: 1.0";
                  window.location = item.download_url;
                }
              }, 3000);
              // window 每次失去焦點
              window.onblur = function() {
                console.log('失去焦點');
                isBlur = true;
                oDiv.style.cssText = "pointer-events: auto; cursor: default; opacity: 1.0";
              };
              var hiddenProperty = 'hidden' in document ? 'hidden' :
                  'webkitHidden' in document ? 'webkitHidden' :
                  'mozHidden' in document ? 'mozHidden' :
                  null;
              var visibilityChangeEvent = hiddenProperty.replace(/hidden/i, 'visibilitychange');
              var onVisibilityChange = function() {
                  if (document[hiddenProperty]) {
                    console.log('失去焦點');
                    isBlur = true;
                    oDiv.style.cssText = "pointer-events: auto; cursor: default; opacity: 1.0";
                  }
              }
              document.addEventListener(visibilityChangeEvent, onVisibilityChange);
        }
      }
    },
    handleClick(tab, event) {
      console.log(tab.name,tab.label);
      this.tabIndex = Number(tab.index);
		},
    dealUrl(item) {
      return item.applicationUrlEntities[0].url + (item.code !== 'Maxbase'? `?token=${this.token}` : '')
    },
   
    selectProduct(item, index) {
      if (item.applicationUrlEntities[0].url) {
        /* request({
          url: '/admin/user-application-config/updateLastApplication/' + item.id,
          method: 'put'
        }).then(response => {
          // if (item.code === 'Maxbase') {//本系统不需要新开页面
          //   window.location.href = item.url;
          // } else {
          //   jump(item.url);
          // }
          // this.$store.state.permission.authIndex = index;
          this.$emit('select-product', item);
        }); */
        let url = this.dealUrl(item)
        Cookies.set('Last-url', url);
        if(item.code !== 'Maxbase') {
          jump(url);
        } else {
          window.location.href = url;
        }
        this.$emit('select-product', item);
      }
    }
  },
  components: {
    VueHeader,
    productList
  }
}
</script>

<style scoped lang="scss">
@import '@/assets/css/_constant.scss';
    .disabled {
        pointer-events: none;
        cursor: default;
        opacity: 0.6;
    }
		.mui-bar-nav{
			margin: 0px;
			padding: 0rem;
			position: flex;
			top: 0px;
			height: 3rem;
			background-color: #004279;
      // .search-input{
      //   display: block;
      //   height:20px;
      //   background-color:rgba(10,83,144,1);
      //   border-radius:4px;
      //   color: #4A4A4A;
      //   width:200px; 
      //   margin: 10px auto;
      // }
      .search-img{
        width: 1.25rem;
        height: 1.25rem;
        position: fixed;
        right: 12px;
        top: 0.875rem;
      }
      .back-img{
        width: 1.25rem;
        height: 1.25rem;
        position: fixed;
        left: 0.625rem;
        top: 0.875rem;
      }
      h3{
        margin: 0rem auto;
        padding: 0rem;
        height:3rem;
        font-size:16px;
        font-family:MicrosoftYaHei;
        color:rgb(255, 255, 255);
        line-height:3rem;
        text-align: center;
      }
		}
   
		/deep/.tabs-class{
			position: fixed;
			left: 0px;
			right: 0px;
			top: 3rem;
			margin: 0px;

      .el-tabs__header{
        margin: 0px 10px;
        // height: 60px;
        // .el-tabs__nav-scroll{
        //   height: 60px;
        // }
        // .el-tabs__nav{
        //   height: 60px;
        // }
      }
      .el-tabs__item{
        font-family:MicrosoftYaHei;
        color:rgba(74,74,74,1);
        font-size: 13px;
      }
      .is-active{
        font-family:MicrosoftYaHei;
        color:rgba(19,110,187,1);
         font-size: 13px;
      }
      // .driks{
      //   margin: 0rem;
      //   width: 100%;
      //   display: flex;
      //   justify-content: flex-start;
      //   flex-direction: row;
      //   flex-wrap: wrap;
      //   figure{
      //     max-width: 6.875rem;
      //     float: left;
      //     margin: 0.625rem 0px 0px 0px;
      //     padding: 0px;
      //     img{
      //       max-width: 6.875rem;
      //       max-height: 6.875rem;
      //       margin: 0rem 1.25rem;
      //     }
      //   }
      //   figcaption{
      //     text-align: center;
      //     margin: 0px 0.625rem;
      //     font-size: 0.75rem;
      //     font-family: "PingFang SC Regular";
      //     height: 2rem;
      //     max-width: 6.875rem;
      //     font-weight:400;
      //     color:rgba(74,74,74,1);
      //   }
      // }
      .driks{
        background-color: rgba(244,244,242,1);
        margin: 0rem;
        width: 100%;
        display: flex;
        justify-content: flex-start;
        flex-direction: row;
        align-items: flex-start;
        flex-wrap: wrap;
        .cell-btom{
          width:100%;
          float: left;
          height: 64px;
          margin: 5px 0px 0px 10px;
          padding: 0px;
          box-shadow:0px 1px 4px 0px rgba(0,0,0,0.12);
          border-radius:2px;
          opacity:0.9976;
          background-color: white;
          .cell-img{
            // float: left;
            position: relative;
            top: 10px;
            bottom: 18px;
            left: 8px;
            width: 30px;
            height: 30px;
            // margin: 0rem 1.25rem;
            // display: block;
          }
          .cell-title{
            // float: left;
            position: relative;
            left: 12px;
            top: 0px;
            margin: 0px;
            padding: 0px;
            text-align: left;
            font-size: 14px;
            font-family: "PingFang SC Regular";
            height: 64px;
            line-height: 64px;
            // display: block;
            // width: 100%;
            width: 100px;
            font-weight:400;
            color:rgba(74,74,74,1);
            // border-bottom: 1px solid rgba(229,229,224,1); 
          }
        }
       
      }
		}
	
</style>