package cn.com.yaohao.visa.service;

import cn.com.yaohao.visa.constant.ResultCode;
import cn.com.yaohao.visa.exception.PassportException;
import cn.com.yaohao.visa.model.TBOrderModel;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class TBOrderService {
    private List<TBOrderModel> addTbOeder(){
        List<TBOrderModel> models=new ArrayList<>();
        models.add(new TBOrderModel("TB2020031900001","13758964235","3","佛山市南海区沙溪工业园a号","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",35,"5479862156"));
        models.add(new TBOrderModel("TB2020031900002","18796325489","4","广州市花都区梯面镇404县道","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",7,"5479862157"));
        models.add(new TBOrderModel("TB2020031900003","15478963256","5","广东省江门市开平市","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",12,"5479862158"));
        models.add(new TBOrderModel("TB2020031900004","16887875688","6","江门市恩平市美华东路52号","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",13,"5479862159"));
        models.add(new TBOrderModel("TB2020031900005","15868765767","7","上海市浦东新区陆家嘴环路717号","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",23,"5479862151"));
        models.add(new TBOrderModel("TB2020031900006","13358369648","12","上海市黄浦区雁荡路105号","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",12,"5479862152"));
        models.add(new TBOrderModel("TB2020031900007","14565387667","13","上海市黄浦区龙华东路800号","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",11,"5475872156"));
        models.add(new TBOrderModel("TB2020031900008","18768536543","14","上海市徐汇区沪闵路9001号","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",8,"54798639872"));
        models.add(new TBOrderModel("TB2020031900009","15826646723","15","上海市浦东新区锦绣路1001号","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",9,"5479869875"));
        models.add(new TBOrderModel("TB2020031900010","13974434337","18","成都市成华区万科路4号","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",17,"1258963547"));
        models.add(new TBOrderModel("TB2020031900011","15622586887","20","天府新区华阳街道南湖北路与南湖东路交叉路口东北角","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",3,"5698423159"));
        models.add(new TBOrderModel("TB2020031900012","14768632156","25","成都市锦江区三圣乡幸福路与锦江大道交叉口","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",4,"563214587"));
        models.add(new TBOrderModel("TB2020031900013","19876434353","3","成都市双流区川大路二段2号","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",5,"6523158739"));
        models.add(new TBOrderModel("TB2020031900014","13872124525","4","武侯区永康大道19号","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",6,"9632584128"));
        models.add(new TBOrderModel("TB2020031900015","16876437862","9","四川省成都市武侯区望江路30号","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",9,"8756321439"));
        models.add(new TBOrderModel("TB2020031900016","17897865343","7","成都市金牛区茶店子路485号","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",12,"9632584237"));
        models.add(new TBOrderModel("TB2020031900017","13642543763","4","杭州市西湖区余杭塘路866号","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",6,"5624785297"));
        models.add(new TBOrderModel("TB2020031900018","13782543986","15","浙江省杭州市西湖区天目山路518号","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",5,"5963258756"));
        models.add(new TBOrderModel("TB2020031900019","13687853653","4","绍兴市越城区二环南路与南镇路交叉口东南角","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",7,"51236875"));
        models.add(new TBOrderModel("TB2020031900020","13878765433","9","中山北路406号(近湖南路)","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",8,"2589635412"));
        models.add(new TBOrderModel("TB2020031900021","15865342576","9","南京市玄武区龙蟠路111号","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",9,"8965745896"));
        models.add(new TBOrderModel("TB2020031900022","14787653437","15","北京市海淀区西三环中路10号","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",10,"7895874569"));
        models.add(new TBOrderModel("TB2020031900023","13686763543","14","北京市丰台区南四环中路235号","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",35,"8963521458"));
        models.add(new TBOrderModel("TB2020031900024","18572543634","12","北京市朝阳区健安东路","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",20,"5963258698"));
        models.add(new TBOrderModel("TB2020031900025","14628735433","11","北京市朝阳区后八里庄5号","广东省深圳市南山区西丽镇塘朗同富裕工业城7栋",33,"5478268852"));
        return models;
    }

    public TBOrderModel getTbOrder(String phone)throws PassportException {
        if (StringUtils.isEmpty(phone)){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        List<TBOrderModel> models=addTbOeder();
        TBOrderModel model=new TBOrderModel();
        for (TBOrderModel mo:models) {
            if(mo.getPhone().equalsIgnoreCase(phone)){
                model=mo;
            }
        }
        if (model==null){
            model=null;
            return model;
        }
        return model;
    }

    public boolean checkTbOrder(String orders)throws PassportException {
        if (StringUtils.isEmpty(orders)){
            throw new PassportException(ResultCode.PARAM_MISS_MSG);
        }
        List<TBOrderModel> models=addTbOeder();
        TBOrderModel model=new TBOrderModel();
        for (TBOrderModel mo:models) {
            if(mo.getOrderId().equalsIgnoreCase(orders)){
                return true;
            }
        }
        return false;
    }
}
