package com.think360.sotaknights.fragments

import android.app.Dialog
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.*
import android.widget.*
import com.rafakob.drawme.DrawMeButton
import com.think360.sotaknights.R
import com.think360.sotaknights.activities.LoginActivity
import com.think360.sotaknights.activities.SotaKnightActivity
import com.think360.sotaknights.api.AppController
import com.think360.sotaknights.api.data.Responce
import com.think360.sotaknights.api.interfaces.ApiService
import com.think360.sotaknights.databinding.RegisterFragmentBinding
import com.think360.sotaknights.util.ConnectivityReceiver
import me.shaohui.advancedluban.Luban
import me.shaohui.advancedluban.OnMultiCompressListener
import net.yazeed44.imagepicker.model.ImageEntry
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import net.yazeed44.imagepicker.util.Picker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.ArrayList
import java.util.regex.Pattern


/**
 * Created by think360 on 27/09/17.
 */
class RegisterFragment : Fragment() {

    @Inject
    internal lateinit var apiService: ApiService
    private lateinit var registerFragmentBinding: RegisterFragmentBinding

    private var imageAadharMP1: MultipartBody.Part? = null
    private var imageAadharMP2: MultipartBody.Part? = null
    private var imagePsaraMP1: MultipartBody.Part? = null
    private var imagePsaraMP2: MultipartBody.Part? = null
    private var imageDlMP1: MultipartBody.Part? = null
    private var imageDlMP2: MultipartBody.Part? = null
    private var imageGlMP1: MultipartBody.Part? = null
    private var imageGlMP2: MultipartBody.Part? = null

    private var   listStateId : ArrayList<String>? = null
    private var  listStateName : ArrayList<String>? = null

    private var  listCityId : ArrayList<String>? = null
    private var  listCityName : ArrayList<String>? = null

    private var  listPreCityId : ArrayList<String>? = null
    private var  listPreCityName : ArrayList<String>? = null

    companion object {
        fun newInstance(): RegisterFragment {
            return RegisterFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        registerFragmentBinding = DataBindingUtil.inflate<RegisterFragmentBinding>(inflater, R.layout.register_fragment, container, false)
        (activity.application as AppController).getComponent().inject(this@RegisterFragment)


        registerFragmentBinding.ivBack.setOnClickListener { LoginActivity.loginActivity!!.onBackPressed() }

        registerFragmentBinding.btnUploadAdharCard.setOnClickListener {
            registerFragmentBinding.etBranchNameAdd.clearFocus()
            LoginActivity.loginActivity!!.hideSoftKeyboard()
            registerFragmentBinding.  progressBar1.isIndeterminate = true
            registerFragmentBinding. progressBar1.visibility = View.VISIBLE
            registerFragmentBinding. progressBar1.setClickable(false)
            Picker.Builder(LoginActivity.loginActivity!!, object : Picker.PickListener {

                override fun onCancel() {
                    registerFragmentBinding.progressBar1.setVisibility(View.GONE)

                }

                override fun onPickedSuccessfully(images: ArrayList<ImageEntry>?) {
                    if (images!!.size > 0) {
                        val imageListAadhar = mutableListOf<File>()
                        for (i in images) {
                            imageListAadhar.add(File(i.path))

                        }
                        Luban.compress(LoginActivity.loginActivity!!, imageListAadhar)
                                .putGear(Luban.THIRD_GEAR)
                                .launch(object : OnMultiCompressListener {

                                    override fun onStart() {
                                        // Log.i(TAG, "start");
                                    }

                                    override fun onSuccess(fileList: List<File>) {
                                        imageAadharMP1 =  prepareFilePart("sota_aadhar_image1",fileList.get(0))
                                        if(images.size == 2) {
                                            imageAadharMP2 = prepareFilePart("sota_aadhar_image2", fileList.get(1))
                                        }
                                        registerFragmentBinding.ivAadharImage.setImageURI(Uri.fromFile(fileList.get(0)))
                                        registerFragmentBinding.progressBar1.setVisibility(View.GONE)

                                    }

                                    override fun onError(e: Throwable) {
                                        e.printStackTrace();
                                    }
                                })

                    } else {
                        registerFragmentBinding.progressBar1.setVisibility(View.GONE)
                        //Toast.makeText(activity, "Please select Front & Backend image of Aadhar card!", Toast.LENGTH_SHORT).show()
                    }
                }


            }
                    , R.style.MIP_theme)
                    .setPickMode(Picker.PickMode.MULTIPLE_IMAGES)
                    .setLimit(2)
                    .build()
                    .startActivity()

        }
        registerFragmentBinding.btnUploadPsasa.setOnClickListener {
            registerFragmentBinding.  progressBar2.isIndeterminate = true
            registerFragmentBinding. progressBar2.visibility = View.VISIBLE
            registerFragmentBinding. progressBar2.setClickable(false)
            Picker.Builder(LoginActivity.loginActivity!!, object : Picker.PickListener {
                override fun onCancel() {
                    registerFragmentBinding.progressBar2.setVisibility(View.GONE)
                }

                override fun onPickedSuccessfully(images: ArrayList<ImageEntry>?) {
                    if (images!!.size > 0) {
                        val imageListPsara = mutableListOf<File>()
                        for (i in images) {
                            imageListPsara.add(File(i.path))

                        }
                        Luban.compress(LoginActivity.loginActivity!!, imageListPsara)
                                .putGear(Luban.THIRD_GEAR)
                                .launch(object : OnMultiCompressListener {

                                    override fun onStart() {
                                        // Log.i(TAG, "start");
                                    }

                                    override fun onSuccess(fileList: List<File>) {
                                        imagePsaraMP1 =  prepareFilePart("sota_psara_image1",fileList.get(0))
                                        if(images.size == 2) {
                                            imagePsaraMP2 = prepareFilePart("sota_psara_image2", fileList.get(1))
                                        }
                                        registerFragmentBinding.ivPsara.setImageURI(Uri.fromFile(fileList.get(0)))
                                        registerFragmentBinding.progressBar2.setVisibility(View.GONE)

                                    }

                                    override fun onError(e: Throwable) {
                                        e.printStackTrace();
                                    }
                                })

                    } else {
                        registerFragmentBinding.progressBar2.setVisibility(View.GONE)
                      //  Toast.makeText(activity, "Please select Front & Backend image for Psara!", Toast.LENGTH_SHORT).show()
                    }
                }


            }, R.style.MIP_theme)
                    .setPickMode(Picker.PickMode.MULTIPLE_IMAGES)
                    .setLimit(2)
                    .build()
                    .startActivity()
        }
        registerFragmentBinding.btnUploadDl.setOnClickListener {
            registerFragmentBinding.  progressBar3.isIndeterminate = true
            registerFragmentBinding. progressBar3.visibility = View.VISIBLE
            registerFragmentBinding. progressBar3.setClickable(false)
            Picker.Builder(LoginActivity.loginActivity!!, object : Picker.PickListener {
                override fun onCancel() {
                    registerFragmentBinding.progressBar3.setVisibility(View.GONE)
                }

                override fun onPickedSuccessfully(images: ArrayList<ImageEntry>?) {
                    if (images!!.size > 0) {
                        val imageListDl = mutableListOf<File>()
                        for (i in images) {
                            imageListDl.add(File(i.path))

                        }
                        Luban.compress(LoginActivity.loginActivity!!, imageListDl)
                                .putGear(Luban.THIRD_GEAR)
                                .launch(object : OnMultiCompressListener {

                                    override fun onStart() {
                                        // Log.i(TAG, "start");
                                    }

                                    override fun onSuccess(fileList: List<File>) {
                                        imageDlMP1 =  prepareFilePart("sota_dl_image1",fileList.get(0))
                                        if(images.size == 2) {
                                            imageDlMP2 = prepareFilePart("sota_dl_image2", fileList.get(1))
                                        }
                                        registerFragmentBinding.ivDl.setImageURI(Uri.fromFile(fileList.get(0)))
                                        registerFragmentBinding.progressBar3.setVisibility(View.GONE)

                                    }
                                    override fun onError(e: Throwable) {
                                        e.printStackTrace();
                                    }
                                })

                    } else {
                        registerFragmentBinding.progressBar3.setVisibility(View.GONE)

                    }
                }


            }
                    , R.style.MIP_theme)
                    .setPickMode(Picker.PickMode.MULTIPLE_IMAGES)
                    .setLimit(2)
                    .build()
                    .startActivity()
        }
        registerFragmentBinding.btnUploadGl.setOnClickListener {
            registerFragmentBinding.  progressBar4.isIndeterminate = true
            registerFragmentBinding. progressBar4.visibility = View.VISIBLE
            registerFragmentBinding. progressBar4.setClickable(false)
            Picker.Builder(LoginActivity.loginActivity!!, object : Picker.PickListener {

                override fun onCancel() {
                    registerFragmentBinding.progressBar4.setVisibility(View.GONE)

                }

                override fun onPickedSuccessfully(images: ArrayList<ImageEntry>?) {
                    if (images!!.size > 0) {
                        val imageListGl = mutableListOf<File>()
                        for (i in images) {
                            imageListGl.add(File(i.path))

                        }
                        Luban.compress(LoginActivity.loginActivity!!, imageListGl)
                                .putGear(Luban.THIRD_GEAR)
                                .launch(object : OnMultiCompressListener {

                                    override fun onStart() {
                                        // Log.i(TAG, "start");
                                    }

                                    override fun onSuccess(fileList: List<File>) {
                                        imageGlMP1 =  prepareFilePart("sota_gl_image1",fileList.get(0))
                                        if(images.size == 2) {
                                            imageGlMP2 = prepareFilePart("sota_gl_image2", fileList.get(1))
                                        }
                                        registerFragmentBinding.ivGl.setImageURI(Uri.fromFile(fileList.get(0)))
                                        registerFragmentBinding.progressBar4.setVisibility(View.GONE)

                                    }

                                    override fun onError(e: Throwable) {
                                        e.printStackTrace();
                                    }
                                })

                    } else {
                        registerFragmentBinding.progressBar4.setVisibility(View.GONE)
                        //Toast.makeText(activity, "Please select Front & backend images Gun Licence!", Toast.LENGTH_SHORT).show()
                    }
                }


            }
                    , R.style.MIP_theme)
                    .setPickMode(Picker.PickMode.MULTIPLE_IMAGES)
                    .setLimit(2)
                    .build()
                    .startActivity()
        }
        registerFragmentBinding.btnRegister.setOnClickListener {
            LoginActivity.loginActivity!!.hideSoftKeyboard()
            registerFragmentBinding.etUserName.clearFocus()
            registerFragmentBinding.etEmail.clearFocus()
            registerFragmentBinding.etMobile.clearFocus()
            registerFragmentBinding.etAdd.clearFocus()
          //  registerFragmentBinding.etCity.clearFocus()
           // registerFragmentBinding.etState.clearFocus()
            registerFragmentBinding.etBankAcc.clearFocus()
            registerFragmentBinding.etIfscCode.clearFocus()
            registerFragmentBinding.etBranchNameAdd.clearFocus()
            if (!TextUtils.isEmpty(registerFragmentBinding.etUserName.text)) {
                registerFragmentBinding.etUserName.setError(null)

                if (!TextUtils.isEmpty(registerFragmentBinding.etEmail.text) && isValidEmail(registerFragmentBinding.etEmail.text.toString())) {
                    registerFragmentBinding.etEmail.setError(null)

                    if (!TextUtils.isEmpty(registerFragmentBinding.etMobile.text) && checkMobileNo(registerFragmentBinding.etMobile.text.toString())) {
                        registerFragmentBinding.etMobile.setError(null)


                                                        if (registerFragmentBinding.chTc.isChecked) {
                                                            if(ConnectivityReceiver.isConnected()){
                                                                registration()
                                                            }else{
                                                                Toast.makeText(AppController.getInstance().applicationContext, "No internet connection!", Toast.LENGTH_SHORT).show()
                                                            }


                                                        } else {
                                                            Toast.makeText(AppController.getInstance().applicationContext, "Please check tems & conditions", Toast.LENGTH_SHORT).show()
                                                        }


                    } else {
                        registerFragmentBinding.etMobile.requestFocus()
                        registerFragmentBinding.etMobile.setError("Mobile Number empty or wrong!")
                    }


                } else {
                    registerFragmentBinding.etEmail.requestFocus()
                    registerFragmentBinding.etEmail.setError("Email empty or wrong!")
                }

            } else {
                registerFragmentBinding.etUserName.requestFocus()
                registerFragmentBinding.etUserName.setError("User Name can't be empty!")
            }

        }
        val s_text_login = SpannableString("I have understood the terms & conditions")

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                showTermsConditions()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        s_text_login.setSpan(clickableSpan, 22, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val fcs = ForegroundColorSpan(Color.rgb(29, 37, 87))
        s_text_login.setSpan(fcs, 22, 40, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        //For UnderLine
        s_text_login.setSpan(UnderlineSpan(), 22, 40, 0)
        registerFragmentBinding.chTc.setText(s_text_login)
        registerFragmentBinding.chTc.setMovementMethod(LinkMovementMethod.getInstance())
        registerFragmentBinding.chTc.setHighlightColor(Color.TRANSPARENT)


        getAllStates()


        return registerFragmentBinding.root
    }



    private fun registration() {
        registerFragmentBinding.sv.smoothScrollTo(0,0)
        registerFragmentBinding.  progressBar.isIndeterminate = true
        registerFragmentBinding. progressBar.visibility = View.VISIBLE
        registerFragmentBinding. progressBar.setClickable(false)
        val device_type = RequestBody.create(MediaType.parse("text/plain"), "android")
        val device_id = RequestBody.create(MediaType.parse("text/plain"), AppController.getSharedPref().getString("firebase_reg_token", "null"))

        val sota_name = RequestBody.create(MediaType.parse("text/plain"), registerFragmentBinding.etUserName.text.toString())
        val sota_email = RequestBody.create(MediaType.parse("text/plain"), registerFragmentBinding.etEmail.text.toString())
        val sota_mobile = RequestBody.create(MediaType.parse("text/plain"), registerFragmentBinding.etMobile.text.toString())

        val sota_address = RequestBody.create(MediaType.parse("text/plain"), registerFragmentBinding.etAdd.text.toString())

        var sota_state = RequestBody.create(MediaType.parse("text/plain"),  "")
        var sota_city = RequestBody.create(MediaType.parse("text/plain"), "")

        if(listStateId!=null && !listStateId!!.get(registerFragmentBinding.spState.selectedItemPosition).equals("-1")){
           sota_state = RequestBody.create(MediaType.parse("text/plain"),  listStateId!!.get(registerFragmentBinding.spState.selectedItemPosition))

           }


        if(listCityId != null && !listCityId!!.get(registerFragmentBinding.spCity.selectedItemPosition).equals("-1")){

            sota_city = RequestBody.create(MediaType.parse("text/plain"), listCityId!!.get(registerFragmentBinding.spCity.selectedItemPosition))
        }

        val sota_ac_no = RequestBody.create(MediaType.parse("text/plain"), registerFragmentBinding.etBankAcc.text.toString())
        val sota_ifsc_code = RequestBody.create(MediaType.parse("text/plain"), registerFragmentBinding.etIfscCode.text.toString())
        val sota_branch_name_add = RequestBody.create(MediaType.parse("text/plain"), registerFragmentBinding.etBranchNameAdd.text.toString())
        val sota_other_city = RequestBody.create(MediaType.parse("text/plain"), registerFragmentBinding.etOtherCity.text.toString())

        val sota_pre_add = RequestBody.create(MediaType.parse("text/plain"), registerFragmentBinding.etPreAdd.text.toString())

        var sota_pre_state = RequestBody.create(MediaType.parse("text/plain"), "")
        var sota_pre_city = RequestBody.create(MediaType.parse("text/plain"), "")

       if(listStateId != null && !listStateId!!.get(registerFragmentBinding.spPreState.selectedItemPosition).equals("-1")){
           sota_pre_state = RequestBody.create(MediaType.parse("text/plain"), listStateId!!.get(registerFragmentBinding.spPreState.selectedItemPosition))

       }
        if(listPreCityId !=null && !listPreCityId!!.get(registerFragmentBinding.spPreCity.selectedItemPosition).equals("-1")){

            sota_pre_city = RequestBody.create(MediaType.parse("text/plain"), listPreCityId!!.get(registerFragmentBinding.spPreCity.selectedItemPosition))
        }
        val sota_pre_other_city = RequestBody.create(MediaType.parse("text/plain"), registerFragmentBinding.etOtherPreCity.text.toString())


        val call = apiService.sotaRegistration(device_type, device_id, sota_name, sota_email, sota_mobile, sota_address,
                                               sota_city, sota_state, sota_ac_no, sota_ifsc_code, sota_branch_name_add,
                                               imageAadharMP1, imageAadharMP2, imagePsaraMP1, imagePsaraMP2, imageDlMP1,
                                               imageDlMP2, imageGlMP1, imageGlMP2,sota_pre_add,sota_pre_city,sota_pre_state,
                                               sota_other_city,sota_pre_other_city )
        call.enqueue(object : Callback<Responce.StatusResponce> {
            override fun onResponse(call: Call<Responce.StatusResponce>, response: Response<Responce.StatusResponce>) {
                if (response.isSuccessful()) {
                    if(response.body().status){
                        registerFragmentBinding.progressBar.setVisibility(View.GONE)

                        val dialog =  Dialog(LoginActivity.loginActivity)
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog.setCancelable(false)
                        dialog.setContentView(R.layout.dialog_thread_alarm)

                        val tvMsg = dialog.findViewById<TextView>(R.id.tvMsg)
                        tvMsg.setText(response.body().message)

                        val btnNo = dialog.findViewById<DrawMeButton>(R.id.btnNo)
                        btnNo.visibility = View.GONE

                        val btnYes = dialog.findViewById<DrawMeButton>(R.id.btnYes)
                        btnYes.setText("OK")
                        btnYes.setOnClickListener {
                            dialog.dismiss()
                            LoginActivity.loginActivity!!.onBackPressed()


                        }
                        dialog.show()


                    }else{
                        registerFragmentBinding.progressBar.setVisibility(View.GONE)
                        val dialog =  Dialog(LoginActivity.loginActivity)
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
                else { registerFragmentBinding.progressBar.setVisibility(View.GONE) }
            }
            override fun onFailure(call: Call<Responce.StatusResponce>, t: Throwable) { registerFragmentBinding.progressBar.setVisibility(View.GONE) }
        })
    }
    private fun prepareFilePart(param : String,file: File): MultipartBody.Part{
        return MultipartBody.Part.createFormData(param, file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun checkMobileNo(mobile: String): Boolean {
        return Pattern.matches("^[7-9][0-9]{9}$", mobile)
    }
/*    fun checkACNO(ac: String): Boolean {
        return Pattern.matches("[0-9]{9,18}", ac)
    }
    fun checkIFSCcode(ifsc: String): Boolean {

        return Pattern.matches("^[A-Za-z]{4}\\d{7}$", ifsc)
    }*/
    private fun showTermsConditions() {
        val dialog = Dialog(AppController.getInstance().applicationContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY


        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_terms_conditions)
        val window = dialog.window
        val wlp = window!!.attributes

        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
               val close = dialog.findViewById<ImageView>(R.id.ivCancel)
        close.setOnClickListener { dialog.dismiss() }

        dialog.show()

    }


    private fun getAllStates(){


        registerFragmentBinding.  progressBar.isIndeterminate = true
        registerFragmentBinding. progressBar.visibility = View.VISIBLE
        registerFragmentBinding. progressBar.setClickable(false)

        apiService.all_States().enqueue(object : Callback<Responce.StateResponce> {

            override fun onResponse(call: Call<Responce.StateResponce>, response: Response<Responce.StateResponce>) {



                if (response.isSuccessful()  ) {
                             if(response.body().getStatus()==1){

                                 listStateId = ArrayList<String>()
                                 listStateId!!.add("-1")

                                 listStateName = ArrayList<String>()
                                 listStateName!!.add("Select State")
                                   registerFragmentBinding. progressBar.visibility = View.GONE

                                 for( i in response.body().getData()){
                                     listStateId!!.add(i.getState_id())
                                     listStateName!!.add(i.getState_name())
                                  }


                                 val   yOJAdapter = ArrayAdapter<String>(LoginActivity.loginActivity, android.R.layout.simple_list_item_1, listStateName)
                                  registerFragmentBinding.spState.adapter = yOJAdapter

                                 registerFragmentBinding.spState.onItemSelectedListener = onItemSelectedListenerState
                             //    registerFragmentBinding.spState.setSelection(0)

                                 val   yOJAdapter2 = ArrayAdapter<String>(LoginActivity.loginActivity, android.R.layout.simple_list_item_1, listStateName)
                                 registerFragmentBinding.spPreState.adapter = yOJAdapter2
                                registerFragmentBinding.spPreState.onItemSelectedListener = onItemSelectedListenerPreState
                             //    registerFragmentBinding.spPreState.setSelection(0)

                               }else{
                              listStateId = ArrayList<String>()
                                 listStateId!!.add("-1")

                                 listStateName = ArrayList<String>()
                                 listStateName!!.add("No state available")
                                 registerFragmentBinding. progressBar.visibility = View.GONE

                                 val   yOJAdapter = ArrayAdapter<String>(LoginActivity.loginActivity, android.R.layout.simple_list_item_1, listStateName)
                                 registerFragmentBinding.spState.adapter = yOJAdapter
                             //    registerFragmentBinding.spState.setSelection(0)
                                 val   yOJAdapter2 = ArrayAdapter<String>(LoginActivity.loginActivity, android.R.layout.simple_list_item_1, listStateName)
                                 registerFragmentBinding.spPreState.adapter = yOJAdapter2
                             //    registerFragmentBinding.spPreState.setSelection(0)
                                   }

                } else{
                  listStateId = ArrayList<String>()
                    listStateId!!.add("-1")

                    listStateName = ArrayList<String>()
                    listStateName!!.add("No state available")
                    registerFragmentBinding. progressBar.visibility = View.GONE

                    val   yOJAdapter = ArrayAdapter<String>(LoginActivity.loginActivity, android.R.layout.simple_list_item_1, listStateName)
                    registerFragmentBinding.spState.adapter = yOJAdapter
                 //   registerFragmentBinding.spState.setSelection(0)
                    val   yOJAdapter2 = ArrayAdapter<String>(LoginActivity.loginActivity, android.R.layout.simple_list_item_1, listStateName)
                    registerFragmentBinding.spPreState.adapter = yOJAdapter2
                  //  registerFragmentBinding.spPreState.setSelection(0)
                }
            }
            override fun onFailure(call: Call<Responce.StateResponce>, t: Throwable) {
              listStateId = ArrayList<String>()
                listStateId!!.add("-1")

                listStateName = ArrayList<String>()
                listStateName!!.add("No state available")
                registerFragmentBinding. progressBar.visibility = View.GONE

                val   yOJAdapter = ArrayAdapter<String>(LoginActivity.loginActivity, android.R.layout.simple_list_item_1, listStateName)
                registerFragmentBinding.spState.adapter = yOJAdapter
              //  registerFragmentBinding.spState.setSelection(0)

                val   yOJAdapter2 = ArrayAdapter<String>(LoginActivity.loginActivity, android.R.layout.simple_list_item_1, listStateName)
                registerFragmentBinding.spPreState.adapter = yOJAdapter2
               // registerFragmentBinding.spPreState.setSelection(0)

                t.printStackTrace()
            }
        })
    }




    private fun getCities(state_id : String){



        registerFragmentBinding.  progressBar.isIndeterminate = true
        registerFragmentBinding. progressBar.visibility = View.VISIBLE
        registerFragmentBinding. progressBar.setClickable(false)

        apiService.all_Cities(state_id).enqueue(object : Callback<Responce.CityResponce> {

            override fun onResponse(call: Call<Responce.CityResponce>, response: Response<Responce.CityResponce>) {
                if (response.isSuccessful()  ) {
                    if(response.body().getStatus()==1){
                        registerFragmentBinding. progressBar.visibility = View.GONE

                        listCityId = ArrayList<String>()
                        listCityId!!.add("-1")

                        listCityName = ArrayList<String>()
                        listCityName!!.add("Select City")


                        for( i in response.body().getData()){
                            listCityId!!.add(i.getCity_id())
                            listCityName!!.add(i.getCity_name())
                        }

                        listCityId!!.add("-2")
                        listCityName!!.add("Other")

                        val   yOJAdapter = ArrayAdapter<String>(LoginActivity.loginActivity, android.R.layout.simple_list_item_1, listCityName)
                        registerFragmentBinding.spCity.adapter = yOJAdapter

                        registerFragmentBinding.spCity.onItemSelectedListener = onItemSelectedListenerCity

                       // registerFragmentBinding.spCity.setSelection(0)

                    }else{
                        listCityId = ArrayList<String>()
                        listCityId!!.add("-1")
                        listCityName = ArrayList<String>()
                        listCityName!!.add("No city available")

                        val   yOJAdapter = ArrayAdapter<String>(LoginActivity.loginActivity, android.R.layout.simple_list_item_1, listCityName)
                        registerFragmentBinding.spCity.adapter = yOJAdapter
                     //   registerFragmentBinding.spCity.setSelection(0)
                        registerFragmentBinding. progressBar.visibility = View.GONE

                    }
                } else{
                    listCityId = ArrayList<String>()
                    listCityId!!.add("-1")
                    listCityName = ArrayList<String>()
                    listCityName!!.add("No city available")
                    registerFragmentBinding. progressBar.visibility = View.GONE

                    val   yOJAdapter = ArrayAdapter<String>(LoginActivity.loginActivity, android.R.layout.simple_list_item_1, listCityName)
                    registerFragmentBinding.spCity.adapter = yOJAdapter
                  //  registerFragmentBinding.spCity.setSelection(0)
                }
            }
            override fun onFailure(call: Call<Responce.CityResponce>, t: Throwable) {

                listCityId = ArrayList<String>()
                listCityId!!.add("-1")
                listCityName = ArrayList<String>()
                listCityName!!.add("No city available")

                registerFragmentBinding. progressBar.visibility = View.GONE
                val   yOJAdapter = ArrayAdapter<String>(LoginActivity.loginActivity, android.R.layout.simple_list_item_1, listCityName)
                registerFragmentBinding.spCity.adapter = yOJAdapter
              //  registerFragmentBinding.spCity.setSelection(0)
                t.printStackTrace()
            }
        })
    }
    private fun getPreCities(state_id : String){


        registerFragmentBinding.  progressBar.isIndeterminate = true
        registerFragmentBinding. progressBar.visibility = View.VISIBLE
        registerFragmentBinding. progressBar.setClickable(false)

        apiService.all_Cities(state_id).enqueue(object : Callback<Responce.CityResponce> {

            override fun onResponse(call: Call<Responce.CityResponce>, response: Response<Responce.CityResponce>) {
                if (response.isSuccessful()  ) {
                    if(response.body().getStatus()==1){
                        registerFragmentBinding. progressBar.visibility = View.GONE

                        listPreCityId = ArrayList<String>()
                        listPreCityId!!.add("-1")


                        listPreCityName = ArrayList<String>()
                        listPreCityName!!.add("Select City")

                        for( i in response.body().getData()){
                            listPreCityId!!.add(i.getCity_id())
                            listPreCityName!!.add(i.getCity_name())
                        }

                        listPreCityId!!.add("-2")
                        listPreCityName!!.add("Other")

                        val   yOJAdapter = ArrayAdapter<String>(LoginActivity.loginActivity, android.R.layout.simple_list_item_1, listPreCityName)
                        registerFragmentBinding.spPreCity.adapter = yOJAdapter
                     //  registerFragmentBinding.spPreCity.setSelection(0)
                     registerFragmentBinding.spPreCity.onItemSelectedListener = onItemSelectedListenerPreCity



                    }else{

                        listPreCityId = ArrayList<String>()
                        listPreCityId!!.add("-1")
                        listPreCityName = ArrayList<String>()
                        listPreCityName!!.add("No city available")

                        val   yOJAdapter = ArrayAdapter<String>(LoginActivity.loginActivity, android.R.layout.simple_list_item_1, listPreCityName)
                        registerFragmentBinding.spPreCity.adapter = yOJAdapter
                     //   registerFragmentBinding.spPreCity.setSelection(0)
                        registerFragmentBinding. progressBar.visibility = View.GONE

                    }
                } else{
                    listPreCityId = ArrayList<String>()
                    listPreCityId!!.add("-1")
                    listPreCityName = ArrayList<String>()
                    listPreCityName!!.add("No city available")
                    registerFragmentBinding. progressBar.visibility = View.GONE


                    val   yOJAdapter = ArrayAdapter<String>(LoginActivity.loginActivity, android.R.layout.simple_list_item_1, listPreCityName)
                    registerFragmentBinding.spPreCity.adapter = yOJAdapter
                  // registerFragmentBinding.spPreCity.setSelection(0)
                }
            }
            override fun onFailure(call: Call<Responce.CityResponce>, t: Throwable) {
                listPreCityId = ArrayList<String>()
                listPreCityId!!.add("-1")

                listPreCityName = ArrayList<String>()
                listPreCityName!!.add("No city available")
                registerFragmentBinding. progressBar.visibility = View.GONE

                val   yOJAdapter = ArrayAdapter<String>(LoginActivity.loginActivity, android.R.layout.simple_list_item_1, listPreCityName)
                registerFragmentBinding.spPreCity.adapter = yOJAdapter
              //  registerFragmentBinding.spPreCity.setSelection(0)
                t.printStackTrace()
            }
        })
    }

    val onItemSelectedListenerState = object : AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val  schId = listStateId!!.get(position)

            if(!schId.equals("-1")){
                getCities(schId)
            }
            if(schId.equals("-2")){
                registerFragmentBinding.etOtherCity.visibility = View.VISIBLE
            }else{
                registerFragmentBinding.etOtherCity.visibility = View.GONE
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {}
    }
    val onItemSelectedListenerPreState = object : AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val  schId = listStateId!!.get(position)
            if(!schId.equals("-1")){
                getPreCities(schId)
            }

            if(schId.equals("-2")){
                registerFragmentBinding.etOtherPreCity.visibility = View.VISIBLE
            }else{
                registerFragmentBinding.etOtherPreCity.visibility = View.GONE
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {}
    }
    val onItemSelectedListenerCity = object : AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val  schId = listCityId!!.get(position)


            if(schId.equals("-2")){
                registerFragmentBinding.etOtherCity.visibility = View.VISIBLE
            }else{
                registerFragmentBinding.etOtherCity.visibility = View.GONE
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {}
    }
    val onItemSelectedListenerPreCity = object : AdapterView.OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val  schId = listPreCityId!!.get(position)


            if(schId.equals("-2")){
                registerFragmentBinding.etOtherPreCity.visibility = View.VISIBLE
            }else{
                registerFragmentBinding.etOtherPreCity.visibility = View.GONE
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>) {}
    }

}





