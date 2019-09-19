'use strict'
// Template version: 1.3.1
// see http://vuejs-templates.github.io/webpack for documentation.
const path = require('path')
let fs = require('fs');

//可通过npm 参数实现不同配置文件的加载
let args = process.argv.splice(2),
  hostPath = path.join(__dirname, '../hosts/env.js'),
  hostConfig = require(hostPath);
//暂时不需要命令行参数
if (args.length !== 0 && args[0] === 'env-config') {
  hostPath = path.join(__dirname, `../hosts/${args[0]}-config.js`);
  if(fs.existsSync(hostPath)) {
    hostConfig = require(hostPath);
  } else { 
    // console.error('Can not found', hostPath, '\b');
    // console.error('Using default config ./hosts/env.js');
  }
}
process.env.HOST_CONFIG = hostConfig;
//写入配置文件 暂时不需要写入
fs.writeFileSync( path.join(__dirname, `../src/hosts/config.js`), `export default ${JSON.stringify(hostConfig)}` );
// process.exit();
// console.log(hostConfig);

// process.HOST_CONFIG = JSON.parse(fs.readFileSync('./hosts/host-config.json', 'utf8'));
module.exports = {
  dev: {
    // Paths
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',
    proxyTable: {
      '/portal-v2/*': {           
        target: `http://${hostConfig['gateway-server']}:10019`,
        secure: false, // 接受 运行在 https 上的服务
        changeOrigin: true
      }     
      
      /* ,
      '/organization/*': {
        target: `http://${hostConfig['gateway-server']}:8040`,
        secure: false, // 接受 运行在 https 上的服务
        changeOrigin: true
      },
      '/enterprise/*': {
        target: `http://${hostConfig['gateway-server']}:8040`,
        secure: false, // 接受 运行在 https 上的服务
        changeOrigin: true
      } */
    },

    // Various Dev Server settings
    host: hostConfig['web-server'], // can be overwritten by process.env.HOST
    // host: '0.0.0.0', // can be overwritten by process.env.HOST
    port: 8082, // can be overwritten by process.env.PORT, if port is in use, a free one will be determined
    autoOpenBrowser: false,
    errorOverlay: true,
    notifyOnErrors: true,
    poll: false, // https://webpack.js.org/configuration/dev-server/#devserver-watchoptions-

    // Use Eslint Loader?
    // If true, your code will be linted during bundling and
    // linting errors and warnings will be shown in the console.
    useEslint: false,
    // If true, eslint errors and warnings will also be shown in the error overlay
    // in the browser.
    showEslintErrorsInOverlay: false,

    /**
     * Source Maps
     */

    // https://webpack.js.org/configuration/devtool/#development
    // devtool: 'cheap-module-eval-source-map',
    devtool: 'eval-source-map',

    // If you have problems debugging vue-files in devtools,
    // set this to false - it *may* help
    // https://vue-loader.vuejs.org/en/options.html#cachebusting
    cacheBusting: true,

    cssSourceMap: true
  },

  build: {
    prodEnv: require('./prod.env'),
    sitEnv: require('./sit.env'),
    ppeEnv: require('./ppe.env'),
    // Template for index.html
    index: path.resolve(__dirname, '../dist/index.html'),

    // Paths
    assetsRoot: path.resolve(__dirname, '../dist'),
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',

    /**
     * Source Maps
     */

    productionSourceMap: true,
    // https://webpack.js.org/configuration/devtool/#production
    devtool: '#source-map',

    // Gzip off by default as many popular static hosts such as
    // Surge or Netlify already gzip all static assets for you.
    // Before setting to `true`, make sure to:
    // npm install --save-dev compression-webpack-plugin
    productionGzip: false,
    productionGzipExtensions: ['js', 'css'],

    // Run the build command with an extra argument to
    // View the bundle analyzer report after build finishes:
    // `npm run build --report`
    // Set to `true` or `false` to always turn it on or off
    bundleAnalyzerReport: process.env.npm_config_report
  }
}
