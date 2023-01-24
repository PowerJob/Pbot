package tech.powerjob.pbot.guard.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.powerjob.pbot.guard.persistence.model.WorkflowInstanceInfoDO;

/**
 * 工作流运行实例数据操作
 *
 * @author tjq
 * @since 2020/5/26
 */
public interface WorkflowInstanceInfoRepository extends JpaRepository<WorkflowInstanceInfoDO, Long> {

}
