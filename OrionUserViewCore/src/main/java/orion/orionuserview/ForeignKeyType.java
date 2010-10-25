/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.orionuserview;

/**
 * Типы внешних ключей
 * @author sl
 */
public enum ForeignKeyType {

    /**
     * Таблица связана с родительской таблицей связью один к одному.
     * Внешний ключ описан в этой таблице
     */
    ONE_TO_ONE_FK,
    /**
     * Таблица связана с родительской таблицей связью один к одному.
     * Внешний ключ описан в родительской таблице
     */
    ONE_TO_ONE_PK,
    /**
     * Таблица связана с родительской таблицей связью один ко многим.
     */
    ONE_TO_MANY,
    /**
     * Таблица связана с родительской таблицей связью многие ко многим.
     */
    MANY_TO_ONE;
}
