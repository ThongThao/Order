import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.order.model.Order
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CartViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _carts = MutableStateFlow<Map<String, RestaurantCart>>(emptyMap())
    val carts: StateFlow<Map<String, RestaurantCart>> = _carts

    // Method to add or update an item in the restaurant cart
    fun addToCart(restaurantCart: RestaurantCart) {
        val currentCarts = _carts.value.toMutableMap()
        val existingRestaurantCart = currentCarts[restaurantCart.restaurantName]

        if (existingRestaurantCart == null) {
            // Restaurant cart does not exist, add it as new
            currentCarts[restaurantCart.restaurantName] = restaurantCart
        } else {
            // Restaurant cart exists, update the items
            val updatedItems = existingRestaurantCart.items!!.toMutableList()
            restaurantCart.items!!.forEach { newItem ->
                val itemIndex = updatedItems.indexOfFirst { it?.id == newItem?.id }
                if (itemIndex == -1) {
                    // Item does not exist, add it
                    updatedItems.add(newItem)
                } else {
                    // Item exists, update it
                    updatedItems[itemIndex] = newItem
                }
            }

            // Calculate the updated total
            val updatedTotal = updatedItems.filterNotNull().sumBy { (it.price ?: 0) * (it.quantity ?: 0) }

            // Create updated RestaurantCart
            val updatedRestaurantCart = existingRestaurantCart.copy(items = updatedItems, total = updatedTotal)
            currentCarts[restaurantCart.restaurantName] = updatedRestaurantCart
        }

        // Update the cart state
        _carts.value = currentCarts
    }

    // Method to save or update the entire cart in Firestore
    fun saveCartToFirestore(userId: String, cart: Cart, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val userCartRef = db.collection("carts").document(userId)

        userCartRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // The cart exists, so update it
                    val existingCart = document.toObject(Cart::class.java)
                    if (existingCart != null) {
                        // Update the existing restaurant carts map
                        val updatedRestaurantCarts = existingCart.restaurantCarts!!.toMutableMap()
                        updatedRestaurantCarts.putAll(cart.restaurantCarts!!) // Add or update the restaurant cart
                        val updatedCart = existingCart.copy(restaurantCarts = updatedRestaurantCarts)

                        // Save the updated cart
                        userCartRef.set(updatedCart)
                            .addOnSuccessListener { onSuccess() }
                            .addOnFailureListener { e -> onError(e.message ?: "Error updating cart") }
                    } else {
                        onError("Error parsing existing cart")
                    }
                } else {
                    // The cart does not exist, so create it
                    userCartRef.set(cart)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { e -> onError(e.message ?: "Error adding cart") }
                }
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error checking cart existence")
            }
    }
    fun getCart(userId: String) {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("carts").whereEqualTo("idCustomer", userId).get().await()
                val cartDocuments = snapshot.documents
                if (cartDocuments.isNotEmpty()) {
                    val cart = cartDocuments[0].toObject(Cart::class.java)
                    _carts.value = cart?.restaurantCarts ?: emptyMap()
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }
    fun placeOrder(order: Order, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val newOrderRef = db.collection("orders").document()
                order.id = newOrderRef.id
                newOrderRef.set(order).addOnSuccessListener {
                    onSuccess()
                }.addOnFailureListener { exception ->
                    onFailure(exception)
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun removeRestaurantCart(userId: String, restaurantName: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val cartRef = db.collection("carts").document(userId)
                db.runTransaction { transaction ->
                    val snapshot = transaction.get(cartRef)
                    val cart = snapshot.toObject(Cart::class.java)
                    if (cart != null && cart.restaurantCarts != null) {
                        val updatedRestaurantCarts = cart.restaurantCarts.toMutableMap()
                        updatedRestaurantCarts.remove(restaurantName)
                        val updatedCart = cart.copy(restaurantCarts = updatedRestaurantCarts)
                        transaction.set(cartRef, updatedCart)
                    }
                }.addOnSuccessListener {
                    onSuccess()
                }.addOnFailureListener { exception ->
                    onFailure(exception)
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }
}