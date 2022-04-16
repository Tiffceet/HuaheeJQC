package com.example.huaheejqc

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UploadImage.newInstance] factory method to
 * create an instance of this fragment.
 */
class UploadImage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    lateinit var imagePreview: ImageView
    lateinit var btn_choose_image: Button
    lateinit var btn_upload_image: Button


    class MainActivity : AppCompatActivity() {

        private val PICK_IMAGE_REQUEST = 71
        private var filePath: Uri? = null
        private var firebaseStore: FirebaseStorage? = null
        private var storageReference: StorageReference? = null
        lateinit var imagePreview: ImageView
        lateinit var btn_choose_image: Button
        lateinit var btn_upload_image: Button


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            btn_choose_image = findViewById(R.id.btn_choose_image)
            btn_upload_image = findViewById(R.id.btn_upload_image)
            imagePreview = findViewById(R.id.image_preview)

            firebaseStore = FirebaseStorage.getInstance()
            storageReference = FirebaseStorage.getInstance().reference

            btn_choose_image.setOnClickListener { launchGallery() }
            btn_upload_image.setOnClickListener { uploadImage() }
        }

        private fun launchGallery() {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
                if(data == null || data.data == null){
                    return
                }

                filePath = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                    imagePreview.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        private fun uploadImage(){
            if(filePath != null){
                val ref = storageReference?.child("myImages/" + UUID.randomUUID().toString())
                val uploadTask = ref?.putFile(filePath!!)

            }else{
                Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
            }
        }

    }
}