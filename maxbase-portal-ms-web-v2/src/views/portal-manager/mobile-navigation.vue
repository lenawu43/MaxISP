<template>
  <article v-show="elementPermission['USER_MANAGER:BROWSE']">
    <div>      
      <article class="inner-content">
        <handle-data
          v-show="!isEdit"
          ref="mainData"
          :pageName="'mobile导航'"
          :searchText="searchText"
          :apiPath="'portal-v2/mobile-navigation'"
          :apiDef="{search: '/portal-v2/mobile-navigation/search'}"
          :dataStructure="dataStructure"
          :tableOperate="tableOperate"
          :elementPermission="elementPermission"
          @handleBtn="handleBtn"
        >
          <span slot="top-buttons">
                <el-button 
                    class="filter-item" icon="el-icon-plus" size="mini" plain
                    v-show="elementPermission['USER_MANAGER:ADD']" 
                    @click="handleClickAdd"
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
                    <el-form-item :label="langText.mobileManager.mobileNavigationCode" prop="mobileNavigationCode" >
                        <el-input v-model="formData.mobileNavigationCode" :disabled="editdisabled"></el-input>
                    </el-form-item>           
              </el-col>
              <!-- <el-col style="visibility: hidden;" :span="2">1</el-col> -->
              <el-col :span="12">
                <el-form-item :label="langText.mobileManager.mobileNavigationName"  prop="mobileNavigationName">
                        <el-input v-model="formData.mobileNavigationName"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
                <el-col :span="12">
                    <el-form-item :label="langText.mobileManager.businessTypeName" prop="businessTypeId" >
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
                <el-form-item :label="langText.mobileManager.order" prop="order" >
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
              <!-- <el-col :span="12"  >
                <el-form-item :label="langText.mobileManager.applicationCode" prop="applicationCode" >
                      <el-input v-model="formData.applicationCode"></el-input>  
                </el-form-item>
              </el-col> -->
              <!-- <el-col style="visibility: hidden;" :span="2">1</el-col> -->
              <!-- <el-col :span="12">  
                <el-form-item :label="langText.mobileManager.description" prop="description" >
                  <el-input v-model="formData.description"></el-input>    
                 </el-form-item>          
              </el-col> -->
              <el-form-item :label="langText.mobileManager.description" prop="description" >
                <el-input v-model="formData.description"></el-input>    
                </el-form-item>  
            </el-row>  
           <el-row>  
             
             <el-form-item :label="langText.mobileManager.iconPath"  prop="iconPath">
                        <!-- <el-input v-model="formData.iconPath" v-show="false"></el-input> -->
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
         </el-row>          
           <!-- web导航 -->
            <el-form-item :label="langText.mobileManager.urlScheme" v-if="false" prop="urlScheme">
               <el-input v-model="formData.urlScheme"></el-input>
            </el-form-item> 
           <el-form-item :label="langText.mobileManager.URL" prop="url" >
               <el-input v-model="formData.url"></el-input>
            </el-form-item>    
           
           <!-- web导航 -->  
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
import axios from 'axios'
import handleData, { handleTableBtn } from "@/components/handleData";
import searchInput from "@/components/search-input";
import serviceDialog from "@/components/service-dialog";
import request from "@/utils/request";
import { valid } from "@/utils/validate";
import applicationGroupManagerVue from './application-group-manager.vue';
import config from '@/hosts/config'

export default {  
  created() { 
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
    // this.tableOperate.push(
    //   {
    //     title: this.langText.form.edit,
    //     icon: "element-ex-icons el-icon-ex-edit",
    //     methodName: "handleAppUpdate"
    //   },
    //   {
    //     title: this.langText.form.delete,
    //     icon: "element-ex-icons el-icon-ex-delete",
    //     methodName: "handleDelete"
    //   }
    // );
  },
  data() { 
    let langText = {
      form: this.$t("form"),
      field: this.$t("field"),
      common: this.$t("common"),      
      function: this.$t("function"),
      mobileManager:this.$t("mobileManager")     
    };

   var checkmobileNavigationCode = (rule, value, callback) => {  
      if (!value || this.$trim(value) === '') {
        callback();
      }
      else{
         request({
          url: `/portal-v2/mobile-navigation/unique`+(this.formData.id? '/' + this.formData.id : ''),
          params:{
            mobileNavigationCode:this.$trim(value)            
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
          callback();
        }).catch(err => {
          callback(err);
        });
      }
    };

    var checkApplictionCode = (rule, value, callback) => {  
      if (!value || this.$trim(value) === '') {
        callback();
      }
      else{
         request({
          url: `/portal-v2/mobile-navigation/unique`+(this.formData.id? '/' + this.formData.id : ''),
          params:{
            applicationCode:this.$trim(value)
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
     
     
   
    return {      
      elementPermission: {
        "USER_MANAGER:BROWSE": true,
        "USER_MANAGER:ADD": true,
        "USER_MANAGER:EDIT": true,
        "USER_MANAGER:DELETE": true       
      },  
      imageUrl:"",
      loading:{},
      hasFile:false,
      Ticket:"",    
      isEdit: false,
      buttonStatus:'',
      editdisabled:false,     
      langText,
      businessTypeData:[],//业务分类
      orderNoData:[],      
      searchText: `${langText.form.searchText}(${langText.mobileManager.mobileNavigationName},${langText.mobileManager.description},${langText.mobileManager.applicationCode})`,   
      dataStructure: [
        {
          field: "mobileNavigationCode",
          label: langText.mobileManager.mobileNavigationCode
        },
        {
          field: "mobileNavigationName",
          label: langText.mobileManager.mobileNavigationName
        },
        {
          field: "description",
          label: langText.mobileManager.description
        },
        {
          field: "imageFullPath",
          label: langText.mobileManager.iconPath,
          type:"image",
          width:"70"
        },
        {
          field: "businessTypeName",
          label: langText.mobileManager.businessTypeName
        },         
        {
          field: "url",
          label: langText.mobileManager.URL
        },
        {
          field: "urlScheme",
          label: langText.mobileManager.urlScheme
        }
            
      ],
      tableOperate: [],
      rules: {
        mobileNavigationCode: [
          {
            required: true,
            message: langText.form.required,
            trigger: "blur"
          },
          { min: 1, max: 50, message: this.$filterString(langText.form.rangeLength, {min: 1, max: 50}), trigger: 'blur' },
          {
            required: true, validator: checkmobileNavigationCode, trigger: 'blur'
          }         
        ],
        mobileNavigationName: [
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
        url:[           
          {
            required: true,
            message: langText.form.required,
            trigger: "blur"
          },
          { min: 0, max: 500, message: this.$filterString(langText.form.rangeLength, {min: 0, max: 500}), trigger: 'blur' },
          {
              validator: validurl,        
              trigger: "blur"              
          }           
       
       ],
        urlScheme:[           
          { min: 0, max: 500, message: this.$filterString(langText.form.rangeLength, {min: 0, max: 500}), trigger: 'blur' }   
        ],        
         order:[           
          {
            required: true,
            message: langText.form.pleaseSelect,
            trigger: "change"
          }
         ],
          applicationCode:[  
          { min: 0, max: 100, message: this.$filterString(langText.form.rangeLength, {min: 0, max: 100}), trigger: 'blur' },         
          {
            validator: checkApplictionCode, trigger: 'blur',              
          }               
          ],
          description:[  
          { min: 0, max: 100, message: this.$filterString(langText.form.rangeLength, {min: 0, max: 100}), trigger: 'blur' }   
          ],
          iconPath:[            
            {
              required: true,
              message: langText.form.pleaseSelect    
            }  
          ]                  
      },
      formData: {
             
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
      this.buttonStatus="add";
      this.hasFile=false;
      this.isEdit = true;
      this.imageUrl="";
      //this.$set(this,'addType', type);
      this.editdisabled=false;    
      this.$refs['mainForm'].clearValidate();
      this.$refs.upload.clearFiles();  
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
              url: "/portal-v2/mobile-navigation" + id,
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
          
      };
    },    
    handleAppUpdate(data) { 
      debugger      
      request({
        url: `/portal-v2/mobile-navigation/${data.id}`
      }).then(response => { 
         this.formData = response.data; 
         this.buttonStatus="update";
         this.hasFile=false;
         this.imageUrl="";
         if(this.formData.iconPath){
           
                this.Ticket=response.data;
               this.Ticket=response.data;              
              let  ip=window.location.hostname; 
              this.imageUrl="http://"+ip+":"+config["img_random_url"]+this.formData.iconPath+".jpg";             
           
         }     
         this.isEdit = true;  
         this.editdisabled=true;        
         this.$refs['mainForm'].clearValidate();  
         this.$refs.upload.clearFiles();                  
         this.initorderNoData(this.formData.businessTypeId); 
      });       
    },
    handleCancel() {
      // this.listQuery.page = 1;
      // this.listQuery.keywords = '';
      this.dataInit();
      this.isEdit = false;      
    },  
    initDropDownList(){
      //业务分类
      if(this.businessTypeData.length==0){
        request({
          url: `/portal-v2/business-type/get-business-type-list-by-platform`,
          params:{
            platform:'mobile'
          }
        }).then(response => {            
            this.businessTypeData=response.data;      
        })
      }   
    },
    initorderNoData(type){      
       this.orderNoData=[];
       if(type){
         request({
          url: `/portal-v2/mobile-navigation/query-order-no/${type}`,
          params:{
            type:this.buttonStatus,
            mobileNavigationId:this.formData.id?this.formData.id:""
          }
         }).then(response => {            
            this.orderNoData=response.data;      
         })
      }
    },
    businessTypeChange(){ 
      //this.formData.order="";
      this.$set(this.formData,"order","");      
      this.initorderNoData(this.formData.businessTypeId);      
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
      //this.formData.iconPath=suffix;
      this.$refs['mainForm'].clearValidate('iconPath'); 
      fileList.splice(0,fileList.length);//清空数组
      if(file){
         fileList.push(file);
         this.imageUrl=URL.createObjectURL(file.raw);;
      }

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
                return request({
                  url: "/portal-v2/mobile-navigation" + id,
                  method,
                  data: this.formData
                }).then(result=>{
                  this.dataInit();
                  this.$refs.mainData.getList();
                });
          })
    }
   },
  components: {
    handleData,
    serviceDialog,
    searchInput    
  }
};
</script>
<style scoped lang="scss">
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



