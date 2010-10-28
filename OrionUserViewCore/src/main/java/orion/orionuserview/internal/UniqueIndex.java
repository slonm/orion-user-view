package orion.orionuserview.internal;

import java.util.HashSet;
import java.util.Set;
import orion.orionuserview.Relation;

/**
 *
 * @author sl
 */
//                      TABLE_CAT|                     TABLE_SCHEM|                      TABLE_NAME|                      NON_UNIQUE|                 INDEX_QUALIFIER|                      INDEX_NAME|                            TYPE|                ORDINAL_POSITION|                     COLUMN_NAME|                     ASC_OR_DESC|                     CARDINALITY|                           PAGES|                FILTER_CONDITION|
public class UniqueIndex {

    public Relation relation;
    public String name;
    public boolean isPrimaryKey=false;
    public Set<String> columns = new HashSet<String>();

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(relation);
        sb.append(" (");
        boolean firstPass=true;
        for(String s:columns){
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
