package cn.com.yaohao.visa.repository;

import cn.com.yaohao.visa.entity.VisaOperationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IVisaOperationRecordRepository extends JpaRepository<VisaOperationRecord,String>,JpaSpecificationExecutor<VisaOperationRecord>,PagingAndSortingRepository<VisaOperationRecord,String> {
}
