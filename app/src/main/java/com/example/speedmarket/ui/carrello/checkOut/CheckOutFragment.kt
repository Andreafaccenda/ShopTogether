package com.example.speedmarket.ui.carrello.checkOut

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentCheckOutBinding
import com.example.speedmarket.model.Carrello
import com.example.speedmarket.ui.carrello.CarrelloFragment
import com.example.speedmarket.util.setupOnBackPressedFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckOutFragment : Fragment() {

    lateinit var binding: FragmentCheckOutBinding
    lateinit var adapter: FragmentPageAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCheckOutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedFragment(CarrelloFragment(),CheckOutFragment())
        val adapter=FragmentPageAdapter(requireActivity().supportFragmentManager,lifecycle)
        binding.viewPager2.adapter=adapter
        binding.viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position == 2){
                    adapter.notifyItemChanged(position)
                }
            }
        })
        TabLayoutMediator(binding.tabLayout,binding.viewPager2){tab,position->
            when(position){
                0->tab.text="Spedizione"
                1->tab.text="Pagamento"
                2->tab.text="Riepilogo ordine"
            }
        }.attach()
       for(i in 0..3){
            val textView=LayoutInflater.from(requireContext()).inflate(R.layout.tab_title,null) as TextView

            binding.tabLayout.getTabAt(i)?.customView=textView
        }

    }


}