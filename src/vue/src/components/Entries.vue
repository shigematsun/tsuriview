<template>
  <div>
    <div v-if="entryList.length">
      <v-row class="mt-5">
        <div v-for="entry in entryList" :key="entry.id" class="ma-3">
          <v-card width="160" :to="'/entry/' + entry.id">
            <v-img
              v-if="entry.imageUrl"
              :src="entry.imageUrl"
              height="100"
            ></v-img>
            <v-img v-else height="100" class="grey align-center">
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
        </div>
      </v-row>
      <v-pagination
        v-model="page"
        :length="pageLength"
        class="mt-2"
        @input="loadEntries"
      ></v-pagination>
    </div>
    <div v-else-if="loaded" class="text-center mt-5">
      該当するものがありません。
    </div>
  </div>
</template>

<script>
export default {
  name: "Entries",
  props: ["condition"],
  data: () => ({
    entryList: [],
    page: 1,
    pageLength: 0,
    loaded: false,
  }),
  mounted() {
    // this.loadEntries();
  },
  methods: {
    loadEntries() {
      this.loaded = false;
      let params = {};
      params.params = {};
      if (this.page) params.params.page = this.page;

      if (this.condition) {
        let obj = this.condition;
        Object.keys(obj).forEach(function (key) {
          if (obj[key]) params.params[key] = obj[key];
        });
      }

      this.$axios.get("/entries", params).then((res) => {
        this.entryList = res.data.entryList;
        this.pageLength = res.data.totalPages;
        // this.$vuetify.goTo(0);
        this.loaded = true;
      });
    },
  },
};
</script>
