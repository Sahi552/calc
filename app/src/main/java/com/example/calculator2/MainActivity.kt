package com.example.calculator2

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator2.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var lastnumeric = false
    var stateError = false
    var lastDot = false

    private lateinit var expression:Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //number button
        val numberbtn = arrayOf(
            binding.one ,binding.two ,binding.three ,binding.four ,binding.five ,binding.six ,
            binding.seven ,binding.eight ,binding.nine ,binding.zero
        )

        for (btn in numberbtn){
            btn.setOnClickListener { ondigitclick(it) }
        }

        //operation btn
        val operatorbtn = arrayOf(
            binding.add ,binding.sub ,binding.mul ,binding.div ,binding.percent ,binding.dot
        )

        for (btn in operatorbtn){
            btn.setOnClickListener { onoperatorclick(it) }
        }

        //special btn
        binding.allclear.setOnClickListener { onallclearclick() }
        binding.clear.setOnClickListener { onclearclick() }
        binding.back.setOnClickListener { onbackclick() }
        binding.equal.setOnClickListener { onequalclick() }

    }

    private fun evalExpression(){

        if (lastnumeric && !stateError){
            val txt = binding.qnsView.text.toString()

            expression = ExpressionBuilder(txt).build()
            try {
                val result = expression.evaluate()
                binding.ansView.text = buildString {
        append("=")
        append(result)
    }

            }catch (e :ArithmeticException){
                binding.ansView.text = "error"
                stateError = true
                lastnumeric = false
            }
        }
    }


    private fun onequalclick() {
        evalExpression()
        binding.qnsView.text = ""
    }

    private fun onbackclick() {
        if (binding.qnsView.text.toString().isNotEmpty()){
            binding.qnsView.text = binding.qnsView.text.dropLast(1)

            try {
                val lastchar = binding.qnsView.text.toString().last()
                if (lastchar.isDigit()){
                    evalExpression()
                }
            }catch (e : Exception){
                binding.ansView.text = "Null"
            }
        }
    }

    private fun onclearclick() {
        if (binding.qnsView.text.toString().isNotEmpty()){
            binding.qnsView.text = ""
            lastnumeric = false
        }
    }

    private fun onallclearclick() {
            binding.qnsView.text =  ""
            binding.ansView.text = ""
            stateError = false
            lastnumeric =false
            lastDot = false
    }

    private fun onoperatorclick(view: View?) {

        if (!stateError && lastnumeric){
            binding.qnsView.append((view as Button).text)
            lastDot = false
            lastnumeric = false
            evalExpression()
        }
    }

    private fun ondigitclick(view: View?) {

        if(stateError){
            binding.qnsView.text = (view as Button).text
            stateError = false
        }else{
            binding.qnsView.append((view as Button).text)
        }
        lastnumeric = true
        evalExpression()

    }

}