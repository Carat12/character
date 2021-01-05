package com.example.character.ui.character

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.character.R
import com.example.character.app.Config
import com.example.character.data.model.Character
import com.example.character.databinding.FragmentLocationBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_location.view.*

class LocationFragment : DialogFragment() {
    private lateinit var character: Character
    private lateinit var mBinding: FragmentLocationBinding
    private lateinit var fragmentView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            character = it.getSerializable(Config.CONTENT_CHARACTER) as Character
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_location, container, false)
        mBinding.character = character
        fragmentView = mBinding.root

        Picasso
            .get()
            .load(character.image)
            .placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(fragmentView.img_view)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return fragmentView
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setGravity(Gravity.CENTER)
        dialog?.window?.setLayout(900, WindowManager.LayoutParams.WRAP_CONTENT)

    }

    companion object {
        const val TAG = "show location dialog"

        @JvmStatic
        fun newInstance(character: Character) =
            LocationFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(Config.CONTENT_CHARACTER, character)
                }
            }
    }
}