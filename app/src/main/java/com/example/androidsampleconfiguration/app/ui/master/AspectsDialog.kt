package com.example.androidsampleconfiguration.app.ui.master

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.State
import com.bumptech.glide.Glide
import com.example.androidsampleconfiguration.app.di.modules.AspectObserver
import com.example.androidsampleconfiguration.app.entity.DialogData
import com.example.androidsampleconfiguration.app.ui.master.AspectAdapter.Aspect
import com.example.androidsampleconfiguration.app.ui.master.AspectAdapter.AspectListener
import com.example.androidsampleconfiguration.commons.extensions.autoNotify
import com.example.androidsampleconfiguration.commons.extensions.translate
import com.example.androidsampleconfiguration.databinding.DialogAspectsBinding
import com.example.androidsampleconfiguration.databinding.ItemAspectBinding
import dagger.android.support.DaggerDialogFragment
import timber.log.Timber
import javax.inject.Inject
import kotlin.properties.Delegates

class AspectsDialog : DaggerDialogFragment(), AspectListener {

    private lateinit var aspectAdapter: AspectAdapter
    private lateinit var binding: DialogAspectsBinding

    @Inject
    lateinit var aspectObserver: AspectObserver

    private val args: AspectsDialogArgs by navArgs()

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
            binding = this
            this@AspectsDialog.isCancelable = false
            this@AspectsDialog.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setupRecycler(args.aspects.toList())
            setupRating()
            setupLayout(args.imageUrl)
        }.root

    private fun DialogAspectsBinding.setupRating() {
        lRating.number = 0
        scoreSelected = false
        lRating.tvScore1.setOnClickListener { setRatingNumber(number = 1) }
        lRating.tvScore2.setOnClickListener { setRatingNumber(number = 2) }
        lRating.tvScore3.setOnClickListener { setRatingNumber(number = 3) }
        lRating.tvScore4.setOnClickListener { setRatingNumber(number = 4) }
        lRating.tvScore5.setOnClickListener { setRatingNumber(number = 5) }
    }

    private fun setRatingNumber(number: Int) {
        with(binding) {
            if (!scoreSelected) scoreSelected = true
            lRating.number = number
        }
    }

    private fun DialogAspectsBinding.setupLayout(imageUrl: String) {
        swipedRight = args.swipedRight
        btnAspectsAccept.setOnClickListener { onAcceptButtonClicked() }

        Glide.with(this@AspectsDialog).load(imageUrl).into(ivPreview)
    }

    private fun onAcceptButtonClicked() {
        val result = aspectAdapter.items.filter { it.selected }.map { it.title }
        aspectObserver.aspectsSubject.onNext(
            DialogData(
                selectedAspects = result.translate(toEnglish = true),
                rating = binding.lRating.number
            )
        )
        findNavController().navigateUp()
    }

    private fun DialogAspectsBinding.setupRecycler(aspects: List<String>) {
        rvAspectsRecycler.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = AspectAdapter(aspectListener = this@AspectsDialog).also { aspectAdapter = it }
            addItemDecoration(SpacesItemDecoration())
        }
        aspectAdapter.items = aspects.translate(toEnglish = false).toAspects()
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

class SpacesItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
        outRect.set(8, 8, 8, 8)
    }
}
