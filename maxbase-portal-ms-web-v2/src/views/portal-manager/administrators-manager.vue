<template>
  <div>
       <el-form :model="formData" ref="mainForm" label-width="150px" :rules="rules">
             <el-row>
                <el-col :span="8">
                    <el-form-item :label="'原密码'"   prop="oldPassWord">
                        <el-input type="password" v-model="formData.oldPassWord" ></el-input>
                    </el-form-item>           
              </el-col>
              <!-- <el-col style="visibility: hidden;" :span="2">1</el-col> -->
              <el-col :span="8">
              </el-col>
              <el-col :span="8">
              </el-col>               
            </el-row>
            <el-row>
                <el-col :span="8">
                    <el-form-item :label="'新密码'"   prop="newPassword">
                        <el-input type="password" v-model="formData.newPassword" ></el-input>
                    </el-form-item>           
              </el-col>
              <!-- <el-col style="visibility: hidden;" :span="2">1</el-col> -->
              <el-col :span="8">
              </el-col>
              <el-col :span="8">
              </el-col>               
            </el-row>
            <el-row>
                <el-col :span="8">
                    <el-form-item :label="'确认密码'"   prop="confirmPassword">
                        <el-input type="password" v-model="formData.confirmPassword" ></el-input>
                    </el-form-item>           
              </el-col>
              <!-- <el-col style="visibility: hidden;" :span="2">1</el-col> -->
              <el-col :span="8">
              </el-col>
              <el-col :span="8">
              </el-col>               
            </el-row>
            <el-row>
                <el-col :span="8">
                    <el-form-item >
                       <el-button
                            @click="handleSubmit"
                            icon="el-icon-check"
                            size="mini"
                            type="primary"
                            plain
                        >{{langText.form.save}}</el-button>
                    </el-form-item>           
              </el-col>
              <!-- <el-col style="visibility: hidden;" :span="2">1</el-col> -->
              <el-col :span="8">
              </el-col>
              <el-col :span="8">
              </el-col>               
            </el-row>
       </el-form>
  </div>
</template>

<script>
import request from "@/utils/request";
export default {
  data(){
      var validaPassword = (rule, value, callback) => {
        if (!value||!this.formData.newPassword) {
          // this.$message.error(this.langText.form.notNull);
          callback();
        } else {
            if(value!==this.formData.newPassword){
                 callback('两次密码不一致');
            }
            else{
                 callback();
            }
        }
      };
      let langText = {
      form: this.$t("form"),
      field: this.$t("field"),
      common: this.$t("common"),      
      webManager:this.$t("webManager"),
      valid: this.$t("valid")     
    };
    return  {
        langText,       
        formData: {
             
         },
         rules: {
            oldPassWord: [
                {
                    required: true,
                    message: langText.form.required,
                    trigger: "blur"
                },
                { min: 6, max: 20, message: this.$filterString(langText.form.rangeLength, {min: 6, max: 20}), trigger: 'blur' }, 
                      
            ],
            newPassword: [
                {
                    required: true,
                    message: langText.form.required,
                    trigger: "blur"
                },
                { min: 6, max: 20, message: this.$filterString(langText.form.rangeLength, {min: 6, max: 20}), trigger: 'blur' }, 
                      
            ],
            confirmPassword: [
                {
                    required: true,
                    message: langText.form.required,
                    trigger: "blur"
                },
                { min: 6, max: 20, message: this.$filterString(langText.form.rangeLength, {min: 6, max: 20}), trigger: 'blur' },
                { validator: validaPassword, trigger: 'blur' } 
                      
            ],
         }            
      }
  },
  methods:{
      handleSubmit(){
          this.$refs.mainForm.validate(valid => {
            if (valid) { 
                request({
                    url: `/portal-v2/user/changePassword`,
                    method:'put',
                    data:{
                        oldPassword:this.formData.oldPassWord,
                        newPassword:this.formData.newPassword       
                    }
                }).then(response => { 
                     this.$message.success('修改密码成功');  
                     this.$refs.mainForm.resetFields();                        
                })

                  
            }   
          })        
      }

  }
  
}
</script>

<style>
</style>
