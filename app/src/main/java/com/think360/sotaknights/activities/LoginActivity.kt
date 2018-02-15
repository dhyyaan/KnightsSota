package com.think360.sotaknights.activities

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.crash.FirebaseCrash

import com.think360.sotaknights.R
import com.think360.sotaknights.api.AppController
import com.think360.sotaknights.api.data.Responce
import com.think360.sotaknights.api.interfaces.ApiService
import com.think360.sotaknights.databinding.LoginActivityBinding
import com.think360.sotaknights.fragments.RegisterFragment
import com.think360.sotaknights.util.ConnectivityReceiver
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class LoginActivity : RuntimePermissionsActivity() {

    @Inject
    internal lateinit var apiService: ApiService
    private lateinit var loginActivityBinding: LoginActivityBinding


    companion object {
        var loginActivity : LoginActivity? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivityBinding = DataBindingUtil.setContentView<LoginActivityBinding>(this,R.layout.login_activity)
        (application as AppController).getComponent().inject(this@LoginActivity)
        loginActivity = this
        if (Build.VERSION.SDK_INT >= 23)
            permissions()
        loginActivityBinding. btnLogin.setOnClickListener {
            if(ConnectivityReceiver.isConnected()){
                login()
            }else{
                Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show()
            }
             }
        loginActivityBinding.btnRegister.setOnClickListener {
            if(ConnectivityReceiver.isConnected()){
                replaceFragment(RegisterFragment.newInstance())
            }else{
                Toast.makeText(this, "No internet connection!", Toast.LENGTH_SHORT).show()
            }
            }

    }
    fun replaceFragment(fragment : Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.addToBackStack(fragment.javaClass.simpleName)
        transaction.replace(R.id.fragContainer, fragment).commitAllowingStateLoss()
    }
    override fun onBackPressed() {

        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    private fun permissions() {
        val REQUEST_PERMISSIONS = 20
        super@LoginActivity.requestAppPermissions(arrayOf(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), R.string.app_name, REQUEST_PERMISSIONS)
    }
    private fun login(){
        loginActivityBinding.etPwd.clearFocus()
        loginActivityBinding.  progressBar.isIndeterminate = true
        loginActivityBinding. progressBar.visibility = View.VISIBLE
        loginActivityBinding. progressBar.setClickable(false)
        if ( !TextUtils.isEmpty(loginActivityBinding.etUserName.text) && !TextUtils.isEmpty(loginActivityBinding.etPwd.text)) {
            apiService.sotaLogin("android",AppController.getSharedPref().getString("firebase_reg_token", "null"), loginActivityBinding.etUserName.text.toString(), loginActivityBinding.etPwd.text.toString()).enqueue(object : Callback<Responce.LoginResponce> {
                        override fun onResponse(call: Call<Responce.LoginResponce>, response: Response<Responce.LoginResponce>) {

                            if (response.body().getStatus()) {
                                Log.d("responce",response.body().toString())
                                AppController. getSharedPref().edit().putBoolean("isLogin",true).apply()
                                AppController. getSharedPref().edit().putString("user_id",response.body().userId).apply()
                                val intent =  Intent(applicationContext, SotaKnightActivity::class.java)
                                overridePendingTransition(R.anim.zoom_exit, 0)
                                    startActivity(intent)
                                loginActivityBinding.progressBar.setVisibility(View.GONE)
                                finish()
                            } else {
                                loginActivityBinding.progressBar.setVisibility(View.GONE)
                                val dialog =  Dialog(loginActivity)
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                                dialog.setCancelable(false)
                                dialog.setContentView(R.layout.dialog_alert)
                                val tvMsg = dialog.findViewById<TextView>(R.id.tvMsg)
                                tvMsg.setText(response.body().message)
                                dialog.show()
                                val t = object : Thread() {
                                    override fun run() {
                                        try {
                                            sleep(2000)
                                            dialog.dismiss()
                                        } catch (e : InterruptedException ) { e.printStackTrace(); }
                                    }
                                }
                                t.start()
                            }
                        }

                        override fun onFailure(call: Call<Responce.LoginResponce>, t: Throwable) {
                            loginActivityBinding.progressBar.setVisibility(View.GONE)
                        }
                    })
        } else {
            loginActivityBinding.progressBar.setVisibility(View.GONE)
            Toast.makeText(this, "Please enter username & password", Toast.LENGTH_SHORT).show()

        }
    }



}
