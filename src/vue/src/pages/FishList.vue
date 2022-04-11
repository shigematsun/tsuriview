<template>
  <div class="mt-15 mx-auto" style="max-width: 1200px">
    <v-card class="mx-auto py-5 px-2">
      <v-row>
        <div
          v-for="fish in fishList"
          :key="fish.fishId"
          class="ma-4"
          style="width: 150px"
        >
          <router-link :to="'/fish/' + fish.fishId">
            <v-avatar size="150">
              <v-img :src="fish.imageUrl" :alt="fish.name" />
            </v-avatar>
            <div class="text-center">
              <b>{{ fish.name }}</b>
            </div>
          </router-link>
        </div>
      </v-row>
    </v-card>
  </div>
</template>

<script>
export default {
  name: "FishList",
  data: () => ({
    fishList: [],
  }),
  mounted() {
    this.$axios.get("/fishes").then((res) => {
      this.fishList = res.data.fishList;
    });
  },
  computed: {},
  methods: {},
};
</script>
