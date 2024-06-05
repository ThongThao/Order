import org.mindrot.jbcrypt.BCrypt

object PasswordHash {

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun verifyPassword(plainTextPassword: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(plainTextPassword, hashedPassword)
    }
}
