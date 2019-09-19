import Vue from 'vue';
// 获取菜单icon
Vue.prototype.$getMenuIcon = function(key) {
  return this.$store.state.menu.menuIcon[key]? key : 'el-icon-ex-help';
}
Vue.prototype.$formatDate = function(timestemp) {
  // debugger
  if(!timestemp || timestemp instanceof Number) {
    return '';
  }
  var date= new Date(timestemp);
  var y = date.getFullYear();  
  var m = date.getMonth() + 1;  
  m = m < 10 ? ('0' + m) : m;  
  var d = date.getDate();  
  d = d < 10 ? ('0' + d) : d;  
  var h = date.getHours();  
  h=h < 10 ? ('0' + h) : h;  
  var minute = date.getMinutes();  
  minute = minute < 10 ? ('0' + minute) : minute;  
  var second=date.getSeconds();  
  second=second < 10 ? ('0' + second) : second;  
  return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;  
}

Vue.prototype.$limitTotal = function(num) {
  return num > 99? '99+':num;
}

Vue.prototype.$setOrder = function(data, order) {
  return (data.page - 1) * data.limit + Number.parseInt(order) + 1;
}

Vue.prototype.$trim =function(str) {
  let newStr =str.replace(/(^\s*)|(\s*$)/g, "")
  newStr = newStr.replace(/\s+/g, " ")
  return newStr
}
/* 
以下参数均为非必传
@promise 后端回调
@success 成功回调
@fail 失败回调
@stopMsg 阻止成功信息弹出
 */
Vue.prototype.$delMsgBox = function({promise, success, fail, stopMsg}) {
  let confirmFn = (res) => {
    !stopMsg && this.$message({
      type: "success",
      message: this.langText.form.deleteSuccess
    });
    success && success(res);
  }
  this.$confirm(this.$t('form').ifContinue, this.$t('form').tips, {
    cancelButtonText : this.$t('form').cancel,
    confirmButtonText: this.$t('form').confirm,
    type : "warning",
    center : true,
    customClass:'message-swap-primary-class'
  }).then(() => {
    try {
      if (promise) {
        promise().then(res => {
          confirmFn(res);
        }).catch(err => {
          fail && fail(err);
        });
      } else {
        confirmFn();
      }
    } catch (error) {
      console.error('error', error)
    }
  }).catch(() => {
    this.$message({
      type: "info",
      message: this.langText.form.canceled
    });
  });
}

//自定义字符串模板
Vue.prototype.$filterString = function (str, data) {
  return data? str.replace(/{#([^#]+)#}/g, (...args) => {
    args[0] = data[args[1]];
    return args[0];
  }) : str;
}

//计算表格表头的宽度
Vue.prototype.$thWidth = function (type, opts) {
  let countWidth = {
    operate() {
      return opts.num? 15*(opts.num + 1) + 48 * opts.num : 140;
    }
  }
  return type && countWidth[type]? countWidth[type]() : null;
}

let timer = null;
Vue.prototype.$delate = function(ms = 300, fn) {
  clearTimeout(timer);
  timer = setTimeout( () => {
    fn && fn();
  }, ms);
}

import { Loading } from 'element-ui';
//处理请求，请求过长时增加加载遮罩层
Vue.prototype.$handleRequest = function(promise, opts = {}) {
  let {success, fail, refName} = opts, loadingInstance;
  if (typeof opts === 'function') {
    success = opts;
  }
  let timer = setTimeout(() => {
    let target = refName? this.$refs[refName] : document.body;
    loadingInstance = Loading.service({
      target,
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)'
    });
  }, 1440);
  let _init = () => {
    clearTimeout(timer);
    this.$nextTick(() => { // 以服务的方式调用的 Loading 需要异步关闭
      loadingInstance && loadingInstance.close();
    });
  }
  promise.then(res => {
    /* setTimeout( () => {
      _init();
      success && success(res);
    }, 2000);
    return; */

    _init();
    success && success(res);
  }).catch(err => {
    _init();
    fail && fail(err);
  });
}

Vue.prototype.$trim = function(str) {
  return str.replace(/(^\s+)|(\s+$)/g, '');
}
// console.log( new Vue().$trim('  asdasd asdasd   dasd   ') );

Vue.prototype.$trimData = function(object) {
  for(var index in object) {
    if (typeof object[index] === 'string') {
      object[index] = this.$trim( object[index] );
    }
  }
  return object;
}

/* 处理提交 
@params form 非必传，form表单的ref对象
@params formData 非必传，form表单的数据
@params submitPromis/getPromise 非必传，promise对象，后台提交的回调，二者必须传一个submitPromise优先
@params success/fail 非必传，请求成功/失败回调
@params processEnd 非必传，提交的进程结束的回调
@params successText 成功提示
*/
Vue.prototype.$submitProcess = function(
    {form, formData, submitPromise, getPromise, success, fail, processEnd}
  ) {
  let handler = () => {
    let _promise = submitPromise || getPromise();
    // console.log('handleSubmit === 88');
    _promise.then(response => {
      try {
        if (response.status === 200) {
          success && success(response);
          this.$message.success(this.langText.form.saveSuccess);
        } else {
          fail && fail(response.message);
          this.$message.error(response.message);
        }
        /* //测试防止重复提交
        setTimeout(() => {
          processEnd && processEnd();
        }, 2000);
        return; */
        processEnd && processEnd();
      } catch (error) {
        console.log('error ===', error);
      }
    }).catch(error => {
      console.error('Promise ERROR!', error);
      fail && fail(error);
      processEnd && processEnd();
    });
  }
  //去除数据中多余的空格
  formData && this.$trimData(formData);
  if(form) {
    form.validate(valid => {
      if (valid) {
        handler();
      } else {
        processEnd && processEnd();
        return false;
      }
    });
  } else {
    handler();
  }
}

Vue.prototype.$deepCopy = function(data) {
  return data? JSON.parse(JSON.stringify(data)) : null;
}
import request from '@/utils/request'
// import { resolve } from 'upath';
//处理页面元素权限
/* 
@param callback 外部对有效权限的处理回调 
 */
Vue.prototype.$setElementPermission = function(callback) {
  /* return request({
    url: '/admin/permission/selectUserFunctionDetail/' + this.$store.state.permission.menuIds[this.$route.path]
  }); */
  return new Promise((resolve, reject) => {
    request({
      url: '/admin/permission/selectUserFunctionDetail/' + this.$store.state.permission.menuIds[this.$route.path]
    }).then(response => {
      let elementPermission = {};
      if (!response.data) {
        resolve({});
        console.warn('没有配置权限');
        return;
      }
      response.data.elementResourceEntities.forEach(element => {
        if (element.available === true) {
          elementPermission[element.code] = true;
          callback && callback(element);
        }
      });
      resolve(elementPermission);
      console.log('elementPermission = ', JSON.stringify(elementPermission));
    }).catch(error => {
      reject(error);
    });
  });
}
// 将传入的permission数据转化为通用的数据
Vue.prototype.$handlePermission = function(elementPermission) {
  let object = {}, _permission = elementPermission;
  // debugger
  for (const key in _permission) {
    let k = key.split(':');
    object[k[1] || k[0]] = _permission[key];
  }
  return object;
}