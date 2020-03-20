package cn.com.yaohao.visa.repository;

import cn.com.yaohao.visa.entity.Logistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILogisticsRepository extends JpaRepository<Logistics,String> {
    Logistics findBySfExpressId(String sfId);
}
