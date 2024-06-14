package com.example.admin.screens.Category

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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.admin.model.Category
import com.example.admin.screens.SelectItemImageSection
import com.example.admin.ui.theme.OrderTheme
import com.example.admin.ui.theme.blue
import com.example.admin.ui.theme.blueColor
import com.example.admin.ui.theme.grayFont
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID


class AddCategory : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrderTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = blueColor,
                                titleContentColor = Color.White,
                                navigationIconContentColor = Color.White,
                                actionIconContentColor = Color.White
                            ),
                            title = {
                                Text(
                                    text = "Add Category",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {this@AddCategory.startActivity(Intent(this@AddCategory, AllCategory::class.java))  }) {
                                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            addUI(LocalContext.current)
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addUI(context: Context){
    var expanded by remember { mutableStateOf(false) }

    val categoryName = remember { mutableStateOf("") }
    val categoryImage = remember { mutableStateOf("") }


    TextButton(onClick = {
        context.startActivity(Intent(context, AllCategory::class.java))
    }) {
        Text(
            text = "All Category",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = blueColor,
        )
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = null,
            tint = blueColor
        )
    }

    Column(
        modifier = Modifier.padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SelectItemImageSection { imageUrl ->
            categoryImage.value = imageUrl
        }
        Spacer(modifier = Modifier.size(7.dp))
        OutlinedTextField(
            value = categoryName.value,
            onValueChange = { categoryName.value = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(59.dp),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = blueColor,
                unfocusedBorderColor = blue
            ),
            label = { Text(text = "Category Name", color = grayFont) }
        )

        Spacer(modifier = Modifier.size(20.dp))
        Button(
            onClick = {
                when {
                    TextUtils.isEmpty(categoryName.value) -> {
                        Toast.makeText(context, "Please enter the product name", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        addCategory(
                            categoryName.value,
                            categoryImage.value,
                            context
                        )
                        context.startActivity(Intent(context, AllCategory::class.java))
                    }
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 3.dp,
                pressedElevation = 6.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = blueColor
            )
        ) {
            Text(text = "Add", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
    }
}

fun addCategory(
    categoryName: String,
    categoryImage: String,
    context: Context
){

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val dbCategory: CollectionReference = db.collection("Category")

    // Kiểm tra xem categoryName đã tồn tại trong Firebase chưa
    dbCategory.whereEqualTo("categoryName", categoryName)
        .get()
        .addOnSuccessListener { documents ->
            if (documents.isEmpty) {
                // Nếu categoryName không tồn tại, thêm category mới
                val categoryId = UUID.randomUUID().toString()
                val category = Category(categoryId, categoryName, categoryImage)

                dbCategory.document(categoryId).set(category)
                    .addOnSuccessListener {
                        Toast.makeText(
                            context,
                            "Added Successful",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            context,
                            "Failed to add category\n$e",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                // Nếu categoryName đã tồn tại, thông báo lỗi
                Toast.makeText(
                    context,
                    "Category with name $categoryName already exists.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        .addOnFailureListener { e ->
            Toast.makeText(
                context,
                "Failed to check category existence\n$e",
                Toast.LENGTH_SHORT
            ).show()
        }
}





