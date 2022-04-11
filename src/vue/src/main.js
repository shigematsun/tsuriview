import Vue from 'vue'
import App from './App.vue'
import Entry from "./pages/Entry"
import Login from "./pages/Login"
import EditEntry from "./pages/EditEntry"
import EditMethod from "./pages/EditMethod"
import EditFish from "./pages/EditFish"
import EditPlace from "./pages/EditPlace"
import Top from "./pages/Top"
import SearchEntries from "./pages/SearchEntries"
import Place from "./pages/Place"
import Fish from "./pages/Fish"
import FishList from "./pages/FishList"
import vuetify from './plugins/vuetify'
import "vuetify/dist/vuetify.min.css"
import '@mdi/font/css/materialdesignicons.css'
import VueRouter from 'vue-router'
import axios from 'axios';
import store from './store';


Vue.use(require('vue-script2'))
Vue.use(VueRouter)

Vue.config.productionTip = false;

axios.defaults.baseURL = '/api';
// Cookieを有効にする。
axios.defaults.withCredentials = true;

Vue.prototype.$axios = axios;

const routes = [
    {path: '/login', component: Login},
    {path: '/entry/:id', component: Entry},
    {path: '/place/:id?', component: Place},
    {path: '/fish/:id', component: Fish},
    {path: '/fish/', component: FishList},
    {path: '/edit/entry/:id', component: EditEntry},
    {path: '/edit/method', component: EditMethod},
    {path: '/edit/fish', component: EditFish},
    {path: '/edit/place', component: EditPlace},
    {path: '/entries', component: SearchEntries},
    {path: '/', component: Top},
]

const router = new VueRouter({
    mode: 'history',
    scrollBehavior() {
        return {x: 0, y: 0}
    },
    routes
})

router.afterEach(() => {
//    gtag('config', 'UA-3817632-9', {'page_path': to.path});
})

new Vue({
    router,
    store,
    vuetify,
    render: h => h(App)
}).$mount('#app')