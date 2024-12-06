## 该SDK用于对接管家婆进销存管理平台

***该SDK开箱即用，需要在引入的SpringBoot项目application文件中做以下配置***

```yaml
thirdApp:
  gjp:
    enabled: true/false 是否启用该SDK
    key: 管家婆Key
    secret: 管家婆密钥
    host: 管家婆GatewayHost
    dbName: 管家婆DBName
    userId: 管家婆授权UserId
    cacheTimeout: 管家婆Token超时时间 推荐 23*60*60 秒
```
配置完毕之后注入`MaidPlatformUtil`工具类即可直接使用，其中可通过方法 `getMaidQueryData(queryParmaModel,apiMaidAbility)` 进行单据查询操作。其中`queryParmaModel` 为通用查询参数，`apiMaidAbility`为Api查询能力。
**查询参数QueryParmaModel实例**

```java
public class QueryParmaModel extends MaidBasicModel{
    /**
     * 分页大小
     */
    private String PageSize;
    /**
     * 分页索引
     */
    private String PageIndex;
    /**
     * 表单类型，无需传递
     */
    private String VchType;
    /**
     * 开始时间格式 yyyy-MM-dd
     */
    private String BeginDate;
    /**
     * 结束时间格式 yyyy-MM-dd
     */
    private String EndDate;
}
```
***PS：别问为什么不是驼峰，因为管家婆的查询参数就是这样的*。**

**Api能力示例**

~~~java
public enum ApiMaidAbility {


    DLY_ORDER_DATA("DlyOrderData","采购订单查询","150"),
    DLY_SALE_DATA("DlySaleData","销售单查询","11"),
    DLY_SALE_DATA_RETURN("DlySaleData","销售退货单查询","45"),
    DLY_OTHER_DATA_DAMAGE("DlyOtherData","报损单查询","9"),
    DLY_OTHER_DATA_OVERFLOW("DlyOtherData","报溢单查询","14"),
    DLY_STOCK_DATA_OUT("DlyStockData","出库单查询","47"),
    DLY_PTYPE("ptype","销售单查询","11"),

    ;

    private String paramKey;
    private String name;
    private String vchType;

}

~~~

End：该SDK目前仅适配了**管家婆-API能力-单据查询**，使用者有其他需求可自行拉取提交PR。生活愉快~🥰
