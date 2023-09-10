package tech.powerjob.pbot.guard.service;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tech.powerjob.pbot.guard.common.GuardConfig;
import tech.powerjob.pbot.guard.persistence.model.*;
import tech.powerjob.pbot.guard.persistence.repository.*;

import java.util.Date;
import java.util.Optional;

/**
 * 重置数据服务
 *
 * @author tjq
 * @since 2020/7/1
 */
@Slf4j
@Service
public class ResetService {

    @Resource
    private GuardConfig guardConfig;

    @Resource
    private AppInfoRepository appInfoRepository;
    @Resource
    private JobInfoRepository jobInfoRepository;
    @Resource
    private ContainerInfoRepository containerInfoRepository;
    @Resource
    private WorkflowInfoRepository workflowInfoRepository;
    @Resource
    private WorkflowNodeInfoRepository workflowNodeInfoRepository;

    private static final String EXAMPLE_PASSWORD = "powerjob123";

    private static final String SYSTEM_EXTRA = "__powerjob_demo_jobs_";

    /**
     * 用户 demo 保留时间
     */
    private static final long MAX_EXPIRE_TIME = 3600 * 1000;

    /**
     * 调用所有重置方法（用于初始化表数据或测试）
     */
    public void resetAll() {
        try {
            resetAppName();
            resetContainer();
            resetJobs();
            resetWorkflow();
        } catch (Exception e) {
            log.error("[ResetService] reset failed!", e);
        }
    }

    // 强制 id 为 1 的 app 为示例 app
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void resetAppName() {

        if (!guardConfig.isEnable()) {
            return;
        }

        final String appName = guardConfig.getAppName();

        Optional<AppInfoDO> appInfoOpt = appInfoRepository.findByAppName(appName);
        if (appInfoOpt.isPresent()) {
            AppInfoDO appInfo = appInfoOpt.get();
            appInfo.setPassword(EXAMPLE_PASSWORD);
            appInfo.setAppName(appName);
            appInfoRepository.saveAndFlush(appInfo);
            log.info("[ResetService] reset appName successfully!");
            return;
        }

        AppInfoDO appInfo = new AppInfoDO();
        appInfo.setId(1L);
        appInfo.setAppName(appName);
        appInfo.setPassword(EXAMPLE_PASSWORD);
        appInfo.setGmtCreate(new Date());
        appInfo.setGmtModified(new Date());
        appInfoRepository.saveAndFlush(appInfo);
        log.info("[ResetService] register samples app successfully!");
    }

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void resetContainer() {

        if (!guardConfig.isEnable()) {
            return;
        }

        ContainerInfoDO container = new ContainerInfoDO();
        container.setId(1L);
        container.setAppId(1L);
        container.setContainerName("powerjob-demo-container");
        container.setStatus(1);
        container.setVersion(guardConfig.getContainerVersion());
        container.setLastDeployTime(new Date());
        container.setSourceType(2);
        container.setSourceInfo("{\"repo\":\"https://gitee.com/KFCFans/PowerJob-Container-Template.git\",\"branch\":\"master\",\"username\":\"\",\"password\":\"\"}");

        container.setGmtModified(new Date());
        container.setGmtCreate(new Date());

        containerInfoRepository.saveAndFlush(container);

        log.info("[ResetService] reset container successfully!");
    }

    @Scheduled(cron = "0 0/3 * * * ? ")
    public void resetJobs() {

        if (!guardConfig.isEnable()) {
            return;
        }

        // JOB1: 官方 HTTP 处理器
        JobInfoDO officialHttpProcessor = newJob(1L, "[CRON] Official Http Processor");
        officialHttpProcessor.setProcessorType(1);
        officialHttpProcessor.setProcessorInfo("tech.powerjob.official.processors.impl.HttpProcessor");
        officialHttpProcessor.setJobParams("{\"method\":\"GET\",\"url\":\"http://www.baidu.com\"}");
        jobInfoRepository.saveAndFlush(officialHttpProcessor);

        // JOB2：官方 SHELL 处理器
        JobInfoDO officialShellProcessor = newJob(2L, "[CRON] Official Shell Processor");
        officialShellProcessor.setProcessorType(1);
        officialShellProcessor.setProcessorInfo("tech.powerjob.official.processors.impl.script.ShellProcessor");
        officialShellProcessor.setJobParams("java -version");
        jobInfoRepository.saveAndFlush(officialShellProcessor);

        // JOB3：官方 PYTHON 处理器
        JobInfoDO officialPythonProcessor = newJob(3L, "[CRON] Official Python Processor");
        officialPythonProcessor.setProcessorType(1);
        officialPythonProcessor.setProcessorInfo("tech.powerjob.official.processors.impl.script.PythonProcessor");
        officialPythonProcessor.setJobParams("print 'welcome to use PowerJob~'");
        jobInfoRepository.saveAndFlush(officialPythonProcessor);

        // Job4: 官方文件清理处理器（Disable）
        JobInfoDO officialCleanupProcessor = newJob(4L, "[CRON] Official FileCleanUp Processor");
        officialCleanupProcessor.setProcessorType(1);
        officialCleanupProcessor.setProcessorInfo("tech.powerjob.official.processors.impl.FileCleanupProcessor");
        officialCleanupProcessor.setJobParams("[{\"filePattern\":\"(shell|python)_[0-9]*\\\\.(sh|py)\",\"dirPath\":\"/root/docker\",\"retentionTime\":24}]");
        officialCleanupProcessor.setStatus(2);
        officialCleanupProcessor.setExecuteType(2);
        jobInfoRepository.saveAndFlush(officialCleanupProcessor);


        // JOB5: CRON-单机
        JobInfoDO cronStandalone = newJob(5L, "[CRON] Standalone");
        cronStandalone.setJobParams("{\"mode\":\"BASE\",\"responseSize\":12}");
        jobInfoRepository.save(cronStandalone);

        // Job6: CRON-广播
        JobInfoDO cronBroadcast = newJob(6L, "[CRON] Broadcast");
        cronBroadcast.setExecuteType(2);
        jobInfoRepository.save(cronBroadcast);

        // Job7: CRON-MapReduce
        JobInfoDO cronMR = newJob(7L, "[CRON] MapReduce");
        cronMR.setJobParams("{\"mode\":\"MR\",\"batchNum\": 10, \"batchSize\": 20,\"subTaskSuccessRate\":0.8}");
        cronMR.setExecuteType(3);
        cronMR.setInstanceTimeLimit(300000L);
        jobInfoRepository.save(cronMR);

        // Job4: FixedRate-单机
        JobInfoDO fixedStandalone = newJob(8L, "[FixedRate] Standalone");
        fixedStandalone.setTimeExpressionType(3);
        fixedStandalone.setTimeExpression("30000");
        jobInfoRepository.save(fixedStandalone);

        // Job5: FixedDelay - SHELL
        JobInfoDO fixedDelayShell = newJob(9L, "[FixedDelay] SHELL");
        fixedDelayShell.setTimeExpressionType(4);
        fixedDelayShell.setTimeExpression("10000");
        fixedDelayShell.setProcessorType(1);
        fixedDelayShell.setJobParams("ls -a");
        fixedDelayShell.setProcessorInfo("tech.powerjob.official.processors.impl.script.ShellProcessor");
        jobInfoRepository.save(fixedDelayShell);

        // Job6: API Job
        JobInfoDO apiJob = newJob(10L, "[API] Standalone");
        apiJob.setTimeExpression(null);
        apiJob.setTimeExpressionType(1);
        jobInfoRepository.save(apiJob);

        // Job 11~19, DAG-Node
        for (long i = 11; i < 20; i++) {
            JobInfoDO dagNode = newJob(i, "DAG-Node-" + (i - 11));
            dagNode.setTimeExpressionType(5);
            dagNode.setTimeExpression(null);
            dagNode.setMaxInstanceNum(0);
            dagNode.setJobParams("{\"mode\":\"BASE\",\"responseSize\":8}");
            jobInfoRepository.save(dagNode);
        }

        log.info("[ResetService] reset jobs successfully!");
    }

    @Scheduled(cron = "0 0/15 * * * ? ")
    public void cleanJobAndWorkflow() {

        if (!guardConfig.isEnable()) {
            return;
        }

        jobInfoRepository.findAll().forEach(jobInfoDO -> {
            final boolean canDelete = checkCanDelete(jobInfoDO.getId(), jobInfoDO.getGmtModified());
            if (canDelete) {
                log.info("[ResetService] delete user created job: {}", JSONObject.toJSONString(jobInfoDO));
                jobInfoRepository.deleteById(jobInfoDO.getId());
            }
        });

        workflowInfoRepository.findAll().forEach(workflowInfoDO -> {
            final boolean canDelete = checkCanDelete(workflowInfoDO.getId(), workflowInfoDO.getGmtModified());
            if (canDelete) {
                log.info("[ResetService] delete user created workflow: {}", JSONObject.toJSONString(workflowInfoDO));
                workflowInfoRepository.deleteById(workflowInfoDO.getId());
            }
        });
    }

    private static boolean checkCanDelete(Long id, Date modifyTime) {

        // 前2个任务固定保留，一定是系统任务
        if (id <= 2) {
            return false;
        }

        // 保留用户1小时内的任务（PBot 正常运行的情况下，modifyTime 系统任务 modifyTime 一定会被更新。PBot 未正常运行，这段代码也执行不了）
        long offset = System.currentTimeMillis() - modifyTime.getTime();
        return offset >= MAX_EXPIRE_TIME;
    }

    private static JobInfoDO newJob(Long id, String name) {
        JobInfoDO base = new JobInfoDO();
        base.setJobDescription("welcome to use PowerJob~");
        base.setId(id);
        base.setJobName(name);
        base.setConcurrency(5);
        base.setAppId(1L);
        base.setInstanceRetryNum(0);
        base.setTaskRetryNum(1);
        base.setMinCpuCores(0);
        base.setMinDiskSpace(0);
        base.setMinMemorySpace(0);
        base.setStatus(1);
        base.setMaxWorkerCount(0);
        base.setMaxInstanceNum(3);
        base.setProcessorType(1);
        base.setInstanceTimeLimit(60000L);

        base.setTimeExpressionType(2);
        base.setTimeExpression("0 0/5 * * * ? *");
        base.setNextTriggerTime(System.currentTimeMillis() + 60000);

        base.setExecuteType(1);
        base.setProcessorInfo("tech.powerjob.official.processors.impl.VerificationProcessor");
        base.setDispatchStrategy(1);

        base.setExtra(SYSTEM_EXTRA);

        base.setGmtCreate(new Date());
        base.setGmtModified(new Date());
        return base;
    }

    @Scheduled(cron = "0 0/7 * * * ? ")
    private void resetWorkflow() {

        if (!guardConfig.isEnable()) {
            return;
        }

        // GraphA: A -> B -> C
        WorkflowNodeInfoDO nodeA = newNode(1L, 11L, "Node-A", 1L);
        WorkflowNodeInfoDO nodeB = newNode(2L, 12L, "Node-B", 1L);
        WorkflowNodeInfoDO nodeC = newNode(3L, 13L, "Node-C", 1L);
        workflowNodeInfoRepository.saveAll(Lists.newArrayList(nodeA, nodeB, nodeC));
        WorkflowInfoDO workflowA = newWorkflow(1L, "A -> B -> C", "{\"edges\":[{\"from\":1,\"to\":2},{\"from\":2,\"to\":3}],\"nodes\":[{\"nodeId\":1},{\"nodeId\":2},{\"nodeId\":3}]}");
        workflowInfoRepository.saveAndFlush(workflowA);


        WorkflowNodeInfoDO nodeAA = newNode(4L, 11L, "Node-A", 2L);
        WorkflowNodeInfoDO nodeBB = newNode(5L, 12L, "Node-B", 2L);
        WorkflowNodeInfoDO nodeCC = newNode(6L, 13L, "Node-C", 2L);
        nodeCC.setEnable(false);
        WorkflowNodeInfoDO nodeDD = newNode(7L, 14L, "Node-D", 2L);
        nodeDD.setNodeParams("{\"mode\":\"ERROR\"}");
        nodeDD.setSkipWhenFailed(true);
        WorkflowNodeInfoDO nodeEE = newNode(8L, 15L, "Node-E", 2L);
        workflowNodeInfoRepository.saveAll(Lists.newArrayList(nodeAA, nodeBB, nodeCC, nodeDD, nodeEE));
        WorkflowInfoDO workflowB = newWorkflow(2L, "complexDAG", "{\"edges\":[{\"from\":4,\"to\":5},{\"from\":4,\"to\":6},{\"from\":4,\"to\":7},{\"from\":5,\"to\":8},{\"from\":6,\"to\":8},{\"from\":7,\"to\":8}],\"nodes\":[{\"nodeId\":4},{\"nodeId\":5},{\"nodeId\":6},{\"nodeId\":7},{\"nodeId\":8}]}");
        workflowInfoRepository.saveAndFlush(workflowB);
    }

    private static WorkflowNodeInfoDO newNode(Long nodeId, Long jobId, String name, Long workflowId) {
        WorkflowNodeInfoDO node = new WorkflowNodeInfoDO();
        node.setAppId(1L);
        node.setId(nodeId);
        node.setJobId(jobId);
        node.setGmtCreate(new Date());
        node.setGmtModified(new Date());
        node.setNodeName(name);
        node.setWorkflowId(workflowId);
        node.setEnable(true);
        node.setSkipWhenFailed(false);
        node.setType(1);
        return node;
    }

    private static WorkflowInfoDO newWorkflow(Long workflowId, String name, String dag) {
        WorkflowInfoDO workflowA = new WorkflowInfoDO();
        workflowA.setAppId(1L);
        workflowA.setGmtCreate(new Date());
        workflowA.setGmtModified(new Date());
        workflowA.setId(workflowId);
        workflowA.setWfName(name);
        workflowA.setStatus(1);
        workflowA.setMaxWfInstanceNum(2);
        workflowA.setNextTriggerTime(System.currentTimeMillis() + 5000);
        workflowA.setWfDescription("welcome to use PowerJob~");
        workflowA.setPeDAG(dag);
        workflowA.setTimeExpressionType(2);
        workflowA.setTimeExpression("0 0/5 * * * ? *");
        workflowA.setExtra(SYSTEM_EXTRA);
        return workflowA;
    }
}
