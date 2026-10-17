package com.example.foodorderingwizard

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    private val vm: OrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val menu = findViewById<View>(R.id.menuLayout)
        val container = findViewById<View>(R.id.fragmentContainer)
        val tvSelection = findViewById<TextView>(R.id.tvSelection)

        // 按鈕 → 開啟對應的 Fragment
        findViewById<Button>(R.id.btnMainMeal).setOnClickListener { openStep(MainMealFragment()) }
        findViewById<Button>(R.id.btnSideDishes).setOnClickListener { openStep(SideDishesFragment()) }
        findViewById<Button>(R.id.btnDrink).setOnClickListener { openStep(DrinkFragment()) }
        findViewById<Button>(R.id.btnOrder).setOnClickListener { openStep(ConfirmFragment()) }

        // 觀察 ViewModel，選擇改變就更新主畫面
        vm.order.observe(this) { order ->
            tvSelection.text = "Current Selection:\n" +
                "Main: ${order.mainMeal ?: "—"}\n" +
                "Sides: ${order.sideDishes.joinToString(", ").ifEmpty { "—" }}\n" +
                "Drink: ${order.drink ?: "—"}"
        }

        // 有 Fragment 時隱藏主選單；回到主畫面時再顯示
        supportFragmentManager.addOnBackStackChangedListener { updateVisibility(menu, container) }
        updateVisibility(menu, container)
    }

    private fun updateVisibility(menu: View, container: View) {
        val showingFragment = supportFragmentManager.backStackEntryCount > 0
        menu.visibility = if (showingFragment) View.GONE else View.VISIBLE
        container.visibility = if (showingFragment) View.VISIBLE else View.GONE
    }

    private fun openStep(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}
