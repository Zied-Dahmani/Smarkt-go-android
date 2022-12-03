package com.esprit.smarktgo.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.esprit.smarktgo.R
import com.esprit.smarktgo.adapter.ProfileAdapter
import com.esprit.smarktgo.model.ProfileItem
import com.esprit.smarktgo.viewmodel.ProfileFragmentViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    lateinit var image: ImageView
    lateinit var fullnameDisplay: TextView
    lateinit var walletDisplay: TextView

    lateinit var fullNameLayout: TextInputLayout
    lateinit var txtFullName: TextInputEditText
    private lateinit var profileViewModel: ProfileFragmentViewModel
    lateinit var myRecycler: RecyclerView
    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var editButton: Button
    lateinit var view2 : View
    var profileList: MutableList<ProfileItem> = ArrayList()

    lateinit var dialogView: View
    lateinit var id:String
    var wallet:Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var myDialog: AlertDialog



        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        view2 = view

        myRecycler = view.findViewById(R.id.profileRecycler)
        fullnameDisplay = view.findViewById(R.id.fullName)
        walletDisplay = view.findViewById(R.id.walletDisplay)

        profileViewModel = ProfileFragmentViewModel(this)
        val googleAccount = GoogleSignIn.getLastSignedInAccount(this.requireContext())
        image = view.findViewById(R.id.userprofileImage)
        val imageURL = googleAccount?.photoUrl

        Glide.with(this.requireActivity())
            .load(imageURL)
            .circleCrop()
            .into(image)
        id=profileViewModel.userId




        profileViewModel.observeUser().observe(requireActivity(), Observer {
            val u = profileViewModel.userLiveData.value
            wallet = u?.wallet!!
            val wd = "%.1f".format(u?.wallet) + " TND"
            fullnameDisplay.text = u?.fullName
            walletDisplay.text = wd

        })
        val onEditName = view.findViewById<ImageView>(R.id.editname)

/*
        if (id.contains("@"))
 {
     onEditName.visibility=View.INVISIBLE
 }
        else
        {
            */

        onEditName.setOnClickListener {
            dialogView = inflater.inflate(R.layout.name_dialog, container,false)
            txtFullName = dialogView.findViewById(R.id.txtFullName)
            fullNameLayout = dialogView.findViewById(R.id.nameContainer)
            editButton = dialogView.findViewById(R.id.editNameButton)
            val builder = AlertDialog.Builder(requireContext())

            builder.setView(dialogView)
            builder.setCancelable(true)
            myDialog = builder.create()
            myDialog.show()
            editButton.setOnClickListener {
                if (validate()) {
                    profileViewModel.updateProfile(txtFullName.text.toString())
                    fullnameDisplay.text=txtFullName.text.toString()
                    dialogView = inflater.inflate(R.layout.name_dialog,container,false )
                    dialogView.visibility=View.GONE
                    myDialog.cancel()

                }

            }



        }


        // }

        initRecyclerView()


        profileAdapter.setOnItemClickListener(object : ProfileAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> {
                        val intent = Intent(requireContext(), WalletActivity::class.java).apply {
                            putExtra("id",id)
                            putExtra("wallet",wallet)
                        }
                        startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(requireContext(), ChatActivity::class.java).apply {
                            putExtra("userName",fullnameDisplay.text)
                        }
                        startActivity(intent)
                    }
                    2 -> {
                        val intent = Intent(requireContext(), SettingsActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

        })

        return view
    }


    private fun initRecyclerView() {
        profileViewModel.setList(profileList)
        profileAdapter = ProfileAdapter(profileList,null)

        myRecycler.apply {
            adapter = profileAdapter
            layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)
        }
    }

    fun validate(): Boolean {
        fullNameLayout.error = null
        if (txtFullName.text!!.isEmpty()) {
            fullNameLayout.error = getString(R.string.mustNotBeEmpty)
            return false
        }
        return true
    }

    /*fun walletUpdated(walletParam:Double)
    {
        walletDisplay.text = "%.1f".format(wallet) + " TND"
        Snackbar.make(view2,"Wallet Updated!", Snackbar.LENGTH_LONG).show()
    }*/
}