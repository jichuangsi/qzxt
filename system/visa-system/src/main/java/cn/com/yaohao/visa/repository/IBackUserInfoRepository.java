package cn.com.yaohao.visa.repository;

import cn.com.yaohao.visa.entity.BackUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IBackUserInfoRepository extends JpaRepository<BackUserInfo,String> {
    int countByaccount(String account);
    BackUserInfo findByAccountAndPwd(String account, String pwd);
    BackUserInfo findByAccountAndPwdAndStatus(String account, String pwd,String status);
    BackUserInfo findByid(String id);

    int countByRoleIdAndRoleIdNotContains(Integer role,Integer role2);

    @Query(value = "SELECT * FROM backuser_info WHERE role_id=?1 AND role_id !='123456' ORDER BY created_time DESC LIMIT ?2,?3",nativeQuery = true)
    List<BackUserInfo> findAllByPage(Integer role, int pageNum, int pageSize);

    @Query(value = "SELECT * FROM backuser_info WHERE role_id !='123456' ORDER BY created_time DESC LIMIT ?1,?2",nativeQuery = true)
    List<BackUserInfo> findAllByPage(int pageNum,int pageSize);

    int countByRoleIdNotContains(Integer role);
}
