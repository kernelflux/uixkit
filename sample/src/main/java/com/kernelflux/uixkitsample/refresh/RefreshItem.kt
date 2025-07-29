package com.kernelflux.uixkitsample.refresh

data class RefreshItem(
    val id: String = System.currentTimeMillis().toString() + "_" + (0..9999).random(), // 唯一标识符
    val imgUrl: String,
    val text: String,
    val type: Int = 0
) {
    /**
     * 自定义 equals 方法，用于 DiffUtil 比较
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RefreshItem

        if (id != other.id) return false
        if (type != other.type) return false
        if (text != other.text) return false
        if (imgUrl != other.imgUrl) return false

        return true
    }

    /**
     * 自定义 hashCode 方法
     */
    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + type
        result = 31 * result + text.hashCode()
        result = 31 * result + imgUrl.hashCode()
        return result
    }
}