package cn.com.yaohao.visa.repository.IntermediateRepasitory;

import cn.com.yaohao.visa.entity.IntermediateTable.PassportSupplementRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPassPortSupplementRelationRepository extends JpaRepository<PassportSupplementRelation,String> {
}
