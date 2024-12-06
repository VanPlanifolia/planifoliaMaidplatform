package van.planifolia.maid.core.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import van.planifolia.maid.core.annotations.IgnoreParma;
import van.planifolia.maid.core.annotations.IgnoreSing;


/**
 * @Description:
 * @Author: Planifolia.Van
 * @Date: 2024/12/4 09:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SingQueryBaseModel extends MaidBasicModel{
    /**
     * 接口名 固定值为：GraspCMServerApi.dll
     */
    private String managename;
    /**
     * 数据库名称
     */
    private String dbname;
    /**
     * 查询能力
     */
    private String paramkey;
    /**
     * 查询参数JSON
     */
    private String paramjson;
    /**
     * token
     */
    private String apiparam;
    /**
     * 接口类型 传query
     */
    private String apitype;
    /**
     * 签名后字符串
     */
    @IgnoreSing
    private String sign;

    /**
     * 云平台手机号 获取API地址返回的GraspCloudMobile，为空时传0
     */
    private String mobile;
    /**
     * 云平台ServerID 获取API地址返回的GraspCloudServerId，为空时传0
     */
    private String serviceid;
    /**
     * 该参数始终为1
     */
    private String interiorapi;

    @IgnoreSing
    @IgnoreParma
    private String endStr;



}
