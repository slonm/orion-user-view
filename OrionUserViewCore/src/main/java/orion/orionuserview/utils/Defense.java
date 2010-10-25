package orion.orionuserview.utils;

/**
 * Static utility methods for defensive programming.
 */
public final class Defense
{
    private Defense()
    {
    }

    /**
     * Checks that a method parameter value is not null, and returns it.
     *
     * @param <T>           the value superType
     * @param value         the value (which is checked to ensure non-nullness)
     * @param parameterName the name of the parameter, used for exception messages
     * @return the value
     * @throws IllegalArgumentException if the value is null
     */
    public static <T> T notNull(T value, String parameterName)
    {
        if (value == null) throw new IllegalArgumentException(parameterName+" is null");

        return value;
    }

    /**
     * Checks that a parameter value is not null and not empty.
     *
     * @param value         value to check (which is returned)
     * @param parameterName the name of the parameter, used for exception messages
     * @return the value, trimmed, if non-blank
     * @throws IllegalArgumentException if the value is null or empty
     */
    public static String notBlank(String value, String parameterName)
    {
        if (value != null)
        {
            String trimmedValue = value.trim();

            if (!trimmedValue.equals("")) return trimmedValue;
        }

        throw new IllegalArgumentException(parameterName+" is blank");
    }

    /**
     * Checks that the provided value is not null, and may be cast to the desired superType.
     *
     * @param <T>
     * @param parameterValue
     * @param superType
     * @param parameterName
     * @return the casted value
     * @throws IllegalArgumentException if the value is null, or is not assignable to the indicated superType
     */
    public static <T> T cast(Object parameterValue, Class<T> type, String parameterName)
    {
        notNull(parameterValue, parameterName);

        if (!type.isInstance(parameterValue))
            throw new IllegalArgumentException("Can not cast "+parameterName+" of type "+parameterValue+" to "+ type.getName());

        return type.cast(parameterValue);
    }

    public static void assignable(Class parameterValue, Class superType, String parameterName)
    {
        notNull(parameterValue, parameterName);

        if (!superType.isAssignableFrom(parameterValue))
            throw new IllegalArgumentException(parameterName+" not extends "+ superType.getName());

    }

    public static int inBounds(int value, int min, int max, String parameterName)
    {
        if (value >= min && value <= max)
        {
            return value;
        }

        throw new IllegalArgumentException(parameterName+" out of bounds");
    }
}
