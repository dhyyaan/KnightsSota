package com.think360.sotaknights.fragments

import android.app.Dialog
import android.databinding.DataBindingUtil

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager

import android.view.*
import android.widget.ImageView
import android.widget.Toast

import com.think360.sotaknights.R
import com.squareup.picasso.Picasso
import com.think360.sotaknights.activities.SotaKnightActivity
import com.think360.sotaknights.api.AppController
import com.think360.sotaknights.api.data.Responce
import com.think360.sotaknights.api.interfaces.ApiService
import com.think360.sotaknights.api.EventToRefresh
import com.think360.sotaknights.databinding.GridviewRowBinding
import com.think360.sotaknights.databinding.ProfileFragmentBinding
import com.think360.sotaknights.interfaces.ActionCallback
import com.think360.sotaknights.model.ImageUrl
import com.think360.sotaknights.recyclerbindingadapter.DataBoundAdapter
import com.think360.sotaknights.recyclerbindingadapter.DataBoundViewHolder
import com.think360.sotaknights.util.ConnectivityReceiver
import com.think360.sotaknights.util.ZoomInOutImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


/**
 * Created by think360 on 28/09/17.
 */
class ProfileFragment : Fragment() {
    @Inject
    internal lateinit var apiService: ApiService
    private val compositeDisposable = CompositeDisposable()
    private lateinit var profileFragmentBinding: ProfileFragmentBinding
    private var profileBindingAdapter: ProfileBindingAdapter? = null
    private lateinit var  actionCallback : ActionCallback
    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        profileFragmentBinding  = DataBindingUtil.inflate<ProfileFragmentBinding>(inflater, R.layout.profile_fragment, container, false)
        (activity.application as AppController).getComponent().inject(this@ProfileFragment)
         var refresh  = true
        compositeDisposable.add((activity.application as AppController).bus().toObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { o ->
            if (o is EventToRefresh && o.body == R.id.navigation_profile && refresh) {
                if(ConnectivityReceiver.isConnected()){
                    getProfile()
                }else{
                    Toast.makeText(AppController.getInstance().applicationContext, "No internet connection!", Toast.LENGTH_SHORT).show()
                }


                refresh = false
            }
        })
        actionCallback = object : ActionCallback {
            override   fun onClick(item: Any) {
                val objItem = item as String
                showDailog(objItem)
            }

        }
        return profileFragmentBinding.root
    }

    private fun getProfile(){
        profileFragmentBinding.progressBar.isIndeterminate = true
        profileFragmentBinding.progressBar.visibility = View.VISIBLE
        profileFragmentBinding.progressBar.setClickable(false)
        apiService.getProfile(AppController.getSharedPref().getString("user_id","null")).enqueue(object : Callback<Responce.ProfileResponce> {
                        override fun onResponse(call: Call<Responce.ProfileResponce>, response: Response<Responce.ProfileResponce>) {

                            if (response.body().getStatus()  ) {

                                profileFragmentBinding.tvNoData.setVisibility(View.GONE)
                                profileFragmentBinding.nscrolview.setVisibility(View.VISIBLE)
                                profileFragmentBinding.progressBar.setVisibility(View.GONE)

                                profileFragmentBinding.cIvProfilePic.setText(response.body().getData().getShort_name())
                                profileFragmentBinding.tvProfileName.setText(response.body().getData().getUser_name())
                                profileFragmentBinding.tvName.setText(response.body().getData().getName())
                                profileFragmentBinding.tvEmail.setText(response.body().getData().getEmail())
                                profileFragmentBinding.tvPhone.setText(response.body().getData().getMobile())
                                profileFragmentBinding.tvAdd.setText(response.body().getData().getAddress())
                                profileFragmentBinding.tvCity.setText(response.body().getData().getCity())
                                profileFragmentBinding.tvState.setText(response.body().getData().getState())
                                profileFragmentBinding.tvAcNo.setText(response.body().getData().getBank_acc_no())
                                profileFragmentBinding.tvBranch.setText(response.body().getData().getBranch_name())
                                profileFragmentBinding.tvTrade.setText(response.body().getData().getSota_business_trade())
                                profileFragmentBinding.tvIFCCode.setText(response.body().getData().getIfsc())
                                val imageItems = ArrayList<ImageUrl>()

                                if(!response.body().getData().getAdhaar().equals("no image")){
                                    imageItems.add(ImageUrl(response.body().getData().getAdhaar()))
                                }

                                if(!response.body().getData().getAadhar_1().equals("no image")){
                                    imageItems.add(ImageUrl(response.body().getData().getAadhar_1()))
                                }

                                if(!response.body().getData().getGun().equals("no image")){
                                    imageItems.add(ImageUrl(response.body().getData().getGun()))
                                }

                                if(!response.body().getData().getGun_1().equals("no image")){
                                    imageItems.add(ImageUrl(response.body().getData().getGun_1()))
                                }


                                if(!response.body().getData().getDrving().equals("no image")){
                                    imageItems.add(ImageUrl(response.body().getData().getDrving()))

                                }

                                if(!response.body().getData().getDriving_2().equals("no image")){
                                    imageItems.add(ImageUrl(response.body().getData().getDriving_2()))

                                }
                                if(!response.body().getData().getPsara().equals("no image")){
                                    imageItems.add(ImageUrl(response.body().getData().getPsara()))

                                }
                                if(!response.body().getData().getPasara_1().equals("no image")){
                                    imageItems.add(ImageUrl(response.body().getData().getPasara_1()))

                                }


                           if(!response.body().getData().getSota_profile_pic().equals("no Image")){
                                    profileFragmentBinding.cIvProfilePic.visibility = View.GONE
                                    profileFragmentBinding.sotaProfilePic.visibility = View.VISIBLE
                                    Picasso.with(AppController.getInstance().applicationContext).load(response.body().getData().getSota_profile_pic()).resize(150, 150).into( profileFragmentBinding.sotaProfilePic)
                                }else{
                                    profileFragmentBinding.cIvProfilePic.visibility = View.VISIBLE
                                    profileFragmentBinding.sotaProfilePic.visibility = View.GONE
                                }
                                profileBindingAdapter = ProfileBindingAdapter(actionCallback,imageItems)
                                val layoutManager = GridLayoutManager(AppController.getInstance().applicationContext, 2)
                                profileFragmentBinding.rv!!.layoutManager = layoutManager
                                profileFragmentBinding.rv!!.adapter = profileBindingAdapter
                                profileFragmentBinding.rv!!.adapter.notifyDataSetChanged()


                            } else {
                                profileFragmentBinding.tvNoData.setVisibility(View.VISIBLE)
                                profileFragmentBinding.nscrolview.setVisibility(View.GONE)
                                profileFragmentBinding.progressBar.setVisibility(View.GONE)
                            }
                        }
                    override fun onFailure(call: Call<Responce.ProfileResponce>, t: Throwable) {
                        profileFragmentBinding.tvNoData.setVisibility(View.VISIBLE)
                        profileFragmentBinding.nscrolview.setVisibility(View.GONE)
                        profileFragmentBinding.progressBar.setVisibility(View.GONE)
                        t.printStackTrace()
                        }
                    })

    }
    internal inner class ProfileBindingAdapter : DataBoundAdapter<GridviewRowBinding> {
       var actionCallback : ActionCallback
        val imageList : ArrayList<ImageUrl>
        constructor(actionCallback : ActionCallback,imageList : ArrayList<ImageUrl>) : super(R.layout.gridview_row){
            this.actionCallback = actionCallback
            this.imageList = imageList
        }
        override fun bindItem(holder: DataBoundViewHolder<GridviewRowBinding>?, position: Int, payloads: MutableList<Any>?) {
            holder?.binding!!.callback = actionCallback
            holder.binding?.data = imageList.get(position)
            Picasso.with(AppController.getInstance().applicationContext).load(imageList.get(position).image).resize(150, 150).into( holder.binding!!.imageView1)

        }
        override fun getItemCount(): Int {
            return imageList.size
        }

    }
    private fun showDailog( image : String) {

        val dialog = Dialog(SotaKnightActivity.sotaKnightActivity,android.R.style.Theme_Light)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dailog_view_report)
        val window = dialog.window
        val wlp = window!!.attributes

        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val report = dialog.findViewById<ZoomInOutImageView>(R.id.ivReport)
        Picasso.with(context).load(image).into( report)
        // report.setImageBitmap(bitmap)
        val welcome_text = dialog.findViewById<ImageView>(R.id.ivCancel)
        welcome_text.setOnClickListener{  dialog.dismiss()

        }


        dialog.show()

    }
}