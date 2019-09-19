<template id="elementuicomp">
		<div>          
      <intent-filter>
        <action android:name="android.intent.action.VIEW"/>
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />        
          <data android:scheme="tpm" android:host="index" android:pathPrefix="/openwith"/> 

         <!-- <data v-for="(item,index) in applicationALL.filter(r=>r.urlScheme)" :key="index" 
         v-text="GetScheme(item.urlScheme)" android:host="index" android:pathPrefix="/openwith"/> -->
      
        
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
				<!-- <img class="search-img" src="../../assets/logo/icon_search.png" alt="搜索" v-show="isSearch" @click="search"> -->
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

       <el-tabs class="tabs-class"  @tab-click="handleClick">	
          <el-tab-pane label="全部">
            <section class="driks" >
              <el-scrollbar :style="{height: innerHeight + 'px'}">
                <!-- @mousedown="mDown(this)" @mouseup="mUp(this)"  -->
                <div class="cell-btom" v-for="(appDetail,index) in applicationALL" :key='index' :style="{width:screenWidth+'px'}" @click="jumpTo(appDetail.url,appDetail.urlScheme,appDetail.id)" :id="`CellID${appDetail.id}`">
                  <img class="cell-img" :src="appDetail.imgFullPath">
                  <span class="cell-title">{{appDetail.mobileNavigationName}}</span>
                </div>
              </el-scrollbar>
            </section>
          </el-tab-pane>    
           <el-tab-pane 
                   v-for="itemType in applicationType"
                   :key="itemType.businessTypeId"
                :label="itemType.businessTypeName"> 
               <section class="driks" >
            <el-scrollbar :style="{height: innerHeight + 'px'}">
                <!-- @mousedown="mDown(this)" @mouseup="mUp(this)"  -->
                <div class="cell-btom" v-for="(appDetail,index) in applicationALL.filter(r=>r.businessTypeId==itemType.businessTypeId)" :key='index' :style="{width:screenWidth+'px'}" @click="jumpTo(appDetail.url,appDetail.urlScheme,appDetail.id)" :id="`CellID${appDetail.id}`">
                  <img class="cell-img" :src="appDetail.imgFullPath">
                  <span class="cell-title">{{appDetail.mobileNavigationName}}</span>
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

import {setToken} from '@/utils/auth'
import config from '@/hosts/config'
import VueHeader from '@/components/header'

import request from '@/utils/request'
import Cookies from 'js-cookie'
export default {
  created() {    
   this.getProductList();   
  },
  data() {
    return {
      langText: {
        header: this.$t('header'),
        common: this.$t('common')
      },      
      tabIndex: 0,
      iCivetUserCode:"",
      productsData: [],
      applicationALL:[],
      applicationType:[],
      token: Cookies.get('Authorization'),
      windowWidth: this.$store.state.common.windowWidth,
      activeName:'first',
      screenWidth:window.screen.width-20,
			fihureWidth:window.screen.width/4,
			imgWidth:window.screen.width/4-40,
      isSearch:true,
      searchText:"",      
      dataJson:[        
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
    GetScheme(scheme){
        return `android:scheme="${scheme}"`;
     
    },
    createImageURL(imageid,index,ticket){ 
      let  ip=window.location.hostname; 
      let url="http://"+ip+":"+config["img_random_url"]+imageid+".jpg"; 
      this.applicationALL[index]["imgFullPath"]=url;       
      this.$set(this.applicationALL,index,this.applicationALL[index]);           
    },
    backPop(){
      window.localStorage.removeItem('CivetUserCode');
      this.$router.push({ path: '/login' });
    },
    search(){
      // this.isSearch = false;
    },
    getProductList(){
       request({    
          url: '/portal-v2/api/mobile-navigation-api/all'
        }).then(response => {
          this.applicationALL=response.data;
          this.applicationALL.forEach(item=>{              
            console.log(item.businessTypeId);
            let isExist=this.applicationType.some(type=>{
              return type.businessTypeId==item.businessTypeId;
            });
            if(!isExist){
              this.applicationType.push({businessTypeId:item.businessTypeId,businessTypeName:item.businessTypeName});
            }   
          }) 
          
          if(this.applicationALL.length>0){
             this.applicationALL.forEach((item,index)=>{
                  this.createImageURL(item.iconPath,index,"");
              });
          }    
       });  

    },
    //跳转各平台
    jumpTo(url,schemeName,id){ 
      if(url){
        request({
          url: `/portal-v2/operation-record/visit-url`,
          method:'post',
          data:
          {
            url:url
          }
        })  
      }
      console.log(this);
      //this.style.color = 'red';    
        if(!schemeName){
          url && window.open(url);
        }else{
              var cellID= `CellID${id}`;
              var oDiv=document.getElementById(cellID);
              oDiv.style.cssText = "pointer-events: none; cursor: default; opacity: 0.4";
              var isBlur = false;
              // 通过URL scheme来调起APP,判断是否安装
              window.location.href = schemeName+'://index/openwith';
              setTimeout(function() {
                if (!isBlur) {
                  oDiv.style.cssText = "pointer-events: auto; cursor: default; opacity: 1.0";
                  window.location = url;
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
      
        //url && window.open(url);
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
    VueHeader    
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