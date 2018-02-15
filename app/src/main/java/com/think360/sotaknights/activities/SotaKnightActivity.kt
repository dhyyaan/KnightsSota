package com.think360.sotaknights.activities

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.think360.sotaknights.R
import com.think360.sotaknights.api.AppController
import com.think360.sotaknights.api.EventToRefresh
import com.think360.sotaknights.customviews.CircularTextView
import com.think360.sotaknights.databinding.SotaKnightActivityBinding
import com.think360.sotaknights.fragments.*
import java.util.ArrayList
import android.app.Activity
import android.content.Intent

class SotaKnightActivity : AppCompatActivity() //, ConnectivityReceiver.ConnectivityReceiverListener
{

    var tvAvailibility  :  CircularTextView? = null
   internal lateinit var sotaKnightActivityBinding  :  SotaKnightActivityBinding
    companion object {
        var sotaKnightActivity : SotaKnightActivity? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sotaKnightActivityBinding   = DataBindingUtil.setContentView<SotaKnightActivityBinding>(this ,R.layout.sota_knight_activity)
        sotaKnightActivity = this
        tvAvailibility  = findViewById(R.id.tvAvailibility)
        sotaKnightActivityBinding.viewPager!!.setAdapter(PagerAdapter(supportFragmentManager, getFragmentArrrayList()))
        sotaKnightActivityBinding. viewPager!!.setOffscreenPageLimit(5)
        disableShiftMode(sotaKnightActivityBinding.navigation)
        if(intent.getIntExtra("getTaskList",4)==2){
           mOnNavigationItemSelectedListener.onNavigationItemSelected((sotaKnightActivityBinding.navigation.getMenu().findItem(R.id.navigation_task)))
            sotaKnightActivityBinding.navigation.selectedItemId =  R.id.navigation_task
        }
        sotaKnightActivityBinding .navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_map -> {
                sotaKnightActivityBinding.viewPager!!.setCurrentItem(0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_cmp -> {
                (application as AppController).bus().send(EventToRefresh(R.id.navigation_cmp))
                sotaKnightActivityBinding.viewPager!!.setCurrentItem(1)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                (application as AppController).bus().send(EventToRefresh(R.id.navigation_profile))
                sotaKnightActivityBinding.viewPager!!.setCurrentItem(2)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_task -> {

                    (application as AppController).bus().send(EventToRefresh(R.id.navigation_task))
                sotaKnightActivityBinding.viewPager!!.setCurrentItem(3)

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                sotaKnightActivityBinding.viewPager!!.setCurrentItem(4)

                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }

    fun disableShiftMode(view: BottomNavigationView) {
        val menuView = view.getChildAt(0) as BottomNavigationMenuView
        try {
            val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
            shiftingMode.isAccessible = true
            shiftingMode.setBoolean(menuView, false)
            shiftingMode.isAccessible = false
            for (i in 0..menuView.childCount - 1) {
                val item = menuView.getChildAt(i) as BottomNavigationItemView
                item.setShiftingMode(false)
                // set once again checked value, so view will be updated
                item.setChecked(item.itemData.isChecked)
            }
        } catch (e: NoSuchFieldException) {
            Log.e("BNVHelper", "Unable to get shift mode field", e)
        } catch (e: IllegalAccessException) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e)
        }

    }

    private fun getFragmentArrrayList(): ArrayList<Fragment> {
        val fragmentSparseArray = ArrayList<Fragment>()
        fragmentSparseArray.add(MapFragment.newInstance())
        fragmentSparseArray.add(CompanyFragment.newInstance())
        fragmentSparseArray.add(ProfileFragment.newInstance())
        fragmentSparseArray.add(TaskFragment.newInstance())
        fragmentSparseArray.add(SettingsFragment.newInstance())

        return fragmentSparseArray

    }
    private inner class PagerAdapter(fm: FragmentManager, fragmentSparseArray: ArrayList<Fragment>) : FragmentPagerAdapter(fm) {

        private var fragmentSparseArray = ArrayList<Fragment>()

        init {
            this.fragmentSparseArray = fragmentSparseArray
        }

        override fun getItem(position: Int): Fragment {
            return fragmentSparseArray[position]
        }

        override fun getCount(): Int {
            return fragmentSparseArray.size
        }
    }
    fun replaceFragment(fragment : Fragment) {
        val transaction = supportFragmentManager.beginTransaction()

        transaction.addToBackStack(fragment.javaClass.simpleName)
        transaction.replace(R.id.fragContainer, fragment).commit()
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
            val inputMethodManager = getSystemService(RuntimePermissionsActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
      when (requestCode) {
      // Check for the integer request code originally supplied to startResolutionForResult().
         1000 -> when (resultCode) {
              Activity.RESULT_OK -> {
                  startActivity(Intent(applicationContext, SplashActivity::class.java))
                  finish()
              }
              Activity.RESULT_CANCELED -> finish()

          }
      }


    }


 /*   override fun onResume() {
        super.onResume()
        // register connection status listener
        AppController.getInstance().setConnectivityListener(this)
    }


    override fun onPause() {
        super.onPause()
        // register connection status listener
        AppController.getInstance().setConnectivityListener(null)
    }

    // Showing the status in Snackbar
    private fun showSnack(isConnected: Boolean) {
        val message: String
        val color: Int
        if (isConnected) {
            message = "Good! Connected to Internet"
            color = Color.WHITE
        } else {
            message = "Sorry! Not connected to internet"
            color = Color.RED
        }

        val snackbar = Snackbar.make(sotaKnightActivityBinding.rlmain, message, Snackbar.LENGTH_LONG)

        val sbView = snackbar.view
        val textView = sbView.findViewById<TextView>(android.support.design.R.id.snackbar_text) as TextView
        textView.setTextColor(color)
        snackbar.show()
    }
   override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showSnack(isConnected)
    }*/

}
