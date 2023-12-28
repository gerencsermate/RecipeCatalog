package hu.bme.aut.android.recipeapp.data

import android.graphics.drawable.Icon
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "Recipe")
data class Recipe(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "category") var category: Category,
    @ColumnInfo(name = "prepTime") var prepTime: Int,
    @ColumnInfo(name = "difficulty") var difficulty: Int,
    @ColumnInfo(name = "steps") var steps: String,
    @ColumnInfo(name = "picture") var picture: Uri? = null
)




