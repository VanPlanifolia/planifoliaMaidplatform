package van.planifolia.maid.core.dto;

import lombok.Data;

/**
 * @Description:
 * @Author: Planifolia.Van
 * @Date: 2024/12/4 16:26
 */
@Data
public class MaidPlatformParmaDTO {
    /**
     * 主机host
     */
    private String apiServerAddress;
    /**
     * 手机号
     */
    private String graspCloudMobile;
    /**
     * serverId
     */
    private String graspCloudServerId;
    /**
     * token
     */
    private String apiParam;
    /**
     * 加签Key
     */
    private String signKey;
}
