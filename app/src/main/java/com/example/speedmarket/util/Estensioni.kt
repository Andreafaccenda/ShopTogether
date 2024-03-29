package com.example.speedmarket.util

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import android.view.Gravity
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
import com.example.speedmarket.model.Prodotto
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_app.*
import java.math.RoundingMode
import java.text.DecimalFormat


fun View.hide(){
    visibility = View.GONE
}

fun View.show(){
    visibility = View.VISIBLE
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
fun Fragment.createDialogInternet(): Dialog {
    val dialog = Dialog(requireContext(), android.R.style.Theme_Dialog)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.dialog_internet)
    dialog.window?.setGravity(Gravity.CENTER)
    dialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setCancelable(true)
    return dialog
}
fun Fragment.dialogInternet() {

    val dialog = createDialogInternet()
    dialog.show()
    val continua = dialog.findViewById<Button>(R.id.btn_continua)
    continua.setOnClickListener{
        dialog.dismiss()
    }
    val imageButton = dialog.findViewById<ImageButton>(R.id.image_close)
    imageButton.setOnClickListener{
        dialog.dismiss()
    }
    val impostazioni = dialog.findViewById<Button>(R.id.btn_impostazioni)
    impostazioni.setOnClickListener{
        val myIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        startActivity(myIntent)
        dialog.dismiss()
    }
}
fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return true
        }
    }
    return false
}
fun Fragment.setupOnBackPressedFragmentNav(id:Int){
    val callback=object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            view?.findNavController()?.navigate(id)
        }
    }
    requireActivity().onBackPressedDispatcher.addCallback(callback)
}

fun Fragment.setupOnBackPressedExit() {
    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val dialog = createDialog()
            dialog.show()
            val button = dialog.findViewById<Button>(R.id.btn_esci)
            button.setOnClickListener {
                requireActivity().finish()
                requireActivity().moveTaskToBack(true)
                System.exit(-1)
            }
            val imageButton = dialog.findViewById<ImageButton>(R.id.image_close)
            imageButton.setOnClickListener {
                dialog.dismiss()
            }
        }

    }
    requireActivity().onBackPressedDispatcher.addCallback(callback)
}
fun Fragment.reload(fragment: Fragment){
        val transaction = fragmentManager!!.beginTransaction()
        transaction.replace(R.id.frame_layout,fragment);
        transaction.commit()

}
fun Fragment.bottomSheetDialog(prodotto:Prodotto):BottomSheetDialog{
    val bottomSheetDialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialogTheme)
    bottomSheetDialog.setContentView(R.layout.bottom_sheet_dailog)
    bottomSheetDialog.findViewById<ImageView>(R.id.image_prodotto)
        ?.let { bindImage(it,prodotto.immagine) }
    bottomSheetDialog.findViewById<TextView>(R.id.descrizione_prodotto)?.text = "Descrizione:${prodotto.descrizione}"
    bottomSheetDialog.findViewById<TextView>(R.id.scadenza_prodotto)?.text = "Data di scadenza: ${prodotto.data_scadenza}"
    bottomSheetDialog.findViewById<TextView>(R.id.prezzo)?.text = "€${calcolaPrezzo(prodotto.prezzo_unitario,
        prodotto.quantita,
        prodotto.offerta!!,
        prodotto.unita_ordinate)}"
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
fun calcolaPrezzo(prezzo_unitario: Float, quantita: Float, offerta: Float, unita_ordinate: Int): String {
    val dec = DecimalFormat("##0.00")
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
fun Fragment.dialog(fragment: Fragment,str1:String,str2:String,str3:String) {

    val dialog = createDialog()
    dialog.show()
    val button = dialog.findViewById<Button>(R.id.btn_esci)
    button.text=str3
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
fun Fragment.setupOnBackPressed(id:Int){
    val callback=object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            requireActivity().bottomNavigationView.selectedItemId= id
        }
    }
    requireActivity().onBackPressedDispatcher.addCallback(callback)
}
fun Fragment.setupOnBackPressedFragment(fragment:Fragment){
    val callback=object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            replaceFragment(fragment)
        }
    }
    requireActivity().onBackPressedDispatcher.addCallback(callback)
}

fun Fragment.replaceFragment(fragment: Fragment){
    val transaction = fragmentManager?.beginTransaction()
    transaction?.replace(R.id.frame_layout, fragment)
    transaction?.commit()
}
fun String.isValidEmail() =
    isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

