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
      <v-card-title><b>魚登録</b></v-card-title>
      <v-select
        v-model="form.id"
        :items="fishList"
        item-text="name"
        item-value="id"
        label="編集する場合は選択してください"
        flat
        @change="this.showFish"
      ></v-select>
      <v-divider></v-divider>
      <v-form ref="form" class="mt-5">
        <v-text-field
          v-model="form.name"
          type="text"
          label="名前"
          :rules="[required, length100]"
        />
        <v-text-field
          v-model="form.imageKey"
          type="text"
          label="画像key"
          :rules="[length1000]"
          @change="this.setImageUrl"
        />
      </v-form>
      <v-img :src="imageUrl"></v-img>
      <v-btn color="accent" class="my-4" @click="this.send">
        <v-icon dark left> mdi-checkbox-marked-circle </v-icon>
        登録
      </v-btn>
    </v-card>
  </div>
</template>

<script>
export default {
  name: "EditFish",
  mounted() {
    this.getTargetList();
  },
  data: () => ({
    form: {},
    messages: [],
    fishList: [],
    imageUrl: "",
    imageBaseUrl: "",
    // 入力規則
    required: (value) => !!value || "入力してください", // 入力必須の制約
    length100: (value) =>
      !value || value.length <= 100 || "100文字以内で入力してください", // 文字数の制約
    length1000: (value) =>
      !value || value.length <= 1000 || "1000文字以内で入力してください", // 文字数の制約
    successAlert: false,
  }),
  methods: {
    getTargetList() {
      this.$axios
        .get("/fishes/edit/init")
        .then((res) => {
          this.fishList = res.data.fishList;
          this.fishList.unshift({ id: "", name: "【新規】" });
          this.imageBaseUrl = res.data.imageBaseUrl;
          this.initInput();
        })
        .catch((err) => {
          alert(err);
        });
    },
    initInput() {
      this.form = {};
      this.form.id = "";
      this.imageUrl = "";
    },
    showFish() {
      if (this.form.id) {
        this.$axios
          .get("/fishes/edit/" + this.form.id)
          .then((res) => {
            this.form = res.data;
            this.setImageUrl();
          })
          .catch((err) => {
            alert(err);
          });
      } else {
        this.initInput();
      }
    },
    setImageUrl() {
      this.imageUrl = this.imageBaseUrl + this.form.imageKey;
    },
    send() {
      if (this.$refs.form.validate()) {
        let config = {
          headers: {
            "content-type": "application/json",
          },
        };

        this.$axios
          .post("/fishes", this.form, config)
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
