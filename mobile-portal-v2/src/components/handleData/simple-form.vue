<template>
  <!-- <el-dialog 
    :title="textMap[formStatus]" 
    :visible.sync="formVisible"
    @open="handleOpen"
    @close="handleClose"
    width="70%"
    @closed="handleClosed">
    <el-form :model="formData" :rules="formRules" ref="form" label-width="100px">
      <el-form-item 
        :key="key" 
        v-for="(item, key) in dataStructure"
        v-if="item.isEditable"
        :label="item.label" 
        :prop="item.field">
        <el-switch 
          v-if="item.type === 'boolean'" 
          v-model="formData[item.field]" 
          :active-text="langText.common.yes" 
          :inactive-text="langText.common.no" >
        </el-switch>
        <el-input 
          v-else 
          v-model="formData[item.field]" 
          :disabled="formStatus === 'update' && item.updFixed"
          :placeholder="item.placeholder">
        </el-input>
      </el-form-item> 
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose('form')" plain>{{langText.form.cancel}}</el-button>
      <el-button type="primary" @click="submit('form')" plain>{{langText.form.save}}</el-button>
      <el-button 
        type="primary" 
        v-if="tableOperate.jump" 
        @click="submit('form', true)">
        {{tableOperate.jump.formBtnText}}
        <i class="el-icon-d-arrow-right"></i>
        </el-button>
    </div>
  </el-dialog> -->
  <el-form :model="formData" :rules="formRules" ref="form" label-width="100px">
    <el-form-item 
      :key="key" 
      v-for="(item, key) in dataStructure"
      v-if="item.isEditable"
      :label="item.label" 
      :prop="item.field">
      <el-switch 
        v-if="item.type === 'boolean'" 
        v-model="formData[item.field]" 
        :active-text="langText.common.yes" 
        :inactive-text="langText.common.no" >
      </el-switch>
      <el-input 
        v-else 
        v-model="formData[item.field]" 
        :disabled="formStatus === 'update' && item.updFixed"
        :placeholder="item.placeholder">
      </el-input>
    </el-form-item> 
  </el-form>
</template>

<script>
  let langText;
  export default {
    data() {
      langText = {
        common: this.$t('common'),
        field: this.$t('field'),
        form: this.$t('form')
      };
      return {
        /* textMap: {
          update: langText.form.edit,
          create: langText.form.create
        }, */
        //组件内的显示隐藏标志位，防止和父组件双向绑定
        // formVisible: false,
        // data: {},
        langText
      }
    },
    props: {
      formData: Object,   //表单数据
      formRules: Object,  //表单验证规则
      dataStructure: Array, //表单数据结构
      formStatus: String  //表单状态：创建或更新
    },
    /* created() {
      for( let key in this.formData ){
        this.data[key] = this.formData[key];
      }
    }, */
    // 目前visible-changed可以完成打开关闭的工作，暂时对外提供下面三个自定义事件
    methods: {
      /* handleOpen() {
        this.formVisible = true;
        // this.$emit('handleOpen', this.formVisible);
      },
      handleClose(formName) {
        this.formVisible = false;
        formName && this.$refs[formName].resetFields();
        // this.$emit('handleClose', this.formVisible);
      },
      handleJump(data) {
        let query = {}, query_jump = this.tableOperate.jump.query;
        for (var key in query_jump) {
          query[key] = data[query_jump[key]];
        }
        // console.log('query ==== ', query);
        this.$router.push({
          path: '/orgManager/enterprise/departManager',
          query
        });
      },
      handleClosed(formName) {
        this.formVisible = false;
        formName && this.$refs[formName].resetFields();
        // this.$emit('handleClosed', this.formVisible);
      }, 
      submit(formName, jumpOr) {
        let _opts = {
          formRef: this.$refs[formName],
          formData: this.formData
        };
        if (jumpOr) {
          _opts.callback = (id) => {
            this.formData.id = id;
            this.handleJump(this.formData);
          };
        }
        this.$emit('handleSubmit', _opts);
      }*/
    }
  }
</script>

<style scoped>

</style>