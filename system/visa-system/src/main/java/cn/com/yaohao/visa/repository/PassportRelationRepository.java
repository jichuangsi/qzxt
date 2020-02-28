package cn.com.yaohao.visa.repository;

import cn.com.yaohao.visa.entity.IntermediateTable.PassportRelation;
import cn.com.yaohao.visa.entity.PassportInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PassportRelationRepository extends JpaRepository<PassportRelation,String>,JpaSpecificationExecutor<PassportRelation> {

    PassportRelation findByPassportId(String id);

}
