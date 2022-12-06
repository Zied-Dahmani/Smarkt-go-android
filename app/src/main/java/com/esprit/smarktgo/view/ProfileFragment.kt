package com.esprit.smarktgo.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.esprit.smarktgo.R
import com.esprit.smarktgo.adapter.ProfileAdapter
import com.esprit.smarktgo.databinding.FragmentProfileBinding
import com.esprit.smarktgo.databinding.NameDialogBinding
import com.esprit.smarktgo.viewmodel.ProfileFragmentViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout


class ProfileFragment : Fragment() {
    private lateinit var profileViewModel: ProfileFragmentViewModel
    lateinit var fullNameLayout: TextInputLayout
    lateinit var myRecycler: RecyclerView
    lateinit var profileAdapter: ProfileAdapter
    lateinit var view2: View
    lateinit var dialogView: View
    lateinit var id: String
    var wallet: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var myDialog: AlertDialog
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val binding = FragmentProfileBinding.bind(view)
        view2 = view
         myRecycler=binding.profileRecycler
        profileViewModel = ProfileFragmentViewModel(this)
        val googleAccount = GoogleSignIn.getLastSignedInAccount(this.requireContext())
        val imageURL = googleAccount?.photoUrl
        Glide.with(this.requireActivity())
            .load(imageURL)
            .circleCrop()
            .into(binding.userprofileImage)
        id = profileViewModel.userId

        profileViewModel.observeUser().observe(requireActivity(), Observer {
            val u = profileViewModel.userLiveData.value
            wallet = u?.wallet!!
            val wd = "%.1f".format(u.wallet) + " TND"
            binding.fullName.text = u.fullName
            binding.walletDisplay.text = wd

        })
        binding.editname.setOnClickListener {
            dialogView = inflater.inflate(R.layout.name_dialog, container, false)
            val nameDialogBinding = NameDialogBinding.bind(dialogView)
            fullNameLayout = nameDialogBinding.nameContainer
            val builder = AlertDialog.Builder(requireContext())
            builder.setView(dialogView)
            builder.setCancelable(true)
            myDialog = builder.create()
            myDialog.show()
           nameDialogBinding.editNameButton.setOnClickListener {
                if (profileViewModel.validateData(nameDialogBinding.txtFullName.text.toString())) {
                    binding.fullName.text = nameDialogBinding.txtFullName.text.toString()
                    dialogView = inflater.inflate(R.layout.name_dialog, container, false)
                    dialogView.visibility = View.GONE
                    myDialog.cancel()
                }
            }
        }
        profileViewModel.initRecyclerView()

        profileAdapter.setOnItemClickListener(object : ProfileAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> {
                        val intent = Intent(requireContext(), WalletActivity::class.java).apply {
                            putExtra("id", id)
                            putExtra("wallet", wallet)
                        }
                        startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(requireContext(), SettingsActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

        })


        return view
    }

    fun validateFullname(message: String)
    {
        fullNameLayout.error=message

    }

    /*fun walletUpdated(walletParam:Double)
    {
        walletDisplay.text = "%.1f".format(wallet) + " TND"
        Snackbar.make(view2,"Wallet Updated!", Snackbar.LENGTH_LONG).show()
    }*/
}