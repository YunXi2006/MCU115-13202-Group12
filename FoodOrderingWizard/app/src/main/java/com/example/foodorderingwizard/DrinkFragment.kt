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

class DrinkFragment : Fragment(){

  // 和 MainActivity 共用同一個 ViewModel
        private val vm: OrderViewModel by activityViewModels()

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_drink, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val rgDrink = view.findViewById<RadioGroup>(R.id.rgDrink)

            // 1. 如果之前選過，把原本的選項勾回來（單選）
            val current = vm.order.value?.drink
            for (i in 0 until rgDrink.childCount) {
                val rb = rgDrink.getChildAt(i) as? RadioButton
                if (rb?.text?.toString() == current) {
                    if (rb != null) {
                        rb.isChecked = true
                    }
                    break
                }
            }

            for (i in 0 until rgDrink.childCount) {
                val rb = rgDrink.getChildAt(i) as? RadioButton
                rb?.setOnClickListener {
                    // 檢查點擊前是不是已經被勾選了
                    // 這裡可以透過記錄上一次的狀態，或者用 tag 來處理
                    val clickedText = rb.text.toString()
                    if (vm.order.value?.drink == clickedText) {
                        // 如果再點一次同一個，就清空選擇並清除 RadioGroup 勾選
                        rgDrink.clearCheck()
                        vm.setDrink("") // 或者看你的 ViewModel 怎麼設計清空飲料
                    } else {
                        vm.setDrink(clickedText)
                    }
                }
            }

            // 2. 點擊 Done 按鈕後的處理
            view.findViewById<Button>(R.id.btnDrinkDone).setOnClickListener {
                val checkedId = rgDrink.checkedRadioButtonId
                if (checkedId == -1) {
                    Toast.makeText(requireContext(), "Please select a drink", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // 取得選中的 RadioButton 文字，寫入 ViewModel
                val selectedDrink = view.findViewById<RadioButton>(checkedId).text.toString()
                vm.setDrink(selectedDrink)

                // 回到主畫面
                parentFragmentManager.popBackStack()
            }
        }
    }
