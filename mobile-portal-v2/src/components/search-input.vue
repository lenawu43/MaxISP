<template>
  <div>
    <el-input :style="inputStyle" 
      ref="input"
      :placeholder="holder"  
      v-model="searchValue" 
      clearable 
      size="small" 
      @keyup.native="handleFilter">
        <i slot="prefix" class="el-input__icon el-icon-search"></i> 
        <!-- <el-button slot="append" icon="el-icon-search" @click="handleFilter" >{{langText.form.search}}</el-button> -->
    </el-input>
  </div>
  <!-- template里面的内容和正常VUE文件一样 -->
</template>
<script>
  let langText;
  export default {
    data(){
      // data里面的数据要定义所有和组件相关的数据
      langText = {
        common: this.$t('common'),
        field: this.$t('field'),
        form: this.$t('form'),
        header: this.$t('header'),
        login: this.$t('login')
      };
      // console.log('langText', langText);
      return{
        searchValue:'',
        searchParameter:{
          keywords:null,
          page:0,
          limit:10
        },
        langText,
        searchFlag: false
      }
    },
    props:{
      placeholder: {
        type: String,
        default: ''
      },
      inputStyle: {
        type: [Object, String],
        // width:456px;margin-right:20px
        default() {
          return {
            width: '456px',
            marginRight: '20px'
          }
        }
      }
    },
    watch: {
      'searchValue'(value) {
        this.$emit("changeKeyword", value);
        this.handleFilter();
      }
    },
    computed: {
      holder() {
        return this.placeholder || langText.form.defaultSearchText;
      }
    },
    methods:{
      changeKeyword(ev) {
        // console.log('ev.keyCode === ', ev.keyCode);
        if(ev.keyCode !== 13) {
          this.searchParameter.keywords = this.searchValue.replace(/\s+/g, '\f');
        } else {
          this.handleFilter();
        }
      },
      handleFilter(){
        if(this.searchFlag) return;
        this.searchFlag = true;
        setTimeout(() => {
          // 自定义组件的方法里面不要调用接口，利用回调$emit 暴露出去
          this.searchParameter.keywords = this.searchValue.replace(/\s+/g, '\f');
          /* this.$refs.input.focus();
          this.$refs.input.select(); */
          this.searchFlag = false;
          this.$emit("handleConfirm", { //$emit()有2个参数，第一个参数是自己定义的事件名字，第二个参数是传递出去的数据
            keyword: this.searchParameter.keywords                    //这里的handleConfirm在外部的组件里面用@handleConfirm="事件"，类似@click="handleClick"
          })
        }, 300); 
      }
    }
  }
</script>

<style scoped>

</style>