<template>
  <el-table
    ref="dataTable"
    :data="list"
    :stripe="true"
    :border="true"
    :highlight-current-row="true"
    :header-cell-style="getRowClass"
    style="width: 100%"
    :max-height="tableProps? tableProps.maxHeight:'auto'"
    @select="changeSelect"
    @select-all="changeSelect"
    @current-change="currentChange">
    <el-table-column
      v-if="selectAble"
      type="selection"
      width="50"
      align="center">
    </el-table-column>
    <el-table-column
      type="index"
      :label="langText.common.order"
      width="60"
      align="center"
      :index="indexMethod">       
    </el-table-column> 
    <el-table-column 
      :align="handleAlign(rules)" 
      :key="key" 
      v-for="(rules, key) in dataStructure" 
      v-if="rules.label"
      :label="rules.label"
      sortable
      :sort-by="rules.field"
      :width="rules.width">
      <template slot-scope="scope">
        <span style="display:inline-block;;width:100%">
          <i 
            v-if="rules.type === 'boolean' && !rules.hideTag" 
            class="element-ex-icons" 
            :class="{'el-icon-ex-pick': scope.row[rules.field], 'el-icon-ex-cancel': !scope.row[rules.field]}">
          </i>
          <span v-else-if="rules.type === 'boolean' && rules.hideTag" >{{scope.row[rules.field]? rules.trueText:rules.falseText}}</span>
          <span v-else-if="rules.type === 'date'">{{$formatDate(scope.row[rules.field])}}</span>
          <span v-else-if="rules.type === 'line'" style="white-space: pre-line;">{{cellLine(scope.row[rules.field],scope.row)}}</span>
          <span style="display:inline-block;text-align:center;width:100%"  v-else-if="rules.type === 'image' && scope.row[rules.field]"><img :src="scope.row[rules.field]"  style="margin:0 auto;height:30px;width:50px" min-width="70"  /></span>            
          <span v-else>{{scope.row[rules.field]}}</span>
        </span>
      </template>
    </el-table-column>
    <el-table-column fixed="right" align="center"
      v-if="tableOperate && tableOperate.length"
      :label="langText.form.operate" :width="operateWidth" style="bakcground: #fff;">
      <template slot-scope="scope">
        <el-button 
          size="mini" plain
          :key="index" v-for="(item, index) in tableOperate" 
          :title="item.title"
          :icon="getBtnIcon(scope.row, item)"
          @click="handleBtn(scope.row, {item, index})"><span v-if="item.btnText">{{item.btnText}}</span></el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
  export default {
    data() {
      return {
        langText: {
          common: this.$t('common'),
          field: this.$t('field'),
          form: this.$t('form')
        },
        query: this.listQuery
      }
    },
    props: {
      list: Array,  //表格数据
      tableOperate: Array,  //表格操作按钮 
                            //相关参数：icon图标 title标题 field按钮状态相关联的字段
                            //         btnText按钮内容 methodName按钮事件回调中可以用到，执行相关方法
      dataStructure: Array, //表格数据结构
      listQuery: Object,  //获取表格数据的传参
      hasOrder: Boolean,  //是否具有排序功能
      tableProps: Object,  //element ui 原生参数
      selectAble: Boolean,  //判断表格数据是否可选
      /* commonPermision: {
        type: Object,
        default() {
          return {}
        }
      } */
    },
    computed: {
      formText() {
        // console.log(this.$t('form'));
        return this.$t('form');
      },
      operateWidth() {//计算操作列的宽度
        let length = this.tableOperate.length;
        return (length + 1) * 11 + 48 * length;//默认按钮是只有图标的，所以写死为48
      }
    },
    watch: {
      list() {
        this.query = {...this.listQuery};
      }
    },
    methods: {
      /* handleUpdate(data) {
        this.$emit('handleUpdate', data);
      },
      handleDelete(data) {
        this.$emit('handleDelete', data);
      },
      handleEnabled(data) {
        this.$emit('handleEnabled', data);
      },
      handleJump(data) {
        let query = {}, jump = this.tableOperate.jump;
        for (var key in jump.query) {
          query[key] = data[jump.query[key]];
        }
        // console.log('path ==== ', query.path);
        this.$router.push({
          path: jump.path,
          query
        });
      }, */
      cellLine(value,row){   
       //navigationType: "applicationGroup"
        let tmpvalue="";
        if(value){
          value.split(",").forEach(item=>{
              tmpvalue+=item+"\n";
          });        
        }
        return tmpvalue;       
      },
      // 目前仅应用于排序
      setCurrentRow(id) {
        this.$nextTick(() => {
          let currentRow;
          for (let i = 0, len = this.list.length; i < len; i++) {
            if(this.list[i].id === id) {
              currentRow = this.list[i];
              break;
            }
          }
          this.$refs.dataTable.setCurrentRow(currentRow);
        });
      },
      handleAlign(rules) {
        let align;
        if (!rules.align && rules.type === 'boolean') {
          align = 'center'
        } else {
          align = rules.align;
        }
        return align || 'left'
      },
      handleBtn(data, opts){
        this.$emit('handleBtn', data, opts);
      },
      getEnabledBtnData(status) {
        return {
          icon: status? 'el-icon-ex-lock' : 'el-icon-ex-unlock',
          class: status? 'danger' : 'success',
          innerText: status? this.langText.form.disabled : this.langText.form.enabled,
          comfirmText: status? this.langText.form.disabled : this.langText.form.enabled,
          notifyMessage: status? this.langText.form.disabledSuccess : this.langText.form.enabledSuccess
        };
      },
      indexMethod(index) {
        // console.log('indexMethod', this.query, index);
        return this.$setOrder(this.query, index);
      },
      getRowClass({ row, column, rowIndex, columnIndex }) {
        if (rowIndex == 0) {
          return "background:#f4f4f2";
        } else {
          return "";
        }
      },
      getBtnIcon(data, item) {
        if (item.type === 'enabled') {
          return data[item.field]? 'element-ex-icons el-icon-ex-lock' : 'element-ex-icons el-icon-ex-unlock';
        } else {
          return item.icon;
        }
      },
      changeSelect(selection, row) {
        this.$emit('changeSelect', selection, row);
      },
      currentChange(...args) {        
        this.$emit('currentChange', ...args);
      }
    }
  }
  /* 
  event:
  @handleBtn 点击操作按钮时的事件
    params data 当前操作行
    params opts 按钮相关参数，目前包括item:上层传入的按钮参数，index:操作按钮的索引顺序
  */
</script>

<style scoped>
.el-tag{
  cursor: pointer;
}
.element-ex-icons{
  font-size: 13px;
}

.el-table .cell {
  white-space: pre-line;
}
</style>