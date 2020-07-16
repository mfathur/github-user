package com.mfathurz.githubuser.ui.detail

import android.content.Context
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mfathurz.githubuser.R
import com.mfathurz.githubuser.ui.detail.follower.FollowerFragment
import com.mfathurz.githubuser.ui.detail.following.FollowingFragment

class FollowPagerAdapter (private val mContext : Context, fm : FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabTitle = intArrayOf(R.string.follower, R.string.following)

    private var bundle :Bundle? = null

    fun setData(bundle: Bundle ){
        this.bundle = bundle
    }

    override fun getItem(position: Int): Fragment {
        var fragment : Fragment? = null
        when(position){
            FOLLOWER_FRAGMENT -> {
                fragment = FollowerFragment()
                fragment.arguments = bundle
            }

            FOLLOWING_FRAGMENT -> {
                fragment = FollowingFragment()
                fragment.arguments = bundle
            }
        }
        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabTitle[position])
    }

    override fun getCount(): Int = 2

    companion object{
        private const val FOLLOWER_FRAGMENT = 0
        private const val FOLLOWING_FRAGMENT = 1
    }

}