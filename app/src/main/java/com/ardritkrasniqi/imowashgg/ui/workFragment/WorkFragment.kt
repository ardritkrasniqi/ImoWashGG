package com.ardritkrasniqi.imowashgg.ui.workFragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.ardritkrasniqi.imowashgg.R
import com.ardritkrasniqi.imowashgg.databinding.WorkFragmentBinding
import com.ardritkrasniqi.imowashgg.models.Counter
import com.ardritkrasniqi.imowashgg.models.Records
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


private const val STORN0KEY = "STORNOKEY"
private const val EDITED = "EDITED"

class WorkFragment : Fragment() {


    private var sharedPreff: SharedPreferences? = null
    private lateinit var binding: WorkFragmentBinding
    private lateinit var database: FirebaseDatabase
    var date = Date()
    private val timeFormat = SimpleDateFormat("HH:mm")
    var currentItemClicked: String? = null
    var felgen2Count = 0
    var felgen3Count = 0
    var expressCount = 0
    var lastItem = ""
    var canDelete = false
    var currentYear: String = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
    var currentMonth: String = SimpleDateFormat("MM", Locale.getDefault()).format(date)
    var currentDay: String = SimpleDateFormat("dd", Locale.getDefault()).format(date)
    var currentItemAdded: String? = null
    var firstTimeAdded = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WorkFragmentBinding.inflate(inflater)
        database = FirebaseDatabase.getInstance()
        sharedPreff = activity?.getSharedPreferences(STORN0KEY, Context.MODE_PRIVATE)
        sharedPreff?.edit()?.putBoolean(EDITED, false)?.apply()
        binding.stornoButton?.isEnabled =
            sharedPreff?.getBoolean(EDITED, false)!!


        val myRef3 = database.getReference("data").ref.child("counter").child(currentYear)
            .child(currentMonth)
            .child(currentDay).ref

        val myRefSales = database.getReference("data").ref.child("records").child(currentYear)
            .child(currentMonth)
            .child(currentDay).ref


        val date: String =
            SimpleDateFormat("EEEE dd-MMMM, yyyy", Locale.getDefault()).format(Date())

        binding.date.text = date

        binding.parentLayout?.setOnClickListener {
            binding.constraintLayout10.setBackgroundColor(resources.getColor(R.color.whiteColor))
            binding.constraintLayout9.setBackgroundColor(resources.getColor(R.color.whiteColor))
            binding.constraintLayout11.setBackgroundColor(resources.getColor(R.color.whiteColor))
            currentItemClicked = null
        }

        binding.felgen2e.setOnClickListener {
            cardColorTrasnition(binding.constraintLayout10)
            binding.constraintLayout9.setBackgroundColor(resources.getColor(R.color.whiteColor))
            binding.constraintLayout11.setBackgroundColor(resources.getColor(R.color.whiteColor))
            currentItemClicked = "felgen_2"
        }

        binding.felgen3e.setOnClickListener {
            binding.constraintLayout10.setBackgroundColor(resources.getColor(R.color.whiteColor))
            binding.constraintLayout11.setBackgroundColor(resources.getColor(R.color.whiteColor))
            cardColorTrasnition(binding.constraintLayout9)
            currentItemClicked = "felgen_3"
        }

        binding.express18e.setOnClickListener {
            binding.constraintLayout9.setBackgroundColor(resources.getColor(R.color.whiteColor))
            binding.constraintLayout10.setBackgroundColor(resources.getColor(R.color.whiteColor))
            cardColorTrasnition(binding.constraintLayout11)
            currentItemClicked = "express"
        }

        binding.stornoButton?.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Vorsichtig")
                .setMessage("Sind Sie sicher, dass Sie den letzten Dienst löschen möchten?")
                .setNegativeButton("Nicht", DialogInterface.OnClickListener { dialog, _ ->
                    run {
                        dialog.dismiss()
                        binding.stornoButton?.isEnabled = false
                    }
                })
                .setPositiveButton("Ja") { _, _ ->
                    run {
                        myRefSales.child(lastItem).child("changed").setValue(true)
                        if (currentItemAdded != null) {
                            myRef3.setValue(
                                when (currentItemAdded) {
                                    "felgen_2" -> Counter(
                                        expressCount,
                                        felgen2Count - 1,
                                        felgen3Count
                                    )
                                    "felgen_3" -> Counter(
                                        expressCount,
                                        felgen2Count,
                                        felgen3Count - 1
                                    )
                                    "express" -> Counter(
                                        expressCount - 1,
                                        felgen2Count,
                                        felgen3Count
                                    )
                                    else -> Counter(expressCount, felgen2Count, felgen3Count)
                                }
                            )
                        }
                        sharedPreff?.edit()?.putBoolean(EDITED, false)?.apply()
                        binding.stornoButton?.isEnabled =
                            sharedPreff?.getBoolean(EDITED, false)!!
                    }
                }.show()
        }

        binding.sendDataButton?.setOnClickListener {
            if (currentItemClicked != null) {
                when (currentItemClicked) {
                    "felgen_2" -> {
                        felgen2Count++
                        Toast.makeText(
                            requireContext(),
                            "Service Felgen2 wurde hinzugefügt",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    "felgen_3" -> {
                        felgen3Count++
                        Toast.makeText(
                            requireContext(),
                            "Service Felgen3 wurde hinzugefügt",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    "express" -> {
                        expressCount++
                        Toast.makeText(
                            requireContext(),
                            "Service Express wurde hinzugefügt",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                myRef3.setValue(Counter(expressCount, felgen2Count, felgen3Count))
                myRefSales.child(System.currentTimeMillis().toString()).setValue(
                    Records(
                        "false", timeFormat.format(Date()),
                        currentItemClicked!!
                    )
                )
                firstTimeAdded++
                currentItemAdded = currentItemClicked!!
                currentItemClicked = null
                binding.constraintLayout10.setBackgroundColor(resources.getColor(R.color.whiteColor))
                binding.constraintLayout9.setBackgroundColor(resources.getColor(R.color.whiteColor))
                binding.constraintLayout11.setBackgroundColor(resources.getColor(R.color.whiteColor))

            } else {
                Toast.makeText(
                    requireContext(),
                    "Bitte wählen sie ein produkt aus",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        myRefSales.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.hasChildren()) {
                    val children = snapshot.children.last() ?: null
                    lastItem = children?.key.toString()
                }
                canDelete = true
                val listPost = mutableListOf<Records>()
                for (postSnapshot: DataSnapshot in snapshot.children) {
                    val post = postSnapshot.getValue(Records::class.java)

                    if (post != null) {
                        listPost.add(post)
                    }
                }

                listPost.reverse()
                binding.workRecycerView?.adapter = WorkAdapter(listPost)
            }
        })

        myRefSales.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.i("myRefSales", "Child cancelled")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.i("myRefSales", "Child Moved")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                sharedPreff!!.edit().putBoolean(EDITED, false).apply()
                binding.stornoButton?.isEnabled =
                    sharedPreff!!.getBoolean(EDITED, false)


            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                sharedPreff!!.edit().putBoolean(EDITED, true).apply()
                if (firstTimeAdded > 0) {
                    binding.stornoButton?.isEnabled = true
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.i("myRefSales", "Child removed")
            }

        })

        myRef3.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value =
                    dataSnapshot.getValue(Counter::class.java)
                binding.felgen1Amount?.text = value?.felge_2.toString()
                felgen2Count = value?.felge_2 ?: 0
                binding.felgen2Amount?.text = value?.felge_3.toString()
                felgen3Count = value?.felge_3 ?: 0
                binding.expressAmount?.text = value?.express.toString()
                expressCount = value?.express ?: 0
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("-->", "Erro: ${error.details}")

            }
        })

        myRef3.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
                binding.workRecycerView?.adapter?.notifyDataSetChanged()
                Log.i("chilld", "child added")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                binding.workRecycerView?.adapter?.notifyDataSetChanged()

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                binding.workRecycerView?.adapter?.notifyDataSetChanged()
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                binding.workRecycerView?.adapter?.notifyDataSetChanged()

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                binding.workRecycerView?.adapter?.notifyDataSetChanged()
            }

        })

        return binding.root
    }


    fun fireBaseTimeStamp(sdf: SimpleDateFormat): String {
        var estimatedServerTimeMs = 0L
        var rawDate: Date = Date(estimatedServerTimeMs)
        val offsetRef =
            FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset")
        offsetRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val offset =
                    snapshot.getValue(Long::class.java)!!
                estimatedServerTimeMs =
                    System.currentTimeMillis() + offset
                rawDate = Date(estimatedServerTimeMs)


                Log.i("formatedTime2", estimatedServerTimeMs.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                System.err.println("Listener was cancelled")
            }
        })

        return sdf.format(rawDate)
    }

    fun cardColorTrasnition(card: ConstraintLayout) {
        val color = arrayOf(
            ColorDrawable(Color.WHITE),
            ColorDrawable(resources.getColor(R.color.highlightedColor))
        )

        val transiton = TransitionDrawable(color)
        card.background = transiton
        transiton.startTransition(200)
    }
}