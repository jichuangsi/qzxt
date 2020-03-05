package cn.com.yaohao.visa.repository;

import cn.com.yaohao.visa.entity.RemarksInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RemarksInformationRepository extends JpaRepository<RemarksInformation,String>,JpaSpecificationExecutor<RemarksInformation> {

    List<RemarksInformation> findByIdIn(List<String> id);

    @Query(value = "SELECT * FROM remarks_information WHERE id IN ?1 ORDER BY operation_time DESC LIMIT ?2,?3",nativeQuery = true)
    List<RemarksInformation> findByIdInOrderByOperationTimeDesc(List<String> id,int pageNum,int pageSize);
}
