import Vue from 'vue';
import Vuex from 'vuex';
import axios from 'axios';

Vue.use(Vuex);
Vue.config.devtools = true;

export default new Vuex.Store({
  state: {
    id: null,
    selectedUser: null,
    loginName: null,
    isAdmin: false
  },
  getters: {
    id: state => state.id,
    isAuthenticated: state => {
      return state.id != null;
    },
    selectedUser: state => state.selectedUser,
    loginName: state => state.loginName,
    isAdmin: state => state.isAdmin,
  },
  mutations: {
    updateId(state, id) {
      state.id = id;
    },
    resetData(state) {
      state.id = null;
      state.loginName = null;
      state.isAdmin = false;
    },
    selectUser(state, user) {
      state.selectedUser = user == "" ? null : user;
    },
    updateLoginName(state, loginName) {
      state.loginName = loginName;
    },
    updateIsAdmin(state, isAdmin) {
      state.isAdmin = isAdmin;
    },
  },
  actions: {
    async login({ commit }, authData) {
      await axios
        .post(
          '/login',
          {
            id: authData.id,
            password: authData.password
          }
        )
        .then(() => {
          commit('updateId', authData.id);
          axios
            .get(
              '/users/login'
            )
            .then((res) => {
              commit('updateLoginName', res.data.name);
              commit('updateIsAdmin', res.data.isAdmin);
            });
        });
    },
    logout({ commit }) {
      axios
        .post(
          '/logout'
        )
        .then(() => {
          commit('resetData');
        });
    },
    selectUser({ commit }, user) {
      commit('selectUser', user);
    },
  }
});
