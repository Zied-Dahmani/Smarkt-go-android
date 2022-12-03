package com.esprit.smarktgo.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import com.esprit.smarktgo.model.Chat
import com.esprit.smarktgo.view.ChatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatViewModel(val mActivity: ChatActivity,val orderId: String, val userId: String): ViewModel() {

    private val db = Firebase.firestore
    lateinit var  chats : ArrayList<Chat>
    var messageOrder = 0
    
    init {
        mActivity.prepareRecyclerView(userId)
        listenToChat()
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