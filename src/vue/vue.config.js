module.exports = {
    publicPath: '/vue/',
    devServer: {
      proxy: {
        "/": {
          target: "http://localhost:8080",
        }
      }
    },
    pages: {
      index: {
        entry: "src/main.js",
        title: "ツリビュー"
      }
    }
  }