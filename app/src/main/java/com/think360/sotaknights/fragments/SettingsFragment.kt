package com.think360.sotaknights.fragments

import android.app.Dialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.rafakob.drawme.DrawMeButton
import com.think360.sotaknights.R
import com.think360.sotaknights.activities.LoginActivity
import com.think360.sotaknights.activities.SotaKnightActivity
import com.think360.sotaknights.api.AppController
import com.think360.sotaknights.databinding.SettingsFragmentBinding
/**
 * Created by think360 on 28/09/17.
 */
class SettingsFragment : Fragment() {
    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val  settingsFragmentBinding  = DataBindingUtil.inflate<SettingsFragmentBinding>(inflater, R.layout.settings_fragment, container, false)

        settingsFragmentBinding. rlAAQ.setOnClickListener{SotaKnightActivity.sotaKnightActivity!!.replaceFragment(AskQuestionFragment.newInstance())}
        settingsFragmentBinding.  rlThreadAlarm.setOnClickListener{

            if(AppController.getSharedPref().getInt("status",7)==1){
                SotaKnightActivity.sotaKnightActivity!!.replaceFragment(ThreadAlarm.newInstance())
            }else{
                Toast.makeText(activity,"Please click Available in Map", Toast.LENGTH_SHORT).show()
            }
            }
        settingsFragmentBinding.  rlLogOut.setOnClickListener{logOutDialog()}

        return settingsFragmentBinding.root
    }

    private fun logOutDialog() {
        val dialog =  Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_thread_alarm)
        val tvMsg = dialog.findViewById<TextView>(R.id.tvMsg)
        tvMsg.setText("       Do you want to Logout?       ")
        val btnYes = dialog.findViewById<DrawMeButton>(R.id.btnYes)


        val btnNo = dialog.findViewById<DrawMeButton>(R.id.btnNo)
        btnNo.setOnClickListener { dialog.dismiss() }
        btnYes.setOnClickListener {
            AppController.getSharedPref().edit().putBoolean("isLogin",false).apply()

            Handler().postDelayed({
                startActivity(Intent(activity, LoginActivity::class.java))
                activity.overridePendingTransition(R.anim.zoom_exit, 0)
                activity.finish()
            }, 200)
        }
        dialog.show()
    }
}