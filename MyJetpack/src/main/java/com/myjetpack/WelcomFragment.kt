package com.myjetpack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.myjetpack.databinding.FragmentWelcomBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WelcomFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WelcomFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentWelcomBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_welcom, container, false
        )
        val bundle = Bundle()
        bundle.putString("name", "TeaOf")
        val navOption = navOptions {
            anim {

            }
        }
        //1. 跳转&&传值  通过bundle
        binding.signIn.setOnClickListener { findNavController().navigate(R.id.loginFragment, bundle, navOption) }
        binding.signUp.setOnClickListener { findNavController().navigate(R.id.registFragment, bundle, navOption) }
        binding.toInner.setOnClickListener { findNavController().navigate(R.id.navigation2) }
        //1.1 跳转&&传值  在xml中 起始fragment 设置argment 标签
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WelcomFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                WelcomFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

//    companion object {
//        @BindingAdapter("android:onClick")
//     const   fun onClick(button: AppCompatButton) {
//            when (button.id) {
////                R.id.signIn -> findNavController().navigate()
////                R.id.signUp -> findNavController().navigate()
//            }
//        }
//    }

}