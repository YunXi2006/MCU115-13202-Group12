package com.example.foodorderingwizard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class SideDishesFragment : Fragment() {

    // 和 MainActivity 共用同一個 ViewModel
    private val vm: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_side_dishes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val checkBoxes = listOf(
            view.findViewById<CheckBox>(R.id.rbFries),
            view.findViewById<CheckBox>(R.id.rbSalad),
            view.findViewById<CheckBox>(R.id.rbCornCup)
        )

        // 如果之前選過，把原本的選項勾回來
        val current = vm.order.value?.sideDishes ?: emptyList()
        for (cb in checkBoxes) {
            cb.isChecked = cb.text.toString() in current
        }

        view.findViewById<Button>(R.id.btnSideDishesDone).setOnClickListener {
            val selected = checkBoxes.filter { it.isChecked }.map { it.text.toString() }
            if (selected.isEmpty()) {
                Toast.makeText(requireContext(), "Please select at least one side dish.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            vm.setSideDishes(selected)
            parentFragmentManager.popBackStack()  // 回到主畫面
        }
    }
}