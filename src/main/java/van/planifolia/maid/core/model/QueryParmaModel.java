package van.planifolia.maid.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description:
 * @Author: Planifolia.Van
 * @Date: 2024/12/4 09:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
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
