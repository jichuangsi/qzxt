package cn.com.yaohao.visa.repository.IntermediateRepasitory;

import cn.com.yaohao.visa.entity.IntermediateTable.PassportSupplementRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface IPassPortSupplementRelationRepository extends JpaRepository<PassportSupplementRelation,String> {
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM PassportSupplementRelation WHERE pass_id=?1")
    void deleteByPassId(String passPortId);
}
