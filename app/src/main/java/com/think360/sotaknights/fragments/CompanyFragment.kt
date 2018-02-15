package com.think360.sotaknights.fragments
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.think360.sotaknights.R
import com.think360.sotaknights.api.AppController

import com.think360.sotaknights.api.data.Responce
import com.think360.sotaknights.api.interfaces.ApiService
import com.think360.sotaknights.api.EventToRefresh
import com.think360.sotaknights.databinding.CompanyFragmentBinding
import com.think360.sotaknights.util.ConnectivityReceiver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by think360 on 28/09/17.
 */
class CompanyFragment  : Fragment() {

    @Inject
    internal lateinit var apiService: ApiService
    private lateinit var companyFragmentBinding : CompanyFragmentBinding
    private val compositeDisposable = CompositeDisposable()
    companion object {
        fun newInstance(): CompanyFragment {
            return CompanyFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        companyFragmentBinding  = DataBindingUtil.inflate<CompanyFragmentBinding>(inflater, R.layout.company_fragment, container, false)
        (activity.application as AppController).getComponent().inject(this@CompanyFragment)
        var refresh  = true
        compositeDisposable.add((activity.application as AppController).bus().toObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { o ->
            if (o is EventToRefresh && o.body == R.id.navigation_cmp && refresh ) {
                if(ConnectivityReceiver.isConnected()){
                    getCompanyInfo()
                }else{
                    Toast.makeText(AppController.getInstance().applicationContext, "No internet connection!", Toast.LENGTH_SHORT).show()
                }


                refresh = false
            }
        })
        companyFragmentBinding.swiperefresh.setRefreshing(true)
        companyFragmentBinding.swiperefresh.setOnRefreshListener{

            if(ConnectivityReceiver.isConnected()){
                getCompanyInfo()
            }else{
                Toast.makeText(AppController.getInstance().applicationContext, "No internet connection!", Toast.LENGTH_SHORT).show()
            }
        }
        return companyFragmentBinding.root
    }

private fun getCompanyInfo(){

    companyFragmentBinding.webview.setWebViewClient(MyBrowser())
    companyFragmentBinding.webview.getSettings().setLoadsImagesAutomatically(true)
    companyFragmentBinding.webview.getSettings().setJavaScriptEnabled(true)
  //  companyFragmentBinding.webview.setVerticalScrollBarEnabled(true);
 companyFragmentBinding.webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY)
    val  mimeType = "text/html"
    val encoding = "UTF-8"
    apiService.getCompanyInfo().enqueue(object : Callback<Responce.CompanyInfoResponce> {

        override fun onResponse(call: Call<Responce.CompanyInfoResponce>, response: Response<Responce.CompanyInfoResponce>) {
            if (response.isSuccessful() && response.body().getStatus()) {
                companyFragmentBinding.swiperefresh.setRefreshing(false)
                companyFragmentBinding.tvNoData.visibility = View.GONE
                companyFragmentBinding.webview.visibility = View.VISIBLE
                companyFragmentBinding.webview.loadDataWithBaseURL("", response.body().description, mimeType, encoding, "");
            } else {
                companyFragmentBinding.tvNoData.visibility = View.VISIBLE
                companyFragmentBinding.webview.visibility = View.GONE
                companyFragmentBinding.swiperefresh.setRefreshing(false)
            }
        }
        override fun onFailure(call: Call<Responce.CompanyInfoResponce>, t: Throwable) {

            companyFragmentBinding.tvNoData.visibility = View.VISIBLE
            companyFragmentBinding.webview.visibility = View.GONE
            companyFragmentBinding.swiperefresh.setRefreshing(false)
            t.printStackTrace()
        }
    })
}

    private inner class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            companyFragmentBinding.swiperefresh.setRefreshing(false)
        }

        override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
            companyFragmentBinding.tvNoData.visibility = View.VISIBLE
            companyFragmentBinding.webview.visibility = View.GONE
            companyFragmentBinding.swiperefresh.setRefreshing(false)

        }
    }

}