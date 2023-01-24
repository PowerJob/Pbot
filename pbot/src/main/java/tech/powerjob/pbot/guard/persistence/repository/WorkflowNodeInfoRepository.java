package tech.powerjob.pbot.guard.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.powerjob.pbot.guard.persistence.model.WorkflowNodeInfoDO;


/**
 * WorkflowNodeInfo 数据访问层
 *
 * @author Echo009
 * @since 2021/2/1
 */
public interface WorkflowNodeInfoRepository extends JpaRepository<WorkflowNodeInfoDO, Long> {


}
