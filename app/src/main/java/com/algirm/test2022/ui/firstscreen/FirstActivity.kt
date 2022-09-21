package com.algirm.test2022.ui.firstscreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.algirm.test2022.databinding.ActivityFirstBinding
import com.algirm.test2022.ui.secondscreen.SecondActivity

class FirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstBinding

    private lateinit var myDialog: MyDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun checkPalindrome() {
        val isPalindrome: Boolean

        val words = binding.editTextPalindrome.text.toString()

        if (words.isBlank()) {
            binding.editTextPalindrome.error = "Please fill the blank."
            return
        }

        val mutableListChar = words.lowercase().toMutableList()
        for (i in mutableListChar.indices.reversed()) {
            if (mutableListChar[i].isWhitespace()) {
                mutableListChar.removeAt(i)
            }
        }
        isPalindrome = mutableListChar.reversed() == mutableListChar
        val args = Bundle().apply {
            putBoolean("isPalindrome", isPalindrome)
        }
        myDialog.arguments = args
        myDialog.show(supportFragmentManager, "myDialog")
    }

    private fun nextPage() {
        val name = binding.editTextName.text.toString()
        val intent = Intent(this, SecondActivity::class.java).apply {
            putExtra("name", name)
        }
        startActivity(intent)
    }

    private fun initView() {
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        binding.buttonCheck.setOnClickListener { checkPalindrome() }
        binding.buttonNext.setOnClickListener { nextPage() }

        myDialog = MyDialogFragment.newInstance()
    }

}