package danbroid.ipfsd.demo.api.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import danbroid.ipfsd.demo.api.R

class TestFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ) = inflater.inflate(R.layout.fragment_test,container,false)

}