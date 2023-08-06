package com.example.nbapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.nbapp.Extensions.toast
import com.example.nbapp.databinding.FragmentLoginBinding
import com.example.nbapp.databinding.FragmentSignupBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class SignupFragment : Fragment(R.layout.fragment_signup) {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding = FragmentSignupBinding.bind(view)
        auth = FirebaseAuth.getInstance()

        binding.buttonSignup.setOnClickListener {
            if (
                binding.editTextSignupEmail.text!!.isNotEmpty() &&
                binding.editTextSignupName.text!!.isNotEmpty() &&
                binding.editTextSignupPassword.text!!.isNotEmpty()
            ) {

                createUser(binding.editTextSignupEmail.text.toString(),binding.editTextSignupPassword.text.toString())

            }
        }

    }

    private fun createUser(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {task ->
                if(task.isSuccessful){
                    requireActivity().toast("New User created")

                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_signupFragment_to_loginFragment)

                }
                else{
                    requireActivity().toast(task.exception!!.localizedMessage)
                }

            }

    }

    }