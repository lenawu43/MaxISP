<template>
  <article v-show="elementPermission['USER_MANAGER:BROWSE']">
    <div>      
      <article class="inner-content">
        <handle-data
          v-show="!isEdit"
          ref="mainData"
          :pageName="'个性化配置'"
          :searchText="searchText"
          :apiPath="'portal-v2/custom-resource'"
          :apiDef="{search: '/portal-v2/custom-resource/searchCustomResource'}"
          :dataStructure="dataStructure"
          :tableOperate="tableOperate"
          :elementPermission="elementPermission"
          @handleBtn="handleBtn"
        >
          <span slot="top-buttons">
                 <!-- <el-dropdown v-show="elementPermission['USER_MANAGER:ADD']" @command="dropdownchange">
                <el-button 
                    class="filter-item" icon="el-icon-plus" size="mini" plain
                    v-show="elementPermission['USER_MANAGER:ADD']" 
                    >{{langText.form.add}}
                </el-button>
                <el-dropdown-menu slot="dropdown">                    
                    <el-dropdown-item
                    v-for="(item,index) in [{type:'text',typeDescription:'文字'},{type:'image',typeDescription:'图片'}]"
                            :key="index"                            
                     :command="item.type">{{item.typeDescription}}</el-dropdown-item>                                       
                </el-dropdown-menu>
                </el-dropdown>     
                -->

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
                    <el-form-item :label="'关键字'"   prop="keyWord">
                        <el-input v-model="formData.keyWord" :disabled="editdisabled"></el-input>
                    </el-form-item>           
              </el-col>
              <!-- <el-col style="visibility: hidden;" :span="2">1</el-col> -->
              <el-col :span="12">
                <el-form-item :label="'关键值'"  prop="keyValue" v-if="addType=='text'">
                        <el-input v-model="formData.keyValue" ></el-input>
                    </el-form-item>  
              </el-col>
            </el-row>
            <el-row>
                <el-form-item :label="'图片'"  v-if="addType=='image'"  prop="iconPath">                         
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
            <el-row>
              <el-form-item :label="'描述'" prop="description">
                  <el-input type="text"  :disabled="true" v-model="formData.description" ></el-input>
                </el-form-item>
            </el-row>
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
import config from '@/hosts/config'

import applicationGroupManagerVue from './application-group-manager.vue';
var validsignlessInteger = (rule, value, callback) => {  
      if (value === '') {
        callback();
      }
      valid('number', value).then(() => {
        callback();
      }).catch(err => {
        callback(err);
      });
    };

export default {  
  created() { 
     this.tableOperate.push(
      {
        title: this.langText.form.edit,
        icon: "element-ex-icons el-icon-ex-edit",
        methodName: "handleAppUpdate"
      },
    //   {
    //     title: this.langText.form.delete,
    //     icon: "element-ex-icons el-icon-ex-delete",
    //     methodName: "handleDelete"
    //   }
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

   var checkkeyWord = (rule, value, callback) => {  
      if (!value || this.$trim(value) === '') {
        callback();
      }
      else{  
         request({
          url: `/portal-v2/custom-resource/unique`+(this.formData.id? '/' + this.formData.id : ''),
          params:{
            keyWord:this.formData.keyWord           
          }
        }).then(response => { 
              callback();            
        }).catch(error=>{            
              if(error.response.status===409){
                callback(langText.form.dataRepeat);
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
      loading:{},       
      isEdit: false,     
      editdisabled:false,     
      langText, 
      addType:'',
      imageUrl:'',   
      iconPath:'',
      hasFile:false,
      searchText: `${langText.form.searchText}(关键字,关键值,类型,描述)`,   
      dataStructure: [
        {
          field: "keyWord",
          label: '关键字'
        },
        {
          field: "keyValue",
          label: '关键值'
        },
        {
          field: "type",
          label: '类型'
        },
        {
          field: "description",
          label: '描述'
        }

      ],
      tableOperate: [],
      rules: {
        keyWord: [
          {
            required: true,
            message: langText.form.required,
            trigger: "blur"
          },
          { min: 1, max: 50, message: this.$filterString(langText.form.rangeLength, {min: 1, max: 50}), trigger: 'blur' },
          {
            required: true, validator: checkkeyWord, trigger: 'blur'
          }         
        ],
        keyValue: [
          {
            required: true,
            message: langText.form.required,
            trigger: "blur"
          },
          { min: 1, max: 50, message: this.$filterString(langText.form.rangeLength, {min: 1, max: 50}), trigger: 'blur' }           
        ],        
         type: [
          {
            required: true,
            message: langText.form.required,
            trigger: "blur"
          }
        ],        
        description: [
          { min: 1, max: 50, message: this.$filterString(langText.form.rangeLength, {min: 1, max: 50}), trigger: 'blur' }        
                   
        ],
         iconPath: [
          {
            required: true,
            message: langText.form.pleaseSelect,    
            trigger: "blur"
          }
               
        ]     
            
      },
      formData: {
             
      },      
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
    //   this.isEdit = true;
    //   //this.$set(this,'addType', type);
    //   this.editdisabled=false;    
    //   this.$refs['mainForm'].clearValidate();
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
          debugger
          if(this.hasFile && this.formData.type=="image"){ 
              this.$refs.upload.submit();  
          }
          else{
          // this.$refs.mainForm.clearValidate();
            return request({
              url: "/portal-v2/custom-resource" + id,
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
      this.imageUrl="";   
      this.hasFile=false;        
      request({
        url: `/portal-v2/custom-resource/${data.id}`
      }).then(response => { 
         this.formData = response.data;    
         this.addType=this.formData.type;  
         this.isEdit = true;  
         let  ip=window.location.hostname;          
         if(this.formData.type=="image"){
            this.imageUrl="http://"+ip+":"+config["img_assignation_url"]+this.formData.keyValue; 
         }
         this.$set(this.formData,"iconPath",this.formData.keyWord);       
         this.editdisabled=true;        
         this.$refs['mainForm'].clearValidate();  
        
      });       
    },
    handleCancel() {
      // this.listQuery.page = 1;
      // this.listQuery.keywords = '';
      this.dataInit();
      this.isEdit = false;      
    },
    dropdownchange(type){  
      this.isEdit = true;     
      this.addType=type;
      this.formData.type=type;
      this.editdisabled=false;   
      this.hasFile=false;     
      this.imageUrl=""; 
      this.$refs['mainForm'].clearValidate();
    },
    uploadRequest(fileObject){     
        debugger       
      let data = new FormData();//重点在这里 如果使用 var data = {}; data.inputfile=... 这样的方式不能正常上传
      data.append("file",fileObject.file);  
      var suffix = fileObject.file.name.substr(fileObject.file.name.lastIndexOf("."));
      
      data.append("fileName",this.formData.keyWord+suffix);  
      
       request({
            url: '/portal-v2/fileUpload/uploadByFileName',
            headers: { 
              "Content-Type": "multipart/form-data"                               
            },           
            timeout:60000,
            data:data,
            method: 'post'   
          }).then(response => {   
              debugger             
                let id = "",method = "POST"; 
                //this.formData.iconPath=response.data;
                this.formData.keyValue=this.formData.keyWord+suffix;
                if (this.formData.id) {
                  id = "/" + this.formData.id;
                  method = "PUT";
                }
                request({
                  url: "/portal-v2/custom-resource" + id,
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
      this.hasFile=true;  
      this.$set(this.formData,"iconPath", suffix);       
      this.$refs['mainForm'].clearValidate('iconPath'); 
      fileList.splice(0,fileList.length);//清空数组
      if(file){
         fileList.push(file);
         this.imageUrl=URL.createObjectURL(file.raw);;
      }
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



