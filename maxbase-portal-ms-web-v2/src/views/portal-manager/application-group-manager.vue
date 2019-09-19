<template>
<article>
  <h3 class="page-title clearfix">
      {{langText.webManager.applicationGroupPageName}}
  </h3>
  <el-button v-if="commonPermision['USER_MANAGER:ADD']" @click="addHandle"
        icon="element-ex-icons el-icon-ex-add" plain size="mini"         
       >{{langText.form.add}}</el-button>
        <el-button 
            v-if="commonPermision['USER_MANAGER:EDIT']" 
            size="mini" icon="element-ex-icons el-icon-ex-arrow-top" plain   
            @click="handleUp" 
            :disabled="selectIndex<=0"        
            >{{langText.tree.up}}</el-button>
        <el-button 
            v-if="commonPermision['USER_MANAGER:EDIT']" 
            @click="handleDown"
            :disabled="newtableData.length-1==selectIndex || newtableData.length==1 || selectIndex<0"
            size="mini" icon="element-ex-icons el-icon-ex-arrow-down" plain            
            >{{langText.tree.down}}
  </el-button>
  <el-table 
    ref="gorpuTable"
    :data="newtableData"    
    :stripe="true"
    :border="true"
    :highlight-current-row="true"   
    :header-cell-style="getRowClass"  
    @current-change="currentChange"
    style="width: 100%;margin-top: 20px; margin-bottom:20px">
    <el-table-column      
      prop="applicationCode"
      :label="langText.webManager.applicationCode"
      width="200">
    </el-table-column>
    <el-table-column
      prop="applicationGroupItemName"
      :label="langText.webManager.applicationGroupItemName"
      >
    </el-table-column>
    <el-table-column      
      :label="langText.webManager.iconPath"
      align="center"
      width="100">
      <template slot-scope="scope">
         <span >           
           <i class="main-font-color element-ex-icons" :class="getExitIcon(scope.row['iconPath'])"></i>
           </span>         
      </template>
    </el-table-column>
    <el-table-column
      prop="url"
      :label="langText.webManager.webUrl"
      >
    </el-table-column>    
    <el-table-column    fixed="right" align="center"   
      :label="langText.form.operate" width="130" style="bakcground: #fff;"
      >
      <template slot-scope="scope">        
      <el-button  size="mini" plain icon="element-ex-icons el-icon-ex-edit" @click="editHandle(scope.$index, scope.row)" :title="langText.form.edit"></el-button>
      <el-button size="mini" plain icon="element-ex-icons el-icon-ex-delete" @click="deleteHandle(scope.$index, scope.row)" :title="langText.form.delete"></el-button>
      </template>
    </el-table-column>
  </el-table>


  <service-dialog
      :show.sync="webMangerShow"
      width="620px"      
      :show-close="true">
      <div slot="title" class="searchBox clearfix">
        <span class="el-dialog__title">添加{{langText.webManager.applicationGroupPageName}}</span>
      </div>        
      <div slot="body">
         <el-form :model="dialogFormData" ref="dialogForm" label-width="150px" :rules="rules" >
             <el-form-item :label="langText.webManager.applicationCode" prop="applicationCode" >
                        <el-input v-model="dialogFormData.applicationCode" :disabled="isEdit"></el-input>
             </el-form-item>  
             <el-form-item :label="langText.webManager.applicationGroupItemName" prop="applicationGroupItemName" >
                        <el-input v-model="dialogFormData.applicationGroupItemName"></el-input>
             </el-form-item> 
             <el-form-item :label="langText.webManager.webUrl" prop="url">
                        <el-input v-model="dialogFormData.url"></el-input>
             </el-form-item>    
             <el-form-item :label="langText.webManager.iconPath">
               <i class="main-font-color element-ex-icons" :class="getExitIcon(dialogFormData.iconPath)" ></i>
                      <el-button class="el-button-a" style="margin-top:14px" size="mini" plain  @click="iconDialogVisible = true">{{langText.form.pleaseSelect}}</el-button>
                      <service-dialog 
                      :title="langText.form.select"
                      :show.sync="iconDialogVisible" 
                      :modal="false"
                      :hideFooter="true">                      
                        <el-scrollbar ref="wrap" slot="body" class="hide-warp" style="height: 486px;">
                          <icon-picker @change-icon="changeIcon"></icon-picker>
                        </el-scrollbar>
                      </service-dialog>
           </el-form-item>    
         </el-form>
      </div>
      <span slot="footer" style="float: right; padding: 20px 0;">
        <el-button class="footer-button" @click="handleCancel" size="mini" plain>{{langText.form.cancel}}</el-button>
        <el-button class="footer-button" @click="handleSubmit" size="mini" type="primary" plain>{{langText.form.save}}</el-button>
      </span>
    </service-dialog>
 </article>
</template>

<script>
  let  langText={};
  import serviceDialog from "@/components/service-dialog";
  import iconPicker from '@/components/menu/icon-picker';
  import { valid } from "@/utils/validate";
  export default {
    props:{
      commonPermision:Object,
      tableData:{
        type:Array,
        default(){
          return []
        }
      }           
    },
    created(){      
    },
    methods: {
      getRowClass({ row, column, rowIndex, columnIndex }) {
        if (rowIndex == 0) {
          return "background:#f4f4f2";
        } else {
          return "";
        }
      },
      handleClick(row) {
        console.log(row);
      },      
      addHandle(){
        //this.$refs['dialogForm'].clearValidate();
        this.isEdit=false;
        this.dialogFormData={};
        this.dialogFormData.iconPath="";
        this.webMangerShow=true;
        if(this.$refs['dialogForm']){
          this.$refs['dialogForm'].clearValidate();
        }        
      },
      handleCancel(){
        this.webMangerShow=false;
      },
      handleSubmit(){
        this.$refs.dialogForm.validate(valid => {
          if (valid) {            
              this.webMangerShow=false;             
              this.dialogFormData=this.$trimData(this.dialogFormData);//去掉首末空格
              if(!this.dialogFormData.iconPath){
              this.dialogFormData.iconPath="";
              }
              if(this.isEdit){           
                this.newtableData[this.index]=this.dialogFormData;
                this.$set( this.newtableData,this.index, this.dialogFormData);         
              }
              else{
                this.dialogFormData.order=this.newtableData.length;
                this.newtableData.push(this.dialogFormData);
              }    
                         
            } else {              
              return false;
            }
        });
        
      },
      getExitIcon(icon) { 
        return this.iconMap['el-icon-' + icon]? 'el-icon-' + icon : 'el-icon-ex-error';
      },
      changeIcon(data) {   
        this.dialogFormData.iconPath = data;
        this.iconDialogVisible = false;
     },
     editHandle(index,data){     
       debugger  
       //this.dialogFormData.iconPath=data.iconPath;
       this.isEdit=true;
       this.webMangerShow=true;
       this.dialogFormData= JSON.parse(JSON.stringify(data));
       this.index=index;
     },
     deleteHandle(index,data){
       this.$delMsgBox({
        promise: () => { 
          this.newtableData.splice(index,1);   
          this.selectIndex=-1;     
          debugger     
          this.newtableData=this.newtableData.map((item,index)=>{
             item.order=index;
             return item;
          }).sort((a,b)=>{
            return  a.order-b.order;
          });         
          return new Promise((resolve, reject) => {      

           })
        }        
      });
      // this.tableData.splice(index);
     },
     currentChange(currentRow, oldCurrentRow){   
        if(currentRow){
          this.selectIndex=currentRow.order;
        }
     },
     handleUp(){
      if(this.selectIndex-1>=0){      
        let operateRow={}; 
        this.newtableData= this.newtableData.map((item,index)=>{
            if(index==this.selectIndex){
                operateRow=item;
                item.order=item.order-1;
            }
            else if(index==this.selectIndex-1){
                item.order=item.order+1;
            }
            return item;
          }).sort((a,b)=>{
            return  a.order-b.order;
          });
          this.selectIndex=this.selectIndex-1;
          this.$nextTick(() => {
              this.$refs["gorpuTable"].setCurrentRow(operateRow);        
          });      
      }  
    },
    handleDown(){
      if(this.newtableData.length-1!= this.selectIndex){      
        let operateRow={}; 
        this.newtableData= this.newtableData.map((item,index)=>{
            if(index==this.selectIndex){
                operateRow=item;
                item.order=item.order+1;
            }
            else if(index==this.selectIndex+1){
                item.order=item.order-1;
            }
            return item;
          }).sort((a,b)=>{
            return  a.order-b.order;
          });
          this.selectIndex=this.selectIndex+1;
          this.$nextTick(() => {
              this.$refs["gorpuTable"].setCurrentRow(operateRow);        
          });      
      }  
    }
    }, 
    data() { 
      var validurl = (rule, value, callback) => {  
        debugger
      if (!value || this.$trim(value) === '') {
        callback();
      }
      else{
        valid('url', this.$trim(value)).then(() => {
          callback();
        }).catch(err => {
          callback(err);
        });
      }
    };

      langText={ 
          form: this.$t("form"),
          field: this.$t("field"),
          common: this.$t("common"),
          tree: this.$t('tree'),
          webManager:this.$t("webManager"),
          valid: this.$t("valid")  
        };

        var checkSpecialChar = (rule, value, callback) => { 
        if (!value || this.$trim(value) === '') {
            callback();
          }
          else{
            let regEn = /[`~!@#$%^&*()=+<>?:"{},.\/;'[\]]/im,
            regCn = /[·！#￥（=）：；“”‘、，|《。》？、【】[\]]/im;
            if(regEn.test(value) || regCn.test(value)) {
                callback(langText.valid.specialChar);             
            }
            else{
              callback();
            }
          }
       }

        var checkApplictionCode = (rule, value, callback) => {  
          if(this.isEdit) {
            callback();
            return;
          }
          
          if (!value || value === '') {
            callback();
          }          
          else{            
            value=this.$trim(value);
            let result= this.newtableData.some(r=>{
               return r.applicationCode==value;
              });
            if(result){
                  callback(langText.form.dataRepeat)
                }else{
                  callback()
              }     
          }
        };        
      return {  
        newtableData:[],
        langText,    
        selectIndex:-1,    
        webMangerShow:false,
        dialogFormData:{},
        isEdit:false,
        index:0,
        iconDialogVisible:false,          
        rules:{         
          applicationCode: [
            {
              required: true,
              message: langText.form.required,
              trigger: "blur"
            },
            { min: 0, max: 100, message: this.$filterString(langText.form.rangeLength, {min: 0, max: 100}), trigger: 'blur' },    
            {
             validator: checkSpecialChar, trigger: 'blur'
            },                 
            {
             validator: checkApplictionCode, trigger: 'blur'
            }
                             
          ],
          applicationGroupItemName:[
            {
              required: true,
              message: langText.form.required,
              trigger: "blur"
            },
            { min: 0, max: 50, message: this.$filterString(langText.form.rangeLength, {min: 0, max: 50}), trigger: 'blur' } 
          ],
          url:[
            {
              required: true,
              message: langText.form.required,
              trigger: "blur"
            },
            { min: 0, max: 500, message: this.$filterString(langText.form.rangeLength, {min: 0, max: 500}), trigger: 'blur' },
            {
              validator: validurl,        
              trigger: "blur"              
            }  
          ]
        }
      }
    },
    components:{
      serviceDialog,
      iconPicker     
    },
    computed: {    
      iconMap() {
        return this.$store.state.menu.menuIcon;
      }
    },
    watch:{
        tableData(val){
          this.newtableData=val;//JSON.parse(JSON.stringify(val)); 
        }
    }
   
  }
</script>