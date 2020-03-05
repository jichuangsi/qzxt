package cn.com.yaohao.visa.repository.IntermediateRepasitory;

import cn.com.yaohao.visa.entity.IntermediateTable.ExpressReceiptRemarkRelation;
import cn.com.yaohao.visa.entity.IntermediateTable.VisaRemarkRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ExpressReceiptRemarkRelationRepository extends JpaRepository<ExpressReceiptRemarkRelation,String>,JpaSpecificationExecutor<ExpressReceiptRemarkRelation> {

    List<ExpressReceiptRemarkRelation> findByExpressId(String expressId);

}
