package com.example.a24012011247_mad_practical_7

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PersonAdapter(
    private val personList: ArrayList<Person>,
    private val databaseHelper: DatabaseHelper
) : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PersonViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.single_item,
                parent,
                false
            )

        return PersonViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PersonViewHolder,
        position: Int
    ) {

        val person = personList[position]

        holder.name.text = person.name
        holder.phone.text = person.phone
        holder.email.text = person.email
        holder.address.text = person.address
    }

    override fun getItemCount(): Int {
        return personList.size
    }

    class PersonViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val name: TextView =
            itemView.findViewById(R.id.textview1)

        val phone: TextView =
            itemView.findViewById(R.id.textview2)

        val email: TextView =
            itemView.findViewById(R.id.textview3)

        val address: TextView =
            itemView.findViewById(R.id.textview4)
    }
}