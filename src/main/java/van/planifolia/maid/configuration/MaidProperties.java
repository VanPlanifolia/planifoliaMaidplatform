package van.planifolia.maid.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: Planifolia.Van
 * @Date: 2024/12/5 14:58
 */
@Data
@ConfigurationProperties(prefix = "thirdapp.gjp")
public class MaidProperties {
    private String enable;

    private String key;

    private String secret;

    private String host;

    private String apiHost;

    private String dbName;

    private String userId;

    private Integer cacheTimeout;

}
