package orion.orionuserview.internal;

import java.util.HashMap;
import java.util.Map;
import orion.orionuserview.Relation;

/**
 *
 * @author sl
 */
//                     PKTABLE_CAT|                   PKTABLE_SCHEM|                    PKTABLE_NAME|                   PKCOLUMN_NAME|                     FKTABLE_CAT|                   FKTABLE_SCHEM|                    FKTABLE_NAME|                   FKCOLUMN_NAME|                         KEY_SEQ|                     UPDATE_RULE|                     DELETE_RULE|                         FK_NAME|                         PK_NAME|                   DEFERRABILITY|
public class CrossReference{
    public Relation pkRelation;
    public String pkName;
    public Relation fkRelation;
    public Map<String, String> pkOnFk=new HashMap<String, String>();
    public String fkName;
    public boolean cascadeDelete=false;
    
    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(fkRelation);
        sb.append(" (");
        boolean firstPass=true;
        for(String s:pkOnFk.values()){
            if(firstPass){
               firstPass=false;
            }else{
                sb.append(", ");
            }
            sb.append(s);
        }
        sb.append(") ==> ");
        sb.append(pkRelation);
        sb.append(" (");
        firstPass=true;
        for(String s:pkOnFk.keySet()){
            if(firstPass){
               firstPass=false;
            }else{
                sb.append(", ");
            }
            sb.append(s);
        }
        sb.append(")");
        return sb.toString();
    }
}
