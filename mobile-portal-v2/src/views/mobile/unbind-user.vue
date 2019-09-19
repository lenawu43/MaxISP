<template>
<div > 
    <el-row class="" :style="{height:height}"  justify="space-between" type="flex"  align="middle"  >  
        <el-col  :span="24"    >   
           <el-button class="login" v-if="isShow"   type="primary" @click="unbind" >解除绑定</el-button>   
        </el-col>
    </el-row>      
</div>
</template>
<script>

import request from '@/utils/request'
import {
  //Message,22
  //MessageBox,
  Notification
} from 'element-ui'

export default {  
  data() {    
       return {
         isShow:true,         
         height:''             
       }
  },
  props: {   
    
  },
  created(){      
    window.addEventListener('resize', this.getHeight);    
  },
  destroyed(){
      window.removeEventListener('resize', this.getHeight)
  },
  mounted() {     
      this.height=window.innerHeight+"px";         
    },

  methods: {
    unbind() {
      let CivetUserCode= this.$store.state.user.CivetUserCode;     
      
      if (!this.iCivetUserCode){
          CivetUserCode=window.localStorage.getItem('CivetUserCode');
      }   
      if(!CivetUserCode||CivetUserCode=="null"){
          this.$message.error({message:"获取香信账号失败",showClose:true});
          return;
      }

      request({
        url: '/admin/3rd-account-match/unbind',
        headers: {'Content-Type': 'application/json'},       
        method: 'options',
        data: {
          thirdPartyAccount: CivetUserCode,
          sourceAccount: "icivet",         
        }}).then(()=>{
              this.$message.success("解绑成功！")   
              this.isShow=false;
             // this.$router.push({ path: '/mobile-product-list' });

        })
    },
    getHeight(){       
          this.height= window.innerHeight+"px";           

    }
  }
}
</script>

<style>
.login{
  width: 300px;
  border-radius: 40px;  
  display: block;
   margin: 0 auto;    
  
}

</style>

