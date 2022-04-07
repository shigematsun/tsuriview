<template>
  <v-row class="mt-15 mx-auto" style="max-width: 1000px">
    <v-alert
      type="success"
      v-model="successAlert"
      dismissible
      class="mt-4"
      align="center"
    >
      登録しました
    </v-alert>

    <v-col cols="12" v-if="editing !== null">
      <v-card max-width="1000" class="mx-auto">
        <v-card-text>
          <div class="mt-4">
            <h2 v-if="editing.id">釣行編集：{{ editing.id }}</h2>
            <h2 v-else>釣行登録</h2>
          </div>

          <v-btn color="accent" class="my-4" @click="this.send">
            <v-icon dark left> mdi-checkbox-marked-circle </v-icon>
            登録
          </v-btn>

          <v-divider></v-divider>
          <v-form ref="form">
            <div class="d-inline-flex">
              <v-text-field
                v-model="editing.date"
                type="date"
                label="日付"
                class="mr-4"
                :rules="[required]"
              />
            </div>
            <div class="d-inline-flex">
              <v-text-field
                v-model="editing.startTime"
                type="time"
                label="開始時刻"
                class="mr-4"
                :rules="[required]"
              />
              <v-text-field
                v-model="editing.endTime"
                type="time"
                label="終了時刻"
                class="mr-4"
                :rules="[required]"
              />
            </div>
            <div class="d-flex">
              <div class="d-inline-flex mr-4">
                <v-select
                  v-model="editing.prefecture"
                  :items="prefectureList"
                  item-text="name"
                  item-value="id"
                  label="都道府県"
                  :rules="[required]"
                  @change="this.setPlaceList"
                >
                </v-select>
              </div>
              <div class="d-inline-flex">
                <v-select
                  v-model="editing.place"
                  :items="placeList"
                  item-text="name"
                  item-value="id"
                  label="場所"
                  :rules="[required]"
                  no-data-text="都道府県を選択してください"
                ></v-select>
              </div>
              <v-icon> mdi-map-marker </v-icon>
            </div>
            <v-data-table
              :headers="fishHeaders"
              :items="fishItems"
              hide-default-footer
              disable-sort
              no-data-text="釣果なし"
            >
              <template #[`item.fish`]="{ item }">
                <v-select
                  v-model="item.fish"
                  :items="fishList"
                  item-text="name"
                  item-value="id"
                  :rules="[required]"
                  flat
                />
              </template>
              <template #[`item.count`]="{ item }">
                <v-text-field
                  v-model="item.count"
                  label=""
                  type="number"
                  min="1"
                  :rules="[required]"
                  required
                >
                </v-text-field>
              </template>
              <template #[`item.min`]="{ item }">
                <v-text-field
                  v-model="item.min"
                  label="cm"
                  type="number"
                  min="1"
                  :rules="[required]"
                  required
                >
                </v-text-field>
              </template>
              <template #[`item.max`]="{ item }">
                <v-text-field
                  v-model="item.max"
                  label="cm"
                  type="number"
                  min="1"
                  required
                >
                </v-text-field>
              </template>
              <template #[`item.method`]="{ item }">
                <v-select
                  v-model="item.method"
                  :items="methodList"
                  item-text="name"
                  item-value="id"
                  :rules="[required]"
                  flat
                />
              </template>
              <template #[`item.time`]="{ item }">
                <v-text-field
                  v-model="item.time"
                  type="time"
                  :rules="[required]"
                />
              </template>
              <template #[`item.delete`]="{ item }">
                <v-icon small @click="deleteFish(item)"> mdi-delete </v-icon>
              </template>
            </v-data-table>
            <v-btn class="mb-4" @click="this.addFish">
              <v-icon dark left> mdi-fish </v-icon>
              魚追加
            </v-btn>
            <div class="d-flex">
              <v-textarea
                id="memo"
                v-model="editing.memo"
                label="メモ"
                required
                filled
                rows="10"
                :rules="[length1000]"
              >
              </v-textarea>
            </div>

            <v-dialog v-model="imageDialog" width="500">
              <template v-slot:activator="{ on, attrs }">
                <v-btn v-bind="attrs" v-on="on">
                  <v-icon dark left> mdi-file-image-plus </v-icon>
                  画像選択
                </v-btn>
              </template>
              <v-row>
                <v-col
                  v-for="image in imageList"
                  :key="image.id"
                  class="d-flex child-flex"
                  cols="4"
                >
                  <v-img
                    :src="image.url"
                    aspect-ratio="1"
                    class="grey lighten-2"
                    @click="selectImage(image)"
                  >
                    <template v-slot:placeholder>
                      <v-row
                        class="fill-height ma-0"
                        align="center"
                        justify="center"
                      >
                        <v-progress-circular
                          indeterminate
                          color="grey lighten-5"
                        >
                        </v-progress-circular>
                      </v-row>
                    </template>
                  </v-img>
                </v-col>
              </v-row>
            </v-dialog>

            <v-row>
              <v-col
                v-for="image in selectedImages"
                :key="image.id"
                class="d-flex child-flex"
                cols="4"
              >
                <v-img
                  :src="image.url"
                  class="grey lighten-2 d-flex align-center"
                  @click="deleteImage(image)"
                  gradient="to top right, rgba(100,115,201,.33), rgba(25,32,72,.7)"
                >
                  <v-layout justify-center>
                    <v-icon dark center> mdi-file-image-remove </v-icon>
                  </v-layout>
                </v-img>
              </v-col>
            </v-row>

            <v-btn color="accent" class="mr-4 mt-10" @click="this.send">
              <v-icon dark left> mdi-checkbox-marked-circle </v-icon>
              登録
            </v-btn>
          </v-form>
        </v-card-text>
      </v-card>
      <v-card max-width="1000" class="mx-auto">
        <v-card-title>画像アップロード</v-card-title>
        <v-card-actions>
          <v-form>
            <v-file-input
              small-chips
              multiple
              accept="image/*"
              label="ファイル"
              v-on:change="selectFile"
            >
            </v-file-input>
            <v-btn @click="this.uploadFiles">
              <v-icon dark left> mdi-cloud-upload </v-icon>
              アップロード
            </v-btn>
          </v-form>
        </v-card-actions>
        <v-card-text>
          <div v-for="message in this.messages" v-bind:key="message">
            <span>{{ message }}</span>
          </div>
        </v-card-text>
      </v-card>
    </v-col>
  </v-row>
</template>

<script>
export default {
  name: "EditEntry",
  components: {},
  mounted() {
    this.initInput();
    this.editing = {};
    this.editing.id =
      this.$route.params.id === "new" ? null : this.$route.params.id;
    this.fishItems = [{}];

    let params = {};
    params.params = {};
    if (this.editing.id) params.params.id = this.editing.id;

    this.$axios
      .get("/entries/edit/init", params)
      .then((res) => {
        this.prefectureList = res.data.prefectureList;
        this.placeListMap = res.data.placeListMap;
        this.fishList = res.data.fishList;
        this.methodList = res.data.methodList;
        if (this.editing.id) {
          this.editing = res.data.entry;
          this.fishItems = res.data.entry.fishInfoList;
          this.selectedImages = res.data.entry.imageList;
          // 場所リストの設定と選択
          this.setPlaceList();
          this.editing.place = res.data.entry.place;
          // 日付形式を変換
          let date = new Date(this.editing.date);
          var twoDigit = function (value) {
            return ("0" + value).slice(-2);
          };
          this.editing.date = [
            date.getFullYear(),
            twoDigit(date.getMonth() + 1),
            twoDigit(date.getDate()),
          ].join("-");
        }
      })
      .catch((err) => {
        alert(err);
      });
    this.$axios
      .get("/images")
      .then((res) => {
        this.imageList = res.data;
      })
      .catch((err) => {
        alert(err);
      });
  },
  complete: function () {
    // clearInterval(this.timer)
  },
  data: () => ({
    editing: null,
    fishItems: [],
    selectedImages: [],
    imageDialog: false,
    messages: [],
    imageList: [],
    prefectureList: [],
    placeListMap: {},
    placeList: [],
    fishList: [],
    methodList: [],
    fishHeaders: [
      { text: "魚", value: "fish", align: "center" },
      { text: "数", value: "count", align: "center", width: "100px" },
      { text: "最小", value: "min", align: "center" },
      { text: "最大", value: "max", align: "center" },
      { text: "釣り方", value: "method", align: "center" },
      { text: "時刻", value: "time", align: "center" },
      {
        text: "削除",
        value: "delete",
        align: "center",
        width: "70px",
        sortable: false,
      },
    ],
    selectedFiles: [],
    // 入力規則
    required: (value) => !!value || "入力してください", // 入力必須の制約
    length1000: (value) =>
      !value || value.length <= 1000 || "1000文字以内で入力してください", // 文字数の制約
    successAlert: false,
  }),
  methods: {
    initInput() {
      this.editing = {};
      this.editing.id = null;
      this.fishItems = [{}];
      this.selectedImages = [];
      this.selectedFiles = [];
    },
    setPlaceList() {
      this.placeList = this.placeListMap[this.editing.prefecture];
    },
    addFish() {
      this.fishItems.push({});
      // 先頭に追加されてしまうため再設定
      let tmp = this.fishItems;
      this.fishItems = tmp;
    },
    deleteFish(item) {
      this.fishItems.splice(this.fishItems.indexOf(item), 1);
    },
    selectImage(image) {
      this.selectedImages.push(image);
      this.imageDialog = false;
    },
    deleteImage(image) {
      this.selectedImages.splice(this.selectedImages.indexOf(image), 1);
    },
    selectFile(event) {
      this.selectedFiles = event;
    },
    uploadFiles() {
      this.messages = [];

      for (let i = 0; i < this.selectedFiles.length; i++) {
        this.upload(i, this.selectedFiles[i]);
      }
    },
    send() {
      if (!this.editing) {
        return;
      }
      if (this.$refs.form.validate()) {
        this.editing.fishInfoList = this.fishItems;
        this.editing.imageList = this.selectedImages;
        let config = {
          headers: {
            "content-type": "application/json",
          },
        };

        this.$axios
          .post("/entries", this.editing, config)
          .then(() => {
            this.successAlert = true;
            this.initInput();
            this.$refs.form.reset();
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
    upload(idx, file) {
      let formData = new FormData();
      formData.append("file", file);
      let config = {
        headers: {
          "content-type": "multipart/form-data",
        },
      };
      this.$axios
        .post("/images", formData, config)
        .then((res) => {
          this.messages.push(res.data.message);

          // 画像リストの再取得
          this.$axios
            .get("/images")
            .then((res) => {
              this.imageList = res.data;
            })
            .catch((err) => {
              alert(err);
            });
        })
        .catch((err) => {
          alert(err);
        });
    },
  },
};
</script>
