package cn.com.yaohao.visa.repository;

import cn.com.yaohao.visa.entity.ExpressReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpressReceiptRepository extends JpaRepository<ExpressReceipt,String>,JpaSpecificationExecutor<ExpressReceipt> {

    @Query(value = "SELECT * FROM `test_express` where problem =\"否\" order by create_time ASC LIMIT ?1,?2",nativeQuery = true)
    List<ExpressReceipt> getOrderByCreateTimeASEC(int num, int size);

    ExpressReceipt findByIdIs(String id);

    ExpressReceipt findByid(String id);

    List<ExpressReceipt> findByidIn(List<String> ids);
}
