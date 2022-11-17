package com.esprit.smarktgo.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
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
    private lateinit var tobeClosed: AlertDialog

    lateinit var fullNameLayout: TextInputLayout
    lateinit var txtFullName: TextInputEditText
    private lateinit var profileViewModel: ProfileFragmentViewModel
    lateinit var myRecycler: RecyclerView
    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var editButton: Button
    lateinit var dialogView: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
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
        profileViewModel.observeUser().observe(requireActivity(), Observer {
            val u = profileViewModel.userLiveData.value
            val wallet = "%.1f".format(u?.wallet) + " TND"
            fullnameDisplay.text = u?.fullName
            walletDisplay.text = wallet
        })
        initRecyclerView()
        spacing()


        profileAdapter.setOnItemClickListener(object : ProfileAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> navigatetoProfile()
                    2 -> showAlert()
                }
            }

        })


        val onEditName = view.findViewById<ImageView>(R.id.editname)
        dialogView = inflater.inflate(R.layout.name_dialog, null)

        onEditName.setOnClickListener {
            txtFullName = dialogView.findViewById(R.id.txtFullName)
            fullNameLayout = dialogView.findViewById(R.id.fnLayout)
            editButton = dialogView.findViewById(R.id.editNameButton)
        //   showEditFullname()
            val bulider = AlertDialog.Builder(requireContext())

            bulider.setView(dialogView)
            bulider.setCancelable(true)
            tobeClosed = bulider.create()
            tobeClosed.show()
            editButton.setOnClickListener {
                if (validate()) {
                    profileViewModel.updateProfile(txtFullName.text.toString())
                    container?.removeView(dialogView)
tobeClosed.dismiss()

                }
            }


        }

        return view
    }

    fun showEditFullname() {


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

    fun spacing() {
        val itemDecoration: ItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        myRecycler.addItemDecoration(itemDecoration)
    }

    fun validate(): Boolean {
        fullNameLayout.error = null
        if (txtFullName.text!!.isEmpty()) {
            fullNameLayout.error = getString(R.string.mustNotBeEmpty)
            return false
        }
        return true

    }


}
