package com.example.mycalculator

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.Menu
import android.view.MenuItem
import com.example.mycalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private var currentInput = ""
    private var firstNumber = 0.0
    private var operator = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnZero.setOnClickListener { onDigit("0") }
        binding.btnOne.setOnClickListener { onDigit("1") }
        binding.btnTwo.setOnClickListener { onDigit("2") }
        binding.btnThree.setOnClickListener { onDigit("3") }
        binding.btnFour.setOnClickListener { onDigit("4") }
        binding.btnFive.setOnClickListener { onDigit("5") }
        binding.btnSix.setOnClickListener { onDigit("6") }
        binding.btnSeven.setOnClickListener { onDigit("7") }
        binding.btnEight.setOnClickListener { onDigit("8") }
        binding.btnNine.setOnClickListener { onDigit("9") }

        binding.btnPLus.setOnClickListener { onOperator("+") }
        binding.btnMinus.setOnClickListener { onOperator("-") }
        binding.btnMultiply.setOnClickListener { onOperator("×") }
        binding.btnDivide.setOnClickListener { onOperator("÷") }

        binding.allClear.setOnClickListener {
            currentInput = ""
            firstNumber = 0.0
            operator = ""
            binding.tvInput.text = currentInput
        }

        binding.btnEqual.setOnClickListener { onEqual() }







    }
    private fun onDigit(digit: String) {
        currentInput += digit
        binding.tvInput.text = currentInput
    }

    private fun onOperator(op: String){
        if (currentInput.isEmpty()) return

        binding.tvCurrentOperand.text = "$op"
        binding.tvOldInput.text = "$currentInput $op"
        firstNumber = currentInput.toDouble()

        operator = op
        currentInput = ""
    }
    private fun onEqual(){
        if (currentInput.isEmpty()) return
        var secondNumber = currentInput.toDouble()
        val result = when (operator) {
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "×" -> firstNumber * secondNumber
            "÷" -> firstNumber / secondNumber

            else -> secondNumber
        }
        binding.tvInput.text = result.toString()
        binding.tvOldInput.text = "${binding.tvOldInput.text} $currentInput ="
        binding.tvCurrentOperand.text = ""
        currentInput = result.toString()
    }


}