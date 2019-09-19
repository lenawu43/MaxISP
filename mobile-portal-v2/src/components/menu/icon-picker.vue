<template>
  <!-- <el-dialog
    title="选择图标"
    :visible.sync="iconDialogVisible"
    width="50%">
    
  </el-dialog> -->
  <ul class="icon-list clearfix">
    <li :key="key" v-for="(item, key) in iconMap" @click="changeIcon(item)">
      <span>
        <i class="element-ex-icons" :class="key"></i>
        <span class="icon-name">{{item}}</span>
      </span>
    </li>
  </ul>
</template>

<script>
  export default {
    computed: {
      iconMap() {
        return this.$store.state.menu.menuIcon;
      }
    },
    methods: {
      changeIcon(data) {
        this.$emit('change-icon', data);
      }
    }
  }
</script>

<style scoped>
.icon-list{
  width: 1024px;
  border-top: 1px solid #eee;
  border-left: 1px solid #eee;
}
.icon-list li{
  cursor: pointer;
  float: left;
  width: 128px;
  text-align: center;
  height: 128px;
  line-height: 128px;
  color: #666;
  font-size: 13px;
  transition: color .15s linear;
  border-right: 1px solid #eee;
  border-bottom: 1px solid #eee;
  margin-right: -1px;
  margin-bottom: -1px;
}
.icon-list li span{
  display: inline-block;
  line-height: normal;
  vertical-align: middle;
  font-family: Helvetica Neue,Helvetica,PingFang SC,Hiragino Sans GB,Microsoft YaHei,SimSun,sans-serif;
  color: #99a9bf;
}
.icon-list li i{    
  display: block;
  font-size: 32px;
  margin-bottom: 15px;
  color: #606266;
  transition: color 0.4s;
}
.icon-list li:hover i{
  color: #409EFF;
}
.icon-list li span.icon-name{    
  display: inline-block;
  padding: 0 3px;
  height: 1em;
  color: #606266;
}
</style>