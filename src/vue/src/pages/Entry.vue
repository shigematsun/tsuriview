<template>
  <div class="mt-15">
    <v-card max-width="740" class="mx-auto pa-5">
      <v-row>
        <v-btn
          v-if="this.entry.nextId"
          class="mr-auto"
          link
          :to="'/entry/' + this.entry.nextId"
        >
          ◀ 次
        </v-btn>
        <v-btn
          v-if="this.entry.canEdit"
          color="accent"
          class="mx-auto"
          :to="'/edit/entry/' + this.entryId"
        >
          <v-icon dark> mdi-pencil </v-icon>編集</v-btn
        >
        <v-btn
          v-if="this.entry.prevId"
          class="ml-auto"
          link
          :to="'/entry/' + this.entry.prevId"
        >
          前 ▶
        </v-btn>
      </v-row>
      <v-row class="pt-2">
        <v-divider></v-divider>
      </v-row>
      <v-row class="mt-5">
        <v-carousel
          v-if="entry.imageUrlList && entry.imageUrlList.length > 0"
          height="auto"
        >
          <v-carousel-item v-for="url in entry.imageUrlList" :key="url">
            <v-img :src="url"></v-img>
          </v-carousel-item>
        </v-carousel>
      </v-row>
      <v-row class="pt-2">
        <v-divider></v-divider>
      </v-row>
      <v-row>
        <v-col cols="5"> <b>釣り人</b> </v-col>
        <v-col cols="7"> {{ entry.userName }}</v-col>
        <v-col cols="5"> <b>日付</b> </v-col>
        <v-col cols="7"> {{ entry.date }}</v-col>
        <v-col cols="5"> <b>時間</b> </v-col>
        <v-col cols="7"> {{ entry.startTime }} ～ {{ entry.endTime }}</v-col>
        <v-col cols="5"> <b>釣果合計</b> </v-col>
        <v-col cols="7"> {{ entry.totalFish }} </v-col>
        <v-col cols="5"> <b>釣った魚</b> </v-col>
        <v-col cols="7">
          <div v-for="(fishName, index) in entry.fishNameList" :key="index">
            {{ fishName }}
          </div>
        </v-col>
        <v-col cols="5"> <b>メモ</b> </v-col>
        <v-col cols="7"> {{ entry.memo }} </v-col>
        <v-col cols="5"> <b>都道府県</b> </v-col>
        <v-col cols="7"> {{ entry.prefecture }} </v-col>
        <v-col cols="5"> <b>場所</b> </v-col>
        <v-col cols="7"> {{ entry.place }} </v-col>
      </v-row>
      <v-row class="pt-2">
        <v-divider></v-divider>
      </v-row>
      <v-row class="pt-2">
        <iframe
          :src="this.entry.mapUrl"
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
      <v-row class="pt-2">
        <v-col class="pl-8"> <b>釣果</b> </v-col>
      </v-row>
      <v-row>
        <v-container fluid>
          <v-timeline align-top dense>
            <v-timeline-item color="grey" icon="mdi-play" small fill-dot>
              <v-row class="pt-1">
                <p class="mt-2">{{ entry.startTime }}</p>
                <p class="mt-2 ml-5">釣行開始</p>
              </v-row>
            </v-timeline-item>

            <v-timeline-item
              v-for="(fish, index) in entry.fishList"
              v-bind:key="index"
              icon="mdi-fish"
              fill-dot
            >
              <v-row class="pt-2">
                <p class="my-0">{{ fish.time }}</p>
                <strong class="my-0 ml-5">{{ fish.fishName }}</strong>
                <p class="my-0 ml-5">{{ fish.count }}匹</p>
              </v-row>
              <v-row class="pt-1">
                <p v-if="fish.max" class="my-0 ml-5 text-caption">
                  {{ fish.min }}～{{ fish.max }}cm
                </p>
                <p v-else class="my-0 ml-5 text-caption">{{ fish.min }}cm</p>
                <p class="my-0 ml-5 text-caption">{{ fish.methodName }}</p>
              </v-row>
            </v-timeline-item>

            <v-timeline-item color="grey" icon="mdi-check-bold" small fill-dot>
              <v-row class="pt-1">
                <p class="mt-2">{{ entry.endTime }}</p>
                <p class="mt-2 ml-5">釣行終了</p>
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
          v-if="this.entry.nextId"
          class="mr-auto"
          link
          :to="'/entry/' + this.entry.nextId"
        >
          ◀ 次
        </v-btn>
        <v-btn
          v-if="this.entry.canEdit"
          color="accent"
          class="mx-auto"
          :to="'/edit/entry/' + this.entryId"
        >
          <v-icon dark> mdi-pencil </v-icon>編集</v-btn
        >
        <v-btn
          v-if="this.entry.prevId"
          class="ml-auto"
          link
          :to="'/entry/' + this.entry.prevId"
        >
          前 ▶
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
      let params = {};
      params.params = {};
      params.params.userId = this.$store.getters.selectedUser;

      this.$axios
        .get("/entries/" + this.entryId, params)
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
