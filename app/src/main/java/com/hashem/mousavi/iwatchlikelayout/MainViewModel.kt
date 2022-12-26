package com.hashem.mousavi.iwatchlikelayout

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class MainViewModel(private val app: Application) : AndroidViewModel(app) {

    private val _state = mutableStateOf<List<MyData>>(emptyList())
    val state: State<List<MyData>> = _state

    fun getApplications(){
        viewModelScope.launch {
           val pm = app.packageManager
            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
            val list = apps.filter { it.icon != 0 }.map {  MyData(it.loadIcon(pm)) }
            _state.value = list
        }
    }

}