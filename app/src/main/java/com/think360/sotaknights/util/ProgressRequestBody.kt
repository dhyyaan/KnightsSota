package com.think360.sotaknights.util

import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.Api
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okio.BufferedSink
import retrofit2.Retrofit
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * Created by think360 on 13/10/17.
 */
class ProgressRequestBody : RequestBody {
    var numWriteToCalls = 0
    var  lastProgressPercentUpdate = 0f
    val mFile: File
    val ignoreFirstNumberOfWriteToCalls: Int
    companion object {
        private val DEFAULT_BUFFER_SIZE = 2048
    }

    constructor(mFile: File) : super() {
        this.mFile = mFile
        ignoreFirstNumberOfWriteToCalls = 0

    }

    constructor(mFile: File, ignoreFirstNumberOfWriteToCalls: Int) : super() {
        this.mFile = mFile
        this.ignoreFirstNumberOfWriteToCalls = ignoreFirstNumberOfWriteToCalls
    }




    protected val getProgressSubject: PublishSubject<Float> = PublishSubject.create<Float>()

    fun getProgressSubject(): Observable<Float> {
        return getProgressSubject
    }


    override fun contentType(): MediaType {
        return MediaType.parse("video/mp4")
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return mFile.length()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        numWriteToCalls++

        val fileLength = mFile.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val `in` = FileInputStream(mFile)
        var uploaded: Long = 0

        try {
            var read: Int
            read = `in`.read(buffer)
            while (read != -1) {

                uploaded += read.toLong()
                sink.write(buffer, 0, read)
                read = `in`.read(buffer)

                // when using HttpLoggingInterceptor it calls writeTo and passes data into a local buffer just for logging purposes.
                // the second call to write to is the progress we actually want to track
                if (numWriteToCalls > ignoreFirstNumberOfWriteToCalls) {
                    val progress = (uploaded.toFloat() / fileLength.toFloat()) * 100f
                    //prevent publishing too many updates, which slows upload, by checking if the upload has progressed by at least 1 percent

                    if (progress - lastProgressPercentUpdate > 1 || progress == 100f) {
                        // publish progress
                        getProgressSubject.onNext(progress)
                        lastProgressPercentUpdate = progress
                    }
                }
            }
        } finally {
            `in`.close()
        }
    }
    fun postVideo(){
/*        val api : Api = Retrofit.Builder()
                .client(OkHttpClient.Builder()
                        //.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build())
                .baseUrl("BASE_URL")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(Api::class.java)

        val videoPart = ProgressRequestBody(File(VIDEO_URI))
        //val videoPart = ProgressRequestBody(File(VIDEO_URI), 1) //HttpLoggingInterceptor workaround
        val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("example[name]", place.providerId)
                .addFormDataPart("example[video]","video.mp4", videoPart)
                .build()

        videoPart.getProgressSubject()
                .subscribeOn(Schedulers.io())
                .subscribe { percentage ->
                    Log.i("PROGRESS", "${percentage}%")
                }

        var postSub : Disposable?= null
        postSub = api.postVideo(requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ r ->
                },{e->
                    e.printStackTrace()
                    postSub?.dispose();

                }, {
                    Toast.makeText(this,"Upload SUCCESS!!",Toast.LENGTH_LONG).show()
                    postSub?.dispose();
                })*/
    }
}

