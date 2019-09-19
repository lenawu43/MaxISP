<template>
  <article v-show="elementPermission['USER_MANAGER:BROWSE']">
    <div>
      <article class="inner-content">
        <handle-data
          v-show="!isEdit"
          ref="mainData"
          :pageName="'web导航'"
          :searchText="searchText"
          :apiPath="'portal-v2/web-navigation'"
          :apiDef="{search: '/portal-v2/web-navigation/searchWebNavigation'}"
          :dataStructure="dataStructure"
          :tableOperate="tableOperate"
          :elementPermission="elementPermission"
          @handleBtn="handleBtn"
        >
          <span slot="top-buttons">
                <el-button 
                    class="filter-item" icon="el-icon-plus" size="mini" plain
                    v-show="elementPermission['USER_MANAGER:ADD']" 
                    @click="dropdownchange('navigation')"
                    >{{langText.form.add}}
                </el-button>
                
                      
          </span>
        </handle-data>
        <div v-show="isEdit">
          <h3 class="page-title clearfix">
            {{langText.common.basicInfo}}
            <div style="margin-bottom: 10px; float: right;">
              <el-button
                @click="handleCancel"
                icon="el-icon-close"
                size="mini"
                plain
              >{{langText.form.cancel}}</el-button>
              <el-button
                @click="handleSubmit"
                icon="el-icon-check"
                size="mini"
                type="primary"
                plain
              >{{langText.form.save}}</el-button>
            </div>
          </h3>
          <el-form :model="formData" ref="mainForm" label-width="150px" :rules="rules">
             <el-row>
                <el-col :span="12">
                    <el-form-item :label="langText.webManager.webNavigationCode" prop="webNavigationCode" >
                        <el-input v-model="formData.webNavigationCode" :disabled="editdisabled"></el-input>
                    </el-form-item>           
              </el-col>
              <!-- <el-col style="visibility: hidden;" :span="2">1</el-col> -->
              <el-col :span="12">
                <el-form-item :label="langText.webManager.webNavigationName"  prop="webNavigationName">
                        <el-input v-model="formData.webNavigationName"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
                <el-col :span="12">
                    <el-form-item :label="langText.webManager.businessTypeName" prop="businessTypeId" >
                        <el-select
                            v-model="formData.businessTypeId"
                            :placeholder="langText.form.pleaseSelect"
                            style="width: 100%;"  
                             @change="businessTypeChange"                         
                            >
                            <el-option
                            v-for="(item,index) in businessTypeData"
                            :key="index"
                            :label="item.businessTypeName"
                            :value="item.id"
                            ></el-option>
                        </el-select>
                    </el-form-item>           
              </el-col>
              <!-- <el-col style="visibility: hidden;" :span="2">1</el-col> -->
              <el-col :span="12">
                <el-form-item :label="langText.webManager.order" prop="order" >
                        <el-select
                            v-model="formData.order"
                            :placeholder="langText.form.pleaseSelect"
                            style="width: 100%;"                           
                            >
                            <el-option
                            v-for="(item,index) in orderNoData"
                            :key="index"
                            :label="item.description"
                            :value="item.order"
                            ></el-option>
                        </el-select>
                    </el-form-item>          
              </el-col>
            </el-row>      
            <el-row>
                <el-col :span="12">
                    <el-form-item :label="langText.webManager.status" prop="status">
                        <el-select
                            v-model="formData.status"
                            :placeholder="langText.form.pleaseSelect"
                            style="width: 100%;"                           
                            >
                            <el-option
                            v-for="(item,index) in statusdata"
                            :key="index"
                            :label="item.typeDescription"
                            :value="item.type"
                            ></el-option>
                        </el-select>
                    </el-form-item>           
              </el-col>
              <!-- <el-col style="visibility: hidden;" :span="2">1</el-col> -->
              <el-col :span="12">                
              </el-col>
            </el-row>   
            <el-form-item :label="langText.webManager.description" prop="description">
                        <el-input v-model="formData.description"></el-input>
            </el-form-item>    
             <el-form-item :label="langText.webManager.iconPath"  prop="iconPath" >                         
                <el-upload
                ref="upload"
                :http-request="uploadRequest"
                :on-change="uploadhandleChange"                      
                class="avatar-uploader"
                action=""
                accept="image/jpeg,image/jpg,image/png,img/bmp"
                :auto-upload="false"                                        
                :show-file-list="false"                            
                >                 
                <img v-if="imageUrl" :src="imageUrl" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>            
            </el-form-item>            
              
           <el-form-item :label="langText.webManager.webUrl" prop="webUrl" v-if="addType=='navigation' || addType=='application'" >
               <el-input v-model="formData.webUrl"></el-input>
            </el-form-item>    
            <el-form-item :label="langText.webManager.appDownloadUrl" prop="appDownloadUrl" v-if="addType=='navigation' || addType=='application' " >
               <el-input v-model="formData.appDownloadUrl"></el-input>
            </el-form-item> 

            <el-form-item :label="langText.webManager.applicationCode"  prop="applicationCode" v-if="addType=='application'">
                <el-input v-model="formData.applicationCode"></el-input>
            </el-form-item> 
           
           
                <app-group-manager  ref="appgroup"  :commonPermision="elementPermission" :tableData.sync="formData.applicationGrpList" v-if="addType=='applicationGroup'">              
                </app-group-manager>
                 
            <!-- 应用组导航 -->       
            <el-row>
              <el-col :span="12">
                <el-form-item :label="langText.field.createdBy">
                  <el-input type="text" :disabled="true" v-model="formData.createdBy" ></el-input>
                </el-form-item>
              </el-col>              
              <el-col :span="12">
                <el-form-item :label="langText.field.createdDate">
                  <el-input type="text" :disabled="true" v-model="createdDate"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <el-form-item :label="langText.field.lastModifiedBy">
                  <el-input type="text" :disabled="true" v-model="formData.lastModifiedBy"></el-input>
                </el-form-item>
              </el-col>              
              <el-col :span="12">
                <el-form-item :label="langText.field.lastModifiedDate">
                  <el-input type="text" :disabled="true"   v-model="lastModifiedDate "></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </article>
    </div>
  </article>
</template>

<script>
import handleData, { handleTableBtn } from "@/components/handleData";
import searchInput from "@/components/search-input";
import serviceDialog from "@/components/service-dialog";
import appGroupManager from "./application-group-manager";
import request from "@/utils/request";
import { valid } from "@/utils/validate";
import applicationGroupManagerVue from './application-group-manager.vue';
import config from '@/hosts/config'

export default {  
  created() {  
    // this.$setElementPermission(element => {
    //   // 处理表格按钮权限  
    //   let btnData = handleTableBtn(element, {
    //     EDIT: {
    //       title: this.langText.form.edit,
    //       icon: 'element-ex-icons el-icon-ex-edit',
    //       methodName: 'handleAppUpdate'
    //     },
    //     DELETE: {
    //       title: this.langText.form.delete,
    //       icon: 'element-ex-icons el-icon-ex-delete',
    //       methodName: 'handleDelete'
    //     }
    //   });
    //   btnData && this.tableOperate.push(btnData);
    // }).then(data => {
    //   this.elementPermission = data;
    // });

    this.initDropDownList();
    this.tableOperate.push(
      {
        title: this.langText.form.edit,
        icon: "element-ex-icons el-icon-ex-edit",
        methodName: "handleAppUpdate"
      },
      {
        title: this.langText.form.delete,
        icon: "element-ex-icons el-icon-ex-delete",
        methodName: "handleDelete"
      }
    );
  },
  data() { 
    let langText = {
      form: this.$t("form"),
      field: this.$t("field"),
      common: this.$t("common"),      
      webManager:this.$t("webManager"),
      valid: this.$t("valid")     
    };
   
    var checkwebNavigationCode = (rule, value, callback) => {  
      
      if (!value || this.$trim(value) === '') {
        callback();
      }
      else{
         request({
          url: `/portal-v2/web-navigation/unique`+(this.formData.id? '/' + this.formData.id : ''),
          params:{
            webNavigationCode:value            
          }
        }).then(response => {            
            if(response.data === false){
              callback(langText.form.dataRepeat)
            }else{
              callback()
            }     
        })    
      }
    };

    var validurl = (rule, value, callback) => {    
      if (!value || this.$trim(value) === '') {
        callback();
      }
      else{
        valid('url', this.$trim(value)).then(() => {
          this.$refs['mainForm'].clearValidate('webUrl'); 
          this.$refs['mainForm'].clearValidate('appDownloadUrl');
          callback();
        }).catch(err => {
          callback(err);
        });
      }
    };

    var checkSpecialChar = (rule, value, callback) => { 
        if (!value || this.$trim(value) === '') {
          callback();
        }
        else{
          let regEn = /[`~!@#$%^&*()=+<>?:"{},.\/;'[\]]/im,
          regCn = /[·！#￥（=）：；“”‘、，|《。》？、【】[\]]/im;
          if(regEn.test(value) || regCn.test(value)) {
              callback(langText.valid.specialChar);             
          }
          else{
            callback();
          }
        }
    }
  

    var checkApplictionCode = (rule, value, callback) => {  
      
      if (!value || this.$trim(value) === '') {
        callback();
      }
      else{
         request({
          url: `/portal-v2/web-navigation/unique`+(this.formData.id? '/' + this.formData.id : ''),
          params:{
            applicationCode:value
          }
        }).then(response => {            
            if(response.data === false){
              callback(langText.form.dataRepeat)
            }else{
              callback()
            }     
        })    
      }
    };

    var checkTwoRequre = (rule, value, callback) => { 
      let  webUrl=!this.formData.webUrl || this.$trim(this.formData.webUrl)=='';
      let  appDownloadUrl=!this.formData.appDownloadUrl || this.$trim(this.formData.appDownloadUrl)=='';
      if(webUrl && appDownloadUrl){
        this.$refs['mainForm'].clearValidate('webUrl'); 
        this.$refs['mainForm'].clearValidate('appDownloadUrl');
        callback(`${langText.webManager.webUrl},${langText.webManager.appDownloadUrl} 必填一个`);
      }
      else{
         callback();      
      }
    };


    
   
    return {      
      elementPermission: {
        "USER_MANAGER:BROWSE": true,
        "USER_MANAGER:ADD": true,
        "USER_MANAGER:EDIT": true,
        "USER_MANAGER:DELETE": true       
      },   
      imageUrl:"",
      hasFile:false,
      Ticket:'',   
      isEdit: false,
      editdisabled:false,
      addType:'',
      langText,
      buttonStatus:'',
      businessTypeData:[],//业务分类
      orderNoData:[],
      statusdata:[],
      navigationTypeData:[],//导航类型
      searchText: `${langText.form.searchText}(${langText.webManager.webNavigationName},${langText.webManager.description},${langText.webManager.applicationCode})`,   
      dataStructure: [
        {
          field: "webNavigationCode",
          label: langText.webManager.webNavigationCode
        },
        {
          field: "webNavigationName",
          label: langText.webManager.webNavigationName
        },
        {
          field: "description",
          label: langText.webManager.description
        },
        {
          field: "imageFullPath",
          label: langText.webManager.iconPath,
          type:"image",
          width:"70"
        },
        {
          field: "businessTypeName",
          label: langText.webManager.businessTypeName
        },        
        {
          field: "navigationTypeName",
          label: langText.webManager.navigationTypeName
        },
        {
          field: "webUrl",
          label: langText.webManager.webUrl
        },
        {
          field: "appDownloadUrl",
          label: langText.webManager.appDownloadUrl
        }
      ],
      tableOperate: [],
      rules: {
        webNavigationCode: [
          {
            required: true,
            message: langText.form.required,
            trigger: "blur"
          },
          { min: 1, max: 50, message: this.$filterString(langText.form.rangeLength, {min: 1, max: 50}), trigger: 'blur' }, 
          {
             validator: checkwebNavigationCode, trigger: 'blur'
          }          
        ],               
        webNavigationName: [
          {
            required: true,
            message: langText.form.required,
            trigger: "blur"
          },
          { min: 1, max: 50, message: this.$filterString(langText.form.rangeLength, {min: 1, max: 50}), trigger: 'blur' }           
        ],
        businessTypeId:[
          {
            required: true,
            message: langText.form.pleaseSelect,
            trigger: "change"
          }
        ],
        status:[
          {
            required: true,
            message: langText.form.pleaseSelect,
            trigger: "change"
          }
        ],
        description:[  
          { min: 0, max: 100, message: this.$filterString(langText.form.rangeLength, {min: 0, max: 100}), trigger: 'blur' }   
        ],        
        webUrl:[  
          { min: 0, max: 500, message: this.$filterString(langText.form.rangeLength, {min: 0, max: 500}), trigger: 'blur' },
          {
              validator: validurl,        
              trigger: "blur"              
          }, 
          {
             validator: checkTwoRequre, trigger: 'blur'
          }  
        ],
        appDownloadUrl:[           
          { min: 0, max: 500, message: this.$filterString(langText.form.rangeLength, {min: 0, max: 500}), trigger: 'blur' },   
          {
             validator: checkTwoRequre, trigger: 'blur'
          }  
        ],
        applicationCode: [   
          { min: 0, max: 100, message: this.$filterString(langText.form.rangeLength, {min: 0, max: 100}), trigger: 'blur' },         
          {
             validator: checkSpecialChar, trigger: 'blur'
          },  
          {
             validator: checkApplictionCode, trigger: 'blur'
          }          
        ],
         iconPath:[            
            {
              required: true,
              message: langText.form.pleaseSelect    
            }  
          ],
         order:[           
          {
            required: true,
            message: langText.form.pleaseSelect,
            trigger: "change"
          }
         ]        
        
      },
      formData: {
        applicationGrpList:[]        
      },
      extendTableNameIdSourse:{options:[]},
      dialogVisible: false,
      adminUserData: [],
      listQuery: {
        page: 1,
        limit: 10
      },
      total: 1,
      selectRowData: {}
    };
  },
  computed: {
    createdDate() {
      return this.$formatDate(this.formData.createdDate);
    },
    lastModifiedDate() {
      return this.$formatDate(this.formData.lastModifiedDate);
    }
  },
  methods: {  
    handleBtn(data, opts) { 
      this[opts.item.methodName](data);
    },
    handleClickAdd() {
     
    },
    changeKeyword(keywords) {
      this.listQuery.page = 1;
      this.listQuery.keywords = keywords;      
    },
    currentChange(currentRow) {
      this.selectRowData = currentRow;
    },
    handleSubmit() {      
      // console.log(this.formData)      
      this.formData.navigationType=this.addType;      
      if(this.addType=="applicationGroup"){
        this.formData.applicationGrpList=this.$refs.appgroup.newtableData;
      }
      this.$submitProcess({
        form: this.$refs.mainForm,
        formData: this.formData,
        getPromise: () => {          
          this.isEdit = false;          
          let id = "",
            method = "POST";
          if (this.formData.id) {
            id = "/" + this.formData.id;
            method = "PUT";
          }

          if(this.hasFile){
           this.$refs.upload.submit();      
            
          }
          else{
          // this.$refs.mainForm.clearValidate();
            return request({
              url: "/portal-v2/web-navigation" + id,
              method,
              data: this.formData
            });
          }
        },
        success: response => {
          this.dataInit();
          this.$refs.mainData.getList();
        }
      });
    },
    dataInit() {
      this.listQuery.page = 1;
      this.listQuery.keywords = "";
      this.formData = {
          applicationGrpList:[]  
      };
    },    
    handleAppUpdate(data) {       
      request({
        url: `/portal-v2/web-navigation/${data.id}`
      }).then(response => {           
         this.buttonStatus="update";
         this.hasFile=false;
         this.imageUrl="";
         this.formData = response.data; 
         
          if(this.formData.iconPath){
              this.Ticket=response.data;              
              let  ip=window.location.hostname;                
              this.imageUrl="http://"+ip+":"+config["img_random_url"]+this.formData.iconPath+".jpg"; 
           
          }

         if(this.formData.navigationType=="applicationGroup"){   
           debugger      
          request({
            url: `/portal-v2/application-group-item/get-list-by-web-navigation-id`,
            params:{
                webNavigationId:data.id
            }
            }).then(response => {  
              response.data=response.data.map((item,index)=>{
                 item.order=index;
                 return item;
              });
              this.formData.applicationGrpList=response.data;
              this.isEdit = true;  
              this.editdisabled=true;               
              this.$refs['appgroup'].selectIndex=-1; 
            });
         } 
         else{
              this.isEdit = true;  
              this.editdisabled=true;
         }
        this.$refs['mainForm'].clearValidate();      
         
        this.addType=this.formData.navigationType;        
        this.initorderNoData(this.formData.businessTypeId); 
      });       
    },
    handleCancel() {
      // this.listQuery.page = 1;
      // this.listQuery.keywords = '';
      this.dataInit();
      this.isEdit = false;      
    },
    dropdownchange(type){  
      this.buttonStatus="add";
      this.imageUrl="";
      this.hasFile=false;
      this.isEdit = true;
      //this.$set(this,'addType', type);
      this.editdisabled=false;        
      this.addType=type;
      this.$refs['mainForm'].clearValidate();    
      this.orderNoData=[];  
      if(type=="applicationGroup"){
         this.$nextTick(() => {
              this.$refs['appgroup'].selectIndex=-1;        
          });  
      }      
    },    
    initDropDownList(){
      //业务分类
      if(this.businessTypeData.length==0){
        request({
          url: `/portal-v2/business-type/get-business-type-list-by-platform`,
          params:{
            platform:'web'
          }
        }).then(response => {            
            this.businessTypeData=response.data;      
        })
      } 

      if(this.navigationTypeData.length==0){
        request({
          url: `/portal-v2/web-navigation/get-dict-by-type`,
          params:{
            type:'navigationType'
          }
        }).then(response => {                         
            this.navigationTypeData=response.data;      
        })
      } 
      //状态
      if(this.statusdata.length==0){
          request({
          url: `/portal-v2/web-navigation/get-dict-by-type`,
          params:{
            type:'status'
          }
        }).then(response => {        
            this.statusdata=response.data;
        }); 
      } 
     
    },
    initorderNoData(type){      
       this.orderNoData=[];
       if(type){
         request({
          url: `/portal-v2/web-navigation/query-order-no/${type}`,
          params:{
            type:this.buttonStatus,
            webNavigationId:this.formData.id?this.formData.id:""
          }
         }).then(response => {            
            this.orderNoData=response.data;      
         })
      }
    },
    businessTypeChange(){ 
      //this.formData.order_no="";
      this.$set(this.formData,"order",""); 
      this.initorderNoData(this.formData.businessTypeId);
    },
    uploadRequest(fileObject){            
      let data = new FormData();//重点在这里 如果使用 var data = {}; data.inputfile=... 这样的方式不能正常上传
      data.append("file",fileObject.file);       
       request({
            url: '/portal-v2/fileUpload/upload',
            headers: { 
              "Content-Type": "multipart/form-data",
               "Ticket":this.Ticket,                   
            },           
            timeout:60000,
            data:data,
            method: 'post'   
          }).then(response => {
                debugger
                let id = "",method = "POST"; 
                this.formData.iconPath=response.data;
                if (this.formData.id) {
                  id = "/" + this.formData.id;
                  method = "PUT";
                }
                request({
                  url: "/portal-v2/web-navigation" + id,
                  method,
                  data: this.formData
                }).then(result=>{
                  this.dataInit();
                  this.$refs.mainData.getList();
                });
          })
    },
    uploadhandleChange(file, fileList){  
        var suffix = file.name.substr(file.name.lastIndexOf("."));
        suffix = suffix.toLowerCase();       
        if(suffix != ".jpeg" && suffix != ".bmp" && suffix != ".png" && suffix != ".jpg"){         
          this.$message.error('上傳錯誤：文件格式只能是 jpeg,bmp,png,jpg');
          fileList.splice(0,fileList.length);//清空数组 
          return;
        }
        if(file.size/1024/1024 >=1){
          //文件过大 
          this.upload_disabled = true;
          this.$message.error('上傳錯誤：文件大小不可超過1M'); 
          fileList.splice(0,fileList.length);//清空数组
          return;
        }
        //   this.loading = this.$loading({
        //   lock: true,
        //   text: 'Loading',
        //   spinner: 'el-icon-loading',
        //   background: 'rgba(0, 0, 0, 0.7)',
        //   target:document.body,
        //   fullscreen:false
        // });
      this.hasFile=true;  
      this.$set(this.formData,"iconPath", suffix);       
      this.$refs['mainForm'].clearValidate('iconPath'); 
      fileList.splice(0,fileList.length);//清空数组
      if(file){
         fileList.push(file);
         this.imageUrl=URL.createObjectURL(file.raw);;
      }

    },    
   },
  components: {
    handleData,
    serviceDialog,
    searchInput,
    appGroupManager
  }
};
</script>

<style scoped lang="scss">
.user-button {
  float: right;
  margin-top: 10px;
}
.el-dialog__title {
  line-height: 32px;
}

/deep/ .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;//#d9d9d9
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }

 /deep/ .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
 /deep/ .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
 /deep/ .avatar {
    width: 178px;
    height: 178px;
    display: block;
    cursor: pointer;
  }
 /deep/ .el-form-item__content{
   line-height: 0px;
 }
  
</style>


