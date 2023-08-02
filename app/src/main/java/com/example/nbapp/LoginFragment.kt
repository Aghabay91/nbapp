package com.example.nbapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.nbapp.Extensions.toast
import com.example.nbapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.log

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("TAG", "onViewCreated: ", )
//        binding = FragmentLoginBinding.bind(view)
        auth = FirebaseAuth.getInstance()

//        if (auth.currentUser != null) {
//            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
//                .navigate(R.id.action_loginFragment_to_mainFragment)
//        }

        binding.buttonSignin.setOnClickListener {

            if (
                binding.editTextLoginEmail.text!!.isNotEmpty() &&
                binding.editTextLoginPassword.text!!.isNotEmpty()
            ) {


                signinUser(binding.editTextLoginEmail.text.toString(),
                    binding.editTextLoginPassword.text.toString())


            } else {
                requireActivity().toast("Some Fields are Empty")
            }

        }

        binding.textViewSignup.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                .navigate(R.id.action_loginFragment_to_signupFragment)
        }

    }

    private fun signinUser(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    requireActivity().toast("Sign In Successful")

                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                        .navigate(R.id.action_loginFragment_to_mainFragment)
                } else {
                    requireActivity().toast(task.exception!!.localizedMessage!!)

                }


            }

    }

    }