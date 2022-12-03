package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.smarktgo.model.Chat
import com.esprit.smarktgo.repository.OrderRepository
import com.esprit.smarktgo.view.ChatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatViewModel(chatActivity: ChatActivity): ViewModel() {

    val mActivity = chatActivity
    var userId: String
    private lateinit var orderId: String
    private val db = Firebase.firestore
    private val orderRepository = OrderRepository()
    lateinit var  chats : ArrayList<Chat>
    var messageOrder = 0
    
    init {
        val googleSignIn = GoogleSignIn.getLastSignedInAccount(mActivity)
        userId = if(googleSignIn!=null) {
            googleSignIn.email!!
        } else FirebaseAuth.getInstance().currentUser?.phoneNumber!!

        mActivity.prepareRecyclerView(userId)
        getOrder()
    }

    private fun listenToChat()
    {
        db.collection("chats")
            .whereEqualTo("orderId", orderId)
            .orderBy("order")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.e("TAG", "Listen failed.", e)
                    return@addSnapshotListener
                }
                chats = ArrayList<Chat>()
                for (doc in value!!) {
                    if(doc.getString("message")!="")
                    {
                        val chat = Chat(doc.getString("dateTime")!!, doc.getString("userId")!!, doc.getString("message")!!,
                            doc.getString("userName")!!, doc.getString("orderId")!!,doc.getField("order")!!)
                        chats.add(chat)
                        if(chat.order>messageOrder)
                            messageOrder=chat.order
                    }
                }
                mActivity.chatAdapter.setList(chats)
                mActivity.binding.rvMessages.scrollToPosition(chats.size - 1);
            }
    }

    private fun getOrder() {
        try {
            viewModelScope.launch {
                val result = orderRepository.get(userId)

                result?.let {
                    orderId =  result.id
                    listenToChat()
                }
            }
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, e.statusCode.toString())
        }
    }


    fun sendMessage(message: String,userName:String)
    {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val current = LocalDateTime.now().format(formatter)

        val db = Firebase.firestore
        val chat = Chat(current,userId,message.trim(),userName,orderId,messageOrder+1)

        db.collection("chats")
            .add(chat)
            .addOnSuccessListener {
                mActivity.binding.rvMessages.scrollToPosition(chats.size - 1)
            }
            .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error adding document", e) }

    }

}