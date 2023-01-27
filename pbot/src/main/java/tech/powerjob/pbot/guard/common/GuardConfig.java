package tech.powerjob.pbot.guard.common;

import lombok.Getter;
import lombok.Setter;

/**
 * GuardConfig
 *
 * @author tjq
 * @since 2023/1/25
 */
@Getter
@Setter
public class GuardConfig {

    private String appName;

    private String serverAddress;

    private String containerVersion;
}
