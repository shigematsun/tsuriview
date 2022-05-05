<template>
  <div class="mt-15">
    <v-card max-width="740" class="mx-auto pa-5">
      <v-row>
        <v-col col="6">
          <v-select
            v-model="fishId"
            :items="fishList"
            item-text="name"
            item-value="id"
            label="魚を選択してください"
            @change="this.showData"
          ></v-select>
        </v-col>
      </v-row>
      <div v-if="fishId">
        <v-row class="pt-2">
          <v-img :src="fish.imageUrl"></v-img>
        </v-row>
        <v-row class="pt-2">
          <v-divider></v-divider>
        </v-row>
        <v-row>
          <v-col cols="5"> <b>トータル釣果</b> </v-col>
          <v-col cols="7"> {{ fish.total }} 匹</v-col>
        </v-row>
        <v-row>
          <v-col cols="5"> <b>サイズ</b> </v-col>
          <v-col cols="3">
            <div>最大</div>
            <div>最小</div>
          </v-col>
          <v-col cols="4">
            <div>{{ fish.max }}cm</div>
            <div>{{ fish.min }}cm</div>
          </v-col>
        </v-row>
        <v-row>
          <v-card min-width="100%">
            <v-card-title> <b>月別の釣果</b> </v-card-title>
            <v-card-text>
              <Chart
                v-if="loaded"
                :chartData="monthChartData"
                :options="monthOptions"
                :height="200"
              />
              <v-progress-circular
                v-else-if="loaded != true"
                indeterminate
                color="#53e09c"
              />
            </v-card-text>
          </v-card>
        </v-row>
        <v-row class="pt-2">
          <v-card min-width="100%">
            <v-card-title> <b>時間別の釣果</b> </v-card-title>
            <v-card-text>
              <Chart
                v-if="loaded"
                :chartData="timeChartData"
                :options="timeOptions"
                :height="200"
              />
              <v-progress-circular
                v-else-if="loaded != true"
                indeterminate
                color="#53e09c"
              />
            </v-card-text>
          </v-card>
        </v-row>
        <v-row class="pt-2">
          <v-divider></v-divider>
        </v-row>
        <v-card-title>
          <b>{{ this.fish.name }}の釣行</b>
        </v-card-title>
        <v-row>
          <v-col cols="12" class="mt-n10 px-0">
            <Entries :condition="condition" ref="entries"></Entries>
          </v-col>
        </v-row>
      </div>
    </v-card>
  </div>
</template>

<script>
import Chart from "../components/LineChart.vue";
import Entries from "../components/Entries.vue";
export default {
  name: "ShowFish",
  components: {
    Chart,
    Entries,
  },
  data: () => ({
    fish: {},
    fishId: 0,
    fishList: [],
    loaded: false,
    condition: {},
    monthChartData: {},
    timeChartData: {},

    monthOptions: {
      responsive: true,
      maintainAspectRatio: false,
      legend: {
        display: true,
      },
      scales: {
        // y軸を整数に
        yAxes: [
          {
            ticks: {
              beginAtZero: true,
              userCallback: function (label) {
                if (Math.floor(label) === label) {
                  return label;
                }
              },
            },
          },
        ],
      },
    },
    timeOptions: {
      responsive: true,
      maintainAspectRatio: false,
      legend: {
        display: true,
      },
      scales: {
        // y軸を整数に
        yAxes: [
          {
            ticks: {
              beginAtZero: true,
              userCallback: function (label) {
                if (Math.floor(label) === label) {
                  return label;
                }
              },
            },
          },
        ],
      },
    },
  }),
  watch: {
    $route() {
      this.loadFish();
    },
  },
  methods: {
    loadFish() {
      let params = {};
      params.params = {};
      params.params.userId = this.$store.getters.selectedUser;

      this.$axios
        .get("/fishes/init", params)
        .then((res) => {
          this.fishList = res.data.fishList;
          this.fishId = this.$route.params.id;
          if (this.fishId) {
            this.showData();
          }
        })
        .catch((err) => {
          alert(err);
        });
    },
    showData() {
      let params = {};
      params.params = {};
      params.params.userId = this.$store.getters.selectedUser;

      this.$axios
        .get("/fishes/" + this.fishId, params)
        .then((res) => {
          this.loaded = false;
          this.fishId = res.data.fishId;
          this.fish = res.data;

          this.condition.fish = res.data.fishId;
          this.$refs.entries.loadEntries();

          this.monthChartData = {
            labels: [
              "1月",
              "2月",
              "3月",
              "4月",
              "5月",
              "6月",
              "7月",
              "8月",
              "9月",
              "10月",
              "11月",
              "12月",
            ],
            datasets: [
              {
                label: "匹",
                data: res.data.monthCountList,
                fill: true,
                backgroundColor: "rgba(54, 162, 235, 0.2)",
                borderColor: "rgba(54, 162, 235, 1)",
              },
            ],
          };

          this.timeChartData = {
            labels: [
              "0時",
              "1時",
              "2時",
              "3時",
              "4時",
              "5時",
              "6時",
              "7時",
              "8時",
              "9時",
              "10時",
              "11時",
              "12時",
              "13時",
              "14時",
              "15時",
              "16時",
              "17時",
              "18時",
              "19時",
              "20時",
              "21時",
              "22時",
              "23時",
            ],
            datasets: [
              {
                label: "匹",
                data: res.data.timeCountList,
                fill: true,
                backgroundColor: "rgba(54, 162, 235, 0.2)",
                borderColor: "rgba(54, 162, 235, 1)",
              },
            ],
          };
          this.loaded = true;
        })
        .catch((err) => {
          alert(err);
        });
    },
  },
  mounted() {
    this.loadFish();
  },
};
</script>
