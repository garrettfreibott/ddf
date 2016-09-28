var webpack = require('webpack')
var validate = require('webpack-validator')
var merge = require('webpack-merge')
var path = require('path')

var config = {
  output: {
    publicPath: '/',
    filename: 'bundle.js',
    path: path.resolve(__dirname, 'public')
  },
  devtool: 'source-map',
  entry: './',
  module: {
    loaders: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        loader: 'babel'
      },
      {
        test: /\.less$/,
        loader: 'style!css?modules&importLoaders=1&localIdentName=[name]__[local]___[hash:base64:5]!less'
      },
      {
        test: /\.woff(2)?(\?v=[0-9]\.[0-9]\.[0-9])?$/,
        loader: 'url?limit=10000&minetype=application/font-woff'
      },
      {
        test: /\.(ttf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?$/,
        loader: 'file'
      }
    ]
  }
}

if (process.env.NODE_ENV !== 'production') {
  config = merge.smart(config, {
    entry: [
      'react-hot-loader/patch',
      'webpack-dev-server/client?http://localhost:8080/',
      'webpack/hot/only-dev-server',
      './'
    ],
    devServer: {
      noInfo: true,
      contentBase: 'public',
      compress: true,
      hot: true,
      historyApiFallback: true,
      host: '0.0.0.0',
      proxy: {
        '/admin': {
          target: 'https://10.101.2.161:8993',
          secure: false
        }
      }
    },
    plugins: [
      new webpack.HotModuleReplacementPlugin()
    ]
  })
} else {
  config = merge.smart(config, {
    plugins: [
      new webpack.DefinePlugin({
        'process.env.NODE_ENV': '"production"'
      }),
      new webpack.optimize.UglifyJsPlugin({
        output: {
          comments: false
        },
        compress: {
          warnings: false
        }
      })
    ]
  })
}

module.exports = validate(config)
