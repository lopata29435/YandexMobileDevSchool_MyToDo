package com.example.mytodolist.data.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.annotations.SerializedName

enum class Importance {
    low,
    basic,
    important
}

class Converters {
    @TypeConverter
    fun fromImportance(importance: Importance?): String? {
        return importance?.name
    }

    @TypeConverter
    fun toImportance(name: String?): Importance? {
        return name?.let { Importance.valueOf(it) }
    }
}

@Entity(tableName = "todo_items")
data class TodoItem(
    @PrimaryKey @SerializedName("id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("importance") val importance: String,
    @SerializedName("deadline") val deadline: Long?,
    @SerializedName("done") val done: Boolean,
    @SerializedName("color") val color: String?,
    @SerializedName("created_at") val created_at: Long,
    @SerializedName("changed_at") val changed_at: Long,
    @SerializedName("last_updated_by") val last_updated_by: String
)

data class TodoResponse(
    val status: String,
    val list: List<TodoItem>,
    val revision: Int
)
