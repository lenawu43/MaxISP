<template>
  <article class="">
    <h3 class="page-title clearfix">
      <i v-if="backBtn" class="element-ex-icons el-icon-ex-arrow-back" @click="handleBack"></i> {{pageName}}
      <search-input v-if="commonPermision['BROWSE']" @handleConfirm="handleFilter" style="float: right;" :placeholder="searchText || langText.form.searchText"></search-input>
    </h3>
    <!-- 按钮区，可配置插槽 -->
    <div class="clearfix">
      <slot name="top-buttons">
        <el-button v-if="commonPermision['ADD']" 
        icon="element-ex-icons el-icon-ex-add" plain size="mini" 
        :disabled="disabledElement.add" 
        @click="handleCreate" >{{langText.form.add}}</el-button>
        <el-button 
          v-if="commonPermision['EDIT'] || isChangeOrder" v-show="showMoveBtn && !listQuery.keywords" 
          size="mini" icon="element-ex-icons el-icon-ex-arrow-top" plain
          :disabled="disabledBtn.up"
          @click="changeOrder('up')">{{langText.tree.up}}</el-button>
        <el-button 
          v-if="commonPermision['EDIT'] || isChangeOrder" v-show="showMoveBtn && !listQuery.keywords" 
          size="mini" icon="element-ex-icons el-icon-ex-arrow-down" plain
          :disabled="disabledBtn.down" 
          @click="changeOrder('down')">{{langText.tree.down}}</el-button>
      </slot>
      <div class="pagination-container">
        <el-pagination 
          v-if="commonPermision['BROWSE']"
          @size-change="handleSizeChange" 
          @current-change="handleCurrentChange" 
          :current-page.sync="listQuery.page"
          :page-sizes="[10,20,30, 50]" 
          :page-size="listQuery.limit" 
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </div>
    <data-table style="margin-top: 20px;"
      v-if="commonPermision['BROWSE']"
      ref="dataTable"
      :list="list" 
      :dataStructure="dataStructure"
      :tableOperate="tableOperate"
      :listQuery="listQuery"
      :hasOrder="true"
      :tableProps="{maxHeight: $store.getters.mainHeight - 220}"
      :select-able="selectAble"
      :commonPermision="commonPermision"
      @handleBtn="handleBtn"
      @changeSelect="changeSelect"
      @currentChange="changeTableRow">
    </data-table>
    <service-dialog 
      width="60%" 
      :title="langText.tree.add" 
      :show.sync="dialogFormVisible" 
      @handleCancel="handleCancel"
      @handleSubmit="handleSubmit">
      <el-form slot="body" :model="formData" :rules="formRules" ref="form" label-width="100px">
        <el-form-item 
          :key="key" 
          v-for="(item, key) in dataStructure"
          v-if="item.isEditable"
          :label="item.label" 
          :prop="item.field">
          <el-switch 
            v-if="item.type === 'boolean'" 
            v-model="formData[item.field]" 
            :active-text="langText.common.yes" 
            :inactive-text="langText.common.no" >
          </el-switch>
          <el-input 
            :title="item.field"
            v-else 
            v-model="formData[item.field]" 
            :disabled="formStatus === 'update' && item.updFixed"
            :placeholder="item.placeholder">
          </el-input>
        </el-form-item> 
      </el-form>
    </service-dialog>
  </article>
</template>

<script>
import dataTable from './data-table'
// import simpleForm from './simple-form'
import serviceDialog from '@/components/service-dialog'
import searchInput from '@/components/search-input'
import request from '@/utils/request'
import {
  //page,
  //addObj,
  delObj,
  //enableObj,
  getObj,
  updObj,
  search,
  unique
} from '@/api/basic'
import {getLang} from '@/utils/lang'
import axios from 'axios'

export default {
  created() {
    this.initFormData(null, true);
    // console.log('this.dynamicKey === ', this.dynamicKey);
    // debugger
    if (!this.dynamicGetData) {
      this.getList();
    } else {
      this.getList(this.dynamicKey);
    }
    // this.formDataCopy();
  },
  watch: {
    dialogFormVisible() {
      this.$nextTick(() => {
        let $form = this.$refs.form;
        $form.clearValidate();
      });
    },
    dynamicKey(value) {
      if(value) {
        this.getList(value);
      } else {
        this.clearData();
      }
    }
  },
  components: {
    dataTable,
    // simpleForm,
    searchInput,
    serviceDialog
  },
  props: {
    apiPath: String,
    apiDef: {
      type: Object,
      default() {
        return {};
      }
    },
    dataStructure: Array,
    tableOperate: Array,
    pageName: {
      type: String,
      default: ''
    },
    searchText: {
      type: String,
      default: ''
    },
    selectAble: {
      type: Boolean,
      default: false
    },
    //动态参数，让外部能够操作组件内的数据
    dynamicGetData: false,
    dynamicKey: Number,
    // 被禁用的元素
    disabledElement: {
      type: Object,
      default() {
        return {}
      }
    },
    // 替换对象，提交时将该对象的属性替换掉formData的属性
    replaceObject:  {
      type: Object,
      default() {
        return {}
      }
    },
    // 排序功能是否启用
    isChangeOrder: {
      type: Boolean,
      default: false
    },
    // 元素权限数据
    elementPermission: {
      type: Object,
      default() {
        return {}
      }
    },
    //防止已经转化过的通用权限再次转化
    stopHandlePermission: {
      type: Boolean,
      default: false
    },
    backBtn: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return  {
      list: [],
      total: null,
      formStatus: '',
      dialogFormVisible: false,
      form: {},
      formData: {},
      formRules:{},
      listQuery: {
        page: 1,
        limit: 10,
        keywords: undefined
      },
      submiting: false,
      showMoveBtn: false,
      disabledBtn: {
        up: false,
        down: false
      },
      currentRow: {},
      langText: {
        form: this.$t('form'),
        common: this.$t('common'),
        field: this.$t('field'),
        tree: this.$t('tree')
      }
    }
  },
  computed: {
    commonPermision() {
      // console.log('commonPermision === ', this.stopHandlePermission? this.elementPermission : this.$handlePermission(this.elementPermission));
      //已经转化过的通用权限直接使用
      return this.stopHandlePermission? this.elementPermission : this.$handlePermission(this.elementPermission);
    }
  },
  methods: {
    initFormData(obj, initRules) {
      let data = {};
      this.dataStructure.forEach(item => {
        if (!obj || !obj[item.field]) {
          data[item.field] = item.default || null;
          //  console.log(item.field, data[item.field]);
        }
        // { validator: checkCodeUnique, trigger: 'blur' }
        if (item.rules && !this.formRules[item.field] && initRules) {
          this.formRules[item.field] = item.rules;
          if (item.isUnique) {
            this.formRules[item.field].push({
              validator: (rule, value, callback) => {
                let params = {}, _id;
                params[item.field] = value;
                /* if(item.exUniqueParam) {
                  for(let key in item.exUniqueParam) {
                    if(item.exUniqueParam[key]) {
                      params[key] = this.formData[key];
                    }
                  }
                } */
                item.handleUnique && item.handleUnique((object) => {
                  params = Object.assign(params, object);
                });
                // console.log('params === ', this.params);
                if(this.formStatus === 'update') {
                  _id = data.id || this.formData.id;
                  if(_id === value) {
                    callback();
                    return;
                  }
                }
                if(item.dynamicKey) {
                  // 可能有多行，先在这里预留好结构
                  item.dynamicKey.forEach(element => {
                    params[element.field] = this.dynamicKey;
                  });
                  item.uniqueField && item.uniqueField.forEach(field => {
                    if(typeof field === 'string' && this.formData[field]) {
                      params[field] = this.formData[field];
                    }
                    if(typeof field === 'object' && field.prop) {
                      params[field.prop] = field.value;
                    }
                  });
                }
                unique(this.apiPath, params, _id).then(res => {
                  if (res.data) {
                    callback();
                  } else {
                    callback(new Error(this.langText.form.dataRepeat));
                  }
                });
              }, trigger: 'blur' 
            });
          }
        }
      });
      this.formData = data;
    },
    getList(id, callback) {
      if(this.dynamicGetData && !id) {
        console.warn('No dynamic key');
        return;
      }
      this.$handleRequest(
        search(this.apiPath, this.listQuery, id, {apiDef: this.apiDef.search}), 
        response => {
          let data = response.data || {
            rows: [],
            total: 0
          };
          this.list = data.rows;
          this.total = data.total;
          callback && callback(response);           
          let  imagecolumn= this.dataStructure.filter(item=>{
              return  item.type==="image"
           })
          imagecolumn.forEach(item=>{
               this.getFullImageURL(item.field);
               return false;//目前只支持一张图片
          })           
        }
      );
    },
    getFullImageURL(columName){
      this.list.forEach((item,index)=>{        
        this.createImageURL(columName,item["iconPath"],index,item["iconDownloadPath"],item["ticket"]);
      });
    },
    createImageURL(columName,imageid,index,requestImgURL,ticket){         
          // axios({
          //   method: 'options',
          //   url: requestImgURL,
          //   headers: {                   
          //     "Ticket":ticket, 
          //     "Content-Type": "application/json"                  
          //   },
          //   responseType: 'blob' 
          // }).then(response => {                
          //   let blob = response;  
          //   debugger              
          //   this.list[index][columName]=URL.createObjectURL(blob);                   
          // }).catch(error=>{
          //   console.log("文件服务",error);
          // });     


        request({
            method: 'get',
            //url: requestImgURL,
            url:  `/storage/preview/${imageid}`,
            headers: {                   
              "Ticket":ticket, 
              "Content-Type": "application/json"                  
            },
            responseType: 'blob' 
            //responseType: 'arraybuffer'
      }).then(response => {                
          let blob = response; 
          debugger              
          this.list[index][columName]=URL.createObjectURL(blob); 
          this.$set(this.list,index,this.list[index]);

        }).catch(r=>{}) ;                          
           
    },
    clearData() {
      this.list = [];
      this.total = null;
    },
    changeOrder(type) {
      if (type === 'up') {
        this.currentRow.order--;
      } else {
        this.currentRow.order++;
      }
      updObj(this.apiPath, this.currentRow.id, this.currentRow).then(response => {
        /* console.log('order === ', this.currentRow.order, 
          this.listQuery.page, 
          this.listQuery.limit,
          (this.listQuery.page - 1)*this.listQuery.limit,
          this.listQuery.page*this.listQuery.limit);
        console.log('page === ', this.listQuery.page); */
        // 改变排序时若超出当前页面，则需要翻页
        if(this.currentRow.order <= (this.listQuery.page - 1)*this.listQuery.limit) {
          this.listQuery.page--;
        } else if(this.currentRow.order >= this.listQuery.page*this.listQuery.limit) {
          this.listQuery.page++;
        }
        // debugger
        this.getList(this.dynamicKey, () => {
          // console.log('id === ', response.data);
          this.$refs.dataTable.setCurrentRow(response.data.id);
        });
      });
    },
    handleCreate(val) {      
      // console.log('val= ==', val);
      // this.listQuery.id = val;
      this.initFormData();
      // console.log(this.formData);
      this.formStatus = 'create';
      this.dialogFormVisible = true;
    },
    handleBtn(data, opts) {      
      //若处理按钮方法是内置的方法，则执行该方法，否则触发事件让上层去调
      // console.log(opts.item.methodName);      
      if (this[opts.item.methodName]) {
        this[opts.item.methodName](data);
      } else {
        this.$emit('handleBtn', data, opts);
      }
    },
    handleEdit(data) {
      this.dialogFormVisible = true;
      this.$handleRequest(
        getObj(this.apiPath, data.id), 
        response => {
          this.formData = response.data || {};
          this.formStatus = 'update';
        }
      );
    },
    changeKeyword(keywords) {
      this.listQuery.keywords = keywords;
      // console.log('keywords === ', this.listQuery.keywords);
    },
    handleFilter(data) {
      //没有搜索接口，暂时不开放
      // console.log('query', this.listQuery)
      this.listQuery = {
        limit: 10,
        page: 1,
        keywords: data.keyword
      }
      this.getList(this.dynamicKey);
    },
    handleEnabled(data) {
      data.enabled = !data.enabled;
      updObj(this.apiPath, data.id, data);
    },
    handleDeletePage() {
      if (this.list.length === 1 && this.listQuery.page > 1) {
        this.listQuery.page--;
      }
    },
    handleDelete(data) {
      this.$delMsgBox({
        promise: () => {
          // console.log('delObj == ', this.apiPath, data.id);
          // debugger
          this.handleDeletePage();          
          // console.log(this.apiDef);
          return delObj(this.apiPath, data.id, {apiDef: this.apiDef.delete})
        },
        success: () => {
          this.getList(this.dynamicKey);
        }
      });
    },
    handleSubmit() {
      if (this.submiting === true) {
        return;
      }
      this.submiting = true;
      this.$trimData(this.formData);
      if (this.isChangeOrder && !this.formData.order) {
        this.formData.order = this.total;
      }
      // debugger
      this.$submitProcess({
        form: this.$refs.form,
        formData: this.formData,
        getPromise: () => {
          let _promise;
          this.replaceObject && Object.assign(this.formData, this.replaceObject);
          if (this.formStatus === 'update') {
            _promise = updObj(this.apiPath, this.formData.id, this.formData);
          } else {
            _promise = request({
              url: '/' + this.apiPath,
              method: 'post',
              data: this.formData
              // params: obj
            });
          } 
          return _promise;
        },
        success: response => {
          this.dialogFormVisible = false;
          this.listQuery.page = 1;
          this.getList(this.dynamicKey);
        },
        processEnd:() => {
          this.submiting = false;
          // console.log(this.submiting);
        }
      });
      
    },
    handleCancel() {
      this.dialogFormVisible = false;
    },
    handleSizeChange(val) {
      this.listQuery.limit = val;
      this.listQuery.page = 1;
      this.getList(this.dynamicKey);
    },
    handleCurrentChange(val) {
      this.listQuery.page = val;
      this.getList(this.dynamicKey);
    },
    changeSelect(selection, row) {
      this.$emit('changeSelect', selection, row);
    },
    changeTableRow(currentRow, oldCurrentRow) {
      // console.log('currentRow ===', currentRow);
      if(!this.isChangeOrder) {
        return;
      }
      this.currentRow = currentRow;
      if(currentRow === null) {
        this.showMoveBtn = false;
        return;
      } else {
        this.showMoveBtn = true;
      }
      // debugger
      if (currentRow.order <= 0) {
        this.disabledBtn.up = true;
        this.disabledBtn.down = false;
      } else if (currentRow.order >= this.total - 1) {
        this.disabledBtn.down = true;
        this.disabledBtn.up = false;
      } else {
        this.disabledBtn.up = false;
        this.disabledBtn.down = false;
      }
      // console.log(currentRow.order <= 0);
    },
    handleBack() {
      this.$emit('handleBack')
    }
  }
}

let langText = getLang();
export function handleTableBtn(data, customData = {}, limitKey) {
  // if(data.if === false) return;
  let object, key = data.code.split(':')[1] || data.code.split(':')[0];
  // code前缀限制
  if(limitKey && data.code.split(':')[0] !== limitKey) return;
  switch (key) {
    case 'EDIT':
      object = customData['EDIT'] || {
        title: langText.form.edit,
        icon: 'element-ex-icons el-icon-ex-edit',
        methodName: 'handleEdit'
      };
      // console.log('customData == =' ,customData['EDIT']);
      break;
    case 'DELETE':
      object = customData['DELETE'] || {
        title: langText.form.delete,
        icon: 'element-ex-icons el-icon-ex-delete',
        methodName: 'handleDelete'
      };
      break;
    case 'ENABLE':
      object = customData['ENABLE'] || {
        title: langText.form.enabled,
        type: 'enabled',
        field: 'enabled',
        icon: 'element-ex-icons el-icon-ex-link',
        methodName: 'handleEnabled'
      }
  }
  return object;
}
</script>

<style scoped>
.pagination-container{
  float: right;
}
.el-icon-ex-arrow-back{
  cursor: pointer;
}
</style>