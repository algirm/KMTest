package com.algirm.test2022.ui.secondscreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.algirm.test2022.R
import com.algirm.test2022.databinding.ActivitySecondBinding
import com.algirm.test2022.ui.thirdscreen.ThirdActivity

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.custom_toolbar)
        val arrow = findViewById<ImageView>(R.id.arrow_toolbar)
        val titleToolbar = findViewById<TextView>(R.id.title_toolbar)
        titleToolbar.text = getString(R.string.second_screen)
        arrow.setOnClickListener {
            finish()
        }
        binding.textViewName.text = intent.extras?.getString("name")
        binding.textViewUserSelected.text
        binding.buttonChooseUser.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            result.data?.let {
                binding.textViewUserSelected.text = it.getStringExtra("user_fullname")
            }
        }
    }
}