<template>
	<v-app id="app" :style="{background: $vuetify.theme.themes[theme].background}">
		<Navigation></Navigation>

		<v-container grid-list-lg class="mt-10">
			<v-card loading v-if="this.$root.loading">
				<v-card-text class="loading-body">
					Loading...
				</v-card-text>
			</v-card>

			<router-view></router-view>

			<footer>
				
			</footer>
		</v-container>

	</v-app>
</template>

<script>
	import Navigation from "./components/Navigation";

	export default {
		name: 'LayoutsDemosBaselineFlipped',
		components: {
			Navigation
		},
		props: {
			source: String,
		},
		data: () => ({
			drawer: false,
		}),
		mounted() {
		},
		computed: {
			theme() {
				return this.$vuetify.theme.dark ? "dark" : "light";
			},
		},
	}
</script>

<style scoped>
	.container {
		margin-top: 36px;
		padding: 0px;
	}

	.main {
		max-width: 740px;
	}

	footer {
		margin: 12px 0;
		font-size: 14px;
		text-align: center;
		font-weight: lighter;
	}

	.v-alert {
		padding: 24px 0;
		margin: 24px 0 32px 0;
	}

	.loading-body {
		margin: 20px;
	}
</style>