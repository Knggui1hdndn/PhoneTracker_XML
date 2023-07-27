package com.gps.phonetracker.numberlocator.family.tracklocation

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.gps.phonetracker.numberlocator.family.tracklocation.databinding.ActivityLoginBinding
import com.gps.phonetracker.numberlocator.family.tracklocation.firebase.LocationFireBase
import com.gps.phonetracker.numberlocator.family.tracklocation.`interface`.InterfaceFireBase
import com.gps.phonetracker.numberlocator.family.tracklocation.`interface`.InterfaceLogin
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users
import com.gps.phonetracker.numberlocator.family.tracklocation.presenter.LoginPresenter
import com.gps.phonetracker.numberlocator.family.tracklocation.utili.ShareData
import org.mindrot.jbcrypt.BCrypt

class Login : AppCompatActivity(), InterfaceLogin.View {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private var uri: Uri? = null
    private val locationFireBase = LocationFireBase(null)

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            this.uri = uri
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val loginPresenter = LoginPresenter(this, this, locationFireBase)
        with(binding) {
            btnUpdateImg.setOnClickListener {
                uri?.let { it1 ->
                    locationFireBase.putImage(it1) { if (it != null) locationFireBase.updateImage(it) {} }
                }
            }

            btnPickImg.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            btnLogin.setOnClickListener {
                loginPresenter.login(
                    edtPhoneNumber.text.toString(),
                    edtPass.text.toString()
                )
            }

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
    }

    override fun error(s: String) {
        ShareData.Toast(this, s)

    }


}