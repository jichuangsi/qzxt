package cn.com.yaohao.visa.repository.IntermediateRepasitory;

import cn.com.yaohao.visa.entity.PassportInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface PassportInformationRepository extends JpaRepository<PassportInformation,String>,JpaSpecificationExecutor<PassportInformation> {

    PassportInformation findByIdIs(String id);
    @Query(value = "SELECT * FROM passport_information WHERE id IN ?1 LIMIT ?2,?3",nativeQuery = true)
    List<PassportInformation> findByIdIn(List<String> id,int pageNum,int pageSize);
    int countByIdIn(List<String> id);
    List<PassportInformation> findByStatus(String status);
    List<PassportInformation> findByStatusAndOrderId(String status,String orderId);
    List<PassportInformation> findByStatusAndOrderIdIn(String status,List<String> orderIds);
    List<PassportInformation> findByStatusAndOrderIdInAndIsExportOrderByOrderId(String status,List<String> orderIds,String status2);
    PassportInformation findByPassportEncoding(String passPortId);
    @Transactional
    @Modifying
    @Query(value = "UPDATE passport_information SET `status`='P',send_status='N',check_time=unix_timestamp(now()),expire_time=DATE_ADD(FROM_UNIXTIME(unix_timestamp(now()), '%Y-%m-%d %H:%i:%S'),INTERVAL ?1 DAY) WHERE id=?2",nativeQuery = true)
    void checkPassPort(int schedule,String passPortId);
    List<PassportInformation> findByPassportEncodingInAndOrderId(List<String> encoding,String orderId);
    int countByPassportEncodingInAndOrderId(List<String> encoding,String orderId);
    PassportInformation findByPassportEncodingAndOrderId(String passPortId,String orderNum);
    List<PassportInformation> findByIdInAndSendStatusOrSendStatus(List<String> pids,String status,String status2);
    List<PassportInformation> findByOrderId(String orderId);
    List<PassportInformation> findByIdIn(List<String> ids);
    List<PassportInformation> findBySfExpressId(String sFExpressId);
    List<PassportInformation> findByPassportEncodingInAndStatusNot(List<String> codes,String status);
    List<PassportInformation> findByPassportEncodingInAndStatus(List<String> codes,String status);
}
