import Vue from 'vue'
import Router from 'vue-router'

// const _import = require('./_import_' + process.env.NODE_ENV)
const _import = file => () => import('@/views/' + file + '.vue')

// import home from '@/views/home'

Vue.use(Router)

export const constantRouterMap = [
   { path: '/login', component: _import('login'), hidden: true },
   { path: '/product-list', component: _import('product-list'), hidden: true },
   { path: '/index', component: _import('inpage'), hidden: true } 

 
]

export default new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

