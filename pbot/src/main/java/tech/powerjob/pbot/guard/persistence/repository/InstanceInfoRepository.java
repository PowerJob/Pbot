package tech.powerjob.pbot.guard.persistence.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tech.powerjob.pbot.guard.persistence.model.InstanceInfoDO;

/**
 * JobLog 数据访问层
 *
 * @author tjq
 * @since 2020/4/1
 */
public interface InstanceInfoRepository extends JpaRepository<InstanceInfoDO, Long>, JpaSpecificationExecutor<InstanceInfoDO> {


}
