package com.yjiang45.project3.ui.main

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso

import com.yjiang45.project3.R
import kotlinx.android.synthetic.main.add_item_fragment.*
import kotlinx.android.synthetic.main.detail_fragment.*
import java.io.File

class detail : Fragment() {

    private val picasso = Picasso.get()

    companion object {
        fun newInstance() = detail()
    }

    //private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        detail_text.text = MainFragment.itemPassed.name

        if(File(context?.filesDir, MainFragment.itemPassed.photoFileName).exists()){
            picasso.load(File(context?.filesDir, MainFragment.itemPassed.photoFileName))
                .resize(500,500)
                .into(detail_Pic)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        //menu.removeItem(R.id.delete_All)
        menu.clear()
    }

}
