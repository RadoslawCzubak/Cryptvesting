package com.rczubak.cryptvesting.ui.dashboard

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rczubak.cryptvesting.R
import com.rczubak.cryptvesting.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupRecyclerView()
        setObservers()
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

    private fun setObservers() {
        viewModel.apply {
            walletCoins.observe(viewLifecycleOwner) {
                adapter.updateData(it)
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