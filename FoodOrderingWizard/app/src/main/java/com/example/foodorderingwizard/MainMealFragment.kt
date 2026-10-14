package com.example.foodorderingwizard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class MainMealFragment : Fragment() {

    // 和 MainActivity 共用同一個 ViewModel
    private val vm: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_meal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rgMainMeal = view.findViewById<RadioGroup>(R.id.rgMainMeal)

        // 如果之前選過，把原本的選項勾回來
        val current = vm.order.value?.mainMeal
        for (i in 0 until rgMainMeal.childCount) {
            val rb = rgMainMeal.getChildAt(i) as RadioButton
            if (rb.text.toString() == current) rb.isChecked = true
        }

        view.findViewById<Button>(R.id.btnMainMealDone).setOnClickListener {
            val checkedId = rgMainMeal.checkedRadioButtonId
            if (checkedId == -1) {
                Toast.makeText(requireContext(), "Please select a main meal.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            vm.setMainMeal(view.findViewById<RadioButton>(checkedId).text.toString())
            parentFragmentManager.popBackStack()  // 回到主畫面
        }
    }
}
