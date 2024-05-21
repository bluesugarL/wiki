import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AboutView from '../views/AboutView.vue'
import DocView from '../views/DocView.vue'
import AdminUser from '../views/admin/admin-user.vue'
import AdminEbook from '../views/admin/admin-ebook.vue'
import AdminCategory from '../views/admin/admin-category.vue'
import AdminDoc from '../views/admin/admin-doc.vue'

const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'home',
        component: HomeView
    },
    {
        path: '/about',
        name: 'about',
        component: AboutView
    },
    {
        path: '/admin/ebook',
        name: 'ebook',
        component: AdminEbook
    },
    {
        path: '/admin/category',
        name: 'category',
        component: AdminCategory
    },
    {
        path: '/admin/doc',
        name: 'doc',
        component: AdminDoc
    },
    {
        path: '/doc',
        name: 'DocView',
        component: DocView
    },
    {
        path: '/admin/user',
        name: 'AdminUser',
        component: AdminUser
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
