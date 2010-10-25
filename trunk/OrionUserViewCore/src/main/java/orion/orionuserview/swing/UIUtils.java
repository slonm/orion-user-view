package orion.orionuserview.swing;

import orion.orionuserview.AttributeDef;
import orion.orionuserview.ForeignKeyDef;
import orion.orionuserview.ForeignKeyType;
import orion.orionuserview.Relation;
import orion.orionuserview.RelationDef;

/**
 *
 * @author sl
 */
public class UIUtils {

    private UIUtils() {
    }

    public static String relationPresentableNameForAdmin(Globals globals, Relation relation) {
        if (relation == null) {
            return null;
        }
        String retName = relation.getName();
        if (relation instanceof RelationDef) {
            RelationDef rd = (RelationDef) relation;

            retName = (rd.getCatalog() != null ? rd.getCatalog() + "." : "")
                    + (rd.getSchema() != null ? rd.getSchema() + "." : "") + retName;
        }
        if (relation.getRemarks() == null) {
            return retName;
        }
        String ret = globals.getParametr(String.class, "AttributePresentationTemplateForAdmin").toLowerCase();
        ret = ret.replace("{name}", retName);
        ret = ret.replace("{remarks}", getRemarks(relation.getRemarks()));
        return ret;
    }

    public static String attributeDefPresentableNameForAdmin(Globals globals, AttributeDef attributeDef) {
        if (attributeDef == null) {
            return null;
        }
        String retName = attributeDef.getName();
        if (attributeDef instanceof RelationDef) {
            RelationDef rd = (RelationDef) attributeDef;

            retName = (rd.getCatalog() != null ? rd.getCatalog() + "." : "")
                    + (rd.getSchema() != null ? rd.getSchema() + "." : "") + retName;
//            if (rd instanceof ForeignKeyDef) {
//                String pkAttrs = "", fkAttrs = "";
//                ForeignKeyDef fkd = (ForeignKeyDef) rd;
//                for (String pkAttr : fkd.getPkOnFk().keySet()) {
//                    pkAttrs = (pkAttrs.length() != 0 ? pkAttrs + ", " : "") + pkAttr;
//                    fkAttrs = (fkAttrs.length() != 0 ? fkAttrs + ", " : "") + fkd.getPkOnFk().get(pkAttr);
//                }
//                String leftAttrs = fkAttrs;
//                String rightAttrs = pkAttrs;
//                String arrow=" --> ";
//                boolean nameAtLeft = false;
//                if (fkd.getForeignKeyType() == ForeignKeyType.ONE_TO_MANY||
//                    fkd.getForeignKeyType() == ForeignKeyType.ONE_TO_ONE_FK) {
//                    nameAtLeft = true;
//                }else if (fkd.getForeignKeyType() == ForeignKeyType.ONE_TO_ONE_PK) {
//                    nameAtLeft = true;
//                    leftAttrs = pkAttrs;
//                    rightAttrs = fkAttrs;
//                    arrow = " <-- ";
//                }
//
//                retName = (nameAtLeft ? retName + " " : "") + "{" + leftAttrs + "}" + arrow + (!nameAtLeft ? retName + " " : "") + "{" + rightAttrs + "}";
//            }
        }
        if (attributeDef.getRemarks() == null) {
            return retName;
        }
        String ret = globals.getParametr(String.class, "AttributePresentationTemplateForAdmin").toLowerCase();
        ret = ret.replace("{name}", retName);
        ret = ret.replace("{remarks}", getRemarks(attributeDef.getRemarks()));
        return ret;
    }

    private static String getRemarks(String remarks){
        String[] s=remarks.split("\\|");
        if(s.length>=3){
            return s[2];
        }else{
            return remarks;
        }
    }
}
