package com.example.huaheejqc.sellerBookManagement

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class sallerBookManager_ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return   when(position){
            0->{
                PostedItem()
            }
            1->{
                PendingOrder()
            }
            2->{
                InTransit()
            }
            3->{
                CompletedOrder()
            }
            else->{
                Fragment()
            }

        }
    }
}