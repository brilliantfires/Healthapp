package com.example.healthapp.data

enum class MediaType {
    IMAGE, VIDEO
}

enum class BloodType {
    A, B, AB, O, UNKNOWN
}

enum class SkinType {
    TYPE_I, TYPE_II, TYPE_III, TYPE_IV, TYPE_V, TYPE_VI
}

enum class Gender {
    MALE, FEMALE, OTHER
}

enum class UserRole {
    USER, ADMIN
}

enum class Category {
    FITNESS, LIFESTYLE, HEALTH, DIET
}

//enum class MyColor {
//    Red, Green, Blue, Magenta, Yellow, Cyan, Orange, Purple, Pink, Teal, Brown, Gray, Black, White
//}

enum class AdminSection(val displayName: String) {
    USERS("Users"),
    ARTICLES("Articles"),
    AUTHORS("Authors"),
    DISPLAY_CARDS("Display Cards");

    // 可以在这里添加方法或扩展属性，例如获取对应视图的标题或描述等
    fun getTitle(): String {
        return when (this) {
            ARTICLES -> "文章列表"
            AUTHORS -> "作者列表"
            DISPLAY_CARDS -> "展示卡片"
            USERS -> "用户列表"
        }
    }
}
