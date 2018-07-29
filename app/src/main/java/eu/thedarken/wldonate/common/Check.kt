package eu.thedarken.wldonate.common


object Check {
    /**
     * Ensures that an object reference passed as a parameter to the calling
     * method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if `reference` is null
     */
    fun <T> notNull(reference: T?): T {
        if (reference == null) throw NullPointerException()
        return reference
    }
}
