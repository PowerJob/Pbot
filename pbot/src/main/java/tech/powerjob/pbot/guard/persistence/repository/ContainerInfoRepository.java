package tech.powerjob.pbot.guard.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.powerjob.pbot.guard.persistence.model.ContainerInfoDO;

/**
 * 容器信息 数据操作层
 *
 * @author tjq
 * @since 2020/5/15
 */
public interface ContainerInfoRepository extends JpaRepository<ContainerInfoDO, Long> {

}
