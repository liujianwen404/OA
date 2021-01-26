package com.ruoyi.hr.domain;

public enum CompanyDeptEnum {

    HQ("集团总部",1),
    SHENZHEN("深圳快马",2),
    DONGGUAN("东莞快马",3),
    ZHONGZHU("中珠快马",4),
    GUANGFO("广佛快马",5),
    WUHAN("武汉分公司",6),
    HK("香港公司",7),

    //各城市一级部门（不包括集团总部）
    YUNYING("运营部",101),
    KEFU("客服部",102),
    XIAOSHOU("销售部",103),
    CAIGOU("采购部",104),
    HR("人力行政部",105),
    CAIWU("财务部",106),
    CANGCHU("仓储配送",107);

    private int num;
    private String name;

    private CompanyDeptEnum(String name, int num){
        this.num = num;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
