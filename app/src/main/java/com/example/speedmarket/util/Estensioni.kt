package com.example.speedmarket.util

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import coil.load
import com.example.speedmarket.R
import com.example.speedmarket.databinding.ActivityAppBinding.bind
import com.example.speedmarket.databinding.ActivityAppBinding.inflate
import com.example.speedmarket.databinding.BottomSheetDailogBinding
import com.example.speedmarket.model.Prodotto
import com.example.speedmarket.ui.AppActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.system.exitProcess


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
fun Fragment.setupOnBackPressedExit(fragmentToremove : Fragment) {
    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val dialog = createDialog()
            dialog.show()
            val button = dialog.findViewById<Button>(R.id.btn_esci)
            button.setOnClickListener {
                requireActivity().finish()
                removeFragment(fragmentToremove)
                exitProcess(0)
            }
            val imageButton = dialog.findViewById<ImageButton>(R.id.image_close)
            imageButton.setOnClickListener {
                dialog.dismiss()
            }
        }

    }
    requireActivity().onBackPressedDispatcher.addCallback(callback)
}
fun Activity.dialog(): Dialog {
    val dialog = Dialog(this, android.R.style.Theme_Dialog)
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
fun Fragment.bottomSheetDialog(prodotto:Prodotto):BottomSheetDialog{
    val bottomSheetDialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialogTheme)
    bottomSheetDialog.setContentView(R.layout.bottom_sheet_dailog)
    bottomSheetDialog.findViewById<ImageView>(R.id.image_prodotto)
        ?.let { bindImage(it,prodotto.immagine) }
    bottomSheetDialog.findViewById<TextView>(R.id.descrizione_prodotto)?.text = "Descrizione:${prodotto.descrizione}"
    bottomSheetDialog.findViewById<TextView>(R.id.scadenza_prodotto)?.text = "Data di scadenza: ${prodotto.data_scadenza}"
    bottomSheetDialog.findViewById<TextView>(R.id.prezzo)?.text = "€${calcolaPrezzo(prodotto.prezzo_unitario, prodotto.quantita, prodotto.offerta!!, prodotto.unita_ordinate)}"
    bottomSheetDialog.findViewById<TextView>(R.id.categoria_prodotto)?.text="Categoria:${prodotto.categoria}"
    bottomSheetDialog.findViewById<TextView>(R.id.produttore_prodotto)?.text="Marchio:${prodotto.produttore}"
    bottomSheetDialog.findViewById<TextView>(R.id.txt_quantità_prodotto)?.text= prodotto.unita_ordinate.toString()
    return bottomSheetDialog

}
fun Fragment.bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri)
    }
}
fun Fragment.calcolaPrezzo(prezzo_unitario:Float, quantita:Float, offerta:Float,unita_ordinate:Int): String {
    val dec = DecimalFormat("#.##")
    return if(offerta < 1) {
        dec.roundingMode = RoundingMode.DOWN
        val prezzo = dec.format(prezzo_unitario * quantita * offerta*unita_ordinate)
        prezzo

    }else{
        dec.roundingMode = RoundingMode.DOWN
        val prezzo = dec.format(prezzo_unitario * quantita*unita_ordinate)
        prezzo
    }

}
fun Fragment.removeFragment(fragment: Fragment){
    val transaction = fragmentManager?.beginTransaction()
    transaction?.remove(fragment)
    transaction?.commitAllowingStateLoss()
}
fun Fragment.dialog(fragment: Fragment,str1:String,str2:String,str3:String) {

    val dialog = createDialog()
    dialog.show()
    val button = dialog.findViewById<Button>(R.id.btn_esci)
    button.setText(str3)
    dialog.findViewById<TextView>(R.id.txt_esci).text=str1
    dialog.findViewById<TextView>(R.id.txt_conferma_di_voler_uscire).text=str2
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
fun Fragment.setupOnBackPressedFragment(fragment:Fragment,fragmentToremove: Fragment){
    val callback=object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            replaceFragment(fragment)
            removeFragment(fragmentToremove)
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

