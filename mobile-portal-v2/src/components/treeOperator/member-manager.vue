<template>
  <div>
    <div style="margin-bottom: 10px;">
      <el-button   
        :disabled="currentOrgPower"
        @click="getOthersMember(formData)"  
        icon="el-icon-plus" size="mini" plain>{{langText.form.add}}</el-button>
        <search-input @changeKeyword="changeMemberKeyword" @handleConfirm="searchMember" style="float: right;" :placeholder="searchText"></search-input>
    </div>
    <service-dialog :title="langText.tree.add" :show.sync="memberDialogVisible" :hideFooter="true">
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
    </service-dialog>
    <el-pagination style="float: right;" @size-change="memberSizeChange" @current-change="memberCurrentChange" :current-page.sync="memberQuery.page"
      :page-sizes="[10,20,30, 50]" :page-size="memberQuery.limit" layout=" prev, pager, next,total, sizes, jumper" :total="memberTotal">
    </el-pagination>
    <data-table 
      :list="memberData.rows" 
      :dataStructure="collapseItem.member.dataStructure"
      :tableProps="membersTableProps"
      :listQuery="memberQuery"
      :hasOrder="true">
    </data-table>
  </div>
</template>

<script>
import request from '@/utils/request'
import serviceDialog from '@/components/service-dialog'
import dataTable from '@/components/handleData/data-table'
import searchInput from '@/components/search-input'
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
      form: this.$t('form'),
      field: this.$t('field'),
      common: this.$t('common'),
      organization: this.$t('organization'),
      tree: this.$t('tree'),
      user: this.$t('user'),
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
              label: langText.field.enabled,
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
      memberDialogVisible: false,
      memberData: [],
      memberTotal: 0,
      othersMemberData: [],
      memberQuery: {
        page: 1,
        limit: 10,
        id: undefined
      },
      othersMemberQuery: {
        page: 1,
        limit: 10,
        id: undefined
      },
      othersMemberTotal: 0,
      othersMemberOperate: [
        {
          btnText: langText.form.add,
          methodName: 'handleAdd'
        }
      ]
    }
  },
  computed: {
    membersTableProps() {
      return {
        maxHeight: this.$store.getters.mainHeight - 300 
      };
    },
    othersMembersTableProps() {
      return {
        maxHeight: this.$store.getters.mainHeight - 300
      };
    },
    currentOrgPower(){
          return this.$store.state.user.currentOrgPower;
    }
    
  },
  methods: {
    getMember(organizationId) {
      if (!organizationId || !this.collapseItem.member) {
        return;
      }
      // let _query = this.memberQuery;
      this.requestMember(organizationId, true);
    },
    changeMemberKeyword(keywords) {
      this.memberQuery.keywords = keywords;
    },
    searchMember(data) {
      this.memberQuery.page = 1;
      this.memberQuery.keywords = data.keyword;
      let organizationId = this.formData.id;
      this.requestMember(organizationId);
    },
    requestMember(organizationId, isEmit) {
      request({
        url: '/admin/user/searchByOrganizationId/' + organizationId,
        params: this.memberQuery
      }).then(response => {
        this.memberData = response.data;
        this.memberTotal = response.data.total;
        if (isEmit) {
          this.$emit('changeTotal', this.memberTotal);
        }
      });
    },
    getOthersMember(formData) {
      //let _this = this;
      // console.log(formData.enterpriseId, _this.selectID[0]);
      // console.log('formData', this.formData);
      // console.log(this.selectID);
      if(!formData.enterpriseId || !this.selectID[0]) {
        this.$message.error(this.langText.organization.pleaseSelect);
        return false;
      }
      this.memberDialogVisible = true;
      this.requestOthersMember();
    },
    changeOthersMemberKeyword(keywords) {
      this.othersMemberQuery.keywords = keywords;
    },
    searchOthersMember(data) {
      this.othersMemberQuery.page = 1;
      this.othersMemberQuery.keywords = data.keyword;
      // let organizationId = this.formData.id;
      this.requestOthersMember();
    },
    requestOthersMember() {
      this.$handleRequest(
        request({
          url: `admin/user/searchByEnterpriseIdAndNotInOrganizationId/${this.formData.enterpriseId}/${this.selectID[0]}`,
          params: this.othersMemberQuery
        }),
        response => {
          this.othersMemberData = response.data.rows;
          this.othersMemberTotal = response.data.total;
        }
      );
    },
    //不需要删除，
    deleteMember() {
    },
    memberSizeChange(val) {
      this.memberQuery.limit = val;
      this.memberQuery.page = 1;
      this.getMember(this.formData.id);
    },
    memberCurrentChange(val) {
      console.log('memberCurrentChange ==== ', val);
      this.memberQuery.page = val;
      this.getMember(this.formData.id);
    },
    othersMemberSizeChange(val) {
      this.othersMemberQuery.limit = val;
      this.othersMemberQuery.page = 1;
      this.getOthersMember(this.formData);
    },
    othersMemberCurrentChange(val) {
      this.othersMemberQuery.page = val;
      this.getOthersMember(this.formData);
    },
    handleMemberBtn(data, opts) {
      let callback = this[opts.item.methodName];
      callback && callback(data);
    },
    handleAdd(data){
      let userId = data.id, orgId = this.formData.id;
      request({
        url: `admin/user/updateOrganization/${userId}/${orgId}`,
        method: 'put'
      }).then(res => {
        this.memberDialogVisible = false;
        this.getMember(this.formData.id);
      });
    }
  },
  components: {
    dataTable,
    serviceDialog,
    searchInput
  }
}
</script>

<style scoped>

</style>