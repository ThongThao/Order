package com.example.admin.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.admin.R
import com.example.admin.ui.theme.blue
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import java.util.UUID

fun uploadToStorage(uri: Uri, context: Context, type: String, onImageUploaded: (String) -> Unit) {
    val storage = Firebase.storage
    // Create a storage reference from our app
    val storageRef = storage.reference

    val uniqueImageName = UUID.randomUUID().toString()
    val spaceRef: StorageReference = storageRef.child("$uniqueImageName.jpg")

    val byteArray: ByteArray? = context.contentResolver
        .openInputStream(uri)
        ?.use { it.readBytes() }

    byteArray?.let {
        val uploadTask = spaceRef.putBytes(byteArray)
        uploadTask.addOnFailureListener {
            Toast.makeText(
                context,
                "Upload failed",
                Toast.LENGTH_SHORT
            ).show()
        }.addOnSuccessListener { _ ->
            spaceRef.downloadUrl.addOnSuccessListener { uri ->
                onImageUploaded(uri.toString()) // Pass the URL to the callback function
            }.addOnFailureListener {
                // Handle failures
                Toast.makeText(
                    context,
                    "Failed to retrieve download URL",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

@Composable
fun SelectItemImageSection(onImageSelected: (String) -> Unit) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val getImage = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = uri
            uploadToStorage(uri, context, "image") { imageUrl ->
                onImageSelected(imageUrl) // Pass the URL to the callback function
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .shadow(1.dp, shape = RoundedCornerShape(16.dp))
                .fillMaxWidth().height(58.dp)
                .background(Color.White)
                .border(width = 1.dp, color = Color.LightGray)
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Select image", style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400,
                    fontFamily = FontFamily.SansSerif
                )
            )

            IconButton(onClick = {
                getImage.launch("image/*")
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "",
                    tint = blue
                )
            }
        }

        // Hiển thị trước ảnh đã chọn
        selectedImageUri?.let { uri ->
            Image(
                painter = rememberImagePainter(uri),
                contentDescription = "Selected image",
                modifier = Modifier
                    .size(200.dp)
                    .padding(10.dp)
            )
        }
    }
}