<template>
  <el-header>
    <div class="content">
      <!-- <i class="element-ex-icons el-icon-ex-fold-menu"></i> -->
      <span class="title">{{title}}</span>
      <ul class="user-ctrl">
        <li  v-if="!hideProduct" style="cursor: pointer;" @click="appDialogVisible = true">
          <i class="element-ex-icons el-icon-ex-app"></i>
          {{langText.header.appMangement}}
        </li>
        <!-- <li style="cursor: pointer;" @click="appDialogVisible = true">
          <i class="element-ex-icons el-icon-ex-app"></i>
          {{langText.header.appMangement}}
        </li> -->
        <li>
          <span class="el-dropdown-link">{{username}} </span>
         
        </li>
      </ul>
    </div>
    <service-dialog width="auto" v-if="!hideProduct" :title="langText.header.selectedApp" hideFooter :show.sync="appDialogVisible">
      <div slot="body">
        <div>
          <product-list :excludedApp="hostsConfig['Application-Code']" styleTheme="block" 
          @select-product="selectProduct"></product-list>
        </div>
      </div>
    </service-dialog>
    <el-dialog
      :title="langText.form.update + '' + langText.login.password"
      :visible.sync="pwdDialogVisible"
      @closed="resetForm('ruleForm')"
      width="30%">
      <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px">
        <el-form-item :label="langText.login.oldPassword" prop="oldPassword">
          <el-input type="password" v-model="ruleForm.oldPassword" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item :label="langText.login.newPassword" prop="newPassword">
          <el-input type="password" v-model="ruleForm.newPassword"></el-input>
        </el-form-item>
        <el-form-item :label="langText.login.retypePassword" prop="retypePassword">
          <el-input type="password" v-model="ruleForm.retypePassword" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="resetForm('ruleForm')" plain>{{langText.form.reset}}</el-button>
          <el-button type="primary" @click="submitForm('ruleForm')" plain>{{langText.form.save}}</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </el-header>
</template>

<script>
  import request from '@/utils/request'
  import serviceDialog from '@/components/service-dialog'
  // import productList from '@/views/base/product-list'  
  import Cookies from 'js-cookie'
  import hostsConfig from '@/hosts/config'
  let langText;
  export default {
    beforeCreate() {
      let lang = Cookies.get('PLAY_LANG');
      if(!lang) {
        lang = 'zh-CN';
        Cookies.set('PLAY_LANG', 'zh-CN');
      }
      this.$i18n.locale = lang;
      // console.log('i18n', this.$i18n.locale);
    },
    props: {
      title: {
        type: String,
        default: ''
      },
      hideProduct: {
        type: Boolean,
        default: true
      }
    },
    data() {
      langText = {
        common: this.$t('common'),
        field: this.$t('field'),
        form: this.$t('form'),
        header: this.$t('header'),
        login: this.$t('login')
      };
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(langText.form.required + langText.login.password);
        } else {
          callback();
        }
      }
      var validOldPass = (rule, value, callback) => {
        // console.log(this.ruleForm.oldPassword, value);
        if (value === '') {
          callback(langText.form.required + langText.login.password);
        } else {
          callback();
        }
      }
      var validatePassSame = (rule, value, callback) => {
        if (value === '') {
          callback(langText.form.required + langText.login.password);
        } else if(this.ruleForm.newPassword !== value){
          callback(langText.form.retryPasswordWarn);
        } else {
          callback();
        }
      };
      let pwdLimit = { min: 6, max: 20, 
      message: this.$filterString(langText.form.rangeLength, {min: 6, max: 20}),
        trigger: 'blur' };
      return {
        appDialogVisible: false,
        pwdDialogVisible: false,
        ruleForm: {
          newPassword: '',
          oldPassword: '',
          retypePassword: ''
        },
        rules: {
          oldPassword: [
            { validator: validatePass, trigger: 'blur' },
            pwdLimit
          ],
          newPassword: [
            { validator: validOldPass, trigger: 'blur' },
            pwdLimit
          ],
          retypePassword: [
            { validator: validatePassSame, trigger: 'blur' },
            pwdLimit
          ]
        },
        langText,
        hostsConfig
      }
    },
    computed: {
      username() {
        return this.$store.getters.name;
      }
    },
    methods: {
      selectProduct() {
        this.appDialogVisible = false;
      },
      handleCommand(command) {
        switch (command) {
          case 'logout':
            this.logout();
            break;
          default:
            this.pwdDialogVisible = true;
            break;
        }
      },
      logout() {
        let handleJump = () => {
          this.$store.commit('CLEAR_MENU_TAG');
          this.$router.push({ path: '/login'});
          location.reload();
        }
        if(Cookies.get('Authorization')) {
          this.$store.dispatch('LogOut').then( () => {
            handleJump();
          } ).catch(error => {
            // console.log('logout ===', error);
          });
        } else {
          handleJump();
        }
      },
      submitForm(formName) {
        let data = this.ruleForm;
        this.$refs[formName].validate((valid) => {
          if (valid) {
            // alert('submit!');
            request({
              url: '/admin/user/changePassword',
              method: 'put',
              data
            }).then(res => {
              // console.log('submitForm', res);
              if(res.status === 200) {
                this.$alert(this.langText.login.resetPwdSuccess, '', {
                  confirmButtonText: this.langText.form.confirm,
                  showClose: false,
                  callback: action => {
                    this.logout();
                  }
                });
              }
            });
          } else {
            // console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    },
    components: {
      serviceDialog     
    }
  }
</script>

<style scoped lang="scss">
.el-header{
  background: #004279;
  padding: 0;
  .title{
    font-size: 20px;
  }
}
.el-header .content{
  color: #fff;
  line-height: 60px;
  margin: 0;
  padding: 0;
  text-indent: 60px;
}
/* .el-header h1 .el-icon-ex-fold-menu{
  width: 56px;
  height: 56px;
  line-height: 56px;
  text-align: center;
  cursor: pointer;
} */
.el-header .content .el-dropdown-link{
  color: #fff;
}
.user-ctrl{
  float: right;
  list-style: none;
  margin: 0;
  padding: 0;
  li{
    margin-left: 20px;
    text-indent: 0;
    float: left;
    font-size: 14px;
    padding: 0 5px;
    .element-ex-icons{
      margin-right: 2px;
    }
  }
  li.link-logout{
    cursor: pointer;
  }
  li.link-logout:hover{
    text-decoration: underline;
  }
}
.lang-picker{
  float: right;
}
</style>