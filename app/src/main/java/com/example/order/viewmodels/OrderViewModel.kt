
import androidx.lifecycle.ViewModel
import com.example.order.model.Order
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OrderViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val ordersCollection = firestore.collection("orders")

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders
    fun fetchOrdersForUser(userId: String) {
        val validStatus = listOf("Shipping", "Processing", "Shipped")

        ordersCollection
            .whereEqualTo("custumerid", userId)
            .whereIn("status", validStatus)
            .addSnapshotListener { snapshot, _ ->
                snapshot?.let { querySnapshot ->
                    val ordersList = mutableListOf<Order>()
                    for (document in querySnapshot.documents) {
                        val order = document.toObject(Order::class.java)
                        order?.let { ordersList.add(it) }
                    }
                    _orders.value = ordersList
                }
            }
    }
    private val _orders1 = MutableStateFlow<List<Order>>(emptyList())
    val orders1: StateFlow<List<Order>> = _orders1
    fun fetchOrdersForUser1(userId: String) {
        val validStatus = listOf("Delivered","Canceled")
        ordersCollection
            .whereEqualTo("custumerid", userId)
            .whereIn("status", validStatus)
            .addSnapshotListener { snapshot, _ ->
                snapshot?.let { querySnapshot ->
                    val ordersList = mutableListOf<Order>()
                    for (document in querySnapshot.documents) {
                        val order = document.toObject(Order::class.java)
                        order?.let { ordersList.add(it) }
                    }
                    _orders1.value = ordersList
                }
            }
    }
    fun getOrderDetail(orderId: String): Flow<Order?> {
        // Create a MutableStateFlow to hold the order detail
        val orderDetailFlow = MutableStateFlow<Order?>(null)

        // Fetch order detail from Firestore based on orderId
        ordersCollection.document(orderId).get()
            .addOnSuccessListener { documentSnapshot ->
                val order = documentSnapshot.toObject(Order::class.java)
                order?.let {
                    // Update the orderDetailFlow with the fetched order detail
                    orderDetailFlow.value = it
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors or log them
                // For simplicity, just log the exception here
                println("Failed to fetch order detail: $exception")
            }

        // Return the StateFlow as a Flow<Order?>
        return orderDetailFlow
    }

}
