package com.asterlsker.housepit.core.converter

import com.asterlsker.housepit.core.enums.OAuth2Provider
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.GenericConverter
import java.lang.reflect.Method


object GenericEnumConverter : GenericConverter {

    override fun getConvertibleTypes(): MutableSet<GenericConverter.ConvertiblePair> {
        val convertiblePair = GenericConverter.ConvertiblePair(String::class.java, Enum::class.java)
        return mutableSetOf(convertiblePair)
    }

    private val valueOf: Method = Enum::class.java.getDeclaredMethod("valueOf", Class::class.java, String::class.java)

    override fun convert(
        source: Any?,
        sourceType: TypeDescriptor,
        targetType: TypeDescriptor,
    ): Any? {
        return source?.let { it as? String }
            ?.let { convert(it, targetType.type) }
    }

    private fun <T> convert(
        name: String,
        type: Class<T>,
    ): T? =
        try {
            @Suppress("UNCHECKED_CAST")
            valueOf.invoke(null, type, name.trim().uppercase()) as T
        } catch (e: Throwable) {
            null
        }
}