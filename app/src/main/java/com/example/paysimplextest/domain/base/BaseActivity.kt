package com.example.paysimplextest.domain.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.paysimplextest.R
import com.example.paysimplextest.data.common.utils.NetworkUtils
import java.text.SimpleDateFormat
import java.util.*


abstract class BaseActivity<VM : BaseViewModel, T : ViewDataBinding> : AppCompatActivity() {

    var TAG: String? = javaClass.name
    lateinit var mContext: Context
    lateinit var baseView: View
    lateinit var binder: T

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        onReadyToRender(binder, mViewModel, savedInstanceState)
    }

    private lateinit var mViewModel: VM
    abstract fun getViewModel(): VM

    @get:LayoutRes
    abstract val layoutRes: Int

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mContext = this
        baseView = window.decorView.rootView
        mViewModel = getViewModel()
        setContentView(layoutRes)
    }

    override fun setContentView(layoutRes: Int) {
        val coordinatorLayout: CoordinatorLayout =
            layoutInflater.inflate(R.layout.activity_base, null) as CoordinatorLayout
        val activityContainer: FrameLayout = coordinatorLayout.findViewById(R.id.layout_container)
        binder = DataBindingUtil.inflate(layoutInflater, layoutRes, activityContainer, true)
        binder.lifecycleOwner = this
        super.setContentView(coordinatorLayout)

    }

    //this will helps to open activity
    open fun openActivity(aClass: Class<*>?) {
        startActivity(Intent(mContext, aClass))
    }

    //this will helps to close activity
    open fun closeActivity() {
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        closeActivity()
    }

    //this will helps to check Internet connection
    fun isNetworkConnected(): Boolean {
        val flag = NetworkUtils.isNetworkConnected(applicationContext)
        if (!flag) {
            Toast.makeText(mContext, "Check Internet Connection", Toast.LENGTH_SHORT).show()
        }
        return flag
    }

    abstract fun onReadyToRender(binder: T, mViewModel: VM, savedInstanceState: Bundle?)

    //this will helps to hide keyboard
    fun hideKeyboard() {
        var view: View? = this.currentFocus
        if (view == null)
            view = View(this)
        val inputMethodManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

    }

    //this will helps to get current date and time
    open fun getCurrentDateTime(): String? {
        val smsTime = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("dd MMM yyyy'&'HH:mm:ss")
        return simpleDateFormat.format(smsTime.time)
    }

    //this will helps to print logs
    open fun printLog(MSG: String, VALUE: Any?) {
        Log.d(TAG, "$MSG\t-->\t$VALUE")
    }
}