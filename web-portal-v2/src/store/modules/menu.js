const menuIcon = {
  'el-icon-ex-app': 'ex-app',
  'el-icon-ex-bookmark': 'ex-bookmark',
  'el-icon-ex-cloud': 'ex-cloud',
  'el-icon-ex-file': 'ex-file',
  'el-icon-ex-format-align-center': 'ex-format-align-center',
  'el-icon-ex-format-align-left': 'ex-format-align-left',
  'el-icon-ex-format-align-right': 'ex-format-align-right',
  'el-icon-ex-group': 'ex-group',
  'el-icon-ex-help': 'ex-help',
  'el-icon-ex-link': 'ex-link',
  'el-icon-ex-lock': 'ex-lock',
  'el-icon-ex-fold-menu': 'ex-fold-menu',
  'el-icon-ex-more-horiz': 'ex-more-horiz',
  'el-icon-ex-notifications': 'ex-notifications',
  'el-icon-ex-order-bigtosmall': 'ex-order-bigtosmall',
  'el-icon-ex-order-smalltobig': 'ex-order-smalltobig',
  'el-icon-ex-phone': 'ex-phone',
  'el-icon-ex-setting': 'ex-setting',
  'el-icon-ex-star': 'ex-star',
  'el-icon-ex-unlock': 'ex-unlock',
  'el-icon-ex-unsee': 'ex-unsee',
  'el-icon-ex-upload': 'ex-upload',
  'el-icon-ex-user': 'ex-user',
  'el-icon-ex-warning': 'ex-warning',
  'el-icon-ex-chart-line': 'ex-chart-line',
  'el-icon-ex-chart-bar': 'ex-chart-bar',
  'el-icon-ex-chart-bar2': 'ex-chart-bar2',
  'el-icon-ex-chart-line2': 'ex-chart-line2',
  'el-icon-ex-chart-pie': 'ex-chart-pie',
  'el-icon-ex-account': 'ex-account',
  'el-icon-ex-password': 'ex-password',
  'el-icon-ex-base': 'ex-base',
  'el-icon-ex-internet': 'ex-internet',
  'el-icon-ex-product': 'ex-product',
  'el-icon-ex-org': 'ex-org',
  'el-icon-ex-maintain': 'ex-maintain',
  'el-icon-ex-chevron-right': 'ex-chevron-right'
};
const menu = {
  state: {
    menuIcon,
    menuTags: [],
    tagsPath: {}
  },
  getters: {

  },
  mutations: {
    ADD_MENU_TAG(state, data) {
      if (state.tagsPath[data.path]) {
        return;
      }
      let _arr = state.menuTags;
      state.tagsPath[data.path] = true;
      if (_arr.length >= 8) {
        state.tagsPath[_arr[0].fullPath] = false;
        // console.log('fullPath === ', _arr[_arr.length - 1].fullPath);
        _arr.shift();
      }
      _arr.push(data);
    },
    DEL_MENU_TAG(state, idx) {
      let _arr = state.menuTags;
      state.tagsPath[_arr[idx].path] = false;
      _arr.splice(idx, 1);
    },
    DEL_OTHERS_MENU_TAG(state, idx) {
      let _arr = state.menuTags;
      if (_arr.length === 1) {
        return false;
      }
      _arr = _arr.filter((element, index) => {
        let flag = false;
        if (index === idx) {
          flag = true;
        } else {
          state.tagsPath[_arr[index].path] = false;
        }
        return flag;
      });
      state.menuTags = _arr;
    },
    CLEAR_MENU_TAG(state) {
      state.menuTags = [];
      state.tagsPath = {}
    }
  }
}
export default menu