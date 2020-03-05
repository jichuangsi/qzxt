package cn.com.yaohao.visa.dao.mapper;

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

    //待补充签证
    @Select(value = "<script>SELECT pi.id as id,pi.`name` as name,er.order_number as orderNumber, pi.passport_encoding as passportEncoding,pi.telephone_number AS telephoneNumber,\n" +
            "pi.expiry_date as expiryDate,psr.illustrated as problem,vpr.vid as expressReceiptId\n" +
            "FROM passport_supplement_relation psr INNER JOIN passport_information pi ON psr.pass_id=pi.id INNER JOIN visa_passport_relation vpr ON pi.id =vpr.pass_id INNER JOIN express_receipt er ON vpr.vid=er.id\n" +
            "WHERE 1=1\n"
            +"<if test='orderNum != null '>AND order_number=#{orderNum}</if>"
            +"<if test='name != null'>AND `name` LIKE %#{name}%</if>"
            +"<if test='passportId != null'>AND order_number=#{passportId}</if>"
            +"<if test='phone != null'>AND pi.telephone_number LIKE %#{phone}%</if>"+
            "LIMIT #{pageNum},#{pageSize}</script>")
    List<ValidationModel> findProblePassWord(@Param("orderNum")String orderNum,@Param("name")String name,@Param("passportId")String passportId,@Param("phone")String phone,@Param("pageNum")int pageNum,@Param("pageSize")int pageSize);
}
