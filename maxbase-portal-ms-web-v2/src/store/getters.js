const getters = {
  name: state => {
    // console.log('!!!', state.user);
    return state.user.name
  },
  $getMenuIcon: (state, key) => {
    return state.menu.menuIcon[key]? key : 'el-icon-ex-help';
  },
  roles: state => state.user.roles,
  userInfo: state => state.user.userInfo,
  menus: state => state.user.menuData,
  addRouters: state => state.permission.addRouters,
  mainHeight: state => {    
    if(sessionStorage.getItem("iframehiddle")){
      return state.common.windowHeight;
    }
    return state.common.windowHeight - 60;
  },
  token: state => state.user.token,
  isInit: state => state.permission.isInit,
  // currentSystem: state => state.permission.authData[state.permission.authIndex]
}
export default getters