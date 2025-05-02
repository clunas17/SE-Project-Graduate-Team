import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)


const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: '/',
    name: 'Manager',
    component: () => import('../views/Manager.vue'),
    redirect: '/home',
    children: [
      { path: '403', name: 'NoAuth', meta: { name: 'Unauthorized' }, component: () => import('../views/manager/403') },
      { path: 'home', name: 'Home', meta: { name: 'HomePage' }, component: () => import('../views/manager/Home') },
      { path: 'admin', name: 'Admin', meta: { name: 'Admin' }, component: () => import('../views/manager/Admin') },
      { path: 'user', name: 'User', meta: { name: 'User' }, component: () => import('../views/manager/User') },
      { path: 'adminPerson', name: 'AdminPerson', meta: { name: 'MyProfile' }, component: () => import('../views/manager/AdminPerson') },
      { path: 'password', name: 'Password', meta: { name: 'EditPassword' }, component: () => import('../views/manager/Password') },
      { path: 'notice', name: 'Notice', meta: { name: 'Notice' }, component: () => import('../views/manager/Notice') },
      { path: 'course', name: 'Course', meta: { name: 'Course' }, component: () => import('../views/manager/Course') },
      { path: 'information', name: 'Information', meta: { name: 'MyCourseInformation' }, component: () => import('../views/manager/Information') },
    ]
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/Front.vue'),
    children: [
      { path: 'home', name: 'Home', meta: { name: 'HomePage' }, component: () => import('../views/front/Home') },
      { path: 'person', name: 'Person', meta: { name: 'MyProfile' }, component: () => import('../views/front/Person') },
      { path: 'course', name: 'Course', meta: { name: 'AllCourses' }, component: () => import('../views/front/Course') },
      { path: 'courseDetail', name: 'CourseDetail', meta: { name: 'CourseDetail' }, component: () => import('../views/front/CourseDetail') },
      { path: 'myInfo', name: 'MyInfo', meta: { name: 'MyCourseInformation' }, component: () => import('../views/front/MyInfo') },
      { path: 'information', name: 'Information', meta: { name: 'CourseInformation' }, component: () => import('../views/front/Information') },
      { path: 'informationDetail', name: 'InformationDetail', meta: { name: 'InformationDetail' }, component: () => import('../views/front/InformationDetail') },
    ]
  },
  { path: '/login', name: 'Login', meta: { name: 'Login' }, component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', meta: { name: 'Register' }, component: () => import('../views/Register.vue') },
  { path: '*', name: 'NotFound', meta: { name: 'Error' }, component: () => import('../views/404.vue') },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})


// // router guard
// router.beforeEach((to ,from, next) => {
//   let user = JSON.parse(localStorage.getItem("xm-user") || '{}');
//   if (to.path === '/') {
//     if (user.role) {
//       if (user.role === 'USER') {
//         next('/front/home')
//       } else {
//         next('/home')
//       }
//     } else {
//       next('/login')
//     }
//   } else {
//     next()
//   }
// })

export default router
