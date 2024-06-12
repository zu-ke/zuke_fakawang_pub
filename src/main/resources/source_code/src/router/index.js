import {createRouter, createWebHistory} from 'vue-router'
import Login from "@/views/Login.vue";
import Register from "@/views/Register.vue";
import Products from "@/views/Products.vue";
import Orders from "@/views/Orders.vue";
import Settings from "@/views/Settings.vue";
import User from "@/views/User.vue";

const routes = [
    {
        path: "/login",
        name: "Login",
        component: Login
    },
    {
        path: "/Register",
        name: "Register",
        component: Register
    },
    {
        path: '/products',
        name: 'Products',
        component: Products
    },
    {
        path: '/orders',
        name: 'Orders',
        component: Orders
    },
    {
        path: '/settings',
        name: 'Settings',
        component: Settings
    },
    {
        path: '/',
        name: 'User',
        component: User
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
