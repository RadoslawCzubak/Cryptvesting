package com.rczubak.cryptvesting.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rczubak.cryptvesting.R
import com.rczubak.cryptvesting.common.Error
import com.rczubak.cryptvesting.common.Resource
import com.rczubak.cryptvesting.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import timber.log.Timber

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    lateinit var binding: FragmentDashboardBinding
    private lateinit var adapter: DashboardAdapter
    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        return binding.root
    }

    @InternalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupRecyclerView()
        setObservers()
        viewModel.observeProfit()
        viewModel.getWallet()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_dashboard, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_statement -> {
                navigateToAddStatementFragment()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        viewModel.apply {
            wallet.observe(viewLifecycleOwner) {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        adapter.updateData(ArrayList(it.data!!.walletCoins))
                        binding.walletCurrentValueTextView.text =
                            "${"%.2f".format(it.data.walletValue)} $"
                    }
                    Resource.Status.LOADING -> {
                        Timber.d("Loading")
                    }
                    Resource.Status.ERROR -> {
                        Timber.d("Error")
                    }
                }
            }

            profit.observe(viewLifecycleOwner) {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        setBalance(it.data!!)
                    }
                    Resource.Status.ERROR -> {
                        when (it.code) {
                            Error.NO_CRYPTO_OWNED.code -> onNoCrypto()
                            else -> onUnknownError()
                        }
                    }
                    Resource.Status.LOADING -> {
                    }
                }
            }
        }
    }

    private fun onUnknownError() {
        Snackbar.make(requireView(), "Unexpected error occurred", Snackbar.LENGTH_SHORT)
    }

    private fun onNoCrypto() {
        setBalance(0.0)
        Snackbar.make(requireView(), "You have no coins in your wallet!", Snackbar.LENGTH_SHORT)
    }


    private fun navigateToAddStatementFragment() {
        val action = DashboardFragmentDirections.actionDashboardFragmentToAddStatementFragment()
        findNavController().navigate(action)
    }

    private fun setupRecyclerView() {
        adapter = DashboardAdapter()
        binding.walletRv.adapter = adapter
        val layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
            }

        binding.walletRv.layoutManager = layoutManager
    }

    @SuppressLint("SetTextI18n")
    private fun setBalance(balance: Double) {
        binding.includeBalance.balanceTextView.apply {
            val balanceString = "%.2f".format(balance)
            text = "$balanceString $"
            setTextColor(
                ContextCompat.getColor(
                    context,
                    if (balance >= 0) R.color.green_600 else R.color.pink_700
                )
            )
        }
    }
}