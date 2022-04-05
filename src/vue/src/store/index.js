import Vue from 'vue';
import Vuex from 'vuex';
import axios from 'axios';

Vue.use(Vuex);
Vue.config.devtools = true;

export default new Vuex.Store({
  state: {
    id: null,
  },
  getters: {
    id: state => state.id,
    isAuthenticated: state => {
      return state.id != null;
    },
  },
  mutations: {
    updateId(state, id) {
      state.id = id;
    },
    resetData(state) {
      state.id = null;
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
  }
});
