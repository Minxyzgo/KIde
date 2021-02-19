package cn.minxyzgo.kide.ui.gallery

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import cn.minxyzgo.kide.databinding.ActivityMainBinding
import cn.minxyzgo.kide.R


class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)
//////        //val textView: TextView = root.findViewById(R.id.text_gallery)
//////        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
//////            textView.text = it
//////        })

        return root
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        val codeModel = CodeModel("<html>111111111111111111", "html")
//        val mViewDataBinding: ActivityMainBinding =
//            DataBindingUtil.setContentView(this, R.layout.activity_main)
//        mViewDataBinding.setVariable(com.android.databinding.library.baseAdapters.BR.viewModel, codeModel)
//        mViewDataBinding.setLifecycleOwner(this)
//        mViewDataBinding.editor.setOnTextChange({ str ->
//            Toast.makeText(
//                this,
//                mViewDataBinding.editor.getText(),
//                Toast.LENGTH_LONG
//            ).show()
//        })
//    }
}