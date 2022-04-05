<template>
  <div class="mt-15 mx-auto" style="max-width: 1200px">
    <v-card class="mx-auto pa-5">
      <v-row>
        <v-icon> mdi-filter </v-icon>
        絞込条件
      </v-row>
      <v-row align="center" justify="center">
        <div class="mx-auto">
          <v-text-field
            v-model="condition.fromDate"
            type="date"
            label="日付from"
          />
        </div>
        <p>～</p>
        <div class="mx-auto">
          <v-text-field v-model="condition.toDate" type="date" label="日付to" />
        </div>
        <div class="mx-auto" style="width: 50px">
          <v-select v-model="condition.month" :items="monthList" label="月">
          </v-select>
        </div>
        <div class="mx-auto" style="width: 100px">
          <v-select
            v-model="condition.prefecture"
            :items="prefectureList"
            item-text="name"
            item-value="id"
            label="都道府県"
            @change="this.setPlaceList"
          >
          </v-select>
        </div>
        <div class="mx-auto" style="width: 200px">
          <v-select
            v-model="condition.place"
            :items="placeList"
            item-text="name"
            item-value="id"
            label="場所"
            no-data-text="都道府県を選択してください"
          ></v-select>
        </div>
        <div class="mx-auto" style="width: 200px">
          <v-select
            v-model="condition.fish"
            :items="fishList"
            item-text="name"
            item-value="id"
            label="魚"
          ></v-select>
        </div>
        <div class="mx-auto" style="width: 200px">
          <v-select
            v-model="condition.method"
            :items="methodList"
            item-text="name"
            item-value="id"
            label="釣り方"
          ></v-select>
        </div>
      </v-row>
      <v-row>
        <v-btn class="ml-auto mr-10 pr-7" @click="this.clear">
          <v-icon dark> mdi-filter-remove </v-icon>
          クリア
        </v-btn>
        <v-btn color="accent" class="mr-auto pr-7" @click="this.search">
          <v-icon dark> mdi-magnify </v-icon>
          検索
        </v-btn>
      </v-row>
    </v-card>
    <Entries :condition="condition" ref="entries"></Entries>
  </div>
</template>

<script>
import Entries from "../components/Entries.vue";

export default {
  name: "SearchEntries",
  components: { Entries },
  data: () => ({
    condition: {},
    prefectureList: [],
    placeListMap: {},
    placeList: [],
    fishList: [],
    methodList: [],
    monthList: ["", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
  }),
  mounted() {
    this.$axios
      .get("/entries/init")
      .then((res) => {
        this.prefectureList = res.data.prefectureList;
        this.prefectureList.unshift({ id: "", name: "" });
        this.placeListMap = res.data.placeListMap;
        this.fishList = res.data.fishList;
        this.fishList.unshift({ id: "", name: "" });
        this.methodList = res.data.methodList;
        this.methodList.unshift({ id: "", name: "" });
      })
      .catch((err) => {
        alert(err);
      });

      this.$refs.entries.loadEntries();
  },
  watch: {},
  computed: {},
  methods: {
    search() {
      this.$refs.entries.page = 1;
      this.$refs.entries.loadEntries();
    },
    clear() {
      this.condition = {};
    },
    setPlaceList() {
      this.placeList = this.placeListMap[this.condition.prefecture];
      if (this.condition.prefecture !== "") this.placeList.unshift({ id: "", name: "" });
    },
  },
};
</script>
