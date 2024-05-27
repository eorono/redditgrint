package com.example.redditgrint

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.example.redditgrint.databinding.ActionBarBinding
import com.example.redditgrint.databinding.ActivityMainBinding
import com.example.redditgrint.ui.HomeFragment
import com.example.redditgrint.ui.MainViewModel
import com.example.redditgrint.ui.theme.RedditgrintTheme

class MainActivity : AppCompatActivity() {

    companion object {
        var globalDebug = false
        lateinit var jsonAww100: String
        lateinit var subreddit1: String
        private const val mainFragTag = "mainFragTag"
        private const val favoritesFragTag = "favoritesFragTag"
        private const val subredditsFragTag = "subredditsFragTag"
    }

    private var actionBarBinding: ActionBarBinding? = null
    private val viewModel: MainViewModel by viewModels()


    fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.rootView.windowToken, 0)
    }


    private fun initActionBar(actionBar: ActionBar) {
        // Disable the default and enable the custom
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBarBinding = ActionBarBinding.inflate(layoutInflater)
        // Apply the custom view
        actionBar.customView = actionBarBinding?.root
    }

    private fun addHomeFragment() {

        supportFragmentManager.commit {
            add(R.id.main_frame, HomeFragment.newInstance(), mainFragTag)

            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        }
    }


    private fun initTitleObservers(binding: ActivityMainBinding) {

        viewModel.observeTitle().observe(this) {
            actionBarBinding?.actionTitle?.text = it.toString()
        }

        viewModel.observeSearchTerm().observe(this) {
            viewModel.netPostsRefresh()
        }
    }
}