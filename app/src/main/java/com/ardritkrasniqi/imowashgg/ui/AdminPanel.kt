package com.ardritkrasniqi.imowashgg.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.recyclerview.widget.RecyclerView
import com.ardritkrasniqi.imowashgg.R
import com.ardritkrasniqi.imowashgg.databinding.FragmentAdminBinding
import com.ardritkrasniqi.imowashgg.databinding.FragmentAdminPanelBinding
import com.ardritkrasniqi.imowashgg.models.Records
import com.ardritkrasniqi.imowashgg.ui.workFragment.WorkAdapter
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class AdminPanel : Fragment() {

    val date = Date()
    var currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
    var currentMonth = SimpleDateFormat("MM", Locale.getDefault()).format(date)
    var currentDay = SimpleDateFormat("dd", Locale.getDefault()).format(date)
    val listPost: MutableList<Records> = mutableListOf()

    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentAdminPanelBinding
    private lateinit var calendar: CalendarView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminPanelBinding.inflate(inflater)

        val database = FirebaseDatabase.getInstance()
        val myRefSales = database.getReference("data").ref.child("records").child(currentYear)
            .child(currentMonth).ref

        recyclerView = binding.adminRecycler
        recyclerView.adapter = AdminAdapter(listPost)
        calendar = binding.kalendari!!

        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var dayOfMonthGood = ""
            dayOfMonthGood = if (dayOfMonth < 10){
                "0$dayOfMonth"
            } else{
                dayOfMonth.toString()
            }
            myRefSales.child(dayOfMonthGood).addValueEventListener(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {

                    val listPost = mutableListOf<Records>()
                    for (postSnapshot: DataSnapshot in snapshot.children) {
                        val post = postSnapshot.getValue(Records::class.java)
                        if (post != null) {
                            listPost.add(post)
                        }
                    }

                    listPost.reverse()
                    recyclerView.adapter = AdminAdapter(listPost)
                }
            })
        }

        myRefSales.child(currentDay).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val listPost = mutableListOf<Records>()
                for (postSnapshot: DataSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(Records::class.java)
                    if (post != null) {
                        listPost.add(post)
                    }
                }
                listPost.reverse()
                recyclerView.adapter = AdminAdapter(listPost)
            }
        })

        myRefSales.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(error: DatabaseError) {
                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                recyclerView.adapter?.notifyDataSetChanged()
            }

        })

        return binding.root
    }

}