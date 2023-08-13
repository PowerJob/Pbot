package tech.powerjob.pbot.guard.controller;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.powerjob.pbot.guard.service.ResetService;


/**
 * test
 * <a href="http://localhost:7777/test/resetAll">url</a>
 *
 * @author tjq
 * @since 2023/1/27
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private ResetService resetService;

    @GetMapping("/resetAll")
    public JSONObject resetAll() {
        JSONObject ret = new JSONObject();
        try {
            resetService.resetAll();
            ret.put("success", true);
        } catch (Exception e) {
            ret.put("success", false);
            ret.put("msg", ExceptionUtils.getStackTrace(e));
        }
        return ret;
    }

    @GetMapping("/cleanAll")
    public void cleanAll() {
        resetService.cleanJobAndWorkflow();
    }

}
