package com.think360.sotaknights.fragments

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.think360.sotaknights.R
import com.think360.sotaknights.api.AppController
import com.think360.sotaknights.api.data.Responce
import com.think360.sotaknights.api.interfaces.ApiService
import com.think360.sotaknights.api.EventToRefresh
import com.think360.sotaknights.databinding.TaskFragmentBinding
import com.think360.sotaknights.databinding.TaskListItemBinding
import com.think360.sotaknights.model.TaskItem
import com.think360.sotaknights.recyclerbindingadapter.DataBoundAdapter
import com.think360.sotaknights.recyclerbindingadapter.DataBoundViewHolder
import com.think360.sotaknights.util.ConnectivityReceiver
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
class TaskFragment : Fragment() {
    @Inject
    internal lateinit var apiService: ApiService
   // private val compositeDisposable = CompositeDisposable()
    private lateinit var  taskFragmentBinding : TaskFragmentBinding
    companion object {
        fun newInstance(): TaskFragment {
            return TaskFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    taskFragmentBinding  = DataBindingUtil.inflate<TaskFragmentBinding>(inflater,R.layout.task_fragment, container, false)
        (activity.application as AppController).getComponent().inject(this@TaskFragment)
        if(ConnectivityReceiver.isConnected()){
            getTask()
        }else{
            Toast.makeText(AppController.getInstance().applicationContext, "No internet connection!", Toast.LENGTH_SHORT).show()
        }

/*        var refresh  = true
     compositeDisposable.add((activity.application as AppController).bus().toObservable().
             subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { o ->
            if (o is EventToRefresh && o.body == R.id.navigation_task &&  refresh) {
                getTask()
                refresh =false
            }

        })*/

        taskFragmentBinding.swiperefresh.setOnRefreshListener{
            if(ConnectivityReceiver.isConnected()){
                getTask()
            }else{
                Toast.makeText(AppController.getInstance().applicationContext, "No internet connection!", Toast.LENGTH_SHORT).show()
            }
        }
        return taskFragmentBinding.root
    }

    internal inner class ProfileBindingAdapter : DataBoundAdapter<TaskListItemBinding> {
        var mProfileList : MutableList<TaskItem>
        constructor(mProfileList: MutableList<TaskItem>) : super(R.layout.task_list_item) {
            this. mProfileList = mProfileList
        }
        override fun bindItem(holder: DataBoundViewHolder<TaskListItemBinding>?, position: Int, payloads: MutableList<Any>?) {
            holder?.binding?.data = mProfileList.get(position)
        }
        override fun getItemCount(): Int {
            return mProfileList.size
        }
    }

    private fun getTask(){
        taskFragmentBinding.swiperefresh.setRefreshing(true)
        apiService.getTask(AppController.getSharedPref().getString("user_id","null")).enqueue(object : Callback<Responce.SotaTaskResponce> {
            override fun onResponse(call: Call<Responce.SotaTaskResponce>, response: Response<Responce.SotaTaskResponce>) {

                if (  response.body().getStatus()) {

                        taskFragmentBinding.tvNoData.visibility = View.GONE
                        taskFragmentBinding.rv.visibility = View.VISIBLE
                        val adapter = ProfileBindingAdapter(response.body().getTaskList())
                        taskFragmentBinding.rv.adapter = adapter
                        adapter.notifyDataSetChanged()
                        taskFragmentBinding.swiperefresh.setRefreshing(false)


                } else {
                    taskFragmentBinding.tvNoData.visibility = View.VISIBLE
                    taskFragmentBinding.rv.visibility = View.GONE
                    taskFragmentBinding.swiperefresh.setRefreshing(false)
                }
            }
            override fun onFailure(call: Call<Responce.SotaTaskResponce>, t: Throwable) {
                taskFragmentBinding.tvNoData.visibility = View.VISIBLE
                taskFragmentBinding.rv.visibility = View.GONE
                taskFragmentBinding.swiperefresh.setRefreshing(false)
                t.printStackTrace()
            }
        })

    }

}