(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["pages-sign-sign"],{"1c71":function(t,e,n){"use strict";var i;n.d(e,"b",(function(){return r})),n.d(e,"c",(function(){return a})),n.d(e,"a",(function(){return i}));var r=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("v-uni-view",{staticClass:"sunui-uploader-bd"},[n("v-uni-view",{staticClass:"sunui-uploader-files"},[t._l(t.upload_before_list,(function(e,i){return[n("v-uni-view",{key:i+"_0",staticClass:"sunui-uploader-file",class:[e.upload_percent<100?"sunui-uploader-file-status":""],on:{click:function(e){arguments[0]=e=t.$handleEvent(e),t.previewImage(i)}}},[n("v-uni-image",{staticClass:"sunui-uploader-img",style:t.upload_img_wh,attrs:{src:e.path,mode:"aspectFill"}}),n("v-uni-view",{directives:[{name:"show",rawName:"v-show",value:t.upimg_move,expression:"upimg_move"}],staticClass:"sunui-img-removeicon right",on:{click:function(e){e.stopPropagation(),arguments[0]=e=t.$handleEvent(e),t.removeImage(i)}}},[t._v("×")])],1)]})),n("v-uni-view",{directives:[{name:"show",rawName:"v-show",value:t.upload_len<t.upload_count,expression:"upload_len < upload_count"}],staticClass:"sunui-uploader-inputbox",style:t.upload_img_wh,attrs:{"hover-class":"sunui-uploader-hover"},on:{click:function(e){arguments[0]=e=t.$handleEvent(e),t.chooseImage.apply(void 0,arguments)}}},[n("v-uni-view",[n("v-uni-text",{staticClass:"iconfont icon-mn_shangchuantupian",staticStyle:{color:"#666"}})],1)],1)],2)],1)},a=[]},"1da1":function(t,e,n){"use strict";function i(t,e,n,i,r,a,o){try{var s=t[a](o),u=s.value}catch(c){return void n(c)}s.done?e(u):Promise.resolve(u).then(i,r)}function r(t){return function(){var e=this,n=arguments;return new Promise((function(r,a){var o=t.apply(e,n);function s(t){i(o,r,a,s,u,"next",t)}function u(t){i(o,r,a,s,u,"throw",t)}s(void 0)}))}}n("d3b7"),Object.defineProperty(e,"__esModule",{value:!0}),e.default=r},"25b2":function(t,e,n){"use strict";(function(t){var i=n("4ea4");n("99af"),n("d81d"),n("a434"),n("a9e3"),n("d3b7"),n("3ca3"),n("ddb0"),Object.defineProperty(e,"__esModule",{value:!0}),e.default=void 0;var r=i(n("2909"));n("96cf");var a=i(n("1da1")),o=n("d512"),s={data:function(){return{upload_len:0,upload_cache:[],upload_cache_list:[],upload_before_list:[]}},name:"sunui-upimg",props:{url:{type:String,default:o.webApiURL+"/webApi/attendance/uploadImg"},upload_img_wh:{type:String,default:"width:162rpx;height:162rpx;"},upload_count:{type:[Number,String],default:9},upload_auto:{type:Boolean,default:!0},upimg_move:{type:Boolean,default:!0},upimg_preview:{type:Array,default:function(){return[]}},upimg_delaytime:{type:[Number,String],default:300},header:{type:Object,default:function(){return{}}}},created:function(){var t=this;return(0,a.default)(regeneratorRuntime.mark((function e(){return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:t,setTimeout((function(){t.upload_before_list=t.upload_before_list.concat(t.upimg_preview),t.upload_len=t.upload_before_list.length,t.upimg_preview.map((function(e){t.upload_cache_list.push(e.path)})),t.emit()}),t.upimg_delaytime);case 2:case"end":return e.stop()}}),e)})))()},methods:{upImage:function(t,e){var n=this,i=t.map((function(t){return u(c)({url:n.url,path:t,name:"file",extra:e,_self:n})}));uni.showLoading({title:"正在上传..."}),Promise.all(i).then((function(t){var e;uni.hideLoading(),(e=n.upload_cache_list).push.apply(e,(0,r.default)(t)),n.emit()})).catch((function(t){uni.hideLoading()}))},chooseImage:function(){var e=this;uni.chooseImage({count:e.upload_count-e.upload_before_list.length,sizeType:["compressed","original"],sourceType:["album","camera"],success:function(t){for(var n=0,i=t.tempFiles.length;n<i;n++)t.tempFiles[n]["upload_percent"]=0,e.upload_before_list.push(t.tempFiles[n]);e.upload_cache=t.tempFilePaths,e.upload(e.upload_auto)},fail:function(e){t("log",e," at components/sunui-upimg/sunui-upimg.vue:133")}})},upload:function(e){var n=this;return(0,a.default)(regeneratorRuntime.mark((function i(){var r;return regeneratorRuntime.wrap((function(i){while(1)switch(i.prev=i.next){case 0:if(r=n,!e){i.next=6;break}return i.next=4,r.upImage(r.upload_cache,r.header);case 4:i.next=7;break;case 6:t("warn","传输参数:this.$refs.xx.upload(true)才可上传,默认false"," at components/sunui-upimg/sunui-upimg.vue:139");case 7:case"end":return i.stop()}}),i)})))()},previewImage:function(t){for(var e=this,n=[],i=0,r=e.upload_before_list.length;i<r;i++)n.push(e.upload_before_list[i].path);uni.previewImage({current:t,urls:n})},removeImage:function(t){var e=this;e.upload_before_list.splice(t,1),e.upload_cache_list.splice(t,1),e.upload_len=e.upload_before_list.length,e.emit()},emit:function(){var t=this;t.$emit("change",t.upload_cache_list)}}};e.default=s;var u=function(t){return function(e){for(var n=arguments.length,i=new Array(n>1?n-1:0),r=1;r<n;r++)i[r-1]=arguments[r];return new Promise((function(n,r){t.apply(void 0,[Object.assign({},e,{success:n,fail:r})].concat(i))}))}},c=function(e){var n=e.url,i=e._self,r=e.path,o=e.name,s=e.extra,u=e.success,c=(e.progress,e.fail),l=uni.uploadFile({url:n,filePath:r,name:o,formData:s,success:function(e){var n=e.data;t("warn","sunui-upimg - 如发现没有获取到返回值请到源码191行修改后端返回图片路径以便正常使用插件",n," at components/sunui-upimg/sunui-upimg.vue:200");try{n=e.data}catch(i){throw n}200==e.statusCode?u&&u(n):c&&c(n)},fail:function(e){t("log",e," at components/sunui-upimg/sunui-upimg.vue:220"),c&&c(e)}});l.onProgressUpdate(function(){var t=(0,a.default)(regeneratorRuntime.mark((function t(e){var n,r;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:n=0,r=i.upload_before_list.length;case 1:if(!(n<r)){t.next=8;break}return t.next=4,e.progress;case 4:i.upload_before_list[n]["upload_percent"]=t.sent;case 5:n++,t.next=1;break;case 8:i.upload_before_list=i.upload_before_list,i.upload_len=i.upload_before_list.length;case 10:case"end":return t.stop()}}),t)})));return function(e){return t.apply(this,arguments)}}())}}).call(this,n("0de9")["log"])},"3aaa":function(t,e,n){var i=n("24fb");e=i(!1),e.push([t.i,'@charset "UTF-8";\r\n/**\r\n * 这里是uni-app内置的常用样式变量\r\n *\r\n * uni-app 官方扩展插件及插件市场（https://ext.dcloud.net.cn）上很多三方插件均使用了这些样式变量\r\n * 如果你是插件开发者，建议你使用scss预处理，并在插件代码中直接使用这些变量（无需 import 这个文件），方便用户通过搭积木的方式开发整体风格一致的App\r\n *\r\n */\r\n/**\r\n * 如果你是App开发者（插件使用者），你可以通过修改这些变量来定制自己的插件主题，实现自定义主题功能\r\n *\r\n * 如果你的项目同样使用了scss预处理，你也可以直接在你的 scss 代码中使用如下变量，同时无需 import 这个文件\r\n */\r\n/* 颜色变量 */\r\n/* 行为相关颜色 */\r\n/* 文字基本颜色 */\r\n/* 背景颜色 */\r\n/* 边框颜色 */\r\n/* 尺寸变量 */\r\n/* 文字尺寸 */\r\n/* 图片尺寸 */\r\n/* Border Radius */\r\n/* 水平间距 */\r\n/* 垂直间距 */\r\n/* 透明度 */\r\n/* 文章场景相关 */@font-face{font-family:iconfont;src:url(//at.alicdn.com/iconfont.eot?t=1574391686418);\r\n  /* IE9 */src:url(//at.alicdn.com/iconfont.eot?t=1574391686418#iefix) format("embedded-opentype"),url("data:application/x-font-woff2;charset=utf-8;base64,d09GMgABAAAAAAMkAAsAAAAAB2QAAALYAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHEIGVgCCcAqCYIJEATYCJAMICwYABCAFhG0HPRt3BhEVlCNkH4dxmzUXNsJHc1SNfR9KTkCtiXv/l+QDBQSFRBJdKoEsg60HUgCsOpWVnWxNx3BvVITqkj3fepbtzM/OfDo4D86iFEIiJAeX02+Bh/O84TLmsrEnYBxQoHtgm6xACoxTkN0zFsgEdQynCShpq7cwbsK0eTKROSkgbNu8cbUspRFrkoNMkC9ZGYWjcrJkX/IIR/zPhz/6hIxELmWmzdowfp1RvxdbYWm1VrUMCO54JvDrSNEbkTCv1DJDGvp6S5VUX9SRdSUHfi+u1cBZ7R+PQMgzEyugNcU5J67DO9VfJiCigD042iuNQqXSunGRfvrWV6/mvX49/+3bhW/eLHr4puOFtxMfvO5w9tX8yv7rIbf3Rrl84Mbe66XSzWet46nn/etMuALua5LqNZUqpKdfDKjsv2qef+yambJsTWM2zDtKIQ0pS7msvSTUpn1tNyts2xZmWUyw3LI4bPisSZNyOUc2y4/scfZs3QZ1UcgqUWtkVednsvnVs7NOHzmqglXIBnqU7+/M9Hp3y3L2RLWYA9uhlat61/LGGwVqt9Nvafv/8R2fmg/pu7LesH9ZOYL3/6e3P6Z2O0rbIztra+Dtc1u2RY1vapOocEtDiT0Kd1VUUkIN42joS19Fk1s1BVmKy0OioA2kMp1REdcbcsr6QV5mJJT0MnF9mbQRchZiET29CAT1fSBR1y1I1fdFRdwPcpr6Q179cIaSBaHRjmVdgxFjCSvGFuonmGYcpK1nESRfUC1dRUm+T3ggeeOEOIiywRwHpDHm+FUlzBIkjT1k5DzsuhEmGi02HGjmKQ1DWfaioBn7gzAWQRWGWqD2BIzGaCDRm4nc+y+QsuhUqKaqyviAiGcGB7FA1AKVS4ZWVddyibdSEoxJQCKjHsjIMNTpjMBUPsxCDRbQPTyTVGh1k20lwfyy/un2QYmpTII1I9Vo+1B4XQ2q0QvwvExGfTgA") format("woff2"),url(//at.alicdn.com/iconfont.woff?t=1574391686418) format("woff"),url(//at.alicdn.com/iconfont.ttf?t=1574391686418) format("truetype"),url(//at.alicdn.com/iconfont.svg?t=1574391686418#iconfont) format("svg")\r\n  /* iOS 4.1- */}.iconfont[data-v-1eebb7d4]{font-family:iconfont!important;font-size:16px;font-style:normal;-webkit-font-smoothing:antialiased;-moz-osx-font-smoothing:grayscale}.icon-mn_shangchuantupian[data-v-1eebb7d4]{font-size:3em}.icon-mn_shangchuantupian[data-v-1eebb7d4]:before{content:"\\e559"}.sunui-uploader-img[data-v-1eebb7d4]{display:block}.sunui-uploader-input[data-v-1eebb7d4]{position:absolute;z-index:1;top:0;left:0;width:100%;height:100%;opacity:0}.sunui-uploader-inputbox[data-v-1eebb7d4]{position:relative;margin-bottom:%?16?%;box-sizing:border-box;background-color:#ededed;display:-webkit-box;display:-webkit-flex;display:flex;-webkit-flex-wrap:wrap;flex-wrap:wrap;-webkit-box-align:center;-webkit-align-items:center;align-items:center;-webkit-box-pack:center;-webkit-justify-content:center;justify-content:center}.sunui-img-removeicon[data-v-1eebb7d4]{position:absolute;color:#fff;width:%?40?%;height:%?40?%;line-height:%?40?%;z-index:2;text-align:center;background-color:#e54d42}.sunui-img-removeicon.right[data-v-1eebb7d4]{top:0;right:0}.sunui-uploader-file[data-v-1eebb7d4]{position:relative;margin-right:%?16?%;margin-bottom:%?16?%}.sunui-uploader-file-status[data-v-1eebb7d4]:before{content:" ";position:absolute;top:0;right:0;bottom:0;left:0;background-color:rgba(0,0,0,.5)}.sunui-loader-filecontent[data-v-1eebb7d4]{position:absolute;top:50%;left:50%;-webkit-transform:translate(-50%,-50%);transform:translate(-50%,-50%);color:#fff;z-index:9}.sunui-uploader-bd[data-v-1eebb7d4]{padding:%?26?%;margin:0}.sunui-uploader-files[data-v-1eebb7d4]{display:-webkit-box;display:-webkit-flex;display:flex;-webkit-flex-wrap:wrap;flex-wrap:wrap}.sunui-uploader-file[data-v-1eebb7d4]:nth-child(4n + 0){margin-right:0}.sunui-uploader-inputbox > uni-view[data-v-1eebb7d4]{text-align:center}.sunui-uploader-file-status[data-v-1eebb7d4]:after{content:" ";position:absolute;top:0;right:0;bottom:0;left:0;background-color:rgba(0,0,0,.5)}.sunui-uploader-hover[data-v-1eebb7d4]{box-shadow:0 0 0 #e5e5e5;background:#e5e5e5}',""]),t.exports=e},"3af7":function(t,e,n){var i=n("3aaa");"string"===typeof i&&(i=[[t.i,i,""]]),i.locals&&(t.exports=i.locals);var r=n("4f06").default;r("b1137e1c",i,!0,{sourceMap:!1,shadowMode:!1})},"3d91":function(t,e,n){"use strict";var i=n("71e3"),r=n.n(i);r.a},"3e74":function(t,e,n){var i=n("24fb");e=i(!1),e.push([t.i,"uni-page-body[data-v-9bff6662]{height:100%;overflow:hidden;width:100%}.sign2[data-v-9bff6662]{width:100%;height:100%}\n.map[data-v-9bff6662]{width:100%;height:170px}.colorGreen[data-v-9bff6662]{color:#32cd32}.colorBlue[data-v-9bff6662]{color:#007aff}.colorRed[data-v-9bff6662]{color:red}.bgBlue[data-v-9bff6662]{background-color:#007aff}.bgGreen[data-v-9bff6662]{background-color:#32cd32}.bgAsh[data-v-9bff6662]{background-color:#c8c7cc}.inputV[data-v-9bff6662]{padding:%?20?%}.inputV .t[data-v-9bff6662]{font-size:%?36?%}.inputV .text[data-v-9bff6662]{border-bottom:1px solid #000;width:100%;color:#666}",""]),t.exports=e},"571c":function(t,e,n){"use strict";n.r(e);var i=n("f3cc"),r=n.n(i);for(var a in i)"default"!==a&&function(t){n.d(e,t,(function(){return i[t]}))}(a);e["default"]=r.a},"589c":function(t,e,n){(function(e){function i(t){if("number"!==typeof t||t<0)return t;var e=parseInt(t/3600);t%=3600;var n=parseInt(t/60);t%=60;var i=t;return[e,n,i].map((function(t){return t=t.toString(),t[1]?t:"0"+t})).join(":")}function r(t,e){return"string"===typeof t&&"string"===typeof e&&(t=parseFloat(t),e=parseFloat(e)),t=t.toFixed(2),e=e.toFixed(2),{longitude:t.toString().split("."),latitude:e.toString().split(".")}}n("a15b"),n("d81d"),n("fb6a"),n("4e82"),n("a9e3"),n("b680"),n("b64b"),n("d3b7"),n("acd8"),n("e25e"),n("ac1f"),n("25f0"),n("1276");var a={UNITS:{"年":315576e5,"月":26298e5,"天":864e5,"小时":36e5,"分钟":6e4,"秒":1e3},humanize:function(t){var e="";for(var n in this.UNITS)if(t>=this.UNITS[n]){e=Math.floor(t/this.UNITS[n])+n+"前";break}return e||"刚刚"},format:function(t){var e=this.parse(t),n=Date.now()-e.getTime();if(n<this.UNITS["天"])return this.humanize(n);var i=function(t){return t<10?"0"+t:t};return e.getFullYear()+"/"+i(e.getMonth()+1)+"/"+i(e.getDay())+"-"+i(e.getHours())+":"+i(e.getMinutes())},parse:function(t){var e=t.split(/[^0-9]/);return new Date(e[0],e[1]-1,e[2],e[3],e[4],e[5])}};function o(t,e){var n=t.getFullYear(),i=("0"+(t.getMonth()+1)).slice(-2),r=("0"+t.getDate()).slice(-2),a=("0"+t.getHours()).slice(-2),o=("0"+t.getMinutes()).slice(-2),s=("0"+t.getSeconds()).slice(-2);if("Y-M-D h:min:s"===e)var u=n+"-"+i+"-"+r+" "+a+":"+o+":"+s;else if("Y-M-D"===e)u=n+"-"+i+"-"+r;if("h:min:s"===e)u=a+":"+o+":"+s;return u}function s(t){for(var e=[],n={},i=0;i<t.length;i++){var r=Object.keys(t[i]);r.sort((function(t,e){return Number(t)-Number(e)}));for(var a="",o=0;o<r.length;o++)a+=JSON.stringify(r[o]),a+=JSON.stringify(t[i][r[o]]);n.hasOwnProperty(a)||(e.push(t[i]),n[a]=!0)}return e=e,e}function o(t,e){var n=t.getFullYear(),i=("0"+(t.getMonth()+1)).slice(-2),r=("0"+t.getDate()).slice(-2),a=("0"+t.getHours()).slice(-2),o=("0"+t.getMinutes()).slice(-2),s=("0"+t.getSeconds()).slice(-2);if("Y-M-D h:min:s"===e)var u=n+"-"+i+"-"+r+" "+a+":"+o+":"+s;else if("Y-M-D"===e)u=n+"-"+i+"-"+r;if("h:min:s"===e)u=a+":"+o+":"+s;else if("h"===e)u=a;else if("min"===e)u=o;return u}function u(t,e){switch(arguments.length){case 1:return parseInt(Math.random()*t+1,10);case 2:return parseInt(Math.random()*(e-t+1)+t,10);default:return 0}}function c(t,e,n){if(0===n)return!1;var i=e[0]-t[0],r=e[1]-t[1];return i*i+r*r<=n*n}function l(t,n,i){if(0===i)return!1;var r=t[0]*Math.PI/180,a=n[0]*Math.PI/180,o=r-a,s=t[1]*Math.PI/180-n[1]*Math.PI/180,u=2*Math.asin(Math.sqrt(Math.pow(Math.sin(o/2),2)+Math.cos(r)*Math.cos(a)*Math.pow(Math.sin(s/2),2)));u*=6378.137,u=Math.round(1e4*u)/1e4;var c=1e3*u;return e("log","当前坐标位置与打卡圆心距离："+c," at common/util.js:163"),c<=i}function d(t){var e=new Date(t),n=new Date;return e.setHours(0,0,0,0)==n.setHours(0,0,0,0)}t.exports={formatTime:i,formatLocation:r,dateUtils:a,formateDate:o,deteleObject:s,randomNum:u,pointInsideCircle:c,isSameDay:d,countRadius:l}}).call(this,n("0de9")["log"])},"63c7":function(t,e,n){"use strict";n.r(e);var i=n("f51f"),r=n("571c");for(var a in r)"default"!==a&&function(t){n.d(e,t,(function(){return r[t]}))}(a);n("3d91");var o,s=n("f0c5"),u=Object(s["a"])(r["default"],i["b"],i["c"],!1,null,"9bff6662",null,!1,i["a"],o);e["default"]=u.exports},"68b5":function(t,e,n){"use strict";n.r(e);var i=n("1c71"),r=n("bb70");for(var a in r)"default"!==a&&function(t){n.d(e,t,(function(){return r[t]}))}(a);n("a787");var o,s=n("f0c5"),u=Object(s["a"])(r["default"],i["b"],i["c"],!1,null,"1eebb7d4",null,!1,i["a"],o);e["default"]=u.exports},"71e3":function(t,e,n){var i=n("3e74");"string"===typeof i&&(i=[[t.i,i,""]]),i.locals&&(t.exports=i.locals);var r=n("4f06").default;r("3abd3825",i,!0,{sourceMap:!1,shadowMode:!1})},"96cf":function(t,e){!function(e){"use strict";var n,i=Object.prototype,r=i.hasOwnProperty,a="function"===typeof Symbol?Symbol:{},o=a.iterator||"@@iterator",s=a.asyncIterator||"@@asyncIterator",u=a.toStringTag||"@@toStringTag",c="object"===typeof t,l=e.regeneratorRuntime;if(l)c&&(t.exports=l);else{l=e.regeneratorRuntime=c?t.exports:{},l.wrap=y;var d="suspendedStart",f="suspendedYield",p="executing",h="completed",g={},v={};v[o]=function(){return this};var m=Object.getPrototypeOf,b=m&&m(m(M([])));b&&b!==i&&r.call(b,o)&&(v=b);var w=k.prototype=x.prototype=Object.create(v);I.prototype=w.constructor=k,k.constructor=I,k[u]=I.displayName="GeneratorFunction",l.isGeneratorFunction=function(t){var e="function"===typeof t&&t.constructor;return!!e&&(e===I||"GeneratorFunction"===(e.displayName||e.name))},l.mark=function(t){return Object.setPrototypeOf?Object.setPrototypeOf(t,k):(t.__proto__=k,u in t||(t[u]="GeneratorFunction")),t.prototype=Object.create(w),t},l.awrap=function(t){return{__await:t}},A(S.prototype),S.prototype[s]=function(){return this},l.AsyncIterator=S,l.async=function(t,e,n,i){var r=new S(y(t,e,n,i));return l.isGeneratorFunction(e)?r:r.next().then((function(t){return t.done?t.value:r.next()}))},A(w),w[u]="Generator",w[o]=function(){return this},w.toString=function(){return"[object Generator]"},l.keys=function(t){var e=[];for(var n in t)e.push(n);return e.reverse(),function n(){while(e.length){var i=e.pop();if(i in t)return n.value=i,n.done=!1,n}return n.done=!0,n}},l.values=M,E.prototype={constructor:E,reset:function(t){if(this.prev=0,this.next=0,this.sent=this._sent=n,this.done=!1,this.delegate=null,this.method="next",this.arg=n,this.tryEntries.forEach(T),!t)for(var e in this)"t"===e.charAt(0)&&r.call(this,e)&&!isNaN(+e.slice(1))&&(this[e]=n)},stop:function(){this.done=!0;var t=this.tryEntries[0],e=t.completion;if("throw"===e.type)throw e.arg;return this.rval},dispatchException:function(t){if(this.done)throw t;var e=this;function i(i,r){return s.type="throw",s.arg=t,e.next=i,r&&(e.method="next",e.arg=n),!!r}for(var a=this.tryEntries.length-1;a>=0;--a){var o=this.tryEntries[a],s=o.completion;if("root"===o.tryLoc)return i("end");if(o.tryLoc<=this.prev){var u=r.call(o,"catchLoc"),c=r.call(o,"finallyLoc");if(u&&c){if(this.prev<o.catchLoc)return i(o.catchLoc,!0);if(this.prev<o.finallyLoc)return i(o.finallyLoc)}else if(u){if(this.prev<o.catchLoc)return i(o.catchLoc,!0)}else{if(!c)throw new Error("try statement without catch or finally");if(this.prev<o.finallyLoc)return i(o.finallyLoc)}}}},abrupt:function(t,e){for(var n=this.tryEntries.length-1;n>=0;--n){var i=this.tryEntries[n];if(i.tryLoc<=this.prev&&r.call(i,"finallyLoc")&&this.prev<i.finallyLoc){var a=i;break}}a&&("break"===t||"continue"===t)&&a.tryLoc<=e&&e<=a.finallyLoc&&(a=null);var o=a?a.completion:{};return o.type=t,o.arg=e,a?(this.method="next",this.next=a.finallyLoc,g):this.complete(o)},complete:function(t,e){if("throw"===t.type)throw t.arg;return"break"===t.type||"continue"===t.type?this.next=t.arg:"return"===t.type?(this.rval=this.arg=t.arg,this.method="return",this.next="end"):"normal"===t.type&&e&&(this.next=e),g},finish:function(t){for(var e=this.tryEntries.length-1;e>=0;--e){var n=this.tryEntries[e];if(n.finallyLoc===t)return this.complete(n.completion,n.afterLoc),T(n),g}},catch:function(t){for(var e=this.tryEntries.length-1;e>=0;--e){var n=this.tryEntries[e];if(n.tryLoc===t){var i=n.completion;if("throw"===i.type){var r=i.arg;T(n)}return r}}throw new Error("illegal catch attempt")},delegateYield:function(t,e,i){return this.delegate={iterator:M(t),resultName:e,nextLoc:i},"next"===this.method&&(this.arg=n),g}}}function y(t,e,n,i){var r=e&&e.prototype instanceof x?e:x,a=Object.create(r.prototype),o=new E(i||[]);return a._invoke=L(t,n,o),a}function _(t,e,n){try{return{type:"normal",arg:t.call(e,n)}}catch(i){return{type:"throw",arg:i}}}function x(){}function I(){}function k(){}function A(t){["next","throw","return"].forEach((function(e){t[e]=function(t){return this._invoke(e,t)}}))}function S(t){function e(n,i,a,o){var s=_(t[n],t,i);if("throw"!==s.type){var u=s.arg,c=u.value;return c&&"object"===typeof c&&r.call(c,"__await")?Promise.resolve(c.__await).then((function(t){e("next",t,a,o)}),(function(t){e("throw",t,a,o)})):Promise.resolve(c).then((function(t){u.value=t,a(u)}),(function(t){return e("throw",t,a,o)}))}o(s.arg)}var n;function i(t,i){function r(){return new Promise((function(n,r){e(t,i,n,r)}))}return n=n?n.then(r,r):r()}this._invoke=i}function L(t,e,n){var i=d;return function(r,a){if(i===p)throw new Error("Generator is already running");if(i===h){if("throw"===r)throw a;return O()}n.method=r,n.arg=a;while(1){var o=n.delegate;if(o){var s=C(o,n);if(s){if(s===g)continue;return s}}if("next"===n.method)n.sent=n._sent=n.arg;else if("throw"===n.method){if(i===d)throw i=h,n.arg;n.dispatchException(n.arg)}else"return"===n.method&&n.abrupt("return",n.arg);i=p;var u=_(t,e,n);if("normal"===u.type){if(i=n.done?h:f,u.arg===g)continue;return{value:u.arg,done:n.done}}"throw"===u.type&&(i=h,n.method="throw",n.arg=u.arg)}}}function C(t,e){var i=t.iterator[e.method];if(i===n){if(e.delegate=null,"throw"===e.method){if(t.iterator.return&&(e.method="return",e.arg=n,C(t,e),"throw"===e.method))return g;e.method="throw",e.arg=new TypeError("The iterator does not provide a 'throw' method")}return g}var r=_(i,t.iterator,e.arg);if("throw"===r.type)return e.method="throw",e.arg=r.arg,e.delegate=null,g;var a=r.arg;return a?a.done?(e[t.resultName]=a.value,e.next=t.nextLoc,"return"!==e.method&&(e.method="next",e.arg=n),e.delegate=null,g):a:(e.method="throw",e.arg=new TypeError("iterator result is not an object"),e.delegate=null,g)}function N(t){var e={tryLoc:t[0]};1 in t&&(e.catchLoc=t[1]),2 in t&&(e.finallyLoc=t[2],e.afterLoc=t[3]),this.tryEntries.push(e)}function T(t){var e=t.completion||{};e.type="normal",delete e.arg,t.completion=e}function E(t){this.tryEntries=[{tryLoc:"root"}],t.forEach(N,this),this.reset(!0)}function M(t){if(t){var e=t[o];if(e)return e.call(t);if("function"===typeof t.next)return t;if(!isNaN(t.length)){var i=-1,a=function e(){while(++i<t.length)if(r.call(t,i))return e.value=t[i],e.done=!1,e;return e.value=n,e.done=!0,e};return a.next=a}}return{next:O}}function O(){return{value:n,done:!0}}}(function(){return this||"object"===typeof self&&self}()||Function("return this")())},a787:function(t,e,n){"use strict";var i=n("3af7"),r=n.n(i);r.a},bb70:function(t,e,n){"use strict";n.r(e);var i=n("25b2"),r=n.n(i);for(var a in i)"default"!==a&&function(t){n.d(e,t,(function(){return i[t]}))}(a);e["default"]=r.a},d512:function(t,e,n){"use strict";(function(t){function n(){t("log",1," at pages/index/index.js:2")}function i(t){var e={main:[t]};uni.setStorageSync("signInfo",JSON.stringify(e))}function r(t,e){e.main.push(t),uni.setStorageSync("signInfo",JSON.stringify(e))}function a(){var t=uni.getStorageSync("signInfo");if(t)return JSON.parse(t)}function o(){uni.removeStorage({key:"signInfo",success:function(){uni.showToast({title:"重置成功"})}})}function s(t){uni.removeStorageSync(t)}function u(t){new Date;var e={mode:t.mode,latitude:t.latitude,longitude:t.longitude,address:t.address,time:t.time,remarks:t.remarks,imgURL:t.imgURL,classId:t.classId,groupId:t.groupId,userId:t.userId,userName:t.userName};return e}Object.defineProperty(e,"__esModule",{value:!0}),e.handleSignClick=n,e.setSignInfo=i,e.addSignInfo=r,e.getSignInfo=a,e.delSignInfo=o,e.removeSignInfo=s,e.getInfo=u,e.webApiURL=e.key=void 0;var c="75VBZ-OHRW5-5WEIX-QKCQN-YJXEZ-H5BWW";e.key=c;var l="https://hr-web.xiantian365.com";e.webApiURL=l}).call(this,n("0de9")["log"])},f3cc:function(t,e,n){"use strict";(function(t){var i=n("4ea4");n("99af"),Object.defineProperty(e,"__esModule",{value:!0}),e.default=void 0;var r=n("d512"),a=n("589c"),o=i(n("68b5")),s={components:{sunUiUpimg:o.default},data:function(){return{userId:null,sessionId:null,userName:"",isTrue:null,signText:"",signstate:"",r:0,scale:"18",clickNum:0,latitude:"",longitude:"",accuracy:"",address:"我的位置",time:(0,a.formateDate)(new Date,"h:min:s"),signInfo:{mode:"",latitude:"",longitude:"",address:"",time:"",remarks:"",upload:""},covers:[{id:0,callout:{content:null,display:"ALWAYS"},latitude:null,longitude:null,iconPath:"../../static/img/location.png"}],circles:[{latitude:null,longitude:null,radius:null,strokeWidth:1,fillColor:"#7fff0099"}],controls:[{id:0,position:{left:300,top:300,width:32,height:32},iconPath:"../../static/img/resetlo.png",clickable:!0}],serviceArr2:[],serviceArr3:[{path:"https://dss0.bdstatic.com/6Ox1bjeh1BF3odCf/it/u=1537304011,3995405403&fm=74&app=80&f=JPEG&size=f121,140?sec=1880279984&t=97b7ba208086526f1a92f5294f1a68a3"}],serviceArr4:[{path:"https://dss0.bdstatic.com/6Ox1bjeh1BF3odCf/it/u=1537304011,3995405403&fm=74&app=80&f=JPEG&size=f121,140?sec=1880279984&t=97b7ba208086526f1a92f5294f1a68a3"},{path:"https://dss0.baidu.com/73t1bjeh1BF3odCf/it/u=63785387,1979900985&fm=85&s=8015CD304A92909C8F80B180030030EB"}],imgURL:[],remarks:""}},onLoad:function(e){var n=uni.getStorageSync("sessionId"),i=uni.getStorageSync("userId"),r=uni.getStorageSync("userName");this.sessionId=JSON.parse(n),this.userId=JSON.parse(i),this.userName=JSON.parse(r),t("log","主页携带的数据: "+JSON.stringify(e)," at pages/sign/sign.vue:122"),t("log","sessionId: "+this.sessionId," at pages/sign/sign.vue:123"),this.covers[0].callout.content=e.address,this.covers[0].latitude=this.circles[0].latitude=e.latitude,this.covers[0].longitude=this.circles[0].longitude=e.longitude,this.r=this.circles[0].radius=e.radius,this.getTime(),this.getLocation()},methods:{getData:function(){var t=this,e=r.webApiURL+"/webApi/attendance/getData";uni.request({url:e,withCredentials:!0,xhrFields:{withCredentials:!0},data:{sessionId:this.sessionId},success:function(e){if(0==e.data.code){var n=e.data.data;t.covers[0].callout.content=n.address,t.covers[0].latitude=t.circles[0].latitude=n.latitude,t.covers[0].longitude=t.circles[0].longitude=n.longitude,t.r=t.circles[0].radius=n.scope}else{var i=e.data.msg;uni.showModal({title:"系统提示",content:i,position:"center",showCancel:!1,success:function(){uni.reLaunch({url:"/pages/login/login"})}})}}})},getTime:function(){var t=this;setInterval((function(){t.time=(0,a.formateDate)(new Date,"h:min:s")}),1e3)},clickSign:function(){this.userId,this.userName;var t=this.imgURL,e=this.remarks;setTimeout((function(){uni.hideLoading(),uni.navigateTo({url:"/pages/index/index?imgURL="+t+"&remarks="+e})}),1e3)},getAdd:function(){if(!0===this.isTrue){var e=this.covers[0].callout.content;return this.address=e,void(this.signInfo.address=e)}var n=this,i=r.webApiURL+"/webApi/api/map/?location=".concat(n.latitude,",").concat(n.longitude,"&key=").concat(r.key);uni.request({url:i,withCredentials:!0,xhrFields:{withCredentials:!0},success:function(t){var e=t.data;if(0==e.status){if(!1===n.isTrue){var i=t.data.result.address+t.data.result.formatted_addresses.recommend;n.address=i,n.signInfo.address=i}}else uni.showToast({title:e.message,icon:"none",position:"center"})},fail:function(e){t("log","腾讯位置定位接口调用失败: "+JSON.stringify(e)," at pages/sign/sign.vue:219")}})},controltap:function(t){this.getLocation()},getLocation:function(){var t=this;0!==this.clickNum&&uni.showLoading({title:"获取中...",mask:!0}),this.clickNum>=3?uni.showToast({title:"请稍后尝试！",icon:"none",mask:!0,position:"center"}):(this.clickNum++,uni.getLocation({type:"gcj02",success:function(e){uni.hideLoading(),t.accuracy=e.accuracy,t.latitude=e.latitude,t.longitude=e.longitude,t.scale=18,t.covers[1]={id:1,latitude:e.latitude,longitude:e.longitude,iconPath:"../../static/img/location.png"};var n=t.circles[0].latitude,i=t.circles[0].longitude,r=t.circles[0].radius,o=(0,a.countRadius)([t.latitude,t.longitude],[n,i],r);t.isTrue=o,t.signstate=o?"在考勤范围内":"不在考勤范围内",t.signText=o?"正常打卡":"外勤打卡",t.signInfo.latitude=e.latitude,t.signInfo.longitude=e.longitude,t.signInfo.mode=o?"正常打卡":"外勤打卡",t.getAdd()},fail:function(e){uni.hideLoading(),t.signText="请检查位置信息",uni.showToast({title:"请检查位置信息状态！",icon:"none",mask:!0,duration:1e3,position:"center"})}}))},openLocation:function(){var t=this;uni.chooseLocation({success:function(e){t.address=e.address,t.signInfo.address=e.address;var n=(0,a.pointInsideCircle)([t.latitude,t.longitude],[t.circles[0].latitude,t.circles[0].longitude],t.circles[0].radius);t.isTrue=n,t.signstate=n?"在考勤范围内":"不在考勤范围内",t.signText=n?"正常打卡":"外勤打卡"}})},getImageInfo1:function(e){t("log","图片返回1：",e," at pages/sign/sign.vue:293"),this.imgURL=e},isLegal:function(){var e=uni.getStorageSync("sessionId");void 0==e&&uni.showModal({title:"系统提示",content:"非法访问",position:"center",showCancel:!1,success:function(){uni.reLaunch({url:"/pages/login/login"})}});var n=r.webApiURL+"/webApi/verify";uni.request({url:n,withCredentials:!0,xhrFields:{withCredentials:!0},method:"POST",data:{sessionId:e},success:function(e){if(t("log","session验证信息:"+JSON.stringify(e.data)," at pages/sign/sign.vue:323"),0!=e.data.code){var n=e.data.data;uni.showModal({title:"系统提示",content:n.msg,position:"center",showCancel:!1,success:function(){uni.reLaunch({url:"/pages/login/login"})}})}},fail:function(t){uni.showModal({title:"系统提示",content:"系统错误,请重新联系管理员",position:"center",showCancel:!1,success:function(){uni.reLaunch({url:"/pages/error/500"})}})}})}}};e.default=s}).call(this,n("0de9")["log"])},f51f:function(t,e,n){"use strict";n.d(e,"b",(function(){return r})),n.d(e,"c",(function(){return a})),n.d(e,"a",(function(){return i}));var i={sunuiUpimg:n("68b5").default},r=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("v-uni-view",{staticClass:"content sign2"},[n("v-uni-view",{staticClass:"page-body"},[n("v-uni-view",{staticClass:"text-info"},[n("v-uni-view",{staticClass:"inputV",staticStyle:{display:"flex","justify-content":"space-between"}},[n("v-uni-view",[t._v("我的位置 （"),n("v-uni-text",{class:t.isTrue?"colorBlue":"colorRed"},[t._v(t._s(t.signstate))]),t._v("）")],1)],1),n("v-uni-view",{staticClass:"inputV"},[n("v-uni-view",{staticClass:"t"},[t._v("当前位置")]),n("v-uni-view",{staticClass:"text"},[t._v(t._s(t.address))])],1),n("v-uni-view",{staticClass:"inputV"},[n("v-uni-view",{staticClass:"t"},[t._v("备注")]),n("v-uni-input",{staticClass:"text",attrs:{type:"text",placeholder:"选填备注"},model:{value:t.remarks,callback:function(e){t.remarks=e},expression:"remarks"}})],1),n("v-uni-view",{staticClass:"inputV"},[n("v-uni-view",{staticClass:"t"},[t._v("上传图片")]),n("v-uni-view",{staticClass:"index-page"},[n("sunui-upimg",{ref:"upimg1",attrs:{upload_auto:!0,upload_count:3},on:{change:function(e){arguments[0]=e=t.$handleEvent(e),t.getImageInfo1.apply(void 0,arguments)}}})],1)],1),n("v-uni-view",{staticClass:"inputV"},[n("v-uni-button",{class:!0===t.isTrue?"bgBlue":!1===t.isTrue?"bgGreen":null===t.isTrue?"bgAsh":"",staticStyle:{color:"white"},on:{click:function(e){arguments[0]=e=t.$handleEvent(e),t.clickSign.apply(void 0,arguments)}}},[t._v(t._s(t.time)+" --- 保存")])],1)],1)],1)],1)},a=[]}}]);