package com.example.speedmarket.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.speedmarket.R
import com.example.speedmarket.ui.AppActivity

fun View.hide(){
    visibility = View.GONE
}

fun View.show(){
    visibility = View.VISIBLE
}

fun View.disable(){
    isEnabled = false
}

fun View.enabled(){
    isEnabled = true
}

fun Fragment.toast(msg: String?){
    Toast.makeText(requireContext(),msg,Toast.LENGTH_LONG).show()
}


fun Fragment.createDialog(): Dialog {
        val dialog = Dialog(requireContext(), android.R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog)
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        return dialog
    }

fun Fragment.setupOnBackPressedExit() {
    val callback=object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            val dialog = createDialog()
            dialog.show()
            val button = dialog.findViewById<Button>(R.id.btn_esci)
            button.setOnClickListener{
                System.exit(-1)
            }
            val imageButton = dialog.findViewById<ImageButton>(R.id.image_close)
            imageButton.setOnClickListener{
                dialog.dismiss()
            }
        }

    }
    requireActivity().onBackPressedDispatcher.addCallback(callback)
}
fun Fragment.dialog(fragment: Fragment) {

    val dialog = createDialog()
    dialog.show()
    val button = dialog.findViewById<Button>(R.id.btn_esci)
    button.setText("Sfoglia")
    dialog.findViewById<TextView>(R.id.txt_esci).text="Carrello"
    dialog.findViewById<TextView>(R.id.txt_conferma_di_voler_uscire).text="Il tuo carrello è vuoto,se vuoi aggiungere dei prodotti premi il seguente bottone"
    button.setOnClickListener{
        replaceFragment(fragment)
        dialog.dismiss()
    }
    val imageButton = dialog.findViewById<ImageButton>(R.id.image_close)
    imageButton.setOnClickListener{
        dialog.dismiss()
    }
}

fun Fragment.setupOnBackPressed(){
    val callback=object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            startActivity(Intent(requireContext(), AppActivity::class.java))
        }

    }
    requireActivity().onBackPressedDispatcher.addCallback(callback)
}

fun Fragment.replaceFragment(fragment: Fragment){
    val transaction = fragmentManager?.beginTransaction()
    transaction?.replace(R.id.frame_layout, fragment)
    transaction?.commit()
}


val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.pxToDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun String.isValidEmail() =
    isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

