package cn.com.yaohao.visa.dao.mapper;

import cn.com.yaohao.visa.entity.ExpressReceipt;
import cn.com.yaohao.visa.entity.Role;
import cn.com.yaohao.visa.model.ValidationModel;
import cn.com.yaohao.visa.model.backuser.UrlMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IVisaMapper {
    @Select(value = "<script>SELECT r.id AS id,r.rolename as rolename FROM user_role_relation urr INNER JOIN role r ON urr.rid=r.id WHERE urr.uid=#{uid}</script>")
    List<Role> findRoleByUid(@Param("uid")String uid);

    @Select(value = "<script>SELECT pn.id as modelId,pn.node_name as modelName,sp.id as staticPageId,sp.page_name as staticPageName,sp.page_url as staticPageUrl,u.id as relationId \n" +
            "FROM urlrelation u INNER JOIN static_page sp ON u.urlid=sp.id INNER JOIN parent_node pn ON sp.parent_node=pn.id \n" +
            "WHERE u.rid IN " +
            "<foreach collection='roleId' item='item' open='(' separator=',' close=')'>#{item}</foreach></script>")
    List<UrlMapping> findAllByRoleIn(@Param("roleId")List<Integer> roleId);

    @Select(value = "<script>SELECT pn.id as modelId,pn.node_name as modelName,sp.id as staticPageId,sp.page_name as staticPageName,sp.page_url as staticPageUrl \n" +
            "FROM static_page sp INNER JOIN parent_node pn ON sp.parent_node=pn.id</script>")
    List<UrlMapping> findAllByRole();

    //查询可访问的接口
    @Select(value = "<script>SELECT pn.id as modelId,pn.node_name as modelName,sp.id as staticPageId,sp.page_name as staticPageName,sp.page_url as staticPageUrl,u.id as relationId,mp.id as id,mp.`name` as name,mp.url as roleUrl \n" +
            "FROM urlrelation u INNER JOIN static_page sp ON u.urlid=sp.id INNER JOIN parent_node pn ON sp.parent_node=pn.id INNER JOIN method_path mp ON sp.id=mp.page_id \n" +
            "WHERE u.rid IN " +
            "<foreach collection='roleId' item='item' open='(' separator=',' close=')'>#{item}</foreach></script>")
    List<UrlMapping> findMethodByRoleIn(@Param("roleId")List<Integer> roleId);


    //待补充签证(状态码F:未通过)
    @Select(value = "<script>SELECT pi.id as id,pi.`name` as name,er.order_number as orderNumber, pi.passport_encoding as passportEncoding,er.telephone_number AS telephoneNumber,\n" +
            "pi.expiry_date as expiryDate,psr.illustrated as problem,vpr.vid as expressReceiptId,er.`schedule` as schedule\n" +
            "FROM passport_information pi LEFT JOIN passport_supplement_relation psr ON pi.id=psr.pass_id INNER JOIN visa_passport_relation vpr ON pi.id =vpr.pass_id INNER JOIN express_receipt er ON vpr.vid=er.id\n" +
            "WHERE pi.`status`='F'\n"
            +"<if test='orderNum != null '>AND order_number=#{orderNum}</if>"
            +"<if test='name != null'>AND `name` LIKE CONCAT('%', #{name},'%')</if>"
            +"<if test='passportId != null'>AND pi.passport_encoding=#{passportId}</if>"
            +"<if test='phone != null'>AND er.telephone_number LIKE CONCAT('%', #{phone},'%')</if>"+
            "LIMIT #{pageNum},#{pageSize}</script>")
    List<ValidationModel> findProblePassWord(@Param("orderNum")String orderNum,@Param("name")String name,@Param("passportId")String passportId,@Param("phone")String phone,@Param("pageNum")int pageNum,@Param("pageSize")int pageSize);

    //待补充签证统计
    @Select(value = "<script>SELECT count(1) FROM passport_information pi LEFT JOIN passport_supplement_relation psr ON pi.id=psr.pass_id INNER JOIN visa_passport_relation vpr ON pi.id =vpr.pass_id INNER JOIN express_receipt er ON vpr.vid=er.id\n" +
            "WHERE pi.`status`='F'\n"
            +"<if test='orderNum != null '>AND order_number=#{orderNum}</if>"
            +"<if test='name != null'>AND `name` LIKE CONCAT('%', #{name},'%')</if>"
            +"<if test='passportId != null'>AND pi.passport_encoding=#{passportId}</if>"
            +"<if test='phone != null'>AND er.telephone_number LIKE CONCAT('%', #{phone},'%')</if>" +
            "</script>")
    int countProblePassWord(@Param("orderNum")String orderNum,@Param("name")String name,@Param("passportId")String passportId,@Param("phone")String phone);

    //签证审核(状态码W:待审核)
    @Select(value = "<script>SELECT pi.`name` as name,er.order_number as orderNumber,pi.passport_encoding as passportEncoding,er.telephone_number as telephoneNumber,er.`schedule` as schedule,pi.expiry_date as expiryDate,pi.id as id,er.id as expressReceiptId\n" +
            "FROM passport_information pi INNER JOIN visa_passport_relation vpr ON pi.id=vpr.pass_id INNER JOIN express_receipt er ON vpr.vid=er.id WHERE pi.`status`= 'W' "
            +"<if test='orderNum != null '>AND er.order_number=#{orderNum}</if>"
            +"<if test='name != null'>AND pi.`name` LIKE CONCAT('%', #{name},'%')</if>"
            +"<if test='passportId != null'>AND pi.passport_encoding=#{passportId}</if>"
            +"<if test='phone != null'>AND er.telephone_number LIKE CONCAT('%', #{phone},'%')</if>"+
            "ORDER BY pi.order_id LIMIT #{pageNum},#{pageSize}</script>")
    List<ValidationModel> findPassPortByStatus(@Param("orderNum")String orderNum,@Param("passportId")String passportId,@Param("name")String name,@Param("phone")String phone,@Param("pageNum")int pageNum,@Param("pageSize")int pageSize);

    //签证审核统计
    @Select(value = "<script>SELECT count(1) FROM passport_information pi INNER JOIN visa_passport_relation vpr ON pi.id=vpr.pass_id INNER JOIN express_receipt er ON vpr.vid=er.id WHERE pi.`status`= 'W' "
            +"<if test='orderNum != null '>AND er.order_number=#{orderNum}</if>"
            +"<if test='name != null'>AND pi.`name` LIKE CONCAT('%', #{name},'%')</if>"
            +"<if test='passportId != null'>AND pi.passport_encoding=#{passportId}</if>"
            +"<if test='phone != null'>AND er.telephone_number LIKE CONCAT('%', #{phone},'%')</if>"+
            "ORDER BY pi.order_id</script>")
    int countPassPortByStatus(@Param("orderNum")String orderNum,@Param("passportId")String passportId,@Param("name")String name,@Param("phone")String phone);

    //签证进度(状态码P:审核通过)(工期内包含周末)
    @Select(value = "<script>SELECT er.order_number as orderNumber,er.schedule as schedule,pi.`name` as name,er.telephone_number as telephoneNumber,pi.send_status as sendStatus,pi.`status` as status,\n" +
            "pi.check_time as checkTime,pi.expire_time as expireTime,pi.id as id,er.id as expressReceiptId,pi.passport_encoding as passportEncoding,pi.is_send_back as isSendBack,\n" +
            "IF(pi.is_send_back='SB',99,IF(TIMESTAMPDIFF(DAY ,date_format(now(), '%Y-%m-%d'),DATE_FORMAT(expire_time,'%Y-%m-%d'))>=0,TIMESTAMPDIFF(DAY ,date_format(now(), '%Y-%m-%d'),DATE_FORMAT(expire_time,'%Y-%m-%d')),0) ) AS DIFF_DATE," +
            "pi.sex as sex,pi.birth_day as birthDay,pi.birth_place as birthPlace,pi.habitation as habitation,pi.expiry_date as expiryDate,pi.return_address AS returnAddress\n" +
            "FROM passport_information pi INNER JOIN visa_passport_relation vpr ON pi.id=vpr.pass_id INNER JOIN express_receipt er ON vpr.vid=er.id \n" +
            "WHERE pi.`status`='P'"
            +"<if test='orderNum != null '>AND er.order_number=#{orderNum}</if>"
            +"<if test='name != null'>AND pi.`name` LIKE CONCAT('%', #{name},'%')</if>"
            +"<if test='passportId != null'>AND pi.passport_encoding=#{passportId}</if>"
            +"<if test='phone != null'>AND er.telephone_number LIKE CONCAT('%', #{phone},'%')</if>"
            +"<if test='status != null'>AND pi.send_status=#{status}</if>"+
            "  ORDER BY DIFF_DATE,pi.order_id LIMIT #{pageNum},#{pageSize}</script>")
    List<ValidationModel> findPassPortByPass(@Param("orderNum")String orderNum,@Param("name")String name,@Param("passportId")String passportId,@Param("phone")String phone,@Param("status")String status,@Param("pageNum")int pageNum,@Param("pageSize")int pageSize);

    //签证进度(状态码P:审核通过)(工期内不包含周末)
    @Select(value = "<script>SELECT er.order_number as orderNumber,er.schedule as schedule,pi.`name` as name,er.telephone_number as telephoneNumber,pi.send_status as sendStatus,pi.`status` as status,\n" +
            "pi.check_time as checkTime,pi.expire_time as expireTime,pi.id as id,er.id as expressReceiptId,pi.passport_encoding as passportEncoding,pi.is_send_back as isSendBack,\n" +
            "IF(pi.is_send_back='SB',99,IF((er.`schedule`- workdaynum(FROM_UNIXTIME(pi.check_time, '%Y-%m-%d'),date_format(now(), '%Y-%m-%d')))>=0,er.`schedule`- workdaynum(FROM_UNIXTIME(pi.check_time, '%Y-%m-%d'),date_format(now(), '%Y-%m-%d')),0)) AS DIFF_DATE," +
            "pi.sex as sex,pi.birth_day as birthDay,pi.birth_place as birthPlace,pi.habitation as habitation,pi.expiry_date as expiryDate,pi.return_address AS returnAddress\n" +
            "FROM passport_information pi INNER JOIN visa_passport_relation vpr ON pi.id=vpr.pass_id INNER JOIN express_receipt er ON vpr.vid=er.id \n" +
            "WHERE pi.`status`='P'"
            +"<if test='orderNum != null '>AND er.order_number=#{orderNum}</if>"
            +"<if test='name != null'>AND pi.`name` LIKE CONCAT('%', #{name},'%')</if>"
            +"<if test='passportId != null'>AND pi.passport_encoding=#{passportId}</if>"
            +"<if test='phone != null'>AND er.telephone_number LIKE CONCAT('%', #{phone},'%')</if>"
            +"<if test='status != null'>AND pi.send_status=#{status}</if>"+
            "  ORDER BY DIFF_DATE,pi.order_id LIMIT #{pageNum},#{pageSize}</script>")
    List<ValidationModel> findPassPortByPass2(@Param("orderNum")String orderNum,@Param("name")String name,@Param("passportId")String passportId,@Param("phone")String phone,@Param("status")String status,@Param("pageNum")int pageNum,@Param("pageSize")int pageSize);

    //签证进度统计
    @Select(value = "<script> SELECT count(1) FROM passport_information pi INNER JOIN visa_passport_relation vpr ON pi.id=vpr.pass_id INNER JOIN express_receipt er ON vpr.vid=er.id \n" +
            "WHERE pi.`status`='P'"
            +"<if test='orderNum != null '>AND er.order_number=#{orderNum}</if>"
            +"<if test='name != null'>AND pi.`name` LIKE CONCAT('%', #{name},'%')</if>"
            +"<if test='passportId != null'>AND pi.passport_encoding=#{passportId}</if>"
            +"<if test='phone != null'>AND er.telephone_number LIKE CONCAT('%', #{phone},'%')</if>"
            +"<if test='status != null'> AND pi.send_status=#{status}</if>"+
            "  ORDER BY pi.order_id</script>")
    int countPassPortByPass(@Param("orderNum")String orderNum,@Param("name")String name,@Param("passportId")String passportId,@Param("phone")String phone,@Param("status")String status);

    @Select(value = "<script>SELECT id as id,count as count,courier_name as courierName,courier_number as courierNumber,telephone_number as telephoneNumber,signatory as signatory,order_number as orderNumber,\n" +
            "order_path as orderPath, problem as problem,`status` as status,is_error as isError,return_address as returnAddress,address as address,`schedule` as schedule,create_time as createTime FROM express_receipt WHERE is_error='0' "
            +"<if test='orderNum != null '>AND order_number=#{orderNum}</if>"
            +"<if test='name != null'>AND signatory LIKE CONCAT('%', #{name},'%')</if>"
            +"<if test='phone != null'>AND er.telephone_number LIKE CONCAT('%', #{phone},'%')</if>"
            +"<if test='schedule != null'> AND `schedule`=#{schedule}</if>"+
            " and create_time BETWEEN #{timeStart} and #{timeEnd} LIMIT #{pageNum},#{pageSize}</script>")
    List<ExpressReceipt> findByIsErrorAndCondition(@Param("orderNum")String orderNum,@Param("name")String name,@Param("phone")String phone,@Param("schedule")String schedule,
                                                   @Param("timeStart")long timeStart,@Param("timeEnd")long timeEnd,@Param("pageNum")int pageNum,@Param("pageSize")int pageSize);


    @Select(value = "<script>SELECT count(1) FROM express_receipt WHERE is_error='0' "
            +"<if test='orderNum != null '>AND order_number=#{orderNum}</if>"
            +"<if test='name != null'>AND signatory LIKE CONCAT('%', #{name},'%')</if>"
            +"<if test='phone != null'>AND er.telephone_number LIKE CONCAT('%', #{phone},'%')</if>"
            +"<if test='schedule != null'> AND `schedule`=#{schedule}</if>"+
            " and create_time BETWEEN #{timeStart} and #{timeEnd} </script>")
    int countByIsErrorAndCondition(@Param("orderNum")String orderNum,@Param("name")String name,@Param("phone")String phone,@Param("schedule")String schedule,
                                                   @Param("timeStart")long timeStart,@Param("timeEnd")long timeEnd);

    @Select(value = "<script>select MAX(`code`)\n" +
            "from logistics\n" +
            "where \n" +
            "FROM_UNIXTIME(create_time/1000,'%Y-%m-%d') >= date_format(#{date},'%Y-%m-%d')</script>")
    String getMaxCodeByDate(@Param("date")String date);
}
