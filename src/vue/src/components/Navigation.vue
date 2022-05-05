<template>
  <div class="navigation">
    <v-navigation-drawer
      v-model="drawer"
      app
      right
      dark
      class="dark-green darken-4"
      disable-resize-watcher
    >
      <v-list dense>
        <v-list-item
          v-if="!this.$store.getters.isAuthenticated"
          link
          to="/login"
        >
          <v-list-item-action>
            <v-icon>mdi-login</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>ログイン</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <template v-if="this.$store.getters.isAuthenticated">
          <v-subheader>{{ this.$store.getters.loginName }}さん</v-subheader>
          <v-list-item link to="/edit/entry/new">
            <v-list-item-action>
              <v-icon>mdi-note-edit</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>釣行登録</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <v-list-item link to="/accountSetting">
            <v-list-item-action>
              <v-icon>mdi-account-circle</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>アカウント設定</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
          <v-list-item link to="/" @click="$store.dispatch('logout')">
            <v-list-item-action>
              <v-icon>mdi-logout</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>ログアウト</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </template>

        <template v-if="this.$store.getters.isAdmin">
          <v-divider></v-divider>
          <v-subheader>管理メニュー</v-subheader>

          <v-list-item link to="/edit/method">
            <v-list-item-action>
              <v-icon>mdi-toolbox</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>釣り方登録</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item link to="/edit/fish">
            <v-list-item-action>
              <v-icon>mdi-fish</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>魚登録</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item link to="/edit/place">
            <v-list-item-action>
              <v-icon>mdi-map-marker</v-icon>
            </v-list-item-action>
            <v-list-item-content>
              <v-list-item-title>場所登録</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </template>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar app color="primary" dark>
      <v-toolbar-title>
        <router-link
          to="/"
          class="mr-10"
          style="color: inherit; text-decoration: none"
        >
          <v-icon large class="mt-n1"> mdi-fish </v-icon>
          <b>ツリビュー</b>
        </router-link>
      </v-toolbar-title>

      <v-spacer />
      <div class="mt-6" style="width: 90px">
        <v-select
          v-model="selectedUser"
          :items="userList"
          item-text="name"
          item-value="id"
          label="データ選択"
          @change="this.setUser"
        ></v-select>
      </div>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" />

      <template v-slot:extension>
        <v-tabs>
          <v-tab to="/">トップ</v-tab>
          <v-tab to="/entries">釣行</v-tab>
          <v-tab to="/fish">魚</v-tab>
          <v-tab to="/place">場所</v-tab>
        </v-tabs>
      </template>
    </v-app-bar>
  </div>
</template>

<script>
export default {
  name: "Navigation",
  data: () => ({
    drawer: false,
    selectedUser: "",
    userList: [],
  }),
  mounted() {
    this.$axios.get("/users").then((res) => {
      this.userList = res.data.userList;
      this.userList.unshift({ id: "", name: "全員" });
    });
  },
  methods: {
    // ユーザ選択時
    setUser() {
      this.$store.dispatch("selectUser", this.selectedUser);
    },
  },
};
</script>

<style scoped>
.navigation {
  margin-bottom: 16px;
}
</style>