package com.example.mlbarcodeapp

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class MainActivity : AppCompatActivity() {

    lateinit var cameraButton: ImageView
    lateinit var resultText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraButton= findViewById(R.id.imageCamera)
        resultText= findViewById(R.id.textResult)

        cameraButton.setOnClickListener {
            // open up the camera and store the image for further ML Processing
            var intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if(intent.resolveActivity(packageManager) != null){
                // image/barcode captured and now proceed for next steps
                startActivityForResult(intent, 123)
            }else{
                Toast.makeText(this, "Oops something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==123 && resultCode== RESULT_OK){
            val extras= data?.extras
            val bitmap= extras?.get("data") as Bitmap
            extraInfoUsingML(bitmap)
        }
    }

    private fun extraInfoUsingML(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val scanner = BarcodeScanning.getClient()

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                // Task completed successfully
                // ...
                var answerString: String= ""
                for (barcode in barcodes) {

                    val rawValue = barcode.rawValue
                    answerString+= rawValue
                }
                resultText.text= answerString
            }
            .addOnFailureListener {
                // Task failed with an exception
                // ...
                Toast.makeText(this, "Oops something went wrong", Toast.LENGTH_SHORT).show()
            }
    }
}