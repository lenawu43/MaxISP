<template>
  <div>
    <el-switch
      v-if="item.type === 'switch'"
      v-model="formData[item.field]"
      :disabled="item.disabled || disabledByEditStaus(item)" 
      active-color="#105A98"
      inactive-color="#ff4949">
    </el-switch>
    <el-select 
      v-else-if="item.type === 'type-select'"
      :disabled="ctrlDisable.form || (formData.children && formData.children.length !== 0) || disabledByEditStaus(item) " 
      :style="'width: 100%;'" 
      v-model="formData.type">
      <el-option 
        :key="optKey"
        v-for="(optData, optKey) in item.data"
        :label="optData.label"
        :value="optData.value">
      </el-option>
    </el-select>
    <div v-else-if="item.type === 'icon'">
      <i class="main-font-color element-ex-icons" :class="getExitIcon(formData.icon)"></i>
      <el-button style="border: none;" size="mini" plain :disabled="ctrlDisable.form || disabledByEditStaus(item)" @click="iconDialogVisible = true">{{langText.form.select}}</el-button>
      <service-dialog 
      :title="langText.form.select"
      :show.sync="iconDialogVisible" 
      :hideFooter="true">
        <icon-picker slot="body" @change-icon="changeIcon"></icon-picker>
      </service-dialog>
    </div>
    <el-input v-else 
      :disabled="!item.isEditable || item.disabled || disabledByEditStaus(item)" 
      type="text" 
      v-model="formData[item.field]" 
      :placeholder="item.placeholder"></el-input>
  </div>
</template>

<script>
  export default {
    props: {
      item: Object,
      formData: Object,
      formStatus: {
        type: Number,
        default: 0
      }
    },
    methods: {
      //修改数据时若字段具有唯一性，则需要禁止
      disabledByEditStaus(rules) {
        return this.formStatus === 1 && rules.isUnique
      }
    }
  }
</script>

<style scoped>

</style>