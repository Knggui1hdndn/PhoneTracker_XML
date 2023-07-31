package com.gps.phonetracker.numberlocator.family.tracklocation.presenter

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.core.content.edit
import com.google.gson.Gson
import com.gps.phonetracker.numberlocator.family.tracklocation.firebase.AccountFireBase
import com.gps.phonetracker.numberlocator.family.tracklocation.firebase.LocationFireBase
import com.gps.phonetracker.numberlocator.family.tracklocation.`interface`.InterfaceLogin
import com.gps.phonetracker.numberlocator.family.tracklocation.model.Users
import com.gps.phonetracker.numberlocator.family.tracklocation.utili.Constraints
import com.gps.phonetracker.numberlocator.family.tracklocation.utili.ShareData


class LoginPresenter(
    private val context: Context,
    private val view: InterfaceLogin.View,
    private val fireBase: AccountFireBase
) : InterfaceLogin.Presenter {
    override fun login(phoneNumber: String, pass: String) {
        val checkPass = pass.isNotEmpty()
        val checkPhoneNumber = phoneNumber.isNotEmpty()
        val checkPhoneMatches = Patterns.PHONE.matcher(phoneNumber).matches()
        if (checkPhoneNumber && checkPass && checkPhoneMatches) {
            fireBase.login(phoneNumber, pass) { u, e ->
                if (u != null) {
                    val gson = Gson();
                    val shared = context.getSharedPreferences(Constraints.CURRENT_USER, Context.MODE_PRIVATE)
                    shared.edit {
                        val json = gson.toJson(u);
                        putString(Constraints.CURRENT_USER, json);
                        apply()
                    }
                    ShareData.currentUser = u
                    ShareData.uid = u.id
                    view.success("ok")
                }else{
                    view.error("error")
                }
            }
        } else {
            if (!checkPass) view.error("Không bỏ trống mật khẩu")
            if (!checkPhoneNumber) view.error("Không bỏ trống số điện thoại")
            else if (!checkPhoneMatches) view.error("Số điện thoại không đúng định dạng")
        }
    }

    override fun register(users: Users) {
        val checkTitle = users.title.isNotEmpty()
        val checkPass = users.title.isNotEmpty()
        val checkPhoneNumber = users.title.isNotEmpty()
        val checkPhoneMatches = Patterns.PHONE.matcher(users.phoneNumber).matches()
        if (checkTitle && checkPass && checkPhoneMatches) {
            fireBase.register(users) { b, e ->
                if (b) view.success("Đăng kí thành công")
                else e?.let { view.error(it) }
            }
        } else {
            if (!checkTitle) view.error("Không bỏ trống title")
            if (!checkPass) view.error("Không bỏ trống mật khẩu")
            if (!checkPhoneNumber) view.error("Không bỏ trống số điện thoại")
            else if (!checkPhoneMatches) view.error("Số điện thoại không đúng định dạng")
        }
    }
}