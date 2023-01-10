package com.example.speedmarket.ui.carrello.checkOut

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentPageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager,lifecycle){
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
       return when(position){
           0->SpedizioneFragment()
           1-> PagamentoFragment()
           2->RiepilogoFragment()
           else->CheckOutFragment()
       }
    }
}