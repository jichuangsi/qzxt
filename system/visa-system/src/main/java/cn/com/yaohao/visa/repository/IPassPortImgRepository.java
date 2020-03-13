package cn.com.yaohao.visa.repository;

import cn.com.yaohao.visa.entity.PassPortImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPassPortImgRepository extends JpaRepository<PassPortImg,Integer> {
    List<PassPortImg> findByOrderNumberAndPassPortCodeIn(String orderNumber,List<String> passportCodes);
    PassPortImg findByOrderNumberAndPassPortCode(String orderNumber,String passPortCode);
}
