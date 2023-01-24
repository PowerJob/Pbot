package tech.powerjob.pbot.guard.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.powerjob.pbot.guard.persistence.model.WorkflowInfoDO;

/**
 * DAG 工作流 数据操作层
 *
 * @author tjq
 * @since 2020/5/26
 */
public interface WorkflowInfoRepository extends JpaRepository<WorkflowInfoDO, Long> {

    int deleteByIdGreaterThan(int id);

}
