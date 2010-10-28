package orion.orionuserview.internal;

import orion.orionuserview.Relation;


/**
 *
 * @author sl
 */
//                       TABLE_CAT|                     TABLE_SCHEM|                      TABLE_NAME|                     COLUMN_NAME|                       DATA_TYPE|                       TYPE_NAME|                     COLUMN_SIZE|                   BUFFER_LENGTH|                  DECIMAL_DIGITS|                  NUM_PREC_RADIX|                        NULLABLE|                         REMARKS|                      COLUMN_DEF|                   SQL_DATA_TYPE|                SQL_DATETIME_SUB|               CHAR_OCTET_LENGTH|                ORDINAL_POSITION|                     IS_NULLABLE|
class Column implements Comparable<Column>{

    public String name;
    public Relation relation;
    public String remarks;
    public int type;
    public int size;
    public int decimalDigits;
    public boolean nullable;
    public int position;

    @Override
    public int compareTo(Column o) {
	return (position<o.position ? -1 : (position==o.position ? 0 : 1));
    }

    @Override
    public String toString() {
        return relation+"."+name;
    }
}
