package cn.com.yaohao.visa.repository;

import cn.com.yaohao.visa.entity.PassportEssential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PassportEssentialRepository extends JpaRepository<PassportEssential,String>,JpaSpecificationExecutor<PassportEssential> {

    PassportEssential findByIdIs(String id);

}
