<template>
  <div :class="styleTheme" class="basic">
    <ul class="product-list clearfix">
      <li :key="index" 
        v-for="(item, index) in productsData" 
        @click="selectProduct(item, index)" v-if="excludedApp !== item.code">
        <!-- <a :href="item.url || ''"> -->
        <a v-if="item.applicationUrlEntities[0]" :href="dealUrl(item)">
          <img class="product-logo" src="@/assets/product-logo/MES.png" alt="Product Logo">
          <!-- <img class="product-logo" :src="item.applicationUrlEntities[0].imageUrl" alt="Product Logo"> -->
          <div class="info">
            <h4 class="text-ellipsis" :title="item.name">{{item.name}}</h4>
            <p class="text-ellipsis" :title="item.description">{{item.description}}</p>
          </div>
        </a>
      </li>
    </ul>
    <el-alert
      :title="$t('header').appAlert"
      type="info">
    </el-alert>
  </div>
</template>

<script>
import request from '@/utils/request'
import Cookies from 'js-cookie'
// import config from '@/hosts/config'
export default {
  created() {
    let type = window.location.hostname.split('.')[0] || 'web';
    // console.log(process.env.NODE_ENV);
    // console.log('this.$route.query.token == ', this.$route.query.token)
    /* switch (window.location.hostname) {
      case config.web10:
        type = 'web172';
        break;
      case config.web172:
        type = 'web2';
        break;
      default:
        type = 'web';
        break;
    } */
    if(process.env.NODE_ENV === 'development') {// 开发环境下写死type
      type = 172;
    }
    request({
      // url: '/admin/application-system/selectApplicationSystemByUserID'
      url: '/admin/application-system/selectAccessibleByTerminalType/web' + type
    }).then(response => {
      this.productsData = response.data;
    });
  },
  data() {
    return {
      productsData: [],
      token: Cookies.get('Authorization')
    }
  },
  props: {
    excludedApp: String | Number,
    styleTheme: {// 主题默认是页面模式
      type: String,
      default: 'page'
    }
  },
  methods: {
    dealUrl(item) {
      return item.applicationUrlEntities[0].url + (item.code !== 'Maxbase'? `?token=${this.token}` : '')
    },
    selectProduct(item, index) {
      if (item.applicationUrlEntities[0].url) {
        /* request({
          url: '/admin/user-application-config/updateLastApplication/' + item.id,
          method: 'put'
        }).then(response => {
          // if (item.code === 'Maxbase') {//本系统不需要新开页面
          //   window.location.href = item.url;
          // } else {
          //   window.open(item.url);
          // }
          // this.$store.state.permission.authIndex = index;
          this.$emit('select-product', item);
        }); */
        Cookies.set('Last-url', this.dealUrl(item));
        this.$emit('select-product', item);
      }
    }
  }
}
</script>

<style scoped lang="scss">
@import '@/assets/css/_constant.scss';
.basic{
  padding: 0 60px;
  .product-list li{
    padding-right: 10px;
    float: left;
    margin: 5px;
    cursor: pointer;
    width: 240px;
    &:hover{
      opacity: 0.9;
      h4{
        text-decoration: underline;
      }
    }
    .product-logo{
      border: #ccc 1px solid;
      width: 80px;
      height: 80px;
      display: block;
      float: left;
    }
    .info{
      width: 108px;
      float: left;
      text-indent: 10px;
      p{
        font-size: 14px;
        color: #4a4a4a;
      }
      h4{
        font-size: 20px;
        line-height: 28px;
        margin-top: 5px;
        color: $main-color;
      }
    }
  }
}
.block {
  padding: 0;
  .product-list li{
    width: 172px;
    .product-logo{
      width: 64px;
      height: 64px;
    }
    .info{
      width: 106px;
      p{
        font-size: 12px;
      }
      h4{
        font-size: 14px;
      }
    }
  }
}
</style>