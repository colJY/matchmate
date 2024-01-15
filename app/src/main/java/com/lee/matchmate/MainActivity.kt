package com.lee.matchmate

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.lee.matchmate.chat.detail.ChatDetailFragmentDirections
import com.lee.matchmate.common.AppGlobalContext
import com.lee.matchmate.common.Constants
import com.lee.matchmate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var navController: NavController

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToChatFragmentIfNeeded(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.bottom_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        binding.bottomNavigationView.setupWithNavController(navController)
        val isLoggedIn = AppGlobalContext.prefs.getBoolean(Constants.IS_LOGGED_IN, false)

        setupJetpackNavigation(isLoggedIn)
        navigateToChatFragmentIfNeeded(intent)
    }

    private fun navigateToChatFragmentIfNeeded(intent: Intent?) {
        intent?.getStringExtra(Constants.ROOM_ID)?.let { roomId ->
            val action = ChatDetailFragmentDirections.actionGlobalChatDetailFragment(roomId)
            navController.navigate(action)

        }
    }

    private fun setupJetpackNavigation(autoLogin: Boolean) {
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.bottom_nav_host_fragment) as NavHostFragment? ?: return
        navController = host.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        val navGraph = navController.navInflater.inflate(R.navigation.bottom_nav_graph)
        if (autoLogin) navGraph.setStartDestination(R.id.main_fragment)
        else navGraph.setStartDestination(R.id.login_fragment)
        navController.setGraph(navGraph, null)
        //네비게이션 바가 보이는 곳과 보이지 않을 곳 설정
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (
                destination == navController.findDestination(R.id.main_fragment) ||
                destination == navController.findDestination(R.id.chat_fragment) ||
                destination == navController.findDestination(R.id.favorite_fragment) ||
                destination == navController.findDestination(R.id.setting_fragment)
            ) {
                binding.bottomNavigationView.visibility = View.VISIBLE
            } else binding.bottomNavigationView.visibility = View.GONE
        }
    }


}

