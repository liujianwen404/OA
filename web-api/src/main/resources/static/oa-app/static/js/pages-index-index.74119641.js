(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["pages-index-index"],{"03ec":function(t,e,i){"use strict";var n=i("5ea6"),s=i.n(n);s.a},"25eb":function(t,e,i){var n=i("23e7"),s=i("c20d");n({target:"Number",stat:!0,forced:Number.parseInt!=s},{parseInt:s})},2687:function(t,e,i){"use strict";(function(t){var n=i("dbce"),s=i("4ea4");i("99af"),i("a9e3"),i("25eb"),i("ac1f"),i("1276"),Object.defineProperty(e,"__esModule",{value:!0}),e.default=void 0;var a=s(i("ade3")),o=s(i("9c41")),l=s(i("d812")),u=i("589c"),c=i("d512"),r=(n(i("a725")),{components:{uniIcon:l.default,uniPopup:o.default},data:function(){return{elasticityFlag:null,groupId:null,classId:null,userId:null,name:"我的名字",bzText:{time:"",address:"",img:"",remarks:""},type:"",r:0,Timer:[{time:"09:00",num:null},{time:"18:00",num:null}],isAm:!1,isPm:!1,amSign:{time:"",address:"",remarks:"",img:""},pmSign:{time:"",address:"",remarks:"",img:""},clickNum:0,is:null,isRest:!1,isSign:!1,time:(0,u.formateDate)(new Date,"h:min:s"),date:(0,u.formateDate)(new Date,"Y-M-D"),latitude:null,longitude:null,radius:null,address:null,wqInfo:null,imgURL:[],remarks:"",signInfo:{mode:"",latitude:"",longitude:"",address:"",time:"",remarks:"",upload:"",classId:"",groupId:"",userId:"",name:""},covers:[{id:0,callout:{content:null,color:"red",display:"ALWAYS"},latitude:null,longitude:null,iconPath:"../../../static/img/location.png"}],circles:[{latitude:null,longitude:null,radius:null,strokeWidth:1,fillColor:"#7fff0099"}]}},onLoad:function(e){this.name=e.userName,this.userId=e.userId,null!=e.imgURL&&""!=e.imgURL&&(this.imgURL=e.imgURL),null!=e.remarks&&""!=e.remarks&&(this.remarks=e.remarks),this.getData(),this.getLocation(),this.getTime(),t("log","当前位置信息: "+this.address," at pages/index/index.vue:292")},methods:{togglePopup:function(t){this.type=t},goRule:function(){uni.navigateTo({url:"/pages/rule/rule?name="+this.name})},getAdd:function(){if(!0===this.is){var e=this.covers[0].callout.content;return this.address=e,void(this.signInfo.address=e)}var i=this,n=c.testURL+"/webApi/api/map/?location=".concat(i.latitude,",").concat(i.longitude,"&key=").concat(c.key);uni.request({url:n,success:function(t){var e=t.data;if(0==e.status)if(null!=e.result.address&&void 0!=e.result.formatted_addresses){var n=e.result.address+e.result.formatted_addresses.recommend;i.address=n,i.signInfo.address=n}else i.address="请检查位置信息！",i.is=null;else uni.showToast({title:e.message,icon:"none",position:"center"})},fail:function(e){t("log","腾讯位置定位接口调用失败: "+JSON.stringify(e)," at pages/index/index.vue:344")}})},getData:function(){var e=this,i=c.testURL+"/webApi/attendance/getAttendanceGroupAndClass";uni.request({url:i,data:{userId:this.userId},success:function(i){if(t("log","初始化数据开始:"+JSON.stringify(i.data)," at pages/index/index.vue:359"),0==i.data.code){var n=i.data.data,s=n.isRest;null!=s&&(e.isRest=s),e.covers[0].callout.content=n.address,e.covers[0].latitude=e.circles[0].latitude=n.latitude,e.covers[0].longitude=e.circles[0].longitude=n.longitude,e.r=e.circles[0].radius=n.scope,e.Timer[0].time=n.work_time,e.Timer[1].time=n.closing_time,e.groupId=n.group_id,e.classId=n.class_id;var a=e.elasticityFlag=n.elasticity_flag,o=n.rest_start_time,l=n.clockIn;if(null!=l.length&&0!=l.length){t("log","打卡信息不为空"," at pages/index/index.vue:379");for(var u=0;u<l.length;u++){var c=l[u].check_type;if("OnDuty"==c){e.amSign.address=l[u].user_address,e.amSign.time=l[u].user_check_time,e.amSign.remarks=l[u].remark,e.isAm=!0,"Outside"==l[u].location_result?e.amSign.mode="外勤打卡":e.amSign.mode="正常打卡";var r=l[u].time_result;"Normal"==r?e.amSign.timeResult="Normal":"NotSigned"==r?(e.amSign.time=void 0,e.amSign.timeResult="NotSigned"):e.amSign.timeResult="AbsenteeismLateOnaDay"==r?"Absenteeism":"Late"}if("OffDuty"==c){e.pmSign.address=l[u].user_address,e.pmSign.time=l[u].user_check_time,e.pmSign.remarks=l[u].remark,e.isAm=!0,e.isPm=!0,"Outside"==l[u].location_result?e.pmSign.mode="外勤打卡":e.pmSign.mode="正常打卡";var d=l[u].time_result;e.pmSign.timeResult="Normal"==d?"Normal":"AbsenteeismEarlyOnaDay"==d?"Absenteeism":"Early"}}null!=e.amSign.time&&""!=e.amSign.time||(e.amSign.time=void 0)}else if(t("log","打卡信息为空"," at pages/index/index.vue:441"),null!=o){var v=Number.parseInt(o.split(":")[0]),m=60*Number.parseInt(v)+Number.parseInt(o.split(":")[1]),f=new Date,g=60*Number.parseInt(f.getHours())+Number.parseInt(f.getMinutes());t("log","restNum: "+m," at pages/index/index.vue:447"),t("log","nowNum: "+g," at pages/index/index.vue:448"),"0"==a&&g>m&&(null!=e.amSign.time&&""!=e.amSign.time||(e.amSign.time=void 0),e.isAm=!0)}t("log","初始化数据结束！"," at pages/index/index.vue:462")}else{var p=i.data.msg;uni.showModal({title:"系统提示",content:p,position:"center",showCancel:!1,success:function(){uni.reLaunch({url:"/pages/login/login"})}})}},fail:function(t){uni.showModal({title:"系统提示",content:msg,position:"center",showCancel:!1,success:function(){uni.reLaunch({url:"/pages/login/login"})}})}})},getClockIn:function(){var e=this,i=c.testURL+"/webApi/clockIn/getClockIn";uni.request({url:i,data:{userId:this.userId},success:function(i){if(t("log","获取员工当日打卡信息:"+JSON.stringify(i.data)," at pages/index/index.vue:501"),0==i.data.code){var n=i.data.data;if(null!=n.length||0!=n.length)for(var s=0;s<n.length;s++){var a=n[s].check_type;"OnDuty"==a&&(e.amSign.address=n[s].user_address,e.amSign.time=n[s].user_check_time,e.isAm=!0),"OffDuty"==a&&(e.pmSign.address=n[s].user_address,e.pmSign.time=n[s].user_check_time,e.isAm=!0,e.isPm=!0)}}},fail:function(t){uni.showModal({title:"系统提示",content:"系统错误,请重新联系管理员",position:"center",showCancel:!1,success:function(){uni.reLaunch({url:"/pages/login/login"})}})}})},addClockIn:function(){var t=this.covers[0].callout.content,e=this.circles[0].radius,i=this.circles[0].latitude,n=this.circles[0].longitude,s=this.userId,a=this.name;uni.navigateTo({url:"/pages/sign/sign?address="+t+"&radius="+e+"&latitude="+i+"&longitude="+n+"&userId="+s+"&name="+a})},bindDateChange:function(t){this.date=t.target.value},reset:function(){var e=this,i=this.isAm,n=this.isPm,s=this.userId,a=this.name;1==i&&1==n?uni.showModal({title:"重置本地打卡信息",content:"重置下班打卡信息,可以更新打卡信息",success:function(i){var n=c.testURL+"/webApi/clockIn/delClockIn";if(i.confirm)e.isSign=!1,e.isPm=!1,uni.request({url:n,data:{userId:s},success:function(e){t("log","删除下班卡接口成功:"+JSON.stringify(e)," at pages/index/index.vue:574");var i=e.data;if(0==i.code)uni.redirectTo({url:"/pages/index/index?userId="+s+"&userName="+a});else{var n=e.data.msg;uni.showModal({title:"系统提示",content:n,position:"center",showCancel:!1,success:function(){uni.redirectTo({url:"/pages/index/index?userId="+s+"&userName="+a})}})}},fail:function(e){t("log","删除打卡接口调用失败: "+JSON.stringify(e)," at pages/index/index.vue:592");var i=e.data.msg;uni.showModal({title:"系统提示",content:i,position:"center",showCancel:!1,success:function(){uni.redirectTo({url:"/pages/index/index?userId="+s+"&userName="+a})}})}});else if(i.cancel)return}}):uni.showModal({title:"重置本地打卡信息",content:"请直接打卡！",success:function(t){}})},showBz:function(t){this.type="middle-list","amSign"==t?(this.bzText.time=this.amSign.time,this.bzText.address=this.amSign.address,this.bzText.remarks=this.amSign.remarks):"pmSign"==t&&(this.bzText.time=this.pmSign.time,this.bzText.address=this.pmSign.address,this.bzText.remarks=this.pmSign.remarks)},getTime:function(){var t=this;setInterval((function(){t.time=(0,u.formateDate)(new Date,"h:min:s")}),100)},getLocation:function(){var t=this;if(0!==this.clickNum&&uni.showLoading({title:"获取中...",mask:!0}),this.clickNum>=3)return this.clickNum--,void uni.showToast({title:"请稍后尝试！",icon:"none",mask:!0,position:"center"});this.clickNum++,uni.getLocation({type:"gcj02",geocode:!0,success:function(e){uni.hideLoading(),t.latitude=e.latitude,t.longitude=e.longitude;var i=t.circles[0].latitude,n=t.circles[0].longitude,s=t.circles[0].radius;t.covers[1]={id:1,latitude:e.latitude,longitude:e.longitude,iconPath:"../../static/location.png"};var a=(0,u.countRadius)([t.latitude,t.longitude],[i,n],s);t.is=a,t.signInfo.latitude=e.latitude,t.signInfo.longitude=e.longitude,t.signInfo.mode=a?"正常打卡":"外勤打卡",t.getAdd()},fail:function(e){uni.hideLoading(),t.address="请检查位置信息！",uni.showToast({title:"请检查位置信息状态！",icon:"none",mask:!0,duration:3e3})}}),null!==this.is||(t.address="请检查位置信息！")},clickSign:function(){var e=this,i=this.is;if(null!==i){uni.showLoading({title:"打卡记录中...",mask:!0}),this.signInfo.time=(0,u.formateDate)(new Date,"Y-M-D h:min:s");var n=(0,c.getSignInfo)();void 0!=n?(0,c.addSignInfo)((0,c.getInfo)(this.signInfo),n):(0,c.setSignInfo)((0,c.getInfo)(this.signInfo));var s=(0,c.getInfo)(this.signInfo);t("log","点击打卡后的打卡数据:"+JSON.stringify(s)," at pages/index/index.vue:713");var o=this.groupId,l=this.classId,r=this.userId,d=this.name,v=this.imgURL,m=this.remarks;void 0==v&&(v=""),void 0==m&&(m="");var f=this.is;!1===this.isAm&&!1===this.isPm?uni.request({url:c.testURL+"/webApi/clockIn/saveClockIn",method:"POST",data:{mode:s.mode,nowT:s.nowT,address:s.address,time:s.time,longitude:s.longitude,latitude:s.latitude,remarks:m,userId:r,userName:d,groupId:o,classId:l,imgURL:v,is:f,isAm:!0,isPm:!1},header:{"content-type":"application/x-www-form-urlencoded"},success:function(i){t("log","上班卡成功回调: "+JSON.stringify(i)," at pages/index/index.vue:753"),0==i.data.code?(uni.showToast((0,a.default)({icon:"success",position:"bottom",title:"打卡成功"},"position","center")),uni.hideLoading(),setTimeout((function(){uni.redirectTo({url:"/pages/index/index?userId="+r+"&userName="+d})}),1e3)):(uni.showToast((0,a.default)({icon:"error",position:"bottom",title:"打卡异常"},"position","center")),(0,c.removeSignInfo)("signInfo"),e.isSign=!1,e.isAm=!1,e.amSign=[])},fail:function(t){uni.showToast((0,a.default)({icon:"success",position:"bottom",title:"系统错误"},"position","center")),(0,c.removeSignInfo)("signInfo"),e.isSign=!1,e.isAm=!1,e.amSign=[]}}):uni.request({url:c.testURL+"/webApi/clockIn/saveClockIn",method:"POST",data:{mode:s.mode,nowT:s.nowT,address:s.address,time:s.time,longitude:s.longitude,latitude:s.latitude,remarks:m,userId:r,userName:d,groupId:o,classId:l,imgURL:v,is:f,isAm:!1,isPm:!0},header:{"content-type":"application/x-www-form-urlencoded"},success:function(i){t("log","下班卡成功回调: "+JSON.stringify(i.data)," at pages/index/index.vue:826"),0==i.data.code?(uni.showToast((0,a.default)({icon:"success",position:"bottom",title:"打卡成功"},"position","center")),uni.hideLoading(),setTimeout((function(){uni.redirectTo({url:"/pages/index/index?userId="+r+"&userName="+d})}),1e3)):(uni.showToast((0,a.default)({icon:"error",position:"bottom",title:"打卡异常"},"position","center")),e.isSign=!1,e.isPm=!1,e.pmSign=[])},fail:function(t){uni.showToast((0,a.default)({icon:"success",position:"bottom",title:"系统错误"},"position","center")),e.isSign=!1,e.isPm=!1,e.pmSign=[]}})}else uni.showToast({title:"请检查位置信息状态！",icon:"none",mask:!0,duration:3e3,position:"center"})},openLocation:function(){var t=this;uni.chooseLocation({success:function(e){t.address=e.address,t.signInfo.address=e.address;var i=(0,u.countRadius)([t.latitude,t.longitude],[t.circles[0].latitude,t.circles[0].longitude],t.r);t.is=i}})},reLocation:function(){this.getLocation()}}});e.default=r}).call(this,i("0de9")["log"])},"26f7":function(t,e,i){"use strict";var n=i("a209"),s=i.n(n);s.a},"338f":function(t,e,i){var n=i("24fb");e=n(!1),e.push([t.i,".map[data-v-714cee29]{width:100%;height:260px}.uni-list-cell-left[data-v-714cee29]{padding:0 %?30?%}.text-desc[data-v-714cee29]{display:-webkit-box;display:-webkit-flex;display:flex;-webkit-box-pack:justify;-webkit-justify-content:space-between;justify-content:space-between;padding:%?10?% %?20?%}.colorRed[data-v-714cee29]{color:red}.colorGreen[data-v-714cee29]{color:#32cd32}.colorYellow[data-v-714cee29]{color:#ff0}.colorBlue[data-v-714cee29]{color:#007aff}.bgBlue[data-v-714cee29]{background-color:#007aff}.bgGreen[data-v-714cee29]{background-color:#32cd32}.bgAsh[data-v-714cee29]{background-color:#c8c7cc}.uni-timeline[data-v-714cee29]{padding:%?30?% %?20?%}.uni-timeline-item-content-t[data-v-714cee29]{font-weight:700}.desc-pad[data-v-714cee29]{padding:0 %?0?%}.desc-pad .bz[data-v-714cee29]{color:#007aff}.desc-pad .bz .icon[data-v-714cee29]{color:#007aff}.uni-timeline-last-item .uni-timeline-item-divider[data-v-714cee29]{background:#bbb}.CBlue[data-v-714cee29]{background-color:#007aff;-webkit-box-shadow:0 5px 5px #007aff;box-shadow:0 5px 5px #007aff}.CGreen[data-v-714cee29]{background-color:#32cd32;-webkit-box-shadow:0 5px 5px #32cd32;box-shadow:0 5px 5px #32cd32}.CAsh[data-v-714cee29]{background-color:#c8c7cc;-webkit-box-shadow:0 5px 5px #c8c7cc;box-shadow:0 5px 5px #c8c7cc}.module[data-v-714cee29]{overflow:hidden;margin:%?20?% auto;width:%?220?%;height:%?220?%;-webkit-border-radius:50%;border-radius:50%;color:#fff;text-align:center}.module .text[data-v-714cee29]{font-size:15px;margin:%?50?% auto %?10?%}.uni-timeline-item .uni-timeline-item-content[data-v-714cee29]{width:100%;padding-right:%?20?%}.content-show[data-v-714cee29]{width:100%}.sign-title[data-v-714cee29]{display:-webkit-box;display:-webkit-flex;display:flex;-webkit-box-pack:justify;-webkit-justify-content:space-between;justify-content:space-between;padding:%?30?% %?24?%;border-bottom:%?1?% solid #333}.sign-title .portrait[data-v-714cee29]{width:%?100?%;height:%?100?%;line-height:%?100?%;-webkit-border-radius:50%;border-radius:50%;background-color:#007aff;color:#fff;font-size:%?28?%;text-align:center}.sign-title .sign-title-l[data-v-714cee29]{display:-webkit-box;display:-webkit-flex;display:flex}.sign-title .sign-title-l .text[data-v-714cee29]{margin-left:%?20?%}.sign-title .sign-title-l .text .name[data-v-714cee29]{font-size:%?32?%}.sign-title .sign-title-l .text  .gz[data-v-714cee29]{color:#00008b;display:-webkit-inline-box;display:-webkit-inline-flex;display:inline-flex}.sign-title .sign-title-l .text .gz uni-text[data-v-714cee29]{display:inline-block}.sign-title .sign-title-l .text .gz .t1[data-v-714cee29]{overflow:hidden; /*超出部分隐藏*/text-overflow:ellipsis;/* 超出部分显示省略号 */white-space:nowrap;/*规定段落中的文本不进行换行 */width:%?166?%/*需要配合宽度来使用*/}.iswq[data-v-714cee29]{padding:0 0 20px 10px;color:#9c3;border:1px solid #9c3;width:40px;height:5px;-webkit-border-radius:4px;border-radius:4px;margin-left:%?20?%;display:inline-block}.isNormal[data-v-714cee29]{padding:0 0 20px 10px;color:#f50;border:1px solid #f50;width:40px;height:5px;-webkit-border-radius:4px;border-radius:4px;margin-left:%?20?%;display:inline-block}.desc-pad .last[data-v-714cee29]{margin-bottom:%?80?%}.addClockIn[data-v-714cee29]{color:#00f}.uni-popup .content .text[data-v-714cee29]{color:#666}",""]),t.exports=e},"589c":function(t,e,i){(function(e){function n(t){if("number"!==typeof t||t<0)return t;var e=parseInt(t/3600);t%=3600;var i=parseInt(t/60);t%=60;var n=t;return[e,i,n].map((function(t){return t=t.toString(),t[1]?t:"0"+t})).join(":")}function s(t,e){return"string"===typeof t&&"string"===typeof e&&(t=parseFloat(t),e=parseFloat(e)),t=t.toFixed(2),e=e.toFixed(2),{longitude:t.toString().split("."),latitude:e.toString().split(".")}}i("a15b"),i("d81d"),i("fb6a"),i("4e82"),i("a9e3"),i("b680"),i("b64b"),i("d3b7"),i("acd8"),i("e25e"),i("ac1f"),i("25f0"),i("1276");var a={UNITS:{"年":315576e5,"月":26298e5,"天":864e5,"小时":36e5,"分钟":6e4,"秒":1e3},humanize:function(t){var e="";for(var i in this.UNITS)if(t>=this.UNITS[i]){e=Math.floor(t/this.UNITS[i])+i+"前";break}return e||"刚刚"},format:function(t){var e=this.parse(t),i=Date.now()-e.getTime();if(i<this.UNITS["天"])return this.humanize(i);var n=function(t){return t<10?"0"+t:t};return e.getFullYear()+"/"+n(e.getMonth()+1)+"/"+n(e.getDay())+"-"+n(e.getHours())+":"+n(e.getMinutes())},parse:function(t){var e=t.split(/[^0-9]/);return new Date(e[0],e[1]-1,e[2],e[3],e[4],e[5])}};function o(t,e){var i=t.getFullYear(),n=("0"+(t.getMonth()+1)).slice(-2),s=("0"+t.getDate()).slice(-2),a=("0"+t.getHours()).slice(-2),o=("0"+t.getMinutes()).slice(-2),l=("0"+t.getSeconds()).slice(-2);if("Y-M-D h:min:s"===e)var u=i+"-"+n+"-"+s+" "+a+":"+o+":"+l;else if("Y-M-D"===e)u=i+"-"+n+"-"+s;if("h:min:s"===e)u=a+":"+o+":"+l;return u}function l(t){for(var e=[],i={},n=0;n<t.length;n++){var s=Object.keys(t[n]);s.sort((function(t,e){return Number(t)-Number(e)}));for(var a="",o=0;o<s.length;o++)a+=JSON.stringify(s[o]),a+=JSON.stringify(t[n][s[o]]);i.hasOwnProperty(a)||(e.push(t[n]),i[a]=!0)}return e=e,e}function o(t,e){var i=t.getFullYear(),n=("0"+(t.getMonth()+1)).slice(-2),s=("0"+t.getDate()).slice(-2),a=("0"+t.getHours()).slice(-2),o=("0"+t.getMinutes()).slice(-2),l=("0"+t.getSeconds()).slice(-2);if("Y-M-D h:min:s"===e)var u=i+"-"+n+"-"+s+" "+a+":"+o+":"+l;else if("Y-M-D"===e)u=i+"-"+n+"-"+s;if("h:min:s"===e)u=a+":"+o+":"+l;else if("h"===e)u=a;else if("min"===e)u=o;return u}function u(t,e){switch(arguments.length){case 1:return parseInt(Math.random()*t+1,10);case 2:return parseInt(Math.random()*(e-t+1)+t,10);default:return 0}}function c(t,e,i){if(0===i)return!1;var n=e[0]-t[0],s=e[1]-t[1];return n*n+s*s<=i*i}function r(t,i,n){if(0===n)return!1;var s=t[0]*Math.PI/180,a=i[0]*Math.PI/180,o=s-a,l=t[1]*Math.PI/180-i[1]*Math.PI/180,u=2*Math.asin(Math.sqrt(Math.pow(Math.sin(o/2),2)+Math.cos(s)*Math.cos(a)*Math.pow(Math.sin(l/2),2)));u*=6378.137,u=Math.round(1e4*u)/1e4;var c=1e3*u;return e("log","当前坐标位置与打卡圆心距离："+c," at common/util.js:163"),c<=n}function d(t){var e=new Date(t),i=new Date;return e.setHours(0,0,0,0)==i.setHours(0,0,0,0)}t.exports={formatTime:n,formatLocation:s,dateUtils:a,formateDate:o,deteleObject:l,randomNum:u,pointInsideCircle:c,isSameDay:d,countRadius:r}}).call(this,i("0de9")["log"])},"5ca3":function(t,e,i){"use strict";i.r(e);var n=i("794f"),s=i.n(n);for(var a in n)"default"!==a&&function(t){i.d(e,t,(function(){return n[t]}))}(a);e["default"]=s.a},"5ea6":function(t,e,i){var n=i("338f");"string"===typeof n&&(n=[[t.i,n,""]]),n.locals&&(t.exports=n.locals);var s=i("4f06").default;s("a82a5fb2",n,!0,{sourceMap:!1,shadowMode:!1})},"78b3":function(t,e,i){"use strict";i.r(e);var n=i("bdcf"),s=i("d544");for(var a in s)"default"!==a&&function(t){i.d(e,t,(function(){return s[t]}))}(a);i("03ec");var o,l=i("f0c5"),u=Object(l["a"])(s["default"],n["b"],n["c"],!1,null,"714cee29",null,!1,n["a"],o);e["default"]=u.exports},"794f":function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default=void 0;var n={name:"uni-popup",props:{show:{type:Boolean,default:!1},position:{type:String,default:"middle"},mode:{type:String,default:"insert"},msg:{type:String,default:""},h5Top:{type:Boolean,default:!1},buttonMode:{type:String,default:"bottom"}},data:function(){return{offsetTop:0}},watch:{h5Top:function(t){this.offsetTop=t?44:0}},methods:{hide:function(){"insert"===this.mode&&"middle"===this.position||this.$emit("hidePopup")},closeMask:function(){"insert"===this.mode&&this.$emit("hidePopup")},moveHandle:function(){}},created:function(){var t=0;t=this.h5Top?0:44,this.offsetTop=t}};e.default=n},"9c41":function(t,e,i){"use strict";i.r(e);var n=i("e0f6"),s=i("5ca3");for(var a in s)"default"!==a&&function(t){i.d(e,t,(function(){return s[t]}))}(a);i("26f7");var o,l=i("f0c5"),u=Object(l["a"])(s["default"],n["b"],n["c"],!1,null,"7722493f",null,!1,n["a"],o);e["default"]=u.exports},a209:function(t,e,i){var n=i("de50");"string"===typeof n&&(n=[[t.i,n,""]]),n.locals&&(t.exports=n.locals);var s=i("4f06").default;s("721b64b4",n,!0,{sourceMap:!1,shadowMode:!1})},bdcf:function(t,e,i){"use strict";i.d(e,"b",(function(){return s})),i.d(e,"c",(function(){return a})),i.d(e,"a",(function(){return n}));var n={uniIcon:i("d812").default,uniPopup:i("9c41").default},s=function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("v-uni-view",{staticClass:"content"},[i("v-uni-view",{staticClass:"page-body"},[i("v-uni-view",{staticClass:"sign-title"},[i("v-uni-view",{staticClass:"sign-title-l"},[i("v-uni-view",{staticClass:"portrait"},[t._v(t._s(t.name))])],1),i("v-uni-view",{staticClass:"sign-title-r"},[i("v-uni-view",{staticClass:"date"},[i("v-uni-view",{staticClass:"uni-input"},[t._v(t._s(t.date)),i("uni-icon",{attrs:{type:"arrowdown"}})],1)],1)],1)],1),i("v-uni-text",{on:{click:function(e){arguments[0]=e=t.$handleEvent(e),t.reset.apply(void 0,arguments)}}},[t._v("重置")]),i("v-uni-view",{staticStyle:{"text-align":"center",padding:"20upx 0"}},[t._v("当前位置:"),i("v-uni-text",[t._v(t._s(t.address))])],1),i("v-uni-view",{staticClass:"uni-timeline"},[i("v-uni-view",{staticClass:"uni-timeline-item uni-timeline-first-item"},[i("v-uni-view",{staticClass:"uni-timeline-item-divider",style:{background:t.isAm?"#bbb":"#1AAD19"}}),i("v-uni-view",{staticClass:"uni-timeline-item-content"},[i("v-uni-view",[i("v-uni-view",{staticClass:"uni-timeline-item-content-t1"},[t._v("上班时间"+t._s(t.Timer[0].time))]),t.isAm?i("v-uni-view",{staticClass:"desc-pad"},[void 0===t.amSign.time?i("v-uni-view",[i("v-uni-view",{staticClass:"time uni-timeline-item-content-t"},[t._v("缺卡")])],1):i("v-uni-view",[i("v-uni-view",{staticClass:"time uni-timeline-item-content-t"},[t._v("打卡时间 "+t._s(t.amSign.time.substring(10,16))),"外勤打卡"==t.amSign.mode?i("v-uni-view",{staticClass:"iswq"},[t._v("外勤")]):t._e(),"Late"==t.amSign.timeResult?i("v-uni-view",{staticClass:"isNormal"},[t._v("迟到")]):t._e(),"Absenteeism"==t.amSign.timeResult?i("v-uni-view",{staticClass:"isNormal"},[t._v("旷工")]):t._e()],1),i("v-uni-view",[i("uni-icon",{attrs:{type:"location-filled"}}),t._v(t._s(t.amSign.address))],1),i("v-uni-view",{staticClass:"bz last",on:{click:function(e){arguments[0]=e=t.$handleEvent(e),t.showBz("amSign")}}},[t._v("备注"),i("uni-icon",{style:{color:"rgb(0, 122, 255)"},attrs:{type:"forward"}})],1)],1)],1):i("v-uni-view",{staticClass:"content-show"},[!0===t.isRest?i("v-uni-view",[i("v-uni-view",{staticClass:"module CAsh"},[i("v-uni-view",{staticClass:"text"},[t._v("无法打卡")]),i("v-uni-view",{staticClass:"time"},[t._v(t._s(t.time))])],1),t.isRest?i("v-uni-view",{staticClass:"colorBlack",staticStyle:{"text-align":"center"}},[t._v("不属于考勤打卡时间")]):t._e()],1):i("v-uni-view",[!0===t.is?i("v-uni-view",[i("v-uni-view",{staticClass:"module CBlue",on:{click:function(e){arguments[0]=e=t.$handleEvent(e),t.clickSign.apply(void 0,arguments)}}},[i("v-uni-view",{staticClass:"text"},[t._v("上班打卡")]),i("v-uni-view",{staticClass:"time"},[t._v(t._s(t.time))])],1),t.is?i("v-uni-view",{staticClass:"colorGreen",staticStyle:{"text-align":"center"}},[t._v("已在考勤范围内"),i("v-uni-text",{staticClass:"addClockIn",on:{click:function(e){arguments[0]=e=t.$handleEvent(e),t.addClockIn.apply(void 0,arguments)}}},[t._v("添加打卡信息")])],1):t._e()],1):!1===t.is?i("v-uni-view",[i("v-uni-view",{staticClass:"module CGreen",on:{click:function(e){arguments[0]=e=t.$handleEvent(e),t.clickSign.apply(void 0,arguments)}}},[i("v-uni-view",{staticClass:"text"},[t._v("外勤打卡")]),i("v-uni-view",{staticClass:"time"},[t._v(t._s(t.time))])],1),t.is?t._e():i("v-uni-view",{staticClass:"colorRed",staticStyle:{"text-align":"center"}},[t._v("不在考勤范围内"),i("v-uni-text",{staticClass:"addClockIn",on:{click:function(e){arguments[0]=e=t.$handleEvent(e),t.addClockIn.apply(void 0,arguments)}}},[t._v("添加打卡信息")])],1)],1):null===t.is?i("v-uni-view",[i("v-uni-view",{staticClass:"module CAsh"},[i("v-uni-view",{staticClass:"text"},[t._v("请检查位置信息")]),i("v-uni-view",{staticClass:"time"},[t._v(t._s(t.time))])],1)],1):t._e()],1)],1)],1)],1)],1),i("v-uni-view",{staticClass:"uni-timeline-item uni-timeline-last-item"},[i("v-uni-view",{staticClass:"uni-timeline-item-divider",style:{background:t.isAm?"#1AAD19":"#bbb"}}),i("v-uni-view",{staticClass:"uni-timeline-item-content"},[i("v-uni-view",[i("v-uni-view",{staticClass:"uni-timeline-item-content-t1"},[t._v("下班时间"+t._s(t.Timer[1].time))]),t.isAm?i("v-uni-view",{staticClass:"desc-pad"},[t.isPm?i("v-uni-view",{staticClass:"desc-pad"},[void 0===t.pmSign.time?i("v-uni-view",[i("v-uni-view",{staticClass:"time uni-timeline-item-content-t"},[t._v("缺卡")])],1):i("v-uni-view",[i("v-uni-view",{staticClass:"time uni-timeline-item-content-t"},[t._v("打卡时间:"+t._s(t.pmSign.time.substring(10,16))),"外勤打卡"==t.pmSign.mode?i("v-uni-view",{staticClass:"iswq"},[t._v("外勤")]):t._e(),"Early"==t.pmSign.timeResult?i("v-uni-view",{staticClass:"isNormal"},[t._v("早退")]):t._e(),"Absenteeism"==t.pmSign.timeResult?i("v-uni-view",{staticClass:"isNormal"},[t._v("旷工")]):t._e()],1),i("v-uni-view",[i("uni-icon",{attrs:{type:"location-filled"}}),t._v(t._s(t.pmSign.address))],1),i("v-uni-view",{staticClass:"bz last",on:{click:function(e){arguments[0]=e=t.$handleEvent(e),t.showBz("pmSign")}}},[t._v("备注"),i("uni-icon",{style:{color:"rgb(0, 122, 255)"},attrs:{type:"forward"}})],1)],1)],1):i("v-uni-view",{staticClass:"content-show"},[!0===t.isRest?i("v-uni-view",[i("v-uni-view",{staticClass:"module CGreen"},[i("v-uni-view",{staticClass:"text"},[t._v("无法打卡")]),i("v-uni-view",{staticClass:"time"},[t._v(t._s(t.time))])],1),t.isRest?i("v-uni-view",{staticClass:"colorBlack",staticStyle:{"text-align":"center"}},[t._v("不属于考勤打卡时间")]):t._e()],1):i("v-uni-view",[!0===t.is?i("v-uni-view",[i("v-uni-view",{staticClass:"module CBlue",on:{click:function(e){arguments[0]=e=t.$handleEvent(e),t.clickSign.apply(void 0,arguments)}}},[i("v-uni-view",{staticClass:"text"},[t._v("下班打卡")]),i("v-uni-view",{staticClass:"time"},[t._v(t._s(t.time))])],1),t.is?i("v-uni-view",{staticClass:"colorGreen",staticStyle:{"text-align":"center"}},[t._v("已在考勤范围内"),i("v-uni-text",{staticClass:"addClockIn",on:{click:function(e){arguments[0]=e=t.$handleEvent(e),t.addClockIn.apply(void 0,arguments)}}},[t._v("添加打卡信息")])],1):t._e()],1):!1===t.is?i("v-uni-view",[i("v-uni-view",{staticClass:"module CGreen",on:{click:function(e){arguments[0]=e=t.$handleEvent(e),t.clickSign.apply(void 0,arguments)}}},[i("v-uni-view",{staticClass:"text"},[t._v("外勤打卡")]),i("v-uni-view",{staticClass:"time"},[t._v(t._s(t.time))])],1),t.is?t._e():i("v-uni-view",{staticClass:"colorRed",staticStyle:{"text-align":"center"}},[t._v("不在考勤范围内"),i("v-uni-text",{staticClass:"addClockIn",on:{click:function(e){arguments[0]=e=t.$handleEvent(e),t.addClockIn.apply(void 0,arguments)}}},[t._v("添加打卡信息")])],1)],1):null===t.is?i("v-uni-view",[i("v-uni-view",{staticClass:"module CAsh"},[i("v-uni-view",{staticClass:"text"},[t._v("请检查位置信息")]),i("v-uni-view",{staticClass:"time"},[t._v(t._s(t.time))])],1)],1):t._e()],1)],1)],1):t._e()],1)],1)],1)],1),i("v-uni-text",{on:{click:function(e){arguments[0]=e=t.$handleEvent(e),t.reLocation.apply(void 0,arguments)}}},[t._v("重新定位")])],1),i("uni-popup",{attrs:{show:"middle-list"===t.type,position:"middle",mode:"fixed"},on:{hidePopup:function(e){arguments[0]=e=t.$handleEvent(e),t.togglePopup("")}}},[i("v-uni-view",{staticClass:"title",staticStyle:{padding:"20upx 0","font-weight":"bold",width:"100%","text-align":"center","border-bottom":"1px solid #666"}},[t._v("打卡备注")]),i("v-uni-view",{staticClass:"content",staticStyle:{padding:"20upx 10upx",width:"100%"}},[i("v-uni-text",{staticClass:"text"},[t._v("打卡时间:")]),i("v-uni-text",[t._v(t._s(t.bzText.time))]),i("v-uni-view",{staticClass:"text"},[t._v("打卡地点:")]),i("v-uni-view",[t._v(t._s(t.bzText.address))])],1),i("v-uni-view",{staticClass:"content",staticStyle:{padding:"20upx 10upx",width:"100%"}},[i("v-uni-view",[t._v(t._s(t.bzText.remarks))])],1),i("v-uni-view",{staticClass:"bottom",staticStyle:{padding:"20upx 10upx",color:"cadetblue",width:"100%","text-align":"center","border-top":"1px solid #666"},on:{click:function(e){arguments[0]=e=t.$handleEvent(e),t.togglePopup("")}}},[t._v("关闭")])],1)],1)},a=[]},d544:function(t,e,i){"use strict";i.r(e);var n=i("2687"),s=i.n(n);for(var a in n)"default"!==a&&function(t){i.d(e,t,(function(){return n[t]}))}(a);e["default"]=s.a},de50:function(t,e,i){var n=i("24fb");e=n(!1),e.push([t.i,'.uni-mask[data-v-7722493f]{height:100vh;position:fixed;z-index:998;top:0;right:0;bottom:0;left:0;background-color:rgba(0,0,0,.3)}.uni-popup[data-v-7722493f]{position:absolute;z-index:999;background-color:#fff}.uni-popup-middle[data-v-7722493f]{display:-webkit-box;display:-webkit-flex;display:flex;-webkit-box-orient:vertical;-webkit-box-direction:normal;-webkit-flex-direction:column;flex-direction:column;-webkit-box-align:center;-webkit-align-items:center;align-items:center;-webkit-box-pack:center;-webkit-justify-content:center;justify-content:center;top:50%;left:50%;-webkit-transform:translate(-50%,-50%);transform:translate(-50%,-50%)}.uni-popup-middle.uni-popup-insert[data-v-7722493f]{min-width:%?380?%;min-height:%?380?%;max-width:100%;max-height:80%;-webkit-transform:translate(-50%,-65%);transform:translate(-50%,-65%);background:none;-webkit-box-shadow:none;box-shadow:none}.uni-popup-middle.uni-popup-fixed[data-v-7722493f]{width:68%;padding:0 %?20?%;-webkit-border-radius:6px;border-radius:6px}.uni-close-bottom[data-v-7722493f],\n.uni-close-right[data-v-7722493f]{position:absolute;bottom:%?-180?%;text-align:center;-webkit-border-radius:50%;border-radius:50%;color:#f5f5f5;font-size:%?60?%;font-weight:700;opacity:.8;z-index:-1}.uni-close-right[data-v-7722493f]{right:%?-60?%;top:%?-80?%}.uni-close-bottom[data-v-7722493f]:after{content:"";position:absolute;width:0;border:1px #f5f5f5 solid;top:%?-200?%;bottom:%?56?%;left:50%;-webkit-transform:translate(-50%);transform:translate(-50%);opacity:.8}.uni-popup-top[data-v-7722493f]{top:0;left:0;width:100%;height:%?100?%;line-height:%?100?%;text-align:center}.uni-popup-bottom[data-v-7722493f]{left:0;bottom:0;width:100%;min-height:%?100?%;line-height:%?100?%;text-align:center}',""]),t.exports=e},e0f6:function(t,e,i){"use strict";var n;i.d(e,"b",(function(){return s})),i.d(e,"c",(function(){return a})),i.d(e,"a",(function(){return n}));var s=function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("v-uni-view",[i("v-uni-view",{directives:[{name:"show",rawName:"v-show",value:t.show,expression:"show"}],staticClass:"uni-mask",style:{top:t.offsetTop+"px"},on:{click:function(e){arguments[0]=e=t.$handleEvent(e),t.hide.apply(void 0,arguments)},touchmove:function(e){e.stopPropagation(),e.preventDefault(),arguments[0]=e=t.$handleEvent(e),t.moveHandle.apply(void 0,arguments)}}}),i("v-uni-view",{directives:[{name:"show",rawName:"v-show",value:t.show,expression:"show"}],staticClass:"uni-popup",class:"uni-popup-"+t.position+" uni-popup-"+t.mode},[t._v(t._s(t.msg)),t._t("default"),"middle"===t.position&&"insert"===t.mode?i("v-uni-view",{staticClass:" uni-icon uni-icon-close",class:{"uni-close-bottom":"bottom"===t.buttonMode,"uni-close-right":"right"===t.buttonMode},on:{click:function(e){arguments[0]=e=t.$handleEvent(e),t.closeMask.apply(void 0,arguments)}}}):t._e()],2)],1)},a=[]}}]);