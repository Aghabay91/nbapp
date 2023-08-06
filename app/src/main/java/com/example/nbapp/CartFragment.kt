package com.example.nbapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nbapp.Extensions.toast
import com.example.nbapp.adapters.CartAdapter
import com.example.nbapp.databinding.FragmentCartBinding
import com.example.nbapp.databinding.FragmentLoginBinding
import com.example.nbapp.models.CartModel
import com.example.nbapp.models.NBDisplayModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class CartFragment : Fragment(), CartAdapter.OnLongClickRemove {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartList: ArrayList<CartModel>
    private lateinit var ordersAdapter: CartAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: CartAdapter
    private var subTotalPrice = 0
    private var totalPrice = 240

    private var orderDatabaseReference = Firebase.firestore.collection("orders")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.firestore
        val shoppingItemsRef = db.collection("orders")

        shoppingItemsRef.get().addOnSuccessListener { value ->
            if (value!=null){
                try {
                    for (document in value.documents){
                        val pid = document.get("pid") as String
                        val uid = document.get("uid") as String
                        val imageUrl = document.get("imageUrl") as String
                        val name = document.get("name") as String
                        val price = document.get("price") as String
                        val size = document.get("size") as String
                        val quantity = document.get("quantity") as Int
                        val orders = CartModel(pid, uid, imageUrl, name, price, size, quantity)
                        cartList.add(orders)
                        Log.e("Order", name)
                    }
                    ordersAdapter.update(cartList)
                }catch (e: Exception){

                }
            }

        }

//        binding = FragmentCartBinding.bind(view)
        auth = FirebaseAuth.getInstance()

        binding.cartActualToolbar.setNavigationOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }


        val layoutManager = LinearLayoutManager(context)


        cartList = ArrayList()

        retrieveCartItems()



        adapter = CartAdapter(requireContext(),cartList ,this)
        binding.rvCartItems.adapter = adapter
        binding.rvCartItems.layoutManager = layoutManager






        binding.btnCartCheckout.setOnClickListener {

            requireActivity().toast("Whooooa!! You've Ordered Products worth ${totalPrice}\n Your Product will be delivered in next 7 days")
            cartList.clear()
            binding.tvLastSubTotalprice.text ="0"
            binding.tvLastTotalPrice.text ="Min 1 product is Required"
            binding.tvLastTotalPrice.setTextColor(Color.RED)
            // TODO: remove the data of the Products from the fireStore after checkout or insert a boolean isDelivered
            adapter.notifyDataSetChanged()
        }


    }

    private fun retrieveCartItems() {

        orderDatabaseReference
            .whereEqualTo("uid",auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (item in querySnapshot) {
                    val cartProduct = item.toObject<CartModel>()


                    cartList.add(cartProduct)
                    subTotalPrice += cartProduct.price!!.toInt()
                    totalPrice += cartProduct.price!!.toInt()
                    binding.tvLastSubTotalprice.text = subTotalPrice.toString()
                    binding.tvLastTotalPrice.text = totalPrice.toString()
                    binding.tvLastSubTotalItems.text = "SubTotal Items(${cartList.size})"
                    adapter.notifyDataSetChanged()


                }

            }
            .addOnFailureListener{
                requireActivity().toast(it.localizedMessage!!)
            }


    }
    override fun onLongRemove(item: CartModel , position:Int) {


        orderDatabaseReference
            .whereEqualTo("uid",item.uid)
            .whereEqualTo("pid",item.pid)
            .whereEqualTo("size",item.size)
            .get()
            .addOnSuccessListener { querySnapshot ->

                for (item in querySnapshot){
                    orderDatabaseReference.document(item.id).delete()
                    cartList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    requireActivity().toast("Removed Successfully!!!")
                }

            }
            .addOnFailureListener {
                requireActivity().toast("Failed to remove")
            }



    }
}