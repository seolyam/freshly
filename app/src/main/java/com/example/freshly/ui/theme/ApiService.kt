import com.example.freshly.ui.theme.LoginRequest
import com.example.freshly.ui.theme.LoginResponse
import com.example.freshly.ui.theme.RegisterRequest
import com.example.freshly.ui.theme.RegisterResponse
import com.example.freshly.ui.theme.UserProfileRequest
import com.example.freshly.ui.theme.UserProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT


interface ApiService {
    @POST("/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<UserProfileResponse>

    @PUT("/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UserProfileRequest
    ): Response<UserProfileResponse>
}
