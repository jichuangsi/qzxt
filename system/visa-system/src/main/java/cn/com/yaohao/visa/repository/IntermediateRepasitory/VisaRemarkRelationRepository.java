package cn.com.yaohao.visa.repository.IntermediateRepasitory;

import cn.com.yaohao.visa.entity.IntermediateTable.VisaRemarkRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface VisaRemarkRelationRepository extends JpaRepository<VisaRemarkRelation,String>,JpaSpecificationExecutor<VisaRemarkRelation> {

    List<VisaRemarkRelation> findByVisaId(String vid);

}
