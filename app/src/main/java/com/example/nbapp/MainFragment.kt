package com.example.nbapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nbapp.Extensions.toast
import com.example.nbapp.models.LikeModel
import com.example.nbapp.models.NBDisplayModel
import com.example.nbapp.databinding.FragmentMainBinding
import com.example.nbapp.adapters.*
import com.example.nbapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainFragment : Fragment(), CategoryOnClickInterface, ProductOnClickInterface,
    LikeOnClickInterface {

    private lateinit var binding: FragmentMainBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var productList: ArrayList<NBDisplayModel>
    private lateinit var categoryList: ArrayList<String>
    private lateinit var productsAdapter: NBDisplayAdapter
    private lateinit var categoryAdapter: MainCategoryAdapter
    private lateinit var auth: FirebaseAuth
    private var likeDBRef = Firebase.firestore.collection("LikedProducts")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        FirebaseDatabase.getInstance().getReference("products").setValue("MyTest")
//            .addOnSuccessListener {
//                Log.d("MyTagHere", "onViewCreated: Success")
//            }.addOnFailureListener {
//            Log.d("MyTagHere", "onViewCreated: ${it.message}")
//        }
//        FirebaseDatabase.getInstance("https://nbapp-e09bc-default-rtdb.europe-west1.firebasedatabase.app/")
//            .getReference("products").setValue("").addOnSuccessListener {
//            Log.d("MyTagHere", "onViewCreated: Success")
//        }.addOnFailureListener {
//            Log.d("MyTagHere", "onViewCreated: ${it.message}")
//        }
//        val databaseReference = FirebaseDatabase.getInstance().getReference("products")
//
//                                                  databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // dataSnapshot içindeki tüm children'ları döngüyle geziyoruz
//                    for (childSnapshot in dataSnapshot.children) {
//                        val key = childSnapshot.key // Child düğümünün anahtarı (p1, p2, ...)
//                        val value = childSnapshot.getValue(String::class.java) // Child düğümünün değeri (p1 düğümündeki değer)
//
//                        if (key != null && value != null) {
//                            // İşlemlerinizi burada gerçekleştirin
//                            Log.e("TAG", "Anahtar: $key, Değer: $value")
//                        }
//                    }
//                } else {
//                    // Veritabanında "products" düğümü boşsa veya hiç çocuk düğümü yoksa burada ilgili işlemleri yapabilirsiniz
//                    Log.e("TAG", "Veritabanında products düğümü boş veya hiç çocuk düğümü yok.")
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Veritabanı işlemi iptal edilirse veya hata oluşursa burada işlemleri yapabilirsiniz
//                Log.e("TAG3", "Veritabanı işlemi iptal edildi veya hata oluştu: " + databaseError.message)
//            }
//        })

        val databaseReference = FirebaseDatabase.getInstance("https://nbapp-e09bc-default-rtdb.europe-west1.firebasedatabase.app/").getReference("products")
        Log.d("TAG5", "Example")
        databaseReference.child("imageUrl").setValue("https://firebasestorage.googleapis.com/v0/b/nbapp-e09bc.appspot.com/onb_asus.jpg?alt=media&token=02000902-fdfc-41d3-828c-2bc86e4697b9").addOnSuccessListener {
            Log.d("MyTagHere", "onViewCreated: ")
        }.addOnFailureListener {
            Log.d("MyTagHere", "${it.message}")
        }
        databaseReference.child("imageUrl").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val p1Value = dataSnapshot.getValue(String::class.java)
                    if (p1Value != null) {
                        // p1Value değişkeni, p1 düğümündeki dize değeri içerecektir
                        Log.e("TAG1", "p1 düğümündeki değer: $p1Value")
                    } else {
                        // p1 düğümü boşsa veya değeri null ise burada ilgili işlemleri yapabilirsiniz
                        Log.e("TAG2", "p1 düğümü boş veya değeri null.")
                    }
                } else {
                    // p1 düğümü yoksa burada ilgili işlemleri yapabilirsiniz
                    Log.e("TAG3", "p1 düğümü yok.")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Veritabanı işlemi iptal edilirse veya hata oluşursa burada işlemleri yapabilirsiniz
                Log.e(
                    "TAG4",
                    "Veritabanı işlemi iptal edildi veya hata oluştu: " + databaseError.message
                )
            }
        })


//        binding = FragmentMainBinding.bind(view)
        categoryList = ArrayList()
        productList = ArrayList()
//        databaseReference = FirebaseDatabase.getInstance().getReference("products")
        auth = FirebaseAuth.getInstance()


        // region implements category Recycler view

        categoryList.add("Trending")
        binding.rvMainCategories.setHasFixedSize(true)
        val categoryLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rvMainCategories.layoutManager = categoryLayoutManager
        categoryAdapter = MainCategoryAdapter(categoryList, this)
        binding.rvMainCategories.adapter = categoryAdapter
//        setCategoryList()

        // endregion implements category Recycler view


        // region implements products Recycler view

        val productLayoutManager = GridLayoutManager(context, 2)
        productsAdapter = NBDisplayAdapter(requireContext(), productList, this, this)
        binding.rvMainProductList.layoutManager = productLayoutManager
        binding.rvMainProductList.adapter = productsAdapter
        setProductsData()
        // endregion implements products Recycler view


        binding.bnvMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.mainFragment -> {
                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                        .navigate(R.id.action_mainFragment_self)
                    true
                }

                R.id.likeFragment -> {
//                    requireActivity().toast("Like Page coming Soon")
                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                        .navigate(R.id.action_mainFragment_to_likeFragment2)
                    true
                }

                R.id.cartFragment -> {

                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                        .navigate(R.id.action_mainFragment_to_cartFragment2)

                    true
                }

                R.id.profileFragment -> {

                    auth.signOut()
                    Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
                        .navigate(R.id.action_mainFragment_to_loginFragment)
                    true
                }

                else -> false

            }

        }


    }

    private fun setCategoryList() {

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                categoryList.clear()
                categoryList.add("Trending")

                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val products = dataSnapshot.getValue(NBDisplayModel::class.java)

                        categoryList.add(products!!.brand!!)

                    }

                    categoryAdapter.notifyDataSetChanged()
                }


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }

        }


        databaseReference.addValueEventListener(valueEvent)


    }

    private fun setProductsData() {

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                productList.clear()

                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val products = dataSnapshot.getValue(NBDisplayModel::class.java)
                        productList.add(products!!)
                    }

                    productsAdapter.notifyDataSetChanged()
                }


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }

        }

//        databaseReference.addValueEventListener(valueEvent)

    }

    override fun onClickCategory(button: Button) {
        binding.tvMainCategories.text = button.text

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                productList.clear()

                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val products = dataSnapshot.getValue(NBDisplayModel::class.java)

                        if (products!!.brand == button.text) {
                            productList.add(products)
                        }

                        if (button.text == "Trending") {
                            productList.add(products)
                        }

                    }

                    productsAdapter.notifyDataSetChanged()
                }


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }

        }

        databaseReference.addValueEventListener(valueEvent)


    }

    override fun onClickProduct(item: NBDisplayModel) {

        val direction = MainFragmentDirections
            .actionMainFragmentToDetailsFragment(
                item.id!!
            )

        Navigation.findNavController(requireView())
            .navigate(direction)


    }

    override fun onClickLike(item: NBDisplayModel) {

        likeDBRef
            .add(
                LikeModel(
                    item.id,
                    auth.currentUser!!.uid,
                    item.brand,
                    item.description,
                    item.imageUrl,
                    item.name,
                    item.price
                )
            )
            .addOnSuccessListener {
                requireActivity().toast("Added to Liked Items")
            }
            .addOnFailureListener {
                requireActivity().toast("Failed to Add to Liked")
            }

    }
}