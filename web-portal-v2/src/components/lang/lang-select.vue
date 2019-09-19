<template>
  <el-dropdown class="lang-picker" @command="changeLang" size="mini" trigger="click" type="primary">
    <span class="el-dropdown-button">
      {{langData[currentLang].abbr}}<img src="@/assets/images/expand.png">
    </span>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item :key="key" :command="key" v-for="(item, key) in langData">{{item.name}}</el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
import Cookies from 'js-cookie'
import config from '@/hosts/config'
export default {
  beforeCreate() {
    let lang = Cookies.get('PLAY_LANG');
    if (!lang) {
      // this.changeLang('zh-CN');
      Cookies.set('PLAY_LANG', 'zh-CN', {
        expires: 30*12*10,
        domain: config['domain-name']
      });
      this.$i18n.locale = 'zh-CN';
    }
    this.$i18n.locale = lang;
  },
  data() {
    return {
      langData: {
        'zh-CN': {
          name: '简体中文',
          abbr: '简'
        },
        'zh-TW': {
          name: '繁體中文',
          abbr: '繁'
        }/* ,
        'en-US': {
          name: 'English',
          abbr: 'En'
        } */
      }
    }
  },
  computed: {
    currentLang() {
      // console.log('this.$i18n.local', this.$i18n.locale);
      return this.$i18n.locale || 'zh-CN';
    }
  },
  methods: {
    changeLang(key) {
      // console.log('changeLang');
      Cookies.set('PLAY_LANG', key, {
        expires:30*12*10,
        domain: config['domain-name']
      });
      this.$i18n.locale = key;
    }
  }
}
</script>

<style scoped lang="scss">
@import '@/assets/css/_constant.scss';
.el-dropdown-button{
  background: $main-color;  
  display: block;  
  line-height: 28px;  
  cursor: pointer;
  border-radius: 2px;
  width: 34px;
  height: 28px;
  background-color:#004279;
 font-family:MicrosoftYaHei;
 color:rgba(255,255,255,1);
 padding-left: 10px;
 position: relative;
 

   
}
.el-dropdown-button  img{
   position:absolute;
   top:4px;

  
}
</style>