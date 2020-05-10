package com.example.androidsampleconfiguration.app.ui.master

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidsampleconfiguration.app.di.modules.AspectObserver
import com.example.androidsampleconfiguration.app.ui.master.AspectAdapter.Aspect
import com.example.androidsampleconfiguration.app.ui.master.AspectAdapter.AspectListener
import com.example.androidsampleconfiguration.commons.extensions.autoNotify
import com.example.androidsampleconfiguration.databinding.DialogAspectsBinding
import com.example.androidsampleconfiguration.databinding.ItemAspectBinding
import dagger.android.support.DaggerDialogFragment
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates

class AspectsDialog : DaggerDialogFragment(), AspectListener {

    private lateinit var aspectAdapter: AspectAdapter

    @Inject
    lateinit var aspectObserver: AspectObserver

    val args: AspectsDialogArgs by navArgs()

    override fun aspectClicked(aspect: Aspect) {
        Timber.d("Aspect ${aspect.title} clicked")
        aspectAdapter.items = aspectAdapter.items.map {
            if (aspect.title == it.title) {
                it.copy(selected = !it.selected)
            } else {
                it
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        DialogAspectsBinding.inflate(inflater, container, false).apply {
            setupRecycler(args.aspects.toList())
            setupLayout()
        }.root

    private fun DialogAspectsBinding.setupLayout() {
        btnAspectsAccept.setOnClickListener {
            val result = aspectAdapter.items.filter { it.selected }.map { it.title }
            aspectObserver.aspectsSubject.onNext(result)
            findNavController().navigateUp()
        }
    }

    private fun DialogAspectsBinding.setupRecycler(aspects: List<String>) {
        rvAspectsRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = AspectAdapter(aspectListener = this@AspectsDialog).also { aspectAdapter = it }
        }
        aspectAdapter.items = aspects.toAspects()
    }

}

fun List<String>.toAspects(): List<Aspect> = map { Aspect(title = it.capitalize(), selected = false) }

class AspectAdapter(private val aspectListener: AspectListener) :
    RecyclerView.Adapter<AspectAdapter.AspectViewHolder>() {

    var items: List<Aspect> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { other -> other.title == title && other.selected == selected }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AspectViewHolder = AspectViewHolder(
        binding = ItemAspectBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        ),
        aspectListener = aspectListener
    )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: AspectViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class AspectViewHolder(val binding: ItemAspectBinding, private val aspectListener: AspectListener) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(aspect: Aspect) {
            binding.tvAspectName.text = aspect.title
            binding.selected = aspect.selected
            binding.tvAspectName.setOnClickListener {
                aspectListener.aspectClicked(aspect = aspect)
            }
        }
    }

    data class Aspect(val title: String, val selected: Boolean)

    interface AspectListener {
        fun aspectClicked(aspect: Aspect)
    }
}
