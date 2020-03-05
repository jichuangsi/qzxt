package cn.com.yaohao.visa.repository.IntermediateRepasitory;

import cn.com.yaohao.visa.entity.IntermediateTable.VisaPassportRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface VisaPassportRelationRepository extends JpaRepository<VisaPassportRelation,String>,JpaSpecificationExecutor<VisaPassportRelation> {

    List<VisaPassportRelation> findByVid(String vid);
    VisaPassportRelation findByPassId(String passId);
}
