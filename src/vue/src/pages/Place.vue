<template>
  <div class="mt-15">
    <v-card max-width="740" class="mx-auto pa-5">
      <v-row>
        <v-col cols="6">
          <v-select
            v-model="prefectureId"
            :items="prefectureList"
            item-text="name"
            item-value="id"
            label="都道府県"
            @change="this.setPlaceList"
          >
          </v-select>
        </v-col>
        <v-col cols="6">
          <v-select
            v-model="placeId"
            :items="placeList"
            item-text="name"
            item-value="id"
            label="場所"
            no-data-text="都道府県を選択してください"
            @change="this.showData"
          ></v-select>
        </v-col>
      </v-row>
      <div v-if="placeId">
        <v-row class="pt-2">
          <iframe
            :src="this.place.mapUrl"
            width="720"
            height="200"
            style="border: 0"
            allowfullscreen=""
            loading="lazy"
          ></iframe>
        </v-row>
        <v-row class="pt-2">
          <v-divider></v-divider>
        </v-row>
        <v-row>
          <v-col cols="5"> <b>釣行回数</b> </v-col>
          <v-col cols="7"> {{ place.times }} 回</v-col>
        </v-row>
        <v-row>
          <v-col cols="12"> <b>トータル釣果</b> </v-col>
          <v-col cols="12">
            <table border="1" style="border-collapse: collapse">
              <tr v-for="fish in place.fishList" :key="fish.id">
                <td class="primary white--text text-center py-1" nowrap>
                  <b class="mx-2">{{ fish.fishName }}</b>
                </td>
                <td class="text-center px-2" nowrap>{{ fish.count }} 匹</td>
                <td class="text-center px-2">
                  {{ fish.min }}～{{ fish.max }}cm
                </td>
                <td class="text-center px-2">
                  <span v-for="method in fish.methodList" :key="method">
                    {{ method }}
                  </span>
                </td>
              </tr>
            </table>
          </v-col>
        </v-row>
        <v-row>
          <v-col cols="12"> <b>月別の魚</b> </v-col>
          <v-col cols="12">
            <table border="1" style="border-collapse: collapse">
              <tr
                v-for="(monthFish, index) in place.monthFishList"
                :key="index"
              >
                <td class="primary white--text text-center py-1" nowrap>
                  <b class="mx-2">{{ index + 1 }}月</b>
                </td>
                <td class="px-2">
                  <span v-for="fish in monthFish" :key="fish">
                    {{ fish }}
                  </span>
                </td>
              </tr>
            </table>
          </v-col>
        </v-row>
        <v-row>
          <v-card min-width="100%">
            <v-card-title> <b>月別の釣行</b> </v-card-title>
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
          <v-divider></v-divider>
        </v-row>
        <v-row>
          <v-col cols="3" class="pt-8"> <b>潮情報</b> </v-col>
          <v-col cols="3">
            <v-text-field
              v-model="tideDate"
              type="date"
              @change="this.setTideUrl"
            />
          </v-col>
        </v-row>
        <v-row class="pt-2">
          <v-img :src="tideUrl"></v-img>
        </v-row>
        <v-row class="pt-2">
          <v-divider></v-divider>
        </v-row>
        <v-card-title>
          <b>{{ this.place.name }}の釣行</b>
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
  name: "ShowPlace",
  components: {
    Chart,
    Entries,
  },
  data: () => ({
    place: {},
    prefectureId: 0,
    tempPrefectureId: 0,
    placeId: 0,
    tempPlaceId: 0,
    prefectureList: [],
    placeListMap: {},
    placeList: [],
    tideDate: null,
    tideUrl: "",
    condition: {},
    monthChartData: {},

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
  }),
  watch: {
    $route() {
      this.loadPlace();
    },
  },
  methods: {
    loadPlace() {
      let params = {};
      params.params = {};
      params.params.userId = this.$store.getters.selectedUser;

      this.$axios
        .get("/places/init", params)
        .then((res) => {
          this.prefectureList = res.data.prefectureList;
          this.placeListMap = res.data.placeListMap;
          this.placeId = this.$route.params.id;

          if (this.placeId) {
            this.showData();
          }
        })
        .catch((err) => {
          alert(err);
        });
    },
    setPlaceList() {
      this.placeList = this.placeListMap[this.prefectureId];
      this.placeId = null;
    },
    showData() {
      this.loaded = false;
      let params = {};
      params.params = {};
      params.params.userId = this.$store.getters.selectedUser;

      this.$axios
        .get("/places/" + this.placeId, params)
        .then((res) => {
          this.prefectureId = res.data.prefectureId;
          this.setPlaceList();
          this.placeId = res.data.placeId;
          this.place = res.data;

          this.condition.place = res.data.placeId;
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
                label: "回数",
                data: res.data.monthCountList,
                fill: true,
                backgroundColor: "rgba(54, 162, 235, 0.2)",
                borderColor: "rgba(54, 162, 235, 1)",
              },
            ],
          };
          this.loaded = true;

          var twoDigit = function (value) {
            return ("0" + value).slice(-2);
          };
          var _today = new Date();
          this.tideDate = [
            _today.getFullYear(),
            twoDigit(_today.getMonth() + 1),
            twoDigit(_today.getDate()),
          ].join("-");
          this.setTideUrl();
        })
        .catch((err) => {
          alert(err);
        });
    },
    setTideUrl() {
      var dateSplit = this.tideDate.split("-");
      this.tideUrl = this.place.tideBaseUrl
        .replace("YYYY", dateSplit[0])
        .replace("MM", dateSplit[1])
        .replace("DD", dateSplit[2]);
    },
  },
  mounted() {
    this.loadPlace();

    this.$store.watch(
      (state, getters) => getters.selectedUser,
      () => {
        // 別画面でも反応してしまうため
        if (this.$refs.entries && this.placeId) {
          this.showData();
        }
      }
    );
  },
};
</script>
