package io.androidalatan.json.moshi.adapter.internal

import com.squareup.moshi.Types
import java.lang.reflect.Type

object TypeUtil {
    fun getRawType(type: Type): Type {
        var clazz = Types.getRawType(type)

        if (clazz.isAnonymousClass && clazz.enclosingClass != null) {
            clazz = clazz.enclosingClass
        }
        return clazz
    }

}