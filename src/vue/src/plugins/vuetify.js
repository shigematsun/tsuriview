import Vue from 'vue';
import Vuetify from 'vuetify/lib';
import colors from 'vuetify/lib/util/colors'

Vue.use(Vuetify);

export default new Vuetify({
	theme: {
		themes: {
			light: {
				primary: colors.lightBlue.darken2,
				secondary: colors.lightBlue.darken1,
				accent: colors.lightBlue.base,
				background: colors.grey.lighten4,
			},
		},
	},
});