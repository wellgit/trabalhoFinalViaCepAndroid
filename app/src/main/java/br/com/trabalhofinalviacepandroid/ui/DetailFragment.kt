package br.com.trabalhofinalviacepandroid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import br.com.trabalhofinalviacepandroid.R
import br.com.trabalhofinalviacepandroid.databinding.FragmentDetailBinding
import br.com.trabalhofinalviacepandroid.databinding.FragmentInitialBinding
import br.com.trabalhofinalviacepandroid.viewmodel.InitialViewModel

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding : FragmentDetailBinding get() = _binding!!
    private val viewModel: InitialViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater,container,false)
        fillFields()
        backScreen()
        return binding.root
    }

    private fun backScreen(){
        _binding?.btnBack?.setOnClickListener{
            clearAll()
            findNavController().navigate(R.id.action_detailFragment_to_initialFragment)
        }
    }

    private fun clearAll(){
        viewModel.txtLogradouro.value=null
        viewModel.txtComplemento.value=null
        viewModel.txtBairro.value=null
        viewModel.txtLocalidade.value=null
        viewModel.txtUF.value=null
        viewModel.txtMessage.value=null
    }

    private fun fillFields(){
        _binding?.txtLogradouro?.text = viewModel.txtLogradouro.value
        _binding?.txtLocalidade?.text = viewModel.txtLocalidade.value
        _binding?.txtUF?.text = viewModel.txtUF.value
        _binding?.txtBairro?.text = viewModel.txtBairro.value
        _binding?.txtComplmento?.text = viewModel.txtComplemento.value
    }

}