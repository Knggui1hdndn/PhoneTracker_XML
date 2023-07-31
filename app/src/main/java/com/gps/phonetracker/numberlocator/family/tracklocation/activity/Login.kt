package com.gps.phonetracker.numberlocator.family.tracklocation.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.gps.phonetracker.numberlocator.family.tracklocation.databinding.ActivityLoginBinding
import com.gps.phonetracker.numberlocator.family.tracklocation.firebase.AccountFireBase
import com.gps.phonetracker.numberlocator.family.tracklocation.firebase.LocationFireBase
import com.gps.phonetracker.numberlocator.family.tracklocation.`interface`.InterfaceLogin
import com.gps.phonetracker.numberlocator.family.tracklocation.presenter.LoginPresenter
import com.gps.phonetracker.numberlocator.family.tracklocation.utili.ShareData

class Login : AppCompatActivity(), InterfaceLogin.View {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private var uri: Uri? = null
    private val accountFireBase = AccountFireBase()

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            this.uri = uri
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (ShareData.currentUser != null) {
            startActivity(Intent(this@Login, MainActivity::class.java))
            finish()
        }
        val loginPresenter = LoginPresenter(this, this, accountFireBase)
        with(binding) {
            btnLogin.setOnClickListener {
                loginPresenter.login(
                    edtPhoneNumber.text.toString(),
                    edtPass.text.toString()
                )
            }

            btnRegister.setOnClickListener {
                startActivity(Intent(this@Login, RegisterActivity::class.java))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun success(s: String) {
        ShareData.Toast(this, s)
        startActivity(Intent(this@Login, MainActivity::class.java))
    }

    override fun error(s: String) {
        ShareData.Toast(this, s)
    }
}