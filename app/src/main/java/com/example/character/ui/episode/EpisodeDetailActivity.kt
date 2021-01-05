package com.example.character.ui.episode

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.character.R
import com.example.character.app.Config
import com.example.character.data.model.Character
import com.example.character.data.model.Episode
import com.example.character.databinding.ActivityEpisodeDetailBinding
import com.example.character.helper.MyViewModelFactory
import com.example.character.helper.toast
import com.example.character.ui.AdapterListener
import com.example.character.ui.MyListener
import com.example.character.ui.MyViewModel
import com.example.character.ui.character.CharacterAdapter
import com.example.character.ui.character.LocationFragment
import kotlinx.android.synthetic.main.activity_episode_detail.*
import kotlinx.android.synthetic.main.tool_bar.*

class EpisodeDetailActivity : AppCompatActivity(), MyListener, AdapterListener {

    private lateinit var mBinding: ActivityEpisodeDetailBinding
    private lateinit var viewModel: MyViewModel
    private lateinit var adapter: CharacterAdapter
    private lateinit var characterList: ArrayList<Character>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_episode_detail)

        init()
    }

    private fun init() {
        //tool bar
        tool_bar.title = Config.CONTENT_EPISODE
        setSupportActionBar(tool_bar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //progress bar
        progress_bar.visibility = View.VISIBLE

        //data binding
        val episode = intent.getSerializableExtra(Config.CONTENT_EPISODE) as Episode
        mBinding.episode = episode

        //recycler view
        adapter = CharacterAdapter(this)
        adapter.setListener(this)
        recycler_view.adapter = adapter
        recycler_view.layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                LinearLayoutManager(this)
            else
                GridLayoutManager(this, 2)

        //view model
        viewModel = ViewModelProvider(this, MyViewModelFactory(application)).get(MyViewModel::class.java)
        getCharacterList(episode)
        viewModel.singleCharacter.observe(this, object : Observer<Character> {
            override fun onChanged(t: Character?) {
                if (t != null) {
                    characterList.add(t)
                    if (characterList.size == (episode.characters.size - errorCount)) {
                        viewModel.characterList.value = characterList
                        viewModel.singleCharacter = MutableLiveData()
                        //cache
                        viewModel.cacheCharacterByEpisode(characterList)
                    }
                } else
                    onFailure(viewModel.getGetSingleCharacterErrorMsg())
            }
        })
        viewModel.characterList.observe(this, {
            adapter.setData(it)
            //progress bar
            progress_bar.visibility = View.GONE
        })
    }

    private fun getCharacterList(episode: Episode) {
        if (viewModel.characterList.value.isNullOrEmpty()) {
            if(hasNetworkConnection()) {
                characterList = ArrayList()
                for (url in episode.characters)
                    viewModel.getSingleCharacter(url)
            }else
                viewModel.getCachedCharacterByEpisode(episode)
        }
    }

    private fun hasNetworkConnection(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected = cm.activeNetworkInfo != null
        if (!isConnected)
            toast(Config.NETWORK_OFFLINE_MESSAGE)
        return isConnected
    }

    override fun onSuccess() {

    }

    private var errorCount = 0
    override fun onFailure(msg: String) {
        toast(msg)
        errorCount++
    }

    override fun onItemClicked(view: View, position: Int) {
        val locationFragment = LocationFragment.newInstance(adapter.getItemData(position))
        locationFragment.show(supportFragmentManager, LocationFragment.TAG)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}