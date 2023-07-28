package com.gps.phonetracker.numberlocator.family.tracklocation.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gps.phonetracker.numberlocator.family.tracklocation.databinding.ActivityRegisterBinding
import com.gps.phonetracker.numberlocator.family.tracklocation.firebase.LocationFireBase
import com.gps.phonetracker.numberlocator.family.tracklocation.`interface`.InterfaceLogin
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users
import com.gps.phonetracker.numberlocator.family.tracklocation.presenter.LoginPresenter
import com.gps.phonetracker.numberlocator.family.tracklocation.utili.ShareData
import org.mindrot.jbcrypt.BCrypt

class RegisterActivity : AppCompatActivity(), InterfaceLogin.View {
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!
    private var uri: Uri? = null
    private val locationFireBase = LocationFireBase(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val loginPresenter = LoginPresenter(this, this, locationFireBase)
        with(binding) {
            btnRegister.setOnClickListener {
                loginPresenter.register(
                    Users(
                        edtTitle.text.toString(),
                        edtPhoneNumber.text.toString(),
                        BCrypt.hashpw(edtPass.text.toString(), BCrypt.gensalt()),
                        ShareData.URL_Avatar_Default
                    )
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun success(s: String) {
        ShareData.Toast(this, s)
        startActivity(Intent(this, Login::class.java))
    }

    override fun error(s: String) {
        ShareData.Toast(this, s)

    }
}