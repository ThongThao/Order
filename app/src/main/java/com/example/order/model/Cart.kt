data class Cart(
    val idCart: String? = "",
    val idCustomer: String? = "",
    val restaurantCarts: Map<String, RestaurantCart>? =null // Map chứa các giỏ hàng của các nhà hàng, key là ID của nhà hàng
)

data class RestaurantCart(
    val restaurantName: String="", // ID của nhà hàng
    val items: List<CartItem?>?=null, // Danh sách các mặt hàng trong giỏ hàng của nhà hàng
    val total: Int=0
)

data class CartItem(
    val id: String?="",
    val name: String?="",
    val description: String?="",
    val image: String?="",
    val price: Int?=0,
    val quantity: Int?=0,
    val note: String?=""
)