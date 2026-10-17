package com.example.foodorderingwizard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ConfirmFragment : Fragment() {

    // 與 MainActivity 共用 ViewModel
    private val vm: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_confirm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvSummary = view.findViewById<TextView>(R.id.tvOrderSummary)
        val btnConfirm = view.findViewById<Button>(R.id.btnConfirmOrder)

        // 1. 讀取目前選擇的餐點並顯示在畫面上
        val order = vm.order.value
        if (order != null) {
            val mainText = order.mainMeal ?: "-"
            val sidesText = if (order.sideDishes.isNotEmpty()) order.sideDishes.joinToString(", ") else "-"
            val drinkText = order.drink ?: "-"

            tvSummary.text = "Main: $mainText\nSides: $sidesText\nDrink: $drinkText"
        }

        // 2. 按下 Confirm 按鈕後的驗證與彈窗處理
        btnConfirm.setOnClickListener {
            val currentOrder = vm.order.value

            // 檢查餐點是否完整選取 (isComplete)
            if (currentOrder?.isComplete() != true) {
                Toast.makeText(
                    requireContext(),
                    "Please select a main meal, at least one side dish, and a drink.",
                    Toast.LENGTH_LONG
                ).show()
                parentFragmentManager.popBackStack()
                return@setOnClickListener
            }

            // 餐點完整，跳出 AlertDialog 確認提交
            AlertDialog.Builder(requireContext())
                .setTitle("Submit Order")
                .setMessage(
                    "Main: ${currentOrder.mainMeal}\n" +
                            "Sides: ${currentOrder.sideDishes.joinToString(", ")}\n" +
                            "Drink: ${currentOrder.drink}\n\nSubmit this order?"
                )
                .setPositiveButton("Submit") { dialog, _ ->
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "Order submitted!", Toast.LENGTH_SHORT).show()

                    // 重置訂單資料並跳回主畫面
                    //vm.reset()

                    parentFragmentManager.popBackStack()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                    // 點擊 Cancel 後，關閉彈窗並返回主畫面
                    parentFragmentManager.popBackStack()
                }
                .show()
        }
    }
}