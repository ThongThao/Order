package com.example.admin.screens.Menu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.admin.R
import com.example.admin.model.Menu
import com.example.admin.ui.theme.OrderTheme
import com.example.admin.ui.theme.Purple80
import com.example.admin.ui.theme.blue
import com.example.admin.ui.theme.blue1
import com.example.admin.ui.theme.blue3
import com.example.admin.ui.theme.card1
import com.example.admin.ui.theme.oops
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class AllItem : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorScheme.background
                ) {

                    AllItemUI(LocalContext.current,
                        intent.getStringExtra("restaurantId"),
                        intent.getStringExtra("restaurantName"),
                        intent.getStringExtra("restaurantImage"),
                        intent.getStringArrayListExtra("menu")
                    )
                }
            }
        }
    }
}

fun fetchMenuItemDetails(itemId: String, onItemFetched: (Menu?, String) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val menuCollection = db.collection("Menu")

    menuCollection.document(itemId).get()
        .addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                val menuItem = document.toObject(Menu::class.java)
                onItemFetched(menuItem, itemId)
            } else {

            }
        }
        .addOnFailureListener { exception ->
            // Handle failure to fetch document
        }
}

@Composable
fun AllItemUI(context: Context,
              restaurantId: String?,
              name: String?,
              imgurl: String?,
              menu: List<String>?) {
    val menuItemsState = remember { mutableStateOf<List<Menu>?>(null) }
    val currentMenuItemId = remember { mutableStateOf<String?>(null) } // Thêm dòng này
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(menu) {
        menu?.forEach { menuItemId ->
            coroutineScope.launch {
                fetchMenuItemDetails(menuItemId) { menuItem, itemId ->
                    currentMenuItemId.value = itemId
                    if (menuItem != null) {
                        menuItemsState.value = (menuItemsState.value ?: emptyList()) + menuItem
                    }
                }
            }
        }
    }

    fun deleteMenuItem() {
        val menuItemId = currentMenuItemId.value // Lấy giá trị của menuItemId từ mutableStateOf
        menuItemId?.let {
            val db = FirebaseFirestore.getInstance()
            val menuRef = db.collection("Menu").document(it)

            // Xóa mục menu khỏi collection "Menu"
            menuRef.delete()
                .addOnSuccessListener {
                    Log.d(TAG, "Menu item successfully deleted!")
                    Toast.makeText(context, "Item Deleted successfully..", Toast.LENGTH_SHORT)
                        .show()
                    (context as? ComponentActivity)?.let { activity ->
                        val intent = Intent(context, AllItem::class.java).apply {
                            putExtra("restaurantId", restaurantId)
                            putExtra("restaurantName", name)
                            putExtra("restaurantImage", imgurl)
                            putStringArrayListExtra("menu", ArrayList(menu))
                        }
                        context.startActivity(intent)
                        activity.finish()
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error deleting document", e)
                    // Xử lý lỗi nếu cần
                }
        }
    }

    fun deleteMenuItemFromRestaurant(restaurantId: String) {
        val menuItemId = currentMenuItemId.value // Lấy giá trị của menuItemId từ mutableStateOf
        menuItemId?.let {
            val db = FirebaseFirestore.getInstance()
            val restaurantRef = db.collection("Restaurant").document(restaurantId)
            restaurantRef.update("menu", FieldValue.arrayRemove(it))
                .addOnSuccessListener {
                    Log.d(TAG, "Menu item successfully deleted!")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error updating document", e)
                    // Xử lý lỗi nếu cần
                }
        }
    }
    fun deleteMenuItemFromCategory(categoryName: String) {
        val menuItemId = currentMenuItemId.value // Lấy giá trị của menuItemId từ mutableStateOf
        if (menuItemId == null) {
            Log.w(TAG, "Current menu item ID is null")
            return
        }

        val db = FirebaseFirestore.getInstance()
        val categoryCollection = db.collection("Category")

        // Truy vấn để tìm document có categoryName tương ứng
        categoryCollection.whereEqualTo("categoryName", categoryName).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.first()
                    val categoryId = document.id // Lấy categoryId từ document

                    val categoryRef = db.collection("Category").document(categoryId)
                    categoryRef.update("menu", FieldValue.arrayRemove(menuItemId))
                        .addOnSuccessListener {
                            Log.d(TAG, "Menu item successfully deleted from category!")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error updating document", e)
                            // Xử lý lỗi nếu cần
                        }
                } else {
                    Log.w(TAG, "No category found with the name: $categoryName")
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error finding category", e)
                // Xử lý lỗi nếu cần
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { context.startActivity(Intent(context, MenuRes::class.java)) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "",
                    tint = blue,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.width(115.dp))
            Text(
                text = "Menu", style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
            Spacer(modifier = Modifier.width(115.dp))
            IconButton(onClick = {
                val i = Intent(context, AddItem::class.java)
                i.putExtra("restaurantName",name)
                context.startActivity(i)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "",
                    tint = blue,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        imgurl?.let {
            Surface(
                modifier = Modifier
                    .border(width = 1.dp, color = blue3)
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,

                    )
            }
        }
    }
    Column(
        modifier = Modifier
            .padding(top = 240.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(width = 2.dp, color = blue3),
            modifier = Modifier
                .wrapContentSize()
                .padding(top = 3.dp),
            color = Color.White
        ) {
            name?.let {
                Text(
                    text = it,
                    fontSize = 25.sp,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(
                        vertical = 6.dp,
                        horizontal = 12.dp
                    ),
                    color = Color.Black
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Menu",
                fontSize = 24.sp,
                color = blue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    vertical = 4.dp,
                    horizontal = 12.dp
                )
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .padding(top = 2.dp)
                    .background(Color.LightGray)
            )
        }
        if (menuItemsState.value.isNullOrEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.oops),
                    contentDescription = null
                )
                Text(
                    text = "Empty Menu!!!", style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = oops
                    )
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(menuItemsState.value ?: emptyList()) { menuItem ->
                    // Display details of each menu item
                    menuItem?.let {
                        Surface(
                            color = card1,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(175.dp)
                                .clip(shape = RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            shadowElevation = 10.dp,
                            onClick = {
                                val i = Intent(context, UpdateItem::class.java)
                                i.putExtra("itemName", menuItem?.itemName)
                                i.putExtra("itemDescription", menuItem?.itemDescription)
                                i.putExtra("itemPrice", menuItem?.itemPrice)
                                i.putExtra("itemType", menuItem?.itemType)
                                i.putExtra("itemRestaurant", menuItem?.itemRestaurant)
                                i.putExtra("itemImage", menuItem?.itemImage)
                                i.putExtra("itemId", menuItem?.itemId)
                                context.startActivity(i)
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.size(width = 100.dp, height = 100.dp)
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(it.itemImage),
                                        contentScale = ContentScale.Crop,
                                        contentDescription = null
                                    )
                                }
                                Spacer(modifier = Modifier.width(15.dp))
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .weight(2f),
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(24.dp),
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .padding(top = 3.dp),
                                        color = blue1
                                    ) {
                                        Text(
                                            text = "${it.itemType}",
                                            fontSize = 14.sp,
                                            style = MaterialTheme.typography.titleSmall,
                                            modifier = Modifier.padding(
                                                vertical = 4.dp,
                                                horizontal = 8.dp
                                            ),
                                            color = Color.Black
                                        )
                                    }
                                    Text(
                                        text = "${it.itemName}",
                                        fontSize = 25.sp,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.width(170.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    val formattedPrice = it.itemPrice?.let {
                                        NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(it)
                                    } ?: "N/A"

                                    Text(text = "Price: $formattedPrice")
                                    Text(
                                        text = "Description:" + it.itemDescription,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.width(170.dp)
                                    )
                                }
                                Column() {
                                    IconButton(
                                        onClick = {
                                            val i = Intent(context, UpdateItem::class.java)
                                            i.putExtra("itemName", menuItem?.itemName)
                                            i.putExtra("itemDescription", menuItem?.itemDescription)
                                            i.putExtra("itemPrice", menuItem?.itemPrice)
                                            i.putExtra("itemType", menuItem?.itemType)
                                            i.putExtra("itemRestaurant", menuItem?.itemRestaurant)
                                            i.putExtra("itemImage", menuItem?.itemImage)
                                            i.putExtra("itemId", menuItem?.itemId)
                                            context.startActivity(i)
                                        }

                                    ) {
                                        Icon(
                                            painterResource(id = R.drawable.ic_edit),
                                            contentDescription = "",
                                            tint = Purple80,
                                            modifier = Modifier.size(27.dp)
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            deleteMenuItem()
                                            restaurantId?.let {
                                                deleteMenuItemFromRestaurant(it)
                                            }
                                            it.itemType?.let { deleteMenuItemFromCategory(it) }
                                        },
                                    ) {
                                        Icon(
                                            painterResource(id = R.drawable.ic_delete),
                                            contentDescription = "",
                                            tint = Color.Red,
                                            modifier = Modifier.size(27.dp)

                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}






@Preview(showBackground = true)
@Composable
fun PreviewAllItemUI() {
    val context = LocalContext.current
    val restaurantId = "restaurantId"
    val name = "Restaurant Name"
    val imgurl = "https://example.com/image.jpg"
    val menu = listOf("menuItem1", "menuItem2", "menuItem3") // Thay thế các menuItem bằng các ID thực tế

    OrderTheme {
        AllItemUI(context, restaurantId, name, imgurl, menu)
    }
}
