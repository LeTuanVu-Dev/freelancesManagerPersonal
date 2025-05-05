package com.freelances.projectmanager.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.freelances.projectmanager.databinding.ItemUserBinding
import com.freelances.projectmanager.model.Personal
import com.freelances.projectmanager.utils.ext.safeClick


class PersonAdapter(
    val onClickItemFeature: (Personal) -> Unit,
    val onClickMore: (View,Personal) -> Unit,
) : ListAdapter<Personal, PersonAdapter.FeatureViewHolder>(
    DIFF_CALLBACK
) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Personal>() {
            override fun areItemsTheSame(
                oldItem: Personal,
                newItem: Personal
            ): Boolean {
                return oldItem.maNv == newItem.maNv
            }

            override fun areContentsTheSame(
                oldItem: Personal,
                newItem: Personal
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeatureViewHolder {
        return FeatureViewHolder(
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FeatureViewHolder(
        private val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Personal) {
            Log.d("FirebaseCheck", "bind: $item")
            binding.tvName.text = item.name
            binding.tvSex.text = item.sex
            binding.tvDate.text = item.date
            binding.tvChucVu.text = item.chucVu
            binding.tvHsl.text = item.hsl
            binding.tvLcb.text = item.lcb
            binding.tvMaNv.text = item.maNv

            binding.ivMore.setOnClickListener { view ->
                onClickMore(view,item)
            }

            binding.root.safeClick {
                onClickItemFeature(item)
            }
        }

    }
}