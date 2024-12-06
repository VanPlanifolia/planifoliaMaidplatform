package van.planifolia.maid.core.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import van.planifolia.maid.core.annotations.IgnoreParma;
import van.planifolia.maid.core.annotations.IgnoreSing;


/**
 * @Description:
 * @Author: Planifolia.Van
 * @Date: 2024/12/3 17:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetPlatformApiListModel extends MaidBasicModel{
    /**
     * 方法名称
     */
    @IgnoreSing
    private String MethodName;

    /**
     * 加签时间戳
     */
    private String InvalidTime;
    /**
     * 随机字符串
     */
    private String RandamStr;
    /**
     * 加签后字符串
     */
    @IgnoreSing
    private String SignStr;
    /**
     * 必要的APP
     */
    private String AppKey;
    /**
     * DBName
     */
    @IgnoreSing
    private String DbName;
    /**
     * 用户ID
     */
    private String UserId;

    @IgnoreSing
    @IgnoreParma
    private String endStr;

}
