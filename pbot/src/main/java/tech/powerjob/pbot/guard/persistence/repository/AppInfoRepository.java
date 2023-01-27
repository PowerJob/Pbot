package tech.powerjob.pbot.guard.persistence.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tech.powerjob.pbot.guard.persistence.model.AppInfoDO;

import java.util.Optional;

/**
 * AppInfo 数据访问层
 *
 * @author tjq
 * @since 2020/4/1
 */
public interface AppInfoRepository extends JpaRepository<AppInfoDO, Long> {

    Optional<AppInfoDO> findByAppName(String appName);
}
