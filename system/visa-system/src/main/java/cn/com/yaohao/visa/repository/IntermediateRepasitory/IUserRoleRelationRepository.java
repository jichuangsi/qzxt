package cn.com.yaohao.visa.repository.IntermediateRepasitory;

import cn.com.yaohao.visa.entity.IntermediateTable.UserRoleRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUserRoleRelationRepository extends JpaRepository<UserRoleRelation,Integer> {
    List<UserRoleRelation> findByUid(String uid);


}
