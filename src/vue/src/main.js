import Vue from 'vue'
import App from './App.vue'
import axios from 'axios';
import store from './store';

Vue.config.productionTip = false

axios.defaults.baseURL = '/api';

Vue.prototype.$axios = axios;

// Cookieを有効にする。
axios.defaults.withCredentials = true;

new Vue({
    store,
    render: h => h(App)
  }).$mount('#app');
