package ru.spbstu.bug_tracker.extensions

fun <T> String.isEnumValue(clazz: Class<T>): Boolean where T : Enum<T> {
    return clazz.enumConstants.map { it.name }.contains(this)
}

inline fun <reified T> String.isEnumValue(): Boolean where T : Enum<T> {
    return isEnumValue(T::class.java)
}