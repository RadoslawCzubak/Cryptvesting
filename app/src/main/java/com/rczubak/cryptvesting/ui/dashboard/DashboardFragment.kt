package com.rczubak.cryptvesting.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rczubak.cryptvesting.R
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
                        binding.walletCurrentValueTextView.text = "${ "%.2f".format(it.data.walletValue)} $"
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
                        val balance = "%.2f".format(it.data!!)
                        binding.includeBalance.balanceTextView.apply {
                            text = "$balance $"
                            setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    if (it.data >= 0) R.color.green_600 else R.color.pink_700
                                )
                            )
                        }
                    }
                    Resource.Status.ERROR -> {

                    }
                    Resource.Status.LOADING -> {

                    }

                }
            }
        }
    }

    private fun navigateToAddStatementFragment() {
        val action = DashboardFragmentDirections.actionDashboardFragmentToAddStatementFragment()
        findNavController().navigate(action)
    }

    private fun setupRecyclerView() {
        adapter = DashboardAdapter()
        binding.walletRv.adapter = adapter
        binding.walletRv.layoutManager = LinearLayoutManager(context)
    }
}