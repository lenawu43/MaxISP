<template>
  <div class="btom">
    <langSelect class="langSelect"></langSelect> 
    <div class="clear"></div>
    <el-form :model="loginForm" :show-message="false" status-icon :rules="rules2" 
      label-position="right"
      ref="loginForm"
      label-width="100px" 
      class="demo-ruleForm">
      <el-row  justify="space-between" >  
            <el-col  :span="24" style="height:40px">
            </el-col>       
      </el-row>    
       <el-row  justify="space-between" >  
            <el-col  :span="24">
                <span class="icon-cloud"></span>    
            </el-col>       
        </el-row>     
        <el-row justify="space-between"    >  
            <el-col  :span="24">
                    <img class="imgcloud" src="@/assets/images/logotype_maxcloud.png" alt="logo">
            </el-col>       
        </el-row>   
        <el-row  justify="space-between"  class="input-row">  
            <el-col  :span="24">
                <el-form-item prop="username" label-width="0px">
                    <el-input :style="styleFormInput" class="login-input" v-model="loginForm.username" :placeholder="langText.login.accountText" >
                    <i slot="prefix" class="el-input__icon el-icon-account"></i>
                    </el-input>
                </el-form-item>
            </el-col>       
        </el-row>   
        <el-row  justify="space-between"  class="input-row">  
            <el-col  :span="24">
                <el-form-item prop="password" label-width="0px">
                <el-input type="password" :style="styleFormInput" class="login-input" v-model="loginForm.password" :placeholder="langText.login.password" 
                @keyup.native="handleInput" auto-complete="off">
                <el-tooltip class="item" slot="prefix" :value="tooltipShow"  manual effect="dark" :content="langText.login.capsLock" placement="bottom-start">
                    <i class="el-input__icon el-icon-lock"></i>
                </el-tooltip>
                </el-input>
                </el-form-item>
            </el-col>       
        </el-row>
        <el-row   justify="space-between"  class="input-row">
          <el-col  :span="24">
              <el-form-item ref="code" prop="code" label-width="0px">
                <div class="input-code">
                  <el-input :style="styleFormInput" class="login-input-code" v-model="loginForm.code" :placeholder="langText.login.verifyCode" 
                  @focus="clearHolder" @keyup.enter.native="submitForm('loginForm')" @blur="recoverHolder($event, langText.login.verifyCode)" auto-complete="off">
                  </el-input>
                  <span style="width: 74px; height:32px" class="code-area">
                    <img class="code-image" @click="getCode" :src="`data:image/jpg;base64,${codeImage}`" :alt="langText.login.verifyCode">
                    <a  @click="getCode">
                      <i class="el-icon-refresh"></i>
                    </a>
                  </span>
                </div>
              </el-form-item>
          </el-col>
        </el-row>
        <el-row  justify="space-between">  
            <el-col  :span="24">
                <el-button class="login" v-show="isShowButton" style="margin-top: 10px;" type="primary" @click="submitForm('loginForm')" :loading="loadingStatu">{{langText.login.loginAction}}</el-button>
            </el-col>       
        </el-row>  
    </el-form> 
  </div>
</template>

<script>
import {
  //Message,
  MessageBox,
  Notification
} from 'element-ui'

import { setToken, removeToken ,getToken} from '@/utils/auth'


  import Cookies from 'js-cookie'
  import langSelect from '@/components/lang/lang-select'
  import config from '@/hosts/config'
  import request from '@/utils/request'
  import '@/assets/css/login.min.css' 

  export default {
    name: 'login',
    created() { 
        this.$store.commit('SET_CivetUrl', "");  
        if(this.$store.state.user.token){
            this.$router.push({ path: '/browser-product-list' }); 
        }else{
            this.$store.dispatch('LogOut'); 
        }  
        //  this.$store.dispatch('LogOut');
        let lang = Cookies.get('PLAY_LANG');
        if (!lang) {
            // this.changeLang('zh-CN');
            Cookies.set('PLAY_LANG', 'zh-CN', {
            expires: 30*12*10,
            domain: config['domain-name']
            });
            lang = 'zh-CN';
            Cookies.set('PLAY_LANG', lang);
        }
        this.$i18n.locale = lang;
    },
    mounted() {
      this.getCode();
      setInterval(() => {
        this.getCode();
      }, 180000);
    },
    data() {
      var validateUser = (rule, value, callback) => {
        if (!value) {
          // this.$message.error(this.langText.form.notNull);
          return callback(new Error(this.langText.form.notNull));
        } else {
          callback();
        }
      };
      var validatePwd = (rule, value, callback) => {
        // console.log('validatePwd');
        if (!value) {
          // this.$message.error(this.langText.form.notNull);
          callback(new Error(this.langText.form.notNull));
        } else {
          callback();
        }
      };
      let timer = null;
      var validateCode = (rule, value, callback) => {
        // console.log('validateCode');
        if (value.length !== 4) {
          // this.getCode();
          return callback(new Error(
            this.$filterString(this.langText.form.fixedLength, {num: 4})
          ));
        } else {
          request({
            url: `/admin/code/checkVerifyCode/${this.codeToken}/${value}`,
            methods: 'get'
          }).then(res => {
            if (res.data === true) {
              callback();
            } else {
              this.$message.error(this.langText.login.verifyCodeError);
              this.getCode();
              // console.log(this.langText.login.verifyCodeError);
              callback(new Error(this.langText.login.verifyCodeError));
            }
          }).catch(res => {
            // console.log(this.langText.login.verifyCodeError);
            callback(new Error(this.langText.login.verifyCodeError));
          });
        }
      };
      return {
        text:'',
        loginForm: {
          username: 'admin',
          password: 'Maxbase123456',
          code: ''
        },
        rules2: {
          username: [
            { validator: validateUser, trigger: 'blur' }
          ],
          password: [
            { validator: validatePwd, trigger: 'blur' }
          ],
          code: [
            { validator: validateCode, trigger: 'blur' }
          ]
        },
        formItemStyle: {
          'margin-left': '0px'
        },
        styleFormInput:{
          height: '36px'
        },
        isSavePwd: true,
        loadingStatu:false,
        config,
        codeImage: null,
        codeToken: '',
        tooltipShow: false,
        authority: 0,
        isShowButton:true,
        maxbaseUserCode:''  

      };
    },
    components: {
      langSelect
    },
    computed: {
      windowH() {
        return window.innerHeight;
      },
      langText() {
        return {
          login: this.$t('login'),
          form: this.$t('form')
        }
      }/* ,
      authData() {
        return this.$store.state.permission.authData;
      } */
    },
    methods: {
      submitForm(formName) {       
        this.$refs[formName].validate((valid) => {
          if (valid) {
            // alert('submit!');
            // console.log('submitForm', this.isSavePwd);
            this.loadingStatu=true
            request({
                url: '/api/oauth/oauth/token',
                headers: {'Content-Type': 'application/x-www-form-urlencoded','Authorization':'Basic TWF4YmFzZTpNYXhiYXNl'},                                     
                method: 'post',
                params:{
                  grant_type:"password",
                  username:this.loginForm.username,
                  password:this.loginForm.password
                }

              }).then((response) => {                
                 this.$store.commit('SET_TOKEN', response.access_token)
                  let obj = {};
                  obj.expires=30;
                  obj.domain = config['domain-name']; //所有子域都可以共享这个cookies         
                  this.$router.push({ path: '/browser-product-list' });
                    setToken(response.access_token, obj);  

                       
            }).catch(err => {
              this.$nextTick(() => {
                this.$refs.loginForm.clearValidate();
              });
              this.$message.error(err); //登录失败提示错误
              this.loadingStatu = false;
              this.getCode();
              this.loadingStatu = false;
            });
          } else {
            return false;
          }
        });
      },
      resetForm(formName) {
        let _ref = this.$refs[formName];
        _ref && _ref.resetFields();
      },
      clearHolder(ev) {
        // console.log('ev', ev);
        ev.srcElement.attributes.placeholder.value = '';
      },
      recoverHolder(ev, val) {
        this.tooltipShow = false;
        ev.srcElement.attributes.placeholder.value = val;
      },
      getCode() {
        // this.resetForm('loginForm');
        this.loginForm.code = '';
        this.$nextTick(() => {
          this.$refs.code && this.$refs.code.clearValidate();
        });
        request({
          url: '/admin/code/getVerifyCode',
          method: 'get'
        }).then(res => {
          this.loginForm.code = res.data.code || '';
          this.codeImage = res.data.codePic;
          this.codeToken = res.data.verifyCodeToken;
        });
      },
      handleInput(e) {
        var valueCapsLock = /^[A-Z]$/.test(e.key); // 按键
        // console.log('e.key', e.key);
        if (valueCapsLock){//输入大写字母提示
          this.tooltipShow = true;
        } else {
          this.tooltipShow = false;
        }  
      },
      changeAuthority(value) {
        // console.log('changeAuthority ===', value);
        this.$store.state.permission.authIndex = value;
      }
    }
  }
</script>

<style scoped>
html{
  margin: 0px;
  padding: 0px;
}
.btom{
  margin-top: 0px;
  background-color:rgba(229,229,224,1);
  height: 812px;
}
.icon-cloud {
  display: block;
  width: 88px;
  height: 88px;
  background: url('../../assets/images/logo_maxcloud.png') no-repeat;
  /* margin: 0 10px; */
  background-size: cover;
  margin: 0 auto;
}

.imgcloud {
  display: block;  
  margin: 0 auto;
  max-width: 80%;
}

.login-input
{
  border-color: #004279;
  border-top: 1px solid #E3E3DD;
  border-left: 1px solid #E3E3DD;
  border-right: 1px solid #E3E3DD;
  border-radius: 0px;
  color: #4A4A4A;
  width:200px; 
  margin: 0 auto;
  display:block;
}
.input-code{
  width: 200px;
  margin: 0 auto;
}
.login-input-code{
  width: 90px;
  height:32px;
  float: left;
}
.code-image{
  width:74px; 
  float: left;
  margin-left: 20px;
  height: 32px;
  margin-top:4px;
}
.el-icon-refresh{
  width: 16px;
  height: 16px;
  float: left;
  line-height: 32px;
  position: relative;
  left: 0px;
  color: #105A98;
}
.center{
  display: block;  
  margin: 0 auto;       
}
 .styleFormInput{
  height: '36px';
  margin: 0 auto;    
  margin-bottom: 10px;
}
.login{
  width: 250px;
  border-radius: 40px;  
  display: block;
  margin: 0 auto;    
  
}
/* .el-col{} */
.el-row>.el-col{
  margin-top: 10px;
  margin-bottom: 10px;
}
.input-row>.el-col{
  height: 40px;
}
.el-form{
  margin: 0px;
}
.langSelect{
  float: right;
  right: 10px;
  top: 10px;
}
.clear{
  clear: both;
}
</style>