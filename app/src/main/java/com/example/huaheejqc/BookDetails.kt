package com.example.huaheejqc

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.huaheejqc.data.Book
import com.example.huaheejqc.data.CartItem
import com.example.huaheejqc.databinding.FragmentAddBookBinding
import com.example.huaheejqc.databinding.FragmentBookDetailsBinding
import com.example.huaheejqc.sellerBookManagement.PendingOrder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.io.File
import java.text.DecimalFormat
import java.util.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookDetails.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookDetails : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentBookDetailsBinding? = null
    private val binding get() = _binding!!
    val args: BookDetailsArgs by navArgs()
    private var cartItemRefs:ArrayList<String> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val realtimeDb = Firebase.database
        _binding = FragmentBookDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        val db = Firebase.firestore
        val bookid: String = args.viewbookid.toString()
        val userid = Firebase.auth.currentUser?.uid
        val stringID = userid.toString()
        var ownerid = ""
        var hashMap:HashMap<String, ArrayList<String>> = HashMap()
        cartItemRefs=ArrayList()

        Log.d("chin", args.viewbookid)

        val docRef = db.collection("books").document(bookid)
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d("TAG", "DocumentSnapshot data: ${document.data}")
                val title: String = document.get("title") as String
                val author: String = document.get("author") as String
                val price: Number = document.get("price") as Number
                val description: String = document.get("description") as String
                val priceString = "RM " + DecimalFormat("####.00").format(price).toString()
                val imageUrl = document.get("imageUrl") as String
                val status = document.get("status") as String
                ownerid = document.get("userid").toString()

                if (status != "Posted"){
                    binding.bookdetailsPlaceorderBtn.visibility = View.INVISIBLE
                }else{
                    binding.bookdetailsPlaceorderBtn.visibility = View.VISIBLE
                }

                if(stringID == ownerid){
                    binding.bookdetailsPlaceorderBtn.visibility = View.INVISIBLE
                }else{
                    binding.bookdetailsPlaceorderBtn.visibility = View.VISIBLE
                }

                db.collection("User").document(ownerid)
                    .get().addOnSuccessListener { document ->
                        if (document != null) {
                            var ownerEmail = document.get("email") as String
                            binding.bookdetailsOwnerTxt.setText(ownerEmail)

                            val storage = Firebase.storage
                            var storageRef = storage.reference
                            var imageRef = storageRef.child("images/${imageUrl}")
                            val localFile = File.createTempFile("images", "jpg")

                            Log.d("asdasdasd","asdasdasd")
                            imageRef.getFile(localFile).addOnSuccessListener {
                                val myBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath())
                                binding.bookdetailsImg.setImageBitmap(myBitmap)
                                Log.d("BookdetailImage", "succ")
                            }.addOnFailureListener {
                                Log.d("noob", "noob")
                            }
                        }
                    }
                binding.bookdetailsBooknameTxt.setText(title)
                binding.bookdetailsAuthorTxt.setText(author)
                binding.bookdetailsPriceTxt.setText(priceString)
                binding.bookdetailsDescriptionTxt.setText(description)
            } else {
                Log.d("TAG", "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }

        binding.bookdetailsPlaceorderBtn.setOnClickListener{
            db.collection("carts")
                .document(stringID)
                .get()
                .addOnSuccessListener { document ->
                    if (document?.data != null) {
                        // get cart items references
                        cartItemRefs.addAll(document.data?.get("books") as ArrayList<String>)
                        cartItemRefs.add(bookid)
                        hashMap.put("books", cartItemRefs)
                        db.collection("carts")
                            .document(stringID)
                            .set(hashMap)
                            .addOnSuccessListener {
                                db.collection("books")
                                    .document(bookid)
                                    .update("status","PendingOrder")
                                    .addOnSuccessListener {
                                        view.findNavController().navigate(R.id.action_bookDetails_to_shoppingCart)
                                    }
                            }

                    }else{
                        cartItemRefs.add(bookid)
                        hashMap.put("books", cartItemRefs)
                        db.collection("carts")
                            .document(stringID)
                            .set(hashMap)
                            .addOnSuccessListener {
                                db.collection("books").document(bookid)
                                    .update("status", "PendingOrder")
                                    .addOnSuccessListener {
                                        view.findNavController().navigate(R.id.action_bookDetails_to_shoppingCart)
                                    }
                            }
                            .addOnFailureListener{

                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("ShoppingCart", "get failed with ", exception)
                }

        }

        binding.bookDetailsChatBtn.setOnClickListener {
            runBlocking {
                val currentUserId = Firebase.auth.currentUser?.uid.toString()
                val bookDoc = db.collection("books").document(bookid).get().await()
                val uid = bookDoc.get("userid") as String
                val userDoc =
                    db.collection("User").document(uid).get().await().data as HashMap<String, Any>
                val bookAuthorName = userDoc["name"] as String
                val innerView = it
                realtimeDb.reference.child("chat-members").get().addOnSuccessListener {
                    val chatDoc = it.value
                    if (chatDoc != null) {
                        chatDoc as HashMap<String, Any>
                        for ((chatId, memberList) in chatDoc) {
                            memberList as HashMap<String, Any>
                            if (memberList.containsKey(uid) && memberList.containsKey(currentUserId)) {
                                val action =
                                    BookDetailsDirections.actionBookDetailsToConversationFragment(
                                        chatId,
                                        bookAuthorName
                                    )
                                innerView.findNavController().navigate(action)
                                return@addOnSuccessListener
                            }
                        }
                    }
                    val newConvo: HashMap<String, Any> = HashMap()
                    newConvo.put(uid, true)
                    newConvo.put(currentUserId, true)
                    val something = realtimeDb.getReference("chat-members").push()
                    val newChatId = something.key.toString()
                    something.setValue(newConvo)
                    val action = BookDetailsDirections.actionBookDetailsToConversationFragment(
                        newChatId,
                        bookAuthorName
                    )
                    innerView.findNavController().navigate(action)
                }.addOnFailureListener {
                    Log.e("firebase", "Error getting data", it)
                }

            }
        }
        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookDetails.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookDetails().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}