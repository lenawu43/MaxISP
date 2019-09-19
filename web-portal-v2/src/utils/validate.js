import {getLang} from '@/utils/lang'
let langText = getLang();
// console.log('PLAY_LANG', langText);
let rules = {
  telephone: {
    rule: /^(0[0-9]{2,3}\-)([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/,
    message: langText.valid.telephone + '0755-88888888'
  },
  cellphone: {
    rule: /^[1][3-8]\d{9}$|^([6|9])\d{7}$|^[0][9]\d{8}$|^[6]([8|6])\d{5}$/,
    message: langText.valid.cellPhone
  },
  zipCode: {
    rule: /^[1-9][0-9]{5}$/,
    message: langText.valid.zipCode
  },
  userCode: {
    rule: /^[a-zA-Z][a-zA-Z0-9_]{5,100}$/,
    message: langText.valid.userCode
  },
  fieldCode: {
    rule: /^[a-zA-Z][a-zA-Z0-9_]{0,100}$/,
    message: langText.valid.filedCode
  },
  number: {
    rule: /^(-?\d+)(\.\d+)?$/,
    message: langText.valid.number
  },
  //大于零的正整数
  signlessInteger: {
    rule:/^[1-9]+\d*$/,
    message: langText.valid.signlessInteger
  },
  url:{
    rule:/^(http|https):\/\/([\w.]+\/?)\S*/i,
    message: langText.valid.url
  }
};

export function add(key, handler) {
  if (rules[key]) {
    console.error('已有key值为'  + key + '的表单验证')
  } else {
    rules[key] = handler;
  }
}
export function valid(key, value) {
  return new Promise(function(resolve, reject) {
    let _obj = rules[key];
    if (_obj.rule.test(value)) {
      resolve();
    } else {
      !_obj.message && console.error('Message is undefined');
      reject(_obj.message || 'Error');
    }
  });
}
