/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orion.orionuserview;

/**
 *
 * @author user
 */
class InvalidDatabaseDefFormatException extends Exception {
    /**
     * Constructs an InvalidPropertiesFormatException with the specified
     * cause.
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link Throwable#getCause()} method).
     */
    public InvalidDatabaseDefFormatException(Throwable cause) {
        super(cause==null ? null : cause.toString());
        this.initCause(cause);
    }

   /**
    * Constructs an InvalidPropertiesFormatException with the specified
    * detail message.
    *
    * @param   message   the detail message. The detail message is saved for
    *          later retrieval by the {@link Throwable#getMessage()} method.
    */
    public InvalidDatabaseDefFormatException(String message) {
        super(message);
    }

}
