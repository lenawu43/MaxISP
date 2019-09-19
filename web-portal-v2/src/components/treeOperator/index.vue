<template>
  <article :class="innerClass">
    <!-- <slot name="page-name">
      <h3 v-if="pageName" class="page-title">{{pageName}}</h3>
    </slot> -->
    <el-container class="shadow-box"  style="height: 100%;">
      <el-aside width="320px" class="main-left">
        <h3 class="page-title">
          <i class="element-ex-icons el-icon-ex-arrow-back" v-if="routerBack" @click="handleBack"></i>
          {{pageName}}
        </h3>
        <ul class="button-list">
          <li>
            <el-button class="simple-button" type="text" size="mini" plain icon="element-ex-icons el-icon-ex-add"
              v-show="elementPermission.ADD"
              @click="handleEdit(true, 2)" 
              :disabled="formStatus === 2 || currentOrgPower" 
              :title="langText.form.add"
            ></el-button>
          </li>
          <li>
            <el-button class="simple-button" type="text" size="mini" plain icon="element-ex-icons el-icon-ex-arrow-top"
              v-if="elementPermission.EDIT"
              :disabled="ctrlDisable.left || formData.order === 0" 
              @click="changeOrder(true)"
              :title="langText.tree.up"
            ></el-button>
            <!-- <i class="element-ex-icons el-icon-ex-arrow-top"></i> -->
          </li>
          <li> 
            <el-button class="simple-button" type="text" size="mini" plain icon="element-ex-icons el-icon-ex-arrow-down"
              v-if="elementPermission.EDIT"
              :disabled="ctrlDisable.left || formData.order >= maxOrder" 
              @click="changeOrder(false)"
              :title="langText.tree.down"
            ></el-button>
            <!-- <i class="element-ex-icons el-icon-ex-arrow-down"></i> -->
          </li>
          <li>
            <el-button class="simple-button" type="text" size="mini" plain icon="element-ex-icons el-icon-ex-delete"
              v-show="elementPermission.DELETE"
              @click="handleEdit(true, 3)" 
              :disabled="(ctrlDisable.right || formStatus === 3 || !deleteAble) || currentOrgPower" 
              :title="langText.form.delete"
            ></el-button>
            <!-- <i class="element-ex-icons el-icon-ex-delete"></i> -->
          </li>
        </ul>
        <div class="inner-content">
          <el-input class="simple-input" v-model="filterText" size="mini" prefix-icon="el-icon-search" :placeholder="langText.form.required"></el-input>
          <el-scrollbar ref="wrap" class="hide-warp" :style="{height: ($store.getters.mainHeight - 207) + 'px'}">
            <div class="menu-tree" ref="menu" v-show="elementPermission.BROWSE">
              <el-tree 
                ref="dataTree"
                style="min-width: 249px;"
                accordion
                :data="data" 
                :props="defaultProps" 
                :expand-on-click-node="false" 
                :highlight-current="true"
                @current-change="handleNodeChange"
                node-key="id"
                :default-expanded-keys="selectID"
                :default-checked-keys="selectID"
                :draggable="false"
                :allow-drop="allowDrop"
                :filter-node-method="filterNode"
                :empty-text="''"
                >
                <span class="custom-tree-node" slot-scope="{ node, data }">
                  <i v-if="hasTreeIcon" class="beforeIcon" :class="'tree-icon element-ex-icons ' + getIcon(node,n=>{return n.hasAdmin ? 'ex-user':node.icon})"></i>                    
                  <i v-else :class="data.children && data.children.length !== 0? 'el-icon-ex-folder': 'el-icon-document'"></i>
                  <span class="ellips" :class="data.organizationManageType">{{ node.label }}</span>
                </span>                
              </el-tree>
              <div class="empty-text" v-if="data.length === 0">
                <i class="element-ex-icons el-icon-ex-warning"></i>
                <p>{{langText.common.noData}}</p>
                <p><i class="element-ex-icons el-icon-ex-add" style="cursor: pointer;" @click="handleEdit(true, 2)"></i>{{langText.common.addData}}</p>
              </div>
            </div>
          </el-scrollbar>
        </div>
      </el-aside>
      <el-main class="main-right">
        <el-tabs v-model="activeName" >
          <el-tab-pane :label="lableInfor" name="basic">
            <div class="operate-ctrl clear-hidden">
              <div style="float: right;">
                <el-button  
                  v-show="elementPermission.EDIT && (!editStatus || !formData.pid)"
                  @click="handleEdit(true, 1)" 
                  :disabled="(ctrlDisable.right || disabledElement.basicInfo) || currentOrgPower" 
                  icon="element-ex-icons el-icon-ex-edit" plain
                  size="mini">{{langText.form.edit}}</el-button>
                <el-button v-show="editStatus && formData.pid" @click="submitForm()" icon="element-ex-icons el-icon-ex-pick" size="mini" plain>{{langText.form.save}}</el-button>
                <el-button v-show="editStatus && formData.pid" @click="cancel()"  icon="element-ex-icons el-icon-ex-cancel" size="mini" plain>{{langText.form.cancel}}</el-button>
              </div>
            </div>
            <div class="menu-content clearfix">
              <el-form :disabled="ctrlDisable.form" :model="formData" status-icon :rules="rules" ref="formData" label-width="100px">
                <el-col :key="key" v-for="(item, key) in formStructure" v-if="!item.isFormHide" :span="item.col">
                  <el-form-item :label="item.label" :prop="item.field">
                    <el-switch
                      v-if="item.type === 'switch'"
                      v-model="formData[item.field]"
                      :disabled="item.disabled || handleDisabled(item)" 
                      active-color="#105A98"
                      inactive-color="#ff4949">
                    </el-switch>
                    <el-select 
                      v-else-if="item.type === 'type-select' || item.type === 'select'"
                      :disabled="ctrlDisable.form || (item.type === 'type-select' && formData.children && formData.children.length !== 0) || handleDisabled(item)" 
                      :style="'width: 100%;'" 
                      v-model="formData.type">
                      <el-option 
                        :key="optKey"
                        v-for="(optData, optKey) in item.data"
                        :label="optData.label"
                        :value="optData.value">
                      </el-option>
                    </el-select>
                    <div v-else-if="item.type === 'icon'">
                      <i class="main-font-color element-ex-icons" :class="getExitIcon(formData.icon)"></i>
                      <el-button class="el-button-a" size="mini" plain :disabled="ctrlDisable.form || handleDisabled(item)" @click="iconDialogVisible = true">{{langText.form.select}}</el-button>
                      <service-dialog 
                      :title="langText.form.select"
                      :show.sync="iconDialogVisible" 
                      :hideFooter="true">
                        <el-scrollbar ref="wrap" slot="body" class="hide-warp" style="height: 486px;">
                          <icon-picker @change-icon="changeIcon"></icon-picker>
                        </el-scrollbar>
                      </service-dialog>
                    </div>
                    <el-select style="width:100%;" 
                      v-else-if="item.type === 'parent'" 
                      v-model="formData.pid" 
                      :placeholder="langText.form.pleaseSelect"
                      @change="changeParent(formData.pid)">
                      <el-option
                        :key="-1"
                        :label="langText.common.null"
                        :value="-1">
                      </el-option>
                      <el-option
                        v-for="item in treeList"
                        :key="item.value"
                        :label="item.name"
                        :value="item.id"
                        :class="typeof item.id"
                        :disabled="item.id === formData.id">
                      </el-option>
                    </el-select>
                    <el-input v-else 
                      :disabled="!item.isEditable || item.disabled || handleDisabled(item)" 
                      type="text" 
                      v-model="formData[item.field]" 
                      :placeholder="item.placeholder"></el-input>
                  </el-form-item>
                </el-col>
              </el-form>
            </div>
            <div class="ex-info">
              <slot name="exInfo"></slot>
            </div>
          </el-tab-pane>
          <el-tab-pane :label="`${langText.organization.member}(${$limitTotal(memberTotal)})`" v-if="memberModule" name="member">
            <member-manager 
              ref="member" 
              :formData="formData" 
              :selectID="selectID" 
              :searchText="searchText" 
              @changeTotal="changeTotal">
            </member-manager>
          </el-tab-pane>
           <el-tab-pane :label="`${langText.organization.manager}(${$limitTotal(roleUserTotal)})`" v-if="memberModule" name="roleuser">
             <roleuser-manager 
              ref="roleuser" 
              :formData="formData" 
              :selectID="selectID" 
              :searchText="searchText" 
              @changeroleTotal="changeroleTotal"
              @getTree="getTree">>
              >
            </roleuser-manager>
          </el-tab-pane>  
        </el-tabs>
      </el-main>
    </el-container>
  </article>
</template>

<script>
import request from '@/utils/request'
import {getTree, addObj, updObj, delObj, search, unique} from '@/api/basic'
import iconPicker from '@/components/menu/icon-picker'
import Vue from 'vue'
import serviceDialog from '@/components/service-dialog'

let components = {};

export default {
  props: {
    innerClass: {
      type: String,
      default: 'inner-content'
    },
    hasTreeIcon: {
      type: Boolean,
      default: false
    },
    lableInfor:{
      type: String,
      default:''
    },  
    dynamicKey: null,
    apiPath: String,
    formStructure: Array,
    searchText: String,
    pageName: String,
    //是否有路由回退按钮
    routerBack: {
      type: Boolean,
      default: false
    },
    disabledElement: {
      type: Object,
      default() {
        return {}
      }
    },
    extendData: {
      type: Object,
      default() {
        return null
      }
    },
    getDetailByChange: '',
    submitHandler: Function,
    elementPermission: {
      type: Object,
      default() {
        return {}
      }
    },
    memberModule: {
      type: Boolean,
      default: false
    },
    routerHandler: {
      type: Function,
      default() {

      }
    },
    exFormValid: {// 附加表单验证开关
      type: Boolean,
      default: false
    },
    apiDef: {
      type: Object,
      default() {
        return {};
      }
    }
  },
  created() {
    this.initFormData();
    this.getTree({
      callback: data => {
        this.$nextTick(() => {
          let dataTree = this.$refs.dataTree;           
          data[0] && dataTree.setCurrentKey(data[0].id);
          // console.log(dataTree.getCurrentNode(data[0].id));
          // 第一次进来默认选中第一个根节点
          // 注意getCurrentNode获取的node和el-tree click事件传入的node不一样，缺少parent
          data[0] && this.handleNodeChange(data[0], dataTree.getCurrentNode(data[0].id), data.length);
        });
      }
    });
    this.initComponents();
  },
  watch: {
    'filterText': function(value) {
      this.$refs.dataTree.filter(value);
    },
    editStatus: function(value) {
      this.$emit('editStatusChange', value);
    },
    formStatus: function(value) {
      this.$emit('formStatusChange', value);
    },
    formData: function(value) {
      // console.log('formDataChange', value);
      this.$emit('formDataChange', value);
    }
  },
  data() {
    let langText = {
      form: this.$t('form'),
      field: this.$t('field'),
      common: this.$t('common'),
      organization: this.$t('organization'),
      tree: this.$t('tree')
    };
    return {
      roleUser:[],
      selectID: [],
      deleteAble: false,
      maxOrder: 0,
      activeName: 'basic',
      filterText: '',
      formStatus: 0,
      editStatus: false,
      iconDialogVisible: false,
      dialogVisible: {
        icon: false,
        member: false
      },
      data: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      memberTotal: 0,
      roleUserTotal:0,
      rules: {},
      formData: {},
      langText,      
      treeList: [],
      initOr: false,
      //当前组织新增删除编辑权限
      currentOrgPower:false
    }
  },
  computed: {    
    iconMap() {
      return this.$store.state.menu.menuIcon;
    },
    //选中了节点，且不处于编辑状态，则左边操作开放
    //没有选中节点，则左边和右操作禁止
    //根节点禁止左边操作
    ctrlDisable() {
      let left = true, right = false, form = false;
      if (this.formData.id && !this.editStatus) {
        // console.log('formData === ', this.formData.pid);
        /* if (this.formData.pid === -1) {
          left = false;
          right = false;
        } else {
          left = true;
          right = true;
        } */
        left = false;
        //right = false;
      } else if(!this.formData.id) {
        //left = true;
        right = true;
      }
      if (!this.editStatus) {
        form = true;
      } else {
        //form = false;
      }
      /* console.log( {
        left,
        form,
        right
      }); */
      return {
        left,
        form,
        right
      }
    }
  },
  methods: {    
    handleBack() {
      /* if (this.routerHandler) {
        this.$emit('back');
        return;
      } */
      this.$emit('back');
      // this.$router.back();
    },
    //FormData获取对应字段的数据，支持字段嵌套
    getFormData(data, field) {
      let fieldArr = field.split('.'), _data = data;
      for(let field of fieldArr) {
        _data = _data[field];
      }
      return _data;
    },
    initComponents() {
      // const _import = file => () => import(file)
      if(this.memberModule) {
        components.memberManager = require('./member-manager').default;
        Vue.component('member-manager', components.memberManager);

        components.roleUser = require('./roleuser-manager').default;
        Vue.component('roleuser-manager', components.roleUser);
      }
      /* console.log('components ', {
        iconPicker,
        // memberManager,
        serviceDialog,

        ...components
      }); */
    },
    //修改数据时若字段具有唯一性，则需要禁止
    //父组件可传入参数disabledElement来控制禁止状态
    handleDisabled(rules) {
      let isDisabled = this.disabledElement[rules.field] || false;
      // debugger
      // console.log(rules.field, 'handleDisabled', (this.formStatus === 1 && rules.isUnique) || isDisabled);
      return (this.formStatus === rules.disabledStatus) || isDisabled;
    },
    initFormData(obj) {
      let data = {...obj};
      this.formStructure.forEach(item => {
        if (!obj || !obj[item.field]) {
          // console.log(item.field, this.formData);
          data[item.field] = item.default;
          //  console.log(item.field, this.formData[item.field]);
        }
        if (!this.rules[item.field]) {
          this.rules[item.field] = item.rules;
          if (item.isUnique) {
            if(!this.rules[item.field]) {
              this.rules[item.field] = [];
            }
            this.rules[item.field].push({
              validator: (rule, value, callback) => {
                let params = {}, _id;
                item.uniqueField && item.uniqueField.forEach(field => {
                  if(typeof field === 'string' && this.formData[field]) {
                    params[field] = this.formData[field];
                  }
                  if(typeof field === 'object' && field.prop) {
                    params[field.prop] = field.value;
                  }
                });
                // console.log('field == ', params);
                params[item.field] = value;
                // console.log('formStatus ===', this.formStatus);
                if(this.formStatus === 1) {
                  // console.log('value = ', value, 'this.formData.id = ', this.formData.id);
                  _id = this.formData.id;
                  if(_id === value) {
                    // console.log('skip');
                    callback();
                    return;
                  }
                } else if(this.dynamicKey) {// 新增的时候若有动态参数，则需要加上ower参数
                  params.owner = this.dynamicKey;
                }
                unique(this.apiPath, params, _id, {apiDef: this.apiDef.unique}).then(res => {
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
      this.formData.children = [];
    },
    //不存在的icon则统一为问号
    getExitIcon(icon) { 
      return this.iconMap['el-icon-' + icon]? 'el-icon-' + icon : 'el-icon-ex-error';
    },
    getIcon(node,fn){  
      let  icon=fn(node.data);
      return this.iconMap['el-icon-' + icon]? 'el-icon-' + icon : 'el-icon-ex-error';        
    },    
    getTree({key, callback}) {
      let id = this.dynamicKey || this.$route.query.id;
      this.$handleRequest(getTree(this.apiPath, id, {apiDef: this.apiDef.getTree}), {
        success: response => {
          let data = response.data;
          if ( !Array.isArray(data) ) {
            this.data = [data];
          } else {
            this.data = data;
          }
          this.treeList = [];
          this.handleTree(this.data);
          // console.log('treeList =', this.treeList);
          callback && callback(data);
          if (key) {
            this.selectID = [key];
            this.$nextTick(() => {
              this.$refs.dataTree.setCurrentKey(key);
            })
            /* setTimeout(() => {
              this.$refs.dataTree.setCurrentKey(key);
            }, 300); */
          }
        }
      });
    },
    handleTree(data) {
      for (let i = 0, len = data.length; i < len; i++) {
        let element = data[i];
        this.treeList.push(element);
        // console.log(element.name);
        if (element.children) {
          this.handleTree(element.children)
        }
      }
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    handleNodeChange(data, node, maxOrder = 0) { 
      this.resetForm('formData');
      this.maxOrder = node.parent && node.parent.childNodes? node.parent.childNodes.length - 1 : this.maxOrder;
      /* console.log('maxOrder ==== ', this.maxOrder);
      console.log('order ==== ', this.formData.order); */
      this.formStatus = 0;
      this.editStatus = false;
      if (data.children && data.children.length !== 0) {
        this.deleteAble = false;
      } else {
        this.deleteAble = true;
      }
      this.formData = this.$deepCopy(data);
      this.selectID = [this.formData.id];
      //数据请求成员列表   
      this.$refs.member && this.$refs.member.getMember(data.id);
      //管理员列表
      this.$refs.roleuser && this.$refs.roleuser.getRoleuser(data.id);

      /* this.$refs.dataTree.setCurrentKey(data.id);
      getObj(this.apiPath, data.id).then(response => {
        this.formData = response.data;
        // this.formData.parentName = node.parent.data.name || this.langText.common.null;
        this.$refs.member && this.$refs.member.getMember(this.formData.id);
      }); */
      // debugger;
      if(!this.getDetailByChange) {
        this.$emit('handleNodeChange',{ data, node, editStatus: false});
        return;
      }
      request({
        url: this.getDetailByChange + '/' + this.formData.id
      }).then(response => {
        this.formData = response.data;
        this.$emit('handleNodeChange',{ data: response.data, node, editStatus: false});
        // this.$refs.member && this.$refs.member.getMember(this.formData.id);
      });
    },
    changeTotal(total) {
      this.memberTotal = total;
    },
    changeroleTotal(total){
      this.roleUserTotal = total;
    },
    handleEdit(isEdit, status) {
      this.$emit('handleEdit', {isEdit, status});
      if (status !== 3) {
        this.editStatus = isEdit;
        this.formStatus = status || 0;
      }
      if (status === 2) {
    
        //添加
          this.formData.pid = this.formData.id;
          this.initFormData({
            // enterpriseId: true,
            // parentName: true,
            pid: this.formData.pid,
            // 防止扩展信息被初始化掉
           // extendEntityList: this.formData.extendEntityList,
            extendXrefEntities:[]
          });
          if(this.data.length === 0 || !this.formData.pid) {
          
            this.formData.pid = -1;
            // console.log('this.formData.pid', this.formData);
          }
      } else if (status === 3) {
        //删除
        this.$delMsgBox({
          promise: () => {
            return delObj(this.apiPath, this.formData.id);
          },
          success: response => {
            this.editStatus = false;
            this.formStatus = 0;
            this.$refs.dataTree.remove(this.formData.id);
            this.initFormData();
            this.resetForm('formData');
            this.$emit('afterDelete', {isEdit, status});
          }
        });
      }
      // console.log('this.formData', this.formData);
      // this.$emit('EdittatusChange', { data: this.formData, isEdit, status});
    },
    //暂时不需要拖拽了
    allowDrop(draggingNode, dropNode, type) {
      if(dropNode.data.pid === -1) {
        return false;
      } else if(type === 'inner' && dropNode.data.type === 1) {
        return false;
      } else {
        return true;
      }
    },
    /* nodeDrop(node, tarNode, type, event){
      let _idx = tarNode.parent.data.children.indexOf(node.data);
      this.formData.pid = tarNode.data.pid;
      this.formData.order = _idx;
      // console.log(tarNode.parent.data.children.indexOf(node.data));
      updObj(this.apiPath, this.formData.id, this.handleSubmitData(), {apiDef: this.apiDef.upd}).then(response => {
        this.$refs.dataTree.updateKeyChildren(this.formData);
        this.$message.success(`${this.langText.form.save}${this.langText.form.success}`);
      });
    }, */
    submitForm() {
      //let curNodeData = this.$refs.dataTree.getCurrentNode();
      this.$refs.formData.validate((valid) => {
        if (valid) {
          let state = {
            exFormValidate: false
          };
          this.$emit('handleSubmit', this.formData, state);
          // console.log('state === ', state);
          // 附加表单验证
          if(this.exFormValid && !state.exFormValidate) {
            return;
          }
          this.submitData();
        } else {
          // console.log('error submit!!');
          return false;
        }
      });
    },
    submitData() {      
      this.resetForm();
      this.submitHandler && this.submitHandler(this.formData);      
      // console.log('formStatus ===', this.formStatus);
      if (this.formStatus === 1 ) {// 更新
        updObj(this.apiPath, this.formData.id, this.handleSubmitData(), {apiDef: this.apiDef.upd}).then(response => {
          if(response.status !== 200 && response.message) {
            this.$message({
              message: response.message,
              type: 'warning'
            });
            return;
          }
          this.resetForm();
          // console.log('curNodeData.children', curNodeData.children);
          // response.data.children = curNodeData.children;
          // curNodeData = response.data;
          // this.$refs.dataTree.updateKeyChildren(-1, response.data);
          this.getTree({key: response.data.id});
          this.$message.success(`${this.langText.form.save}${this.langText.form.success}`);
          // this.formData.id = response.data;
        });
      } else {// 新增      
        // console.log('submit!', this.$refs.dataTree.getCurrentNode());
        let curNode = this.$refs.dataTree.getNode(this.formData.pid);
        if(curNode) {
          let childNodes = curNode.childNodes;
          this.formData.order = childNodes? childNodes.length : 0;
          // console.log('childNodes === ', childNodes);
        } else if(this.formData.pid === -1) {// 若添加的是根节点
          this.formData.order = this.data.length;
        }
        this.maxOrder  = this.formData.order;
        let submitData = this.handleSubmitData()
        if(submitData === false){
          return;
        }
         
        addObj(this.apiPath, submitData, {apiDef: this.apiDef.add}).then(response => {
          if(response.status !== 200 && response.message) {
            this.$message({
              message: response.message,
              type: 'warning'
            });
            return;
          }
          // console.log('addObj ===', JSON.stringify(this.data));
          // debugger
          this.getTree({key: response.data.id});
          this.formData.id = response.data.id;
          // this.formData = response.data;
          try {// 添加的时候提交再出发一次点击
            let dataTree = this.$refs.dataTree;
            response.data && dataTree.setCurrentKey(response.data.id);
            response.data && this.handleNodeChange(response.data, dataTree.getCurrentNode(response.data.id));
          } catch (error) {
            console.error('error! id === ', response.data.id, 'info:', error);
          }
          this.$message.success(`${this.langText.form.save}${this.langText.form.success}`);
          // this.$refs.dataTree.setCheckedKeys([response.data.id]);
          // console.log('getCurrentKey', this.$refs.dataTree.getCurrentKey(response.data.id), response.data.id);
          
          this.resetForm('formData');
        });
        console.log(' formData ==', this.formData);
      }
      this.deleteAble = true;
      this.editStatus = false;
      this.formStatus = 0;
    },
    //改变排序
    changeOrder(isUp) {
      let dataTree = this.$refs.dataTree, curNode = dataTree.getCurrentNode();
      let parNodes = dataTree.getNode(this.formData.id).parent;
      // console.log('parNodes ==', parNodes);
      // debugger
      if (isUp) {
        this.formData.order --;
        // let _idx = parNodes.data.children? parNodes.data.children.indexOf(curNode) : parNodes.data.indexOf(curNode);
        let _tarNode = dataTree.getNode(parNodes.childNodes[this.formData.order].data.id);
        // dataTree.updateKeyChildren();
        curNode.order = this.formData.order;
        _tarNode.data.order = this.formData.order + 1;
        // debugger
        // console.log('_tarNode ===', _tarNode.data.order, ' - ', this.formData.order + 1);
        dataTree.remove(this.formData.id);
        dataTree.insertBefore(curNode, _tarNode.data);
      } else {
        this.formData.order ++;
        curNode.order = this.formData.order;
        // console.log('formData.order', this.formData.order);
        let _tarNode = dataTree.getNode(parNodes.childNodes[this.formData.order].data.id);
        _tarNode.data.order = this.formData.order - 1;
        // console.log('_tarNode ===', _tarNode.data, ' - ', this.formData.order - 1);
        dataTree.remove(this.formData.id);
        // dataTree.insertAfter(curNode, parNodes.childNodes[this.formData.order - 1]);
        dataTree.insertAfter(curNode, _tarNode.data);
      }
      dataTree.setCurrentKey(this.formData.id);
      this.$delate(300, () => {
        updObj(this.apiPath, this.formData.id, this.handleSubmitData(), {apiDef: this.apiDef.upd}).then( () => {
          // this.$message.success(this.langText.form.saveSuccess);
        });
      });
    },
    handleSubmitData() {// 部分模块需要特殊处理数据  
      let submitData = JSON.parse(JSON.stringify(this.formData));
      if(this.extendData){
          Object.assign(submitData, this.extendData);
      }
      /* let validStatus = {
        flag: true
      }; */
      this.$emit('handleSubmitData', {
        formData: submitData,
        // validStatus
      });
      return submitData;
    },
    cancel() {
      let currentNodeData;
      // console.log('cancel === ', this.formData);
      
      if(this.formData.id) {
        currentNodeData = this.$refs.dataTree.getNode(this.formData.id).data;
      } else {
        currentNodeData = this.$refs.dataTree.getCurrentNode();
      }
      // debugger
      /* console.log(' currentNodeData =', currentNodeData);
      if(currentNodeData) {
        this.formData = this.$deepCopy(currentNodeData);
      } */
      currentNodeData && this.handleNodeChange(currentNodeData, this.$refs.dataTree.getCurrentNode(currentNodeData.id));
      this.editStatus = false;
      this.formStatus = 0;
      this.resetForm('formData');
      this.$emit('cancel', this.formData);
    },
    resetForm() {
      this.$refs.formData.clearValidate();
    },
    changeIcon(data) {
      // console.log('field', data);
      this.formData.icon = data;
      this.iconDialogVisible = false;
    },
    handleConfirm(data) {
      search();
    },
    changeParent(id) {
      if(id) {        
        let dataTree = this.$refs.dataTree, 
          parNode = dataTree.getNode(id);
        // console.log('parNode =', parNode)
        this.formData.order = parNode.childNodes.length;
      } else {
        this.formData.order = 0;
      }
    }    
  },
  components: {
    iconPicker,
    // memberManager,
    serviceDialog
  }
}
</script>

<style scoped lang="scss">
@import '@/assets/css/_constant.scss';
.main-left{
  position: relative;
  padding: 15px;
  border-right: 1px #ccc solid;
}
.main-left .el-icon-ex-undo{
  width: 24px;
  height: 24px;
  line-height: 24px;
  text-align: center;
  background: #409EFF;
  color: #fff;
  border-radius: 2px;
  cursor: pointer;
  margin: 2px;
}
.main-right{
  padding: 15px;
}
.menu-tree{
  margin-top: 15px;
  width: auto;
  display: inline-block;
}
.menu-content{
  margin-top: 15px;
}
/* .el-form-item{
  margin-bottom: 10px;
} */
.tree-icon{
  margin-right: 3px;
  font-size: 14px;
}
.page-title{
  position: relative;
  height: 20.8px;
}
.page-title .element-ex-icons {
  color: #105A98;
}
.button-list{
  position: absolute;
  top: 15px;
  right: 15px;
}
.button-list>li{
  float: left;
}
.button-list>li>.simple-button{
  width: 24px;
  height: 24px;
  font-size: 16px;
}
.empty-text{
  text-align: center;
  padding: 80px 0;
}
.empty-text>.element-ex-icons{
  color: #ccc;
  font-size: 28px;
}
.empty-text>p{
  color: #4A4A4A;
  font-size: 14px;
}
/* .button-list>li>i{
  width: 24px;
  height: 24px;
  font-size: 16px;
  text-align: center;
  cursor: pointer;
}
.button-list>li>i.active{
  color: #105A98;
} */
.main-left .inner-content{
  background: #efefef;
}

.ex-info{
  padding-top: 20px;
  border-top: 1px #ccc solid;
}

.el-icon-ex-arrow-back:hover{
  background-color: $main-color;
  color: #fff;
  cursor: pointer;
}

.abu{
    position:absolute;
    top:5px;
    right: -2px;
  }

  .xlspan{
     display:inline-block;
     width: 99% ;        
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;    
  }

</style>