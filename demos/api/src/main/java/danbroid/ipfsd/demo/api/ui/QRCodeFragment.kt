package danbroid.ipfsd.demo.api.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import danbroid.ipfsd.demo.api.DemoNavGraph
import danbroid.ipfsd.demo.api.R
import danbroid.ipfsd.demo.api.databinding.FragmentQrcodeBinding


class QRCodeFragment : Fragment(R.layout.fragment_qrcode) {

  private var _binding: FragmentQrcodeBinding? = null
  private val binding: FragmentQrcodeBinding
    inline get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ) = FragmentQrcodeBinding.inflate(inflater, container, false).let {
    _binding = it
    it.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  val data: String by lazy {
    requireArguments().getString(DemoNavGraph.args.data)!!
  }


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val writer = QRCodeWriter()
    try {
      val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
      val width = bitMatrix.width
      val height = bitMatrix.height
      val bmp: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
      for (x in 0 until width) {
        for (y in 0 until height) {
          bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
        }
      }
      binding.barcode.setImageBitmap(bmp)
      //  (findViewById(R.id.img_result_qr) as ImageView).setImageBitmap(bmp)
    } catch (e: WriterException) {
      log.error(e.message, e)
    }
  }
}

private val log = danbroid.logging.getLog(QRCodeFragment::class)
