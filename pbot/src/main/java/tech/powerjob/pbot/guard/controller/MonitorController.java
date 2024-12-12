package tech.powerjob.pbot.guard.controller;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 监控测试
 *
 * @author tjq
 * @since 2024/12/10
 */
@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @Autowired
    private MeterRegistry meterRegistry;


    @GetMapping("/random")
    public String randomRequest() {

        meterRegistry.counter("PBOT_SYS_QPS", Lists.newArrayList(Tag.of("method", "random"), Tag.of("service", "monitor"))).increment();

        long rt = ThreadLocalRandom.current().nextLong(300);
        return "random:" + rt;
    }
}
