(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["pages-index-index"],{"25eb":function(i,t,e){var s=e("23e7"),n=e("c20d");s({target:"Number",stat:!0,forced:Number.parseInt!=n},{parseInt:n})},2687:function(i,t,e){"use strict";(function(i){var s=e("dbce"),n=e("4ea4");e("99af"),e("a9e3"),e("25eb"),e("ac1f"),e("1276"),Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var a=n(e("ade3")),o=n(e("9c41")),r=n(e("d812")),l=n(e("6c8a")),u=e("589c"),d=e("d512"),c=(s(e("a725")),{components:{uniIcon:r.default,uniPopup:o.default},data:function(){return{elasticityFlag:null,groupId:null,classId:null,userId:null,name:"我的名字",bzText:{time:"",address:"",img:"",remarks:""},type:"",r:0,Timer:[{time:"09:00",num:null},{time:"18:00",num:null}],isAm:!1,isPm:!1,amSign:{time:"",address:"",remarks:"",img:""},pmSign:{time:"",address:"",remarks:"",img:""},clickNum:0,is:null,isRest:!1,isSign:!1,time:(0,u.formateDate)(new Date,"h:min:s"),date:(0,u.formateDate)(new Date,"Y-M-D"),latitude:null,longitude:null,radius:null,address:null,wqInfo:null,imgURL:[],remarks:"",signInfo:{mode:"",latitude:"",longitude:"",address:"",time:"",remarks:"",upload:"",classId:"",groupId:"",userId:"",name:""},covers:[{id:0,callout:{content:null,color:"red",display:"ALWAYS"},latitude:null,longitude:null,iconPath:"../../../static/img/location.png"}],circles:[{latitude:null,longitude:null,radius:null,strokeWidth:1,fillColor:"#7fff0099"}]}},onLoad:function(t){this.name=t.userName,this.userId=t.userId,null!=t.imgURL&&""!=t.imgURL&&(this.imgURL=t.imgURL),null!=t.remarks&&""!=t.remarks&&(this.remarks=t.remarks),this.getData(),this.getLocation(),this.getTime(),i("log","当前位置信息: "+this.address," at pages/index/index.vue:293")},methods:{togglePopup:function(i){this.type=i},goRule:function(){uni.navigateTo({url:"/pages/rule/rule?name="+this.name})},getAdd:function(){if(!0===this.is){var t=this.covers[0].callout.content;return this.address=t,void(this.signInfo.address=t)}var e=this,s=d.testURL+"/webApi/api/map/?location=".concat(e.latitude,",").concat(e.longitude,"&key=").concat(d.key);uni.request({url:s,success:function(i){var t=i.data;if(0==t.status)if(null!=t.result.address&&void 0!=t.result.formatted_addresses){var s=t.result.address+t.result.formatted_addresses.recommend;e.address=s,e.signInfo.address=s}else e.address="请检查位置信息！",e.is=null;else uni.showToast({title:t.message,icon:"none",position:"center"})},fail:function(t){i("log","腾讯位置定位接口调用失败: "+JSON.stringify(t)," at pages/index/index.vue:345")}})},getData:function(){var t=this,e=d.testURL+"/webApi/attendance/getAttendanceGroupAndClass";uni.request({url:e,data:{userId:this.userId},success:function(e){if(i("log","初始化数据开始:"+JSON.stringify(e.data)," at pages/index/index.vue:360"),0==e.data.code){var s=e.data.data,n=s.isRest;null!=n&&(t.isRest=n),t.covers[0].callout.content=s.address,t.covers[0].latitude=t.circles[0].latitude=s.latitude,t.covers[0].longitude=t.circles[0].longitude=s.longitude,t.r=t.circles[0].radius=s.scope,t.Timer[0].time=s.work_time,t.Timer[1].time=s.closing_time,t.groupId=s.group_id,t.classId=s.class_id;var a=t.elasticityFlag=s.elasticity_flag,o=s.rest_start_time,r=s.clockIn;if(null!=r.length&&0!=r.length){i("log","打卡信息不为空"," at pages/index/index.vue:380");for(var l=0;l<r.length;l++){var u=r[l].check_type;if("OnDuty"==u){t.amSign.address=r[l].user_address,t.amSign.time=r[l].user_check_time,t.amSign.remarks=r[l].remark,t.isAm=!0,"Outside"==r[l].location_result?t.amSign.mode="外勤打卡":t.amSign.mode="正常打卡";var d=r[l].time_result;"Normal"==d?t.amSign.timeResult="Normal":"NotSigned"==d?(t.amSign.time=void 0,t.amSign.timeResult="NotSigned"):t.amSign.timeResult="AbsenteeismLateOnaDay"==d?"Absenteeism":"Late"}if("OffDuty"==u){t.pmSign.address=r[l].user_address,t.pmSign.time=r[l].user_check_time,t.pmSign.remarks=r[l].remark,t.isAm=!0,t.isPm=!0,"Outside"==r[l].location_result?t.pmSign.mode="外勤打卡":t.pmSign.mode="正常打卡";var c=r[l].time_result;t.pmSign.timeResult="Normal"==c?"Normal":"AbsenteeismEarlyOnaDay"==c?"Absenteeism":"Early"}}null!=t.amSign.time&&""!=t.amSign.time||(t.amSign.time=void 0)}else if(i("log","打卡信息为空"," at pages/index/index.vue:442"),null!=o){var m=Number.parseInt(o.split(":")[0]),p=60*Number.parseInt(m)+Number.parseInt(o.split(":")[1]),v=new Date,g=60*Number.parseInt(v.getHours())+Number.parseInt(v.getMinutes());i("log","restNum: "+p," at pages/index/index.vue:448"),i("log","nowNum: "+g," at pages/index/index.vue:449"),"0"==a&&g>p&&(null!=t.amSign.time&&""!=t.amSign.time||(t.amSign.time=void 0),t.isAm=!0)}i("log","初始化数据结束！"," at pages/index/index.vue:463")}else{var f=e.data.msg;uni.showModal({title:"系统提示",content:f,position:"center",showCancel:!1,success:function(){uni.reLaunch({url:"/pages/login/login"})}})}},fail:function(i){uni.showModal({title:"系统提示",content:msg,position:"center",showCancel:!1,success:function(){uni.reLaunch({url:"/pages/login/login"})}})}})},getClockIn:function(){var t=this,e=d.testURL+"/webApi/clockIn/getClockIn";uni.request({url:e,data:{userId:this.userId},success:function(e){if(i("log","获取员工当日打卡信息:"+JSON.stringify(e.data)," at pages/index/index.vue:502"),0==e.data.code){var s=e.data.data;if(null!=s.length||0!=s.length)for(var n=0;n<s.length;n++){var a=s[n].check_type;"OnDuty"==a&&(t.amSign.address=s[n].user_address,t.amSign.time=s[n].user_check_time,t.isAm=!0),"OffDuty"==a&&(t.pmSign.address=s[n].user_address,t.pmSign.time=s[n].user_check_time,t.isAm=!0,t.isPm=!0)}}},fail:function(i){uni.showModal({title:"系统提示",content:"系统错误,请重新联系管理员",position:"center",showCancel:!1,success:function(){uni.reLaunch({url:"/pages/login/login"})}})}})},addClockIn:function(){var i=this.covers[0].callout.content,t=this.circles[0].radius,e=this.circles[0].latitude,s=this.circles[0].longitude,n=this.userId,a=this.name;uni.navigateTo({url:"/pages/sign/sign?address="+i+"&radius="+t+"&latitude="+e+"&longitude="+s+"&userId="+n+"&name="+a})},bindDateChange:function(i){this.date=i.target.value},reset:function(){var t=this,e=this.isAm,s=this.isPm,n=this.userId,a=this.name;1==e&&1==s?uni.showModal({title:"重置本地打卡信息",content:"重置下班打卡信息,可以更新打卡信息",success:function(e){var s=d.testURL+"/webApi/clockIn/delClockIn";if(e.confirm)t.isSign=!1,t.isPm=!1,uni.request({url:s,data:{userId:n},success:function(t){i("log","删除下班卡接口成功:"+JSON.stringify(t)," at pages/index/index.vue:575");var e=t.data;if(0==e.code)uni.redirectTo({url:"/pages/index/index?userId="+n+"&userName="+a});else{var s=t.data.msg;uni.showModal({title:"系统提示",content:s,position:"center",showCancel:!1,success:function(){uni.redirectTo({url:"/pages/index/index?userId="+n+"&userName="+a})}})}},fail:function(t){i("log","删除打卡接口调用失败: "+JSON.stringify(t)," at pages/index/index.vue:593");var e=t.data.msg;uni.showModal({title:"系统提示",content:e,position:"center",showCancel:!1,success:function(){uni.redirectTo({url:"/pages/index/index?userId="+n+"&userName="+a})}})}});else if(e.cancel)return}}):uni.showModal({title:"重置本地打卡信息",content:"请直接打卡！",success:function(i){}})},showBz:function(i){this.type="middle-list","amSign"==i?(this.bzText.time=this.amSign.time,this.bzText.address=this.amSign.address,this.bzText.remarks=this.amSign.remarks):"pmSign"==i&&(this.bzText.time=this.pmSign.time,this.bzText.address=this.pmSign.address,this.bzText.remarks=this.pmSign.remarks)},getTime:function(){var i=this;setInterval((function(){i.time=(0,u.formateDate)(new Date,"h:min:s")}),100)},getLocation:function(){var t=this;if(0!==this.clickNum&&uni.showLoading({title:"获取中...",mask:!0}),this.clickNum>=3)return this.clickNum--,void uni.showToast({title:"请稍后尝试！",icon:"none",mask:!0,position:"center"});this.clickNum++,uni.getSystemInfo({success:function(e){"ios"==e.platform?(i("log","ios定位方法==============="," at pages/index/index.vue:660"),uni.getLocation({type:"wgs84",geocode:!0,success:function(i){uni.hideLoading(),t.latitude=i.latitude,t.longitude=i.longitude;var e=t.circles[0].latitude,s=t.circles[0].longitude,n=t.circles[0].radius;t.covers[1]={id:1,latitude:i.latitude,longitude:i.longitude,iconPath:"../../static/location.png"};var a=(0,u.countRadius)([t.latitude,t.longitude],[e,s],n);t.is=a,t.signInfo.latitude=i.latitude,t.signInfo.longitude=i.longitude,t.signInfo.mode=a?"正常打卡":"外勤打卡",t.getAdd()},fail:function(t){i("log","获取当前位置失败，打开当前App的权限设置界面。可用于引导用户赋权======>"+JSON.stringify(t)," at pages/index/index.vue:689"),l.default.gotoAppPermissionSetting()}})):"android"==e.platform&&(i("log","android定位方法==============="," at pages/index/index.vue:695"),uni.getLocation({type:"gcj02",geocode:!0,success:function(i){uni.hideLoading(),t.latitude=i.latitude,t.longitude=i.longitude;var e=t.circles[0].latitude,s=t.circles[0].longitude,n=t.circles[0].radius;t.covers[1]={id:1,latitude:i.latitude,longitude:i.longitude,iconPath:"../../static/location.png"};var a=(0,u.countRadius)([t.latitude,t.longitude],[e,s],n);t.is=a,t.signInfo.latitude=i.latitude,t.signInfo.longitude=i.longitude,t.signInfo.mode=a?"正常打卡":"外勤打卡",t.getAdd()},fail:function(t){i("log","获取当前位置失败，打开当前App的权限设置界面。可用于引导用户赋权======>"+JSON.stringify(t)," at pages/index/index.vue:727"),l.default.gotoAppPermissionSetting()}}))},fail:function(i){uni.hideLoading(),t.address="请检查位置信息！",t.is=null,uni.showToast({title:"请检查位置信息状态！",icon:"none",mask:!0,duration:3e3})}}),null!==this.is||(t.address="请检查位置信息！")},clickSign:function(){var t=this,e=this.is;if(null!==e){uni.showLoading({title:"打卡记录中...",mask:!0}),this.signInfo.time=(0,u.formateDate)(new Date,"Y-M-D h:min:s");var s=(0,d.getSignInfo)();void 0!=s?(0,d.addSignInfo)((0,d.getInfo)(this.signInfo),s):(0,d.setSignInfo)((0,d.getInfo)(this.signInfo));var n=(0,d.getInfo)(this.signInfo);i("log","点击打卡后的打卡数据:"+JSON.stringify(n)," at pages/index/index.vue:768");var o=this.groupId,r=this.classId,l=this.userId,c=this.name,m=this.imgURL,p=this.remarks;void 0==m&&(m=""),void 0==p&&(p="");var v=this.is;!1===this.isAm&&!1===this.isPm?uni.request({url:d.testURL+"/webApi/clockIn/saveClockIn",method:"POST",data:{mode:n.mode,nowT:n.nowT,address:n.address,time:n.time,longitude:n.longitude,latitude:n.latitude,remarks:p,userId:l,userName:c,groupId:o,classId:r,imgURL:m,is:v,isAm:!0,isPm:!1},header:{"content-type":"application/x-www-form-urlencoded"},success:function(e){i("log","上班卡成功回调: "+JSON.stringify(e)," at pages/index/index.vue:808"),0==e.data.code?(uni.showToast((0,a.default)({icon:"success",position:"bottom",title:"打卡成功"},"position","center")),uni.hideLoading(),setTimeout((function(){uni.redirectTo({url:"/pages/index/index?userId="+l+"&userName="+c})}),1e3)):(uni.showToast((0,a.default)({icon:"error",position:"bottom",title:"打卡异常"},"position","center")),(0,d.removeSignInfo)("signInfo"),t.isSign=!1,t.isAm=!1,t.amSign=[])},fail:function(i){uni.showToast((0,a.default)({icon:"success",position:"bottom",title:"系统错误"},"position","center")),(0,d.removeSignInfo)("signInfo"),t.isSign=!1,t.isAm=!1,t.amSign=[]}}):uni.request({url:d.testURL+"/webApi/clockIn/saveClockIn",method:"POST",data:{mode:n.mode,nowT:n.nowT,address:n.address,time:n.time,longitude:n.longitude,latitude:n.latitude,remarks:p,userId:l,userName:c,groupId:o,classId:r,imgURL:m,is:v,isAm:!1,isPm:!0},header:{"content-type":"application/x-www-form-urlencoded"},success:function(e){i("log","下班卡成功回调: "+JSON.stringify(e.data)," at pages/index/index.vue:881"),0==e.data.code?(uni.showToast((0,a.default)({icon:"success",position:"bottom",title:"打卡成功"},"position","center")),uni.hideLoading(),setTimeout((function(){uni.redirectTo({url:"/pages/index/index?userId="+l+"&userName="+c})}),1e3)):(uni.showToast((0,a.default)({icon:"error",position:"bottom",title:"打卡异常"},"position","center")),t.isSign=!1,t.isPm=!1,t.pmSign=[])},fail:function(i){uni.showToast((0,a.default)({icon:"success",position:"bottom",title:"系统错误"},"position","center")),t.isSign=!1,t.isPm=!1,t.pmSign=[]}})}else uni.showToast({title:"请检查位置信息状态！",icon:"none",mask:!0,duration:3e3,position:"center"})},openLocation:function(){var i=this;uni.chooseLocation({success:function(t){i.address=t.address,i.signInfo.address=t.address;var e=(0,u.countRadius)([i.latitude,i.longitude],[i.circles[0].latitude,i.circles[0].longitude],i.r);i.is=e}})},reLocation:function(){this.getLocation()}}});t.default=c}).call(this,e("0de9")["log"])},"26f7":function(i,t,e){"use strict";var s=e("a209"),n=e.n(s);n.a},"4cb3":function(i,t,e){var s=e("58da");"string"===typeof s&&(s=[[i.i,s,""]]),s.locals&&(i.exports=s.locals);var n=e("4f06").default;n("374e33b8",s,!0,{sourceMap:!1,shadowMode:!1})},"589c":function(i,t,e){(function(t){function s(i){if("number"!==typeof i||i<0)return i;var t=parseInt(i/3600);i%=3600;var e=parseInt(i/60);i%=60;var s=i;return[t,e,s].map((function(i){return i=i.toString(),i[1]?i:"0"+i})).join(":")}function n(i,t){return"string"===typeof i&&"string"===typeof t&&(i=parseFloat(i),t=parseFloat(t)),i=i.toFixed(2),t=t.toFixed(2),{longitude:i.toString().split("."),latitude:t.toString().split(".")}}e("a15b"),e("d81d"),e("fb6a"),e("4e82"),e("a9e3"),e("b680"),e("b64b"),e("d3b7"),e("acd8"),e("e25e"),e("ac1f"),e("25f0"),e("1276");var a={UNITS:{"年":315576e5,"月":26298e5,"天":864e5,"小时":36e5,"分钟":6e4,"秒":1e3},humanize:function(i){var t="";for(var e in this.UNITS)if(i>=this.UNITS[e]){t=Math.floor(i/this.UNITS[e])+e+"前";break}return t||"刚刚"},format:function(i){var t=this.parse(i),e=Date.now()-t.getTime();if(e<this.UNITS["天"])return this.humanize(e);var s=function(i){return i<10?"0"+i:i};return t.getFullYear()+"/"+s(t.getMonth()+1)+"/"+s(t.getDay())+"-"+s(t.getHours())+":"+s(t.getMinutes())},parse:function(i){var t=i.split(/[^0-9]/);return new Date(t[0],t[1]-1,t[2],t[3],t[4],t[5])}};function o(i,t){var e=i.getFullYear(),s=("0"+(i.getMonth()+1)).slice(-2),n=("0"+i.getDate()).slice(-2),a=("0"+i.getHours()).slice(-2),o=("0"+i.getMinutes()).slice(-2),r=("0"+i.getSeconds()).slice(-2);if("Y-M-D h:min:s"===t)var l=e+"-"+s+"-"+n+" "+a+":"+o+":"+r;else if("Y-M-D"===t)l=e+"-"+s+"-"+n;if("h:min:s"===t)l=a+":"+o+":"+r;return l}function r(i){for(var t=[],e={},s=0;s<i.length;s++){var n=Object.keys(i[s]);n.sort((function(i,t){return Number(i)-Number(t)}));for(var a="",o=0;o<n.length;o++)a+=JSON.stringify(n[o]),a+=JSON.stringify(i[s][n[o]]);e.hasOwnProperty(a)||(t.push(i[s]),e[a]=!0)}return t=t,t}function o(i,t){var e=i.getFullYear(),s=("0"+(i.getMonth()+1)).slice(-2),n=("0"+i.getDate()).slice(-2),a=("0"+i.getHours()).slice(-2),o=("0"+i.getMinutes()).slice(-2),r=("0"+i.getSeconds()).slice(-2);if("Y-M-D h:min:s"===t)var l=e+"-"+s+"-"+n+" "+a+":"+o+":"+r;else if("Y-M-D"===t)l=e+"-"+s+"-"+n;if("h:min:s"===t)l=a+":"+o+":"+r;else if("h"===t)l=a;else if("min"===t)l=o;return l}function l(i,t){switch(arguments.length){case 1:return parseInt(Math.random()*i+1,10);case 2:return parseInt(Math.random()*(t-i+1)+i,10);default:return 0}}function u(i,t,e){if(0===e)return!1;var s=t[0]-i[0],n=t[1]-i[1];return s*s+n*n<=e*e}function d(i,e,s){if(0===s)return!1;var n=i[0]*Math.PI/180,a=e[0]*Math.PI/180,o=n-a,r=i[1]*Math.PI/180-e[1]*Math.PI/180,l=2*Math.asin(Math.sqrt(Math.pow(Math.sin(o/2),2)+Math.cos(n)*Math.cos(a)*Math.pow(Math.sin(r/2),2)));l*=6378.137,l=Math.round(1e4*l)/1e4;var u=1e3*l;return t("log","当前坐标位置与打卡圆心距离："+u," at common/util.js:163"),u<=s}function c(i){var t=new Date(i),e=new Date;return t.setHours(0,0,0,0)==e.setHours(0,0,0,0)}i.exports={formatTime:s,formatLocation:n,dateUtils:a,formateDate:o,deteleObject:r,randomNum:l,pointInsideCircle:u,isSameDay:c,countRadius:d}}).call(this,e("0de9")["log"])},"58da":function(i,t,e){var s=e("24fb");t=s(!1),t.push([i.i,".map[data-v-102e6f85]{width:100%;height:260px}.uni-list-cell-left[data-v-102e6f85]{padding:0 %?30?%}.text-desc[data-v-102e6f85]{display:-webkit-box;display:-webkit-flex;display:flex;-webkit-box-pack:justify;-webkit-justify-content:space-between;justify-content:space-between;padding:%?10?% %?20?%}.colorRed[data-v-102e6f85]{color:red}.colorGreen[data-v-102e6f85]{color:#32cd32}.colorYellow[data-v-102e6f85]{color:#ff0}.colorBlue[data-v-102e6f85]{color:#007aff}.bgBlue[data-v-102e6f85]{background-color:#007aff}.bgGreen[data-v-102e6f85]{background-color:#32cd32}.bgAsh[data-v-102e6f85]{background-color:#c8c7cc}.uni-timeline[data-v-102e6f85]{padding:%?30?% %?20?%}.uni-timeline-item-content-t[data-v-102e6f85]{font-weight:700}.desc-pad[data-v-102e6f85]{padding:0 %?0?%}.desc-pad .bz[data-v-102e6f85]{color:#007aff}.desc-pad .bz .icon[data-v-102e6f85]{color:#007aff}.uni-timeline-last-item .uni-timeline-item-divider[data-v-102e6f85]{background:#bbb}.CBlue[data-v-102e6f85]{background-color:#007aff;-webkit-box-shadow:0 5px 5px #007aff;box-shadow:0 5px 5px #007aff}.CGreen[data-v-102e6f85]{background-color:#32cd32;-webkit-box-shadow:0 5px 5px #32cd32;box-shadow:0 5px 5px #32cd32}.CAsh[data-v-102e6f85]{background-color:#c8c7cc;-webkit-box-shadow:0 5px 5px #c8c7cc;box-shadow:0 5px 5px #c8c7cc}.module[data-v-102e6f85]{overflow:hidden;margin:%?20?% auto;width:%?220?%;height:%?220?%;-webkit-border-radius:50%;border-radius:50%;color:#fff;text-align:center}.module .text[data-v-102e6f85]{font-size:15px;margin:%?50?% auto %?10?%}.uni-timeline-item .uni-timeline-item-content[data-v-102e6f85]{width:100%;padding-right:%?20?%}.content-show[data-v-102e6f85]{width:100%}.sign-title[data-v-102e6f85]{display:-webkit-box;display:-webkit-flex;display:flex;-webkit-box-pack:justify;-webkit-justify-content:space-between;justify-content:space-between;padding:%?30?% %?24?%;border-bottom:%?1?% solid #333}.sign-title .portrait[data-v-102e6f85]{width:%?100?%;height:%?100?%;line-height:%?100?%;-webkit-border-radius:50%;border-radius:50%;background-color:#007aff;color:#fff;font-size:%?28?%;text-align:center}.sign-title .sign-title-l[data-v-102e6f85]{display:-webkit-box;display:-webkit-flex;display:flex}.sign-title .sign-title-l .text[data-v-102e6f85]{margin-left:%?20?%}.sign-title .sign-title-l .text .name[data-v-102e6f85]{font-size:%?32?%}.sign-title .sign-title-l .text  .gz[data-v-102e6f85]{color:#00008b;display:-webkit-inline-box;display:-webkit-inline-flex;display:inline-flex}.sign-title .sign-title-l .text .gz uni-text[data-v-102e6f85]{display:inline-block}.sign-title .sign-title-l .text .gz .t1[data-v-102e6f85]{overflow:hidden; /*超出部分隐藏*/text-overflow:ellipsis;/* 超出部分显示省略号 */white-space:nowrap;/*规定段落中的文本不进行换行 */width:%?166?%/*需要配合宽度来使用*/}.iswq[data-v-102e6f85]{padding:0 0 20px 10px;color:#9c3;border:1px solid #9c3;width:40px;height:5px;-webkit-border-radius:4px;border-radius:4px;margin-left:%?20?%;display:inline-block}.isNormal[data-v-102e6f85]{padding:0 0 20px 10px;color:#f50;border:1px solid #f50;width:40px;height:5px;-webkit-border-radius:4px;border-radius:4px;margin-left:%?20?%;display:inline-block}.desc-pad .last[data-v-102e6f85]{margin-bottom:%?80?%}.addClockIn[data-v-102e6f85]{color:#00f}.uni-popup .content .text[data-v-102e6f85]{color:#666}",""]),i.exports=t},"5ca3":function(i,t,e){"use strict";e.r(t);var s=e("794f"),n=e.n(s);for(var a in s)"default"!==a&&function(i){e.d(t,i,(function(){return s[i]}))}(a);t["default"]=n.a},"6c8a":function(i,t,e){(function(t){var s;function n(){var i=!1,e=plus.ios.import("UIApplication"),s=e.sharedApplication(),n=0;if(s.currentUserNotificationSettings){var a=s.currentUserNotificationSettings();n=a.plusGetAttribute("types"),t("log","enabledTypes1:"+n," at js_sdk/wa-permission/permission.js:19"),0==n?t("log","推送权限没有开启"," at js_sdk/wa-permission/permission.js:21"):(i=!0,t("log","已经开启推送功能!"," at js_sdk/wa-permission/permission.js:24")),plus.ios.deleteObject(a)}else n=s.enabledRemoteNotificationTypes(),0==n?t("log","推送权限没有开启!"," at js_sdk/wa-permission/permission.js:30"):(i=!0,t("log","已经开启推送功能!"," at js_sdk/wa-permission/permission.js:33")),t("log","enabledTypes2:"+n," at js_sdk/wa-permission/permission.js:35");return plus.ios.deleteObject(s),plus.ios.deleteObject(e),i}function a(){var i=!1,e=plus.ios.import("CLLocationManager"),s=e.authorizationStatus();return i=2!=s,t("log","定位权限开启："+i," at js_sdk/wa-permission/permission.js:48"),plus.ios.deleteObject(e),i}function o(){var i=!1,e=plus.ios.import("AVAudioSession"),s=e.sharedInstance(),n=s.recordPermission();return t("log","permissionStatus:"+n," at js_sdk/wa-permission/permission.js:70"),1684369017==n||1970168948==n?t("log","麦克风权限没有开启"," at js_sdk/wa-permission/permission.js:72"):(i=!0,t("log","麦克风权限已经开启"," at js_sdk/wa-permission/permission.js:75")),plus.ios.deleteObject(e),i}function r(){var i=!1,e=plus.ios.import("AVCaptureDevice"),s=e.authorizationStatusForMediaType("vide");return t("log","authStatus:"+s," at js_sdk/wa-permission/permission.js:86"),3==s?(i=!0,t("log","相机权限已经开启"," at js_sdk/wa-permission/permission.js:89")):t("log","相机权限没有开启"," at js_sdk/wa-permission/permission.js:91"),plus.ios.deleteObject(e),i}function l(){var i=!1,e=plus.ios.import("PHPhotoLibrary"),s=e.authorizationStatus();return t("log","authStatus:"+s," at js_sdk/wa-permission/permission.js:102"),3==s?(i=!0,t("log","相册权限已经开启"," at js_sdk/wa-permission/permission.js:105")):t("log","相册权限没有开启"," at js_sdk/wa-permission/permission.js:107"),plus.ios.deleteObject(e),i}function u(){var i=!1,e=plus.ios.import("CNContactStore"),s=e.authorizationStatusForEntityType(0);return 3==s?(i=!0,t("log","通讯录权限已经开启"," at js_sdk/wa-permission/permission.js:120")):t("log","通讯录权限没有开启"," at js_sdk/wa-permission/permission.js:122"),plus.ios.deleteObject(e),i}function d(){var i=!1,e=plus.ios.import("EKEventStore"),s=e.authorizationStatusForEntityType(0);return 3==s?(i=!0,t("log","日历权限已经开启"," at js_sdk/wa-permission/permission.js:135")):t("log","日历权限没有开启"," at js_sdk/wa-permission/permission.js:137"),plus.ios.deleteObject(e),i}function c(){var i=!1,e=plus.ios.import("EKEventStore"),s=e.authorizationStatusForEntityType(1);return 3==s?(i=!0,t("log","备忘录权限已经开启"," at js_sdk/wa-permission/permission.js:150")):t("log","备忘录权限没有开启"," at js_sdk/wa-permission/permission.js:152"),plus.ios.deleteObject(e),i}function m(i){return new Promise((function(e,s){plus.android.requestPermissions([i],(function(i){for(var s=0,n=0;n<i.granted.length;n++){var a=i.granted[n];t("log","已获取的权限："+a," at js_sdk/wa-permission/permission.js:167"),s=1}for(n=0;n<i.deniedPresent.length;n++){var o=i.deniedPresent[n];t("log","拒绝本次申请的权限："+o," at js_sdk/wa-permission/permission.js:172"),s=0}for(n=0;n<i.deniedAlways.length;n++){var r=i.deniedAlways[n];t("log","永久拒绝申请的权限："+r," at js_sdk/wa-permission/permission.js:177"),s=-1}e(s)}),(function(i){t("log","申请权限错误："+i.code+" = "+i.message," at js_sdk/wa-permission/permission.js:187"),e({code:i.code,message:i.message})}))}))}function p(i){return"location"==i?a():"camera"==i?r():"photoLibrary"==i?l():"record"==i?o():"push"==i?n():"contact"==i?u():"calendar"==i?d():"memo"==i&&c()}function v(){if(s){var i=plus.ios.import("UIApplication"),t=i.sharedApplication(),e=plus.ios.import("NSURL"),n=e.URLWithString("app-settings:");t.openURL(n),plus.ios.deleteObject(n),plus.ios.deleteObject(e),plus.ios.deleteObject(t)}else{var a=plus.android.importClass("android.content.Intent"),o=plus.android.importClass("android.provider.Settings"),r=plus.android.importClass("android.net.Uri"),l=plus.android.runtimeMainActivity(),u=new a;u.setAction(o.ACTION_APPLICATION_DETAILS_SETTINGS);var d=r.fromParts("package",l.getPackageName(),null);u.setData(d),l.startActivity(u)}}function g(){if(s){var i=!1,e=plus.ios.import("CLLocationManager");i=e.locationServicesEnabled();return t("log","系统定位开启:"+i," at js_sdk/wa-permission/permission.js:253"),plus.ios.deleteObject(e),i}var n=plus.android.importClass("android.content.Context"),a=plus.android.importClass("android.location.LocationManager"),o=plus.android.runtimeMainActivity(),r=o.getSystemService(n.LOCATION_SERVICE);i=r.isProviderEnabled(a.GPS_PROVIDER);return t("log","系统定位开启:"+i," at js_sdk/wa-permission/permission.js:262"),i}e("d3b7"),i.exports={judgeIosPermission:p,requestAndroidPermission:m,checkSystemEnableLocation:g,gotoAppPermissionSetting:v}}).call(this,e("0de9")["log"])},7517:function(i,t,e){"use strict";e.d(t,"b",(function(){return n})),e.d(t,"c",(function(){return a})),e.d(t,"a",(function(){return s}));var s={uniIcon:e("d812").default,uniPopup:e("9c41").default},n=function(){var i=this,t=i.$createElement,e=i._self._c||t;return e("v-uni-view",{staticClass:"content"},[e("v-uni-view",{staticClass:"page-body"},[e("v-uni-view",{staticClass:"sign-title"},[e("v-uni-view",{staticClass:"sign-title-l"},[e("v-uni-view",{staticClass:"portrait"},[i._v(i._s(i.name))])],1),e("v-uni-view",{staticClass:"sign-title-r"},[e("v-uni-view",{staticClass:"date"},[e("v-uni-view",{staticClass:"uni-input"},[i._v(i._s(i.date)),e("uni-icon",{attrs:{type:"arrowdown"}})],1)],1)],1)],1),e("v-uni-text",{on:{click:function(t){arguments[0]=t=i.$handleEvent(t),i.reset.apply(void 0,arguments)}}},[i._v("重置")]),e("v-uni-view",{staticStyle:{"text-align":"center",padding:"20upx 0"}},[i._v("当前位置:"),e("v-uni-text",[i._v(i._s(i.address))])],1),e("v-uni-view",{staticClass:"uni-timeline"},[e("v-uni-view",{staticClass:"uni-timeline-item uni-timeline-first-item"},[e("v-uni-view",{staticClass:"uni-timeline-item-divider",style:{background:i.isAm?"#bbb":"#1AAD19"}}),e("v-uni-view",{staticClass:"uni-timeline-item-content"},[e("v-uni-view",[e("v-uni-view",{staticClass:"uni-timeline-item-content-t1"},[i._v("上班时间"+i._s(i.Timer[0].time))]),i.isAm?e("v-uni-view",{staticClass:"desc-pad"},[void 0===i.amSign.time?e("v-uni-view",[e("v-uni-view",{staticClass:"time uni-timeline-item-content-t"},[i._v("缺卡")])],1):e("v-uni-view",[e("v-uni-view",{staticClass:"time uni-timeline-item-content-t"},[i._v("打卡时间 "+i._s(i.amSign.time.substring(10,16))),"外勤打卡"==i.amSign.mode?e("v-uni-view",{staticClass:"iswq"},[i._v("外勤")]):i._e(),"Late"==i.amSign.timeResult?e("v-uni-view",{staticClass:"isNormal"},[i._v("迟到")]):i._e(),"Absenteeism"==i.amSign.timeResult?e("v-uni-view",{staticClass:"isNormal"},[i._v("旷工")]):i._e()],1),e("v-uni-view",[e("uni-icon",{attrs:{type:"location-filled"}}),i._v(i._s(i.amSign.address))],1),e("v-uni-view",{staticClass:"bz last",on:{click:function(t){arguments[0]=t=i.$handleEvent(t),i.showBz("amSign")}}},[i._v("备注"),e("uni-icon",{style:{color:"rgb(0, 122, 255)"},attrs:{type:"forward"}})],1)],1)],1):e("v-uni-view",{staticClass:"content-show"},[!0===i.isRest?e("v-uni-view",[e("v-uni-view",{staticClass:"module CAsh"},[e("v-uni-view",{staticClass:"text"},[i._v("无法打卡")]),e("v-uni-view",{staticClass:"time"},[i._v(i._s(i.time))])],1),i.isRest?e("v-uni-view",{staticClass:"colorBlack",staticStyle:{"text-align":"center"}},[i._v("不属于考勤打卡时间")]):i._e()],1):e("v-uni-view",[!0===i.is?e("v-uni-view",[e("v-uni-view",{staticClass:"module CBlue",on:{click:function(t){arguments[0]=t=i.$handleEvent(t),i.clickSign.apply(void 0,arguments)}}},[e("v-uni-view",{staticClass:"text"},[i._v("上班打卡")]),e("v-uni-view",{staticClass:"time"},[i._v(i._s(i.time))])],1),i.is?e("v-uni-view",{staticClass:"colorGreen",staticStyle:{"text-align":"center"}},[i._v("已在考勤范围内"),e("v-uni-text",{staticClass:"addClockIn",on:{click:function(t){arguments[0]=t=i.$handleEvent(t),i.addClockIn.apply(void 0,arguments)}}},[i._v("添加打卡信息")])],1):i._e()],1):!1===i.is?e("v-uni-view",[e("v-uni-view",{staticClass:"module CGreen",on:{click:function(t){arguments[0]=t=i.$handleEvent(t),i.clickSign.apply(void 0,arguments)}}},[e("v-uni-view",{staticClass:"text"},[i._v("外勤打卡")]),e("v-uni-view",{staticClass:"time"},[i._v(i._s(i.time))])],1),i.is?i._e():e("v-uni-view",{staticClass:"colorRed",staticStyle:{"text-align":"center"}},[i._v("不在考勤范围内"),e("v-uni-text",{staticClass:"addClockIn",on:{click:function(t){arguments[0]=t=i.$handleEvent(t),i.addClockIn.apply(void 0,arguments)}}},[i._v("添加打卡信息")])],1)],1):null===i.is?e("v-uni-view",[e("v-uni-view",{staticClass:"module CAsh"},[e("v-uni-view",{staticClass:"text"},[i._v("请检查位置信息")]),e("v-uni-view",{staticClass:"time"},[i._v(i._s(i.time))])],1)],1):i._e()],1)],1)],1)],1)],1),e("v-uni-view",{staticClass:"uni-timeline-item uni-timeline-last-item"},[e("v-uni-view",{staticClass:"uni-timeline-item-divider",style:{background:i.isAm?"#1AAD19":"#bbb"}}),e("v-uni-view",{staticClass:"uni-timeline-item-content"},[e("v-uni-view",[e("v-uni-view",{staticClass:"uni-timeline-item-content-t1"},[i._v("下班时间"+i._s(i.Timer[1].time))]),i.isAm?e("v-uni-view",{staticClass:"desc-pad"},[i.isPm?e("v-uni-view",{staticClass:"desc-pad"},[void 0===i.pmSign.time?e("v-uni-view",[e("v-uni-view",{staticClass:"time uni-timeline-item-content-t"},[i._v("缺卡")])],1):e("v-uni-view",[e("v-uni-view",{staticClass:"time uni-timeline-item-content-t"},[i._v("打卡时间:"+i._s(i.pmSign.time.substring(10,16))),"外勤打卡"==i.pmSign.mode?e("v-uni-view",{staticClass:"iswq"},[i._v("外勤")]):i._e(),"Early"==i.pmSign.timeResult?e("v-uni-view",{staticClass:"isNormal"},[i._v("早退")]):i._e(),"Absenteeism"==i.pmSign.timeResult?e("v-uni-view",{staticClass:"isNormal"},[i._v("旷工")]):i._e()],1),e("v-uni-view",[e("uni-icon",{attrs:{type:"location-filled"}}),i._v(i._s(i.pmSign.address))],1),e("v-uni-view",{staticClass:"bz last",on:{click:function(t){arguments[0]=t=i.$handleEvent(t),i.showBz("pmSign")}}},[i._v("备注"),e("uni-icon",{style:{color:"rgb(0, 122, 255)"},attrs:{type:"forward"}})],1)],1)],1):e("v-uni-view",{staticClass:"content-show"},[!0===i.isRest?e("v-uni-view",[e("v-uni-view",{staticClass:"module CGreen"},[e("v-uni-view",{staticClass:"text"},[i._v("无法打卡")]),e("v-uni-view",{staticClass:"time"},[i._v(i._s(i.time))])],1),i.isRest?e("v-uni-view",{staticClass:"colorBlack",staticStyle:{"text-align":"center"}},[i._v("不属于考勤打卡时间")]):i._e()],1):e("v-uni-view",[!0===i.is?e("v-uni-view",[e("v-uni-view",{staticClass:"module CBlue",on:{click:function(t){arguments[0]=t=i.$handleEvent(t),i.clickSign.apply(void 0,arguments)}}},[e("v-uni-view",{staticClass:"text"},[i._v("下班打卡")]),e("v-uni-view",{staticClass:"time"},[i._v(i._s(i.time))])],1),i.is?e("v-uni-view",{staticClass:"colorGreen",staticStyle:{"text-align":"center"}},[i._v("已在考勤范围内"),e("v-uni-text",{staticClass:"addClockIn",on:{click:function(t){arguments[0]=t=i.$handleEvent(t),i.addClockIn.apply(void 0,arguments)}}},[i._v("添加打卡信息")])],1):i._e()],1):!1===i.is?e("v-uni-view",[e("v-uni-view",{staticClass:"module CGreen",on:{click:function(t){arguments[0]=t=i.$handleEvent(t),i.clickSign.apply(void 0,arguments)}}},[e("v-uni-view",{staticClass:"text"},[i._v("外勤打卡")]),e("v-uni-view",{staticClass:"time"},[i._v(i._s(i.time))])],1),i.is?i._e():e("v-uni-view",{staticClass:"colorRed",staticStyle:{"text-align":"center"}},[i._v("不在考勤范围内"),e("v-uni-text",{staticClass:"addClockIn",on:{click:function(t){arguments[0]=t=i.$handleEvent(t),i.addClockIn.apply(void 0,arguments)}}},[i._v("添加打卡信息")])],1)],1):null===i.is?e("v-uni-view",[e("v-uni-view",{staticClass:"module CAsh"},[e("v-uni-view",{staticClass:"text"},[i._v("请检查位置信息")]),e("v-uni-view",{staticClass:"time"},[i._v(i._s(i.time))])],1)],1):i._e()],1)],1)],1):i._e()],1)],1)],1)],1),e("v-uni-text",{on:{click:function(t){arguments[0]=t=i.$handleEvent(t),i.reLocation.apply(void 0,arguments)}}},[i._v("重新定位")])],1),e("uni-popup",{attrs:{show:"middle-list"===i.type,position:"middle",mode:"fixed"},on:{hidePopup:function(t){arguments[0]=t=i.$handleEvent(t),i.togglePopup("")}}},[e("v-uni-view",{staticClass:"title",staticStyle:{padding:"20upx 0","font-weight":"bold",width:"100%","text-align":"center","border-bottom":"1px solid #666"}},[i._v("打卡备注")]),e("v-uni-view",{staticClass:"content",staticStyle:{padding:"20upx 10upx",width:"100%"}},[e("v-uni-text",{staticClass:"text"},[i._v("打卡时间:")]),e("v-uni-text",[i._v(i._s(i.bzText.time))]),e("v-uni-view",{staticClass:"text"},[i._v("打卡地点:")]),e("v-uni-view",[i._v(i._s(i.bzText.address))])],1),e("v-uni-view",{staticClass:"content",staticStyle:{padding:"20upx 10upx",width:"100%"}},[e("v-uni-view",[i._v(i._s(i.bzText.remarks))])],1),e("v-uni-view",{staticClass:"bottom",staticStyle:{padding:"20upx 10upx",color:"cadetblue",width:"100%","text-align":"center","border-top":"1px solid #666"},on:{click:function(t){arguments[0]=t=i.$handleEvent(t),i.togglePopup("")}}},[i._v("关闭")])],1)],1)},a=[]},"78b3":function(i,t,e){"use strict";e.r(t);var s=e("7517"),n=e("d544");for(var a in n)"default"!==a&&function(i){e.d(t,i,(function(){return n[i]}))}(a);e("de59");var o,r=e("f0c5"),l=Object(r["a"])(n["default"],s["b"],s["c"],!1,null,"102e6f85",null,!1,s["a"],o);t["default"]=l.exports},"794f":function(i,t,e){"use strict";Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var s={name:"uni-popup",props:{show:{type:Boolean,default:!1},position:{type:String,default:"middle"},mode:{type:String,default:"insert"},msg:{type:String,default:""},h5Top:{type:Boolean,default:!1},buttonMode:{type:String,default:"bottom"}},data:function(){return{offsetTop:0}},watch:{h5Top:function(i){this.offsetTop=i?44:0}},methods:{hide:function(){"insert"===this.mode&&"middle"===this.position||this.$emit("hidePopup")},closeMask:function(){"insert"===this.mode&&this.$emit("hidePopup")},moveHandle:function(){}},created:function(){var i=0;i=this.h5Top?0:44,this.offsetTop=i}};t.default=s},"9c41":function(i,t,e){"use strict";e.r(t);var s=e("e0f6"),n=e("5ca3");for(var a in n)"default"!==a&&function(i){e.d(t,i,(function(){return n[i]}))}(a);e("26f7");var o,r=e("f0c5"),l=Object(r["a"])(n["default"],s["b"],s["c"],!1,null,"7722493f",null,!1,s["a"],o);t["default"]=l.exports},a209:function(i,t,e){var s=e("de50");"string"===typeof s&&(s=[[i.i,s,""]]),s.locals&&(i.exports=s.locals);var n=e("4f06").default;n("721b64b4",s,!0,{sourceMap:!1,shadowMode:!1})},d544:function(i,t,e){"use strict";e.r(t);var s=e("2687"),n=e.n(s);for(var a in s)"default"!==a&&function(i){e.d(t,i,(function(){return s[i]}))}(a);t["default"]=n.a},de50:function(i,t,e){var s=e("24fb");t=s(!1),t.push([i.i,'.uni-mask[data-v-7722493f]{height:100vh;position:fixed;z-index:998;top:0;right:0;bottom:0;left:0;background-color:rgba(0,0,0,.3)}.uni-popup[data-v-7722493f]{position:absolute;z-index:999;background-color:#fff}.uni-popup-middle[data-v-7722493f]{display:-webkit-box;display:-webkit-flex;display:flex;-webkit-box-orient:vertical;-webkit-box-direction:normal;-webkit-flex-direction:column;flex-direction:column;-webkit-box-align:center;-webkit-align-items:center;align-items:center;-webkit-box-pack:center;-webkit-justify-content:center;justify-content:center;top:50%;left:50%;-webkit-transform:translate(-50%,-50%);transform:translate(-50%,-50%)}.uni-popup-middle.uni-popup-insert[data-v-7722493f]{min-width:%?380?%;min-height:%?380?%;max-width:100%;max-height:80%;-webkit-transform:translate(-50%,-65%);transform:translate(-50%,-65%);background:none;-webkit-box-shadow:none;box-shadow:none}.uni-popup-middle.uni-popup-fixed[data-v-7722493f]{width:68%;padding:0 %?20?%;-webkit-border-radius:6px;border-radius:6px}.uni-close-bottom[data-v-7722493f],\n.uni-close-right[data-v-7722493f]{position:absolute;bottom:%?-180?%;text-align:center;-webkit-border-radius:50%;border-radius:50%;color:#f5f5f5;font-size:%?60?%;font-weight:700;opacity:.8;z-index:-1}.uni-close-right[data-v-7722493f]{right:%?-60?%;top:%?-80?%}.uni-close-bottom[data-v-7722493f]:after{content:"";position:absolute;width:0;border:1px #f5f5f5 solid;top:%?-200?%;bottom:%?56?%;left:50%;-webkit-transform:translate(-50%);transform:translate(-50%);opacity:.8}.uni-popup-top[data-v-7722493f]{top:0;left:0;width:100%;height:%?100?%;line-height:%?100?%;text-align:center}.uni-popup-bottom[data-v-7722493f]{left:0;bottom:0;width:100%;min-height:%?100?%;line-height:%?100?%;text-align:center}',""]),i.exports=t},de59:function(i,t,e){"use strict";var s=e("4cb3"),n=e.n(s);n.a},e0f6:function(i,t,e){"use strict";var s;e.d(t,"b",(function(){return n})),e.d(t,"c",(function(){return a})),e.d(t,"a",(function(){return s}));var n=function(){var i=this,t=i.$createElement,e=i._self._c||t;return e("v-uni-view",[e("v-uni-view",{directives:[{name:"show",rawName:"v-show",value:i.show,expression:"show"}],staticClass:"uni-mask",style:{top:i.offsetTop+"px"},on:{click:function(t){arguments[0]=t=i.$handleEvent(t),i.hide.apply(void 0,arguments)},touchmove:function(t){t.stopPropagation(),t.preventDefault(),arguments[0]=t=i.$handleEvent(t),i.moveHandle.apply(void 0,arguments)}}}),e("v-uni-view",{directives:[{name:"show",rawName:"v-show",value:i.show,expression:"show"}],staticClass:"uni-popup",class:"uni-popup-"+i.position+" uni-popup-"+i.mode},[i._v(i._s(i.msg)),i._t("default"),"middle"===i.position&&"insert"===i.mode?e("v-uni-view",{staticClass:" uni-icon uni-icon-close",class:{"uni-close-bottom":"bottom"===i.buttonMode,"uni-close-right":"right"===i.buttonMode},on:{click:function(t){arguments[0]=t=i.$handleEvent(t),i.closeMask.apply(void 0,arguments)}}}):i._e()],2)],1)},a=[]}}]);