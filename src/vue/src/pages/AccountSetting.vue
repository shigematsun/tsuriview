<template>
  <div class="mt-15 mx-auto" style="max-width: 500px">
    <v-alert
      type="success"
      v-model="successAlert"
      dismissible
      class="mt-4"
      align="center"
    >
      更新しました
    </v-alert>

    <v-card class="mx-auto pa-5">
      <v-card-title><b>アカウント設定</b></v-card-title>
      <v-form ref="form" class="mt-5">
        <v-text-field
          v-model="form.name"
          type="text"
          label="表示名"
          :rules="[required]"
        />
        <v-text-field
          v-model="form.password"
          type="password"
          label="パスワード"
        />
        <v-text-field
          v-model="form.password2"
          type="password"
          label="パスワード（確認）"
          :rules="[same]"
        />
      </v-form>
      <v-btn color="accent" class="my-4" @click="this.update">
        <v-icon dark left> mdi-checkbox-marked-circle </v-icon>
        更新
      </v-btn>
    </v-card>
  </div>
</template>

<script>
export default {
  name: "AccountSetting",
  mounted() {
    this.$axios
      .get("/users/setting/")
      .then((res) => {
        this.form = res.data;
      })
      .catch((err) => {
        alert(err);
      });
  },
  data: () => ({
    form: {},
    name: "",
    password: "",
    password2: "",
    successAlert: false,
  }),
  computed: {
    required() {
      return (value) => !!value || "入力してください";
    }, // 入力必須の制約
    same() {
      return (value) => value == this.form.password || "パスワードが一致しません";
    }, // パスワード一致
  },
  methods: {
    async update() {
      if (this.$refs.form.validate()) {
        let config = {
          headers: {
            "content-type": "application/json",
          },
        };

        this.$axios
          .post("/users/setting", this.form, config)
          .then(() => {
            this.successAlert = true;
            this.initInput();
            this.$refs.form.resetValidation();
            this.$store.commit('updateLoginName', this.form.name);
            window.scrollTo({
              top: 0,
              behavior: "smooth",
            });
          })
          .catch((err) => {
            alert(err);
          });
      }
    },
    initInput() {
      this.form.password = "";
      this.form.password2 = "";
    },
  },
};
</script>
