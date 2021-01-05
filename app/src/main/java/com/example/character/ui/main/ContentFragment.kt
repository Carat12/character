package com.example.character.ui.main

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.character.R
import com.example.character.app.Config
import com.example.character.data.model.Character
import com.example.character.data.model.Episode
import com.example.character.databinding.FragmentContentBinding
import com.example.character.helper.MyViewModelFactory
import com.example.character.helper.toast
import com.example.character.ui.AdapterListener
import com.example.character.ui.MyListener
import com.example.character.ui.MyViewModel
import com.example.character.ui.character.CharacterAdapter
import com.example.character.ui.character.LocationFragment
import com.example.character.ui.episode.EpisodeAdapter
import com.example.character.ui.episode.EpisodeDetailActivity
import kotlinx.android.synthetic.main.fragment_content.*
import kotlinx.android.synthetic.main.fragment_content.view.*
import kotlin.collections.ArrayList

class ContentFragment : Fragment(), AdapterListener, MyListener, View.OnClickListener {

    private lateinit var content: String
    private lateinit var mBinding: FragmentContentBinding
    private lateinit var fragmentView: View
    private lateinit var viewModel: MyViewModel
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var episodeAdapter: EpisodeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            content = it.getString(Config.CONTENT_KEY) as String
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_content, container, false)
        mBinding.lifecycleOwner = this
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(activity!!.application)
        ).get(MyViewModel::class.java)
        mBinding.viewModel = viewModel
        fragmentView = mBinding.root

        init()

        return fragmentView
    }

    private fun init() {
        //progress bar
        fragmentView.progress_bar.visibility = View.VISIBLE
        fragmentView.recycler_view.layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                LinearLayoutManager(activity)
            else GridLayoutManager(activity, 2)

        if (content == Config.CONTENT_CHARACTER) {
            characterAdapter = CharacterAdapter(activity!!)
            characterAdapter.setListener(this)
            fragmentView.recycler_view.adapter = characterAdapter

            //observe character list
            initialCharacter()
            viewModel.characterList.observe(this, object : Observer<ArrayList<Character>> {
                override fun onChanged(t: ArrayList<Character>?) {
                    //progress bar
                    updateSettings()
                    if (t != null) {
                        characterAdapter.setData(t)
                        fragmentView.btn_next.isEnabled = viewModel.hasNextPageCharacter()
                        //cache data
                        viewModel.cacheCharacterByPage()
                    } else {
                        onFailure(viewModel.getGetCharacterErrorMsg())
                        //try cache
                        viewModel.getCachedCharacterByPage()
                    }
                }
            })

            //observe cached character list
            viewModel.cachedCharacterList.observe(this, { t ->
                characterAdapter.setData(t)
                //progress bar
                updateSettings()
                if(t.isEmpty())
                    onFailure(Config.NETWORK_OFFLINE_MESSAGE + " Please retry later.")
            })
        } else {
            episodeAdapter = EpisodeAdapter(activity!!)
            episodeAdapter.setAdapterListener(this)
            fragmentView.recycler_view.adapter = episodeAdapter

            //observe episode list
            initialEpisode()
            viewModel.episodeList.observe(this, object : Observer<ArrayList<Episode>> {
                override fun onChanged(t: ArrayList<Episode>?) {
                    //progress bar
                    updateSettings()
                    if (t != null) {
                        episodeAdapter.setData(t)
                        fragmentView.btn_next.isEnabled = viewModel.hasNextPageEpisode()
                        //cache
                        viewModel.cacheEpisodeByPage()
                    } else {
                        onFailure(viewModel.getGetEpisodeErrorMsg())
                        //try cache
                        viewModel.getCachedEpisodeByPage()
                    }
                }
            })

            viewModel.cachedEpisodeList.observe(this, {
                episodeAdapter.setData(it)
                //progress bar
                updateSettings()
                if(it.isEmpty())
                    onFailure(Config.NETWORK_OFFLINE_MESSAGE +  " Please retry later.")
            })
        }

        //observe current page
        viewModel.currentPage.observe(this, {
            Log.d("woozi", "observe page")
            fragmentView.btn_prev.isEnabled = it != "1"
            //fragmentView.btn_next.isEnabled = it != if(Content)
        })

        //buttons
        fragmentView.btn_prev.setOnClickListener(this)
        fragmentView.btn_next.setOnClickListener(this)
    }

    private fun initialCharacter() {
        if (viewModel.characterList.value.isNullOrEmpty() && viewModel.cachedCharacterList.value.isNullOrEmpty())
            viewModel.getCharacter(hasNetworkConnection())
    }

    private fun initialEpisode() {
        if (viewModel.episodeList.value.isNullOrEmpty() && viewModel.cachedEpisodeList.value.isNullOrEmpty())
            viewModel.getEpisode(hasNetworkConnection())
    }

    fun updateSettings(){
        //progress bar
        fragmentView.progress_bar.visibility = View.GONE
        fragmentView.nested_scroll_view.fullScroll(View.FOCUS_UP)
    }

    override fun onClick(v: View?) {
        when (v) {
            btn_prev -> viewModel.prevPage()
            btn_next -> viewModel.nextPage()
        }
        onPageChanged()
    }

    private fun onPageChanged() {
        //progress bar
        fragmentView.progress_bar.visibility = View.VISIBLE

        if (content == Config.CONTENT_CHARACTER) {
            Log.d("woozi", "get char onPageChanged")
            viewModel.getCharacter(hasNetworkConnection())
        } else {
            Log.d("woozi", "get epi onPageChanged")
            viewModel.getEpisode(hasNetworkConnection())
        }
    }

    override fun onItemClicked(view: View, position: Int) {
        when (content) {
            Config.CONTENT_CHARACTER -> {
                val locationFragment =
                    LocationFragment.newInstance(characterAdapter.getItemData(position))
                locationFragment.show(childFragmentManager, LocationFragment.TAG)
            }
            Config.CONTENT_EPISODE -> {
                val intent = Intent(activity, EpisodeDetailActivity::class.java)
                intent.putExtra(Config.CONTENT_EPISODE, episodeAdapter.getItemData(position))
                startActivity(intent)
            }
        }
    }

    override fun onSuccess() {

    }

    override fun onFailure(msg: String) {
        toast(msg)
    }

    companion object {
        @JvmStatic
        fun newInstance(content: String) =
            ContentFragment().apply {
                arguments = Bundle().apply {
                    putString(Config.CONTENT_KEY, content)
                }
            }
    }

    private fun hasNetworkConnection(): Boolean {
        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected = cm.activeNetworkInfo != null
        if (!isConnected) toast(Config.NETWORK_OFFLINE_MESSAGE)
        return isConnected
    }
}
