package com.ardritkrasniqi.imowashgg.ui.loginFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ardritkrasniqi.imowashgg.R
import com.ardritkrasniqi.imowashgg.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth


private lateinit var binding: FragmentLoginBinding
private lateinit var firebaseAuth: FirebaseAuth

class LoginFragment : Fragment() {

    private var email: String = ""
    private var password: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater)
        firebaseAuth = FirebaseAuth.getInstance()
        hideKeyboard()

        binding.employeeButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_workFragment)
        }


        binding.bossButton.setOnClickListener {
            email = binding.emailEdit.text.toString()
            password = binding.passwordEdit.text.toString()



            if(email.isNotEmpty() && password.isNotEmpty()){
                loginUser(email, password)
            } else {
                Toast.makeText(this.requireContext(), "Mbushi gjitha te dhenat", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    private fun loginUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()){ task ->
            if(task.isSuccessful){
                findNavController().navigate(R.id.action_loginFragment_to_adminPanel)
            } else{
                Toast.makeText(this.requireContext(), "Authentikimi deshtoi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hideKeyboard() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

}