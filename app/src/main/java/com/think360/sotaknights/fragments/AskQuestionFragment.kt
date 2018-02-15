package com.think360.sotaknights.fragments

import android.app.Dialog

import android.databinding.DataBindingUtil
import android.os.Bundle

import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.rafakob.drawme.DrawMeButton
import com.rafakob.drawme.DrawMeEditText
import com.think360.sotaknights.R
import com.think360.sotaknights.activities.LoginActivity
import com.think360.sotaknights.activities.SotaKnightActivity
import com.think360.sotaknights.api.AppController
import com.think360.sotaknights.api.data.Responce
import com.think360.sotaknights.api.interfaces.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import com.google.android.gms.internal.zzahf.runOnUiThread
import com.think360.sotaknights.databinding.AskQuestionFragmentBinding
import com.think360.sotaknights.util.ConnectivityReceiver


/**
 * Created by think360 on 29/09/17.
 */
class AskQuestionFragment  : Fragment() {
    @Inject
    internal lateinit var apiService: ApiService
    private lateinit var askQuestionFragmentBinding: AskQuestionFragmentBinding
    companion object {
        fun newInstance(): AskQuestionFragment {
            return AskQuestionFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        askQuestionFragmentBinding  = DataBindingUtil.inflate<AskQuestionFragmentBinding>(inflater, R.layout.ask_question_fragment, container, false)
        (activity.application as AppController).getComponent().inject(this@AskQuestionFragment)

        SotaKnightActivity.sotaKnightActivity!!.sotaKnightActivityBinding.  navigation .visibility = View.GONE
        SotaKnightActivity.sotaKnightActivity!!.sotaKnightActivityBinding.flBack.visibility = View.VISIBLE

        SotaKnightActivity.sotaKnightActivity!!.sotaKnightActivityBinding.flBack.setOnClickListener{

            SotaKnightActivity.sotaKnightActivity!!.sotaKnightActivityBinding.navigation.visibility = View.VISIBLE
            SotaKnightActivity.sotaKnightActivity!!.sotaKnightActivityBinding.flBack.visibility = View.GONE
            SotaKnightActivity.sotaKnightActivity!!.onBackPressed()
        }

        askQuestionFragmentBinding.  btnSend.setOnClickListener {
           if(!TextUtils.isEmpty(askQuestionFragmentBinding.etAskAQuestion.text.toString())){
               if(ConnectivityReceiver.isConnected()){
                   askQuestion()
               }else{
                   Toast.makeText(AppController.getInstance().applicationContext, "No internet connection!", Toast.LENGTH_SHORT).show()
               }
        }
        else{
           Toast.makeText(activity,"Field can't be Empty!",Toast.LENGTH_SHORT) .show()
        }}
        return askQuestionFragmentBinding.root
    }
    private fun askQuestion(){
        SotaKnightActivity.sotaKnightActivity!!.hideSoftKeyboard()
        askQuestionFragmentBinding.  progressBar.isIndeterminate = true
        askQuestionFragmentBinding. progressBar.visibility = View.VISIBLE
        askQuestionFragmentBinding. progressBar.setClickable(false)
        apiService.askQuestion(AppController.getSharedPref().getString("user_id","null"),askQuestionFragmentBinding.etAskAQuestion.text.toString()).enqueue(object : Callback<Responce.StatusResponce> {
            override fun onResponse(call: Call<Responce.StatusResponce>, response: Response<Responce.StatusResponce>) {
                if (response.body().getStatus()) {
                    askQuestionFragmentBinding.progressBar.setVisibility(View.GONE)
                    val dialog =  Dialog(SotaKnightActivity.sotaKnightActivity)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.dialog_alert)
                    val tvMsg = dialog.findViewById<TextView>(R.id.tvMsg)
                    tvMsg.setText(response.body().message)
                    dialog.show()
                  val t = object : Thread() {
                        override fun run() {
                            try {
                                sleep(2200)
                                dialog.dismiss()
                                runOnUiThread {
                                    //stuff that updates ui
                                    SotaKnightActivity.sotaKnightActivity!!.sotaKnightActivityBinding.navigation.visibility = View.VISIBLE
                                    SotaKnightActivity.sotaKnightActivity!!.sotaKnightActivityBinding.flBack.visibility = View.GONE
                                    SotaKnightActivity.sotaKnightActivity!!.onBackPressed()
                                }
                            } catch (e : InterruptedException ) { e.printStackTrace(); }
                        }
                    }
                    t.start()
                } else {
                    askQuestionFragmentBinding.progressBar.setVisibility(View.GONE)
                }
            }

            override fun onFailure(call: Call<Responce.StatusResponce>, t: Throwable) {
                askQuestionFragmentBinding.progressBar.setVisibility(View.GONE)
                t.printStackTrace()

            }
        })
    }
}