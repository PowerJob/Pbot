package tech.powerjob.pbot.guard.persistence.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tech.powerjob.pbot.guard.persistence.model.OmsLockDO;

/**
 * 利用唯一性约束作为数据库锁
 *
 * @author tjq
 * @since 2020/4/2
 */
public interface OmsLockRepository extends JpaRepository<OmsLockDO, Long> {

}
