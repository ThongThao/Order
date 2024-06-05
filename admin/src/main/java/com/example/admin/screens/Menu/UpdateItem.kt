package com.example.admin.screens.Menu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.admin.R
import com.example.admin.model.Menu
import com.example.admin.screens.SelectItemImageSection
import com.example.admin.ui.theme.OrderTheme
import com.example.admin.ui.theme.blue
import com.example.admin.ui.theme.grayFont
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class UpdateItem : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Row(
                        modifier = Modifier
                            .padding(top = 10.dp),
                    ) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = "",
                                tint = blue,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(60.dp))
                        Text(
                            text = "Edit Item", style = TextStyle(
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(0.dp, 50.dp, 0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){

                        updateitemUI(
                            LocalContext.current,
                            intent.getStringExtra("itemName"),
                            intent.getStringExtra("itemDescription"),
                            intent.getIntExtra("itemPrice",0).takeIf { it != 0 },
                            intent.getStringExtra("itemType"),
                            intent.getStringExtra("itemRestaurant"),
                            intent.getStringExtra("itemImage"),
                            intent.getStringExtra("itemId"),
                        )

                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun updateitemUI(
    context: Context,
    name: String?,
    description: String?,
    price:Int?,
    type: String?,
    restaurant: String?,
    imgurl: String?,
    itemId: String?
) {
    val itemName = remember {
        mutableStateOf(name)
    }
    val itemDescription = remember {
        mutableStateOf(description)
    }
    val itemType = remember {
        mutableStateOf(type)
    }
    val itemPrice = remember {
        mutableStateOf(price?.toString() ?: "")
    }
    val itemRestaurant = remember {
        mutableStateOf(restaurant)
    }
    val itemImage = remember {
        mutableStateOf(imgurl)
    }
    val newImageUrl = remember {
        mutableStateOf<String?>(null)
    }
    val categories = remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    val restaurants = remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }
    var expanded1 by remember { mutableStateOf(false) }

    // Fetch categories from Firestore
    LaunchedEffect(Unit) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val dbCategory: CollectionReference = db.collection("Category")

        dbCategory.get().addOnSuccessListener { querySnapshot ->
            val list = querySnapshot.documents.mapNotNull {
                Pair(it.id, it.getString("categoryName") ?: "")
            }
            categories.value = list
        }.addOnFailureListener { exception ->
            Log.e("Firestore", "Error getting categories", exception)
        }
    }
    LaunchedEffect(Unit) {
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val dbRestaurant: CollectionReference = db.collection("Restaurant")

        dbRestaurant.get().addOnSuccessListener { querySnapshot ->
            val list = querySnapshot.documents.mapNotNull {
                Pair(it.id, it.getString("restaurantName") ?: "")
            }
            restaurants.value = list
        }.addOnFailureListener { exception ->
            Log.e("Firestore", "Error getting restaurants", exception)
        }
    }


    Column(
        modifier = Modifier
//            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = itemName.value.toString(),
            onValueChange = { itemName.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = blue,
                unfocusedBorderColor = Color.LightGray
            ),
            label = { Text(text = "Name", color = grayFont) }
        )
        Spacer(modifier = Modifier.size(7.dp))
        OutlinedTextField(
            value = itemDescription.value.toString(),
            onValueChange = { itemDescription.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = blue,
                unfocusedBorderColor = Color.LightGray
            ),
            label = { Text(text = "Short Description ", color = grayFont) }
        )
        Spacer(modifier = Modifier.size(7.dp))
        OutlinedTextField(
            value = itemPrice.value,
            onValueChange = { newValue ->
                // Update the state with the new value
                itemPrice.value = newValue
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number // Ensures numeric input
            ),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = blue,
                unfocusedBorderColor = Color.LightGray
            ),
            label = { Text(text = "Price", color = grayFont) }
        )
        Spacer(modifier = Modifier.size(7.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Category",
                color = grayFont
            )
            Spacer(modifier = Modifier.size(7.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                border = BorderStroke(width = 1.dp, color = Color.LightGray),
                modifier = Modifier.height(58.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = itemType.value.toString(),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp),
                        style = TextStyle(color = Color.Black),
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                    IconButton(
                        onClick = { expanded = true },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Dropdown Icon",
                            modifier = Modifier.padding(end = 15.dp)
                        )
                    }
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(150.dp)
            ) {
                categories.value.forEach { (categoryId, categoryName) ->
                    DropdownMenuItem(
                        {
                            Text(text = categoryName, color = Color.Black)
                        },
                        onClick = {
                            itemType.value = categoryName
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(7.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Restaurant",
                color = grayFont
            )
            Spacer(modifier = Modifier.size(7.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                border = BorderStroke(width = 1.dp, color = Color.LightGray),
                modifier = Modifier.height(58.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = itemRestaurant.value.toString(),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp),
                        style = TextStyle(color = Color.Black),
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                    IconButton(
                        onClick = { expanded1 = true },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Dropdown Icon",
                            modifier = Modifier.padding(end = 15.dp)
                        )
                    }
                }
            }
            DropdownMenu(
                expanded = expanded1,
                onDismissRequest = { expanded1 = false },
                modifier = Modifier.width(150.dp)
            ) {
                restaurants.value.forEach { (restaurantId, restaurantName) ->
                    DropdownMenuItem(
                        {
                            Text(text = restaurantName, color = Color.Black)
                        },
                        onClick = {
                            itemRestaurant.value = restaurantName
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.size(12.dp))
        SelectItemImageSection { imageUrl ->
            // Gán URL mới cho mutable state variable
            newImageUrl.value = imageUrl
        }
        Spacer(modifier = Modifier.size(30.dp))
        Button(
            onClick = {
                if (TextUtils.isEmpty(itemName.value.toString())) {
                    Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val imageUrl = newImageUrl.value ?: itemImage.value
                    updateResToFirebase(
                        itemId,
                        itemName.value,
                        itemDescription.value,
                        itemPrice.value.toInt(),
                        itemType.value,
                        itemRestaurant.value,
                        imageUrl,
                        context
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 3.dp,
                pressedElevation = 6.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = blue
            ),
//                border = BorderStroke(0.5.dp, Color.Red)
        ) {
            Text(text = "Update", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.size(30.dp))
        TextButton(
            onClick = {
                val intent = Intent(context, MenuRes::class.java)
                context.startActivity(intent)
            },
        ) {
            Text(
                text = "View menu",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red,
            )
        }
    }
}
private fun updateResToFirebase(
    itemId: String?,
    name: String?,
    description: String?,
    price:Int?,
    type: String?,
    restaurant: String?,
    imgurl: String?,
    context: Context
) {
    val updatedMenu = Menu(itemId, name,description,price,type,restaurant,imgurl)

    // getting our instance from Firebase Firestore.
    val db = FirebaseFirestore.getInstance();
    db.collection("Menu").document(itemId.toString()).set(updatedMenu)
        .addOnSuccessListener {
            // on below line displaying toast message and opening
            // new activity to view courses.
            Toast.makeText(context, "Product Updated successfully..", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Fail to update product : " + it.message, Toast.LENGTH_SHORT)
                .show()
        }
}

