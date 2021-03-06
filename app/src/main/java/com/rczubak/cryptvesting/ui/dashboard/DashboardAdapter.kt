package com.rczubak.cryptvesting.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.rczubak.cryptvesting.R
import com.rczubak.cryptvesting.common.toPx
import com.rczubak.cryptvesting.databinding.ItemCoinBinding
import com.rczubak.cryptvesting.domain.model.WalletCoin
import kotlin.math.pow
import kotlin.math.round
import kotlin.random.Random

class DashboardAdapter : RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    private val data = ArrayList<WalletCoin>()
    private val random = Random(1)

    inner class ViewHolder(private val binding: ItemCoinBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(coin: WalletCoin) {
            binding.itemCoinAmount.text = coin.amount.toString()
            binding.itemCoinSymbol.text = coin.currencySymbol
            binding.itemCoinValue.text =
                if (coin.valueInUSD != null) "${
                    round((coin.valueInUSD * 10.0.pow(6.0))) / 10.0.pow(6.0)
                } $" else "N/D"
            binding.imageView.load(
                coin.logoUrl
            ) {
                crossfade(500)
                transformations(CircleCropTransformation())
                scale(Scale.FIT)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCoinBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_coin, parent, false
        )
        binding.coinCard.apply {
            val params = layoutParams
            params.height = random.nextInt(200, 300).toPx(parent.context)
            layoutParams = params
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun updateData(wallet: ArrayList<WalletCoin>) {
        data.clear()
        data.addAll(wallet)
        notifyDataSetChanged()
    }
}