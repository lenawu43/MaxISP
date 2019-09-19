<template>
  <div>
    <vue-header :title="system_name" :hideProduct="true"></vue-header>
    <article class="product-panel" :style="{height: innerHeight + 'px'}">
      <section class="left-menu">
        <ul>
          <li @mouseenter="tabIndex = 0" :class="{active: tabIndex === 0}">
            <i class="element-ex-icons el-icon-ex-overview"></i>
            {{langText.common.overview}}
            <i class="el-icon-arrow-right"></i>
          </li>
          <li @mouseenter="tabIndex = 1" :class="{active: tabIndex === 1}">
            <i class="element-ex-icons el-icon-ex-service"></i>
            {{langText.common.allService}}
            <i class="el-icon-arrow-right"></i>
          </li>
        </ul>
      </section>
        <section class="right-content" :style="{width: innerWidth + 'px', height: innerHeight + 'px'}">
        <el-scrollbar class="hide-warp" :style="{height: innerHeight + 'px'}">
          <section v-show="tabIndex === 0" class="container" :style="{height: innerHeight + 'px'}">
            <div class="overview">
              <img :src="index_img">
              <!-- <ul class="product-links">
                <li :key="index" v-if="item.url" v-for="(item, index) in linksInfo"
                  :style="{
                    width: innerWidth * item.width + 'px',
                    height: orginPicture.height * scaleRate * item.height + 'px',
                    left: innerWidth * item.left + 'px',
                    top: orginPicture.height * scaleRate * item.top + 'px'
                  }"
                  @click="jump(item.url)"
                >
                </li>
              </ul> -->
            </div>
          </section>
          <div v-show="tabIndex === 1" class="product-content" >
            <!-- 弹出二维码 -->
            <el-dialog width="400px" :visible.sync="dialogTableVisible">
               <PopView :imgUrl="imgUrladdress" :appName="appName"></PopView>
            </el-dialog>
              <el-tabs type="border-card" @tab-click="tabhandleClick">                 
                 <el-tab-pane label="全部">
                   <article v-for="itemType in applicationType"
                   :key="itemType.businessTypeId"
                   >
                      <h3>{{itemType.businessTypeName}}</h3>
                      <ul class="product-list clearfix" >
                        <li @click="jumptoall(appDetail)"
                        v-for="(appDetail,index) in applicationALL.filter(r=>r.businessTypeId==itemType.businessTypeId)" 
                        :key="index"
                         >
                          <img :src="appDetail.imgFullPath">
                          <h4>{{appDetail.webNavigationName}}</h4>
                          <p>{{appDetail.description}}</p>
                          <div class="downloadbtn">                            
                            <div><el-button v-if="appDetail.webUrl||appDetail.navigationType==='applicationGroup'" style="width:80px;" size="mini" round @click.self.stop>Web</el-button></div>                       
                            <div><el-button v-if="appDetail.appDownloadUrl" size="mini" style="width:80px;" round @click.stop="appDownload(appDetail.appDownloadUrl,appDetail.webNavigationName)">App下载</el-button></div>
                          </div>  
                           <span @click.stop="" v-if="appDetail.status!=='normal'" class="in-development">{{langText.common.inDevelopment}}</span>             
                        </li>   

                      </ul>
                   </article>
                 </el-tab-pane>

                <el-tab-pane 
                   v-for="itemType in applicationType"
                   :key="itemType.businessTypeId"
                :label="itemType.businessTypeName">                
                      <h3>{{itemType.businessTypeName}}</h3>
                      <ul class="product-list clearfix" >
                        <li @click="jumptoall(appDetail)"
                          v-for="(appDetail,index) in applicationALL.filter(r=>r.businessTypeId==itemType.businessTypeId)" 
                          :key="index"
                         >
                        <img :src="appDetail.imgFullPath">
                        <h4>{{appDetail.webNavigationName}}</h4>
                        <p>{{appDetail.description}}</p>
                        <div class="downloadbtn">
                            <div><el-button v-if="appDetail.webUrl||appDetail.navigationType==='applicationGroup'" style="width:80px;" size="mini" round @click.self.stop>Web</el-button></div>                       
                            <div><el-button v-if="appDetail.appDownloadUrl" size="mini" style="width:80px;" round @click.stop="appDownload(appDetail.appDownloadUrl,appDetail.webNavigationName)">App下载</el-button></div>
                        </div>
                        <span @click.stop="" v-if="!(appDetail.status=='normal')" class="in-development">{{langText.common.inDevelopment}}</span>
                        </li>
                      </ul>   
                </el-tab-pane> 
              </el-tabs>
              <el-input v-show="issearchShow" class="searchInput"
              placeholder="关键词搜索" 
              prefix-icon="el-icon-search"
              v-model="searchTitle" 
              @blur="searchClick"
              clearable>
            </el-input>
              <button v-show="issearchShow" class="searchBtn" @click="searchClick">搜索</button>
                   <div v-if="isAdmin" class="absolute">                 
                  <a target="_blank" style="text-decoration: none;" :href="GetUrl()"><span style="color:#004279"><i style="backgroud-color:#004279" class="el-icon-setting"></i></span></a>
        </div> 
          </div>
        </el-scrollbar>
      </section>
       

      
    </article>
  </div>
</template>

<script>
import VueHeader from '@/components/header'
import productList from '@/components/product-list'
import request from '@/utils/request'
import Cookies from 'js-cookie'
import PopView from './popview'
import config from '@/hosts/config.js'

// 原图宽度w=1761 h=1103
let orginPicture = {
  width: 1761,
  height: 864
};
export default {
  // var weiXinUrl="../../assets/product-logo/MAXBASE.png";
  created() {    
    let type = window.location.hostname.split('.')[0] || 'web';
    if(process.env.NODE_ENV === 'development') {// 开发环境下写死type
      type = 172;
    }
    // request({
    //   // url: '/admin/application-system/selectApplicationSystemByUserID'
    //   url: '/admin/application-system/selectAccessibleByTerminalType/web' + type
    // }).then(response => {
    //   this.productsData = response.data;
    // });

    request({
      // url: '/admin/application-system/selectApplicationSystemByUserID'
      url: '/portal-v2/api/web-navigation-api/all'
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
       });  
       console.log(this.applicationType);
       if(this.applicationALL.length>0){
          this.applicationALL.forEach((item,index)=>{
          this.createImageURL(item.iconPath,index,"ticket");
          })
       }    
      //  request({
      //     url: `/api/maxbase-portal-admin/portal/api/web-navigation-api/is-admin`
      //   }).then(response => { 
      //      this.isAdmin=response.data;
      //   })      
    });

     

    
  },
  data() {
    return {
      langText: {
        header: this.$t('header'),
        common: this.$t('common')
      },
      isAdmin:false,
      applicationALL:[],
      applicationType:[],
      tabIndex: 0,
      productsData: [],
      dialogTableVisible:false,
      imgUrladdress:"",
      appName:"",
      searchTitle:"",
      issearchShow:false,
      token: Cookies.get('Authorization'),
      orginPicture,
      linksInfo: [// 根据原图尺寸算出各个链接的位置/大小的比值
        {//1
          left: null,
          top: null,
          width: null,
          height: null,
          url: ''
        },
        {//2 eFox Net Center
          left: 98/orginPicture.width,
          top: 442/orginPicture.height,
          width: 203/orginPicture.width,
          height: 22/orginPicture.height,
          url: 'http://cqippdwebcenter.ecmms.foxconn/FGIReport/WOList.aspx?pAdd=1&pEdit=1&pDelete=1&pCopy=1&admin=T&groupName=&pModule=SFC&pSection=Tool&pFunction=WO+List&pfunctionname=SFC_WO_LIST&Name=AUTO'
        },
        {//3 DPKS
          left: 439/orginPicture.width,
          top: 300/orginPicture.height,
          width: 182/orginPicture.width,
          height: 22/orginPicture.height,
          url: 'http://10.245.254.34:8082/MES/FDPS/SMPullListTrack.aspx'
        },
        {//4 LCS
          left: 98/orginPicture.width,
          top: 464/orginPicture.height,
          width: 203/orginPicture.width,
          height: 22/orginPicture.height,
          url: 'http://10.245.254.87:8080/system/login.html'
        },
        {//5 VMI
          left: 439/orginPicture.width,
          top: 279/orginPicture.height,
          width: 182/orginPicture.width,
          height: 22/orginPicture.height,
          url: 'http://10.246.69.42/webfrmwk/main.asp?remaindays=99999'
        },
        {//6 eFox Net Center
          left: 1338/orginPicture.width,
          top: 241/orginPicture.height,
          width: 180/orginPicture.width,
          height: 65/orginPicture.height,
          url: 'http://10.245.36.180:8081/Home/Index'
        },
        {//7 龙宫平台
          left: 0/orginPicture.width,
          top: 0/orginPicture.height,
          width: 0/orginPicture.width,
          height: 0/orginPicture.height,
          url: 'http://10.60.136.155:80'
        },
        {//8 花果山
          left: 308/orginPicture.width,
          top: 20/orginPicture.height,
          width: 1081/orginPicture.width,
          height: 138/orginPicture.height,
          url: 'http://tm.iisd.efoxconn.com/#/login?uname=hgs_customer&pword=123'
        },
        {//9 TPM
          left: 660/orginPicture.width,
          top: 693/orginPicture.height,
          width: 351/orginPicture.width,
          height: 64/orginPicture.height,
          url: 'http://10.60.136.207:800/default.html'
        },
        {//10 TQM
          left: 1080/orginPicture.width,
          top: 583/orginPicture.height,
          width: 309/orginPicture.width,
          height: 22/orginPicture.height,
          url: 'http://10.202.22.169/Manager/login.aspx?Page=IPQC/Reports/IPQCAuditWeekReport.aspx&type=grno'
        },
        {//11 PLM
          left: 386/orginPicture.width,
          top: 646/orginPicture.height,
          width: 220/orginPicture.width,
          height: 22/orginPicture.height,
          url: 'http://10.202.22.191:7070/IISDIntegrate/gotoDashboard?role=1'
        },
        {//12 SAP
          left: 690/orginPicture.width,
          top: 21/orginPicture.height,
          width: 358/orginPicture.width,
          height: 42/orginPicture.height,
          url: ''
        },
        {//13 園區
          left: 76/orginPicture.width,
          top: 749/orginPicture.height,
          width: 262/orginPicture.width,
          height: 22/orginPicture.height,
          url: 'http://210.63.218.118:4813/web/pagesecurity.html'
        },
        {//14 3 i
          left: 1080/orginPicture.width,
          top: 561/orginPicture.height,
          width: 309/orginPicture.width,
          height: 22/orginPicture.height,
          url: 'http://10.57.232.169:30000/'
        }
      ]
    }
  },
  computed: {
    innerHeight() {
      return this.$store.getters.mainHeight;
    },
    innerWidth() {
      return this.$store.state.common.windowWidth - 160;
    },
    scaleRate() {
      // console.log('scale =', this.$store.state.common.windowWidth - 160, orginPicture.width, (this.$store.state.common.windowWidth - 160) / orginPicture.width);
      return (this.$store.state.common.windowWidth - 160) / orginPicture.width;
    },
    system_name() {
      // console.log('scale =', this.$store.state.common.windowWidth - 160, orginPicture.width, (this.$store.state.common.windowWidth - 160) / orginPicture.width);
      return this.$store.getters.dataResource["system_name"]||'system_name';
    },
    index_img() {
      // console.log('scale =', this.$store.state.common.windowWidth - 160, orginPicture.width, (this.$store.state.common.windowWidth - 160) / orginPicture.width);
      return this.$store.getters.dataResource["index_img"]||'index_img';
    },

  },
  methods: {
    GetUrl(){     
      let hostname=window.location.hostname;     
      let returnvalue=`https://${hostname}:702`;
      if(this.token){
        returnvalue=`${returnvalue}?token=${this.token}`;
      }
      return  returnvalue;
    },
    createImageURL(imageid,index,ticket){ 
      let  ip=window.location.hostname; 
      let url="http://"+ip+":"+config["img_random_url"]+imageid+".jpg"; 
      this.applicationALL[index]["imgFullPath"]=url; 
      this.$set(this.applicationALL,index,this.applicationALL[index]);  
    },
    dealUrl(item) {
      return item.applicationUrlEntities[0].url + (item.code !== 'Maxbase'? `?token=${this.token}` : '')
    },
    appDownload(imgUrl,appName){
      request({
          url: `/portal-v2/api/web-navigation-api/get-app-download-url-qrcode`,
          params:
          {
            downloadUrl:imgUrl,
            width:260,
            height:260
          }
        }).then(response => {            
            this.dialogTableVisible = true;
            this.imgUrladdress = response.data;
            this.appName = appName;
        })       
    },
    searchClick(){
      //搜索
    },
    tabhandleClick(tab, event){
      //菜单导航点击
      if(tab.index == 0){
        // this.issearchShow = true;
      }else{
        this.issearchShow = false;
      }
    },
    jumptoall(data){
      if(data.navigationType==="applicationGroup"){  
        //this.$store.commit('setcurrentOrgPower', data.applicationGrpList);
         let routeData = this.$router.resolve({path:'/index',query:{id:data.id,groupName:data.webNavigationName}});
         window.open(routeData.href, '_blank');
      }
      else{
        if(data.webUrl){
          request({
            url: `/portal-v2/operation-record/visit-url`,
            method:'post',
            data:
            {
              url:data.webUrl
            }
          })  
        }
        data.webUrl && window.open(data.webUrl);
      }

    },
    jump(url) {
      console.log('jump ==', url);
      url && window.open(url);
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
    productList,
    PopView,
    config    
  }
}
</script>

<style scoped lang="scss">
@import '@/assets/css/_constant.scss';
 .absolute{
          position:absolute;
          right:12px;
          top:21px;          
          z-index:100;
          width: 32px;
          height: 32px;         
          line-height: 32px;
          color: #004279;
          text-align: center;          
          font-size: 24px;
        }
/deep/.product-panel{
  background: #E5E5E0;
  .left-menu{
    width: 160px;
    height: 100%;
    background: $main-color;
    float: left;
    li{
        font-size: 14px;
        color: #fff;
        line-height: 56px;
        padding: 0 20px;
        cursor: pointer;
        .el-icon-ex-overview{
          font-size: 24px;
          margin-right: 10px;
          position: relative;
          top: 3px;
        }
        .el-icon-arrow-right{
          float: right;
          line-height: 56px;
        }
        &:hover, &.active{
          background: #004279;
        }
      }
    }
    .right-content{
      overflow: hidden;
      float: left;
      .container{
        background: #091e3d;
        .overview{
          position: relative;
          img{
            display: block;
            width: 100%;
          }
          .product-links li{
            position: absolute;
            // border: 1px solid red;
            cursor: pointer;
          }
        }
      }
      .product-content{ 
        //  position:relative;
       
        //菜单属性自定义
        .el-tabs__header{          
          background-color:#E5E5E0;        
          padding: 15px 0 0 15px;
          // border:1px solid rgba(208,208,199,1);
          // width: 50%;
          .el-tabs__item{//未选中
              border:1px solid rgba(208,208,199,1);
              font-size:14px;
              font-family:MicrosoftYaHei;
              color:rgba(74,74,74,1.0);
          }
          .is-active{//选中
            background:rgba(255,255,255,1);
            color:rgb(12, 134, 235);
            border-radius:4px 0px 0px 4px;
            border:1px solid rgba(208,208,199,1);
            font-weight: bold;
          }
        }

        //弹出二维码控件属性自定义
        .el-dialog {
          height: 400px;
          .el-dialog__body {
            padding: 0px;
          }
        }
        .el-tabs{
          background-color: #E5E5E0;
        }
        //搜索
        .searchInput{
          position: absolute;
          right: 120px;
          top: 10px;
          width:220px;
          height:40px;
        }
        .searchBtn{
          position: absolute;
          right: 24px;
          top: 10px;
          width:80px;
          height:40px;
          background:rgba(16,90,152,1);
          border-radius:5px;
          font-size:12px;
          font-family:MicrosoftYaHei;
          color:rgba(255,255,255,1);
          padding: 9px 20px;
          border: rgba(16,90,152,1) solid 0px;
          // margin: 10px;
        }
        .el-button{
          border: rgb(224, 224, 224) solid 1px;
          color: #036bc0;
          margin-bottom: 5px;
        }
        .downloadbtn{
          padding: 0px 10px;
          position: absolute;
          top: 0px;
          right: 0px;
          height: 122px;
          width: 80px;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items:center;
        }
        h3{
          color: #4A4A4A;
          margin: 10px 0;
          font-size: 14px;
        }
        .product-list{
          li{           
            background: #fff;
            width: 440px;
            height: 120px;
            border:1px solid rgba(208,208,199,1);
            float: left;
            margin-right: 24px;
            margin-bottom: 24px;
            cursor: pointer;
            position: relative;
            .in-development{              
              cursor: auto;
              background:rgba(24,24,24,0.5);
              display: block;
              width: 440px;
              height: 120px;
              position: absolute;
              top: 0;
              left: 0;
              color: #fff;
              font-size: 16px;
              line-height: 120px;
              text-align: center;              
            }
            img{
              display: block;
              width: 80px;
              height: 80px;
              margin: 20px;
              float: left;
            }
            h4{
              color: #004279;
              font-size: 20px;
              margin-top: 40px;
            }
            p{
              font-size: 12px;
              color: #4A4A4A;
              line-height: 24px;
              width: 353px;
            }            
          }
        }
      }
    }
 }
</style>