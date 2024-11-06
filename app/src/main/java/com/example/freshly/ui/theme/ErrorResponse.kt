import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response

data class ErrorResponse(
    val message: String?
)
fun parseErrorResponse(response: Response<*>): String {
    return try {
        val gson = Gson()
        val type = object : TypeToken<ErrorResponse>() {}.type
        val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()?.charStream(), type)
        errorResponse?.message ?: "Unknown error"
    } catch (e: Exception) {
        "An error occurred"
    }
}
