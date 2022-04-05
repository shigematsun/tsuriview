<template>
  <div class="mt-15">
    <v-card max-width="740" class="mx-auto pa-5">
      <v-row>
        <v-btn
          v-if="this.entry.prevId"
          class="mr-auto"
          link
          :to="'/entry/' + this.entry.prevId"
        >
          ◀ 前
        </v-btn>
        <v-btn
          v-if="this.$store.getters.isAuthenticated"
          color="accent"
          class="mx-auto"
          :to="'/edit/entry/' + this.entryId"
        >
          <v-icon dark> mdi-pencil </v-icon>編集</v-btn
        >
        <v-btn
          v-if="this.entry.nextId"
          class="ml-auto"
          link
          :to="'/entry/' + this.entry.nextId"
        >
          次 ▶
        </v-btn>
      </v-row>
      <v-row class="pt-2">
        <v-divider></v-divider>
      </v-row>
      <v-row class="mt-5">
        <v-carousel v-if="entry.imageUrlList && entry.imageUrlList.length > 0">
          <v-carousel-item v-for="url in entry.imageUrlList" :key="url">
            <v-img :src="url"></v-img>
          </v-carousel-item>
        </v-carousel>
      </v-row>
      <v-row class="pt-2">
        <v-divider></v-divider>
      </v-row>
      <v-row>
        <v-col cols="3"> <b>日時</b> </v-col>
        <v-col cols="4"> {{ entry.date }}</v-col>
        <v-col cols="5"> {{ entry.startTime }} ～ {{ entry.endTime }}</v-col>
        <v-col cols="3"> <b>釣果合計</b> </v-col>
        <v-col cols="9"> {{ entry.totalFish }} </v-col>
        <v-col cols="3"> <b>釣った魚</b> </v-col>
        <v-col cols="9">
          <div v-for="(fishName, index) in entry.fishNameList" :key="index">
            {{ fishName }}
          </div>
        </v-col>
        <v-col cols="3"> <b>メモ</b> </v-col>
        <v-col cols="9"> {{ entry.memo }} </v-col>
        <v-col cols="3"> <b>都道府県</b> </v-col>
        <v-col cols="9"> {{ entry.prefecture }} </v-col>
        <v-col cols="3"> <b>場所</b> </v-col>
        <v-col cols="9"> {{ entry.place }} </v-col>
      </v-row>
      <v-row class="pt-2">
        <v-divider></v-divider>
      </v-row>
      <v-row class="pt-2">
        <iframe
          :src="this.entry.mapUrl"
          width="720"
          height="360"
          style="border: 0"
          allowfullscreen=""
          loading="lazy"
        ></iframe>
      </v-row>
      <v-row class="pt-2">
        <v-divider></v-divider>
      </v-row>
      <v-row class="pt-2">
        <v-col class="pl-8"> <b>釣果</b> </v-col>
      </v-row>
      <v-row>
        <v-container fluid>
          <v-timeline align-top dense>
            <v-timeline-item color="grey" icon="mdi-play" small fill-dot>
              <v-row class="pt-1">
                <v-col cols="2">
                  {{ entry.startTime }}
                </v-col>
                <v-col cols="4"> 釣行開始 </v-col>
              </v-row>
            </v-timeline-item>

            <v-timeline-item
              v-for="(fish, index) in entry.fishList"
              v-bind:key="index"
              icon="mdi-fish"
              fill-dot
            >
              <v-row class="pt-1">
                <v-col cols="2">
                  {{ fish.time }}
                </v-col>
                <v-col cols="3">
                  <strong>{{ fish.fishName }}</strong>
                </v-col>
                <v-col cols="1">
                  <p>{{ fish.count }}匹</p>
                </v-col>
                <v-col cols="2">
                  <p v-if="fish.max">{{ fish.min }}～{{ fish.max }}cm</p>
                  <p v-else>{{ fish.min }}cm</p>
                </v-col>
                <v-col cols="4">
                  <p>{{ fish.methodName }}</p>
                </v-col>
              </v-row>
            </v-timeline-item>

            <v-timeline-item color="grey" icon="mdi-check-bold" small fill-dot>
              <v-row class="pt-1">
                <v-col cols="2">
                  {{ entry.endTime }}
                </v-col>
                <v-col cols="4"> 釣行終了 </v-col>
              </v-row>
            </v-timeline-item>
          </v-timeline>
        </v-container>
      </v-row>
      <v-row class="pt-2">
        <v-divider></v-divider>
      </v-row>
      <v-row class="pt-2">
        <v-img :src="this.entry.tideUrl"></v-img>
      </v-row>
      <v-row class="py-2">
        <v-divider></v-divider>
      </v-row>
      <v-row>
        <v-btn
          v-if="this.entry.prevId"
          class="mr-auto"
          link
          :to="'/entry/' + this.entry.prevId"
        >
          ◀ 前
        </v-btn>
        <v-btn
          v-if="this.entry.nextId"
          class="ml-auto"
          link
          :to="'/entry/' + this.entry.nextId"
        >
          次 ▶
        </v-btn>
      </v-row>
    </v-card>
  </div>
</template>

<script>
export default {
  name: "ShowEntry",
  data: () => ({
    entry: {},
    entryId: 0,
  }),
  watch: {
    $route() {
      this.loadEntry();
    },
  },
  methods: {
    loadEntry() {
      this.entryId = this.$route.params.id;
      this.$axios
        .get("/entries/" + this.entryId)
        .then((res) => {
          this.imageUrlList = res.data.imageUrlList;
          this.entry = res.data;
        })
        .catch((err) => {
          alert(err);
        });
    },
  },
  mounted() {
    this.loadEntry();
  },
};
</script>
