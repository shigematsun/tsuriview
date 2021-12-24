module.exports = {
    publicPath: '/vue/',
    devServer: {
      proxy: {
        "/": {
          target: "http://localhost:8080",
        }
      }
    }
  }