package com.example.paysimplextest.domain.base

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import java.io.IOException
import java.nio.charset.Charset
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

abstract class BaseFragment<VM : BaseViewModel, T : ViewDataBinding> : Fragment() {

    protected var TAG: String = this.javaClass.simpleName
    private var mActivity: BaseActivity<VM, T>? = null
    lateinit var mContext: Context

    var binder: T? = null
    private lateinit var mViewModel: VM

    private var rootView: View? = null
    private var isFragmentLoaded = false

    @get:LayoutRes
    abstract val layoutRes: Int

    abstract fun getViewModel(): VM

    fun getDataBinder(): T {
        return this.binder!!
    }

    override fun onAttach(context: Context) {
        try {
            super.onAttach(context)
            if (context is BaseActivity<*, *>) {
                val activity = context as BaseActivity<VM, T>?
                this.mActivity = activity

                mContext = context
                //activity?.onFragmentAttached()
            }
        } catch (e: Throwable) {
            throw ClassCastException("$context must implement FragmentListener")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binder = DataBindingUtil.inflate(inflater, layoutRes, container, false)!!
        binder?.lifecycleOwner = viewLifecycleOwner
        rootView = binder?.root!!
        return rootView!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            mViewModel.let { vm ->
                binder?.let { binder ->
                    onReadyToRender(view, vm, binder, savedInstanceState)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binder = null
    }

    override fun onDetach() {
        super.onDetach()
        this.mActivity = null
    }

    override fun onDestroy() {
        super.onDestroy()
        isFragmentLoaded = false
    }

    fun getBaseActivity(): BaseActivity<VM, T>? {
        return this.mActivity
    }

    protected abstract fun onReadyToRender(
        view: View,
        viewModel: VM,
        binder: T,
        savedInstanceState: Bundle?,
    )


    fun launchOnLifecycleScope(execute: suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            execute()
        }
    }

    //this will helps to print logs
    open fun printLog(MSG: String, VALUE: Any?) {
        Log.d(TAG, "$MSG\t-->\t$VALUE")
    }

    open fun getJSONFromAssets(): String {
        var json: String? = null
        val charset: Charset = Charsets.UTF_8
        try {
            val myUsersJSONFile = requireActivity().assets.open("Calender.json")
            val size = myUsersJSONFile.available()
            val buffer = ByteArray(size)
            myUsersJSONFile.read(buffer)
            myUsersJSONFile.close()
            json = String(buffer, charset)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }
        return json
    }

    @SuppressLint("NewApi")
    fun getSlots(startTime: String, endTime: String, minInterval: Int): ArrayList<LocalTime> {

        val simpleDateFormat = SimpleDateFormat("hh:mm")

        val date1 = simpleDateFormat.parse(startTime)
        val date2 = simpleDateFormat.parse(endTime)

        val difference = date2.time - date1.time
        val days = (difference / (1000 * 60 * 60 * 24)).toInt()
        var hours = (((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60))).toInt() + 1
        hours = if (hours < 0) -hours else hours

        Log.i("======= Hours", " :: $hours");

        val slotLoops = Duration.ofHours(hours.toLong()).toMinutes().toInt() / minInterval
        val slotTimes: ArrayList<LocalTime> = ArrayList(slotLoops)
        var slotStartTime = LocalTime.parse(startTime/*"06:00:00"*/)

        for (i in 1..slotLoops) {
            if (slotStartTime < LocalTime.parse(endTime)) {
                slotTimes.add(slotStartTime)
                slotStartTime = slotStartTime.plusMinutes(minInterval.toLong())
            }
        }

        System.out.println("${slotTimes.size} time slots: ");
        System.out.println(slotTimes);

        return slotTimes
    }

    fun convertDate(value: String, input: String, output: String): String {
        val originalFormat = SimpleDateFormat(input)
        val targetFormat = SimpleDateFormat(output)
        var date: Date? = null
        try {
            date = originalFormat.parse(value)
            println("Old Format :   " + originalFormat.format(date!!))
            println("New Format :   " + targetFormat.format(date))
            Log.d(ContentValues.TAG, "onCreate: ${targetFormat.format(date)}")
        } catch (ex: ParseException) {
            // Handle Exception.
        }
        return targetFormat.format(date)
    }


}
