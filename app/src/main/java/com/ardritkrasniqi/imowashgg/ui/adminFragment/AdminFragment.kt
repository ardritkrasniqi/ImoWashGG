package com.ardritkrasniqi.imowashgg.ui.adminFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ardritkrasniqi.imowashgg.R
import com.ardritkrasniqi.imowashgg.databinding.FragmentAdminBinding
import com.ardritkrasniqi.imowashgg.models.Records
import com.ardritkrasniqi.imowashgg.ui.workFragment.WorkAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class AdminFragment : Fragment() {

    val date = Date()
    var currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
    var currentMonth = SimpleDateFormat("MM", Locale.getDefault()).format(date)
    var currentDay = SimpleDateFormat("dd", Locale.getDefault()).format(date)
    val listPost: MutableList<Records> = mutableListOf()
    private lateinit var binding: FragmentAdminBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminBinding.inflate(inflater)

        val database = FirebaseDatabase.getInstance()

        val myRefSales = database.getReference("data").ref.child("records").child(currentYear)
            .child(currentMonth)
            .child("30").ref
        val recyclerView : RecyclerView? = view?.findViewById(R.id.admin_recycler)



        myRefSales.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot: DataSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(Records::class.java)

                    if (post != null) {
                        listPost.add(post)
                    }
                }


                recyclerView?.adapter = WorkAdapter(listPost)
                Log.i("lista", listPost.toString())
            }
        })

         return binding.root
    }

}