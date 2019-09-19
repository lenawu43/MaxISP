<template>
  <article v-show="elementPermission['USER_MANAGER:BROWSE']">
    <div>      
      <article class="inner-content">
        <handle-data
          v-show="!isEdit"
          ref="mainData"
          :pageName="'业务类型维护'"
          :searchText="searchText"
          :apiPath="'portal-v2/business-type'"
          :apiDef="{search: '/portal-v2/business-type/searchBusinessType'}"
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
                    <el-form-item :label="'业务类型名称'"  prop="businessTypeName" >
                        <el-input v-model="formData.businessTypeName" :disabled="editdisabled"></el-input>
                    </el-form-item>           
              </el-col>
              <!-- <el-col style="visibility: hidden;" :span="2">1</el-col> -->
              <el-col :span="12">
                <el-form-item :label="'平台'" prop="platform" >
                        <el-select
                            v-model="formData.platform"
                            :placeholder="langText.form.pleaseSelect"
                            style="width: 100%;" 
                            >
                            <el-option
                            v-for="(item,index) in [{label:'mobile',id:'mobile'},{label:'web',id:'web'}]"
                            :key="index"
                            :label="item.label"
                            :value="item.id"
                            ></el-option>
                        </el-select>
                    </el-form-item>       
              </el-col>
            </el-row>
            <el-row>
                <el-col :span="12">
                    <el-form-item :label="'显示顺序'"  prop="order">                    
                        <el-input v-model="formData.order" >
                    </el-input>
                    </el-form-item>  
              </el-col>
              <!-- <el-col style="visibility: hidden;" :span="2">1</el-col> -->
              <el-col :span="12">               
              </el-col>
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

   var checkBussinessName = (rule, value, callback) => {  
      if (!value || this.$trim(value) === ''||!this.formData.platform) {
        callback();
      }
      else{  
          if(this.editdisabled){
                request({
                url: `/portal-v2/business-type/unique`+(this.formData.id? '/' + this.formData.id : ''),
                params:{
                    platform:this.formData.platform,
                    businessTypeName:this.formData.businessTypeName
                }
                }).then(response => {  
                    callback();            
                }).catch(error=>{            
                    if(error.response.status===409){
                        callback(langText.form.dataRepeat);
                    }
                }) 
          }   
          else{
               request({
                url: `/portal-v2/business-type/unique-validate`,
                params:{
                    platform:this.formData.platform,
                    businessTypeName:this.formData.businessTypeName
                }
                }).then(response => { 
                    if(response.data){
                        callback();        
                    } 
                    else{
                         callback(langText.form.dataRepeat);
                    }
                        
                })
          }    
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
      searchText: `${langText.form.searchText}(业务类型名称,平台)`,   
      dataStructure: [
        {
          field: "businessTypeName",
          label: '业务类型名称'
        },
        {
          field: "platform",
          label: '平台'
        },
        {
          field: "order",
          label: '显示序号'
        }            
      ],
      tableOperate: [],
      rules: {
        businessTypeName: [
          {
            required: true,
            message: langText.form.required,
            trigger: "blur"
          },
          { min: 1, max: 50, message: this.$filterString(langText.form.rangeLength, {min: 1, max: 50}), trigger: 'blur' },
          {
            required: true, validator: checkBussinessName, trigger: 'blur'
          }         
        ],
         platform: [
          {
            required: true,
            message: langText.form.required,
            trigger: "blur"
          }
        ],        
        order: [
          {
            required: true,
            message: langText.form.required,
            trigger: "blur"
          },         
          {
            required: true, validator: validsignlessInteger, trigger: 'blur'
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
      this.isEdit = true;
      //this.$set(this,'addType', type);
      this.editdisabled=false;    
      this.$refs['mainForm'].clearValidate();
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
          
          // this.$refs.mainForm.clearValidate();
        return request({
            url: "/portal-v2/business-type" + id,  
            method,
            data: this.formData
        });
          
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
        url: `/portal-v2/business-type/${data.id}`
      }).then(response => { 
         this.formData = response.data;          
         this.isEdit = true;  
         this.editdisabled=true;        
         this.$refs['mainForm'].clearValidate();  
        
      });       
    },
    handleCancel() {
      // this.listQuery.page = 1;
      // this.listQuery.keywords = '';
      this.dataInit();
      this.isEdit = false;      
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
</style>



