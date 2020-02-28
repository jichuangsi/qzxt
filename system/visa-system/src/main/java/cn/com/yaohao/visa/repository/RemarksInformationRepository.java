package cn.com.yaohao.visa.repository;

import cn.com.yaohao.visa.entity.RemarksInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RemarksInformationRepository extends JpaRepository<RemarksInformation,String>,JpaSpecificationExecutor<RemarksInformation> {

    List<RemarksInformation> findByIdIn(List<String> id);
}
