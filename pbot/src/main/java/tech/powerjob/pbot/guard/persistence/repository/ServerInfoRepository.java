package tech.powerjob.pbot.guard.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.powerjob.pbot.guard.persistence.model.ServerInfoDO;

/**
 * 服务器信息 数据操作层
 *
 * @author tjq
 * @since 2020/4/15
 */
public interface ServerInfoRepository extends JpaRepository<ServerInfoDO, Long> {
}
