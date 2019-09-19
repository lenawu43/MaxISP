import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import common from './modules/common'
import user from './modules/user'
import permission from './modules/permission'
import menu from './modules/menu'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    common,
    user,
    permission,
    menu
  },
  getters
})

export default store
