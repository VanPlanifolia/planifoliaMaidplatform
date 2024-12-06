package van.planifolia.maid.core.ability;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum GateWayMaidAbility {



    GET_SING_KEY("获取SingKey","getsignstr","/CMGraspApi/GateWay"),
    GET_API_URL("获取ApiList","getcustomerapiurl","/CMGraspApi/GateWay"),
    ;
    @Getter
    private String name;
    private String ability;
    @Getter
    private String uri;

    /**
     * 获取能力完整URL
     * @return 获取结果
     */
    public String getAbility(){
        return "graspcm.cmapi."+ability;
    }

    /**
     * 在查询的时候取获取能力Code
     * @return 获取到的能力Code
     */
    public String getAbilityCode() {

        return ability;
    }
}
