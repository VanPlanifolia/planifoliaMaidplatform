package van.planifolia.maid.core.model.builder;


import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import van.planifolia.maid.configuration.MaidProperties;
import van.planifolia.maid.core.ability.ApiMaidAbility;
import van.planifolia.maid.core.dto.MaidPlatformParmaDTO;
import van.planifolia.maid.core.model.GetPlatformApiListModel;
import van.planifolia.maid.core.model.GetSingKeyModel;
import van.planifolia.maid.core.model.SingQueryBaseModel;
import van.planifolia.maid.core.singer.MaidPlatformSingUtil;
import van.planifolia.maid.util.time.DateTimeUtil;


import java.util.Date;
import java.util.UUID;

/**
 * @Description: 管家婆的请求参数构建器，它不仅可以根据传入参数与能力去构建请求Model并且会自动加签封装成可以直接请求的查询Model
 * @Author: Planifolia.Van
 * @Date: 2024/12/5 15:15
 */
@Slf4j
public class MaidModelBuilder {


    private final MaidProperties maidProperties;

    /**
     * 构造器注入
     * @param maidProperties 管家婆平台配置
     */
    public MaidModelBuilder(MaidProperties maidProperties) {
        this.maidProperties = maidProperties;
    }


    /**
     * 构建获取SingKey的请求Model
     * @return 构建的SingKeyModel
     */
    public GetSingKeyModel singBaseModelBuilder(){
        GetSingKeyModel getSingKeyModel = new GetSingKeyModel();
        getSingKeyModel.setAppKey(maidProperties.getKey());
        getSingKeyModel.setRandamStr(UUID.randomUUID().toString());
        getSingKeyModel.setInvalidTime(DateTimeUtil.getTimeStr(DateTimeUtil.YYYYMMDDHHMMSS,new Date()));
        getSingKeyModel.setEndStr(maidProperties.getSecret());
        String notSingStr = MaidPlatformSingUtil.commSinger(getSingKeyModel);
        String singStr = MaidPlatformSingUtil.doMd5(notSingStr);
        getSingKeyModel.setSignStr(singStr);
        return getSingKeyModel;
    }

    /**
     * 构建获取平台Api列表的请求Model
     * @return 构建结果
     */
    public GetPlatformApiListModel apiBaseModelBuilder(){
        GetPlatformApiListModel getPlatformApiListModel = new GetPlatformApiListModel();
        getPlatformApiListModel.setDbName(maidProperties.getDbName());
        getPlatformApiListModel.setUserId(maidProperties.getUserId());
        getPlatformApiListModel.setAppKey(maidProperties.getKey());
        getPlatformApiListModel.setRandamStr(UUID.randomUUID().toString());
        getPlatformApiListModel.setInvalidTime(DateTimeUtil.getTimeStr(DateTimeUtil.YYYYMMDDHHMMSS,new Date()));
        getPlatformApiListModel.setEndStr(maidProperties.getSecret());
        String notSingStr = MaidPlatformSingUtil.commSinger(getPlatformApiListModel);
        String singStr = MaidPlatformSingUtil.doMd5(notSingStr);
        getPlatformApiListModel.setSignStr(singStr);
        return getPlatformApiListModel;
    }


    /**
     * 构建Api查询Model
     * @param requestJSON 查询参数
     * @param apiMaidAbility API能力
     * @param maidParma 管家婆系统参数
     * @return 构建结果
     */
    public SingQueryBaseModel queryBaseBuilder(JSONObject requestJSON, ApiMaidAbility apiMaidAbility, MaidPlatformParmaDTO maidParma){
        SingQueryBaseModel singQueryBaseModel = new SingQueryBaseModel();
        singQueryBaseModel.setManagename("GraspCMServerApi.dll");
        singQueryBaseModel.setMobile(maidParma.getGraspCloudMobile());
        singQueryBaseModel.setServiceid(maidParma.getGraspCloudServerId());
        singQueryBaseModel.setInteriorapi("1");
        singQueryBaseModel.setDbname(maidProperties.getDbName());
        singQueryBaseModel.setApitype("query");
        singQueryBaseModel.setApiparam(maidParma.getApiParam());
        singQueryBaseModel.setEndStr(maidParma.getSignKey());
        singQueryBaseModel.setParamkey(apiMaidAbility.getParamKey());
        singQueryBaseModel.setParamjson(requestJSON.toJSONString());
        String notSingStr = MaidPlatformSingUtil.commSinger(singQueryBaseModel);
        String singStr = MaidPlatformSingUtil.doMd5(notSingStr);
        singQueryBaseModel.setSign(singStr);
        return singQueryBaseModel;
    }


}
