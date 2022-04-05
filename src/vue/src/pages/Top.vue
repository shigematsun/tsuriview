<template>
  <div class="mt-15 mx-auto" style="max-width: 1200px">
    <v-card class="mx-auto pa-5">
      <v-row>
        <v-card-title><b>最新の釣行</b></v-card-title>
      </v-row>

      <v-row class="mt-5">
        <v-col v-for="entry in entryList" :key="entry.id">
          <v-card width="200" :to="'/entry/' + entry.id">
            <v-img
              v-if="entry.imageUrl"
              :src="entry.imageUrl"
              height="160"
            ></v-img>
            <v-img v-else height="160" class="grey align-center">
              <v-layout justify-center>
                <v-icon dark> mdi-image-off </v-icon>
              </v-layout>
            </v-img>

            <v-card-text>
              <v-row align="center">
                <v-icon> mdi-calendar </v-icon>
                {{ entry.date }}
              </v-row>
              <v-row align="center">
                <v-icon> mdi-map-marker </v-icon>
                {{ entry.place }}
              </v-row>
              <v-row align="center">
                <v-chip
                  v-for="(fish, index) in entry.fishList"
                  :key="index"
                  color="primary"
                  small
                  class="ml-1 mb-1"
                >
                  {{ fish }}
                </v-chip>
              </v-row>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>

      <v-row class="mb-0">
        <v-btn color="accent" class="mx-auto" to="/entries"> もっと見る </v-btn>
      </v-row>
    </v-card>

    <v-card class="mx-auto pa-5 mt-5">
      <v-row>
        <div class="ml-5" style="width: 50px">
          <v-select
            v-model="month"
            :items="monthList"
            label=""
            @change="this.loadFishPlace"
          >
          </v-select>
        </div>
        <v-card-title><b>月に釣れた魚</b></v-card-title>
      </v-row>

      <v-row class="mt-5">
        <div
          v-for="fish in fishList"
          :key="fish.fishId"
          class="ma-4"
          style="width: 200px"
        >
          <router-link :to="'/fish/' + fish.fishId">
            <v-avatar size="200">
              <img :src="fish.imageUrl" :alt="fish.name" />
            </v-avatar>
            <div class="text-center">
              <b>{{ fish.name }}</b>
            </div>
          </router-link>
        </div>
      </v-row>

      <v-row class="mb-0">
        <v-btn color="accent" class="mx-auto" to="/fish"> もっと見る </v-btn>
      </v-row>
    </v-card>

    <v-card class="mx-auto pa-5 mt-5">
      <v-row>
        <div class="ml-5" style="width: 50px">
          <v-select
            v-model="month"
            :items="monthList"
            label=""
            @change="this.loadFishPlace"
          >
          </v-select>
        </div>
        <v-card-title><b>月に釣れた場所</b></v-card-title>
      </v-row>

      <v-row class="mt-5">
        <v-col v-for="place in placeList" :key="place.placeId">
          <v-card width="200" :to="'/place/' + place.placeId">
            <v-card-title class="text-subtitle-1">
              <b>{{ place.name }}</b>
            </v-card-title>
            <v-card-text>
              <v-row align="center">
                <v-icon class="ml-1"> mdi-map</v-icon>
                {{ place.prefecture }}
              </v-row>
              <v-row align="center">
                <v-chip
                  v-for="(fish, index) in place.fishNameList"
                  :key="index"
                  color="primary"
                  small
                  class="ml-1 mb-1"
                >
                  {{ fish }}
                </v-chip>
              </v-row>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>

      <v-row class="mb-0">
        <v-btn color="accent" class="mx-auto" to="/place"> もっと見る </v-btn>
      </v-row>
    </v-card>
  </div>
</template>

<script>
export default {
  name: "Top",
  data: () => ({
    entryList: [],
    fishList: [],
    placeList: [],
    monthList: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
    month: 0,
  }),
  mounted() {
    let now = new Date();
    this.month = now.getMonth() + 1;

    this.$axios.get("/entries/top").then((res) => {
      this.entryList = res.data.entryList;
    });

    this.loadFishPlace();
  },
  computed: {},
  methods: {
    loadFishPlace() {
      this.$axios
        .get("/fishes/top", { params: { month: this.month } })
        .then((res) => {
          this.fishList = res.data.fishList;
        });
      this.$axios
        .get("/places/top", { params: { month: this.month } })
        .then((res) => {
          this.placeList = res.data.placeList;
        });
    },
  },
};
</script>
