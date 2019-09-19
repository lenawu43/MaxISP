<template>
  <el-tree
    class="adjust-expand-icon"
    :data="data"
    node-key="id"
    ref="tree"
    highlight-current
    :props="defaultProps"
    @current-change="handleNodeChange">
    <span class="custom-tree-node" slot-scope="{ node, data }">
      <span><i class="element-ex-icons el-icon-ex-pick"></i> {{ node.label }}</span>
    </span>
  </el-tree>
</template>
<script>
  /* creat by 2018-08-22
  单纯用于选择的树，没有添加删除等功能 */
  export default {
    props: {
      data: {
        type: Array,
        default: []
      }
    },
    data() {
      return {
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      }
    },
    methods: {
      handleNodeChange(...args) {
        // console.log(args);
        this.$emit('handleNodeChange', ...args);
      }
    }
  }
</script>

<style scoped>

</style>