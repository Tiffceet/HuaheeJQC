package com.example.huaheejqc.orderStatus

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.huaheejqc.sallerBookManagement.CompletedOrder
import com.example.huaheejqc.sallerBookManagement.PendingOrder
import com.example.huaheejqc.sallerBookManagement.PostedItem

class orderStatus_ViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return   when(position){
            0->{
                ToPay()
            }
            1->{
                ToShip()
            }
            2->{
                ToReceive()
            }
            else->{
                Fragment()
            }

        }
    }
}