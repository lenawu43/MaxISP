<template>
  <el-dialog
    :title="title"
    :visible.sync="visible"
    @close="handleClose"
    :show-close="showClose"
    :modal="modal"
    :width="width"    
    :show="show">
    <template slot="title">
      <slot name="title"></slot>
    </template>
    <!-- <el-scrollbar :style="{height: ($store.getters.mainHeight - 250) + 'px'}" ref="wrap"
      class="hide-warp" 
      > -->
    <slot name="body"></slot>
    <!-- <el-scrollbar ref="wrap"
      class="hide-warp" 
      >
    </el-scrollbar> -->
    <template slot="footer">
      <slot name="footer" v-if="!hideFooter">
        <el-button size="mini" plain @click="handleCancel">{{langText.form.cancel}}</el-button>
        <el-button size="mini" plain type="primary" @click="handleSubmit">{{langText.form.save}}</el-button>
      </slot>
    </template>
  </el-dialog>
</template>
<script>
export default {
  data () {
    return {
      visible: this.show,
      langText: {
        form: this.$t('form')
      },
      isInit: false
    };
  },
  props: {
    title: String,
    width: {
      default: '70%'
    },
    show: {
      type: Boolean,
      default: false
    },
    hideFooter: {
      type: Boolean,
      default: false
    },
    showClose: {
      type: Boolean,
      default: true
    },
    modal: {
      type: Boolean,
      default: true
    }
  },
  watch: {
    show (value) {
      // console.log('bodyHeight ===', this.$store.getters.mainHeight - 250);
      this.visible = value;
      if (value) {
        this.$emit('open');
        if (this.isInit) {
          this.$emit('afterInit');
        } else {
          this.$nextTick( () => {
            this.isInit = true;
            this.$emit('afterInit');
          } );
        }
      } else {
        this.$emit('close');
      }
    }
  },
  methods: {
    handleClose() {
      // this.visible = false;
      this.$emit('update:show');
      this.$emit('handleClose');
    },
    handleCancel() {
      // this.visible = false;
      this.$emit('handleCancel');
    },
    handleSubmit() {
      // this.visible = false;
      this.$emit('handleSubmit');
    }
  }
};

/* 
  event 
  @open
  @close
  @handleCancel
  @handleSubmit
  @afterInit open事件的升级版，解决在open事件执行时不能通过$refs找到dialog内部的组件的情况
*/
</script>
