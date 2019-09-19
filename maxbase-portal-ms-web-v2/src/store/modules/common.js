import hostConfig from '@/hosts/config.js'
import request from '@/utils/request'
export default {
  state: {
    //固定表头宽度
    thWidth: {
      word2: '80px',
      date: '160px'
    },
    windowHeight: window.innerHeight,
    windowWidth: window.innerWidth,
    hostConfig,
    dictionaryCode: {
      areaCode: 'DIC0009',
      function: 'DIC0007',
      position: 'DIC0008'
    }
  },
  mutations: {
    CHANGE_WINDOW_SIZE(state) {
      // console.log('windowHeight === ', window.innerHeight);
      state.windowHeight = window.innerHeight;
      state.windowWidth = window.innerWidth;
      // console.log('windowHeight ==', state.windowHeight);
      // console.log('windowWidth ==', state.windowWidth);
    }
  },
  actions: {
    GET_DICTIONARY({state}, {key}) {
      return request({
        url: '/api/dict/dict-value/selectByDictionaryCode/' + state.dictionaryCode[key],
        method: 'get'
      });
    }
  }
}