package tech.powerjob.pbot.guard.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import tech.powerjob.pbot.guard.persistence.model.JobInfoDO;

/**
 * JobInfo 数据访问层
 *
 * @author tjq
 * @since 2020/4/1
 */
public interface JobInfoRepository extends JpaRepository<JobInfoDO, Long>, JpaSpecificationExecutor<JobInfoDO> {

    int deleteByIdGreaterThan(int id);
}
