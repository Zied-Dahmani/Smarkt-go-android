package com.esprit.smarktgo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.esprit.smarktgo.R
import com.esprit.smarktgo.adapter.ChatAdapter
import com.esprit.smarktgo.adapter.UserAdapter
import com.esprit.smarktgo.databinding.ActivityCartGroupBinding
import com.esprit.smarktgo.databinding.ActivityChatBinding
import com.esprit.smarktgo.model.Chat
import com.esprit.smarktgo.viewmodel.CartGroupViewModel
import com.esprit.smarktgo.viewmodel.ChatViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    lateinit var binding : ActivityChatBinding
    private lateinit var chatViewModel: ChatViewModel
    lateinit var chatAdapter: ChatAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        binding.toolbar.setNavigationOnClickListener { finish() }

        chatViewModel = ChatViewModel(this)

        binding.sendIcon.setOnClickListener {
            if(binding.message.text!!.isNotEmpty())
            {
                val userName = intent.getStringExtra("userName")
                chatViewModel.sendMessage(binding.message.text.toString(), userName!!)
                binding.message.text!!.clear()
                binding.rvMessages.scrollToPosition(chatViewModel.chats.size - 1);
            }
        }
    }

    fun prepareRecyclerView(userId:String) {
        chatAdapter = ChatAdapter(userId)
        binding.rvMessages.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL ,false)
        }
    }

}