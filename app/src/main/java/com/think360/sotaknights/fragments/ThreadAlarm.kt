package com.think360.sotaknights.fragments

import android.app.Dialog

import android.databinding.DataBindingUtil
import android.os.Bundle

import android.support.v4.app.Fragment
import android.view.*

import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.internal.zzahf.runOnUiThread
import com.rafakob.drawme.DrawMeButton
import com.think360.sotaknights.R

import com.think360.sotaknights.activities.SotaKnightActivity
import com.think360.sotaknights.api.AppController
import com.think360.sotaknights.api.data.Responce
import com.think360.sotaknights.api.interfaces.ApiService
import com.think360.sotaknights.databinding.ThreadAlarmFragmentBinding
import com.think360.sotaknights.util.ConnectivityReceiver
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by think360 on 29/09/17.
 */
class ThreadAlarm : Fragment() {
   @Inject
    internal lateinit var apiService: ApiService
    private lateinit  var  threadAlarmFragmentBinding : ThreadAlarmFragmentBinding
    companion object {
        fun newInstance(): ThreadAlarm {
            return ThreadAlarm()
        }
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        threadAlarmFragmentBinding  = DataBindingUtil.inflate<ThreadAlarmFragmentBinding>( inflater,R.layout.thread_alarm_fragment, container, false)
        (activity.application as AppController).getComponent().inject(this@ThreadAlarm)

        SotaKnightActivity.sotaKnightActivity!!.sotaKnightActivityBinding.  navigation .visibility = View.GONE
        SotaKnightActivity.sotaKnightActivity!!.sotaKnightActivityBinding.flBack.visibility = View.VISIBLE

        SotaKnightActivity.sotaKnightActivity!!.sotaKnightActivityBinding.flBack.setOnClickListener{

            SotaKnightActivity.sotaKnightActivity!!.sotaKnightActivityBinding.navigation.visibility = View.VISIBLE
            SotaKnightActivity.sotaKnightActivity!!.sotaKnightActivityBinding.flBack.visibility = View.GONE
            SotaKnightActivity.sotaKnightActivity!!.onBackPressed()
        }
        threadAlarmFragmentBinding. btnThreadAlarm.setOnClickListener{
            threatAlertDialog()
        }
        return threadAlarmFragmentBinding.root
    }

    private fun threatAlertDialog() {
        val dialog =  Dialog(SotaKnightActivity.sotaKnightActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_thread_alarm)
        val tvMsg = dialog.findViewById<TextView>(R.id.tvMsg)
        tvMsg.setText("Are you sure to Push Threat Alert?")
        val btnYes = dialog.findViewById<DrawMeButton>(R.id.btnYes)
        dialog.show()

        val btnNo = dialog.findViewById<DrawMeButton>(R.id.btnNo)
        btnNo.setOnClickListener { dialog.dismiss() }
        btnYes.setOnClickListener {
            dialog.dismiss()
            if(ConnectivityReceiver.isConnected()){
                threadAlarmFragmentBinding.  progressBar.isIndeterminate = true
                threadAlarmFragmentBinding. progressBar.visibility = View.VISIBLE
                threadAlarmFragmentBinding. progressBar.setClickable(false)


                apiService.pushThreadAlarm(AppController.getSharedPref().getString("user_id","null"),AppController.getSharedPref().getString("lat","null"),AppController.getSharedPref().getString("lang","null")).enqueue(object : Callback<Responce.StatusResponce> {
                    override fun onResponse(call: Call<Responce.StatusResponce>, response: Response<Responce.StatusResponce>) {

                        if (response.isSuccessful() && response.body().getStatus()) {

                            threadAlarmFragmentBinding.progressBar.setVisibility(View.GONE)
                            val dialog =  Dialog(activity)

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
                                        runOnUiThread {
                                            //stuff that updates ui
                                            SotaKnightActivity.sotaKnightActivity!!.sotaKnightActivityBinding.navigation.visibility = View.VISIBLE
                                            SotaKnightActivity.sotaKnightActivity!!.sotaKnightActivityBinding.flBack.visibility = View.GONE
                                            SotaKnightActivity.sotaKnightActivity!!.onBackPressed()
                                        }
                                    } catch (e : InterruptedException ) {
                                        e.printStackTrace();
                                    }


                                }
                            }
                            t.start()

                        } else {
                            threadAlarmFragmentBinding.progressBar.setVisibility(View.GONE)
                        }
                    }

                    override fun onFailure(call: Call<Responce.StatusResponce>, t: Throwable) {
                        threadAlarmFragmentBinding.progressBar.setVisibility(View.GONE)
                        t.printStackTrace()
                    }
                })
            }else{
                Toast.makeText(AppController.getInstance().applicationContext, "No internet connection!", Toast.LENGTH_SHORT).show()
            }

        }
    }

}