package br.com.hermivaldo.demosoap

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.PropertyInfo
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun calcular(view: View){
        val valor01 = etVal01.text.toString()
        val valor02 = etVal02.text.toString()
        val operacao = etOperador.text.toString()


        CallWebService().execute(valor01, valor02, spOperacao.selectedItem.toString())
    }

    inner class CallWebService : AsyncTask<String, Void, String>(){

        private val methodName: String? = "calcular"
        private val nameSpace: String? = "http://heiderlopes.com.br/"
        private val soapAction: String? = nameSpace+methodName
        private val url: String? = "http://10.3.2.42:8080/CalculadoraWSService/CalculadoraWS?wsdl"
        private val parameterNumber1: String? = "num1"
        private val parameterOperation: String? = "op"
        private val parameterNumber2: String? = "num2"

        override fun doInBackground(vararg params: String?): String {
            var result = ""

            val soapObject = SoapObject(nameSpace, methodName)

            val number1Info = PropertyInfo()
            number1Info.name = parameterNumber1
            number1Info.value = params[0]
            number1Info.type = Integer::class.java

            soapObject.addProperty(number1Info)

            val number2Info = PropertyInfo()
            number2Info.name = parameterNumber2
            number2Info.value = params[1]
            number2Info.type = Integer::class.java

            soapObject.addProperty(number2Info)

            val operationInfo = PropertyInfo()
            operationInfo.name = parameterOperation
            operationInfo.value = params[2]
            operationInfo.type = String::class.java

            soapObject.addProperty(operationInfo)

            val envelope = SoapSerializationEnvelope(SoapEnvelope.VER11)
            envelope.setOutputSoapObject(soapObject)

            val httpTransportSE = HttpTransportSE(url)

            try {
                httpTransportSE.call(soapAction, envelope)
                val soapPrimitive = envelope.response
                result = soapPrimitive.toString()
            }catch (e: Exception){
                e.printStackTrace()
            }

            return result
        }

        override fun onPostExecute(result: String?) {
            tvResultado.text = result
        }

    }

}


