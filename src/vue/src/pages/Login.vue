<template>
  <div class="mt-15 mx-auto" style="max-width: 500px">
    <v-alert
      type="error"
      v-model="errorAlert"
      dismissible
      class="mt-4"
      align="center"
    >
      ログインできませんでした
    </v-alert>

    <v-card class="mx-auto pa-5">
      <v-card-title><b>ログイン</b></v-card-title>
      <v-form ref="form" class="mt-5">
        <v-text-field v-model="id" type="text" label="ID" :rules="[required]" />
        <v-text-field
          v-model="password"
          type="password"
          label="パスワード"
          :rules="[required]"
        />
      </v-form>
      <v-btn color="accent" class="my-4" @click="this.login">
        <v-icon dark left> mdi-checkbox-marked-circle </v-icon>
        ログイン
      </v-btn>
    </v-card>
  </div>
</template>

<script>
export default {
  name: "Login",
  data: () => ({
    id: "",
    password: "",
    // 入力規則
    required: (value) => !!value || "入力してください", // 入力必須の制約
    errorAlert: false,
  }),
  methods: {
    async login() {
      await this.$store
        .dispatch("login", {
          id: this.id,
          password: this.password,
        })
        .catch(() => {
          this.errorAlert = true;
        });
      if (this.$store.getters.isAuthenticated) {
        this.$router.push({ path: "/edit/entry/new" });
      } else {
        this.id = "";
        this.password = "";
      }
    },
  },
};
</script>
