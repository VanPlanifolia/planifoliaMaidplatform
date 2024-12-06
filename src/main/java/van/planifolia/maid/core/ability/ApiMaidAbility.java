package van.planifolia.maid.core.ability;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
