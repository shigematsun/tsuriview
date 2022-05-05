<template>
  <div class="mt-15 mx-auto" style="max-width: 500px">
    <v-alert
      type="success"
      v-model="successAlert"
      dismissible
      class="mt-4"
      align="center"
    >
      登録しました
    </v-alert>

    <v-card class="mx-auto pa-5">
      <v-card-title><b>釣り方登録</b></v-card-title>
      <v-select
        v-model="form.id"
        :items="methodList"
        item-text="name"
        item-value="id"
        label="編集する場合は選択してください"
        flat
        @change="this.showMethod"
      ></v-select>
      <v-divider></v-divider>
      <v-form ref="form" class="mt-5">
        <v-text-field
          v-model="form.name"
          type="text"
          label="名前"
          :rules="[required, length100]"
        />
      </v-form>
      <v-btn color="accent" class="my-4" @click="this.send">
        <v-icon dark left> mdi-checkbox-marked-circle </v-icon>
        登録
      </v-btn>
    </v-card>
  </div>
</template>

<script>
export default {
  name: "EditMethod",
  mounted() {
    this.getTargetList();
  },
  data: () => ({
    form: {},
    messages: [],
    methodList: [],
    // 入力規則
    required: (value) => !!value || "入力してください", // 入力必須の制約
    length100: (value) =>
      !value || value.length <= 100 || "100文字以内で入力してください", // 文字数の制約
    successAlert: false,
  }),
  methods: {
    getTargetList() {
      this.$axios
        .get("/methods/edit/init")
        .then((res) => {
          this.methodList = res.data.methodList;
          this.methodList.unshift({ id: "", name: "【新規】" });
          this.initInput();
        })
        .catch((err) => {
          alert(err);
        });
    },
    initInput() {
      this.form = {};
      this.form.id = "";
    },
    showMethod() {
      if (this.form.id) {
        this.$axios
          .get("/methods/edit/" + this.form.id)
          .then((res) => {
            this.form = res.data;
          })
          .catch((err) => {
            alert(err);
          });
      } else {
        this.initInput();
      }
    },
    send() {
      if (this.$refs.form.validate()) {
        let config = {
          headers: {
            "content-type": "application/json",
          },
        };

        this.$axios
          .post("/methods", this.form, config)
          .then(() => {
            this.successAlert = true;
            this.initInput();
            this.$refs.form.reset();
            this.getTargetList();
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
  },
};
</script>
