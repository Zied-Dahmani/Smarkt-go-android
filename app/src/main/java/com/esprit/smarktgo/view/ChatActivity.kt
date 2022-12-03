package com.esprit.smarktgo.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.esprit.smarktgo.R
import com.esprit.smarktgo.adapter.ChatAdapter
import com.esprit.smarktgo.databinding.ActivityChatBinding
import com.esprit.smarktgo.viewmodel.ChatViewModel


class ChatActivity : AppCompatActivity() {

    lateinit var binding : ActivityChatBinding
    private lateinit var chatViewModel: ChatViewModel
    lateinit var chatAdapter: ChatAdapter
    lateinit var orderId :String
    lateinit var userId :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.cartGroup -> {
                    val intent = Intent(this, CartGroupActivity::class.java).apply {
                        putExtra("userId",userId)
                    }
                    startActivity(intent)
                }
            }
            true
        }

        orderId = intent.getStringExtra("orderId")!!
        userId = intent.getStringExtra("userId")!!
        chatViewModel = ChatViewModel(this,orderId,userId)

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