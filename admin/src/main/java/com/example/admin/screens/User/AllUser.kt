package com.example.admin.screens.User

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.admin.model.User
import com.example.admin.screens.Home
import com.example.admin.ui.theme.blackcart
import com.example.admin.ui.theme.blueColor
import com.example.admin.ui.theme.delete
import com.google.firebase.firestore.FirebaseFirestore

class AllUser : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
            ) {
                Scaffold {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = blueColor,
                            titleContentColor = Color.White,
                            navigationIconContentColor = Color.White,
                            actionIconContentColor = Color.White
                        ),
                        title = {
                            Text(
                                text = "All User",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                this@AllUser.startActivity(
                                    Intent(
                                        this@AllUser,
                                        Home::class.java
                                    )
                                )
                            }) {
                                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                            }
                        },
                    )
                }
                var userList = mutableStateListOf<User>()
                var db: FirebaseFirestore = FirebaseFirestore.getInstance()

                db.collection("users")
                    .get()
                    .addOnSuccessListener { queryDocumentSnapshot ->
                        if (!queryDocumentSnapshot.isEmpty) {
                            val list = queryDocumentSnapshot.documents
                            for (d in list) {
                                val c: User? = d.toObject(User::class.java)
                                c?.id = d.id
                                Log.e("TAG", "User id is : " + c!!.id)
                                userList.add(c)
                            }
                        } else {
                            Toast.makeText(
                                this@AllUser,
                                "No data found in Database",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(
                            this@AllUser,
                            "Fail to get the data.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                alluserUI(LocalContext.current, userList)
            }
        }
    }
}

private fun deleteDataFromFirebase(id: String?, context: Context) {


    val db = FirebaseFirestore.getInstance();

    db.collection("users").document(id.toString()).delete().addOnSuccessListener {
        Toast.makeText(context, "Deleted successfully..", Toast.LENGTH_SHORT).show()
        context.startActivity(Intent(context, AllUser::class.java))
    }.addOnFailureListener {

        Toast.makeText(context, "Fail to delete item..", Toast.LENGTH_SHORT).show()
    }

}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun alluserUI(context: Context, userList: SnapshotStateList<User>) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp, 75.dp, 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {


        LazyColumn {
            itemsIndexed(userList) { index, item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 5.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
//                            .padding(end = 5.dp),
//                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = null,
                            modifier = Modifier.height(20.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        userList[index]?.role?.let {
                            Text(
                                text = it,
                                fontSize = 15.sp,
                                color = blackcart,
                                fontWeight = FontWeight.Medium
                            )
                        }

                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color.LightGray,
                                shape = RoundedCornerShape(12.dp),
                            )
                            .padding(bottom = 2.dp),
                        shadowElevation = 6.dp,
                        onClick = {

                        }
                    ) {

                        Row(
                            modifier = Modifier
                                .padding(bottom = 10.dp, start = 10.dp),
                        ) {
                            androidx.compose.material.Surface(
                                shape = RoundedCornerShape(9.dp),
                                modifier = Modifier
                                    .padding(top = 22.dp)
                                    .size(width = 70.dp, height = 70.dp)
                            ) {

                                userList[index]?.image?.let {
                                    Image(
                                        painter = rememberAsyncImagePainter(it),
                                        contentDescription = "",
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .padding(start = 10.dp, top = 20.dp)
                                    .fillMaxWidth()
                                    .weight(2f),
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                ) {
                                    Text(
                                        text = "Name:",
                                        fontSize = 15.sp,
                                        color = Color.DarkGray
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    userList[index]?.fullName?.let {
                                        Text(
                                            text = it,
                                            fontSize = 16.sp,
                                            color = blackcart,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                ) {
                                    Text(
                                        text = "Phone:",
                                        fontSize = 15.sp,
                                        color = Color.DarkGray
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    userList[index]?.phoneNumber?.let {
                                        Text(
                                            text = it,
                                            fontSize = 16.sp,
                                            color = blackcart,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                ) {
                                    Text(
                                        text = "Email:",
                                        fontSize = 15.sp,
                                        color = Color.DarkGray
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    userList[index]?.email?.let {
                                        Text(
                                            text = it,
                                            fontSize = 16.sp,
                                            color = blackcart,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }

                                }
                                Row(
                                    modifier = Modifier
                                        .padding(bottom = 10.dp)
                                ) {
                                    Text(
                                        text = "Address:",
                                        fontSize = 15.sp,
                                        color = Color.DarkGray
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    userList[index]?.address?.let {
                                        Text(
                                            text = it,
                                            fontSize = 16.sp,
                                            color = blackcart,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .weight(0.3f),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.End
                            ) {
                                Button(
                                    onClick = {
                                        deleteDataFromFirebase(item.id, context)
                                    },
                                    contentPadding = PaddingValues(),
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(backgroundColor = delete, contentColor = Color.White),
                                    modifier = Modifier
                                        .width(23.dp)
                                        .height(23.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Clear,
                                        null,
                                        modifier = Modifier
                                            .size(18.dp),
                                        tint = Color.White
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

