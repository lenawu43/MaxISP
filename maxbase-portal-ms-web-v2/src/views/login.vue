<template>
  <div class="login-wrapper" :style="{height: windowH + 'px'}">
    <div class="wrapper-bg">
      <div class="login-panel" >
        <div class="loginleft">
           <!-- <lang-select></lang-select>           -->
            <h1 class="login-title">
              <img src="@/assets/images/logotype_maxisp.png" alt="logo">
            </h1>           
            <el-form :model="loginForm" :show-message="false" status-icon :rules="rules2" 
              label-position="right"
              ref="loginForm" label-width="100px" class="demo-ruleForm">
              <el-form-item prop="username" label-width="0px">
                <el-input :style="styleFormInput" class="login-input" v-model="loginForm.username" :placeholder="langText.login.accountText" 
                  @focus="clearHolder" @blur="recoverHolder($event, langText.login.accountText)">
                  <i slot="prefix" class="el-input__icon el-icon-account"></i>
                </el-input>
              </el-form-item>              
              <el-form-item prop="password" label-width="0px">
                <el-input type="password" :style="styleFormInput" class="login-input" v-model="loginForm.password" :placeholder="langText.login.password" 
                  @focus="clearHolder" @keyup.native="handleInput" @blur="recoverHolder($event, langText.login.password)" auto-complete="off">
                  <el-tooltip class="item" slot="prefix" :value="tooltipShow"  manual effect="dark" :content="langText.login.capsLock" placement="bottom-start">
                    <i class="el-input__icon el-icon-lock"></i>
                  </el-tooltip>
                </el-input>
              </el-form-item>              
              <el-form-item ref="code" prop="code" label-width="0px" style="margin-bottom: 5px;" v-if="false">
                <el-input :style="styleFormInput" style="width: 172px; height:32px; float: left;" class="login-input-code" v-model="loginForm.code" :placeholder="langText.login.verifyCode" 
                  @focus="clearHolder" @keyup.enter.native="submitForm('loginForm')" @blur="recoverHolder($event, langText.login.verifyCode)" auto-complete="off">
                </el-input>
                <span style="width: 74px; height:32px" class="code-area">
                  <img class="code-image" @click="getCode" :src="`data:image/jpg;base64,${codeImage}`" :alt="langText.login.verifyCode">
                  <a  @click="getCode">
                    <i class="el-icon-refresh"></i>
                  </a>
                </span>
              </el-form-item>
              <!-- <el-form-item prop="authority" label-width="0px">
                <el-select v-show="false" class="authority" @change="changeAuthority" v-model="authority" placeholder="请选择">
                  <el-option
                    v-for="(item, index) in authData"
                    :key="index"
                    :label="item.name"
                    :value="index">
                  </el-option>
              </el-select>
              </el-form-item> -->
              <!-- <el-form-item label-width="0px" style="margin-bottom: 5px;">
                <el-checkbox v-model="isSavePwd" class="savePwd">{{langText.login.savePwd}}</el-checkbox>
              </el-form-item> -->             
              <el-form-item label-width="0px">
                <el-button class="login" style="margin-top: 10px;" type="primary" @click="submitForm('loginForm')" :loading="loadingStatu">{{langText.login.loginAction}}</el-button>
              </el-form-item>
            </el-form>
        </div>  
         <footer class="login-footer">
            <ul>
              <li>
                <img src="@/assets/images/logo_maxnerva.png" alt="footer-icon.png" >
              </li>
              <li class="copyright">{{langText.login.copyright}}</li>
            </ul>
        </footer>  
        <!-- <div class="loginright"> 
          <div class="logintop">{{langText.login.customService}}</div>       
             
                          
          </div>  -->
        </div>   
        
     </div>
     
  </div>
</template>

<script>
  import Cookies from 'js-cookie'
  import langSelect from '@/components/lang/lang-select'
  import config from '@/hosts/config'
  import request from '@/utils/request'
  import '@/assets/css/login.min.css'
  export default {
    name: 'login',
    created() {
      /* if(this.$route.query.action === 'reload') {//推出登陆时刷新页面，防止路由重复
        this.$route.query.action = '';
      } */
      debugger
      let lang = Cookies.get('PLAY_LANG');
      if (!lang) {
        // this.changeLang('zh-CN');
        Cookies.set('PLAY_LANG', 'zh-CN', {
          expires: 30*12*10,
          domain: config['domain-name']
        });
        lang = 'zh-CN';
      } else {
        Cookies.set('PLAY_LANG', lang)
      }
      this.$i18n.locale = lang;
    },
    mounted() {
      this.getCode();
      this.timer && clearInterval(this.timer);
      this.timer = setInterval(() => {
        this.getCode();
        // console.log('getCode');
      }, 180000);
    },
    destroyed () {
      // console.log('clearInterval');
      clearInterval(this.timer)
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
      //let timer = null;
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
        loginForm: {
          username: config.username,
          password: config.password,
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
            // { validator: validateCode, trigger: 'blur' }
          ]
        },
        formItemStyle: {
          'margin-left': '0px'
        },
        styleFormInput:{
          height: '32px'
        },
        isSavePwd: true,
        loadingStatu:false,
        config,
        codeImage: null,
        codeToken: '',
        tooltipShow: false,
        authority: 0,
        timer: null
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
            this.loadingStatu=true;
            debugger
            request({
              url: `/portal-v2/user/login`,
              method: 'POST',
              data:{
                username:this.loginForm.username,
                password:this.loginForm.password
              }
            }).then(res=>{
              debugger
              if(res.data){
                sessionStorage.setItem("logSuccess",true);
                this.$router.push({ path: '/index' });
              }
              else{
                 this.$message.error('用户名或密码错误!'); 
              }
              this.$refs.loginForm && this.$refs.loginForm.clearValidate();
              this.loadingStatu = false;
            }).catch(err=>{
               this.$nextTick(() => {
                this.$refs.loginForm && this.$refs.loginForm.clearValidate();
              });
              this.$message.error(err); //登录失败提示错误
              this.loadingStatu=false;
              //this.getCode();
              this.loadingStatu = false;
            });

            // this.$store.dispatch('LoginByUsername', {
            //   userInfo: this.loginForm, 
            //   isSavePwd: this.isSavePwd
            // }).then(() => {            
            //   this.$router.push({ path: '/index' });
            //   this.$refs.loginForm && this.$refs.loginForm.clearValidate();
            //   this.loadingStatu = false;
            // }).catch(err => {
            //   this.$nextTick(() => {
            //     this.$refs.loginForm && this.$refs.loginForm.clearValidate();
            //   });
            //   this.$message.error(err); //登录失败提示错误
            //   this.loadingStatu=false;
            //   //this.getCode();
            //   this.loadingStatu = false;
            // });
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
        // this.$nextTick(() => {
        //   this.$refs.code && this.$refs.code.clearValidate();
        // });
        // request({
        //   url: '/admin/code/getVerifyCode',
        //   method: 'get'
        // }).then(res => {
        //   this.loginForm.code = res.data.code || '';
        //   this.codeImage = res.data.codePic;
        //   this.codeToken = res.data.verifyCodeToken;
        // });
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

<style>

</style>


<style scoped>
.loginleft{
  float: left;
  width: 776px;
  height: 640px;
}
.loginright{
    float: left;
    width: 340px;
    background-color: #FFFFFF;
    height: 640px;
}
.loginright >.logintop{
   font-size: 18px;
   color: #4A4A4A; 
   margin-top: 115px;
   margin-left:100px;   
   font-family:MicrosoftYaHei;
   color:rgba(74,74,74,0.54);
}
.loginright  .appname{
  font-size: 18px;
  color: #4A4A4A;  
  position:absolute;
  left: 0px; 
  font-family:HelveticaNeue-Medium;
  font-weight:500;
  color:rgba(74,74,74,1);  
}

.loginright  .apptype{
  width:68px;
  height:22px;
  background:rgba(16,90,152,1);
  border-radius:12px;
  font-size: 12px;
  color: #FFFFFF;  
  background-color: #105A98;  
  right: 0px;
  line-height: 22px;
  position:absolute;
  text-align: center;   
  font-family:MicrosoftYaHei;  
}

.loginright  .apptext{ 
 width: 140px;
 margin: 0 auto;
 position: relative;
 height: 18px;
 margin-top: 23px;
}

.loginright  img{
  display: block;
  width: 140px;
  height: 140px;
  margin: 21px auto;
}


.login-wrapper .wrapper-bg{
  position: absolute;
  width: 100%;
  height: 100%;
  background: #E3E3DD;
  
}
.login-panel{
  position: relative;
  width: 800px;
  height: 640px;
  padding: 0 0 0 0px;
  margin: 0 auto;
  top: 50%;
  transform: translateY(-320px);  
  background-color: #105A98;  
  box-shadow:0px 4px 14px 2px rgba(0,0,0,0.3);
}

.login-panel .icon-cloud {
  display: block;
  width: 88px;
  height: 88px;
  background: url('../assets/images/logo_maxcloud.png') no-repeat;
  /* margin: 0 10px; */
  background-size: cover;
  margin: 0 auto;
}
.login-title{
  text-align: center;
  /* width: 58.5%;	 */  
  display: block;
  margin: 0;
  padding: 0;
  margin: 0 auto;
  margin-top:132px;
  
}
.login-title img{
  widows: 369px;
  height: 64px;
}
/* .login-title>span, .login-title>img{
} */
.login-title>.title-name{
  font-size: 35px;
  line-height: 35px;
  color: #fff;
  font-weight: normal;
}
.el-form{
  width: 300px; 
  margin: 52px auto 0px ;
}
.el-form-item{  
  height: 54px;
  margin-top:0px;
  text-align:center;
}

.login-panel .code-area{
  color: rgba(255,255,255,1);
  padding: 0 0px;  
}

.login-panel .login{
  width: 260px;
  height: 40px;
  border-radius: 40px;
  background-color: #004279;
  font-size:14px;
  font-family:MicrosoftYaHei;
  color:rgba(255,255,255,1);  
}
.el-input .el-input__icon{
  font-size: 22px;
  width: 12px;
  color: #C0BF87;
}
.savePwd{
  color: #fff;
}
.login-footer{
  position: absolute;
  width: 100%;
  bottom: 0;
  padding: 10px 0;
  top:650px;
}
.login-footer>ul{
  width: 600px;
  text-align: center;
  margin: 0 auto;
}
.login-footer>ul li{
  padding: 6px 40px;
  color: #fff;
  font-size: 14px;
  box-sizing: border-box;
}
.login-footer>ul .copyright{
  font-size: 12px;
  line-height: 32px; 
  color: #4a4a4a;
}
.lang-picker{
  position: absolute;
  top: 24px;
  left: 707px;
  
}
.code-image{
  width:74px; 
  float: left;
  margin-left: 35px;
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
.code-area{
  cursor: pointer;
}
.el-icon-refresh{
  font-size: 22px;
  color: #FFFFFF;
}

</style>