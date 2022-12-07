package com.example.speedmarket.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.speedmarket.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


open class UtilsFragment : Fragment() {

    lateinit var mprogressDialog : Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    fun showErrorSnackbar(v:View,message :String,errorMessage : Boolean){
        if(errorMessage){
            Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .setBackgroundTint(resources.getColor(R.color.colorSnackBarError)).show()}
        else
        {
            Snackbar.make(v, message
                , Snackbar.LENGTH_LONG)
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .setBackgroundTint(resources.getColor(R.color.colorSnackBarSuccess)).show()
        }
    }
   fun showProgressDialog(){

        mprogressDialog = Dialog(requireContext())
        mprogressDialog.setContentView(R.layout.dialog_progess)
        //mprogressDialog.tv_progress_text.text = text
        mprogressDialog.setCancelable(false)
        mprogressDialog.setCanceledOnTouchOutside(false)
        mprogressDialog.show()


    }
    fun hideProgressDialog(){
        mprogressDialog.dismiss()
    }

    fun showAlertDialog(){
        val alert =  AlertDialog.Builder(requireContext())
        alert.setTitle("Shop App")
            .setMessage("Are you logged?")
            .setCancelable(true)
            .setPositiveButton("Yes",null)
            .setNegativeButton("No",null)

    }



}