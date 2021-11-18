package com.rczubak.cryptvesting.ui.addStatement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rczubak.cryptvesting.R
import com.rczubak.cryptvesting.databinding.FragmentAddStatementBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddStatementFragment : Fragment() {

    private val viewModel: AddStatementViewModel by viewModels()
    private lateinit var binding: FragmentAddStatementBinding
    private lateinit var adapter: AddStatementAdapter
    private val openFileLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            val inputStream = requireContext().contentResolver?.openInputStream(uri)
            viewModel.readFile(inputStream!!)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_statement, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setOnClickListeners()
        setObservers()
        binding.chooseStatementButton.setBackgroundResource(R.drawable.button_background)
    }

    private fun setupRecyclerView() {
        adapter = AddStatementAdapter { isEmpty: Boolean ->
            setNoValuesView(isEmpty)
        }
        binding.apply {
            newStatementRv.adapter = adapter
            newStatementRv.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setOnClickListeners() {
        binding.chooseStatementButton.setOnClickListener {
            openFileLauncher.launch("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            binding.apply {
                newStatementBtns.visibility = View.VISIBLE
                chooseStatementButton.visibility = View.GONE
            }
        }

        binding.saveButton.setOnClickListener {
            viewModel.saveTransactions()
        }

        binding.cancelButton.setOnClickListener {
            viewModel.cancelTransactions()
            binding.apply {
                newStatementBtns.visibility = View.GONE
                chooseStatementButton.visibility = View.VISIBLE
            }
        }
    }

    private fun setObservers() {
        viewModel.transactionsToAdd.observe(viewLifecycleOwner) {
            adapter.updateData(it)
            binding.newStatementBtns.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.transactionsSaveStatus.observe(viewLifecycleOwner) {
            if (it.getContentIfNotHandled() == true) {
                showSnackbar()
                val action =
                    AddStatementFragmentDirections.actionAddStatementFragmentToDashboardFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun setNoValuesView(isEmpty: Boolean) {
        binding.emptyTransactionsView.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showSnackbar() {
        Snackbar.make(this.requireView(), R.string.save_positive, Snackbar.LENGTH_SHORT).show()
    }

}