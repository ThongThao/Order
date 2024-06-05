import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.order.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignInViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user
    fun signIn(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    currentUser?.let { user ->
                        // Retrieve user data from Firestore to check role
                        firestore.collection("users")
                            .document(user.uid)
                            .get()
                            .addOnSuccessListener { document ->
                                val role = document.getString("role")
                                if (role == "Customer") {
                                    val userData = User(
                                        id = user.uid,
                                        fullName = document.getString("fullName"),
                                        phoneNumber = document.getString("phoneNumber"),
                                        email = user.email ?: "",
                                        address = document.getString("address"),
                                        sex = document.getString("sex"),
                                        image = document.getString("image"),
                                        role = role ?: ""
                                    )
                                    _user.value = userData
                                    onSuccess()
                                } else {
                                    onError("Bạn không có quyền truy cập")
                                }
                                
                            }
                            .addOnFailureListener { e ->
                                onError(e.message ?: "Failed to retrieve user data")
                            }
                    }
                } else {
                    onError(task.exception?.message ?: "Đăng nhập thất bại")
                }
            }
    }
}
