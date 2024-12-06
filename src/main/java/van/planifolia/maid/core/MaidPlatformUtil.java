package van.planifolia.maid.core;

import com.alibaba.fastjson2.JSONObject;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import van.planifolia.maid.configuration.MaidProperties;
import van.planifolia.maid.core.ability.ApiMaidAbility;
import van.planifolia.maid.core.ability.GateWayMaidAbility;
import van.planifolia.maid.core.dto.MaidPlatformParmaDTO;
import van.planifolia.maid.core.model.GetPlatformApiListModel;
import van.planifolia.maid.core.model.GetSingKeyModel;
import van.planifolia.maid.core.model.QueryParmaModel;
import van.planifolia.maid.core.model.SingQueryBaseModel;
import van.planifolia.maid.core.model.builder.MaidModelBuilder;
import van.planifolia.maid.util.http.HttpClientUtil;
import van.planifolia.maid.util.statics.MkStatic;
import van.planifolia.maid.util.str.ConsoleColors;
import van.planifolia.maid.util.str.Strings;
import van.planifolia.maid.util.time.DateTimeUtil;

import java.util.Map;

/**
 * @Description:
 * @Author: Planifolia.Van
 * @Date: 2024/12/3 14:20
 */
@Slf4j
public class MaidPlatformUtil {

    private final MaidProperties maidProperties;

    private final MaidModelBuilder maidModelBuilder;

    private final Cache<Object,Object> cacheUtil;

    /**
     * 构造器注入
     * @param maidProperties 管家婆配置
     * @param maidModelBuilder maidModelBuilder bean
     */
    public MaidPlatformUtil(MaidModelBuilder maidModelBuilder,MaidProperties maidProperties,Cache<Object,Object> cacheUtil) {
        this.maidModelBuilder = maidModelBuilder;
        this.maidProperties=maidProperties;
        this.cacheUtil=cacheUtil;
    }

    /**
     * 获取加密Key，默认从Redis中查询，当查询不到的时候去构建SingKey
     * @return 查询到的SingKey
     */
    private String getSingKey(){
        GetSingKeyModel getSingKeyModel = maidModelBuilder.singBaseModelBuilder();
        getSingKeyModel.setMethodName(GateWayMaidAbility.GET_SING_KEY.getAbility());
        getSingKeyModel.setInvalidTime(DateTimeUtil.getTimeStr(null,DateTimeUtil.parseDateStr(getSingKeyModel.getInvalidTime(),DateTimeUtil.YYYYMMDDHHMMSS)));
        JSONObject requestJSON = getSingKeyModel.toRequestJSON();
        return HttpClientUtil.doOkHttpPost(maidProperties.getHost() + GateWayMaidAbility.GET_SING_KEY.getUri(), requestJSON, null);
    }

    /**
     * 获取平台API列表信息，服务于获取Token
     * @return 获取结果
     */
    private String getPlatformApiList(){
        GetPlatformApiListModel getPlatformApiListModel = maidModelBuilder.apiBaseModelBuilder();
        getPlatformApiListModel.setMethodName(GateWayMaidAbility.GET_API_URL.getAbility());
        getPlatformApiListModel.setInvalidTime(DateTimeUtil.getTimeStr(null,DateTimeUtil.parseDateStr(getPlatformApiListModel.getInvalidTime(),DateTimeUtil.YYYYMMDDHHMMSS)));
        JSONObject requestJSON = getPlatformApiListModel.toRequestJSON();
        return  HttpClientUtil.doOkHttpPost(maidProperties.getHost() + GateWayMaidAbility.GET_API_URL.getUri(), requestJSON, null);
    }

    /**
     * 获取管家婆平台的APIToken信息，有限查询缓存
     * @return 获取到的Token信息
     */
    private MaidPlatformParmaDTO getMaidParma(){
        Object tokenCache = cacheUtil.get(MkStatic.MAID_PLATFORM_PARMA, e->null);
        if (tokenCache instanceof MaidPlatformParmaDTO){
            log.info(Strings.toColor("管家婆token尚未失效！", ConsoleColors.GREEN));
            return (MaidPlatformParmaDTO) tokenCache;
        }
        log.info(Strings.toColor("管家婆token已失效，尝试重新获取！", ConsoleColors.RED));

        String platformApiJSON = getPlatformApiList();
        String singKeyStr = getSingKey();
        JSONObject singKeyJSON = JSONObject.parseObject(singKeyStr);
        log.info(Strings.toColor("管家婆平台获取SingKey信息返回值：\n{}",ConsoleColors.GREEN),singKeyJSON);

        String singKeyRetMsg = singKeyJSON.getString("RetMsg");
        String signKey = JSONObject.parseObject(singKeyRetMsg).getString("SignKey");

        JSONObject apiTokenJSONObject = JSONObject.parseObject(platformApiJSON);
        log.info(Strings.toColor("管家婆平台获取Token信息返回值：\n{}",ConsoleColors.GREEN),apiTokenJSONObject);
        Object RetCode = apiTokenJSONObject.get("RetCode");
        if (!MkStatic.MAID_PLATFORM_SUCCESS_CODE.equals(RetCode)){
            throw new RuntimeException("获取管家婆平台通用数据失败");
        }
        String retMsg = apiTokenJSONObject.getString("RetMsg");
        JSONObject retMsgObject = JSONObject.parseObject(retMsg);
        String apiParam = retMsgObject.getString("ApiParam");
        String apiServerAddress = retMsgObject.getString("ApiServerAddress");
        String graspCloudMobile = retMsgObject.getString("GraspCloudMobile");
        String graspCloudServerId = retMsgObject.getString("GraspCloudServerId");
        MaidPlatformParmaDTO maidPlatformParmaDTO = new MaidPlatformParmaDTO();
        maidPlatformParmaDTO.setApiParam(apiParam);
        maidPlatformParmaDTO.setApiServerAddress(apiServerAddress);
        maidPlatformParmaDTO.setGraspCloudMobile(Strings.isBlank(graspCloudMobile)?MkStatic.ZERO_STR:graspCloudMobile);
        maidPlatformParmaDTO.setGraspCloudServerId(Strings.isBlank(graspCloudServerId)?MkStatic.ZERO_STR:graspCloudMobile);
        maidPlatformParmaDTO.setSignKey(signKey);
        cacheUtil.put(MkStatic.MAID_PLATFORM_PARMA,maidPlatformParmaDTO);
        return maidPlatformParmaDTO;
    }



    /**
     * 管家婆的单据通用查询结果
     * @param queryParmaModel 查询参数
     * @param apiMaidAbility 查询能力
     * @return 查询结果
     */
    public String getMaidQueryData(QueryParmaModel queryParmaModel, ApiMaidAbility apiMaidAbility){
        queryParmaModel.setVchType(apiMaidAbility.getVchType());
        JSONObject requestJSON = queryParmaModel.toRequestJSON();
        SingQueryBaseModel singQueryBaseModel = maidModelBuilder.queryBaseBuilder(requestJSON, apiMaidAbility,getMaidParma());
        Map<String, String> requestMap = singQueryBaseModel.toRequestMap();
        return HttpClientUtil.doOkHttpPost(maidProperties.getApiHost(), requestMap, null);
    }


}
