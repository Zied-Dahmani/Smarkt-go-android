package com.esprit.smarktgo.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esprit.smarktgo.R
import com.esprit.smarktgo.adapter.ProfileAdapter
import com.esprit.smarktgo.model.ProfileItem
import com.esprit.smarktgo.model.User
import com.esprit.smarktgo.viewmodel.ProfileFragmentViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment: Fragment( ) {

    private lateinit var profileViewModel: ProfileFragmentViewModel
    lateinit var myRecycler: RecyclerView
    private lateinit var profileAdapter: ProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        myRecycler = view.findViewById(R.id.profileRecycler)
        profileViewModel = ProfileFragmentViewModel()

        initRecyclerView()

        profileAdapter.setOnItemClickListener(object : ProfileAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> navigatetoProfile()
                    2 -> showAlert()
                }
            }

        })

        return view
    }

    private fun initRecyclerView() {
        var profileList: MutableList<ProfileItem> = ArrayList()
        profileViewModel.setList(profileList)
        profileAdapter = ProfileAdapter(profileList)

        myRecycler.apply {
            adapter = profileAdapter
            layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)
        }
    }




    private fun navigatetoProfile() {
        val intent = Intent(requireContext(), ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun signOut() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        val mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        val intent = Intent(requireContext(), SignInActivity::class.java)
        startActivity(intent)
        mGoogleSignInClient.signOut()
        FirebaseAuth.getInstance().signOut()
    }


   private fun showAlert() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Do you want to leave us ?")
            .setCancelable(false)
            .setPositiveButton("Log Out", DialogInterface.OnClickListener { dialog, id ->
                signOut()
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("You are about to log out")
        alert.show()
    }
    }
