package com.example.androidsampleconfiguration.app.ui.master

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidsampleconfiguration.databinding.DialogAspectsBinding
import com.example.androidsampleconfiguration.databinding.ItemAspectBinding
import dagger.android.support.DaggerDialogFragment

class AspectsDialog : DaggerDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        DialogAspectsBinding.inflate(inflater, container, false).apply {
            setupRecycler(listOf("COLOR", "OTHER1", "OTHER3"))
        }.root

    private fun DialogAspectsBinding.setupRecycler(aspects: List<String>) {
        rvAspectsRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = AspectAdapter(aspects)
            addItemDecoration(createItemDecorator(context))
        }
    }
}

fun createItemDecorator(context: Context): DividerItemDecoration {
    return DividerItemDecoration(context, RecyclerView.HORIZONTAL)
}

class AspectAdapter(val aspects: List<String>) : RecyclerView.Adapter<AspectAdapter.AspectViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AspectViewHolder = AspectViewHolder(
        ItemAspectBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
    )

    override fun getItemCount(): Int = aspects.size

    override fun onBindViewHolder(holder: AspectViewHolder, position: Int) {
        holder.bind(aspects[position])
    }

    class AspectViewHolder(val binding: ItemAspectBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(aspect: String) {
            binding.tvAspectName.text = aspect
        }
    }
}
