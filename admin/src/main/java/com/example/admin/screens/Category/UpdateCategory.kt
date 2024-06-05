package com.example.admin.screens.Category

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.admin.model.Category
import com.example.admin.screens.SelectItemImageSection
import com.example.admin.ui.theme.OrderTheme
import com.example.admin.ui.theme.blue
import com.example.admin.ui.theme.blue3
import com.example.admin.ui.theme.grayFont
import com.google.firebase.firestore.FirebaseFirestore

class UpdateCategory : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrderTheme {
                val navController = rememberNavController()
                Surface(

                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold() {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = blue3,
                                titleContentColor = Color.White,
                                navigationIconContentColor = Color.White,
                                actionIconContentColor = Color.White
                            ),
                            title = {
                                Text(
                                    text = "Edit Category",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold,
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack()}) {
                                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                                }
                            },
                            actions = {
                                IconButton(onClick = {/* Do Something*/ }) {
                                    Icon(imageVector = Icons.Filled.Settings, null)
                                }
                            }
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(0.dp, 50.dp, 0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){

                        updateUI(
                            LocalContext.current,
                            intent.getStringExtra("categoryName"),
                            intent.getStringExtra("categoryImage"),
                            intent.getStringExtra("categoryId"),
                        )

                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun updateUI(
    context: Context,
    name: String?,
    imgurl: String?,
    categoryId: String?
){
    val categoryName = remember {
        mutableStateOf(name)
    }
    val categoryImage = remember {
        mutableStateOf(imgurl)
    }
    val newImageUrl = remember {
        mutableStateOf<String?>(null)
    }

    Column(
        modifier = Modifier
//            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = categoryName.value.toString(),
            onValueChange = { categoryName.value = it },
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
            label = { Text(text = "Name", color = grayFont)}
        )
        Spacer(modifier = Modifier.size(7.dp))
        SelectItemImageSection { imageUrl ->
            // Gán URL mới cho mutable state variable
            newImageUrl.value = imageUrl
        }
        Spacer(modifier = Modifier.size(30.dp))
        Button(
            onClick = {
                if (TextUtils.isEmpty(categoryName.value.toString())) {
                    Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val imageUrl = newImageUrl.value ?: categoryImage.value
                    updateDataToFirebase(
                        categoryId,
                        categoryName.value,
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
            Text(text = "UPDATE", fontWeight = FontWeight.Bold,fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.size(30.dp))
        TextButton(onClick = {
            context.startActivity(Intent(context, AllCategory::class.java))
        },
        ) {
            Text(
                text = "Back",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red,
            )
        }
    }

}

private fun updateDataToFirebase(
    categoryId: String?,
    name: String?,
    imgurl: String?,
    context: Context
) {
    val updatedCategory = Category(categoryId, name, imgurl)

    // getting our instance from Firebase Firestore.
    val db = FirebaseFirestore.getInstance();
    db.collection("Category").document(categoryId.toString()).set(updatedCategory)
        .addOnSuccessListener {
            // on below line displaying toast message and opening
            // new activity to view courses.
            Toast.makeText(context, "Product Updated successfully..", Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(context, AllCategory::class.java))

            // Cập nhật danh sách nhà hàng thuộc danh mục trong cơ sở dữ liệu
            updateRestaurantsInCategory(categoryId, context)

        }.addOnFailureListener {
            Toast.makeText(context, "Fail to update product : " + it.message, Toast.LENGTH_SHORT)
                .show()
        }
}

private fun updateRestaurantsInCategory(categoryName: String?, context: Context) {
    // Lấy danh sách các nhà hàng thuộc danh mục cần cập nhật
    val db = FirebaseFirestore.getInstance()
    db.collection("Restaurant")
        .whereEqualTo("restaurantType", categoryName)
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                // Cập nhật danh mục của từng nhà hàng
                db.collection("Restaurant").document(document.id)
                    .update("restaurantType", categoryName)
                    .addOnSuccessListener {
                        // Log.d(TAG, "DocumentSnapshot successfully updated!")
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Fail to update restaurant : " + e.message, Toast.LENGTH_SHORT).show()
                    }
            }
        }
        .addOnFailureListener { exception ->
            Toast.makeText(context, "Error updating restaurants: $exception", Toast.LENGTH_SHORT).show()
        }
}

