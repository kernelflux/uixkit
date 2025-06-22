package com.kernelflux.uixkit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * [AutoBindingDelegate] is an abstract base class that automatically infers and instantiates [ViewBinding].
 * Subclasses only need to inherit from this base class and specify the data type [T] and ViewBinding type [VB],
 * without manually handling ViewBinding inflation.
 *
 * Note: To enable automatic ViewBinding inference, this class uses Java's generic reflection.
 * Make sure your delegate subclass directly extends this base class with concrete type arguments.
 * Indirect inheritance through multiple abstract layers may cause type information to be lost,
 * preventing correct inference.
 */
abstract class AutoBindingDelegate<T : Any, VB : ViewBinding> : ItemDelegate<T, VB> {

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewBinding(parent: ViewGroup): VB {
        val vbClass = findViewBindingClass(this::class.java)
            ?: throw IllegalStateException(
                "Unable to automatically infer the ViewBinding type. Please ensure that your class directly extends AutoBindingDelegate with concrete type arguments."
            )
        val method = vbClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        return method.invoke(null, LayoutInflater.from(parent.context), parent, false) as VB
    }

    /**
     * Recursively finds the class object of the ViewBinding generic parameter
     * in the class hierarchy. This helps resolve type erasure issues in reflection
     * and supports one or more levels of inheritance.
     */
    private fun findViewBindingClass(clazz: Class<*>): Class<VB>? {
        var current: Class<*>? = clazz
        while (current != null && current != Any::class.java) {
            val type = current.genericSuperclass
            if (type is ParameterizedType) {
                val args = type.actualTypeArguments
                if (args.size >= 2 && args[1] is Class<*>) {
                    @Suppress("UNCHECKED_CAST")
                    return args[1] as Class<VB>
                }
            }
            current = current.superclass
        }
        return null
    }
}