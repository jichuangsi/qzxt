package cn.com.yaohao.visa.repository.IntermediateRepasitory;

import cn.com.yaohao.visa.entity.PassportInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PassportInformationRepository extends JpaRepository<PassportInformation,String>,JpaSpecificationExecutor<PassportInformation> {

    PassportInformation findByIdIs(String id);
    List<PassportInformation> findByIdIn(List<String> id);
}
