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
      <v-card-title><b>場所登録</b></v-card-title>
      <v-form ref="form" class="mt-5">
        <v-select
          v-model="form.prefecture"
          :items="prefectureList"
          item-text="name"
          item-value="id"
          label="都道府県"
          @change="this.setPlaceList"
          :rules="[required]"
        >
        </v-select>
        <v-select
          v-model="form.id"
          :items="placeList"
          item-text="name"
          item-value="id"
          label="編集する場合は選択してください"
          no-data-text="都道府県を選択してください"
          flat
          @change="this.showPlace"
        ></v-select>
        <v-divider></v-divider>
        <v-text-field
          v-model="form.name"
          type="text"
          label="名前"
          :rules="[required, length100]"
        />
        <v-text-field
          v-model="form.tidePc"
          type="text"
          label="潮・都道府県コード"
          :rules="[length2]"
        />
        <v-text-field
          v-model="form.tideHc"
          type="text"
          label="潮・港コード"
          :rules="[length2]"
        />
        <v-text-field
          v-model="form.mapUrl"
          type="text"
          label="地図URL"
          :rules="[length1000]"
        />
      </v-form>
      <iframe
        :src="form.mapUrl"
        width="460"
        height="360"
        style="border: 0"
        allowfullscreen=""
        loading="lazy"
      ></iframe>
      <v-btn color="accent" class="my-4" @click="this.send">
        <v-icon dark left> mdi-checkbox-marked-circle </v-icon>
        登録
      </v-btn>
    </v-card>
  </div>
</template>

<script>
export default {
  name: "EditPlace",
  components: {},
  mounted() {
    this.getTargetList();
  },
  complete: function () {
    // clearInterval(this.timer)
  },
  data: () => ({
    form: {},
    messages: [],
    prefectureList: [],
    placeListMap: {},
    placeList: [],
    // 入力規則
    required: (value) => !!value || "入力してください", // 入力必須の制約
    length2: (value) =>
      !value || value.length <= 2 || "2文字以内で入力してください", // 文字数の制約
    length100: (value) =>
      !value || value.length <= 100 || "100文字以内で入力してください", // 文字数の制約
    length1000: (value) =>
      !value || value.length <= 1000 || "1000文字以内で入力してください", // 文字数の制約
    successAlert: false,
  }),
  methods: {
    getTargetList() {
      this.$axios
        .get("/places/edit/init")
        .then((res) => {
          this.prefectureList = res.data.prefectureList;
          this.placeListMap = res.data.placeListMap;
          this.initInput();
          this.$refs.form.resetValidation();
        })
        .catch((err) => {
          alert(err);
        });
    },
    initInput() {
      this.form.id = "";
      this.form.name = "";
      this.form.tidePc = "";
      this.form.tideHc = "";
      this.form.mapUrl = "";
    },
    setPlaceList() {
      this.placeList = this.placeListMap[this.form.prefecture];
      if (!this.placeList) this.placeList = {};
      this.placeList.unshift({ id: "", name: "【新規】" });
      this.form.id = null;
    },
    showPlace() {
      if (this.form.id) {
        this.$axios
          .get("/places/edit/" + this.form.id)
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
          .post("/places", this.form, config)
          .then(() => {
            this.successAlert = true;
            this.initInput();
            this.$refs.form.resetValidation();
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
