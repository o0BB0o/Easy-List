package com.yjiang45.project3.ui.main

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso

import com.yjiang45.project3.R
import com.yjiang45.project3.database.Items
import kotlinx.android.synthetic.main.add_item_fragment.*
import java.io.File
import java.util.*

class addItem : BottomSheetDialogFragment(), AdapterView.OnItemSelectedListener {

    companion object {
        fun newInstance(listener: AddItemListener) = addItem().apply {
            this.listener=listener
        }
        private const val REQUEST_PHOTO = 0
    }

    var selected = "?"
    private var listener: AddItemListener? = null
    private var newItem: Items = Items()
    private val vm: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private var uuid = UUID.randomUUID()//item id
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri
    private val picasso = Picasso.get()//call picasso to get photo

    interface AddItemListener {
        fun onAddItem(items: Items)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_item_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        photoFile = File(context?.applicationContext?.filesDir, "IMG_$uuid.jpg")
        photoUri = FileProvider.getUriForFile(
            requireActivity(),
            "com.yjiang45.project3.fileprovider",
            photoFile
        )
        //viewModel = ViewModelProviders.of(this).get(AddItemViewModel::class.java)
        spinner.onItemSelectedListener=this
        add_btn.setOnClickListener {
            onDone()
            dismiss()
        }
        //open up the camera and try to capture a photo and store it
        imageButton.apply {
            val pm = requireActivity().packageManager
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolveActivity =
                pm.resolveActivity(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
            if (resolveActivity == null || !cameraPermission()) {//check permission of the phone
                isEnabled = false
            }
            setOnClickListener {
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                val cameraActivities =
                    pm.queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
                for (cameraActivity in cameraActivities) {
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        photoUri,
                        Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
                    )
                    startActivityForResult(captureImage, REQUEST_PHOTO)
                }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent) {//select each iem
            spinner -> selected = parent?.getItemAtPosition(position).toString()
        }

    }
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun onDone() {//finish creating the item
        val itemName = edit_item.text.toString()
        newItem.name = itemName
        newItem.category = selected
        vm.insert(newItem)
        newItem.uuid = uuid
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {//request photot from the system
            resultCode != Activity.RESULT_OK -> return
            requestCode == REQUEST_PHOTO -> {
                requireActivity().revokeUriPermission(
                    photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )

                if (photoFile.exists()) {
                    picasso.load(photoUri)
                        .fit()
                        .centerCrop()
                        .into(imageButton)
                }
            }
        }
    }

    private fun cameraPermission(): Boolean {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        )
        return permission == PackageManager.PERMISSION_GRANTED
    }
}
