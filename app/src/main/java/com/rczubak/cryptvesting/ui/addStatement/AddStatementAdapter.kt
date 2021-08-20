package com.rczubak.cryptvesting.ui.addStatement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rczubak.cryptvesting.R
import com.rczubak.cryptvesting.data.models.domain.CryptoCurrencyModel
import com.rczubak.cryptvesting.data.models.domain.TransactionModel
import com.rczubak.cryptvesting.databinding.ItemTransactionBinding

class AddStatementAdapter(private val onRecyclerViewEmpty: () -> Unit) : RecyclerView.Adapter<AddStatementAdapter.AddStatementViewHolder>() {
    inner class AddStatementViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: TransactionModel){
            binding.buyAmountTv.text = transaction.amount.toString()
            binding.buyCoinTv.text = transaction.buyCoin
            binding.sellAmountTv.text = transaction.price.toString()
            binding.sellCoinTv.text = transaction.sellCoin
            binding.transactionTypeTv.text = transaction.type.name
        }
    }

    private val data = ArrayList<TransactionModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddStatementViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemTransactionBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_transaction,
            parent,
            false
        )
        return AddStatementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddStatementViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        if (data.size == 0){
            onRecyclerViewEmpty()
        }
        return data.size
    }

    fun updateData(data: ArrayList<TransactionModel>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}