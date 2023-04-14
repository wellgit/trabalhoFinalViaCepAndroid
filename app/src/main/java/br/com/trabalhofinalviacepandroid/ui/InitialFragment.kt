package br.com.trabalhofinalviacepandroid.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.trabalhofinalviacepandroid.R
import br.com.trabalhofinalviacepandroid.databinding.FragmentInitialBinding
import br.com.trabalhofinalviacepandroid.viewmodel.InitialViewModel

class InitialFragment : Fragment() {

    private var _binding: FragmentInitialBinding? = null
    private val binding : FragmentInitialBinding get() = _binding!!
    private val viewModel: InitialViewModel by activityViewModels()
    private lateinit var messageAlert : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInitialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSearchButtonPressed()
    }

    private fun onSearchButtonPressed(){
        _binding?.btnFindCep?.setOnClickListener {
            // Valida se o CEP eh valido
            if (isValidCep()){
                try {
                    viewModel.getCep(binding.inputCep.text.toString())
                    viewModel.resposta.observe(viewLifecycleOwner, Observer {
                        if (it !=null){
                            //navegação da primeira tela pra segunda tela
                            goToNextScreen()
                        }
                    })
                }catch (e: Exception){
                    Log.i("ERRO_CONSULTA_CEP", e.printStackTrace().toString())
//                    clearAll()
//                    _binding?.txtMessage?.text = viewModel.txtMessage.value
                    messageAlert = viewModel.txtMessage.value.toString()
                    showMessage(messageAlert)
                }
            }else{
                _binding?.txtMessage?.text = "INFORME UM VALOR PARA A BUSCA OU REVISE O ITEM DIGITADO"
//                messageAlert = "INFORME UM VALOR PARA A BUSCA OU REVISE O ITEM DIGITADO"
//                showMessage(messageAlert)

            }
            clearAll()
        }
    }

    private fun isValidCep(): Boolean {
        val inputCep = _binding?.inputCep?.text.toString()
        return inputCep !=null && !inputCep.isBlank() && inputCep!!.length == 8
    }

    private fun clearAll(){
        _binding?.inputCep?.text?.clear()
    }

    private fun goToNextScreen(){
        findNavController().navigate(R.id.action_initialFragment_to_detailFragment)
    }

    private fun showMessage (message:String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}