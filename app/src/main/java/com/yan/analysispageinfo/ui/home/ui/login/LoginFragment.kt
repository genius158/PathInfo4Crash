package com.yan.analysispageinfo.ui.home.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yan.analysispageinfo.R
import xcrash.XCrash


class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        view.findViewById<View>(R.id.oom).setOnClickListener {
            tryOOM()
        }
        view.findViewById<View>(R.id.nativeCrash).setOnClickListener {
            XCrash.testNativeCrash(false)
        }
    }

    private fun tryOOM() {
        val list: MutableList<ByteArray> = ArrayList()

        while (true) {
            list.add(ByteArray(1024 * 1024)) // 每次增加一个1M大小的数组对象
        }
    }

}