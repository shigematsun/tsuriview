<template>
  <div class="mt-15 mx-auto" style="max-width: 1200px">
    <v-card class="mx-auto pa-5">
      <v-row>
        <v-icon> mdi-filter </v-icon>
        絞込条件
      </v-row>
      <v-row align="center" justify="center">
        <v-col cols="6" sm="5" md="3" lg="2" xl="2">
        <!-- <div class="mx-auto" style="width: 170px"> -->
          <v-text-field
            v-model="condition.fromDate"
            type="date"
            label="日付（開始～）"
            class="inputDate"
          />
        <!-- </div> -->
        </v-col>
        <v-col cols="6" sm="5" md="3" lg="2" xl="2">
        <!-- <div class="mx-auto" style="width: 170px"> -->
          <v-text-field v-model="condition.toDate" type="date" label="日付（～終了）" />
        <!-- </div> -->
        </v-col>
        <v-col cols="3" sm="2" md="2" lg="1" xl="1">
        <!-- <div class="mx-auto" style="width: 50px"> -->
          <v-select v-model="condition.month" :items="monthList" label="月">
          </v-select>
        </v-col>
        <!-- </div> -->
        <!-- <div class="mx-auto" style="width: 100px"> -->
        <v-col cols="4" sm="6" md="4" lg="1" xl="1">
          <v-select
            v-model="condition.prefecture"
            :items="prefectureList"
            item-text="name"
            item-value="id"
            label="都道府県"
            @change="this.setPlaceList"
          >
          </v-select>
        </v-col>
        <!-- </div> -->
        <!-- <div class="mx-auto" style="width: 150px"> -->
        <v-col cols="5" sm="6" md="4" lg="2" xl="2">
          <v-select
            v-model="condition.place"
            :items="placeList"
            item-text="name"
            item-value="id"
            label="場所"
            no-data-text="都道府県を選択してください"
          ></v-select>
        </v-col>
        <!-- </div> -->
        <!-- <div class="mx-auto" style="width: 150px"> -->
        <v-col cols="6" sm="6" md="4" lg="2" xl="2">
          <v-select
            v-model="condition.fish"
            :items="fishList"
            item-text="name"
            item-value="id"
            label="魚"
          ></v-select>
        </v-col>
        <!-- </div> -->
        <!-- <div class="mx-auto" style="width: 150px"> -->
        <v-col cols="6" sm="6" md="4" lg="2" xl="2">
          <v-select
            v-model="condition.method"
            :items="methodList"
            item-text="name"
            item-value="id"
            label="釣り方"
          ></v-select>
        </v-col>
        <!-- </div> -->
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
    <div class="px-2">
      <Entries :condition="condition" ref="entries"></Entries>
    </div>
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
      if (this.condition.prefecture !== "")
        this.placeList.unshift({ id: "", name: "" });
    },
  },
};
</script>
