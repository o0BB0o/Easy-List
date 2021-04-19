package com.yjiang45.project3.ui.main

import android.app.AlertDialog
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yjiang45.project3.R
import com.yjiang45.project3.database.Items
import kotlinx.android.synthetic.main.card_item.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.android.synthetic.main.card_item.*
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment.*
import androidx.preference.PreferenceManager
import com.yjiang45.project3.MainActivity.Companion.DELETE_ALL
import com.yjiang45.project3.MainActivity.Companion.SHOW_UNCHECKED

class MainFragment : Fragment(), addItem.AddItemListener {

    private val prefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }

    companion object {
        fun newInstance() = MainFragment()
        var itemPassed = Items()
    }

    override fun onAddItem(items: Items) {
        viewModel.insert(items)
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var itemsRecycler: RecyclerView
    private lateinit var adapter: ItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
        adapter=ItemsAdapter()
        itemsRecycler = view.findViewById(R.id.items_view)
        itemsRecycler.layoutManager = LinearLayoutManager(context)
        itemsRecycler.adapter = adapter

        applyPreference()

        viewModel.items.observe(viewLifecycleOwner, Observer {
            adapter.updateWords(it)
        })

        val helperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val thisItem = adapter.getWordAtPosition(viewHolder.adapterPosition)
                viewModel.deleteWord(items = thisItem)
                itemDeletedAlert(thisItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(helperCallback)
        itemTouchHelper.attachToRecyclerView(itemsRecycler)
        floatingActionButton.setOnClickListener{
            val dialog = addItem.newInstance(this)
            dialog.show(childFragmentManager,"dialog")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    fun itemDeletedAlert(items: Items) {
        val msg = resources.getString(R.string.deleted_alert, items.name)
        val builder = AlertDialog.Builder(context)
        with(builder) {
            setTitle(R.string.alert)
            setMessage(msg)
            setPositiveButton(R.string.ok, null)
            show()
        }
    }

    private inner class ItemsViewHolder(view: View) : RecyclerView.ViewHolder(view),View.OnClickListener {
        private lateinit var items: Items
        private val itemTextView: TextView = itemView.item_textView
        private val categoryTextView: TextView = itemView.category
        private val checkBox: CheckBox = itemView.checkBox

        init {
            itemView.setOnClickListener(this)
            checkBox.setOnCheckedChangeListener { _ , isChecked ->
                items.checked = isChecked
                viewModel.updateItem(items)
            }
        }

        override fun onClick(v: View?) {
            itemPassed = items
            navHostFragment.findNavController().navigate(R.id.action_mainFragment_to_detail)
        }

        fun bind(items: Items) {
            this.items = items
            itemTextView.text = items.name
            categoryTextView.text = items.category
            checkBox.isChecked = items.checked
        }
    }

    private inner class ItemsAdapter : RecyclerView.Adapter<ItemsViewHolder>() {

        var items: List<Items> = emptyList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
            val view = layoutInflater.inflate(R.layout.card_item, parent, false)
            return ItemsViewHolder(view)
        }

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
            holder.bind(items[position])
        }

        fun updateWords(newItems: List<Items>) {
            this.items = newItems
            notifyDataSetChanged()
        }

        fun getWordAtPosition(position: Int): Items {
            return items[position]
        }
    }

    private fun applyPreference(){
        if (prefs.getBoolean(SHOW_UNCHECKED,false)){
            viewModel.unCheckedItems.observe(viewLifecycleOwner, Observer {
                adapter.updateWords(it)
            })
        }else {
            viewModel.items.observe(viewLifecycleOwner, Observer {
                adapter.updateWords(it)
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (prefs.getBoolean(DELETE_ALL,false)){
            menu.removeItem(R.id.delete_All)
        }
        else{
        }
    }
}
