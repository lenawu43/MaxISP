<template>
  <div>
    <div style="margin-bottom: 10px;">
      <el-button  
        style="float: right;" 
        v-show="editBtn"
        :disabled="currentOrgPower"
        @click="handleEditBtn"  
        icon="el-icon-edit" size="mini" plain>{{langText.form.edit}}</el-button>
        <el-button :disabled="commitdisabled" class="flright" icon="el-icon-check" size="mini" @click="handleCommit" v-show="cmtBtn" plain>{{langText.form.save}}</el-button>  
        <el-button class="flright" icon="el-icon-close" size="mini" @click="handleBack" v-show="celBtn" plain>{{langText.form.cancel}}</el-button>  
        <br/><br/>
        <!-- <search-input @changeKeyword="changeMemberKeyword" @handleConfirm="searchMember" style="float: right;" :placeholder="searchText"></search-input> -->
    </div>
        <!-- 角色成员弹窗-->
    <service-dialog
      :title="`${langText.role.roleUser}${langText.form.managa}`"
      class="role-user no-padding-dialog"
      :show.sync="roleDialogVisible"
      >
      <div slot="title" class="searchBox clearfix">
        <span class="el-dialog__title">{{langText.role.organizationPermisionManger}}</span>
        <search-input @handleConfirm="searchUser" style="float: right;" :placeholder="langText.role.searchUserName"></search-input>
      </div>
      <div slot="body">
        <role-user 
          ref="roleUser"
          :orgData="orgData"
          @changeSelection="changeUser"
        ></role-user>
      </div>
      <span slot="footer" style="float: right; padding-right: 20px;">
        <el-button class="footer-button" @click="handleCancel('roleUser')" size="mini" plain>{{langText.form.cancel}}</el-button>
        <el-button class="footer-button" @click="handleConfirm('roleUser')" size="mini" type="primary" plain>{{langText.form.save}}</el-button>
      </span>
    </service-dialog>

    <!-- <service-dialog :title="langText.tree.add" :show.sync="memberDialogVisible" :hideFooter="true">
      <div slot="body" style="min-height: 400px;">
        <search-input @changeKeyword="changeOthersMemberKeyword" @handleConfirm="searchOthersMember" style="margin-bottom: 10px; float:right;" :placeholder="searchText"></search-input>
        <div style="width: 100%; height: 38px; float: right; padding:0 20px;">
          <el-pagination style="float: right; " @size-change="othersMemberSizeChange" @current-change="othersMemberCurrentChange" :current-page.sync="othersMemberQuery.page"
            :page-sizes="[10,20,30, 50]" :page-size="memberQuery.limit" layout=" prev, pager, next,total, sizes, jumper" :total="othersMemberTotal">
          </el-pagination>
        </div>
        <data-table 
          :list="othersMemberData" 
          :dataStructure="collapseItem.member.dataStructure"
          :listQuery="othersMemberQuery"
          :tableOperate="othersMemberOperate"
          :hasOrder="true"
          :tableProps="othersMembersTableProps"
          @handleBtn="handleMemberBtn">
        </data-table>
      </div>
    </service-dialog> -->
    <!-- <el-pagination style="float: right;" @size-change="memberSizeChange" @current-change="memberCurrentChange" :current-page.sync="memberQuery.page"
      :page-sizes="[10,20,30, 50]" :page-size="memberQuery.limit" layout=" prev, pager, next,total, sizes, jumper" :total="roleUserTotal">
    </el-pagination> -->
    <data-table 
      :list="roleUser" 
      :dataStructure="collapseItem.member.dataStructure"
      :tableProps="membersTableProps"
      :listQuery="memberQuery"
      :hasOrder="true">
    </data-table>
    <div class="tabs-button clearfix">            
        <el-button size="mini" style="float: right;" icon="el-icon-plus" 
              @click="showRoleDialog" v-show="!editBtn" plain>{{langText.role.organizationUserManger}}</el-button>
    </div>
  </div>
</template>

<script>
import request from       '@/utils/request'
import serviceDialog from '@/components/service-dialog'
import dataTable from     '@/components/handleData/data-table'
import searchInput from   '@/components/search-input'
import roleUser from      '@/views/base/user/role-user';
export default {
  // name: 'member-manager',
  props: {
    searchText: String,
    selectID: {
      type: Array,
      default: []
    },
  
    formData: {
      type: Object,
      default() {
        return {};
      }
    }
  },
  data() {
    let langText = {
      form:   this.$t('form'),
      field:  this.$t('field'),
      common: this.$t('common'),
      organization: this.$t('organization'),
      tree: this.$t('tree'),
      role: this.$t('role'),
      user: this.$t('user')
    };
    return {
      collapseItem:{
        member: {
          name: '2',
          apiPath: 'admin/user',
          dataStructure: [
            {
              label: langText.user.userCode,
              field: 'userCode',
              align: 'left'
            },
            {
              label: langText.user.name,
              field: 'name',
              align: 'left'
            },
            {
              label: langText.user.gender,
              field: 'gender',
              type: 'boolean',
              width: '80px',
              trueText: langText.user.male,
              falseText: langText.user.female,
              hideTag: true
            },
            {
              label: langText.user.organizationName,
              hideStatus: 'list',
              field: 'organizationName',
              align: 'left'
            },
            {
              label: langText.field.enable,
              width: '80px',
              field: 'enabled',
              type: 'boolean'
            },
            {
              label: langText.user.positionName,
              field: 'positionName',
              align: 'left'
            }
          ]
        }
      },
      langText,
      // memberDialogVisible: false,
      //角色用户 
      tempRoleUser: [],
      roleUser:[],         
      //角色组织管理
      dialogStatus: '0',
      roleDialogVisible: false,  
      orgData: [],
      editBtn:true,
      cmtBtn:false,
      celBtn:false,
      commitdisabled:false,
      // roleUser: [],
      roleUserTotal: 0,
      memberQuery: {
        page: 1,
        limit: 10,
        id: undefined
      },

      // othersMemberData: [],
      // othersMemberQuery: {
      //   page: 1,
      //   limit: 10,
      //   id: undefined
      // },
      // othersMemberTotal: 0,
      // othersMemberOperate: [
      //   {
      //     btnText: langText.form.add,
      //     methodName: 'handleAdd'
      //   }
      // ]
    }
  },
  computed: {
    membersTableProps() {
      return {
        maxHeight: this.$store.getters.mainHeight - 300 
      };
    },
    currentOrgPower(){
          return this.$store.state.user.currentOrgPower;
    }
    // othersMembersTableProps() {
    //   return {
    //     maxHeight: this.$store.getters.mainHeight - 300
    //   };
    // }
  },

  methods: {
    // getList() {
    //   this.$handleRequest(page(this.currentPage, this.idPath), {
    //     success: res => {
    //       this.tableData = res.data.rows;
    //       this.getPermisionAndUser();
    //       this.pageData=res.data.total
    //     }
    //   });
    // },
    // 搜索
    searchUser(SPKeyword) {
      this.$refs.roleUser.searchUser(SPKeyword);
    },
    //弹窗已经选择的数据
    changeUser(data) {
      this.tempRoleUser = data;
    },
       //外层搜索
    searchMember(data) {
      this.memberQuery.page = 1;
      this.memberQuery.keywords = data.keyword;
      let organizationId = this.formData.id;
      this.requestMember(organizationId);
    },
    getRoleuser(organizationId) {
      if (!organizationId || !this.collapseItem.member) {
        return;
      }
      // let _query = this.memberQuery;
      this.requestMember(organizationId, true);
    },
    changeMemberKeyword(keywords) {
      this.memberQuery.keywords = keywords;
    },
    //获取组织角色成员
    requestMember(organizationId, isEmit) {
      request({
        url: '/admin/user/searchByManagedOrganizationId/' + organizationId,
        params: this.memberQuery
      }).then(response => {
        this.roleUser  = response.data.rows;
        this.roleUserTotal = response.data.total;
        if (isEmit) {
          this.$emit('changeroleTotal', this.roleUserTotal);
        }
      }).catch(err =>{
        // this.$message.error('组织管理员获取失败');
      });
    },
     //角色组织管理
    getOrgData() {
      /* if (this.orgData.length !== 0) {
        return;
      } */
      let _id = this.formData.enterpriseId;
      request({
        url: '/admin/organization/getTree/' + _id,
        type: 'get'
      }).then(res => {
        this.orgData = res.data;
        // this.$refs.roleUser.afterGetMenuTree(this.orgData);
        this.$nextTick(() => {
          this.$refs.roleUser.handleSelected(this.roleUser);
        });
        /* this.roleUser.forEach(item => {
          roleUser.handleData(item);
        }); */
      });
    },
    //数据初始
    
    initDialog() {
      this.$refs.roleUser && this.$refs.roleUser.clearSelection();
      // this.$refs.$rolePermision && this.$refs.$rolePermision.clearSelection();
      // this.menuData = [];
      this.orgData = [];
    },
    
    //弹出视图保存,数据保存列表，但是没提交服务器
    handleConfirm(ref) {
        // console.log('handleConfirm');
        this.roleDialogVisible = false
        this.roleUser = this.tempRoleUser;
        //
        this.roleUserTotal = this.tempRoleUser.length;
        this.$emit('changeroleTotal', this.roleUserTotal);
        this.initDialog();
    },
    getList(){
      let organizationId = this.formData.id;
      this.getRoleuser(organizationId);
    },
    //弹出视图取消
    handleCancel(ref) {
        this.roleDialogVisible = false;
        this.initDialog();
    },
    //外层取消按钮
    handleBack() {      
      this.commitdisabled = false;
      this.editBtn=true;
      this.cmtBtn=false;
      this.celBtn=false;
      this.initDialog();
      //取消刷新列表，不保存选择的
      this.getList();
    },
    //外层保存按钮
    handleCommit(){
      this.commitdisabled = true;
      //提交选择的数据请求
      //http://localhost:8082/admin/organization/updateOrganizationAdmin/10006?body="[10003,10004]"
      var padic = [];
      for(let enter of this.roleUser){
        padic.push(enter.id);
      }
      debugger
      request({
        url: '/admin/organization/updateOrganizationAdmin/'+this.formData.id,
        method:'put',
        data:padic
      }).then(response => {
        this.getList();
        this.$emit('changeroleTotal', this.roleUserTotal);
        this.$message.success(`${this.langText.form.save}${this.langText.form.success}`);
        this.conmitSuccess();
        this.$emit('getTree',{key: this.formData.id});
      }).catch(err =>{
        this.conmitFail();
      });
    },
    
    //   //保存选择的数据
    // handleAdd(data){
    //   let userId = data.id, orgId = this.formData.id;
    //   request({
    //     url: `admin/user/updateOrganizationAdmin/${userId}/${orgId}`,
    //     method: 'put'
    //   }).then(res => {
    //     this.memberDialogVisible = false;
    //     this.getRoleuser(this.formData.id);
    //   });
    // },
    //外层按钮保存提交回掉
    conmitSuccess() {
      this.editBtn = true;
      this.cmtBtn  = false;
      this.celBtn  = false;
      this.commitdisabled = false;
    },
    conmitFail(){
        this.commitdisabled = false;
    },
    //table=2 编辑按钮
    handleEditBtn() {
      // this.roleUser =[]
      this.editBtn  =false
      this.cmtBtn   =true
      this.celBtn   =true
    },
    //角色用户管理
    showRoleDialog() {
        this.roleDialogVisible = true;
        this.getOrgData();
    },

    // getOthersMember(formData) {
    //   //let _this = this;
    //   // console.log(formData.enterpriseId, _this.selectID[0]);
    //   // console.log('formData', this.formData);
    //   // console.log(this.selectID);
    //   if(!formData.enterpriseId || !this.selectID[0]) {
    //     this.$message.error(this.langText.organization.pleaseSelect);
    //     return false;
    //   }
      // this.memberDialogVisible = true;
      // this.requestOthersMember();
    // },
    // changeOthersMemberKeyword(keywords) {
    //   this.othersMemberQuery.keywords = keywords;
    // },
    // searchOthersMember(data) {
    //   this.othersMemberQuery.page = 1;
    //   this.othersMemberQuery.keywords = data.keyword;
    //   // let organizationId = this.formData.id;
    //   this.requestOthersMember();
    // },
    // requestOthersMember() {
    //   this.$handleRequest(
    //     request({
    //       url: `admin/user/searchByEnterpriseIdAndNotInOrganizationId/${this.formData.enterpriseId}/${this.selectID[0]}`,
    //       params: this.othersMemberQuery
    //     }),
    //     response => {
    //       this.othersMemberData  = response.data.rows;
    //       this.othersMemberTotal = response.data.total;
    //     }
    //   );
    // },
    //不需要删除，
    // deleteMember() {
    // },
    // memberSizeChange(val) {
    //   this.memberQuery.limit = val;
    //   this.memberQuery.page  = 1;
    //   this.getRoleuser(this.formData.id);
    // },
    // memberCurrentChange(val) {
    //   console.log('memberCurrentChange ==== ', val);
    //   this.memberQuery.page = val;
    //   this.getRoleuser(this.formData.id);
    // },
    // othersMemberSizeChange(val) {
    //   this.othersMemberQuery.limit = val;
    //   this.othersMemberQuery.page = 1;
    //   this.getOthersMember(this.formData);
    // },
    // othersMemberCurrentChange(val) {
    //   this.othersMemberQuery.page = val;
    //   this.getOthersMember(this.formData);
    // },
    // handleMemberBtn(data, opts) {
    //   let callback = this[opts.item.methodName];
    //   callback && callback(data);
    // },
  
  },
  components: {
    dataTable,
    serviceDialog,
    searchInput,
    roleUser,
  }
}
</script>

<style scoped>
.operation {
  margin-top: 20px;
  margin-bottom: 10px;
  display: inline-block;
  overflow-x: auto;
  width: 100%;
}
.page {
  display: inline-block;
}
.flright{
  float: right;
}
.tabs-container{
  position: relative;
}
.tabs-button{
  margin-top: 20px;
}
.subtitle{
  font-size: 20px;
}
.el-scrollbar {
  overflow-x: hidden;
}
.el-scrollbar__wrap {
  overflow-x: hidden;
}
.operate-width{
  width:100px
}
.el-icon-ex-arrow-back{
  cursor: pointer;
}
</style>