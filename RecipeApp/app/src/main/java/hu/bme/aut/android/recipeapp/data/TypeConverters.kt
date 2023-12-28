package hu.bme.aut.android.recipeapp.data

import android.net.Uri
import androidx.room.TypeConverter

class TypeConverters {
    companion object{
        @JvmStatic
        @TypeConverter
        fun getByOrdinal(ordinal: Int): Category?{
            var ret: Category? = null
            for(cat in Category.values()){
                if(cat.ordinal == ordinal){
                    ret = cat
                    break
                }
            }
            return ret
        }

        @JvmStatic
        @TypeConverter
        fun toInt(category: Category) : Int{
            return category.ordinal
        }

        @JvmStatic
        @TypeConverter
        fun getFromString(str: String): Uri?{
            if(str.isNullOrEmpty()){
                return null
            }
            return Uri.parse(str)
        }

        @JvmStatic
        @TypeConverter
        fun toString(uri: Uri?) : String{
            return uri?.toString() ?: ""
        }
    }
}