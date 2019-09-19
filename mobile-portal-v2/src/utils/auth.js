import Cookies from 'js-cookie'
import config from '@/hosts/config'

const TokenKey = 'Authorization'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token, opts) {
  // console.log('setToken', opts);
  return Cookies.set(TokenKey, token, opts)
}

export function removeToken() {
  return Cookies.remove(TokenKey, {
    'domain': config['domain-name']
  })
  // debugger
  /* return Cookies.set(TokenKey, '', {
    'max-age': 0,
    'domain': '.b.com'
  }); */
}

export function isMobilPage() {  
  var userAgentInfo = navigator.userAgent;
  var Agents = ["Android", "iPhone",
        "SymbianOS", "Windows Phone",
        "iPad", "iPod"];
  var flag = false;
  for (var v = 0; v < Agents.length; v++) {
    if (userAgentInfo.indexOf(Agents[v]) > 0) {
      flag = true;
      break;
    }
  }
  return flag;
}