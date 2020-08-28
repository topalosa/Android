package com.n8422.sumcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder


class MainActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /*Number Buttons*/

        button1.setOnClickListener {
            evaluateExpression("1", clear = true)
        }

        button2.setOnClickListener {
            evaluateExpression("2", clear = true)
        }

        button3.setOnClickListener {
            evaluateExpression("3", clear = true)
        }
        button4.setOnClickListener {
            evaluateExpression("4", clear = true)
        }

        button5.setOnClickListener {
            evaluateExpression("5", clear = true)
        }

        button6.setOnClickListener {
            evaluateExpression("6", clear = true)
        }

        button7.setOnClickListener {
            evaluateExpression("7", clear = true)
        }

        button8.setOnClickListener {
            evaluateExpression("8", clear = true)
        }

        button9.setOnClickListener {
            evaluateExpression("9", clear = true)
        }

        button0.setOnClickListener {
            evaluateExpression("0", clear = true)
        }

        /*Operators*/

        buttonPLUS.setOnClickListener {
            evaluateExpression("+", clear = true)
        }

        buttonMIN.setOnClickListener {
            evaluateExpression("-", clear = true)
        }

        buttonMUL.setOnClickListener {
            evaluateExpression("*", clear = true)
        }



        buttonC.setOnClickListener {
            val h = ""
            editText.text = Editable.Factory.getInstance().newEditable(h)

            editText.text = Editable.Factory.getInstance().newEditable(h)
        }

        buttonSUM.setOnClickListener {
            val text = editText.text.toString()
            val expression = ExpressionBuilder(text).build()

            val result = expression.evaluate()
            val longResult = result.toLong()
            if (result == longResult.toDouble()) {
                editText.text = Editable.Factory.getInstance().newEditable(longResult.toString())
            } else {
                editText.text = Editable.Factory.getInstance().newEditable(result.toString())
            }
        }

        buttonC.setOnClickListener {
            val h = ""
            val text = editText.text.toString()
            if(text.isNotEmpty()) {
                editText.text = Editable.Factory.getInstance().newEditable(text.drop(1))
            }

            editText.text = Editable.Factory.getInstance().newEditable(h)
        }
    }

    /*Function to calculate the expressions using expression builder library*/

    fun evaluateExpression(string: String, clear: Boolean) {

        if(clear) {

            editText.append(string)
        } else {
            editText.append(editText.text)
            editText.append(string)

        }
    }
}