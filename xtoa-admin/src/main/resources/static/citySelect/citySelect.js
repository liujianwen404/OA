/**
 * Created by mzf on 2018/8/7.
 */
$(function(){
    var divCity = $(".searchCitySelect");

    if (divCity.length > 0){
        var cityNameValue = "请选择";
        var cityValue = $(".cityValue");
        if (cityValue.length > 0){
            if (cityValue.val()) {
                cityNameValue = cityValue.val();
            }
        }


        divCity.html('<div class="citySelect">\n' +
            '                            <span style="background-color: beige;' +
            'border: 1px solid #ddd;border-radius: 4px;background: transparent;outline: none;" class="cityName">'+ cityNameValue +'</span>\n' +
            '                            <i class="iconDown"></i>\n' +
            '                            <i class="line"></i>\n' +
            '                        </div>\n' +
            '\n' +
            '                        <div class="dropDown" style="position: absolute;\n' +
            '    z-index: 99;\n' +
            '    background: #fff;width: 100%;">\n' +
            '                            <div class="dropProv">\n' +
            '                                <ul class="dropProvUl dropUl"></ul>\n' +
            '                            </div>\n' +
            '                            <div class="dropCity">\n' +
            '                                <ul class="dropCityUl dropUl"></ul>\n' +
            '                            </div>\n' +
            '                        </div>')


        addArray();
        //console.log(arr2);
        //加载省级列表
        for(let i=0;i<arr.length;i++) {
            $('.dropProvUl').append("<li class='dropProvLi'>" + arr[i] + "</li>");
        }
        //点击选择城市时，先隐藏省级市级选择块
        $('.citySelect').on('click',function(){
            //$('.dropDown').toggle();
            $('.dropCity').css('display',"none");
            $('.dropProv').toggle();
            //点击省份时，自动选择省会城市
            $('.dropProvLi').on('click',function(){
                $('.cityName').text( arr2[$(this).index()][0]);
                $('.cityValue').val(arr2[$(this).index()][0]);
                $('.dropDown div').css("display","none");
            });
            //给省级列表添加mouseover事件
            $('.dropProvLi').on('mouseover',function(){
                $('.dropCity').css("display","inline-block");
                $('.dropProvLi').css("background-color","white");
                $('.dropCityUl').empty();
                $(this).css("background-color","#f1f3f6");
                //加载城市列表
                for(let j=0;j<arr2[$(this).index()].length;j++){
                    $('.dropCityUl').append("<li class='dropCityLi'>"+arr2[$(this).index()][j]+"</li>");

                }
                //选择城市
                $('.dropCityLi').on("click", function () {
                    //console.log($(this).text());
                    $('.cityName').text($(this).text());
                    $('.cityValue').val($(this).text());
                    $('.dropDown div').css("display","none");
                });
                //选择城市
                /*$('.dropProvUl').on("click", function () {
                    //console.log($(this).text());
                    $('.cityName').text($(this).text());
                    $('.cityValue').val($(this).text());
                    $('.dropDown div').css("display","none");
                });*/
            });
        });
        // console.log(arr[17]);


    }

});
//把市级添加到arr2中对应的省级
function addArray(){
     arr=["不限","北京市","天津市","上海市","重庆市","河北省","山西省","内蒙古","辽宁省","吉林省","黑龙江","江苏省","浙江省","安徽省","福建省","江西省","山东省","河南省","湖北省",
         "湖南省","广东省","广西省","海南省","四川省","贵州省","云南省","西藏省","陕西省","甘肃省","青海省","宁夏省","新疆省","香港","澳门","台湾"];
     arr2=["不限","北京市","天津市","上海市","重庆市","河北省","山西省","内蒙古","辽宁省","吉林省","黑龙江","江苏省","浙江省","安徽省","福建省","江西省","山东省","河南省","湖北省",
         "湖南省","广东省","广西省","海南省","四川省","贵州省","云南省","西藏省","陕西省","甘肃省","青海省","宁夏省","新疆省","香港","澳门","台湾"];
    function addTo(id,iArray){
        arr2[id] = [];
        for(let i=0;i<iArray.length;i++){
            arr2[id][i]=iArray[i];
        }

    }
    addTo("0",["不限"]);
   addTo("1",["北京市"]);
   addTo("2",["天津市"]);
   addTo("3",["上海市"]);
   addTo("4",["重庆市"]);
   addTo("5",["石家庄市","张家口市","承德市","秦皇岛市","唐山市","廊坊市","保定市","衡水市","沧州市","邢台市","邯郸市"]);
   addTo("6",["太原市","大同市","阳泉市","长治市","晋城市","朔州市","晋中市","运城市","忻州市","临汾市","吕梁市"]);
   addTo("7",["呼和浩特市","包头市","乌海市","赤峰市","通辽市","鄂尔多斯市","呼伦贝尔市","兴安盟","锡林郭勒盟","乌兰察布市","巴彦淖尔市","阿拉善盟"]);
   addTo("8",["沈阳市","大连市","鞍山市","抚顺市","本溪市","丹东市","锦州市","营口市","阜新市","辽阳市","盘锦市","铁岭市","朝阳市","葫芦岛市"]);
   addTo("9",["长春市","吉林市","四平市","辽源市","通化市","白山市","松原市","白城市","延边朝鲜族自治州"]);
   addTo("10",["哈尔滨市","齐齐哈尔市","鸡西市","鹤岗市","双鸭山市","大庆市","伊春市","佳木斯市","七台河市","牡丹江市","黑河市","绥化市","大兴安岭地区"]);
   addTo("11",["南京市","无锡市","徐州市","常州市","苏州市","南通市","连云港市","淮安市","盐城市","扬州市","镇江市","泰州市","宿迁市"]);
   addTo("12",["杭州市","宁波市","温州市","嘉兴市","湖州市","绍兴市","金华市","义乌市","衢州市","舟山市","台州市","丽水市"]);
   addTo("13",["合肥市","芜湖市","蚌埠市","淮南市","马鞍山市","淮北市","铜陵市","安庆市","黄山市","滁州市","阜阳市","宿州市","六安市","亳州市","池州市","宣城市"]);
   addTo("14",["福州市","厦门市","莆田市","三明市","泉州市","漳州市","南平市","龙岩市","宁德市"]);
   addTo("15",["南昌市","景德镇市","萍乡市","九江市","新余市","鹰潭市","赣州市","吉安市","宜春市","抚州市","上饶市"]);
   addTo("16",["济南市","青岛市","淄博市","枣庄市","东营市","烟台市","潍坊市","济宁市","泰安市","威海市","日照市","莱芜市","临沂市","德州市","聊城市","滨州市","菏泽市"]);
   addTo("17",["郑州市","开封市","洛阳市","平顶山市","安阳市","鹤壁市","新乡市","焦作市","濮阳市","许昌市","漯河市","三门峡市","南阳市","商丘市","信阳市","周口市","驻马店市","济源市"]);
   addTo("18",["武汉市","黄石市","十堰市","宜昌市","襄阳市","鄂州市","荆门市","孝感市","荆州市","黄冈市","咸宁市","随州市","恩施土家族苗族自治州","恩施市","仙桃市","潜江市","天门市","神农架林区"]);
   addTo("19",["长沙市","株洲市","湘潭市","衡阳市","邵阳市","岳阳市","常德市","张家界市","益阳市","郴州市","永州市","怀化市","娄底市","湘西土家族苗族自治州"]);
   addTo("20",["广州市","韶关市","深圳市","珠海市","汕头市","佛山市","江门市","湛江市","茂名市","肇庆市","惠州市","梅州市","汕尾市","河源市","阳江市","清远市","东莞市","中山市","潮州市","揭阳市","云浮市"]);
   addTo("21",["南宁市","柳州市","桂林市","梧州市","北海市","防城港市","钦州市","贵港市","玉林市","百色市","贺州市","河池市","来宾市","崇左市"]);
   addTo("22",["海口市","三亚市","三沙市","五指山市","琼海市","儋州市","文昌市","万宁市","东方市","定安县","屯昌县","澄迈县","临高县","白沙黎族自治县","昌江黎族自治县","乐东黎族自治县","陵水黎族自治县","保亭黎族苗族自治县","琼中黎族苗族自治县"]);
   addTo("23",["成都市","自贡市","攀枝花市","泸州市","德阳市","绵阳市","广元市","遂宁市","内江市","乐山市","南充市","眉山市","宜宾市","广安市","达州市","雅安市","巴中市","资阳市","阿坝藏族羌族自治州","甘孜藏族自治州","凉山彝族自治州"]);
   addTo("24",["贵阳市","六盘水市","遵义市","安顺市","毕节市","铜仁市","黔西南布依族苗族自治州","黔东南苗族侗族自治州","黔南布依族苗族自治州"]);
   addTo("25",["昆明市","曲靖市","玉溪市","保山市","昭通市","丽江市","普洱市","临沧市","楚雄彝族自治州","红河哈尼族彝族自治州","文山壮族苗族自治州","西双版纳傣族自治州","大理白族自治州","德宏傣族景颇族自治州","怒江傈僳族自治州","迪庆藏族自治州"]);
   addTo("26",["拉萨市","日喀则市","昌都市","林芝市","山南市","那曲地区","阿里地区"]);
   addTo("27",["西安市","铜川市","宝鸡市","咸阳市","渭南市","延安市","汉中市","榆林市","安康市","商洛市"]);
   addTo("28",["兰州市","嘉峪关市","金昌市","白银市","天水市","武威市","张掖市","平凉市","酒泉市","庆阳市","定西市","陇南市","临夏回族自治州","甘南藏族自治州"]);
   addTo("29",["西宁市","海东市","海北藏族自治州","黄南藏族自治州","海南藏族自治州","果洛藏族自治州","玉树藏族自治州","海西蒙古族藏族自治州"]);
   addTo("30",["银川市","石嘴山市","吴忠市","固原市","中卫市"]);
   addTo("31",["乌鲁木齐市","克拉玛依市","吐鲁番市","哈密市","昌吉回族自治州","博尔塔拉蒙古自治州","巴音郭楞蒙古自治州","阿克苏地区","克孜勒苏柯尔克孜自治州","喀什地区","和田地区","伊犁哈萨克自治州","塔城地区","阿勒泰地区","石河子市","阿拉尔市","图木舒克市","五家渠市","铁门关市"]);
   addTo("32",["香港特别行政区"]);
   addTo("33",["澳门特别行政区"]);
   addTo("34",["台北","高雄","台中","花莲","基隆","嘉义","金门","连江","苗栗","南投","澎湖","屏东","台东","台南","桃园","新竹","宜兰","云林","彰化"]);
   // console.log(arr);
};

